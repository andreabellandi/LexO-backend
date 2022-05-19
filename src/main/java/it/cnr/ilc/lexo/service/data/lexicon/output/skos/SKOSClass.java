/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.skos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.lexicon.output.GroupedLinkedEntity;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a SKOS class")
public class SKOSClass implements Data {

    @ApiModelProperty(value = "label class")
    private String label;
    @ApiModelProperty(value = "the list of relations the skos class is involved in")
    private List<GroupedLinkedEntity> entities;
    @ApiModelProperty(value = "creator")
    private String creator;
    @ApiModelProperty(value = "creation date")
    private String creationDate;
    @ApiModelProperty(value = "llast update")
    private String lastUpdate;
    
    @ApiModelProperty(value = "if the class is certain")
    private double confidence;

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public List<GroupedLinkedEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<GroupedLinkedEntity> entities) {
        this.entities = entities;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
