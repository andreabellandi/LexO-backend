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

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the main features of an etymology")
public class EtymologyItem extends Entity implements Data {

    @ApiModelProperty(value = "etymology label")
    private String label;
    @ApiModelProperty(value = "etymology hypothesis author")
    private String hypothesisOf;
    @ApiModelProperty(value = "etymology note", example = "textual content ...")
    private String note;
    @ApiModelProperty(value = "etymology IRI")
    private String etymology;
    @ApiModelProperty(value = "lexical entry IRI")
    private String lexicalEntry;

    public EtymologyItem() {
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

    public String getEtymology() {
        return etymology;
    }

    public void setEtymology(String etymology) {
        this.etymology = etymology;
    }

    public String getLexicalEntry() {
        return lexicalEntry;
    }

    public void setLexicalEntry(String lexicalEntry) {
        this.lexicalEntry = lexicalEntry;
    }

    public String getHypothesisOf() {
        return hypothesisOf;
    }

    public void setHypothesisOf(String hypothesisOf) {
        this.hypothesisOf = hypothesisOf;
    }

}
