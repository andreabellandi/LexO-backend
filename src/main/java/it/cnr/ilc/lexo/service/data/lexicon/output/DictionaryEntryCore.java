/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import it.cnr.ilc.lexo.service.data.output.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a dictionary entry")
public class DictionaryEntryCore extends Entity implements Data {

    @ApiModelProperty(value = "dictionary entry IRI")
    private String dictionaryEntry;
    @ApiModelProperty(value = "type of the dictionary entry")
    private ArrayList<String> type;
    @ApiModelProperty(value = "label")
    private String label;
    private ArrayList<String> pos;
    private String status;
    private String revisor;
    private String author;
    private String completionDate;
    private String revisionDate;
    private String language;
    private ArrayList<String> images;
    private boolean hasChildren;
    private String note;
    private ArrayList<LinkedEntity> seeAlso;
    private ArrayList<LinkedEntity> sameDictionaryEntryAs;

    public String getDictionaryEntry() {
        return dictionaryEntry;
    }

    public void setDictionaryEntry(String dictionaryEntry) {
        this.dictionaryEntry = dictionaryEntry;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<LinkedEntity> getSeeAlso() {
        return seeAlso;
    }

    public void setSeeAlso(ArrayList<LinkedEntity> seeAlso) {
        this.seeAlso = seeAlso;
    }

    public ArrayList<LinkedEntity> getSameDictionaryEntryAs() {
        return sameDictionaryEntryAs;
    }

    public void setSameDictionaryEntryAs(ArrayList<LinkedEntity> sameDictionaryEntryAs) {
        this.sameDictionaryEntryAs = sameDictionaryEntryAs;
    }

    public void setRevisor(String revisor) {
        this.revisor = revisor;
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

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<String> getPos() {
        return pos;
    }

    public void setPos(ArrayList<String> pos) {
        this.pos = pos;
    }

}
