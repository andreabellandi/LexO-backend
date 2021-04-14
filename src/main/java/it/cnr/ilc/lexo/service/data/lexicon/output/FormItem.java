/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the main features of a form")
public class FormItem implements Data {

    @ApiModelProperty(value = "form type", allowableValues = "canonicalForm, otherForm")
    private String type;
    @ApiModelProperty(value = "form written represenation", example = "accedere")
    private String label;
    @ApiModelProperty(value = "creation author for the form", example = "user1")
    private String author;
    @ApiModelProperty(value = "form note", example = "textual content ...")
    private String note;
    @ApiModelProperty(value = "form phonetic representation", example = "a'tʃ:ɛdere")
    private String phoneticRep;
    @ApiModelProperty(value = "form morphology")
    private ArrayList<Morphology> morphology;
    @ApiModelProperty(value = "form IRI", example = "namespace:MUSaccedereVERB")
    private String form;
    @ApiModelProperty(value = "form short IRI", example = "MUSaccedereVERB")
    private String formInstanceName;

    public FormItem() {
    }

    public ArrayList<Morphology> getMorphology() {
        return morphology;
    }

    public void setMorphology(ArrayList<Morphology> morphology) {
        this.morphology = morphology;
    }

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

    public String getPhoneticRep() {
        return phoneticRep;
    }

    public void setPhoneticRep(String phoneticRep) {
        this.phoneticRep = phoneticRep;
    }

}
