package it.cnr.ilc.lexo.util;

import java.text.Normalizer;

/**
 *
 * @author andreabellandi
 */
public class StringUtil {

    public static String escapeMetaCharacters(String inputString) {
        final String[] metaCharacters = {"\\", "^", "$", "{", "}", "[", "]", "(", ")", "/", "*", "+", "?", "|", "<", ">", ":", "&", "%"};

        for (int i = 0; i < metaCharacters.length; i++) {
            if (inputString.contains(metaCharacters[i])) {
                inputString = inputString.replace(metaCharacters[i], "\\\\" + metaCharacters[i]);
            }
        }
        return inputString;
    }

    public static String upperCaseToCamelCase(String text) {
        StringBuilder builder = new StringBuilder();
        boolean upper = false;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '_') {
                upper = true;
            } else if (upper) {
                builder.append(Character.toUpperCase(text.charAt(i)));
                upper = false;
            } else {
                builder.append(Character.toLowerCase(text.charAt(i)));
            }
        }
        return builder.toString();
    }

    public static String camelCaseToUpperCase(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (Character.isUpperCase(text.charAt(i))) {
                builder.append('_').append(text.charAt(i));
            } else {
                builder.append(Character.toUpperCase(text.charAt(i)));
            }
        }
        return builder.toString();
    }

}
