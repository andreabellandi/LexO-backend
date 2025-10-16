/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.util.Converter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author andreabellandi
 */
public class JobManager implements Manager {

    public static enum Status {
        UPLOADED, PROCESSING, READY, FAILED
    }

    public static final class JobInfo {

        public final String id;
        public volatile Status status;
        public volatile String message;
        public volatile int progress = 0;   // 0..100
        public final Instant createdAt = Instant.now();

        JobInfo(String id, Status status) {
            this.id = id;
            this.status = status;
        }
    }

    private final Map<String, JobInfo> JOBS = new ConcurrentHashMap<>();
    private final ExecutorService EXEC = new ThreadPoolExecutor(
            2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            r -> {
                Thread t = new Thread(r, "converter");
                t.setDaemon(true);
                return t;
            }
    );

    public String newId() {
        return UUID.randomUUID().toString();
    }

    public void registerUpload(String id) {
        JOBS.putIfAbsent(id, new JobInfo(id, Status.UPLOADED));
    }

    public JobInfo ensureExists(String id) {
        JobInfo j = JOBS.get(id);
        if (j == null) {
            throw new NotFound("Unknown id: " + id);
        }
        return j;
    }

    public JobInfo get(String id) {
        JobInfo j = JOBS.get(id);
        if (j == null) {
            throw new NotFound("Unknown id: " + id);
        }
        return j;
    }

    public JobInfo startAsync(String id, Path src, Path out) {
        JobInfo job = ensureExists(id);
        synchronized (job) {
            if (job.status == Status.PROCESSING || job.status == Status.READY) {
                return job;
            }
            job.status = Status.PROCESSING;
            job.message = null;
            job.progress = 0;
            CompletableFuture.runAsync(() -> {
                try {
                    Converter.convert(src, out, p -> job.progress = p);
                    job.progress = 100;
                    job.status = Status.READY;
                } catch (IOException e) {
                    job.status = Status.FAILED;
                    job.message = e.getClass().getSimpleName() + ": " + e.getMessage();
                }
            }, EXEC);
            return job;
        }
    }

    public void shutdown() {
        EXEC.shutdown();
    }

    public static class NotFound extends RuntimeException {

        public NotFound(String m) {
            super(m);
        }
    }

    public void copyWithLimit(InputStream in, OutputStream out, long limit) throws IOException, ManagerException {
        byte[] buf = new byte[8192];
        long total = 0;
        int r;
        while ((r = in.read(buf)) != -1) {
            total += r;
            if (total > limit) {
                throw new ManagerException("file size exceeded " + limit);
            }
            out.write(buf, 0, r);
        }
    }

}
