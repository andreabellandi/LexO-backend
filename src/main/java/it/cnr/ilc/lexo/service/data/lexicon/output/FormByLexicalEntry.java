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
import it.cnr.ilc.lexo.service.data.output.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the main features of a form ")
public class FormByLexicalEntry extends Entity implements Data {

    @ApiModelProperty(value = "form written represenation", example = "accedere")
    private String label;
    @ApiModelProperty(value = "form morphology")
    private ArrayList<Morphology> morphology;
    @ApiModelProperty(value = "form IRI", example = "namespace:MUSaccedereVERB")
    private String form;
    @ApiModelProperty(value = "form short IRI", example = "MUSaccedereVERB")
    private String formInstanceName;
    @ApiModelProperty(value = "lexical entry IRI", example = "namespace:MUSaccedereVERB")
    private String lexicalEntry;
    @ApiModelProperty(value = "lexical entry short IRI", example = "MUSaccedereVERB")
    private String lexicalEntryInstanceName;
    
    public FormByLexicalEntry() {
    }

    public ArrayList<Morphology> getMorphology() {
        return morphology;
    }

    public void setMorphology(ArrayList<Morphology> morphology) {
        this.morphology = morphology;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
