package it.cnr.ilc.lexo.manager.text.model;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
    public String id;
    public int ordinal;
    public String text;
    public int beginChar;
    public int endChar;
    public String conlluSentId;
    public Paragraph paragraph;
    public Heading heading;
    public boolean inHeadingTitle;
    public final List<Token> tokens = new ArrayList<Token>();
}
