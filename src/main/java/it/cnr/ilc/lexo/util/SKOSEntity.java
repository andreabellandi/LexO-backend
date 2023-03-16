/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import java.util.EnumSet;

/**
 *
 * @author andreabellandi
 */
public class SKOSEntity {

    public static boolean containsString(Class<? extends Enum> enumClass, String string) {
        String[] stringValues = stringValues(enumClass);
        for (int i = 0; i < stringValues.length; i++) {
            if (stringValues[i].equals(string)) {
//            if (stringValues[i].contains(string)) {
                return true;
            }
        }
        return false;
    }

    public static String[] stringValues(Class<? extends Enum> enumClass) {
        EnumSet enumSet = EnumSet.allOf(enumClass);
        String[] values = new String[enumSet.size()];
        int i = 0;
        for (Object enumValue : enumSet) {
            values[i] = String.valueOf(enumValue);
            i++;
        }
        return values;
    }

    public static enum SKOSClass {
        Concept(SparqlPrefix.SKOS.getUri() + "Concept"),
        ConceptScheme(SparqlPrefix.SKOS.getUri() + "ConceptScheme"),
        Collection(SparqlPrefix.SKOS.getUri() + "Collection"),
        OrderedCollection(SparqlPrefix.SKOS.getUri() + "OrderedCollection");

        private final String type;

        private SKOSClass(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum SchemeProperty {
        inScheme(SparqlPrefix.SKOS.getUri() + "inScheme"),
        hasTopConcept(SparqlPrefix.SKOS.getUri() + "hasTopConcept"),
        topConceptOf(SparqlPrefix.SKOS.getUri() + "topConceptOf");

        private final String type;

        private SchemeProperty(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum SemanticRelation {
        semanticRelation(SparqlPrefix.SKOS.getUri() + "semanticRelation"),
        broaderTransitive(SparqlPrefix.SKOS.getUri() + "broaderTransitive"),
        narrowerTransitive(SparqlPrefix.SKOS.getUri() + "narrowerTransitive"),
        broader(SparqlPrefix.SKOS.getUri() + "broader"),
        narrower(SparqlPrefix.SKOS.getUri() + "narrower"),
        related(SparqlPrefix.SKOS.getUri() + "related");

        private final String type;

        private SemanticRelation(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum LexicalLabel {
        altLabel(SparqlPrefix.SKOS.getUri() + "altLabel"),
        hiddenLabel(SparqlPrefix.SKOS.getUri() + "hiddenLabel"),
        prefLabel(SparqlPrefix.SKOS.getUri() + "prefLabel");

        private final String type;

        private LexicalLabel(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum Notation {
        notation(SparqlPrefix.SKOS.getUri() + "notation");

        private final String type;

        private Notation(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum NoteProperty {
        changeNote(SparqlPrefix.SKOS.getUri() + "changeNote"),
        definition(SparqlPrefix.SKOS.getUri() + "definition"),
        example(SparqlPrefix.SKOS.getUri() + "example"),
        historyNote(SparqlPrefix.SKOS.getUri() + "historyNote"),
        scopeNote(SparqlPrefix.SKOS.getUri() + "scopeNote"),
        note(SparqlPrefix.SKOS.getUri() + "note");

        private final String type;

        private NoteProperty(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum MappingProperty {
        broadMatch(SparqlPrefix.SKOS.getUri() + "broadMatch"),
        closeMatch(SparqlPrefix.SKOS.getUri() + "closeMatch"),
        exactMatch(SparqlPrefix.SKOS.getUri() + "exactMatch"),
        mappingRelation(SparqlPrefix.SKOS.getUri() + "mappingRelation"),
        narrowMatch(SparqlPrefix.SKOS.getUri() + "narrowMatch"),
        relatedMatch(SparqlPrefix.SKOS.getUri() + "relatedMatch");

        private final String type;

        private MappingProperty(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum CollectionProperty {
        member(SparqlPrefix.SKOS.getUri() + "member"),
        memberList(SparqlPrefix.SKOS.getUri() + "memberList");

        private final String type;

        private CollectionProperty(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum SKOSXLClass {
        Label(SparqlPrefix.SKOS_XL.getUri() + "Label");

        private final String type;

        private SKOSXLClass(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum SKOSXLLexicalLabel {
        literalForm(SparqlPrefix.SKOS_XL.getUri() + "literalForm"),
        altLabel(SparqlPrefix.SKOS_XL.getUri() + "altLabel"),
        hiddenLabel(SparqlPrefix.SKOS_XL.getUri() + "hiddenLabel"),
        prefLabel(SparqlPrefix.SKOS_XL.getUri() + "prefLabel"),
        labelRelation(SparqlPrefix.SKOS_XL.getUri() + "labelRelation");

        private final String type;

        private SKOSXLLexicalLabel(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

}
