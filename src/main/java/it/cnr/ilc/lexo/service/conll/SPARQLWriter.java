/**
 * @author Enrico Carniani
 */
package it.cnr.ilc.lexo.service.conll;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public class SPARQLWriter {

    static final public String SEPARATOR = "# data-chunk";
    final private StringBuffer buffer = new StringBuffer();
    final private String creator;
    final private int chunkSize = 1500;
    private int numberOfTriples = 0;
    private String prefixes;

    public int getNumberOfTriples() {
        return numberOfTriples;
    }

    public void insertTriple(String subject, String link, String object) {
        boolean isEndOfChunk = chunkSize > 0 && numberOfTriples % chunkSize == 0;

        if (isEndOfChunk && numberOfTriples > 0) {
            if (numberOfTriples > 0) {
                buffer.append(String.format("}\n%s\n%s", SEPARATOR, prefixes));
            }
        }
        if (numberOfTriples == 0 || isEndOfChunk) {
            buffer.append("INSERT DATA {\n");
        }

        object = object.replaceAll("[\n\t ]+", " ");
        String query = String.format("\t%s %s %s .\n", subject, link, object);
        buffer.append(query);
        numberOfTriples++;
    }

    public void insertTriple(String subject, String link, Map<String, String> anon) {
        String object = "[ ";
        int count = anon.size();
        for (Entry<String, String> entry : anon.entrySet()) {
            object += entry.getKey() + " " + entry.getValue();
            if (--count > 0) {
                object += " ; ";
            }
        }
        object += " ]";
        insertTriple(subject, link, object);
    }

    private void addMetaData(String entryFQN) {
        Date now = DateProvider.getInstance().getDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmX"); // Quoted "Z" to indicate UTC, no timezone offset
        String date = df.format(now);

        insertTripleWithString(entryFQN, "dct:creator", creator);
        insertTripleWithString(entryFQN, "dct:created", date + ":00");
        insertTripleWithString(entryFQN, "dct:modified", date + ":00");
    }

    private void createWordEntry(String lexiconFQN, Word word, String rdfType, String namespace) {
        insertTriple(lexiconFQN, "lime:entry", namespace + word.FQName);
        insertTriple(namespace + word.FQName, "rdf:type", rdfType);
        insertTripleWithLanguage(namespace + word.FQName, "rdfs:label", word.canonicalForm.text, word.language);
        if (word.partOfSpeech != null) {
            insertTriple(namespace + word.FQName, "lexinfo:partOfSpeech", word.partOfSpeech);
        }
        insertTripleWithString(namespace + word.FQName, "vs:term_status", "working");
        for (Entry<String, String> entry : word.additionalInfo.entrySet()) {
            insertTripleWithString(namespace + word.FQName, entry.getKey(), entry.getValue());
        }
        addMetaData(namespace + word.FQName);
    }

    private void createLexicalSense(Word word, String lexicalSenseFQN, String definition, String namespace) {
        insertTriple(namespace + word.FQName, "ontolex:sense", lexicalSenseFQN);
        insertTriple(lexicalSenseFQN, "rdf:type", "ontolex:LexicalSense");
        if (definition != null) {
            insertTripleWithString(lexicalSenseFQN, "skos:definition", definition);
        }
        if (word.conceptFQN != null) {
            insertTriple(lexicalSenseFQN, "ontolex:reference", word.conceptFQN);
        }
        addMetaData(lexicalSenseFQN);
    }

    private void createLexicalSenses(Word word, String namespace) {
        if (word.senses.isEmpty()) {
            word.senses.put("", null);
        }
        for (Entry<String, String> sense : word.senses.entrySet()) {
            String lexicalSenseFQN = String.format(namespace + "%s_sense%s", word.FQName, sense.getKey());
            createLexicalSense(word, lexicalSenseFQN, sense.getValue(), namespace);
        }
    }

    private void createCanonicalForm(Word word, String namespace) {
        String canonicalFormFQN = namespace + word.canonicalForm.FQName;
        insertTriple(namespace + word.FQName, "ontolex:canonicalForm", canonicalFormFQN);
        insertTriple(canonicalFormFQN, "rdf:type", "ontolex:Form");
        insertTripleWithLanguage(canonicalFormFQN, "ontolex:writtenRep", word.canonicalForm.text, word.language);
        addMetaData(canonicalFormFQN);

        for (Entry<String, String> entry : word.canonicalForm.features.entrySet()) {
            insertTriple(canonicalFormFQN, entry.getValue(), entry.getKey());
        }

    }

    private void createOtherForms(Word word, String namespace) {
        for (Form otherForm : word.getOtheForms()) {
            String otherFormFQN = namespace + otherForm.FQName;
            insertTriple(namespace + word.FQName, "ontolex:otherForm", otherFormFQN);
            insertTriple(otherFormFQN, "rdf:type", "ontolex:Form");
            insertTripleWithLanguage(otherFormFQN, "ontolex:writtenRep", otherForm.text, word.language);
            addMetaData(otherFormFQN);

            for (Entry<String, String> entry : otherForm.features.entrySet()) {
                insertTriple(otherFormFQN, entry.getValue(), entry.getKey());
            }
        }
    }
    // Hi-level interface

    public SPARQLWriter(String prefix, String baseIRI, String creator) {
        this.prefixes = "PREFIX _NS_: <_BASEIRI_>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dct: <http://purl.org/dc/terms/>\n"
                + "PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX lime: <http://www.w3.org/ns/lemon/lime#>\n"
                + "PREFIX vs: <http://www.w3.org/2003/06/sw-vocab-status/ns#>\n"
                + "PREFIX vartrans: <http://www.w3.org/ns/lemon/vartrans#>\n"
                + "PREFIX lexinfo: <http://www.lexinfo.net/ontology/3.0/lexinfo#>\n";
        this.creator = creator;
//        prefixes = String.format(prefixes, prefix, baseIRI);
        prefixes = prefixes.replace("_NS_", prefix).replace("_BASEIRI_", baseIRI);
        buffer.append(prefixes);
    }

    public String createLexicon(String lexiconFQN, String language) {
        insertTriple(lexiconFQN, "rdf:type", "lime:Lexicon");
        insertTripleWithString(lexiconFQN, "lime:language", language);
        addMetaData(lexiconFQN);
        return lexiconFQN;
    }

    public void addWord(Word word, String lexiconFQN, String rdfType, String namespace) {
        createWordEntry(lexiconFQN, word, rdfType, namespace);
        createLexicalSenses(word, namespace);
        createCanonicalForm(word, namespace);
        createOtherForms(word, namespace);
    }

    public String formatObjectWithUrlIfPossible(String object) {
        try {
            new URL(object);
            object = String.format("<%s>", object);
        } catch (MalformedURLException e) {
            object = formatObject(object);
        }
        return object;
    }

    public String formatObjectWithLanguage(String object, String language) {
        object = String.format("%s@%s", formatObject(object), language);
        return object;
    }

    private String formatObject(String object) {
        object = object.replaceAll("\"", "\\\\\"");
        object = object.replaceAll("\n", "\\\\n");
        object = String.format("\"%s\"", object.trim());
        return object;
    }

    public void insertTripleWithUrlIfPossible(String subject, String link, String object) {
        insertTriple(subject, link, formatObjectWithUrlIfPossible(object));
    }

    public void insertTripleWithLanguage(String subject, String link, String object, String language) {
        insertTriple(subject, link, formatObjectWithLanguage(object, language));
    }

    public void insertTripleWithString(String subject, String link, String object) {
        insertTriple(subject, link, formatObject(object));
    }

    @Override
    public String toString() {
        if (numberOfTriples % chunkSize != 0) {
            buffer.append("}\n");
        }
        return buffer.toString();
    }
}
