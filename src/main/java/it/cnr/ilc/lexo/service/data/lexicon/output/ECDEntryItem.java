/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import it.cnr.ilc.lexo.service.data.output.Entity;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author andreabellandi
 */
public class ECDEntryItem extends Entity implements Data {

    private String status;
    private String revisor;
    private ArrayList<String> pos;
    private String label;
    private String language;
    private String author;
    private String dictionaryEntry;
    private String completionDate;
    private String revisionDate;
    private ArrayList<String> type;
    private Map<String, String> seeAlso;
    private boolean hasChildren;

    public ECDEntryItem() {
    }

    public String getStatus() {
        return status;
    }

    public Map<String, String> getSeeAlso() {
        return seeAlso;
    }

    public void setSeeAlso(Map<String, String> seeAlso) {
        this.seeAlso = seeAlso;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
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

    public ArrayList<String> getPos() {
        return pos;
    }

    public void setPos(ArrayList<String> pos) {
        this.pos = pos;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDictionaryEntry() {
        return dictionaryEntry;
    }

    public void setDictionaryEntry(String dictionaryEntry) {
        this.dictionaryEntry = dictionaryEntry;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(String revisionDate) {
        this.revisionDate = revisionDate;
    }

}
