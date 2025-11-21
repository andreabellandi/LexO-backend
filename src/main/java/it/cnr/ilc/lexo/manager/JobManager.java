/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.manager.converter.adapter.Converter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.BufferedWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import it.cnr.ilc.lexo.util.ConverterRegistry;
import it.cnr.ilc.lexo.util.RepositoryRegistry;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author andreabellandi
 */
public class JobManager {

    private static final JobManager INSTANCE = new JobManager();

    public static JobManager get() {
        return INSTANCE;
    }

    public enum JobType {
        PARSE, QUERY, CONVERT
    }

    public enum JobState {
        PENDING, RUNNING, COMPLETED, FAILED, CANCELLED
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class JobInfo {

        public String fileId;
        public JobType type;
        public JobState state;
        public int progress; // 0..100
        public String message;
        public String resultId; // for QUERY: queryId; CONVERT: out path

        public JobInfo() {
        }

        public JobInfo(String fileId, JobType type) {
            this.fileId = fileId;
            this.type = type;
            this.state = JobState.PENDING;
            this.progress = 0;
        }
    }

    public enum ResultFormat {
        JSON, CSV, BOTH
    }

    // Default result size limits (can be overridden per request)
    public static final long DEFAULT_MAX_RESULT_BYTES = 25L * 1024 * 1024; // 25 MiB
    public static final int DEFAULT_MAX_RESULT_ROWS = 1_000_000;

    private final ExecutorService ioPool = Executors.newFixedThreadPool(4);
    private final ExecutorService cpuPool = Executors.newFixedThreadPool(4);

//    private final Map<String, Path> uploads = new ConcurrentHashMap<>();
    // fileId -> uploaded files list
    private final Map<String, java.util.List<Path>> uploads = new ConcurrentHashMap<>();
    private final Map<String, Path> converted = new ConcurrentHashMap<>();

    private final Map<String, JobInfo> jobs = new ConcurrentHashMap<>();
    private final Map<String, Future<?>> futures = new ConcurrentHashMap<>();

    // fileId -> queryId -> paths
    private static class QueryPaths {

        Path json;
        Path csv;
    }
    private final Map<String, Map<String, QueryPaths>> queryResults = new ConcurrentHashMap<>();

    private JobManager() {
        try {
            Files.createDirectories(Paths.get("data/uploads"));
            Files.createDirectories(Paths.get("data/converted"));
            Files.createDirectories(Paths.get("data/query"));
        } catch (IOException e) {
        }
    }

    private String key(String fileId, JobType type) {
        return fileId + ":" + type.name();
    }

    // ---------- Upload handling ----------
//    public Path saveUploadEnforcingLimit(String fileId, InputStream in, String origName, long maxBytes) throws IOException {
//        Path out = Paths.get("data/uploads/" + fileId + getExt(origName));
//        long written = 0L;
//        byte[] buf = new byte[8192];
//        int r;
//        try ( OutputStream os = Files.newOutputStream(out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
//            while ((r = in.read(buf)) != -1) {
//                written += r;
//                if (written > maxBytes) {
//                    throw new IOException("File exceeds 50MB limit");
//                }
//                os.write(buf, 0, r);
//            }
//        }
//        uploads.put(fileId, out);
//        return out;
//    }
    public Path saveUploadEnforcingLimit(String fileId, InputStream in, String origName, long maxBytes) throws IOException {
        // nome fisico: fileId-UUID.estensione
        String ext = getExt(origName);
        Path out = Paths.get("data/uploads/" + fileId + "-" + java.util.UUID.randomUUID().toString() + ext);
        long written = 0L;
        byte[] buf = new byte[8192];
        int r;
        try ( OutputStream os = Files.newOutputStream(out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            while ((r = in.read(buf)) != -1) {
                written += r;
                if (written > maxBytes) {
                    throw new IOException("File exceeds 50MB limit");
                }
                os.write(buf, 0, r);
            }
        }
        uploads.computeIfAbsent(fileId, k -> new java.util.concurrent.CopyOnWriteArrayList<Path>())
                .add(out);
        return out;
    }

    private static String getExt(String name) {
        int i = name.lastIndexOf('.');
        return i >= 0 ? name.substring(i) : "";
    }

//    public Path getUpload(String fileId) {
//        return uploads.get(fileId);
//    }
    public java.util.List<Path> getUploads(String fileId) {
        java.util.List<Path> list = uploads.get(fileId);
        return (list != null) ? list : java.util.Collections.<Path>emptyList();
    }

// Compatibilità: primo file (usato da convert, ecc.)
    public Path getUpload(String fileId) {
        java.util.List<Path> list = uploads.get(fileId);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public Path getConverted(String fileId) {
        return converted.get(fileId);
    }

    public JobInfo getJob(String fileId, JobType type) {
        return jobs.get(key(fileId, type));
    }

    public Collection<JobInfo> getAllJobsFor(String fileId) {
        List<JobInfo> out = new ArrayList<>();
        for (JobType t : JobType.values()) {
            JobInfo ji = getJob(fileId, t);
            if (ji != null) {
                out.add(ji);
            }
        }
        return out;
    }

    public JobInfo startParse(String fileId) {
        java.util.List<Path> files = getUploads(fileId);
        if (files == null || files.isEmpty()) {
            throw new IllegalStateException("No uploaded files for " + fileId);
        }
        JobInfo ji = new JobInfo(fileId, JobType.PARSE);
        jobs.put(key(fileId, JobType.PARSE), ji);
        Future<?> f = ioPool.submit(() -> {
            try {
                ji.state = JobState.RUNNING;
                ji.progress = 1;
                // Only a repository for all files
                Repository repo = RepositoryRegistry.create(fileId);
                int totalFiles = files.size();
                int index = 0;
                for (Path file : files) {
                    index++;
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                    if (!Files.exists(file)) {
                        continue;
                    }
                    RDFFormat fmt = Rio.getParserFormatForFileName(file.getFileName().toString())
                            .orElse(RDFFormat.TURTLE);
                    long size = Files.size(file);
                    try ( InputStream in = Files.newInputStream(file);  RepositoryConnection conn = repo.getConnection()) {

                        final int fileIndex = index;
                        ProgressInputStream pin = new ProgressInputStream(in, size, pSingle -> {
                            // progress globale approssimato: parte già completata + porzione del file corrente
                            int base = (int) (((fileIndex - 1) * 100.0) / totalFiles);
                            int step = (int) (pSingle / (double) totalFiles);
                            int global = Math.min(99, base + step);
                            ji.progress = global;
                        });
                        conn.add(pin, "urn:base:", fmt);
                    }
                }
                ji.progress = 100;
                ji.state = JobState.COMPLETED;
                ji.message = "Parsed " + files.size() + " file(s) at " + java.time.Instant.now();
            } catch (InterruptedException ie) {
                ji.state = JobState.CANCELLED;
                ji.message = "Parse cancelled";
            } catch (Throwable t) {
                ji.state = JobState.FAILED;
                ji.message = t.getMessage();
            }
        });
        futures.put(key(fileId, JobType.PARSE), f);
        return ji;
    }

    // ---------- Async: Parse ----------
//    public JobInfo startParse(String fileId) {
//        Path file = getUpload(fileId);
//        if (file == null) {
//            throw new IllegalStateException("No uploaded file for " + fileId);
//        }
//        JobInfo ji = new JobInfo(fileId, JobType.PARSE);
//        jobs.put(key(fileId, JobType.PARSE), ji);
//        Future<?> f = ioPool.submit(() -> {
//            try {
//                ji.state = JobState.RUNNING;
//                ji.progress = 1;
//                Repository repo = RepositoryRegistry.create(fileId);
//                RDFFormat fmt = Rio.getParserFormatForFileName(file.getFileName().toString()).orElse(RDFFormat.TURTLE);
//                long size = Files.size(file);
//                try ( InputStream in = Files.newInputStream(file);  RepositoryConnection conn = repo.getConnection()) {
//                    ProgressInputStream pin = new ProgressInputStream(in, size, p -> {
//                        ji.progress = p;
//                    });
//                    conn.add(pin, "urn:base:", fmt);
//                }
//                ji.progress = 100;
//                ji.state = JobState.COMPLETED;
//                ji.message = "Parsed at " + Instant.now();
//            } catch (Throwable t) {
//                if (Thread.currentThread().isInterrupted()) {
//                    ji.state = JobState.CANCELLED;
//                    ji.message = "Parse cancelled";
//                } else {
//                    ji.state = JobState.FAILED;
//                    ji.message = t.getMessage();
//                }
//            }
//        });
//        futures.put(key(fileId, JobType.PARSE), f);
//        return ji;
//    }
    public JobInfo startGenericConvert(String fileId,
            String from,
            String to,
            Map<String, String> options) {
        Path input = getUpload(fileId);
        if (input == null) {
            throw new IllegalStateException("No uploaded file for " + fileId);
        }

        Converter converter = ConverterRegistry.get().resolve(from, to);
        converter.validateOptions(options);

        JobInfo ji = new JobInfo(fileId, JobType.CONVERT);
        jobs.put(key(fileId, JobType.CONVERT), ji);

        Future<?> f = ioPool.submit(() -> {
            Path out = null;
            try {
                ji.state = JobState.RUNNING;
                ji.progress = 1;

                Path outDir = Paths.get("data/converted");
                Files.createDirectories(outDir);

                // basename + estensione suggerita dal converter
                String base = input.getFileName().toString();
                int dot = base.lastIndexOf('.');
                if (dot > 0) {
                    base = base.substring(0, dot);
                }
                out = outDir.resolve(base + converter.outputExtension(options));

                converter.convert(
                        fileId,
                        input,
                        out,
                        options,
                        pct -> ji.progress = Math.max(1, Math.min(99, pct)),
                        processed -> ji.message = "Processed: " + processed,
                        () -> Thread.currentThread().isInterrupted()
                );

                converted.put(fileId, out);
                ji.resultId = out.toString();
                ji.progress = 100;
                ji.state = JobState.COMPLETED;

            } catch (InterruptedException ie) {
                ji.state = JobState.CANCELLED;
                ji.message = "Conversion cancelled";
                safeDelete(out);
            } catch (Throwable t) {
                ji.state = JobState.FAILED;
                ji.message = t.getMessage();
                safeDelete(out);
            }
        });

        futures.put(key(fileId, JobType.CONVERT), f);
        return ji;
    }

    // ---------- Async: Query (SELECT only, inference + export + limits) ----------
    public JobInfo startQuery(String fileId, String sparql, long timeoutMillis, boolean includeInferred,
            ResultFormat fmt, long maxBytes, int maxRows) {
        JobInfo ji = new JobInfo(fileId, JobType.QUERY);
        String queryId = UUID.randomUUID().toString();
        ji.resultId = queryId;
        jobs.put(key(fileId, JobType.QUERY), ji);
        Future<?> f = cpuPool.submit(() -> {
            long start = System.currentTimeMillis();
            Path baseDir = Paths.get("data/query", fileId, queryId);
            boolean truncated = false;
            try {
                ji.state = JobState.RUNNING;
                ji.progress = 1;
                Files.createDirectories(baseDir);
                Repository repo = RepositoryRegistry.get(fileId);
                if (repo == null) {
                    throw new IllegalStateException("No repository for fileId: " + fileId);
                }
                int rowCount = 0;
                List<String> headers;
                Path jsonPath = (fmt == ResultFormat.JSON || fmt == ResultFormat.BOTH) ? baseDir.resolve("result.json") : null;
                Path csvPath = (fmt == ResultFormat.CSV || fmt == ResultFormat.BOTH) ? baseDir.resolve("result.csv") : null;

                // Counting streams
                CountingOutputStream jsonCounting = null;
                CountingOutputStream csvCounting = null;
                JsonGenerator jgen = null;
                BufferedWriter csvWriter = null;

                try ( RepositoryConnection conn = repo.getConnection()) {
                    TupleQuery tq = conn.prepareTupleQuery(sparql);
                    tq.setMaxExecutionTime((int) (timeoutMillis / 1000));
                    tq.setIncludeInferred(includeInferred);
                    try ( TupleQueryResult res = tq.evaluate()) {
                        headers = res.getBindingNames();
                        // Open writers lazily
                        if (jsonPath != null) {
                            jsonCounting = new CountingOutputStream(Files.newOutputStream(jsonPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
                            jgen = new JsonFactory().createGenerator(jsonCounting, com.fasterxml.jackson.core.JsonEncoding.UTF8);
                            jgen.writeStartArray();
                        }
                        if (csvPath != null) {
                            csvCounting = new CountingOutputStream(Files.newOutputStream(csvPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
                            csvWriter = new BufferedWriter(new OutputStreamWriter(csvCounting, StandardCharsets.UTF_8));
                            writeCsvLine(csvWriter, headers);
                        }
                        while (res.hasNext()) {
                            if (Thread.currentThread().isInterrupted()) {
                                throw new InterruptedException();
                            }
                            if (rowCount >= maxRows) {
                                truncated = true;
                                break;
                            }
                            BindingSet bs = res.next();
                            // JSON row
                            if (jgen != null) {
                                jgen.writeStartObject();
                                for (String h : headers) {
                                    String v = (bs.hasBinding(h) && bs.getValue(h) != null) ? bs.getValue(h).stringValue() : null;
                                    if (v == null) {
                                        jgen.writeNullField(h);
                                    } else {
                                        jgen.writeStringField(h, v);
                                    }
                                }
                                jgen.writeEndObject();
                                jgen.flush();
                                if (jsonCounting.getCount() > maxBytes) {
                                    truncated = true;
                                    break;
                                }
                            }
                            // CSV row
                            if (csvWriter != null) {
                                List<String> vals = new ArrayList<>(headers.size());
                                for (String h : headers) {
                                    String v = (bs.hasBinding(h) && bs.getValue(h) != null) ? bs.getValue(h).stringValue() : "";
                                    vals.add(v);
                                }
                                writeCsvLine(csvWriter, vals);
                                csvWriter.flush();
                                if (csvCounting.getCount() > maxBytes) {
                                    truncated = true;
                                    break;
                                }
                            }
                            rowCount++;
                            long elapsed = System.currentTimeMillis() - start;
                            ji.progress = (int) Math.min(99, (elapsed * 100 / Math.max(1, timeoutMillis)));
                        }
                    }
                } finally {
                    if (jgen != null) {
                        try {
                            jgen.writeEndArray();
                            jgen.close();
                        } catch (Exception ignore) {
                        }
                    }
                    if (csvWriter != null) {
                        try {
                            csvWriter.flush();
                            csvWriter.close();
                        } catch (Exception ignore) {
                        }
                    }
                }

                // register paths
                QueryPaths qp = new QueryPaths();
                qp.json = jsonPath;
                qp.csv = csvPath;
                queryResults.computeIfAbsent(fileId, k -> new ConcurrentHashMap<>()).put(queryId, qp);

                ji.message = "Rows=" + rowCount + "; includeInferred=" + includeInferred + "; format=" + fmt + "; truncated=" + truncated;
                ji.progress = 100;
                ji.state = JobState.COMPLETED;
                ji.resultId = queryId;
            } catch (org.eclipse.rdf4j.query.QueryInterruptedException tie) {
                ji.state = JobState.FAILED;
                ji.message = "Timeout or interrupted";
                safeDeleteDir(baseDir);
            } catch (InterruptedException ie) {
                ji.state = JobState.CANCELLED;
                ji.message = "Query cancelled";
                safeDeleteDir(baseDir);
            } catch (Throwable t) {
                ji.state = JobState.FAILED;
                ji.message = t.getMessage();
                safeDeleteDir(baseDir);
            }
        });
        futures.put(key(fileId, JobType.QUERY), f);
        return ji;
    }

    private static void writeCsvLine(Writer w, List<String> cols) throws IOException {
        boolean first = true;
        for (String c : cols) {
            if (!first) {
                w.write(',');
            }
            first = false;
            w.write(escapeCsv(c));
        }
        w.write("");
    }

    private static String escapeCsv(String v) {
        if (v == null) {
            return "";
        }
        boolean need = v.contains(",") || v.contains(
                "") || v.contains("\"");
        if (!need) {
            return v;
        }
        return '"' + v.replace("\"", "\"\"") + '"';
    }

    // ---------- Query results retrieval & cleanup ----------
    public Path getQueryResult(String fileId, String queryId, String format) {
        Map<String, QueryPaths> byQ = queryResults.get(fileId);
        if (byQ == null) {
            return null;
        }
        QueryPaths qp = byQ.get(queryId);
        if (qp == null) {
            return null;
        }
        if ("json".equalsIgnoreCase(format)) {
            return qp.json;
        }
        if ("csv".equalsIgnoreCase(format)) {
            return qp.csv;
        }
        return null;
    }

    public void cleanupQueryResult(String fileId, String queryId) {
        Map<String, QueryPaths> byQ = queryResults.get(fileId);
        Path dir = Paths.get("data/query", fileId, queryId);

        if (byQ != null) {
            QueryPaths qp = byQ.remove(queryId);
            // I singoli file possono essere già eliminati dal download; non è più necessario chiamare safeDelete singolarmente
            // usiamo la cancellazione ricorsiva sulla cartella del queryId
            if (qp != null) {
                deleteRecursively(dir);
            } else {
                // anche se non troviamo i path, tentiamo comunque di rimuovere la dir
                deleteRecursively(dir);
            }
            if (byQ.isEmpty()) {
                queryResults.remove(fileId);
            }
        } else {
            // non c'è mappa per il fileId: prova comunque a rimuovere la dir del queryId
            deleteRecursively(dir);
        }

        // rimuovi job record per QUERY
        jobs.remove(key(fileId, JobType.QUERY));
        futures.remove(key(fileId, JobType.QUERY));

        // prova a eliminare anche la dir del fileId se è rimasta vuota
        Path fileDir = Paths.get("data/query", fileId);
        deleteIfEmpty(fileDir);

        // opzionale: prova a eliminare anche "data/query" se è vuota
        Path baseDir = Paths.get("data/query");
        deleteIfEmpty(baseDir);
    }

    /**
     * Cancellazione ricorsiva robusta (niente stream da chiudere).
     */
    private static void deleteRecursively(Path root) {
        if (root == null || !Files.exists(root)) {
            return;
        }
        try {
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        Files.deleteIfExists(file);
                    } catch (IOException e) {
                        // fallback: prova on-exit (utile su Windows in caso di lock temporanei)
                        try {
                            file.toFile().deleteOnExit();
                        } catch (Throwable ignore) {
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    try {
                        Files.deleteIfExists(dir);
                    } catch (IOException e) {
                        try {
                            dir.toFile().deleteOnExit();
                        } catch (Throwable ignore) {
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            // ultimo fallback
            try {
                root.toFile().deleteOnExit();
            } catch (Throwable ignore) {
            }
        }
    }

    /**
     * Elimina la directory se è vuota.
     */
    private static void deleteIfEmpty(Path dir) {
        if (dir == null) {
            return;
        }
        try ( DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
            if (!ds.iterator().hasNext()) {
                try {
                    Files.deleteIfExists(dir);
                } catch (IOException e) {
                    try {
                        dir.toFile().deleteOnExit();
                    } catch (Throwable ignore) {
                    }
                }
            }
        } catch (IOException ignore) {
            // ignoriamo
        }
    }

//    public void cleanupQueryResult(String fileId, String queryId) {
//        Map<String, QueryPaths> byQ = queryResults.get(fileId);
//        if (byQ != null) {
//            QueryPaths qp = byQ.remove(queryId);
//            if (qp != null) {
//                safeDelete(qp.json);
//                safeDelete(qp.csv);
//                // also remove directory
//                Path dir = Paths.get("data/query", fileId, queryId);
//                safeDeleteDir(dir);
//            }
//            if (byQ.isEmpty()) {
//                queryResults.remove(fileId);
//            }
//        }
//        // remove job record for QUERY
//        jobs.remove(key(fileId, JobType.QUERY));
//        futures.remove(key(fileId, JobType.QUERY));
//    }
    private static void safeDelete(Path p) {
        if (p != null) try {
            Files.deleteIfExists(p);
        } catch (IOException ignore) {
        }
    }

    private static void safeDeleteDir(Path p) {
        if (p != null) try {
            if (Files.exists(p)) {
                Files.walk(p).sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignore) {
                    }
                });
            }
        } catch (IOException ignore) {
        }
    }

    // ---------- Cancel & Cleanup ----------
    public boolean cancel(String fileId, JobType type) {
        String k = key(fileId, type);
        Future<?> f = futures.get(k);
        if (f != null) {
            boolean ok = f.cancel(true);
            JobInfo ji = jobs.get(k);
            if (ji != null) {
                ji.state = JobState.CANCELLED;
                ji.message = "Cancelled by user";
            }
            return ok;
        }
        return false;
    }

    public void cleanupAllFor(String fileId) throws IOException, Exception {
        for (JobType t : JobType.values()) {
            cancel(fileId, t);
        }
        // clean query artifacts
        Map<String, QueryPaths> byQ = queryResults.remove(fileId);
        if (byQ != null) {
            for (Map.Entry<String, QueryPaths> e : byQ.entrySet()) {
                safeDelete(e.getValue().json);
                safeDelete(e.getValue().csv);
                safeDeleteDir(Paths.get("data/query", fileId, e.getKey()));
            }
        }
        RepositoryRegistry.remove(fileId);
        java.util.List<Path> ups = uploads.remove(fileId);
        if (ups != null) {
            for (Path up : ups) {
                if (up != null) {
                    Files.deleteIfExists(up);
                }
            }
        }
        Path cv = converted.remove(fileId);
        if (cv != null) {
            Files.deleteIfExists(cv);
        }
        for (JobType t : JobType.values()) {
            jobs.remove(key(fileId, t));
            futures.remove(key(fileId, t));
        }
    }

    public void shutdown() {
        ioPool.shutdownNow();
        cpuPool.shutdownNow();
    }

    private static String shorten(String s) {
        if (s == null) {
            return null;
        }
        s = s.replaceAll("", " ");
        return s.length() > 200 ? s.substring(0, 200) + "…" : s;
    }

    // Progress wrapper
    static class ProgressInputStream extends FilterInputStream {

        private final long total;
        private long seen;
        private final java.util.function.IntConsumer onProgress;

        protected ProgressInputStream(InputStream in, long total, java.util.function.IntConsumer onProgress) {
            super(in);
            this.total = total;
            this.onProgress = onProgress;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int r = super.read(b, off, len);
            if (r > 0) {
                update(r);
            }
            return r;
        }

        @Override
        public int read() throws IOException {
            int r = super.read();
            if (r != -1) {
                update(1);
            }
            return r;
        }

        private void update(int r) throws IOException {
            seen += r;
            int p = (int) Math.min(99, (seen * 100 / Math.max(1, total)));
            onProgress.accept(p);
            if (Thread.currentThread().isInterrupted()) {
                throw new IOException("Interrupted");
            }
        }
    }

    // CountingOutputStream (no exceptions; caller inspects getCount())
    static class CountingOutputStream extends OutputStream {

        private final OutputStream delegate;
        private long count = 0L;

        CountingOutputStream(OutputStream d) {
            this.delegate = d;
        }

        public long getCount() {
            return count;
        }

        @Override
        public void write(int b) throws IOException {
            delegate.write(b);
            count++;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            delegate.write(b, off, len);
            count += len;
        }

        @Override
        public void flush() throws IOException {
            delegate.flush();
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }
    }

}
