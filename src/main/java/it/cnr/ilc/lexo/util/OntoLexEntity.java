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
        Word("Word"),
        Multiword("MultiwordExpression"),
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

}
