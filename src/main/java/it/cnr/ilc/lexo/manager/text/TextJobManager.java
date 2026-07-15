package it.cnr.ilc.lexo.manager.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.ilc.lexo.manager.text.model.ParsedTextDocument;
import it.cnr.ilc.lexo.manager.text.model.ValidationIssue;
import it.cnr.ilc.lexo.service.data.text.output.TextRecord;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Asynchronous manager dedicated to TXT/CommonMark + optional CoNLL-U -> NIF jobs.
 * It deliberately mirrors LexO-server's existing JobManager without changing it.
 */
public final class TextJobManager {

    private static final TextJobManager INSTANCE = new TextJobManager();

    public static TextJobManager get() {
        return INSTANCE;
    }

    public enum UploadKind {
        TEXT, CONLLU
    }

    public enum TextJobType {
        CONVERT
    }

    public enum TextJobState {
        PENDING, RUNNING, COMPLETED, FAILED, CANCELLED
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TextJobInfo {
        public String fileId;
        public TextJobType type;
        public volatile TextJobState state;
        public volatile int progress;
        public volatile String message;
        public String resultId;
        public List<ValidationIssue> issues;

        public TextJobInfo() {
        }

        public TextJobInfo(String fileId, TextJobType type) {
            this.fileId = fileId;
            this.type = type;
            this.state = TextJobState.PENDING;
            this.progress = 0;
        }
    }

    public static final long DEFAULT_MAX_TEXT_BYTES = 50L * 1024L * 1024L;
    public static final long DEFAULT_MAX_CONLLU_BYTES = 50L * 1024L * 1024L;

    private final ObjectMapper mapper = new ObjectMapper();
    private final ExecutorService ioPool = Executors.newFixedThreadPool(4);
    private final Map<String, UploadSet> uploads = new ConcurrentHashMap<String, UploadSet>();
    private final Map<String, TextRecord> records = new ConcurrentHashMap<String, TextRecord>();
    private final Map<String, TextJobInfo> jobs = new ConcurrentHashMap<String, TextJobInfo>();
    private final Map<String, Future<?>> futures = new ConcurrentHashMap<String, Future<?>>();

    private final Path root;
    private final Path uploadRoot;
    private final Path documentRoot;
    private final Path workRoot;
    private final String publicBaseUri;
    private final String structureNamespace;

    private TextJobManager() {
        root = Paths.get(System.getProperty("lexo.text.storage.dir", "data/texts"));
        uploadRoot = root.resolve("uploads");
        documentRoot = root.resolve("documents");
        workRoot = root.resolve("work");
        publicBaseUri = System.getProperty("lexo.text.publicBaseUri",
                "https://lexo.ilc.cnr.it/resources/texts/");
        structureNamespace = System.getProperty("lexo.text.structureNamespace",
                "https://lexo.ilc.cnr.it/vocabulary/nif-structure#");
        try {
            Files.createDirectories(uploadRoot);
            Files.createDirectories(documentRoot);
            Files.createDirectories(workRoot);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot initialize text storage at " + root, e);
        }
    }

    public Path saveUpload(String fileId, InputStream input, String originalName,
                           UploadKind kind, long maxBytes) throws IOException {
        requireSafeFileId(fileId);
        if (input == null) {
            throw new IOException("Missing upload stream");
        }
        String safeName = sanitizeFileName(originalName);
        Path dir = uploadRoot.resolve(fileId);
        Files.createDirectories(dir);
        Path target = dir.resolve(safeName);
        Path temp = dir.resolve("." + safeName + "." + UUID.randomUUID().toString() + ".part");
        try {
            copyLimited(input, temp, maxBytes);
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temp);
        }

        UploadSet set = uploads.computeIfAbsent(fileId, k -> new UploadSet(fileId, Instant.now().toString()));
        synchronized (set) {
            if (kind == UploadKind.TEXT) {
                if (set.text != null && !set.text.equals(target)) {
                    Files.deleteIfExists(target);
                    throw new IOException("Only one text file is allowed for a text job");
                }
                set.text = target;
                set.textFileName = safeName;
            } else {
                if (set.conllu != null && !set.conllu.equals(target)) {
                    Files.deleteIfExists(target);
                    throw new IOException("Only one CoNLL-U file is allowed for a text job");
                }
                set.conllu = target;
                set.conlluFileName = safeName;
            }
        }
        return target;
    }

    public boolean hasTextUpload(String fileId) {
        UploadSet set = findUploadSet(fileId);
        return set != null && set.text != null && Files.exists(set.text);
    }

    public TextJobInfo startConversion(String fileId) {
        requireSafeFileId(fileId);
        UploadSet upload = findUploadSet(fileId);
        if (upload == null || upload.text == null || !Files.exists(upload.text)) {
            throw new IllegalStateException("No uploaded TXT/Markdown file for " + fileId);
        }
        TextJobInfo current = jobs.get(fileId);
        if (current != null && (current.state == TextJobState.PENDING
                || current.state == TextJobState.RUNNING)) {
            throw new IllegalStateException("A text conversion job is already running for " + fileId);
        }
        if (Files.exists(documentRoot.resolve(fileId))) {
            throw new IllegalStateException("A completed text record already exists for " + fileId);
        }

        TextJobInfo job = new TextJobInfo(fileId, TextJobType.CONVERT);
        jobs.put(fileId, job);
        Future<?> future = ioPool.submit(() -> executeConversion(upload, job));
        futures.put(fileId, future);
        return job;
    }

    private void executeConversion(UploadSet upload, TextJobInfo job) {
        String fileId = upload.fileId;
        Path workDir = workRoot.resolve(fileId + "-" + UUID.randomUUID().toString());
        boolean committed = false;
        try {
            job.state = TextJobState.RUNNING;
            job.progress = 2;
            checkCancelled();

            String rawText = readUtf8Strict(upload.text);
            String rawConllu = upload.conllu == null ? null : readUtf8Strict(upload.conllu);
            job.progress = 15;
            job.message = "Input read and UTF-8 validated";
            checkCancelled();

            ControlledCommonMarkParser parser = new ControlledCommonMarkParser();
            ParsedTextDocument doc;
            if (rawConllu == null) {
                doc = parser.parse(rawText);
            } else {
                doc = parser.parseStructure(rawText);
                new ConlluSegmenter().apply(doc, rawConllu, upload.conlluFileName);
            }
            job.progress = 55;
            job.message = "Document structure and linguistic segmentation validated";
            checkCancelled();

            Files.createDirectories(workDir);
            Path originalDir = workDir.resolve("original");
            Path canonicalPath = workDir.resolve("canonical.txt");
            Path conlluDir = workDir.resolve("conllu");
            Path nifPath = workDir.resolve("document.ttl");
            Path metadataPath = workDir.resolve("metadata.json");
            Files.createDirectories(originalDir);
            Files.copy(upload.text, originalDir.resolve(upload.textFileName),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.write(canonicalPath, doc.cleanText.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            if (upload.conllu != null) {
                Files.createDirectories(conlluDir);
                Files.copy(upload.conllu, conlluDir.resolve(upload.conlluFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            job.progress = 70;
            checkCancelled();

            NifModelWriter writer = new NifModelWriter(publicBaseUri, structureNamespace);
            writer.write(nifPath, fileId, upload.textFileName, doc);
            job.progress = 88;
            job.message = "RDF/NIF model serialized";
            checkCancelled();

            Path finalDir = documentRoot.resolve(fileId);
            TextRecord record = buildRecord(fileId, upload, doc, writer.documentUri(fileId), finalDir);
            mapper.writerWithDefaultPrettyPrinter().writeValue(metadataPath.toFile(), record);
            moveDirectory(workDir, finalDir);
            committed = true;
            records.put(fileId, record);
            uploads.remove(fileId);
            deleteRecursively(uploadRoot.resolve(fileId));

            job.resultId = fileId;
            job.progress = 100;
            job.state = TextJobState.COMPLETED;
            job.message = "Converted to NIF: headings=" + doc.allHeadings.size()
                    + ", paragraphs=" + doc.paragraphs.size()
                    + ", sentences=" + doc.sentences.size()
                    + ", tokens=" + doc.tokens.size();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            job.state = TextJobState.CANCELLED;
            job.message = "Text conversion cancelled";
        } catch (TextValidationException e) {
            job.state = TextJobState.FAILED;
            job.message = e.getMessage();
            job.issues = new ArrayList<ValidationIssue>(e.getIssues());
            uploads.remove(fileId);
            deleteRecursively(uploadRoot.resolve(fileId));
        } catch (Throwable e) {
            job.state = TextJobState.FAILED;
            job.message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
        } finally {
            if (!committed) {
                deleteRecursively(workDir);
            }
        }
    }

    private TextRecord buildRecord(String fileId, UploadSet upload, ParsedTextDocument doc,
                                   String documentUri, Path finalDir) {
        TextRecord record = new TextRecord();
        record.fileId = fileId;
        record.documentUri = documentUri;
        record.segmentationMethod = doc.segmentationMethod;
        record.frontMatterPresent = Boolean.valueOf(doc.frontMatterPresent);
        record.originalFileName = upload.textFileName;
        record.conlluFileName = upload.conlluFileName;
        record.originalPath = relative(finalDir.resolve("original").resolve(upload.textFileName));
        record.canonicalPath = relative(finalDir.resolve("canonical.txt"));
        record.nifPath = relative(finalDir.resolve("document.ttl"));
        record.metadataPath = relative(finalDir.resolve("metadata.json"));
        if (upload.conlluFileName != null) {
            record.conlluPath = relative(finalDir.resolve("conllu").resolve(upload.conlluFileName));
        }
        record.createdAt = upload.createdAt;
        record.completedAt = Instant.now().toString();
        record.headingCount = Integer.valueOf(doc.allHeadings.size());
        record.paragraphCount = Integer.valueOf(doc.paragraphs.size());
        record.sentenceCount = Integer.valueOf(doc.sentences.size());
        record.tokenCount = Integer.valueOf(doc.tokens.size());
        record.metadata.putAll(doc.metadata);
        record.warnings.addAll(doc.warnings);
        return record;
    }

    public Collection<TextJobInfo> getAllJobsFor(String fileId) {
        TextJobInfo job = jobs.get(fileId);
        if (job == null) {
            return Collections.emptyList();
        }
        List<TextJobInfo> out = new ArrayList<TextJobInfo>(1);
        out.add(job);
        return out;
    }

    public TextJobInfo getJob(String fileId) {
        return jobs.get(fileId);
    }

    public boolean cancel(String fileId) {
        Future<?> future = futures.get(fileId);
        if (future == null) {
            return false;
        }
        boolean cancelled = future.cancel(true);
        TextJobInfo job = jobs.get(fileId);
        if (cancelled && job != null) {
            job.state = TextJobState.CANCELLED;
            job.message = "Cancelled by user";
        }
        return cancelled;
    }

    public TextRecord getRecord(String fileId) {
        requireSafeFileId(fileId);
        TextRecord record = records.get(fileId);
        if (record != null) {
            return record;
        }
        Path metadata = documentRoot.resolve(fileId).resolve("metadata.json");
        if (!Files.exists(metadata)) {
            return null;
        }
        try {
            record = mapper.readValue(metadata.toFile(), TextRecord.class);
            records.put(fileId, record);
            return record;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read metadata for " + fileId, e);
        }
    }

    public Path getOriginal(String fileId) {
        TextRecord record = getRecord(fileId);
        return record == null ? null : resolveRelative(record.originalPath);
    }

    public Path getCanonical(String fileId) {
        TextRecord record = getRecord(fileId);
        return record == null ? null : resolveRelative(record.canonicalPath);
    }

    public Path getConllu(String fileId) {
        TextRecord record = getRecord(fileId);
        return record == null || record.conlluPath == null ? null : resolveRelative(record.conlluPath);
    }

    public Path getNif(String fileId) {
        TextRecord record = getRecord(fileId);
        return record == null ? null : resolveRelative(record.nifPath);
    }

    public boolean delete(String fileId) throws IOException {
        requireSafeFileId(fileId);
        cancel(fileId);
        jobs.remove(fileId);
        futures.remove(fileId);
        records.remove(fileId);
        uploads.remove(fileId);
        boolean existed = Files.exists(documentRoot.resolve(fileId))
                || Files.exists(uploadRoot.resolve(fileId));
        deleteRecursively(documentRoot.resolve(fileId));
        deleteRecursively(uploadRoot.resolve(fileId));
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(workRoot, fileId + "-*")) {
            for (Path path : stream) {
                deleteRecursively(path);
            }
        }
        return existed;
    }

    public void cleanupUpload(String fileId) {
        uploads.remove(fileId);
        deleteRecursively(uploadRoot.resolve(fileId));
    }

    public void shutdown() {
        ioPool.shutdownNow();
    }

    public Path getStorageRoot() {
        return root;
    }

    private UploadSet findUploadSet(String fileId) {
        UploadSet cached = uploads.get(fileId);
        if (cached != null) {
            return cached;
        }
        Path dir = uploadRoot.resolve(fileId);
        if (!Files.isDirectory(dir)) {
            return null;
        }
        UploadSet discovered = new UploadSet(fileId, Instant.now().toString());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (!Files.isRegularFile(path) || path.getFileName().toString().startsWith(".")) {
                    continue;
                }
                String name = path.getFileName().toString();
                String lower = name.toLowerCase(Locale.ROOT);
                if (isTextExtension(lower)) {
                    discovered.text = path;
                    discovered.textFileName = name;
                } else if (isConlluExtension(lower)) {
                    discovered.conllu = path;
                    discovered.conlluFileName = name;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot inspect uploads for " + fileId, e);
        }
        if (discovered.text == null && discovered.conllu == null) {
            return null;
        }
        UploadSet previous = uploads.putIfAbsent(fileId, discovered);
        return previous == null ? discovered : previous;
    }

    private static void copyLimited(InputStream input, Path target, long maxBytes) throws IOException {
        long limit = maxBytes > 0 ? maxBytes : Long.MAX_VALUE;
        long written = 0L;
        byte[] buffer = new byte[8192];
        try (java.io.OutputStream out = Files.newOutputStream(target,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            int read;
            while ((read = input.read(buffer)) != -1) {
                written += read;
                if (written > limit) {
                    throw new IOException("File exceeds configured limit of " + limit + " bytes");
                }
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            Files.deleteIfExists(target);
            throw e;
        }
    }

    private static String readUtf8Strict(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        try {
            CharBuffer chars = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes));
            return chars.toString();
        } catch (CharacterCodingException e) {
            throw new IOException("File is not valid UTF-8: " + path.getFileName(), e);
        }
    }

    private static void checkCancelled() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }

    private static void moveDirectory(Path source, Path target) throws IOException {
        Files.createDirectories(target.getParent());
        try {
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(source, target);
        }
    }

    private String relative(Path path) {
        return root.toAbsolutePath().normalize().relativize(path.toAbsolutePath().normalize())
                .toString().replace('\\', '/');
    }

    private Path resolveRelative(String path) {
        if (path == null) {
            return null;
        }
        Path normalizedRoot = root.toAbsolutePath().normalize();
        Path resolved = normalizedRoot.resolve(path).normalize();
        if (!resolved.startsWith(normalizedRoot)) {
            throw new IllegalStateException("Artifact path escapes the configured text storage");
        }
        return resolved;
    }

    private static String sanitizeFileName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing original filename");
        }
        String safe = Paths.get(name).getFileName().toString();
        safe = safe.replaceAll("[\\p{Cntrl}]", "_");
        if (safe.isEmpty() || ".".equals(safe) || "..".equals(safe)) {
            throw new IllegalArgumentException("Invalid filename");
        }
        return safe;
    }

    private static void requireSafeFileId(String fileId) {
        if (fileId == null || !fileId.matches("[A-Za-z0-9._-]+")) {
            throw new IllegalArgumentException("Invalid fileId");
        }
    }

    public static boolean isTextExtension(String lowerName) {
        return lowerName.endsWith(".txt") || lowerName.endsWith(".md")
                || lowerName.endsWith(".markdown");
    }

    public static boolean isConlluExtension(String lowerName) {
        return lowerName.endsWith(".conllu") || lowerName.endsWith(".conll-u")
                || lowerName.endsWith(".conll");
    }

    private static void deleteRecursively(Path root) {
        if (root == null || !Files.exists(root)) {
            return;
        }
        try (java.util.stream.Stream<Path> paths = Files.walk(root)) {
            paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ignored) {
                    try {
                        path.toFile().deleteOnExit();
                    } catch (Throwable alsoIgnored) {
                    }
                }
            });
        } catch (IOException ignored) {
            try {
                root.toFile().deleteOnExit();
            } catch (Throwable alsoIgnored) {
            }
        }
    }

    private static final class UploadSet {
        final String fileId;
        final String createdAt;
        Path text;
        String textFileName;
        Path conllu;
        String conlluFileName;

        UploadSet(String fileId, String createdAt) {
            this.fileId = fileId;
            this.createdAt = createdAt;
        }
    }
}
