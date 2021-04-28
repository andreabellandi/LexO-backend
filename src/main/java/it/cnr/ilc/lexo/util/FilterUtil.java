package it.cnr.ilc.lexo.util;

import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 * @author andreabellandi
 */
public class FilterUtil {

    private static final String JOLLY_REPLACER = "§";

    public static boolean match(String text, String value, boolean startsWith, boolean endsWith, boolean matchCase, boolean wholeWord, boolean wholeString) {
        value = value.replaceAll("\\.\\.\\.", "…");
        value = value.replaceAll("([\\.\\(\\)\\[\\]\\{\\}\\^\\+\\|\\$])", "\\\\$1");
        value = value.replaceAll("\\\\\\*", JOLLY_REPLACER).replaceAll("\\*", ".+").replaceAll(JOLLY_REPLACER, "\\\\*");
        value = value.replaceAll("\\\\\\?", JOLLY_REPLACER).replaceAll("\\?", ".").replaceAll(JOLLY_REPLACER, "\\\\?");
//        value = matchCase ? value : StringUtil.removeDiacriticalMarks(value);
        value = wholeWord ? "\\b" + value + "\\b" : value;
        value = startsWith ? "^" + value : value;
        value = endsWith ? value + "$" : value;
        Pattern pattern = Pattern.compile(value, matchCase ? 0 : Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
//        text = matchCase ? text : StringUtil.removeDiacriticalMarks(text);
        return wholeString ? pattern.matcher(text).matches() : pattern.matcher(text).find();
    }

    public static boolean match(String text, Number value) {
        return Stream.of(text.split(" ")).anyMatch(s -> s.equals(value.toString()));
    }

}
