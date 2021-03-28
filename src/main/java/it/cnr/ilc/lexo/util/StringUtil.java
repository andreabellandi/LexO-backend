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

}
