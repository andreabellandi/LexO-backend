/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager.converter.implementation;

import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.util.OntoLexEntity;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
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
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import javax.xml.stream.XMLStreamException;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
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

    private static final int DEFAULT_BATCH = 1000;

    private static final String PFX
            = "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#>\n"
            + "PREFIX lime:    <http://www.w3.org/ns/lemon/lime#>\n"
            + "PREFIX skos:    <http://www.w3.org/2004/02/skos/core#>\n"
            + "PREFIX lexinfo: <http://www.lexinfo.net/ontology/3.0/lexinfo#>\n"
            + "PREFIX vartrans:<http://www.w3.org/ns/lemon/vartrans#>\n"
            + "PREFIX dct:     <http://purl.org/dc/terms/>\n";

    private static final Map<String, String> POS = new HashMap<>();

    static {
        POS.put("http://www.lexinfo.net/ontology/3.0/lexinfo#noun", "noun");
        POS.put("http://www.lexinfo.net/ontology/3.0/lexinfo#verb", "verb");
        POS.put("http://www.lexinfo.net/ontology/3.0/lexinfo#adjective", "adjective");
        POS.put("http://www.lexinfo.net/ontology/3.0/lexinfo#adverb", "adverb");
    }

    private static Set<String> ALLOWED_LANGS = Collections.emptySet();

    // caches
    private static final Map<String, Set<String>> sense2entries = new HashMap<>();
    private static final Map<String, String> senseDef = new HashMap<>();
    private static final Map<String, Set<String>> sense2concepts = new HashMap<>();
    private static final Map<String, String> entryLang = new HashMap<>();
    private static final Map<String, String> entryPos = new HashMap<>();
    private static final Map<String, Set<String>> entry2concepts = new HashMap<>();
    private static final Map<String, Set<String>> senseSubjects = new HashMap<>();
    private static final Map<String, Set<String>> entrySubjects = new HashMap<>();
    private static final Map<String, Set<String>> derivedPropsBySource = new HashMap<>();
    private static final Set<String> ALL_ENTRIES_SEEN = new LinkedHashSet<>();

    private static final Map<String, Set<String>> triplesBySubjectCompact = new HashMap<>();
    private static final Map<String, Set<String>> triplesByObjectCompact = new HashMap<>();

    private static final Set<String> MULTIWORD_ENTRIES = new HashSet<>();

    private static final UnionFind UF = new UnionFind();

    private OntoLexToTBXConverter() {
    }

    public static Result convert(Repository repo,
            Path input,
            Path output,
            Map<String, String> options,
            final IntConsumer onProgress,
            final LongConsumer onProcessed,
            final BooleanSupplier shouldCancel,
            final java.util.function.Consumer<String> onMessage)
            throws Exception {
        long total = Files.size(input);
        if (total <= 0) {
            total = 1;
        }

        if (options.get("type") != null) {
            if (options.get("type").equals("automatic")) {
                automaticConversion(repo, input, output,
                        options.get("langs"),
                        options.get("conceptRDF"),
                        options.get("termRDF"),
                        onProgress,
                        onMessage);
            } else {
                // TODO: interactiveConversion
            }
        } else {
            automaticConversion(repo, input, output,
                    options.get("langs"),
                    options.get("conceptRDF"),
                    options.get("termRDF"),
                    onProgress,
                    onMessage);
        }

        if (onProgress != null) {
            onProgress.accept(100);
        }
        return new Result(output);
    }

    private static void automaticConversion(Repository repo,
            Path input,
            Path output,
            String filteredLanguages,
            String conceptRDF,
            String termRDF,
            final IntConsumer onProgress,
            final java.util.function.Consumer<String> onMessage)
            throws IOException, XMLStreamException, Exception {

        boolean rdfNoteForConcept = "true".equals(conceptRDF);
        boolean rdfNoteForTerm = "true".equals(termRDF);

        if (filteredLanguages != null && !filteredLanguages.trim().isEmpty()) {
            LinkedHashSet<String> s = new LinkedHashSet<>();
            for (String t : filteredLanguages.split(",")) {
                String z = t.trim();
                if (!z.isEmpty()) {
                    s.add(z);
                }
            }
            ALLOWED_LANGS = Collections.unmodifiableSet(s);
        } else {
            ALLOWED_LANGS = Collections.emptySet();
        }

        try ( RepositoryConnection conn = repo.getConnection()) {

            if (onMessage != null) {
                onMessage.accept("Parsing OntoLex-Lemon graph...");
            }
            if (onMessage != null) {
                onMessage.accept("Parsing the imported resource...");
            }
            parseResource(conn, onProgress);
            if (onMessage != null) {
                onMessage.accept("Imported resource parsed.");
            }

            if (onMessage != null) {
                onMessage.accept("Converting components to TBX...");
            }
            writeTBX(conn, output, rdfNoteForConcept, rdfNoteForTerm, onProgress, onMessage);
        }
    }

    private static void parseResource(RepositoryConnection conn, final IntConsumer onProgress) {
        // --- PASSO 1: costruzione componenti ---
        // A) sense con reference
        String lastC = null;
        while (true) {
            String q = PFX
                    + "SELECT ?c ?sense ?entry ?lexLang ?pos ?def WHERE {\n"
                    + "  ?sense ontolex:reference ?c .\n"
                    + "  ?entry ontolex:sense ?sense .\n"
                    + "  ?lex a lime:Lexicon ; lime:entry ?entry ; lime:language ?lexLang .\n"
                    + langFilterClause("?lexLang")
                    + "  OPTIONAL { ?entry lexinfo:partOfSpeech ?pos . }\n"
                    + "  OPTIONAL { ?sense skos:definition ?def . }\n"
                    + (lastC == null ? "" : "  FILTER(STR(?c) > \"" + esc(lastC) + "\")\n")
                    + "} ORDER BY ?c ?sense ?entry LIMIT " + DEFAULT_BATCH;

            TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
            String lastSeen = null;
            Set<String> batchSenses = new LinkedHashSet<>();
            Set<String> batchEntries = new LinkedHashSet<>();
            try {
                while (rs.hasNext()) {
                    BindingSet b = rs.next();
                    String c = iri(b, "c");
                    lastSeen = c;
                    String s = resId(b, "sense");
                    String e = resId(b, "entry");
                    String lang = lit(b, "lexLang");
                    String posUri = iri(b, "pos");
                    String def = lit(b, "def");
                    if (c == null || s == null || e == null || lang == null) {
                        continue;
                    }

                    batchSenses.add(s);
                    batchEntries.add(e);

                    UF.union(S(s), C(c));
                    UF.union(S(s), E(e));

                    sense2entries.computeIfAbsent(s, k -> new LinkedHashSet<>()).add(e);
                    if (def != null && !senseDef.containsKey(s)) {
                        senseDef.put(s, def);
                    }
                    sense2concepts.computeIfAbsent(s, k -> new LinkedHashSet<>()).add(c);
                    entryLang.put(e, lang);
                    if (posUri != null && !entryPos.containsKey(e)) {
                        entryPos.put(e, POS.getOrDefault(posUri, shrink(posUri)));
                    }
                    ALL_ENTRIES_SEEN.add(e);
                }
            } finally {
                rs.close();
            }
            if (lastSeen == null) {
                break;
            }
            lastC = lastSeen;

            addSenseEdgesTranslation(conn, batchSenses);
            addSenseEdgesVartrans(conn, batchSenses);

            addEntryEdgesTranslatableAs(conn, batchEntries);
            addEntryEdgesDerived_BY_TARGET(conn, batchEntries);

            addSubjectsForSenses(conn, batchSenses);
            addSubjectsForEntries(conn, batchEntries);
        }

        // B) sense senza reference
        String lastS = null;
        while (true) {
            String q = PFX
                    + "SELECT ?sense ?entry ?lexLang ?pos ?def WHERE {\n"
                    + "  ?entry ontolex:sense ?sense .\n"
                    + "  ?lex a lime:Lexicon ; lime:entry ?entry ; lime:language ?lexLang .\n"
                    + langFilterClause("?lexLang")
                    + "  FILTER NOT EXISTS { ?sense ontolex:reference ?c . }\n"
                    + "  OPTIONAL { ?entry lexinfo:partOfSpeech ?pos . }\n"
                    + "  OPTIONAL { ?sense skos:definition ?def . }\n"
                    + (lastS == null ? "" : "  FILTER(STR(?sense) > \"" + esc(lastS) + "\")\n")
                    + "} ORDER BY ?sense ?entry LIMIT " + DEFAULT_BATCH;

            TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
            String lastSeen = null;
            Set<String> batchSenses = new LinkedHashSet<>();
            Set<String> batchEntries = new LinkedHashSet<>();
            try {
                while (rs.hasNext()) {
                    BindingSet b = rs.next();
                    String s = resId(b, "sense");
                    lastSeen = s;
                    String e = resId(b, "entry");
                    String lang = lit(b, "lexLang");
                    String pos = iri(b, "pos");
                    String def = lit(b, "def");
                    if (s == null || e == null || lang == null) {
                        continue;
                    }
                    batchSenses.add(s);
                    batchEntries.add(e);
                    UF.union(S(s), E(e));
                    sense2entries.computeIfAbsent(s, k -> new LinkedHashSet<>()).add(e);
                    if (def != null && !senseDef.containsKey(s)) {
                        senseDef.put(s, def);
                    }
                    entryLang.put(e, lang);
                    if (pos != null && !entryPos.containsKey(e)) {
                        entryPos.put(e, POS.getOrDefault(pos, shrink(pos)));
                    }
                    ALL_ENTRIES_SEEN.add(e);
                }
            } finally {
                rs.close();
            }
            if (lastSeen == null) {
                break;
            }
            lastS = lastSeen;
            addSenseEdgesSynonym(conn, batchSenses);
            addSenseEdgesTranslation(conn, batchSenses);
            addSenseEdgesVartrans(conn, batchSenses);
            addEntryEdgesTranslatableAs(conn, batchEntries);
            addEntryEdgesDerived_BY_TARGET(conn, batchEntries);
            addSubjectsForSenses(conn, batchSenses);
            addSubjectsForEntries(conn, batchEntries);
        }
        // C) entry con denotes ma senza sensi
        lastC = null;
        while (true) {
            String q = PFX
                    + "SELECT ?c ?entry ?lexLang ?pos WHERE {\n"
                    + "  ?entry ontolex:denotes ?c .\n"
                    + "  FILTER NOT EXISTS { ?entry ontolex:sense ?s . }\n"
                    + "  ?lex a lime:Lexicon ; lime:entry ?entry ; lime:language ?lexLang .\n"
                    + langFilterClause("?lexLang")
                    + "  OPTIONAL { ?entry lexinfo:partOfSpeech ?pos . }\n"
                    + (lastC == null ? "" : "  FILTER(STR(?c) > \"" + esc(lastC) + "\")\n")
                    + "} ORDER BY ?c ?entry LIMIT " + DEFAULT_BATCH;
            TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
            String lastSeen = null;
            Set<String> batchEntries = new LinkedHashSet<>();
            try {
                while (rs.hasNext()) {
                    BindingSet b = rs.next();
                    String c = iri(b, "c");
                    lastSeen = c;
                    String e = resId(b, "entry");
                    String lang = lit(b, "lexLang");
                    String pos = iri(b, "pos");
                    if (c == null || e == null || lang == null) {
                        continue;
                    }
                    batchEntries.add(e);
                    UF.union(E(e), C(c));
                    entryLang.put(e, lang);
                    if (pos != null && !entryPos.containsKey(e)) {
                        entryPos.put(e, POS.getOrDefault(pos, shrink(pos)));
                    }
                    entry2concepts.computeIfAbsent(e, k -> new LinkedHashSet<>()).add(c);
                    ALL_ENTRIES_SEEN.add(e);
                }
            } finally {
                rs.close();
            }
            if (lastSeen == null) {
                break;
            }
            lastC = lastSeen;
            addEntryEdgesTranslatableAs(conn, batchEntries);
            addEntryEdgesDerived_BY_TARGET(conn, batchEntries);
            addSubjectsForEntries(conn, batchEntries);
        }
        onProgress.accept(20);

    }

    private static void writeTBX(RepositoryConnection conn,
            Path output,
            boolean rdfNoteForConcept,
            boolean rdfNoteForTerm,
            final IntConsumer onProgress,
            final java.util.function.Consumer<String> onMessage) throws IOException, Exception {

        // Range scelti:
        //  - 20–60 : preload (EntryForms) in funzione del numero di entries
        //  - 60–80 : scrittura conceptEntry in funzione del numero di component
        final int RANGE_PRELOAD_START = 20;
        final int RANGE_PRELOAD_END = 60;
        final int RANGE_LOOP_START = 60;
        final int RANGE_LOOP_END = 80;

        if (onProgress != null) {
            onProgress.accept(RANGE_PRELOAD_START); // inizio fase TBX
        }

        // 1) raccogli tutte le entries
        Set<String> allEntries = new LinkedHashSet<>();
        for (Set<String> es : sense2entries.values()) {
            allEntries.addAll(es);
        }
        allEntries.addAll(entry2concepts.keySet());
        allEntries.addAll(ALL_ENTRIES_SEEN);
        if (onMessage != null) {
            onMessage.accept("Terms collected.");
        }
        // 2) costruisci i component (come prima)
        Map<String, Component> comps = new LinkedHashMap<>();
        for (String s : sense2entries.keySet()) {
            String compId = UF.findNode(S(s));
            Component comp = comps.computeIfAbsent(compId, k -> new Component());
            comp.senses.add(s);
            comp.entries.addAll(sense2entries.get(s));
            Set<String> cs = sense2concepts.get(s);
            if (cs != null) {
                comp.concepts.addAll(cs);
            }
        }
        for (String e : allEntries) {
            String compId = UF.findNode(E(e));
            Component comp = comps.computeIfAbsent(compId, k -> new Component());
            comp.entries.add(e);
            Set<String> cs = entry2concepts.get(e);
            if (cs != null) {
                comp.concepts.addAll(cs);
            }
        }
        if (onMessage != null) {
            onMessage.accept("Components built.");
        }
        if (onMessage != null) {
            onMessage.accept("Indexing definitions and POS...");
        }
        // 3) PRE-CALCOLO: definizioni per entry (entry -> def), una sola volta
        Map<String, String> entry2Def = buildEntryDefinitionIndex();

        // 4) PRE-CALCOLO: lingua/pos per tutte le entries (una sola volta)
        ensureEntryLangPos(conn, allEntries);
        if (onMessage != null) {
            onMessage.accept("Definitions and POS indexed");
        }
        if (onMessage != null) {
            onMessage.accept("Preloading entry forms...");
        }
        // 5) PRE-CALCOLO: tutte le EntryForms per tutte le entries (batch)
        //    -> qui distribuiamo il progresso da 20 a 60 in modo uniforme in base alle entries
        Map<String, EntryForms> allEntryForms
                = preloadEntryForms(conn, allEntries, onProgress,
                        RANGE_PRELOAD_START, RANGE_PRELOAD_END, onMessage);
        if (onMessage != null) {
            onMessage.accept("Entry forms loaded");
        }
        // 6) scrittura TBX (JDOM2 in streaming) con progress 60–80 sui component
        final int totalComps = comps.size();
        int processedComps = 0;
        int lastLoopProgress = RANGE_LOOP_START;

        try ( OutputStream os = new FileOutputStream(output.toString());  java.io.BufferedWriter w = new java.io.BufferedWriter(
                new OutputStreamWriter(os, StandardCharsets.UTF_8),
                1 << 16 /*64 KiB*/)) {

            Format fmt = Format.getPrettyFormat();
            fmt.setIndent("  ");
            fmt.setLineSeparator(System.lineSeparator());
            fmt.setOmitDeclaration(true);
            XMLOutputter xo = new XMLOutputter(fmt);
            final String nl = System.lineSeparator();

            writeTBXHeader(w);

            if (totalComps == 0) {
                if (onProgress != null) {
                    onProgress.accept(100);
                }
            } else {
                // garantiamo che prima del loop siamo almeno a RANGE_LOOP_START
                if (onProgress != null) {
                    onProgress.accept(RANGE_LOOP_START);
                    lastLoopProgress = RANGE_LOOP_START;
                }
                for (Map.Entry<String, Component> kv : comps.entrySet()) {
                    Component comp = kv.getValue();
                    String conceptId = comp.concepts.isEmpty()
                            ? ("pseudo:group:" + kv.getKey())
                            : minLex(comp.concepts);

                    // lang -> set TermRecord
                    Map<String, LinkedHashSet<TermRecord>> langTerms = new LinkedHashMap<>();
                    Map<String, LinkedHashSet<String>> langSenses = new LinkedHashMap<>();
                    Map<String, LinkedHashSet<String>> langEntries = new LinkedHashMap<>();

                    // --- costruzione langTerms + tracciamento entries per lingua ---
                    for (String e : comp.entries) {
                        String lang = entryLang.get(e);
                        if (lang == null) {
                            continue;
                        }
                        if (!ALLOWED_LANGS.isEmpty() && !ALLOWED_LANGS.contains(lang)) {
                            continue;
                        }
                        String pos = entryPos.get(e);
                        String def = entry2Def.get(e); // pre-calcolata, O(1)

                        EntryForms forms = allEntryForms.get(e);
                        TermSelection sel = selectTerm(forms);

                        List<String> dprops = new ArrayList<>(
                                derivedPropsBySource.getOrDefault(e, Collections.<String>emptySet()));

                        // multiword = true se l'entry è ontolex:MultiwordExpression
                        boolean isMultiword = MULTIWORD_ENTRIES.contains(e);

                        langTerms
                                .computeIfAbsent(lang, k -> new LinkedHashSet<TermRecord>())
                                .add(new TermRecord(sel.mainTerm, pos, def, sel.altForms,
                                        sel.isFromOntoLexForm, sel.genders, sel.numbers,
                                        dprops, isMultiword));

                        // entries per lingua per le note ontolex model
                        langEntries
                                .computeIfAbsent(lang, k -> new LinkedHashSet<String>())
                                .add(e);
                    }

                    // --- senses per lingua ---
                    for (String s : comp.senses) {
                        Set<String> es = sense2entries.get(s);
                        if (es == null) {
                            continue;
                        }
                        for (String e : es) {
                            String lang = entryLang.get(e);
                            if (lang == null) {
                                continue;
                            }
                            if (!ALLOWED_LANGS.isEmpty() && !ALLOWED_LANGS.contains(lang)) {
                                continue;
                            }
                            langSenses
                                    .computeIfAbsent(lang, k -> new LinkedHashSet<String>())
                                    .add(s);
                            langEntries
                                    .computeIfAbsent(lang, k -> new LinkedHashSet<String>())
                                    .add(e);
                        }
                    }

                    // --- subjects aggregati ---
                    LinkedHashSet<String> subjects = new LinkedHashSet<>();
                    for (String s : comp.senses) {
                        Set<String> ss = senseSubjects.get(s);
                        if (ss != null) {
                            subjects.addAll(ss);
                        }
                    }
                    for (String e : comp.entries) {
                        Set<String> es = entrySubjects.get(e);
                        if (es != null) {
                            subjects.addAll(es);
                        }
                    }

                    // --- costruzione elemento JDOM e scrittura ---
                    Element ce = buildConceptEntry(conn, conceptId, conceptId,
                            langTerms, subjects,
                            comp.concepts, comp.senses, rdfNoteForConcept);

                    injectLangSecOntoLexNotes(conn, ce,
                            langSenses, langEntries,
                            langTerms.keySet(), rdfNoteForTerm);

                    xo.output(ce, w);
                    w.write(nl);

                    // aggiorna progress in base al numero di component già scritti (60–80)
                    processedComps++;
                    if (onMessage != null && processedComps % 100 == 0) {
                        onMessage.accept("Writing TBX entries... (" + processedComps + "/" + totalComps + ")");
                    }
                    if (onProgress != null && totalComps > 0) {
                        int p = RANGE_LOOP_START
                                + (int) ((long) (RANGE_LOOP_END - RANGE_LOOP_START)
                                * processedComps / totalComps);
                        if (p > lastLoopProgress) {
                            onProgress.accept(p);
                            lastLoopProgress = p;
                        }
                    }
                }
            }

            writeTBXFooter(w);
            w.flush();
        }

        // fine: mettiamo 100%
        if (onProgress != null) {
            onProgress.accept(100);
            if (onMessage != null) {
                onMessage.accept("Writing TBX entries... (" + processedComps + "/" + totalComps + ")");
            }
        }
    }

    /**
     * Costruisce una mappa entry -> definizione prendendo la prima definizione
     * disponibile fra i sensi collegati a quell'entry. È l'equivalente
     * vettoriale di pickAnyDefForEntry(e), ma O(#senses + #entries) invece di
     * O(#entries * #senses).
     */
    private static Map<String, String> buildEntryDefinitionIndex() {
        Map<String, String> entry2Def = new HashMap<>();
        for (Map.Entry<String, Set<String>> kv : sense2entries.entrySet()) {
            String sense = kv.getKey();
            String def = senseDef.get(sense);
            if (def == null) {
                continue;
            }
            for (String e : kv.getValue()) {
                // mantieni la prima definizione trovata (stessa semantica di pickAnyDefForEntry)
                entry2Def.putIfAbsent(e, def);
            }
        }
        return entry2Def;
    }

    /**
     * Precarica tutte le EntryForms per l'insieme di entries dato, usando
     * loadEntryForms in chunk per tenere piccola la clausola VALUES.
     */
    /**
     * Precarica tutte le EntryForms per l'insieme di entries dato, usando
     * loadEntryForms in chunk per tenere piccola la clausola VALUES e
     * aggiornando onProgress nel range [startProgress, endProgress].
     */
    /**
     * Precarica tutte le EntryForms per l'insieme di entries dato, usando
     * loadEntryForms in chunk per tenere piccola la clausola VALUES e
     * aggiornando onProgress nel range [startProgress, endProgress].
     */
    private static Map<String, EntryForms> preloadEntryForms(RepositoryConnection conn,
            Set<String> allEntries,
            IntConsumer onProgress,
            int startProgress,
            int endProgress,
            final java.util.function.Consumer<String> onMessage) throws Exception {
        Map<String, EntryForms> result = new HashMap<>();
        if (allEntries.isEmpty()) {
            if (onProgress != null) {
                onProgress.accept(endProgress);
            }
            return result;
        }

        final int CHUNK = 1000;
        List<String> list = new ArrayList<>(allEntries);
        final int total = list.size();
        int processed = 0;
        int lastProgress = startProgress;

        for (int i = 0; i < total; i += CHUNK) {
            int end = Math.min(total, i + CHUNK);
            Set<String> chunk = new LinkedHashSet<>(list.subList(i, end));

            Map<String, EntryForms> partial = loadEntryForms(conn, chunk);
            if (partial != null && !partial.isEmpty()) {
                result.putAll(partial);
            }

            processed += (end - i);

            if (onProgress != null && endProgress > startProgress) {
                int p = startProgress
                        + (int) ((long) (endProgress - startProgress) * processed / total);
                if (p > lastProgress) {
                    onProgress.accept(p);
                    lastProgress = p;
                }
                if (onMessage != null) {
                    onMessage.accept("Analyzing entry forms... (" + i + "/" + total + ")");
                }
            }
        }

        if (onProgress != null && lastProgress < endProgress) {
            onProgress.accept(endProgress);
        }

        return result;
    }


    /* ===================== TBX ed-2 ===================== */
    private static void writeTBXHeader(Writer w) throws IOException {
        w.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        w.write("<?xml-model href=\"https://raw.githubusercontent.com/LTAC-Global/TBX-Basic_dialect/master/DCT/TBX-Basic_DCT.sch\" type=\"application/xml\" schematypens=\"http://purl.oclc.org/dsdl/schematron\"?>\n");
        w.write("<?xml-model href=\"https://raw.githubusercontent.com/LTAC-Global/TBX-Basic_dialect/master/DCT/TBX-Basic.nvdl\" type=\"application/xml\" schematypens=\"http://purl.oclc.org/dsdl/nvdl/ns/structure/1.0\"?>\n");
        w.write("<tbx xmlns=\"urn:iso:std:iso:30042:ed-2\" type=\"TBX-Basic\" style=\"dct\" xml:lang=\"en\" xmlns:min=\"http://www.tbxinfo.net/ns/min\" xmlns:basic=\"http://www.tbxinfo.net/ns/basic\">\n");
        w.write("  <tbxHeader>\n");
        w.write("    <fileDesc>\n");
        w.write("      <sourceDesc>\n");
        w.write("        <p>Generated from OntoLex-Lemon (Union-Find globale) " + OffsetDateTime.now() + "</p>\n");
        w.write("      </sourceDesc>\n");
        w.write("    </fileDesc>\n");
        w.write("  </tbxHeader>\n");
        w.write("  <text>\n");
        w.write("    <body>\n");
    }

    private static void writeTBXFooter(Writer w) throws IOException {
        w.write("    </body>\n");
        w.write("  </text>\n");
        w.write("</tbx>\n");
    }

    /**
     * Costruisce la conceptEntry e inserisce: - descrip conceptIdentifier -
     * subjectField - langSec (con definizioni a livello di lang) - note
     * (type="ontolex model") a livello di conceptEntry con: * "sense
     * ontolex:reference concept" * triple dove concept è soggetto/oggetto
     */
    private static Element buildConceptEntry(RepositoryConnection conn,
            String idKey,
            String conceptIdentifierValue,
            Map<String, LinkedHashSet<TermRecord>> langTerms,
            Collection<String> subjects,
            Collection<String> conceptsIRIs,
            Collection<String> sensesIRIs,
            boolean rdfNoteForConcept) throws Exception {
        Element conceptEntry = new Element("conceptEntry");
        conceptEntry.setAttribute(new Attribute("id", "c_" + sha1Hex(idKey).substring(0, 16)));

        conceptEntry.addContent(new Element("descrip")
                .setAttribute("type", "conceptIdentifier").setText(conceptIdentifierValue));

        Namespace minNs = Namespace.getNamespace("min", "http://www.tbxinfo.net/ns/min");
        if (subjects != null) {
            for (String s : subjects) {
                if (s == null || s.trim().isEmpty()) {
                    continue;
                }
                conceptEntry.addContent(new Element("subjectField", minNs).setText(s));
            }
        }

        // ===== NOTE ONTOLEX MODEL (concept-level) =====
        if (rdfNoteForConcept) {
            Prefixer px = new Prefixer();
            LinkedHashSet<String> lines = new LinkedHashSet<>();
            // 1) linea <sense> ontolex:reference <concept>
            if (conceptsIRIs != null && sensesIRIs != null) {
                for (String c : conceptsIRIs) {
                    for (String s : sensesIRIs) {
                        Set<String> cs = sense2concepts.get(s);
                        if (cs != null && cs.contains(c)) {
                            lines.add(turtleLineCompact(px, s, "http://www.w3.org/ns/lemon/ontolex#reference", c));
                        }
                    }
                }
            }
            // 2) batch prefetch: tutte le triple con S=concept e tutte le triple con O=concept
            if (conceptsIRIs != null && !conceptsIRIs.isEmpty()) {
                prefetchTriplesBySubjectCompact(conn, px, new LinkedHashSet<>(conceptsIRIs));
                prefetchTriplesByObjectCompact(conn, px, new LinkedHashSet<>(conceptsIRIs));
                for (String c : conceptsIRIs) {
                    lines.addAll(triplesBySubjectCompact.getOrDefault(c, Collections.emptySet()));
                    lines.addAll(triplesByObjectCompact.getOrDefault(c, Collections.emptySet()));
                }
            }
            if (!lines.isEmpty()) {
                Element note = new Element("note").setAttribute("type", "ontolex model");
                setPrettyBlockText(note, px.buildPrefixHeader() + joinLines(lines));
                conceptEntry.addContent(note);
            }
        }
        // ==============================================

        List<String> langs = new ArrayList<>(langTerms.keySet());
        Collections.sort(langs);
        if (!ALLOWED_LANGS.isEmpty()) {
            langs.removeIf(l -> !ALLOWED_LANGS.contains(l));
        }
        Namespace xmlNs = Namespace.XML_NAMESPACE;

        for (String lang : langs) {
            Element langSec = new Element("langSec");
            langSec.setAttribute("lang", lang, xmlNs);

            // definizioni a livello di lingua
            LinkedHashSet<String> defs = new LinkedHashSet<>();
            for (TermRecord tr : langTerms.get(lang)) {
                if (tr.definition != null && !tr.definition.trim().isEmpty()) {
                    defs.add(tr.definition);
                }
            }
            for (String d : defs) {
                langSec.addContent(new Element("descrip").setAttribute("type", "definition").setText(d));
            }

            // i termSec
            for (TermRecord tr : langTerms.get(lang)) {
                Element termSec = new Element("termSec");

                // <term> o <term type="multi-word term">
                Element termEl = new Element("term").setText(tr.term);
                if (tr.multiword) {
                    termEl.setAttribute("type", "multi-word term");
                }
                termSec.addContent(termEl);

                if (tr.pos != null) {
                    termSec.addContent(new Element("termNote")
                            .setAttribute("type", "partOfSpeech")
                            .setText(tr.pos));
                }
                for (String alt : tr.altForms) {
                    if (alt == null || alt.isEmpty()) {
                        continue;
                    }
                    termSec.addContent(new Element("termNote").setAttribute("type", "alternativeForm").setText(alt));
                }
                if (tr.fromOntoLexForm) {
                    for (String g : tr.genders) {
                        if (g != null && !g.isEmpty()) {
                            termSec.addContent(new Element("grammaticalGender").setText(g));
                        }
                    }
                    for (String n : tr.numbers) {
                        if (n != null && !n.isEmpty()) {
                            termSec.addContent(new Element("grammaticalNumber").setText(n));
                        }
                    }
                }
                for (String df : tr.derivedProps) {
                    if (df != null && !df.isEmpty()) {
                        termSec.addContent(new Element("note").setAttribute("type", "derivedForm").setText(df));
                    }
                }
                langSec.addContent(termSec);
            }

            conceptEntry.addContent(langSec);
        }
        return conceptEntry;
    }

    /**
     * Inserisce in ciascuna langSec una <note type="ontolex model"> con: -
     * triple di tutti i sensi della lingua (soggetto/oggetto) - triple di tutte
     * le entry della lingua (soggetto/oggetto)
     */
    private static void injectLangSecOntoLexNotes(RepositoryConnection conn,
            Element conceptEntry,
            Map<String, LinkedHashSet<String>> langSenses,
            Map<String, LinkedHashSet<String>> langEntries,
            Collection<String> langsOrder,
            boolean rdfNoteForTerm) {
        // indicizza langSec già create
        Map<String, Element> byLang = new HashMap<>();
        for (Element child : conceptEntry.getChildren("langSec")) {
            String l = child.getAttributeValue("lang", Namespace.XML_NAMESPACE);
            if (l != null) {
                byLang.put(l, child);
            }
        }
        for (String lang : langsOrder) {
            Element langSec = byLang.get(lang);
            if (langSec == null) {
                continue;
            }

            Prefixer px = new Prefixer();
            LinkedHashSet<String> lines = new LinkedHashSet<>();

            LinkedHashSet<String> senseSet = new LinkedHashSet<>(langSenses.getOrDefault(lang, new LinkedHashSet<String>()));
            LinkedHashSet<String> entrySet = new LinkedHashSet<>(langEntries.getOrDefault(lang, new LinkedHashSet<String>()));
            LinkedHashSet<String> allIris = new LinkedHashSet<>();
            for (String s : senseSet) {
                if (s != null && !s.startsWith("_:")) {
                    allIris.add(s);
                }
            }
            for (String e : entrySet) {
                if (e != null && !e.startsWith("_:")) {
                    allIris.add(e);
                }
            }
            if ((rdfNoteForTerm) && !allIris.isEmpty()) {
                // Batch prefetch
                prefetchTriplesBySubjectCompact(conn, px, allIris);
                prefetchTriplesByObjectCompact(conn, px, allIris);
                // Merge O(1)
                for (String id : allIris) {
                    lines.addAll(triplesBySubjectCompact.getOrDefault(id, Collections.emptySet()));
                    lines.addAll(triplesByObjectCompact.getOrDefault(id, Collections.emptySet()));
                }
            }

            if ((rdfNoteForTerm) && !lines.isEmpty()) {
                Element note = new Element("note").setAttribute("type", "ontolex model");
                setPrettyBlockText(note, px.buildPrefixHeader() + joinLines(lines));
                langSec.addContent(0, note);
            }
        }
    }

    /* ===================== Query helpers: edges, subjects, derived ===================== */
    private static String langFilterClause(String var) {
        if (ALLOWED_LANGS.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("  FILTER(").append(var).append(" IN (");
        boolean first = true;
        for (String l : ALLOWED_LANGS) {
            if (!first) {
                sb.append(", ");
            }
            sb.append("\"").append(esc(l)).append("\"");
            first = false;
        }
        sb.append("))\n");
        return sb.toString();
    }

    private static void addSenseEdgesTranslation(RepositoryConnection conn, Collection<String> batchSenses) {
        if (batchSenses.isEmpty()) {
            return;
        }
        String values = valuesIRI("s1", batchSenses);
        String q = PFX
                + "SELECT ?s1 ?s2 WHERE { " + values
                + "  { ?s1 lexinfo:translation ?s2 } UNION { ?s2 lexinfo:translation ?s1 }\n"
                + "}";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                UF.union(S(iri(b, "s1")), S(iri(b, "s2")));
            }
        } finally {
            rs.close();
        }
    }

    private static void addSenseEdgesVartrans(RepositoryConnection conn, Collection<String> batchSenses) {
        if (batchSenses.isEmpty()) {
            return;
        }
        String values = valuesIRI("s", batchSenses);
        String q = PFX
                + "SELECT ?s1 ?s2 WHERE {\n"
                + "  ?t a vartrans:Translation .\n"
                + "  { ?t vartrans:source ?s1 . ?t vartrans:target ?s2 . FILTER(?s1 IN (?s)) }\n"
                + "  UNION { ?t vartrans:source ?s2 . ?t vartrans:target ?s1 . FILTER(?s2 IN (?s)) }\n"
                + "}";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                UF.union(S(iri(b, "s1")), S(iri(b, "s2")));
            }
        } finally {
            rs.close();
        }
    }

    private static void addSenseEdgesSynonym(RepositoryConnection conn, Collection<String> batchSenses) {
        if (batchSenses.isEmpty()) {
            return;
        }
        List<String> iris = new ArrayList<>();
        for (String s : batchSenses) {
            if (!s.startsWith("_:")) {
                iris.add(s);
            }
        }
        if (iris.isEmpty()) {
            return;
        }
        String v = valuesIRI("s1", iris);
        String q = PFX
                + "SELECT ?s1 ?s2 WHERE { " + v
                + "  { ?s1 lexinfo:synonym ?s2 } UNION { ?s2 lexinfo:synonym ?s1 }\n"
                + "}";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                UF.union(S(iri(b, "s1")), S(iri(b, "s2")));
            }
        } finally {
            rs.close();
        }
    }

    private static void addEntryEdgesTranslatableAs(RepositoryConnection conn, Collection<String> batchEntries) {
        if (batchEntries.isEmpty()) {
            return;
        }
        List<String> iris = new ArrayList<>();
        for (String e : batchEntries) {
            if (!e.startsWith("_:")) {
                iris.add(e);
            }
        }
        if (iris.isEmpty()) {
            return;
        }
        String v = valuesIRI("e1", iris);
        String q = PFX
                + "SELECT ?e1 ?e2 WHERE { " + v
                + "  { ?e1 vartrans:translatableAs ?e2 } UNION { ?e2 vartrans:translatableAs ?e1 }\n"
                + "}";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                String e1 = iri(b, "e1");
                String e2 = iri(b, "e2");
                UF.union(E(e1), E(e2));
                ALL_ENTRIES_SEEN.add(e1);
                ALL_ENTRIES_SEEN.add(e2);
            }
        } finally {
            rs.close();
        }
    }

    private static void addEntryEdgesDerived_BY_TARGET(RepositoryConnection conn, Collection<String> batchEntries) {
        if (batchEntries.isEmpty()) {
            return;
        }
        List<String> iris = new ArrayList<>();
        for (String e : batchEntries) {
            if (!e.startsWith("_:")) {
                iris.add(e);
            }
        }
        if (iris.isEmpty()) {
            return;
        }

        String v = valuesIRI("tgt", iris);
        String q = PFX
                + "SELECT ?src ?tgt ?p WHERE { " + v
                + "  { ?src lexinfo:clippedTermFor   ?tgt BIND(\"clippedTermFor\"   AS ?p) } UNION\n"
                + "  { ?src lexinfo:contractionFor   ?tgt BIND(\"contractionFor\"   AS ?p) } UNION\n"
                + "  { ?src lexinfo:abbreviationFor  ?tgt BIND(\"abbreviationFor\"  AS ?p) } UNION\n"
                + "  { ?src lexinfo:acronymFor       ?tgt BIND(\"acronymFor\"       AS ?p) } UNION\n"
                + "  { ?src lexinfo:initialismFor    ?tgt BIND(\"initialismFor\"    AS ?p) } UNION\n"
                + "  { ?src lexinfo:shortFormFor     ?tgt BIND(\"shortFormFor\"     AS ?p) }\n"
                + "}";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                String src = resId(b, "src");
                String tgt = resId(b, "tgt");
                String p = lit(b, "p");
                if (src == null || tgt == null || p == null) {
                    continue;
                }
                UF.union(E(src), E(tgt));
                derivedPropsBySource.computeIfAbsent(src, k -> new LinkedHashSet<>()).add(p);
                ALL_ENTRIES_SEEN.add(src);
                ALL_ENTRIES_SEEN.add(tgt);
            }
        } finally {
            rs.close();
        }
    }

    private static void addSubjectsForSenses(RepositoryConnection conn, Collection<String> batchSenses) {
        if (batchSenses.isEmpty()) {
            return;
        }
        List<String> iris = new ArrayList<>();
        for (String s : batchSenses) {
            if (!s.startsWith("_:")) {
                iris.add(s);
            }
        }
        if (iris.isEmpty()) {
            return;
        }

        String v = valuesIRI("s", iris);
        String q = PFX + "SELECT ?s ?sub WHERE { " + v + " ?s dct:subject ?sub . }";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                String s = iri(b, "s");
                Value sub = b.getValue("sub");
                if (s == null || sub == null) {
                    continue;
                }
                String txt = valueToString(sub);
                if (txt == null || txt.trim().isEmpty()) {
                    continue;
                }
                senseSubjects.computeIfAbsent(s, k -> new LinkedHashSet<>()).add(txt);
            }
        } finally {
            rs.close();
        }
    }

    private static void addSubjectsForEntries(RepositoryConnection conn, Collection<String> batchEntries) {
        if (batchEntries.isEmpty()) {
            return;
        }
        List<String> iris = new ArrayList<>();
        for (String e : batchEntries) {
            if (!e.startsWith("_:")) {
                iris.add(e);
            }
        }
        if (iris.isEmpty()) {
            return;
        }

        String v = valuesIRI("e", iris);
        String q = PFX + "SELECT ?e ?sub WHERE { " + v + " ?e dct:subject ?sub . }";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                String e = iri(b, "e");
                Value sub = b.getValue("sub");
                if (e == null || sub == null) {
                    continue;
                }
                String txt = valueToString(sub);
                if (txt == null || txt.trim().isEmpty()) {
                    continue;
                }
                entrySubjects.computeIfAbsent(e, k -> new LinkedHashSet<>()).add(txt);
            }
        } finally {
            rs.close();
        }
    }

    /* ===================== Triple fetchers & formatting ===================== */
    private static Set<String> fetchTriplesSubject(RepositoryConnection conn, String iri) {
        LinkedHashSet<String> lines = new LinkedHashSet<>();
        if (iri == null || iri.startsWith("_:")) {
            return lines;
        }
        String q = PFX
                + "SELECT ?p ?o WHERE { <" + escIri(iri) + "> ?p ?o }";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                String p = iri(b, "p");
                Value o = b.getValue("o");
                lines.add(turtleLine(iri, p, o));
            }
        } finally {
            rs.close();
        }
        return lines;
    }

    private static Set<String> fetchTriplesObject(RepositoryConnection conn, String iri) {
        LinkedHashSet<String> lines = new LinkedHashSet<>();
        if (iri == null || iri.startsWith("_:")) {
            return lines;
        }
        String q = PFX
                + "SELECT ?s ?p WHERE { ?s ?p <" + escIri(iri) + "> }";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                String s = iri(b, "s");
                String p = iri(b, "p");
                if (s != null && p != null) {
                    lines.add(turtleLine(s, p, iri));
                }
            }
        } finally {
            rs.close();
        }
        return lines;
    }

    private static String turtleLine(String sIri, String pIri, String oIri) {
        return "<" + sIri + "> <" + pIri + "> <" + oIri + "> .";
    }

    private static String turtleLine(String sIri, String pIri, Value o) {
        return "<" + sIri + "> <" + pIri + "> " + valueTerm(o) + " .";
    }

    private static String valueTerm(Value v) {
        if (v instanceof IRI) {
            return "<" + ((IRI) v).stringValue() + ">";
        }
        if (v instanceof BNode) {
            return "_:" + ((BNode) v).getID();
        }
        Literal lit = (Literal) v;
        String label = escapeLiteral(lit.getLabel());
        if (lit.getLanguage().isPresent()) {
            return "\"" + label + "\"@" + lit.getLanguage().get();
        }
        IRI dt = lit.getDatatype();
        if (dt != null && !XMLSchema.STRING.equals(dt)) {
            return "\"" + label + "\"^^<" + dt.stringValue() + ">";
        }
        return "\"" + label + "\"";
    }

    private static String escapeLiteral(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    private static String escIri(String s) {
        return s.replace("\\", "\\\\").replace(">", "%3E").replace("<", "%3C").replace("\"", "%22");
    }

    private static Set<String> fetchTriplesSubjectCompact(RepositoryConnection conn, Prefixer px, String iri) {
        LinkedHashSet<String> lines = new LinkedHashSet<>();
        if (iri == null || iri.startsWith("_:")) {
            return lines;
        }
        String q = PFX + "SELECT ?p ?o WHERE { <" + escIri(iri) + "> ?p ?o }";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                String p = iri(b, "p");
                Value o = b.getValue("o");
                lines.add(turtleLineCompact(px, iri, p, o));
            }
        } finally {
            rs.close();
        }
        return lines;
    }

    private static Set<String> fetchTriplesObjectCompact(RepositoryConnection conn, Prefixer px, String iri) {
        LinkedHashSet<String> lines = new LinkedHashSet<>();
        if (iri == null || iri.startsWith("_:")) {
            return lines;
        }
        String q = PFX + "SELECT ?s ?p WHERE { ?s ?p <" + escIri(iri) + "> }";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                String s = iri(b, "s");
                String p = iri(b, "p");
                if (s != null && p != null) {
                    lines.add(turtleLineCompact(px, s, p, iri));
                }
            }
        } finally {
            rs.close();
        }
        return lines;
    }

    private static String turtleLineCompact(Prefixer px, String sIri, String pIri, String oIri) {
        return px.compactIri(sIri) + " " + px.compactIri(pIri) + " " + px.compactIri(oIri) + " .";
    }

    private static String turtleLineCompact(Prefixer px, String sIri, String pIri, Value o) {
        return px.compactIri(sIri) + " " + px.compactIri(pIri) + " " + valueTermCompact(px, o) + " .";
    }

    private static String valueTermCompact(Prefixer px, Value v) {
        if (v instanceof IRI) {
            return px.compactIri(((IRI) v).stringValue());
        }
        if (v instanceof BNode) {
            return "_:" + ((BNode) v).getID();
        }
        Literal lit = (Literal) v;
        String label = escapeLiteral(lit.getLabel());
        if (lit.getLanguage().isPresent()) {
            return "\"" + label + "\"@" + lit.getLanguage().get();
        }
        IRI dt = lit.getDatatype();
        if (dt != null && !XMLSchema.STRING.equals(dt)) {
            return "\"" + label + "\"^^" + px.compactIri(dt.stringValue());
        }
        return "\"" + label + "\"";
    }

    /* ===================== Load forms (with gender/number) ===================== */
    private static Map<String, EntryForms> loadEntryForms(RepositoryConnection conn, Set<String> entryIds) throws Exception {
        Map<String, EntryForms> map = new HashMap<>();
        List<String> iris = new ArrayList<>();
        for (String e : entryIds) {
            if (!e.startsWith("_:")) {
                iris.add(e);
            }
        }
        if (iris.isEmpty()) {
            return map;
        }

        String v = valuesIRI("entry", iris);
        String q = PFX
                + "SELECT ?entry ?kind ?f ?lit ?g ?n WHERE {\n"
                + v
                + "  { ?entry ontolex:canonicalForm ?f . ?f ontolex:writtenRep ?lit .\n"
                + "    OPTIONAL { ?f lexinfo:gender ?g } OPTIONAL { ?f lexinfo:number ?n }\n"
                + "    BIND(\"canonical\" AS ?kind) }\n"
                + "  UNION { ?entry ontolex:otherForm ?f . ?f ontolex:writtenRep ?lit .\n"
                + "    OPTIONAL { ?f lexinfo:gender ?g } OPTIONAL { ?f lexinfo:number ?n }\n"
                + "    BIND(\"other\" AS ?kind) }\n"
                + "  UNION { ?entry ontolex:lexicalForm ?f . ?f ontolex:writtenRep ?lit .\n"
                + "    OPTIONAL { ?f lexinfo:gender ?g } OPTIONAL { ?f lexinfo:number ?n }\n"
                + "    BIND(\"lexical\" AS ?kind) }\n"
                + "  UNION { ?entry rdfs:label ?lit . BIND(\"label\" AS ?kind) BIND (BNODE() AS ?f) }\n"
                + "} ORDER BY ?entry ?kind ?f ?lit";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        Map<String, Map<String, Map<String, FormVal>>> tmp = new HashMap<>();

        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                String e = resId(b, "entry");
                String kind = lit(b, "kind");
                String fId = resId(b, "f");
                Value vLit = b.getValue("lit");
                String gIri = iri(b, "g");
                String nIri = iri(b, "n");
                if (e == null || kind == null || fId == null || !(vLit instanceof Literal)) {
                    continue;
                }

                FormVal fv = tmp
                        .computeIfAbsent(e, k -> new HashMap<>())
                        .computeIfAbsent(kind, k -> new HashMap<>())
                        .computeIfAbsent(fId, k -> new FormVal(((Literal) vLit).getLabel()));

                if (gIri != null) {
                    fv.genders.add(shrink(gIri));
                }
                if (nIri != null) {
                    fv.numbers.add(shrink(nIri));
                }
            }
        } finally {
            rs.close();
        }
        for (Map.Entry<String, Map<String, Map<String, FormVal>>> e : tmp.entrySet()) {
            EntryForms ef = new EntryForms();
            Map<String, Map<String, FormVal>> byKind = e.getValue();
            addAll(ef.canonical, byKind.get("canonical"));
            addAll(ef.other, byKind.get("other"));
            addAll(ef.lexical, byKind.get("lexical"));
            Map<String, FormVal> labels = byKind.get("label");
            if (labels != null) {
                for (FormVal fv : labels.values()) {
                    ef.label.add(fv.text);
                }
            }
            map.put(e.getKey(), ef);
        }
        return map;
    }

    private static void ensureEntryLangPos(RepositoryConnection conn, Collection<String> entriesMissing) {
        List<String> iris = new ArrayList<>();
        for (String e : entriesMissing) {
            if (e != null && !e.startsWith("_:")
                    && (!entryLang.containsKey(e) || !entryPos.containsKey(e) || !MULTIWORD_ENTRIES.contains(e))) {
                iris.add(e);
            }
        }
        if (iris.isEmpty()) {
            return;
        }
        String v = valuesIRI("entry", iris);
        String q = PFX
                + "SELECT ?entry ?lexLang ?pos ?t WHERE {\n"
                + v
                + "  ?lex a lime:Lexicon ; lime:entry ?entry ; lime:language ?lexLang .\n"
                + "  OPTIONAL { ?entry lexinfo:partOfSpeech ?pos }\n"
                + "  OPTIONAL { ?entry a ?t . FILTER(?t IN (ontolex:LexicalEntry, ontolex:Word, ontolex:MultiwordExpression)) }\n"
                + "}";
        TupleQueryResult rs = conn.prepareTupleQuery(QueryLanguage.SPARQL, q).evaluate();
        try {
            while (rs.hasNext()) {
                BindingSet b = rs.next();
                String e = resId(b, "entry");
                String lang = lit(b, "lexLang");
                String pos = iri(b, "pos");
                String t = iri(b, "t");
                if (e == null || lang == null) {
                    continue;
                }
                entryLang.putIfAbsent(e, lang);
                if (pos != null) {
                    entryPos.putIfAbsent(e, POS.getOrDefault(pos, shrink(pos)));
                }
                if (OntoLexEntity.LexicalEntryTypes.Multiword.toString().equals(t)) {
                    MULTIWORD_ENTRIES.add(e);
                }
            }
        } finally {
            rs.close();
        }
    }

    private static void addAll(List<FormVal> target, Map<String, FormVal> src) {
        if (src == null) {
            return;
        }
        target.addAll(src.values());
    }

    /* ===================== Helpers & DTO ===================== */
    private static String S(String sense) {
        return "S:" + sense;
    }

    private static String E(String entry) {
        return "E:" + entry;
    }

    private static String C(String concept) {
        return "C:" + concept;
    }

    private static String iri(BindingSet b, String n) {
        Value v = b.getValue(n);
        return (v instanceof IRI) ? v.stringValue() : null;
    }

    private static String lit(BindingSet b, String n) {
        Value v = b.getValue(n);
        return (v != null && v.isLiteral()) ? v.stringValue() : null;
    }

    private static String resId(BindingSet b, String n) {
        Value v = b.getValue(n);
        if (v == null) {
            return null;
        }
        if (v instanceof IRI) {
            return v.stringValue();
        }
        if (v instanceof BNode) {
            return "_:" + ((BNode) v).getID();
        }
        return null;
    }

    private static String valuesIRI(String var, Collection<String> iris) {
        StringBuilder sb = new StringBuilder("VALUES ?").append(var).append(" { ");
        for (String s : iris) {
            sb.append("<").append(s).append("> ");
        }
        sb.append("}\n");
        return sb.toString();
    }

    private static String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String shrink(String uri) {
        int i = Math.max(uri.lastIndexOf('#'), uri.lastIndexOf('/'));
        return (i >= 0 && i + 1 < uri.length()) ? uri.substring(i + 1) : uri;
    }

    private static String minLex(Collection<String> xs) {
        String m = null;
        for (String x : xs) {
            if (m == null || x.compareTo(m) < 0) {
                m = x;
            }
        }
        return m;
    }

    private static String valueToString(Value v) {
        if (v == null) {
            return null;
        }
        if (v instanceof Literal) {
            return ((Literal) v).getLabel();
        }
        return v.stringValue();
    }

    // Forms & selections
    private static class FormVal {

        final String text;
        final List<String> genders = new ArrayList<>();
        final List<String> numbers = new ArrayList<>();

        FormVal(String t) {
            this.text = t;
        }
    }

    private static class EntryForms {

        final List<FormVal> canonical = new ArrayList<>();
        final List<FormVal> other = new ArrayList<>();
        final List<FormVal> lexical = new ArrayList<>();
        final List<String> label = new ArrayList<>();
    }

    private static class TermSelection {

        final String mainTerm;
        final List<String> altForms;
        final boolean isFromOntoLexForm;
        final List<String> genders;
        final List<String> numbers;

        TermSelection(String m, List<String> a, boolean fromForm, List<String> g, List<String> n) {
            this.mainTerm = m;
            this.altForms = a;
            this.isFromOntoLexForm = fromForm;
            this.genders = (g == null) ? Collections.<String>emptyList() : new ArrayList<>(new LinkedHashSet<>(g));
            this.numbers = (n == null) ? Collections.<String>emptyList() : new ArrayList<>(new LinkedHashSet<>(n));
        }
    }

    private static TermSelection selectTerm(EntryForms ef) {
        List<String> alt = new ArrayList<>();
        String main;
        boolean fromForm = false;
        List<String> g = null, n = null;

        if (ef != null && !ef.canonical.isEmpty()) {
            FormVal fv = ef.canonical.get(0);
            fromForm = true;
            main = fv.text;
            g = fv.genders;
            n = fv.numbers;
            for (int i = 1; i < ef.canonical.size(); i++) {
                alt.add(ef.canonical.get(i).text);
            }
            for (FormVal o : ef.other) {
                alt.add(o.text);
            }
            for (FormVal l : ef.lexical) {
                alt.add(l.text);
            }
            alt.addAll(ef.label);
        } else if (ef != null && !ef.other.isEmpty()) {
            FormVal fv = ef.other.get(0);
            fromForm = true;
            main = fv.text;
            g = fv.genders;
            n = fv.numbers;
            for (int i = 1; i < ef.other.size(); i++) {
                alt.add(ef.other.get(i).text);
            }
            for (FormVal l : ef.lexical) {
                alt.add(l.text);
            }
            alt.addAll(ef.label);
        } else if (ef != null && !ef.lexical.isEmpty()) {
            FormVal fv = ef.lexical.get(0);
            fromForm = true;
            main = fv.text;
            g = fv.genders;
            n = fv.numbers;
            for (int i = 1; i < ef.lexical.size(); i++) {
                alt.add(ef.lexical.get(i).text);
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

        LinkedHashSet<String> set = new LinkedHashSet<>(alt);
        set.remove(main);
        return new TermSelection(main, new ArrayList<>(set), fromForm, g, n);
    }

    private static class TermRecord {

        final String term, pos, definition;
        final List<String> altForms;
        final boolean fromOntoLexForm;
        final List<String> genders, numbers;
        final List<String> derivedProps;
        final boolean multiword;   // <-- NUOVO

        TermRecord(String t, String p, String d,
                List<String> a,
                boolean fromForm,
                List<String> g,
                List<String> n,
                List<String> dprops,
                boolean multiword) {           // <-- parametro in più
            term = t;
            pos = p;
            definition = d;
            altForms = (a == null) ? Collections.<String>emptyList() : new ArrayList<>(new LinkedHashSet<>(a));
            fromOntoLexForm = fromForm;
            genders = (g == null) ? Collections.<String>emptyList() : new ArrayList<>(new LinkedHashSet<>(g));
            numbers = (n == null) ? Collections.<String>emptyList() : new ArrayList<>(new LinkedHashSet<>(n));
            derivedProps = (dprops == null) ? Collections.<String>emptyList() : new ArrayList<>(new LinkedHashSet<>(dprops));
            this.multiword = multiword;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TermRecord)) {
                return false;
            }
            TermRecord x = (TermRecord) o;
            return Objects.equals(term, x.term)
                    && Objects.equals(pos, x.pos)
                    && Objects.equals(definition, x.definition)
                    && Objects.equals(altForms, x.altForms)
                    && fromOntoLexForm == x.fromOntoLexForm
                    && Objects.equals(genders, x.genders)
                    && Objects.equals(numbers, x.numbers)
                    && Objects.equals(derivedProps, x.derivedProps)
                    && multiword == x.multiword;        // <-- confronta anche multiword
        }

        @Override
        public int hashCode() {
            return Objects.hash(term, pos, definition, altForms,
                    fromOntoLexForm, genders, numbers, derivedProps, multiword); // <-- anche qui
        }
    }

    private static class Component {

        final Set<String> senses = new LinkedHashSet<>();
        final Set<String> entries = new LinkedHashSet<>();
        final Set<String> concepts = new LinkedHashSet<>();
    }

    private static String pickAnyDefForEntry(String e) {
        for (Map.Entry<String, Set<String>> kv : sense2entries.entrySet()) {
            if (kv.getValue().contains(e)) {
                String d = senseDef.get(kv.getKey());
                if (d != null) {
                    return d;
                }
            }
        }
        return null;
    }

    private static String sha1Hex(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] h = md.digest(s.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : h) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> m = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String k = args[i].substring(2);
                String v = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[++i] : "true";
                m.put(k, v);
            }
        }
        return m;
    }

    private static void setPrettyBlockText(Element note, String content) {
        String block = System.lineSeparator() + content + System.lineSeparator();
        note.setText(block);
    }

    private static String joinLines(Collection<String> lines) {
        String sep = System.lineSeparator();
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : lines) {
            if (!first) {
                sb.append(sep);
            }
            sb.append(s);
            first = false;
        }
        return sb.toString();
    }

    private static String required(Map<String, String> m, String k) {
        String v = m.get(k);
        if (v == null || v.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing --" + k);
        }
        return v;
    }

    private static String nsOf(String iri) {
        int i = Math.max(iri.lastIndexOf('#'), iri.lastIndexOf('/'));
        return (i >= 0) ? iri.substring(0, i + 1) : iri;
    }

    private static String localOf(String iri) {
        int i = Math.max(iri.lastIndexOf('#'), iri.lastIndexOf('/'));
        return (i >= 0 && i + 1 < iri.length()) ? iri.substring(i + 1) : iri;
    }

    private static void prefetchTriplesBySubjectCompact(RepositoryConnection conn, Prefixer px, Set<String> iris) {
        // Filtra blank node e già in cache
        LinkedHashSet<String> todo = new LinkedHashSet<>();
        for (String s : iris) {
            if (s == null || s.startsWith("_:")) {
                continue;
            }
            if (!triplesBySubjectCompact.containsKey(s)) {
                todo.add(s);
            }
        }
        if (todo.isEmpty()) {
            return;
        }

        // BATCH: CONSTRUCT di tutte le triple con soggetto in todo
        String values = valuesIRI("s", todo);
        String q = PFX
                + "CONSTRUCT { ?s ?p ?o } WHERE { " + values + " ?s ?p ?o }";
        org.eclipse.rdf4j.query.GraphQuery gq = conn.prepareGraphQuery(QueryLanguage.SPARQL, q);
        try ( org.eclipse.rdf4j.query.GraphQueryResult gr = gq.evaluate()) {
            while (gr.hasNext()) {
                org.eclipse.rdf4j.model.Statement st = gr.next();
                String s = (st.getSubject() instanceof IRI) ? ((IRI) st.getSubject()).stringValue() : null;
                String p = (st.getPredicate() instanceof IRI) ? ((IRI) st.getPredicate()).stringValue() : null;
                Value o = st.getObject();
                if (s == null || p == null) {
                    continue;
                }

                String line = turtleLineCompact(px, s, p, o);
                triplesBySubjectCompact
                        .computeIfAbsent(s, k -> new LinkedHashSet<>())
                        .add(line);
            }
        }
        // Inizializza anche i soggetti senza triple per evitare re-query
        for (String s : todo) {
            triplesBySubjectCompact.putIfAbsent(s, new LinkedHashSet<>());
        }
    }

    private static void prefetchTriplesByObjectCompact(RepositoryConnection conn, Prefixer px, Set<String> iris) {
        // Filtra blank node e già in cache
        LinkedHashSet<String> todo = new LinkedHashSet<>();
        for (String o : iris) {
            if (o == null || o.startsWith("_:")) {
                continue;
            }
            if (!triplesByObjectCompact.containsKey(o)) {
                todo.add(o);
            }
        }
        if (todo.isEmpty()) {
            return;
        }

        // BATCH: CONSTRUCT di tutte le triple con oggetto in todo
        String values = valuesIRI("o", todo);
        String q = PFX
                + "CONSTRUCT { ?s ?p ?o } WHERE { ?s ?p ?o . " + values + " FILTER(?o IN ("
                + // NOTA: VALUES copre già gli oggetti; niente FILTER duplicata:
                // teniamo solo VALUES per massima compatibilità
                ")) }";
        // -- Correzione: usiamo solo VALUES
        q = PFX + "CONSTRUCT { ?s ?p ?o } WHERE { ?s ?p ?o . " + values + " }";
        org.eclipse.rdf4j.query.GraphQuery gq = conn.prepareGraphQuery(QueryLanguage.SPARQL, q);
        try ( org.eclipse.rdf4j.query.GraphQueryResult gr = gq.evaluate()) {
            while (gr.hasNext()) {
                org.eclipse.rdf4j.model.Statement st = gr.next();
                String s = (st.getSubject() instanceof IRI) ? ((IRI) st.getSubject()).stringValue() : null;
                String p = (st.getPredicate() instanceof IRI) ? ((IRI) st.getPredicate()).stringValue() : null;
                String o = (st.getObject() instanceof IRI) ? ((IRI) st.getObject()).stringValue()
                        : (st.getObject() instanceof BNode) ? null : null; // per le note oggetto=IRI; i literal non servono qui
                if (s == null || p == null) {
                    continue;
                }

                // Manteniamo tutte le triple; compattiamo anche i literal quando presenti
                String line = turtleLineCompact(px, s, p, st.getObject());
                // Registra per oggetto-Iri (se literal/bnode non registriamo) 
                if (o != null) {
                    triplesByObjectCompact
                            .computeIfAbsent(o, k -> new LinkedHashSet<>())
                            .add(line);
                }
            }
        }
        for (String o : todo) {
            triplesByObjectCompact.putIfAbsent(o, new LinkedHashSet<>());
        }
    }


    /* ===================== Prefixer ===================== */
    private static class Prefixer {

        private final Map<String, String> ns2pref = new LinkedHashMap<>();
        private final Map<String, String> pref2ns = new LinkedHashMap<>();
        private final LinkedHashSet<String> usedNs = new LinkedHashSet<>();
        private int termCount = 1;
        private int ontoCount = 1;

        Prefixer() {
            put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
            put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
            put("skos", "http://www.w3.org/2004/02/skos/core#");
            put("ontolex", "http://www.w3.org/ns/lemon/ontolex#");
            put("lime", "http://www.w3.org/ns/lemon/lime#");
            put("lexinfo", "http://www.lexinfo.net/ontology/3.0/lexinfo#");
            put("vartrans", "http://www.w3.org/ns/lemon/vartrans#");
            put("dct", "http://purl.org/dc/terms/");
            put("xsd", "http://www.w3.org/2001/XMLSchema#");
        }

        private void put(String pref, String ns) {
            ns2pref.put(ns, pref);
            pref2ns.put(pref, ns);
        }

        private String ensurePrefix(String ns) {
            String p = ns2pref.get(ns);
            if (p != null) {
                return p;
            }
            String low = ns.toLowerCase();
            if (low.contains("onto")) {
                p = "ontology" + (ontoCount++);
            } else {
                p = "terminology" + (termCount++);
            }
            put(p, ns);
            return p;
        }

        String compactIri(String iri) {
            if (iri == null) {
                return "<null>";
            }
            String ns = nsOf(iri);
            String local = localOf(iri);
            if (ns.equals(iri) || local.isEmpty()) {
                return "<" + iri + ">";
            }
            String pref = ensurePrefix(ns);
            usedNs.add(ns);
            return pref + ":" + local;
        }

        String buildPrefixHeader() {
            StringBuilder sb = new StringBuilder();
            List<String> prefs = new ArrayList<>();
            for (String ns : usedNs) {
                prefs.add(ns2pref.get(ns));
            }
            Collections.sort(prefs);
            for (String p : prefs) {
                String ns = pref2ns.get(p);
                sb.append("@prefix ").append(p).append(": <").append(ns).append("> .")
                        .append(System.lineSeparator());
            }
            if (sb.length() > 0) {
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        }
    }

    /* ===================== Union-Find ===================== */
    private static class UnionFind {

        private final Map<String, String> parent = new HashMap<>();

        private String find(String x) {
            if (x == null) {
                return null;
            }
            String p = parent.get(x);
            if (p == null) {
                parent.put(x, x);
                return x;
            }
            if (!p.equals(x)) {
                p = find(p);
                parent.put(x, p);
            }
            return p;
        }

        String findNode(String x) {
            return find(x);
        }

        void union(String a, String b) {
            if (a == null || b == null) {
                return;
            }
            String ra = find(a), rb = find(b);
            if (!ra.equals(rb)) {
                parent.put(ra, rb);
            }
        }

    }

}
