package it.cnr.ilc.lexo.util;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author andreabellandi
 */
public class StringUtil {

    public static final String URL_PATTERN = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
    public static final Pattern pattern = Pattern.compile(URL_PATTERN);

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

    public static boolean validateURL(String url) {
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean prefixedURL(String url, String... urlPrefix) {
        if (urlPrefix.length > 0) {
            boolean prefixed = false;
            for (String p : urlPrefix) {
                if (url.startsWith(p)) {
                    prefixed = true;
                    break;
                }
            }
            if (!prefixed) {
                return false;
            }
            return true;
        }
        return false;
    }

}
