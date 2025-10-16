package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.UtilityManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author andreabellandi
 */
public class StringUtil {

    public static final String URL_PATTERN = "http(s)?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
    public static final Pattern PATTERN = Pattern.compile(URL_PATTERN);

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
        Matcher matcher = PATTERN.matcher(url);
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

    public static boolean existsIRI(String id) throws ManagerException {
        UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
        if (!utilityManager.exists(id)) {
            return false;
        }
        return true;
    }

    public static boolean existsTypedIRI(String id, String type) throws ManagerException {
        UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
        if (!utilityManager.existsTyped(id, type)) {
            return false;
        }
        return true;
    }

    public static void validateIRI(String iri) throws ManagerException {
        if (iri != null) {
            if (iri.isEmpty()) {
                throw new ManagerException("cannot be empty");
            } else {
                if (!ManagerFactory.getManager(UtilityManager.class).existsNamespace(iri)) {
                    throw new ManagerException("is not a valid IRI");
                }
            }
        } else {
            throw new ManagerException("cannot be undefined");
        }
    }

    public static String getRomanNumber(int n) {
        switch (n) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return null;
        }
    }

    public static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
   
}
