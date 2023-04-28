/**
 * @author Enrico Carniani
 */

package it.cnr.ilc.lexo.service.conll;

import it.cnr.ilc.lexo.service.conll.core.Document;
import it.cnr.ilc.lexo.service.conll.core.MultiwordToken;
import it.cnr.ilc.lexo.service.conll.core.Sentence;
import it.cnr.ilc.lexo.service.conll.core.Token;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Compiler {

    private static Form createOtherForm(Word word, String writtenRep) {
        String FQName = String.format("%s_form_%s", word.FQName, writtenRep);
        Form form = new Form(FQName, writtenRep);
        return form;
    }

    private static Map<String, String> getMapFromFieldString(String fieldString) {
        final Pattern fieldPattern = Pattern.compile("(?<key>[^|=]+)=(?<value>([^\\\"|]+|(\\\"[^\\\"]+\\\")))");
        Matcher field = fieldPattern.matcher(fieldString);
        Map<String, String> output = new HashMap<>();

        while (field.find()) {
            String key = field.group("key");
            String value = field.group("value");
            output.put(key, value);
        }    

        return output;
    }

    private static Map<String, String> compileField(String fieldString, Map<String, String> keyMap, Map<String, String> valueMap) {
        Map<String, String> output = new HashMap<>();
        Map<String, String> input = getMapFromFieldString(fieldString);

        for(Map.Entry<String,String> entry: input.entrySet()) {
            String mappedKey = keyMap.get(entry.getKey());
            if (mappedKey == null) {
                System.err.println(String.format("Key %s not defined, skipping", entry.getKey()));
                continue;
            }

            String value = entry.getValue();
            String mappedValue = valueMap.getOrDefault(value, "<http://nomapping>");
            if (!valueMap.containsKey(value)) {
                System.err.println(String.format("Value %s not defined, using %s", value, mappedValue));
            }
            output.put(mappedKey, mappedValue);
        }    

        return output;
    }
    private static void compileMisc(String miscString, Word word) {        
        if (miscString != null  && miscString.length() > 0)
            word.additionalInfo.put("skos:note", miscString);
            
        Map<String,String> misc = getMapFromFieldString(miscString);
        if (!misc.containsKey("SENSE")) return;
    
        String definition = misc.get("DEFINITION").replaceAll("\"", "");
        Map<String,String> senses = getMapFromFieldString(definition);
        word.senses.putAll(senses);
    }

    private static void compileFeatures(String featuresString, Form form) {
        final Map<String, String> keyMap = Stream.of(new String[][] {
            {"Definite", "lexinfo:definite"},
            {"Degree", "lexinfo:degree"},
            {"Gender", "lexinfo:gender"},
            {"Mood", "lexinfo:mood"},
            {"Neg",	"lexinfo:negative"},
            {"Number", "lexinfo:number"},
            {"Person", "lexinfo:person"},
            {"Tense", "lexinfo:tense"},
            {"VerbForm", "lexinfo:verbForm"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        final Map<String, String> valueMap = Stream.of(new String[][] {
            {"1", "lexinfo:firstPerson"},
            {"2", "lexinfo:secondPerson"},
            {"3", "lexinfo:thirdPerson"},
            {"Fem", "lexinfo:feminine"},
            {"Ger", "lexinfo:gerund"},
            {"Imp", "lexinfo:imperative"},
            {"Ind", "lexinfo:indicative"},
            {"Inf", "lexinfo:infinite"},
            {"Masc", "lexinfo:masculine"},
            {"Past", "lexinfo:past"},
            {"Plur", "lexinfo:plural"},
            {"Sing", "lexinfo:singular"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        
        Map<String,String> features = compileField(featuresString, keyMap, valueMap);
        form.features.putAll(features);   
    }

    static public Collection<Word> compileLexicon(Document document, String namespace, String language) {

        final Map<String, String> parts = Stream.of(new String[][] {
                { "ADV", "lexinfo:adverb" },
                { "VERB", "lexinfo:verb" },
                { "ADJ", "lexinfo:adjective" },
                { "NOUN", "lexinfo:noun" },
                { "PROPN", "lexinfo:properNoun" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        Map<String, Word> lemmas = new HashMap<>();
        List<Sentence> sentences = document.getSentences();

        for (Sentence sentence : sentences) {

            for (Token token : sentence.getTokens()) {
                Optional<MultiwordToken> mwt = token.getMwt();
                if (mwt.isPresent() && mwt.get().getTokens().get(0) == token)
                    continue;

                if (!parts.containsKey(token.getUpos())) 
                    continue;
                    
                String lemma = token.getLemma().toLowerCase();
                String writtenRep = token.getForm().toLowerCase();
                String partOfSpeech = parts.get(token.getUpos());
                String features = token.getFeats();
                String misc = token.getMisc();

                String key = String.format("%s+%s", lemma, partOfSpeech);
                Word word = lemmas.get(key);
                Form form = null;
                boolean needsCompile = true;

                if (word == null) {
                    word = new Word(lemma, partOfSpeech, language);
                    lemmas.put(key, word);

                    if (lemma.equals(writtenRep)) {
                        form = word.canonicalForm;
                    } else {
                        form = createOtherForm(word, writtenRep);
                        needsCompile = false;
                    }

                } else if ((form = word.findForm(writtenRep)) == null) {
                    form = createOtherForm(word, writtenRep);
                    word.addOtherForm(form);
                } else if (word.canonicalForm.features.isEmpty()) {
                    form = word.canonicalForm;
                }

            if (needsCompile) {
                compileMisc(misc, word);
                compileFeatures(features, form);
            }
            token.addMisc("annotation", namespace + form.FQName.substring(1));
        }
        }
        return lemmas.values();
    }
}
