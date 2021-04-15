/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;

/**
 *
 * @author andreabellandi
 */
public class LexicalEntryItem implements Data {

    private String status;
    private String revisor;
    private String type;
    private String pos;
    private String label;
    private String language;
    private String author;
    private String note;
    private boolean hasChildren;
    private String lexicalEntry;
    private String lexicalEntryInstanceName;
    private ArrayList<Morphology> morphology;

    public LexicalEntryItem() {
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRevisor() {
        return revisor;
    }

    public void setRevisor(String revisor) {
        this.revisor = revisor;
    }

    public String getLexicalEntry() {
        return lexicalEntry;
    }

    public void setLexicalEntry(String lexicalEntry) {
        this.lexicalEntry = lexicalEntry;
    }

    public String getLexicalEntryInstanceName() {
        return lexicalEntryInstanceName;
    }

    public void setLexicalEntryInstanceName(String lexicalEntryInstanceName) {
        this.lexicalEntryInstanceName = lexicalEntryInstanceName;
    }

    public ArrayList<Morphology> getMorphology() {
        return morphology;
    }

    public void setMorphology(ArrayList<Morphology> morphology) {
        this.morphology = morphology;
    }

    

}
