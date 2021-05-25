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
public class EnumUtil {

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

    public static enum SearchModes {
        Equals("equals"),
        StartsWith("startsWith"),
        Contains("contains"),
        Ends("ends");

        private final String searchMode;

        private SearchModes(String searchMode) {
            this.searchMode = searchMode;
        }

        @Override
        public String toString() {
            return this.searchMode;
        }
    }
    
    public static enum FormTypes {
        All(""),
        Flexed("flexed"),
        Entry("entry");

        private final String formType;

        private FormTypes(String formType) {
            this.formType = formType;
        }

        @Override
        public String toString() {
            return this.formType;
        }
    }
    
    public static enum LexicalEntryStatus {
        All(""),
        Reviewed("reviewed"),
        Working("working"),
        Completed("completed");

        private final String status;

        private LexicalEntryStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return this.status;
        }
    }
    
    public static enum LexicalEntryTypes {
        All(""),
        Word("word"),
        Multiword("multi-word expression"),
        Affix("affix");

        private final String type;

        private LexicalEntryTypes(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
    
    public static enum LexicalAspects {
        Core("core"),
        Decomp("decomposition"),
        Etym("etymology"),
        VarTrans("variation and translation"),
        SynSem("syntax and semantics");

        private final String aspect;

        private LexicalAspects(String aspect) {
            this.aspect = aspect;
        }

        @Override
        public String toString() {
            return this.aspect;
        }
        
    }
    
    public static enum LinkedEntityType {
        Internal("internal"),
        External("external");

        private final String linkedEntityType;

        private LinkedEntityType(String linkedEntityType) {
            this.linkedEntityType = linkedEntityType;
        }

        @Override
        public String toString() {
            return this.linkedEntityType;
        }
        
    }
    
    public static enum SearchFormTypes {
        Keyword("keyword"),
        Lemma("lemma");

        private final String searchFormTypes;

        private SearchFormTypes(String searchFormTypes) {
            this.searchFormTypes = searchFormTypes;
        }

        @Override
        public String toString() {
            return this.searchFormTypes;
        }
        
    }
    
    public static enum LexicalEntryAttributes {
        Label("label"),
        Type("type"),
        Status("status"),
        Note("note"),
        Language("language"),
        Denotes("denotes");

        private final String lexicalEntryAttributes;

        private LexicalEntryAttributes(String lexicalEntryAttributes) {
            this.lexicalEntryAttributes = lexicalEntryAttributes;
        }

        @Override
        public String toString() {
            return this.lexicalEntryAttributes;
        }
        
    }
    
    public static enum LinguisticRelation {
        Morphology("morphology"),
        LexicalRel("lexicalRel"),
        SenseRel("senseRel"),
        ConceptRel("conceptRel");

        private final String linguisticRelation;

        private LinguisticRelation(String linguisticRelation) {
            this.linguisticRelation = linguisticRelation;
        }        

        @Override
        public String toString() {
            return this.linguisticRelation;
        }
        
    }
    
    public static enum AcceptedSearchFormExtendTo {
        None(""),
        Hypernym("hypernym"),
        Hyponym("hyponym"),
        Synonym("synonym");

        private final String acceptedSearchFormExtendTo;

        private AcceptedSearchFormExtendTo(String acceptedSearchFormExtendTo) {
            this.acceptedSearchFormExtendTo = acceptedSearchFormExtendTo;
        }

        @Override
        public String toString() {
            return this.acceptedSearchFormExtendTo;
        }
        
    }
    
    public static enum AcceptedSearchFormExtensionDegree {
        One("1"),
        Two("2"),
        Three("3");

        private final String acceptedSearchFormExtensionDegree;

        private AcceptedSearchFormExtensionDegree(String acceptedSearchFormExtensionDegree) {
            this.acceptedSearchFormExtensionDegree = acceptedSearchFormExtensionDegree;
        }

        @Override
        public String toString() {
            return this.acceptedSearchFormExtensionDegree;
        }
        
    }
}
