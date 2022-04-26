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

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a component of a multiword")
public class Component implements Data {

    @ApiModelProperty(value = "component IRI")
    private String component;
    @ApiModelProperty(value = "component short IRI")
    private String componentInstanceName;
    @ApiModelProperty(value = "component label")
    private String label;
    @ApiModelProperty(value = "morphological traits of the component")
    private ArrayList<Morphology> morphology;
    @ApiModelProperty(value = "position of the component in the decomposition")
    private int position;
    @ApiModelProperty(value = "component creator", example = "user3")
    private String creator;
    @ApiModelProperty(value = "component note", example = "some note ...")
    private String note;
    @ApiModelProperty(value = "component creation date")
    private String creationDate;
    @ApiModelProperty(value = "component last update")
    private String lastUpdate;

    @ApiModelProperty(value = "if the component is certain")
    private double confidence;

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponentInstanceName() {
        return componentInstanceName;
    }

    public void setComponentInstanceName(String componentInstanceName) {
        this.componentInstanceName = componentInstanceName;
    }

    public String getLabel() {
        return label;
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

    public ArrayList<Morphology> getMorphology() {
        return morphology;
    }

    public void setMorphology(ArrayList<Morphology> morphology) {
        this.morphology = morphology;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
