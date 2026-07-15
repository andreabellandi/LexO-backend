package it.cnr.ilc.lexo.manager.text.model;

import java.util.ArrayList;
import java.util.List;

public class Heading {
    public String id;
    public String number;
    public String text;
    public int level;
    public int beginChar;
    public int endChar;
    public Heading parent;
    public TitleSegment titleSegment;
    public final List<Heading> children = new ArrayList<Heading>();
    public final List<Paragraph> paragraphs = new ArrayList<Paragraph>();
    public final List<Sentence> titleSentences = new ArrayList<Sentence>();
}
