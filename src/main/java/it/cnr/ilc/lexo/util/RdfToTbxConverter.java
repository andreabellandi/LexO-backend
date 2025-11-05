/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;

/**
 *
 * @author andreabellandi
 */
public final class RdfToTbxConverter {

  public static final class Result {
    public final Path outputPath;
    public final long processed;
    public Result(Path out, long processed) { this.outputPath = out; this.processed = processed; }
  }

  private RdfToTbxConverter() {}

  public static Result convert(Path input, Path output, String formatOpt,
                               final IntConsumer onProgress,
                               final LongConsumer onProcessed,
                               final BooleanSupplier shouldCancel) throws Exception {
    long total = Files.size(input);
    if (total <= 0) total = 1;

    RDFFormat fmt = resolveFormat(input, formatOpt);

    // 1) Parse in-memory (per dataset enormi valuta approccio streaming)
    Model model = new TreeModel();
    InputStream raw = null;
    Rdf4jLoadUtil.CountingInputStream in = null;
    try {
      raw = Files.newInputStream(input);
      in = new Rdf4jLoadUtil.CountingInputStream(raw, total, onProgress, shouldCancel);
      RDFParser parser = Rio.createParser(fmt);
      parser.setRDFHandler(new StatementCollector(model));
      parser.parse(in, input.toUri().toString());
    } finally {
      try { if (in != null) in.close(); } catch (Exception ignore) {}
      try { if (raw != null) raw.close(); } catch (Exception ignore) {}
    }

    // 2) Scrittura TBX minimale (aggiungi il mapping OntoLex -> TBX qui)
    OutputStream os = null;
    try {
        
      Thread.sleep(60000);
        
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
      w.writeEndElement(); w.writeEndElement();
      w.writeStartElement("sourceDesc");
      w.writeStartElement("p");
      w.writeCharacters("Source RDF: " + input.getFileName());
      w.writeEndElement(); w.writeEndElement();
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
      try { if (os != null) os.close(); } catch (Exception ignore) {}
    }

    onProcessed.accept(model.size());
    onProgress.accept(100);
    return new Result(output, model.size());
  }

  private static RDFFormat resolveFormat(Path file, String formatOpt) {
    if (formatOpt != null && !formatOpt.isEmpty()) {
      String f = formatOpt.trim().toUpperCase(Locale.ROOT);
      if ("TURTLE".equals(f) || "TTL".equals(f)) return RDFFormat.TURTLE;
      if ("RDFXML".equals(f) || "RDF/XML".equals(f) || "RDF".equals(f)) return RDFFormat.RDFXML;
      if ("JSONLD".equals(f) || "JSON-LD".equals(f)) return RDFFormat.JSONLD;
    }
    return Rio.getParserFormatForFileName(file.getFileName().toString()).orElse(RDFFormat.TURTLE);
  }
}
