package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.UtilityManager;
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
    
}
