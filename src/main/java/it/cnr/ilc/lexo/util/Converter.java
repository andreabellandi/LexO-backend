package it.cnr.ilc.lexo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author andreabellandi
 */
public class Converter {

    public static void convert(Path in, Path out, ProgressListener progress) throws IOException {
        long size = Files.size(in);
        if (size <= 0) {
            size = 1;
        }
        long read = 0;
        int last = -1;

        try ( BufferedReader br = Files.newBufferedReader(in);  BufferedWriter bw = Files.newBufferedWriter(out)) {
            String line;
            while ((line = br.readLine()) != null) {
                byte[] bytes = line.getBytes();
                read += bytes.length + System.lineSeparator().getBytes().length;

                bw.write(line.toUpperCase());
                bw.newLine();

                int p = (int) Math.min(100, (read * 100) / size);
                if (p != last) {
                    progress.onProgress(p);
                    last = p;
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ignored) {
                }
            }
        }
        progress.onProgress(100);
    }
}
