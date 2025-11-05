/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.util.RDFInserter;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

/**
 *
 * @author andreabellandi
 */
public final class Rdf4jLoadUtil {

    public static final class Result {

        public final Repository repository;
        public final long triples;

        public Result(Repository repository, long triples) {
            this.repository = repository;
            this.triples = triples;
        }
    }

    private Rdf4jLoadUtil() {
    }

    public static Result loadIntoMemory(Path file,
            String formatOpt,
            String baseUriOpt,
            final IntConsumer onProgress,
            final LongConsumer onTriples,
            final BooleanSupplier shouldCancel) throws  RepositoryException, IOException, UnsupportedRDFormatException, RDFParseException, RDFHandlerException, Exception {
        long total = Files.size(file);
        if (total <= 0) {
            total = 1;
        }

        RDFFormat format = resolveFormat(file, formatOpt);
        String base = (baseUriOpt != null && !baseUriOpt.isEmpty()) ? baseUriOpt : file.toUri().toString();

        Repository repo = new SailRepository(new MemoryStore());
        repo.init();

        final AtomicLong count = new AtomicLong(0);

        try (final RepositoryConnection cx = repo.getConnection();  InputStream raw = Files.newInputStream(file);  InputStream in = new CountingInputStream(raw, total, onProgress, shouldCancel)) {

            RDFParser parser = Rio.createParser(format);

            // Inseritore che scrive direttamente nella connessione (e aggiorna progress/contatore)
            RDFInserter inserter = new RDFInserter(cx) {
                @Override
                public void handleStatement(Statement st) {
                    if (shouldCancel.getAsBoolean()) {
                        throw new RuntimeException("CANCEL");
                    }
                    super.handleStatement(st);              // <-- fa cx.add(st)
                    long n = count.incrementAndGet();
                    if ((n & 0x3FFF) == 0) {
                        onTriples.accept(n);
                    }
                }
            };

            parser.setRDFHandler(inserter);

            try {
                parser.parse(in, base);
            } catch (RuntimeException re) {
                if ("CANCEL".equals(re.getMessage())) {
                    throw new InterruptedException();
                }
                throw re;
            }

            onTriples.accept(count.get());
            onProgress.accept(100);
            return new Result(repo, count.get());
        } catch (Exception e) {
            try {
                repo.shutDown();
            } catch (RepositoryException ignore) {
            }
            throw e;
        }
    }

    private static RDFFormat resolveFormat(Path file, String formatOpt) {
        if (formatOpt != null && !formatOpt.isEmpty()) {
            String f = formatOpt.trim().toUpperCase(Locale.ROOT);
            if ("TURTLE".equals(f) || "TTL".equals(f)) {
                return RDFFormat.TURTLE;
            }
            if ("RDFXML".equals(f) || "RDF/XML".equals(f) || "RDF".equals(f)) {
                return RDFFormat.RDFXML;
            }
            if ("JSONLD".equals(f) || "JSON-LD".equals(f)) {
                return RDFFormat.JSONLD;
            }
            if ("TRIG".equals(f)) {
                return RDFFormat.TRIG;
            }
            if ("NTRIPLES".equals(f) || "NT".equals(f)) {
                return RDFFormat.NTRIPLES;
            }
            if ("NQUADS".equals(f) || "NQ".equals(f)) {
                return RDFFormat.NQUADS;
            }
        }
        RDFFormat guess = Rio.getParserFormatForFileName(file.getFileName().toString()).orElse(RDFFormat.TURTLE);
        return guess;
    }

    /**
     * Stream che aggiorna progress (%) per byte e supporta cancel.
     */
    public static class CountingInputStream extends java.io.FilterInputStream {

        private final long total;
        private long read = 0L;
        private int last = -1;
        private final IntConsumer progress;
        private final BooleanSupplier cancel;

        public CountingInputStream(InputStream in, long total, IntConsumer progress, BooleanSupplier cancel) {
            super(in);
            this.total = total;
            this.progress = progress;
            this.cancel = cancel;
        }

        @Override
        public int read() throws java.io.IOException {
            check();
            int b = super.read();
            if (b >= 0) {
                tick(1);
            }
            return b;
        }

        @Override
        public int read(byte[] b, int off, int len) throws java.io.IOException {
            check();
            int r = super.read(b, off, len);
            if (r > 0) {
                tick(r);
            }
            return r;
        }

        private void tick(int n) {
            read += n;
            int p = (int) Math.min(100, (read * 100) / total);
            if (p != last) {
                progress.accept(p);
                last = p;
            }
        }

        private void check() throws java.io.IOException {
            if (cancel.getAsBoolean()) {
                throw new java.io.IOException("CANCEL");
            }
        }
    }
}
