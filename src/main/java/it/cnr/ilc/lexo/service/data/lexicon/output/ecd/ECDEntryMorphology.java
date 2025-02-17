/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.ecd;

import it.cnr.ilc.lexo.service.data.output.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import java.util.ArrayList;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing all the morphological forms of an ECD entry")
public class ECDEntryMorphology extends Entity implements Data {

    @ApiModelProperty(value = "morphological traits of the form")
    private ArrayList<Morphology> morphology;
    @ApiModelProperty(value = "morphological traits inherited by the lexical entry")
    private ArrayList<Morphology> inheritedMorphology;
    @ApiModelProperty(value = "form type", allowableValues = "canonicalForm, otherForm, lexicalForm")
    private String type;
    @ApiModelProperty(value = "form written represenation")
    private String label;
    @ApiModelProperty(value = "form note")
    private String note;
    @ApiModelProperty(value = "form phonetic representation")
    private String phoneticRep;
    @ApiModelProperty(value = "form IRI")
    private String form;
    @ApiModelProperty(value = "pos of the lexical entry")
    private String pos;

    public ArrayList<Morphology> getMorphology() {
        return morphology;
    }

    public void setMorphology(ArrayList<Morphology> morphology) {
        this.morphology = morphology;
    }

    public ArrayList<Morphology> getInheritedMorphology() {
        return inheritedMorphology;
    }

    public void setInheritedMorphology(ArrayList<Morphology> inheritedMorphology) {
        this.inheritedMorphology = inheritedMorphology;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhoneticRep() {
        return phoneticRep;
    }

    public void setPhoneticRep(String phoneticRep) {
        this.phoneticRep = phoneticRep;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

}
