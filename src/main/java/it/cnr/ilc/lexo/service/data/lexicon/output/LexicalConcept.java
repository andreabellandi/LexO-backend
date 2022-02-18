/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a lexical concept")
public class LexicalConcept implements Data {

    @ApiModelProperty(value = "lexical concept IRI")
    private String lexicalConcept;
    @ApiModelProperty(value = "lexical concept short IRI")
    private String lexicalConceptInstanceName;
    @ApiModelProperty(value = "lexical concept label")
    private String label;
    @ApiModelProperty(value = "lexical concept definition")
    private String definition;
    @ApiModelProperty(value = "lexical concept creator", example = "user3")
    private String creator;
    @ApiModelProperty(value = "lexical concept note", example = "some note ...")
    private String note;
    @ApiModelProperty(value = "lexical concept creation date")
    private String creationDate;
    @ApiModelProperty(value = "lexical concept last update")
    private String lastUpdate;

    public String getLexicalConcept() {
        return lexicalConcept;
    }

    public void setLexicalConcept(String lexicalConcept) {
        this.lexicalConcept = lexicalConcept;
    }

    public String getLexicalConceptInstanceName() {
        return lexicalConceptInstanceName;
    }

    public void setLexicalConceptInstanceName(String lexicalConceptInstanceName) {
        this.lexicalConceptInstanceName = lexicalConceptInstanceName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
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
