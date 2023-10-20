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
@ApiModel(description = "Output model representing a for restriction")
public class FormRestriction extends Entity implements Data {

    @ApiModelProperty(value = "IRI of the form restriction")
    private String formRestriction;
    @ApiModelProperty(value = "form restriction note")
    private String note;
    @ApiModelProperty(value = "form restriction example")
    private String example;
    @ApiModelProperty(value = "form restriction label")
    private String label;
    @ApiModelProperty(value = "morphological features")
    private ArrayList<Morphology> morphology;

    public String getFormRestriction() {
        return formRestriction;
    }

    public void setFormRestriction(String formRestriction) {
        this.formRestriction = formRestriction;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
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

}
