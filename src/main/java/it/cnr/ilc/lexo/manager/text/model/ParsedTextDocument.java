package it.cnr.ilc.lexo.manager.text.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParsedTextDocument {
    public String cleanText;
    public String segmentationMethod;
    public boolean frontMatterPresent;
    public String conlluFileName;
    public final Map<String, String> metadata = new LinkedHashMap<String, String>();
    public final List<Heading> rootHeadings = new ArrayList<Heading>();
    public final List<Heading> allHeadings = new ArrayList<Heading>();
    public final List<Paragraph> paragraphs = new ArrayList<Paragraph>();
    public final List<Sentence> sentences = new ArrayList<Sentence>();
    public final List<Token> tokens = new ArrayList<Token>();
    public final List<ValidationIssue> warnings = new ArrayList<ValidationIssue>();
}
