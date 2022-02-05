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
@ApiModel(description = "Output model representing a component correspondant of a multiword")
public class ComponentCorrespondant implements Data {

    @ApiModelProperty(value = "component IRI")
    private String correspondant;
    @ApiModelProperty(value = "component short IRI")
    private String correspondantInstanceName;
    @ApiModelProperty(value = "the type of the component correspondant", allowableValues = "lexicalEntry, component, external")
    private String correspondantType;
    @ApiModelProperty(value = "component correspondant label (in case of external correspondant)")
    private String label;
    @ApiModelProperty(value = "morphological traits of the component correspondant")
    private ArrayList<Morphology> morphology;
    @ApiModelProperty(value = "position of the component correspondant in the decomposition")
    private int position;
    @ApiModelProperty(value = "component creator")
    private String creator;
    @ApiModelProperty(value = "component note")
    private String note;
    @ApiModelProperty(value = "component creation date")
    private String creationDate;
    @ApiModelProperty(value = "component last update")
    private String lastUpdate;

    public String getCorrespondant() {
        return correspondant;
    }

    public void setCorrespondant(String correspondant) {
        this.correspondant = correspondant;
    }

    public String getCorrespondantInstanceName() {
        return correspondantInstanceName;
    }

    public void setCorrespondantInstanceName(String correspondantInstanceName) {
        this.correspondantInstanceName = correspondantInstanceName;
    }

    public String getCorrespondantType() {
        return correspondantType;
    }

    public void setCorrespondantType(String correspondantType) {
        this.correspondantType = correspondantType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
