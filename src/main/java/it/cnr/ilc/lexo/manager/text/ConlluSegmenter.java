package it.cnr.ilc.lexo.manager.text;

import it.cnr.ilc.lexo.manager.text.model.Heading;
import it.cnr.ilc.lexo.manager.text.model.Paragraph;
import it.cnr.ilc.lexo.manager.text.model.ParsedTextDocument;
import it.cnr.ilc.lexo.manager.text.model.Sentence;
import it.cnr.ilc.lexo.manager.text.model.Token;
import it.cnr.ilc.lexo.manager.text.model.ValidationIssue;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Applies CoNLL-U sentence/token segmentation to a canonical text. */
public final class ConlluSegmenter {

    private static final Pattern COMMENT = Pattern.compile("^#\\s*([^=]+?)\\s*=\\s*(.*)$");
    private static final Pattern INTEGER_ID = Pattern.compile("^[1-9][0-9]*$");
    private static final Pattern RANGE_ID = Pattern.compile("^[1-9][0-9]*-[1-9][0-9]*$");
    private static final Pattern EMPTY_NODE_ID = Pattern.compile("^[1-9][0-9]*\\.[1-9][0-9]*$");
    private static final Pattern TOKEN_RANGE = Pattern.compile("^(\\d+)\\s*:\\s*(\\d+)$");

    public void apply(ParsedTextDocument doc, String rawConllu, String conlluFileName)
            throws ConlluValidationException {
        List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
        if (rawConllu == null || rawConllu.trim().isEmpty()) {
            issues.add(new ValidationIssue(1, 1, "EMPTY_CONLLU", "Il file CoNLL-U è vuoto"));
            throw new ConlluValidationException(issues);
        }
        if (doc == null || doc.cleanText == null) {
            issues.add(new ValidationIssue(1, 1, "MISSING_CANONICAL_TEXT",
                    "Il testo NIF canonico non è disponibile"));
            throw new ConlluValidationException(issues);
        }

        String conllu = Normalizer.normalize(rawConllu, Normalizer.Form.NFC)
                .replace("\r\n", "\n")
                .replace('\r', '\n');
        if (!conllu.isEmpty() && conllu.charAt(0) == '\uFEFF') {
            conllu = conllu.substring(1);
        }

        String[] lines = conllu.split("\\n", -1);
        List<SentenceDraft> drafts = new ArrayList<SentenceDraft>();
        SentenceDraft current = new SentenceDraft();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineNo = i + 1;
            if (line.trim().isEmpty()) {
                flushDraft(current, drafts, issues);
                current = new SentenceDraft();
            } else if (line.startsWith("#")) {
                parseComment(line, lineNo, current, issues);
            } else {
                parseTokenLine(line, lineNo, current, issues);
            }
        }
        flushDraft(current, drafts, issues);

        if (drafts.isEmpty()) {
            issues.add(new ValidationIssue(1, 1, "NO_CONLLU_SENTENCE",
                    "Il file CoNLL-U non contiene frasi con token ordinari"));
        }

        List<Sentence> sentences = new ArrayList<Sentence>();
        List<Token> tokens = new ArrayList<Token>();
        int sentenceOrdinal = 0;
        int tokenOrdinal = 0;
        int previousSentenceEnd = -1;
        int textCodePoints = doc.cleanText.codePointCount(0, doc.cleanText.length());

        for (SentenceDraft draft : drafts) {
            if (draft.tokens.isEmpty()) {
                continue;
            }

            int sentenceBeginCp = draft.startCodePoint != null
                    ? draft.startCodePoint.intValue() : draft.tokens.get(0).beginCodePoint;
            int sentenceEndCp = draft.endCodePoint != null
                    ? draft.endCodePoint.intValue()
                    : draft.tokens.get(draft.tokens.size() - 1).endCodePoint;

            if ((draft.startCodePoint == null) != (draft.endCodePoint == null)) {
                issues.add(new ValidationIssue(draft.firstLine, 1, "INCOMPLETE_SENTENCE_OFFSETS",
                        "Specificare entrambi i commenti # start_char e # end_char, oppure nessuno"));
            }
            if (!validRange(sentenceBeginCp, sentenceEndCp, textCodePoints)) {
                issues.add(new ValidationIssue(draft.firstLine, 1, "INVALID_SENTENCE_OFFSETS",
                        "Offset di frase non validi: " + sentenceBeginCp + ":" + sentenceEndCp));
                continue;
            }

            int sentenceBeginChar = toCharOffset(doc.cleanText, sentenceBeginCp);
            int sentenceEndChar = toCharOffset(doc.cleanText, sentenceEndCp);
            if (sentenceBeginChar < previousSentenceEnd) {
                issues.add(new ValidationIssue(draft.firstLine, 1, "OVERLAPPING_SENTENCES",
                        "Le frasi CoNLL-U devono essere ordinate e non sovrapposte"));
            }
            previousSentenceEnd = Math.max(previousSentenceEnd, sentenceEndChar);

            Paragraph paragraph = containingParagraph(doc.paragraphs, sentenceBeginChar, sentenceEndChar);
            Heading heading = null;
            boolean inHeadingTitle = false;
            if (paragraph == null) {
                heading = containingHeadingTitle(doc.allHeadings, sentenceBeginChar, sentenceEndChar);
                inHeadingTitle = heading != null;
            }
            if (paragraph == null && heading == null) {
                issues.add(new ValidationIssue(draft.firstLine, 1, "SENTENCE_OUTSIDE_STRUCTURE",
                        "La frase " + sentenceBeginCp + ":" + sentenceEndCp
                        + " non è interamente contenuta in un paragrafo o nel titolo di un'intestazione"));
                continue;
            }

            String sentenceText = doc.cleanText.substring(sentenceBeginChar, sentenceEndChar);
            if (draft.textComment != null && !draft.textComment.equals(sentenceText)) {
                doc.warnings.add(new ValidationIssue(draft.firstLine, 1,
                        "CONLLU_SENTENCE_TEXT_MISMATCH",
                        "Il commento # text non coincide esattamente con la sottostringa indicata dagli offset"));
            }

            Sentence sentence = new Sentence();
            sentence.ordinal = ++sentenceOrdinal;
            sentence.id = "sentence-" + sentence.ordinal;
            sentence.conlluSentId = blankToNull(draft.sentId);
            sentence.paragraph = paragraph;
            sentence.heading = heading;
            sentence.inHeadingTitle = inHeadingTitle;
            sentence.beginChar = sentenceBeginChar;
            sentence.endChar = sentenceEndChar;
            sentence.text = sentenceText;

            int previousTokenEnd = sentenceBeginChar;
            for (TokenDraft tokenDraft : draft.tokens) {
                if (!validRange(tokenDraft.beginCodePoint, tokenDraft.endCodePoint, textCodePoints)) {
                    issues.add(new ValidationIssue(tokenDraft.line, 1, "INVALID_TOKEN_OFFSETS",
                            "Offset del token non validi: " + tokenDraft.beginCodePoint
                            + ":" + tokenDraft.endCodePoint));
                    continue;
                }

                int tokenBeginChar = toCharOffset(doc.cleanText, tokenDraft.beginCodePoint);
                int tokenEndChar = toCharOffset(doc.cleanText, tokenDraft.endCodePoint);
                if (tokenBeginChar < sentenceBeginChar || tokenEndChar > sentenceEndChar) {
                    issues.add(new ValidationIssue(tokenDraft.line, 1, "TOKEN_OUTSIDE_SENTENCE",
                            "Il token " + tokenDraft.id + " non è contenuto negli offset della frase"));
                    continue;
                }
                if (tokenBeginChar < previousTokenEnd) {
                    issues.add(new ValidationIssue(tokenDraft.line, 1, "OVERLAPPING_TOKENS",
                            "I token devono essere ordinati e non sovrapposti"));
                    continue;
                }
                if (containsNonWhitespace(doc.cleanText, previousTokenEnd, tokenBeginChar)) {
                    issues.add(new ValidationIssue(tokenDraft.line, 1, "UNANALYZED_SENTENCE_TEXT",
                            "Una porzione non bianca della frase non è coperta da token CoNLL-U"));
                }

                String anchored = doc.cleanText.substring(tokenBeginChar, tokenEndChar);
                if (!tokenDraft.form.equals(anchored)) {
                    issues.add(new ValidationIssue(tokenDraft.line, 2, "TOKEN_FORM_MISMATCH",
                            "FORM '" + tokenDraft.form + "' non coincide con la sottostringa '"
                            + anchored + "' individuata dagli offset"));
                    continue;
                }

                Token token = new Token();
                token.ordinal = ++tokenOrdinal;
                token.id = "token-" + token.ordinal;
                token.conlluId = tokenDraft.id;
                token.text = anchored;
                token.beginChar = tokenBeginChar;
                token.endChar = tokenEndChar;
                token.lemma = underscoreToNull(tokenDraft.columns[2]);
                token.upos = underscoreToNull(tokenDraft.columns[3]);
                token.xpos = underscoreToNull(tokenDraft.columns[4]);
                token.feats = underscoreToNull(tokenDraft.columns[5]);
                token.head = underscoreToNull(tokenDraft.columns[6]);
                token.deprel = underscoreToNull(tokenDraft.columns[7]);
                token.deps = underscoreToNull(tokenDraft.columns[8]);
                token.misc = underscoreToNull(tokenDraft.columns[9]);
                token.sentence = sentence;
                sentence.tokens.add(token);
                tokens.add(token);
                previousTokenEnd = tokenEndChar;
            }

            if (sentence.tokens.isEmpty()) {
                issues.add(new ValidationIssue(draft.firstLine, 1, "EMPTY_SENTENCE_AFTER_VALIDATION",
                        "La frase non contiene token validi"));
            } else if (containsNonWhitespace(doc.cleanText, previousTokenEnd, sentenceEndChar)) {
                issues.add(new ValidationIssue(draft.firstLine, 1, "UNANALYZED_SENTENCE_TEXT",
                        "Una porzione non bianca della frase non è coperta da token CoNLL-U"));
            }
            sentences.add(sentence);
        }

        validateParagraphCoverage(doc, sentences, issues);
        if (!issues.isEmpty()) {
            throw new ConlluValidationException(issues);
        }

        clearExistingSegmentation(doc);
        for (Sentence sentence : sentences) {
            if (sentence.paragraph != null) {
                sentence.paragraph.sentences.add(sentence);
            } else if (sentence.heading != null) {
                sentence.heading.titleSentences.add(sentence);
            }
            doc.sentences.add(sentence);
        }
        doc.tokens.addAll(tokens);
        doc.segmentationMethod = "conllu";
        doc.conlluFileName = conlluFileName;
    }

    private static void parseComment(String line, int lineNo, SentenceDraft current,
                                     List<ValidationIssue> issues) {
        Matcher matcher = COMMENT.matcher(line);
        if (!matcher.matches()) {
            return;
        }
        String key = normalizeKey(matcher.group(1));
        String value = matcher.group(2).trim();
        if (current.firstLine == 0) {
            current.firstLine = lineNo;
        }
        if ("sentid".equals(key)) {
            current.sentId = value;
        } else if ("text".equals(key)) {
            current.textComment = value;
        } else if ("startchar".equals(key)) {
            current.startCodePoint = parseNonNegative(value, lineNo, "INVALID_SENTENCE_START", issues);
        } else if ("endchar".equals(key)) {
            current.endCodePoint = parseNonNegative(value, lineNo, "INVALID_SENTENCE_END", issues);
        }
    }

    private static void parseTokenLine(String line, int lineNo, SentenceDraft current,
                                       List<ValidationIssue> issues) {
        if (current.firstLine == 0) {
            current.firstLine = lineNo;
        }
        String[] columns = line.split("\\t", -1);
        if (columns.length != 10) {
            issues.add(new ValidationIssue(lineNo, 1, "INVALID_CONLLU_COLUMNS",
                    "Una riga CoNLL-U deve contenere esattamente 10 colonne separate da tab"));
            return;
        }
        String id = columns[0];
        if (RANGE_ID.matcher(id).matches() || EMPTY_NODE_ID.matcher(id).matches()) {
            return;
        }
        if (!INTEGER_ID.matcher(id).matches()) {
            issues.add(new ValidationIssue(lineNo, 1, "INVALID_CONLLU_ID",
                    "ID CoNLL-U non valido: " + id));
            return;
        }
        if (columns[1].isEmpty() || "_".equals(columns[1])) {
            issues.add(new ValidationIssue(lineNo, 2, "MISSING_TOKEN_FORM",
                    "La colonna FORM è obbligatoria per i token ordinari"));
            return;
        }

        Map<String, String> misc = parseMisc(columns[9]);
        Integer begin = null;
        Integer end = null;
        String range = misc.get("tokenrange");
        if (range != null) {
            Matcher rangeMatcher = TOKEN_RANGE.matcher(range);
            if (rangeMatcher.matches()) {
                begin = parseNonNegative(rangeMatcher.group(1), lineNo, "INVALID_TOKEN_START", issues);
                end = parseNonNegative(rangeMatcher.group(2), lineNo, "INVALID_TOKEN_END", issues);
            } else {
                issues.add(new ValidationIssue(lineNo, 10, "INVALID_TOKEN_RANGE",
                        "TokenRange deve avere forma inizio:fine"));
            }
        } else {
            String beginValue = misc.get("startchar");
            String endValue = misc.get("endchar");
            if (beginValue != null) {
                begin = parseNonNegative(beginValue, lineNo, "INVALID_TOKEN_START", issues);
            }
            if (endValue != null) {
                end = parseNonNegative(endValue, lineNo, "INVALID_TOKEN_END", issues);
            }
        }
        if (begin == null || end == null) {
            issues.add(new ValidationIssue(lineNo, 10, "MISSING_TOKEN_OFFSETS",
                    "Ogni token ordinario deve avere TokenRange oppure start_char/end_char nel campo MISC"));
            return;
        }

        TokenDraft draft = new TokenDraft();
        draft.line = lineNo;
        draft.id = id;
        draft.form = columns[1];
        draft.columns = columns;
        draft.beginCodePoint = begin.intValue();
        draft.endCodePoint = end.intValue();
        current.tokens.add(draft);
    }

    private static Map<String, String> parseMisc(String value) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        if (value == null || value.isEmpty() || "_".equals(value)) {
            return result;
        }
        String[] fields = value.split("\\|");
        for (String field : fields) {
            int equals = field.indexOf('=');
            if (equals <= 0) {
                continue;
            }
            result.put(normalizeKey(field.substring(0, equals)), field.substring(equals + 1));
        }
        return result;
    }

    private static void flushDraft(SentenceDraft draft, List<SentenceDraft> drafts,
                                   List<ValidationIssue> issues) {
        if (draft.firstLine == 0 && draft.tokens.isEmpty()
                && draft.sentId == null && draft.textComment == null) {
            return;
        }
        if (draft.tokens.isEmpty()) {
            issues.add(new ValidationIssue(draft.firstLine == 0 ? 1 : draft.firstLine, 1,
                    "CONLLU_SENTENCE_WITHOUT_TOKENS",
                    "Un blocco di frase CoNLL-U non contiene token ordinari"));
        }
        drafts.add(draft);
    }

    private static void validateParagraphCoverage(ParsedTextDocument doc, List<Sentence> sentences,
                                                  List<ValidationIssue> issues) {
        for (Paragraph paragraph : doc.paragraphs) {
            int cursor = paragraph.beginChar;
            boolean found = false;
            boolean failed = false;
            for (Sentence sentence : sentences) {
                if (sentence.paragraph != paragraph) {
                    continue;
                }
                found = true;
                if (containsNonWhitespace(doc.cleanText, cursor, sentence.beginChar)) {
                    issues.add(new ValidationIssue(1, 1, "UNANALYZED_PARAGRAPH_TEXT",
                            "Il paragrafo " + paragraph.id
                            + " contiene testo non coperto da frasi CoNLL-U"));
                    failed = true;
                    break;
                }
                cursor = Math.max(cursor, sentence.endChar);
            }
            if (!failed && (!found || containsNonWhitespace(doc.cleanText, cursor, paragraph.endChar))) {
                issues.add(new ValidationIssue(1, 1, "UNANALYZED_PARAGRAPH_TEXT",
                        "Il paragrafo " + paragraph.id
                        + " non è interamente coperto dalle frasi CoNLL-U"));
            }
        }
    }

    private static void clearExistingSegmentation(ParsedTextDocument doc) {
        doc.sentences.clear();
        doc.tokens.clear();
        for (Paragraph paragraph : doc.paragraphs) {
            paragraph.sentences.clear();
        }
        for (Heading heading : doc.allHeadings) {
            heading.titleSentences.clear();
        }
    }

    private static boolean containsNonWhitespace(String text, int begin, int end) {
        int safeBegin = Math.max(0, begin);
        int safeEnd = Math.min(text.length(), end);
        for (int i = safeBegin; i < safeEnd;) {
            int cp = text.codePointAt(i);
            if (!Character.isWhitespace(cp)) {
                return true;
            }
            i += Character.charCount(cp);
        }
        return false;
    }

    private static Paragraph containingParagraph(List<Paragraph> paragraphs, int begin, int end) {
        for (Paragraph paragraph : paragraphs) {
            if (begin >= paragraph.beginChar && end <= paragraph.endChar) {
                return paragraph;
            }
        }
        return null;
    }

    private static Heading containingHeadingTitle(List<Heading> headings, int begin, int end) {
        for (Heading heading : headings) {
            if (heading.titleSegment != null
                    && begin >= heading.titleSegment.beginChar
                    && end <= heading.titleSegment.endChar) {
                return heading;
            }
        }
        return null;
    }

    private static boolean validRange(int begin, int end, int max) {
        return begin >= 0 && end > begin && end <= max;
    }

    private static int toCharOffset(String text, int codePointOffset) {
        return text.offsetByCodePoints(0, codePointOffset);
    }

    private static Integer parseNonNegative(String value, int lineNo, String code,
                                            List<ValidationIssue> issues) {
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed < 0) {
                throw new NumberFormatException("negative");
            }
            return Integer.valueOf(parsed);
        } catch (NumberFormatException e) {
            issues.add(new ValidationIssue(lineNo, 1, code,
                    "È richiesto un intero non negativo: " + value));
            return null;
        }
    }

    private static String normalizeKey(String value) {
        return value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    private static String underscoreToNull(String value) {
        return value == null || value.isEmpty() || "_".equals(value) ? null : value;
    }

    private static String blankToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private static final class SentenceDraft {
        int firstLine;
        String sentId;
        String textComment;
        Integer startCodePoint;
        Integer endCodePoint;
        final List<TokenDraft> tokens = new ArrayList<TokenDraft>();
    }

    private static final class TokenDraft {
        int line;
        String id;
        String form;
        String[] columns;
        int beginCodePoint;
        int endCodePoint;
    }
}
