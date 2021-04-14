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
}
