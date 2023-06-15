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
@ApiModel(description = "Output model representing metadata of a lexical entity")
public class Metadata implements Data {

    @ApiModelProperty(value = "lexical entity id")
    private String lexicalEntity;
    @ApiModelProperty(value = "confidence")
    private double confidence;
    @ApiModelProperty(value = "when the entity was created")
    private String creationDate;
    @ApiModelProperty(value = "when the entity was completed")
    private String completionDate;
    @ApiModelProperty(value = "when th entity was reviewed")
    private String revisionDate;
    @ApiModelProperty(value = "the last update")
    private String lastUpdate;
    @ApiModelProperty(value = "note")
    private String note;
    @ApiModelProperty(value = "whose created the entity")
    private String creator;
    @ApiModelProperty(value = "whose completed the entity")
    private String author;
    @ApiModelProperty(value = "whose reviewed the entity")
    private String reviewer;
    @ApiModelProperty(value = "status")
    private String status;
    @ApiModelProperty(value = "provenance (dublin core source)")
    private String provenance;

    public String getLexicalEntity() {
        return lexicalEntity;
    }

    public void setLexicalEntity(String lexicalEntity) {
        this.lexicalEntity = lexicalEntity;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
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

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

}
