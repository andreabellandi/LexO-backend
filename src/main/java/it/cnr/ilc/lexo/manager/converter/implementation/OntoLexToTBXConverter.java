/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager.converter.implementation;

import it.cnr.ilc.lexo.service.data.converter.tbx.EntryForms;
import it.cnr.ilc.lexo.service.data.converter.tbx.EntryInfo;
import it.cnr.ilc.lexo.service.data.converter.tbx.Relation;
import it.cnr.ilc.lexo.service.data.converter.tbx.TermRecord;
import it.cnr.ilc.lexo.service.data.converter.tbx.TermSelection;
import it.cnr.ilc.lexo.sparql.SparqlConvertionData;
import it.cnr.ilc.lexo.util.PoSMapping;
import it.cnr.ilc.lexo.util.RDF4jLoadUtil;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import it.cnr.ilc.lexo.util.StringUtil;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import static org.apache.commons.io.IOUtils.writer;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

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
                automaticConversion(repo, input, output, options.get("model"), onProgress);
            } else {
                // TODO: call interactiveConversion
            }
        } else {
            automaticConversion(repo, input, output, options.get("model"), onProgress);
        }
//        long processedTriples = repo.getConnection().size();
//        onProcessed.accept(processedTriples);
        onProgress.accept(100);
//        return new Result(output, processedTriples);
        return new Result(output);
    }

    private static void automaticConversion(Repository repo, Path input, Path output, String model, final IntConsumer onProgress) throws IOException, XMLStreamException {
        // Concetti già esportati (reference + denotes) per evitare duplicati
        Set<String> exportedConcepts = new HashSet<>();
        try ( OutputStream os = new FileOutputStream(output.toString());  OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            Format fmt = Format.getPrettyFormat();
            fmt.setOmitDeclaration(true);
            XMLOutputter xo = new XMLOutputter(fmt);
            try ( RepositoryConnection conn = repo.getConnection()) {
                writeTBXHeader(writer);
                onProgress.accept(20);
                long rcref = writeTBXRealConceptViaReference(conn, 1000, xo, writer, exportedConcepts);
                onProgress.accept(40);
                long rcden = writeTBXRealConceptViaDenotes(conn, 1000, xo, writer, exportedConcepts);
                onProgress.accept(60);
                long dcls = writeTBXDummyConceptForOrphanLexicalSenses(conn, 1000, xo, writer);
                onProgress.accept(80);
                long dcle = writeTBXDummyConceptForOrphanLexicalEntries(conn, 1000, xo, writer);
                writeTBXFooter(writer);
                writer.flush();
                onProgress.accept(90);
                writer.close();
                os.close();
                System.out.println("Completato.");
                System.out.println("  - termEntry concetti reali (reference): " + rcref);
                System.out.println("  - termEntry concetti reali (denotes):   " + rcden);
                System.out.println("  - termEntry concetti fittizi (sensi):   " + dcls);
                System.out.println("  - termEntry concetti fittizi (entry):   " + dcle);
            } catch (Exception ex) {
                Logger.getLogger(OntoLexToTBXConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static long writeTBXRealConceptViaReference(RepositoryConnection conn, int batchSize, XMLOutputter xo,
            OutputStreamWriter writer, Set<String> exportedConcepts) throws Exception {
        // concepts referenced by senses (ontolex:reference)
        String lastCRef = null;
        long totalRealConceptEntriesFromReference = 0;
        while (true) {
            String filter = (lastCRef == null) ? ""
                    : "  FILTER( STR(?c) > \"" + StringUtil.escapeForSPARQLString(lastCRef) + "\" )\n";
            String q = SparqlConvertionData.GET_REAL_REFERENCED_CONCEPTS.replace("_FILTER_", filter).replace("_LIMIT_", Integer.toString(batchSize));
            TupleQuery tq = conn.prepareTupleQuery(QueryLanguage.SPARQL, q);
            // concetto -> entry -> info
            Map<String, Map<String, EntryInfo>> perConcept = new LinkedHashMap<>();
            List<String> conceptOrder = new ArrayList<>();
            Set<String> batchEntries = new HashSet<>();
            String lastSeenC = null;
            try ( TupleQueryResult res = tq.evaluate()) {
                while (res.hasNext()) {
                    BindingSet bs = res.next();
                    String c = RDF4jLoadUtil.getUri(bs, "c");
                    String entryId = RDF4jLoadUtil.getResourceId(bs, "entry");
                    String posUri = RDF4jLoadUtil.getUri(bs, "pos");
                    String def = RDF4jLoadUtil.getLiteralString(bs, "def");
                    String lexLang = RDF4jLoadUtil.getLiteralString(bs, "lexLang");
                    if (c == null || entryId == null || lexLang == null) {
                        continue;
                    }
                    lastSeenC = c;
                    batchEntries.add(entryId);
                    Map<String, EntryInfo> entryMap
                            = perConcept.computeIfAbsent(c, k -> {
                                conceptOrder.add(k);
                                return new LinkedHashMap<>();
                            });
                    EntryInfo info = entryMap.get(entryId);
                    String posTbx = posUri == null ? null : PoSMapping.getPOS_MAP().get(posUri);

                    if (info == null) {
                        info = new EntryInfo(entryId, lexLang, posTbx, def);
                        entryMap.put(entryId, info);
                    } else {
                        if (info.pos == null && posTbx != null) {
                            info.pos = posTbx;
                        }
                        if (info.definition == null && def != null) {
                            info.definition = def;
                        }
                    }
                }
            }
            if (lastSeenC == null) {
                break;
            }
            lastCRef = lastSeenC;
            // carica le forme per gli entry di questo batch
            Map<String, EntryForms> entryForms = loadEntryForms(conn, batchEntries);
            // relazioni, se esistono
//            Map<String, List<Relation>> rels = Collections.emptyMap();
//            if (!perConcept.isEmpty()) {
//                String q2 = buildQ2Relations(perConcept.keySet());
//                if (q2 != null) {
//                    Map<String, List<Relation>> tmp = new HashMap<>();
//                    TupleQuery tq2 = conn.prepareTupleQuery(QueryLanguage.SPARQL, q2);
//                    try ( TupleQueryResult r2 = tq2.evaluate()) {
//                        while (r2.hasNext()) {
//                            BindingSet bs2 = r2.next();
//                            String c = getUri(bs2, "c");
//                            String related = getUri(bs2, "relatedC");
//                            String relType = getLiteralString(bs2, "relType");
//                            if (c != null && relType != null && related != null) {
//                                tmp.computeIfAbsent(c, k -> new ArrayList<>())
//                                        .add(new Relation(relType, related));
//                            }
//                        }
//                    }
//                    rels = tmp;
//                }
//            }
            for (String c : conceptOrder) {
                Map<String, EntryInfo> entryMap = perConcept.get(c);
                Map<String, LinkedHashSet<TermRecord>> langTerms = new LinkedHashMap<>();
                for (EntryInfo info : entryMap.values()) {
                    EntryForms ef = entryForms.get(info.entryId);
                    TermSelection sel = selectTerm(ef);
                    TermRecord tr = new TermRecord(sel.mainTerm, info.pos, info.definition, sel.altForms);
                    LinkedHashSet<TermRecord> terms = langTerms.computeIfAbsent(info.lexLang, k -> new LinkedHashSet<>());
                    terms.add(tr);
                }
                Element te = buildTermEntry(
                        c, // idKey
                        c, // conceptIdentifier (reale)
                        langTerms,
                        null// for relations rels.get(c)
                );
                xo.output(te, writer);
                writer.write("\n");
                totalRealConceptEntriesFromReference++;
                exportedConcepts.add(c);
                if (totalRealConceptEntriesFromReference % 1000 == 0) {
                    System.out.println("termEntry concetti reali (reference): "
                            + totalRealConceptEntriesFromReference + " (ultimo: " + c + ")");
                }
            }
            writer.flush();
        }
        return totalRealConceptEntriesFromReference;
    }

    private static long writeTBXDummyConceptForOrphanLexicalSenses(RepositoryConnection conn, int batchSize, XMLOutputter xo,
            OutputStreamWriter writer) throws Exception {
        String lastSenseStr = null;
        long totalPseudoFromSenses = 0;
        while (true) {
            String filter = (lastSenseStr == null) ? ""
                    : "  FILTER( STR(?sense) > \"" + StringUtil.escapeForSPARQLString(lastSenseStr) + "\" )\n";
            String qOrphanSenses = SparqlConvertionData.GET_DUMMY_NOT_REFERENCED_CONCEPTS.replace("_FILTER_", filter).replace("_LIMIT_", Integer.toString(batchSize));
            TupleQuery tqOrphans = conn.prepareTupleQuery(QueryLanguage.SPARQL, qOrphanSenses);
            Map<String, Map<String, EntryInfo>> perSense = new LinkedHashMap<>();
            List<String> senseOrder = new ArrayList<>();
            Set<String> batchEntries = new HashSet<>();
            String lastSeenSense = null;
            try ( TupleQueryResult resOrphans = tqOrphans.evaluate()) {
                while (resOrphans.hasNext()) {
                    BindingSet bs = resOrphans.next();
                    String senseId = RDF4jLoadUtil.getResourceId(bs, "sense");
                    String entryId = RDF4jLoadUtil.getResourceId(bs, "entry");
                    String posUri = RDF4jLoadUtil.getUri(bs, "pos");
                    String def = RDF4jLoadUtil.getLiteralString(bs, "def");
                    String lexLang = RDF4jLoadUtil.getLiteralString(bs, "lexLang");
                    if (senseId == null || entryId == null || lexLang == null) {
                        continue;
                    }
                    lastSeenSense = senseId;
                    batchEntries.add(entryId);
                    Map<String, EntryInfo> entryMap
                            = perSense.computeIfAbsent(senseId, k -> {
                                senseOrder.add(k);
                                return new LinkedHashMap<>();
                            });
                    EntryInfo info = entryMap.get(entryId);
                    String posTbx = posUri == null ? null : PoSMapping.getPOS_MAP().get(posUri);
                    if (info == null) {
                        info = new EntryInfo(entryId, lexLang, posTbx, def);
                        entryMap.put(entryId, info);
                    } else {
                        if (info.pos == null && posTbx != null) {
                            info.pos = posTbx;
                        }
                        if (info.definition == null && def != null) {
                            info.definition = def;
                        }
                    }
                }
            }
            if (lastSeenSense == null) {
                break;
            }
            lastSenseStr = lastSeenSense;
            Map<String, EntryForms> entryForms = loadEntryForms(conn, batchEntries);
            for (String senseKey : senseOrder) {
                Map<String, EntryInfo> entryMap = perSense.get(senseKey);
                Map<String, LinkedHashSet<TermRecord>> langTerms = new LinkedHashMap<>();
                for (EntryInfo info : entryMap.values()) {
                    EntryForms ef = entryForms.get(info.entryId);
                    TermSelection sel = selectTerm(ef);
                    TermRecord tr = new TermRecord(sel.mainTerm, info.pos, info.definition, sel.altForms);
                    LinkedHashSet<TermRecord> terms
                            = langTerms.computeIfAbsent(info.lexLang, k -> new LinkedHashSet<>());
                    terms.add(tr);
                }
                String pseudoConcept = "pseudo:sense:" + senseKey;
                Element te = buildTermEntry(
                        "sense:" + senseKey, // idKey
                        pseudoConcept, // conceptIdentifier fittizio
                        langTerms,
                        null
                );
                xo.output(te, writer);
                writer.write("\n");
                totalPseudoFromSenses++;
                if (totalPseudoFromSenses % 1000 == 0) {
                    System.out.println("termEntry concetti fittizi (sensi): "
                            + totalPseudoFromSenses + " (ultimo senseKey: " + senseKey + ")");
                }
            }
            writer.flush();
        }
        return totalPseudoFromSenses;
    }

    private static long writeTBXRealConceptViaDenotes(RepositoryConnection conn, int batchSize, XMLOutputter xo,
            OutputStreamWriter writer, Set<String> exportedConcepts) throws Exception {
        String lastCDenotes = null;
        long totalRealConceptEntriesFromDenotes = 0;
        while (true) {
            String filter = (lastCDenotes == null) ? ""
                    : "  FILTER( STR(?c) > \"" + StringUtil.escapeForSPARQLString(lastCDenotes) + "\" )\n";
            String qDenotes = SparqlConvertionData.GET_REAL_DENOTED_CONCEPTS.replace("_FILTER_", filter).replace("_LIMIT_", Integer.toString(batchSize));
            TupleQuery tqDenotes = conn.prepareTupleQuery(QueryLanguage.SPARQL, qDenotes);
            Map<String, Map<String, EntryInfo>> perConceptDenotes = new LinkedHashMap<>();
            List<String> conceptDenotesOrder = new ArrayList<>();
            Set<String> batchEntries = new HashSet<>();
            String lastSeenC2 = null;
            try ( TupleQueryResult resDenotes = tqDenotes.evaluate()) {
                while (resDenotes.hasNext()) {
                    BindingSet bs = resDenotes.next();
                    String c = RDF4jLoadUtil.getUri(bs, "c");
                    String entryId = RDF4jLoadUtil.getResourceId(bs, "entry");
                    String posUri = RDF4jLoadUtil.getUri(bs, "pos");
                    String lexLang = RDF4jLoadUtil.getLiteralString(bs, "lexLang");
                    if (c == null || entryId == null || lexLang == null) {
                        continue;
                    }
                    lastSeenC2 = c;
                    // Se già esportato via reference, salta il concetto
                    if (exportedConcepts.contains(c)) {
                        continue;
                    }
                    batchEntries.add(entryId);
                    Map<String, EntryInfo> entryMap
                            = perConceptDenotes.computeIfAbsent(c, k -> {
                                conceptDenotesOrder.add(k);
                                return new LinkedHashMap<>();
                            });
                    EntryInfo info = entryMap.get(entryId);
                    String posTbx = posUri == null ? null : PoSMapping.getPOS_MAP().get(posUri);
                    if (info == null) {
                        info = new EntryInfo(entryId, lexLang, posTbx, null);
                        entryMap.put(entryId, info);
                    } else {
                        if (info.pos == null && posTbx != null) {
                            info.pos = posTbx;
                        }
                    }
                }
            }
            if (lastSeenC2 == null) {
                break;
            }
            lastCDenotes = lastSeenC2;
            Map<String, EntryForms> entryForms = loadEntryForms(conn, batchEntries);
            for (String c : conceptDenotesOrder) {
                Map<String, EntryInfo> entryMap = perConceptDenotes.get(c);
                Map<String, LinkedHashSet<TermRecord>> langTerms = new LinkedHashMap<>();
                for (EntryInfo info : entryMap.values()) {
                    EntryForms ef = entryForms.get(info.entryId);
                    TermSelection sel = selectTerm(ef);
                    TermRecord tr = new TermRecord(sel.mainTerm, info.pos, info.definition, sel.altForms);
                    LinkedHashSet<TermRecord> terms
                            = langTerms.computeIfAbsent(info.lexLang, k -> new LinkedHashSet<>());
                    terms.add(tr);
                }

                Element te = buildTermEntry(
                        "denotes:" + c,
                        c, // concetto reale
                        langTerms,
                        null
                );
                xo.output(te, writer);
                writer.write("\n");
                totalRealConceptEntriesFromDenotes++;
                exportedConcepts.add(c);
                if (totalRealConceptEntriesFromDenotes % 1000 == 0) {
                    System.out.println("termEntry concetti reali (denotes): "
                            + totalRealConceptEntriesFromDenotes + " (ultimo: " + c + ")");
                }
            }
            writer.flush();
        }
        return totalRealConceptEntriesFromDenotes;
    }

    private static long writeTBXDummyConceptForOrphanLexicalEntries(RepositoryConnection conn, int batchSize, XMLOutputter xo,
            OutputStreamWriter writer) throws Exception {
        String lastEntryKey = null;
        long totalPseudoFromEntries = 0;
        while (true) {
            String filter = (lastEntryKey == null) ? ""
                    : "  FILTER( STR(?entry) > \"" + StringUtil.escapeForSPARQLString(lastEntryKey) + "\" )\n";
            String qOrphanEntries = SparqlConvertionData.GET_DUMMY_NOT_DENOTED_CONCEPTS.replace("_FILTER_", filter).replace("_LIMIT_", Integer.toString(batchSize));
            TupleQuery tqOrphanEntries = conn.prepareTupleQuery(QueryLanguage.SPARQL, qOrphanEntries);
            Map<String, EntryInfo> perEntry = new LinkedHashMap<>();
            List<String> entryOrder = new ArrayList<>();
            Set<String> batchEntries = new HashSet<>();
            String lastSeenEntry = null;
            try ( TupleQueryResult resE = tqOrphanEntries.evaluate()) {
                while (resE.hasNext()) {
                    BindingSet bs = resE.next();
                    String entryId = RDF4jLoadUtil.getResourceId(bs, "entry");
                    String posUri = RDF4jLoadUtil.getUri(bs, "pos");
                    String lexLang = RDF4jLoadUtil.getLiteralString(bs, "lexLang");
                    if (entryId == null || lexLang == null) {
                        continue;
                    }
                    lastSeenEntry = entryId;
                    batchEntries.add(entryId);
                    EntryInfo info = perEntry.get(entryId);
                    String posTbx = posUri == null ? null : PoSMapping.getPOS_MAP().get(posUri);
                    if (info == null) {
                        info = new EntryInfo(entryId, lexLang, posTbx, null);
                        perEntry.put(entryId, info);
                        entryOrder.add(entryId);
                    } else {
                        if (info.pos == null && posTbx != null) {
                            info.pos = posTbx;
                        }
                    }
                }
            }
            if (lastSeenEntry == null) {
                break;
            }
            lastEntryKey = lastSeenEntry;
            Map<String, EntryForms> entryForms = loadEntryForms(conn, batchEntries);
            for (String entryKey : entryOrder) {
                EntryInfo info = perEntry.get(entryKey);
                Map<String, LinkedHashSet<TermRecord>> langTerms = new LinkedHashMap<>();
                EntryForms ef = entryForms.get(info.entryId);
                TermSelection sel = selectTerm(ef);
                TermRecord tr = new TermRecord(sel.mainTerm, info.pos, info.definition, sel.altForms);
                LinkedHashSet<TermRecord> terms
                        = langTerms.computeIfAbsent(info.lexLang, k -> new LinkedHashSet<>());
                terms.add(tr);
                String pseudoConcept = "pseudo:entry:" + entryKey;
                Element te = buildTermEntry(
                        "entry:" + entryKey,
                        pseudoConcept,
                        langTerms,
                        null
                );
                xo.output(te, writer);
                writer.write("\n");
                totalPseudoFromEntries++;
                if (totalPseudoFromEntries % 1000 == 0) {
                    System.out.println("termEntry concetti fittizi (entry): "
                            + totalPseudoFromEntries + " (ultimo entryKey: " + entryKey + ")");
                }
            }
            writer.flush();
        }
        return totalPseudoFromEntries;
    }

    private static void writeTBXHeader(Writer w) throws IOException {
        w.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        w.write("<?xml-model href=\"https://raw.githubusercontent.com/LTAC-Global/TBX-Basic_dialect/master/DCT/TBX-Basic_DCT.sch\" type=\"application/xml\" schematypens=\"http://purl.oclc.org/dsdl/schematron\"?>\n");
        w.write("<?xml-model href=\"https://raw.githubusercontent.com/LTAC-Global/TBX-Basic_dialect/master/DCT/TBX-Basic.nvdl\" type=\"application/xml\" schematypens=\"http://purl.oclc.org/dsdl/nvdl/ns/structure/1.0\"?>\n");
        w.write("<tbx xmlns=\"urn:iso:std:iso:30042:ed-2\" type=\"TBX-Basic\" style=\"dct\" xml:lang=\"en\" xmlns:min=\"http://www.tbxinfo.net/ns/min\" xmlns:basic=\"http://www.tbxinfo.net/ns/basic\">\n");
        w.write("  <tbxHeader>\n");
        w.write("    <fileDesc>\n");
        w.write("      <sourceDesc>\n");
        w.write("        <p>Generated from OntoLex-Lemon via LexO-server (" + java.time.OffsetDateTime.now().toString() + ")</p>\n");
        w.write("      </sourceDesc>\n");
        w.write("    </fileDesc>\n");
        w.write("  </tbxHeader>\n");
        w.write("\n");
        w.write("  <text>\n");
        w.write("    <body>\n");
    }

    private static void writeTBXFooter(Writer w) throws IOException {
        w.write("    </body>\n");
        w.write("  </text>\n");
        w.write("</tbx>\n");
    }

    private static Map<String, EntryForms> loadEntryForms(RepositoryConnection conn, Set<String> entryIds) throws Exception {
        Map<String, EntryForms> map = new HashMap<>();
        String qForms = SparqlConvertionData.GET_FORMS_LABELS_FOR_BATCH_OF_ENTRIES.replace("_VALUES_", buildFormsQuery(entryIds));
        if (qForms == null) {
            return map;
        }
        TupleQuery tq = conn.prepareTupleQuery(QueryLanguage.SPARQL, qForms);
        try ( TupleQueryResult res = tq.evaluate()) {
            while (res.hasNext()) {
                BindingSet bs = res.next();
                String entryId = RDF4jLoadUtil.getResourceId(bs, "entry");
                String kind = RDF4jLoadUtil.getLiteralString(bs, "kind");
                Value vLit = bs.getValue("lit");
                if (entryId == null || kind == null || !(vLit instanceof Literal)) {
                    continue;
                }
                String text = ((Literal) vLit).getLabel();
                EntryForms ef = map.computeIfAbsent(entryId, k -> new EntryForms());
                ef.add(kind, text);
            }
        }
        return map;
    }

    private static String buildFormsQuery(Collection<String> entryIds) {
        List<String> iris = new ArrayList<>();
        for (String id : entryIds) {
            if (id != null && !id.startsWith("_:")) { // ignore bnode in VALUES
                iris.add(id);
            }
        }
        if (iris.isEmpty()) {
            return null;
        }
        StringBuilder values = new StringBuilder("VALUES ?entry { ");
        for (String iri : iris) {
            values.append("<").append(iri).append("> ");
        }
        values.append("}\n");
        return values.toString();
    }

    private static TermSelection selectTerm(EntryForms ef) {
        List<String> alt = new ArrayList<>();
        String main;
        if (ef != null && !ef.canonical.isEmpty()) {
            main = ef.canonical.get(0);
            if (ef.canonical.size() > 1) {
                alt.addAll(ef.canonical.subList(1, ef.canonical.size()));
            }
            alt.addAll(ef.other);
            alt.addAll(ef.lexical);
            alt.addAll(ef.label);
        } else if (ef != null && !ef.other.isEmpty()) {
            main = ef.other.get(0);
            if (ef.other.size() > 1) {
                alt.addAll(ef.other.subList(1, ef.other.size()));
            }
            alt.addAll(ef.lexical);
            alt.addAll(ef.label);
        } else if (ef != null && !ef.lexical.isEmpty()) {
            main = ef.lexical.get(0);
            if (ef.lexical.size() > 1) {
                alt.addAll(ef.lexical.subList(1, ef.lexical.size()));
            }
            alt.addAll(ef.label);
        } else if (ef != null && !ef.label.isEmpty()) {
            main = ef.label.get(0);
            if (ef.label.size() > 1) {
                alt.addAll(ef.label.subList(1, ef.label.size()));
            }
        } else {
            main = "unknown_term";
        }
        // remove duplicates and the main term from the set of alternative ones
        LinkedHashSet<String> set = new LinkedHashSet<>(alt);
        set.remove(main);
        return new TermSelection(main, new ArrayList<>(set));
    }

//    private static Element buildTermEntry(String idKey,
//            String conceptIdentifierValue,
//            Map<String, LinkedHashSet<TermRecord>> langTerms,
//            List<Relation> relations) throws Exception {
//        String termEntryId = "c_" + StringUtil.sha1Hex(idKey).substring(0, 16);
//        Element termEntry = new Element("termEntry");
//        termEntry.setAttribute(new Attribute("id", termEntryId));
//        if (conceptIdentifierValue != null) {
//            Element conceptDesc = new Element("descrip")
//                    .setAttribute("type", "conceptIdentifier")
//                    .setText(conceptIdentifierValue);
//            termEntry.addContent(conceptDesc);
//        }
//        List<String> langs = new ArrayList<>(langTerms.keySet());
//        Collections.sort(langs);
//        Namespace xmlNs = Namespace.XML_NAMESPACE;
//        for (String lang : langs) {
//            Element langSet = new Element("langSet");
//            langSet.setAttribute("lang", lang, xmlNs);
//            for (TermRecord tr : langTerms.get(lang)) {
//                Element tig = new Element("tig");
//                Element term = new Element("term").setText(tr.term);
//                tig.addContent(term);
//                if (tr.pos != null) {
//                    Element termNotePOS = new Element("termNote")
//                            .setAttribute("type", "partOfSpeech")
//                            .setText(tr.pos);
//                    tig.addContent(termNotePOS);
//                }
//                if (tr.altForms != null) {
//                    for (String alt : tr.altForms) {
//                        if (alt == null || alt.isEmpty()) {
//                            continue;
//                        }
//                        Element altNote = new Element("termNote")
//                                .setAttribute("type", "alternativeForm")
//                                .setText(alt);
//                        tig.addContent(altNote);
//                    }
//                }
//                if (tr.definition != null) {
//                    Element def = new Element("descrip")
//                            .setAttribute("type", "definition")
//                            .setText(tr.definition);
//                    tig.addContent(def);
//                }
//                langSet.addContent(tig);
//            }
//            termEntry.addContent(langSet);
//        }
//        if (relations != null && !relations.isEmpty()) {
//            for (Relation r : relations) {
//                Element relEl = new Element("descrip")
//                        .setAttribute("type", r.type)
//                        .setAttribute("target", "#" + "c_" + StringUtil.sha1Hex(r.relatedConceptIRI).substring(0, 16))
//                        .setText(r.relatedConceptIRI);
//                termEntry.addContent(relEl);
//            }
//        }
//        return termEntry;
//    }
    
    private static org.jdom2.Element buildTermEntry(
        String idKey,
        String conceptIdentifierValue,
        Map<String, LinkedHashSet<TermRecord>> langTerms,
        List<Relation> relations) throws Exception {

    String conceptEntryId = "c_" + StringUtil.sha1Hex(idKey).substring(0, 16);

    // <conceptEntry id="...">
    org.jdom2.Element conceptEntry = new org.jdom2.Element("conceptEntry");
    conceptEntry.setAttribute(new org.jdom2.Attribute("id", conceptEntryId));

    // <descrip type="conceptIdentifier">...</descrip>
    if (conceptIdentifierValue != null) {
        org.jdom2.Element conceptDesc = new org.jdom2.Element("descrip")
                .setAttribute("type", "conceptIdentifier")
                .setText(conceptIdentifierValue);
        conceptEntry.addContent(conceptDesc);
    }

    // langSec (xml:lang) + termSec
    java.util.List<String> langs = new java.util.ArrayList<>(langTerms.keySet());
    java.util.Collections.sort(langs);

    org.jdom2.Namespace xmlNs = org.jdom2.Namespace.XML_NAMESPACE;

    for (String lang : langs) {
        // <langSec xml:lang="xx">
        org.jdom2.Element langSec = new org.jdom2.Element("langSec");
        langSec.setAttribute("lang", lang, xmlNs);

        for (TermRecord tr : langTerms.get(lang)) {
            // <termSec>
            org.jdom2.Element termSec = new org.jdom2.Element("termSec");

            // <term>MAIN</term>
            org.jdom2.Element term = new org.jdom2.Element("term").setText(tr.term);
            termSec.addContent(term);

            // POS come termNote
            if (tr.pos != null) {
                org.jdom2.Element termNotePOS = new org.jdom2.Element("termNote")
                        .setAttribute("type", "partOfSpeech")
                        .setText(tr.pos);
                termSec.addContent(termNotePOS);
            }

            // alternative forms
            if (tr.altForms != null) {
                for (String alt : tr.altForms) {
                    if (alt == null || alt.isEmpty()) continue;
                    org.jdom2.Element altNote = new org.jdom2.Element("termNote")
                            .setAttribute("type", "alternativeForm")
                            .setText(alt);
                    termSec.addContent(altNote);
                }
            }

            // definizione (se presente)
            if (tr.definition != null) {
                org.jdom2.Element def = new org.jdom2.Element("descrip")
                        .setAttribute("type", "definition")
                        .setText(tr.definition);
                termSec.addContent(def);
            }

            langSec.addContent(termSec);
        }
        conceptEntry.addContent(langSec);
    }

    // relazioni (sinonimo/iperonimo) come descrip a livello di conceptEntry
    if (relations != null && !relations.isEmpty()) {
        for (Relation r : relations) {
            org.jdom2.Element relEl = new org.jdom2.Element("descrip")
                    .setAttribute("type", r.type) // "synonym" | "hypernym"
                    .setAttribute("target", "#" + "c_" + StringUtil.sha1Hex(r.relatedConceptIRI).substring(0, 16))
                    .setText(r.relatedConceptIRI);
            conceptEntry.addContent(relEl);
        }
    }

    return conceptEntry;
}

}
