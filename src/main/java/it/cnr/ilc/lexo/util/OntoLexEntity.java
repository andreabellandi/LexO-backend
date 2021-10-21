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
public class OntoLexEntity {

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

    public static enum LexicalEntryTypes {
        LexicalEntry("LexicalEntry"),
        Word("Word"),
        Etymon("Etymon"),
        Multiword("MultiWordExpression"),
        Affix("Affix");

        private final String type;

        private LexicalEntryTypes(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
    
    public static enum FormTypes {
        LexicalForm("lexicalForm"),
        CanonicalForm("canonicalForm"),
        OtherForm("otherForm");

        private final String type;

        private FormTypes(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
    
    public static enum ReferenceTypes {
        Reference("reference"),
        Denotes("denotes");

        private final String type;

        private ReferenceTypes(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
    
    public static enum EtyLinkTypes {
        EtySource("etySource");

        private final String type;

        private EtyLinkTypes(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

}
