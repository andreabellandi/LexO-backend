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
@ApiModel(description = "Output model representing the core data of a form")
public class FormCore implements Data {

    @ApiModelProperty(value = "form IRI", example = "namespace:MUSabbacchiareVERB_PHUabbacchiammo_P1IR")
    private String form;
    @ApiModelProperty(value = "form short IRI", example = "MUSabbacchiareVERB_PHUabbacchiammo_P1IR")
    private String formInstanceName;
    @ApiModelProperty(value = "a list of representation properties")
    private List<Property> label;
    @ApiModelProperty(value = "form types", example = "canonicalForm", allowableValues = "lexicalForm, canonicalForm, otherForm")
    private String type;
    @ApiModelProperty(value = "morphological traits of the form")
    private ArrayList<Morphology> morphology;
    @ApiModelProperty(value = "morphological traits inherited by the lexical entry")
    private ArrayList<Morphology> inheritedMorphology;
    @ApiModelProperty(value = "the counting of the links of a form and their type: Reference (see Also, same As, bilbiography, ...), Multimedia, Attestation, Other)")
    private ArrayList<LexicalEntryElementItem> links;
    @ApiModelProperty(value = "form creator (who creates the entry) ", example = "user3")
    private String creator;
    @ApiModelProperty(value = "form note", example = "some note ...")
    private String note;
    @ApiModelProperty(value = "form creation date")
    private String creationDate;
    @ApiModelProperty(value = "form last update")
    private String lastUpdate;

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getFormInstanceName() {
        return formInstanceName;
    }

    public void setFormInstanceName(String formInstanceName) {
        this.formInstanceName = formInstanceName;
    }

    public List<Property> getLabel() {
        return label;
    }

    public void setLabel(List<Property> label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public ArrayList<LexicalEntryElementItem> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<LexicalEntryElementItem> links) {
        this.links = links;
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
