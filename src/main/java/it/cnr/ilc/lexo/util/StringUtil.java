package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.UtilityManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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

    public static int romanToInt(String roman) {
        String s = roman.trim().toUpperCase(Locale.ROOT);
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Numero romano vuoto");
        }

        final Map<Character, Integer> val = ROMAN_VALUES;
        int total = 0;
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            Integer v1 = val.get(c);
            if (v1 == null) {
                throw new IllegalArgumentException("Simbolo romano non valido: " + c);
            }

            if (i + 1 < s.length()) {
                char d = s.charAt(i + 1);
                Integer v2 = val.get(d);
                if (v2 == null) {
                    throw new IllegalArgumentException("Simbolo romano non valido: " + d);
                }
                if (v1 < v2) { // notazione sottrattiva
                    total += (v2 - v1);
                    i += 2;
                    continue;
                }
            }
            total += v1;
            i++;
        }
        return total;
    }

    public static int parseArabic(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("arabicNumber not valid: " + s, e);
        }
    }

    public static String req(String s, String field) {
        if (s == null) {
            throw new IllegalArgumentException("null field: " + field);
        }
        return s;
    }

    private static final Map<Character, Integer> ROMAN_VALUES;

    static {
        Map<Character, Integer> m = new HashMap<>();
        m.put('I', 1);
        m.put('V', 5);
        m.put('X', 10);
        m.put('L', 50);
        m.put('C', 100);
        m.put('D', 500);
        m.put('M', 1000);
        ROMAN_VALUES = Collections.unmodifiableMap(m);
    }

    public static String nz(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * Concatena pezzi (già stringhe) per formare un'unica IRI (nessun
     * separatore).
     */
    public static String concatUris(String base, String... parts) {
        StringBuilder sb = new StringBuilder(base);
        for (String p : parts) {
            sb.append(p);
        }
        return sb.toString();
    }

    /**
     * Escaping minimale per literal string SPARQL (virgolette e backslash).
     */
    public static String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /**
     * a|A->1, b|B->2, ... ; valori non alfabetici generano errore.
     */
    public static int letterIndex(String letter) {
        if (letter == null || letter.trim().isEmpty()) {
            throw new IllegalArgumentException("Lettera vuota");
        }
        char c = letter.trim().toLowerCase(Locale.ROOT).charAt(0);
        if (c < 'a' || c > 'z') {
            throw new IllegalArgumentException("Lettera non valida: " + letter);
        }
        return (c - 'a') + 1;
    }

    /**
     * Parsifica un intero positivo (>=1).
     */
    public static int parsePositiveInt(String s, String what) {
        try {
            int v = Integer.parseInt(s.trim());
            if (v < 1) {
                throw new IllegalArgumentException();
            }
            return v;
        } catch (Exception e) {
            throw new IllegalArgumentException("Valore non valido per " + what + ": " + s);
        }
    }

    public static String romanValues(int max) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= max; i++) {
            sb.append('(').append(i).append(" \"").append(toRoman(i)).append("\") ");
        }
        return sb.toString().trim();
    }

    public static String letterValues(int max) {
        StringBuilder sb = new StringBuilder();
        int m = Math.min(max, 26);
        for (int i = 1; i <= m; i++) {
            sb.append('(').append(i).append(" \"").append((char) ('a' + i - 1)).append("\") ");
        }
        return sb.toString().trim();
    }

    public static String toRoman(int n) {
        int[] vals = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] syms = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder sb = new StringBuilder();
        int x = n;
        for (int i = 0; i < vals.length; i++) {
            while (x >= vals[i]) {
                sb.append(syms[i]);
                x -= vals[i];
            }
        }
        return sb.toString();
    }
}
