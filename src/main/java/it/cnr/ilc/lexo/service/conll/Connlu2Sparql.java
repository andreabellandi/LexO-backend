/**
 * @author Enrico Carniani
 */
package it.cnr.ilc.lexo.service.conll;

import it.cnr.ilc.lexo.service.conll.core.Document;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CharSequenceReader;

public class Connlu2Sparql {

    Document document;
    String language;
    String creator;
    String namespace;
    private SPARQLWriter sparql;

    public Connlu2Sparql(InputStream inCoNLL, SPARQLWriter sparql, String language, String namespace, String creator) throws ConllException, IOException {
        this.sparql = sparql;
        this.language = language;
        this.namespace = namespace;
        this.creator = creator;
        document = parseCoNLL(inCoNLL);
    }

    private static Document parseCoNLL(InputStream inCoNLL) throws ConllException, IOException {
        byte[] buffer = IOUtils.toByteArray(inCoNLL);
        Reader targetReader = new CharSequenceReader(new String(buffer));

        targetReader.close();
        DocumentReader coNLLUReader = new CoNLLUReader(targetReader);
        Document document = coNLLUReader.readDocument();
        return document;
    }

    public String createSPARQL() {
        Collection<Word> words = Compiler.compileLexicon(document, namespace, language);
        String lexiconFQN = sparql.createLexicon(namespace + ":connll-u", language);
        for (Word word : words) {
            sparql.addWord(word, lexiconFQN, "ontolex:Word", namespace);
        }
        return sparql.toString();
    }

}


