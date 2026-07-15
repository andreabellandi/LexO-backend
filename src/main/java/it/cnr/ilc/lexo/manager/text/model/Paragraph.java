package it.cnr.ilc.lexo.manager.text.model;

import java.util.ArrayList;
import java.util.List;

public class Paragraph {
    public String id;
    public int ordinal;
    public String text;
    public int beginChar;
    public int endChar;
    public Heading heading;
    public final List<Sentence> sentences = new ArrayList<Sentence>();
}
