/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the etymology of a lexical entry")
public class Etymology implements Data {

    @ApiModelProperty(value = "etymology IRI")
    private String etymology;
    @ApiModelProperty(value = "etymology short IRI")
    private String etymologyInstanceName;
    @ApiModelProperty(value = "label")
    private String label;
    @ApiModelProperty(value = "confidence of the hypothesis")
    private double confidence;
    @ApiModelProperty(value = "string representing the name of who made the hypothesis")
    private String hypothesisOf;
    @ApiModelProperty(value = "etymology creator", example = "user3")
    private String creator;
    @ApiModelProperty(value = "etymology note", example = "some note ...")
    private String note;
    @ApiModelProperty(value = "etymology creation date")
    private String creationDate;
    @ApiModelProperty(value = "etymology last update")
    private String lastUpdate;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getHypothesisOf() {
        return hypothesisOf;
    }

    public void setHypothesisOf(String hypothesisOf) {
        this.hypothesisOf = hypothesisOf;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
