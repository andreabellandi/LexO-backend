/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the main features of an etymology")
public class EtymologyItem implements Data {

    @ApiModelProperty(value = "etymology label")
    private String label;
    @ApiModelProperty(value = "etymology confidence")
    private double confidence;
    @ApiModelProperty(value = "etymology hypothesis author")
    private String hypothesisOf;
    @ApiModelProperty(value = "creation author for the etymology", example = "user1")
    private String creator;
    @ApiModelProperty(value = "last update of the etymology")
    private String lastUpdate;
    @ApiModelProperty(value = "creation date of the etymology")
    private String creationDate;
    @ApiModelProperty(value = "etymology note", example = "textual content ...")
    private String note;
    @ApiModelProperty(value = "etymology IRI")
    private String etymology;
    @ApiModelProperty(value = "etymology short IRI")
    private String etymologyInstanceName;
    @ApiModelProperty(value = "lexical entry IRI")
    private String lexicalEntry;
    @ApiModelProperty(value = "lexical entry short IRI")
    private String lexicalEntryInstanceName;

    public EtymologyItem() {
    }

    public String getLabel() {
        return label;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEtymology() {
        return etymology;
    }

    public void setEtymology(String etymology) {
        this.etymology = etymology;
    }

    public String getEtymologyInstanceName() {
        return etymologyInstanceName;
    }

    public void setEtymologyInstanceName(String etymologyInstanceName) {
        this.etymologyInstanceName = etymologyInstanceName;
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

    public String getHypothesisOf() {
        return hypothesisOf;
    }

    public void setHypothesisOf(String hypothesisOf) {
        this.hypothesisOf = hypothesisOf;
    }

}
