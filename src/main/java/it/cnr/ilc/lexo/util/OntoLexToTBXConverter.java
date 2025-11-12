/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.eclipse.rdf4j.repository.Repository;

/**
 *
 * @author andreabellandi
 */
public final class OntoLexToTBXConverter {

    public static final class Result {

        public final Path outputPath;
//        public final long processed;

        public Result(Path out//, long processed
        ) {
            this.outputPath = out;
//            this.processed = processed;
        }
    }

    private OntoLexToTBXConverter() {
    }

    public static Result convert(Repository repo, Path input, Path output, Map<String, String> options,
            final IntConsumer onProgress,
            final LongConsumer onProcessed,
            final BooleanSupplier shouldCancel) throws Exception {
        long total = Files.size(input);
        if (total <= 0) {
            total = 1;
        }
        if (options.get("type") != null) {
            if (options.get("type").equals("automatic")) {
                automaticConversion(input, output, options.get("model"));
            } else {
                // TODO: call interactiveConversion
            }
        } else {
            automaticConversion(input, output, options.get("model"));
        }
//        long processedTriples = repo.getConnection().size();
//        onProcessed.accept(processedTriples);
        onProgress.accept(100);
//        return new Result(output, processedTriples);
        return new Result(output);
    }
    
    private static void automaticConversion(Path input, Path output, String model) throws IOException, XMLStreamException {
        OutputStream os = null;
        try {
            os = Files.newOutputStream(output);
            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter w = xof.createXMLStreamWriter(os, "UTF-8");
            w.writeStartDocument("UTF-8", "1.0");
            w.writeStartElement("tbx");
            w.writeAttribute("xmlns", "urn:iso:std:iso:30042:ed-2");
            w.writeAttribute("type", "TBX-Basic");
            w.writeAttribute("style", "dca");
            w.writeAttribute("xml:lang", "en");

            w.writeStartElement("tbxHeader");
            w.writeStartElement("fileDesc");
            w.writeStartElement("titleStmt");
            w.writeStartElement("title");
            w.writeCharacters("Generated from RDF");
            w.writeEndElement();
            w.writeEndElement();
            w.writeStartElement("sourceDesc");
            w.writeStartElement("p");
            w.writeCharacters("Source RDF: " + input.getFileName());
            w.writeEndElement();
            w.writeEndElement();
            w.writeEndElement(); // fileDesc
            w.writeEndElement(); // tbxHeader

            w.writeStartElement("text");
            w.writeStartElement("body");
            // TODO: mapping OntoLex → TBX (termEntry/langSet/tig/term/...)
            w.writeEndElement(); // body
            w.writeEndElement(); // text
            w.writeEndElement(); // tbx
            w.writeEndDocument();
            w.flush();
            w.close();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

}
