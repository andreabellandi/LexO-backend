package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.manager.JobManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 *
 * @author andreabellandi
 */
public final class FileStore {

    private static final Path ROOT = Paths.get(System.getProperty("java.io.tmpdir"), "uploads");
    private static final Path SRC = ROOT.resolve("source");
    private static final Path OUT = ROOT.resolve("converted");

    static {
        try {
            Files.createDirectories(SRC);
            Files.createDirectories(OUT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path sourcePath(String id, String filename) {
        return SRC.resolve(id + "_" + filename);
    }

    public static Path findSource(String id) {
        try ( Stream<Path> s = Files.list(SRC)) {
            return s.filter(p -> p.getFileName().toString().startsWith(id + "_"))
                    .findFirst()
                    .orElseThrow(() -> new JobManager.NotFound("Source not found"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path outPath(String id) {
        return OUT.resolve(id + ".txt"); // estensione target
    }
}
