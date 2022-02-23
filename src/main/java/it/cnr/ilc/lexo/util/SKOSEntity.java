/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

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
        Concept("Concept"),
        ConceptScheme("ConceptScheme"),
        Collection("Collection"),
        OrderedCollection("OrderedCollection");

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
        inScheme("inScheme"),
        hasTopConcept("hasTopConcept"),
        topConceptOf("topConceptOf");

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
        semanticRelation("semanticRelation"),
        broaderTransitive("broaderTransitive"),
        narrowerTransitive("narrowerTransitive"),
        broader("broader"),
        narrower("narrower"),
        related("related");

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
        altLabel("altLabel"),
        hiddenLabel("hiddenLabel"),
        prefLabel("prefLabel");

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
        notation("notation");

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
        changeNote("changeNote"),
        definition("definition"),
        example("example"),
        historyNote("historyNote"),
        scopeNote("scopeNote"),
        note("note");

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
        broadMatch("broadMatch"),
        closeMatch("closeMatch"),
        exactMatch("exactMatch"),
        mappingRelation("mappingRelation"),
        narrowMatch("narrowMatch"),
        relatedMatch("relatedMatch");

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
        member("member"),
        memberList("memberList");

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
        Label("Label");

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
        literalForm("literalForm"),
        altLabel("altLabel"),
        hiddenLabel("hiddenLabel"),
        prefLabel("prefLabel");

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
