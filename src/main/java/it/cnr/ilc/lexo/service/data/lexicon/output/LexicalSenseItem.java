/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
public class LexicalSenseItem implements Data {

    private String sense;
    private String senseInstanceName;
    private String lexicalEntry;
    private String pos;
    private boolean hasChildren;
    private String label;
    private String definition;
    private String author;
    private String note;
    private String usage;
    private String concept;
    private String conceptInstanceName;

    public String getSense() {
        return sense;
    }

    public void setSense(String sense) {
        this.sense = sense;
    }

    public String getSenseInstanceName() {
        return senseInstanceName;
    }

    public void setSenseInstanceName(String senseInstanceName) {
        this.senseInstanceName = senseInstanceName;
    }

    public String getLexicalEntry() {
        return lexicalEntry;
    }

    public void setLexicalEntry(String lexicalEntry) {
        this.lexicalEntry = lexicalEntry;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
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

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getConceptInstanceName() {
        return conceptInstanceName;
    }

    public void setConceptInstanceName(String conceptInstanceName) {
        this.conceptInstanceName = conceptInstanceName;
    }

}
