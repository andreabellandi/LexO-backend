/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.queryExpansion;

import it.cnr.ilc.lexo.service.data.lexicon.output.*;
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
@ApiModel(description = "Output model representing the main features of a form")
public class Form extends Entity implements Data {

    @ApiModelProperty(value = "form written represenation", example = "accedere")
    private String label;
    @ApiModelProperty(value = "form morphology")
    private ArrayList<Morphology> morphology;
//    @ApiModelProperty(value = "form IRI", example = "namespace:MUSaccedereVERB")
//    private String formInstanceName;
    @ApiModelProperty(value = "lexical entry short IRI", example = "MUSaccedereVERB")
    private String formType;
    private String lexicalEntryInstanceName;
    private String targetSenseInstanceName;
    private String definition;

    public Form() {
    }

    public ArrayList<Morphology> getMorphology() {
        return morphology;
    }

    public void setMorphology(ArrayList<Morphology> morphology) {
        this.morphology = morphology;
    }

    public String getLexicalEntryInstanceName() {
        return lexicalEntryInstanceName;
    }

    public void setLexicalEntryInstanceName(String lexicalEntryInstanceName) {
        this.lexicalEntryInstanceName = lexicalEntryInstanceName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTargetSenseInstanceName() {
        return targetSenseInstanceName;
    }

    public void setTargetSenseInstanceName(String targetSenseInstanceName) {
        this.targetSenseInstanceName = targetSenseInstanceName;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

}
