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
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntityLinksItem.Link;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the etymology of a lexical entry")
public class Etymology extends Entity implements Data {

    @ApiModelProperty(value = "etymology IRI")
    private String etymology;
    @ApiModelProperty(value = "label")
    private String label;
    @ApiModelProperty(value = "the counting of the links of a lexical entry and their type: Reference (see Also, same As) Bilbiography, Multimedia, Attestation, Other)")
    private ArrayList<Link> links;
    @ApiModelProperty(value = "string representing the name of who made the hypothesis")
    private String hypothesisOf;

    @ApiModelProperty(value = "etymology note", example = "some note ...")
    private String note;

    public String getEtymology() {
        return etymology;
    }

    public void setEtymology(String etymology) {
        this.etymology = etymology;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getHypothesisOf() {
        return hypothesisOf;
    }

    public void setHypothesisOf(String hypothesisOf) {
        this.hypothesisOf = hypothesisOf;
    }
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

}
