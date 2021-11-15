/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.graphviz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the node summary")
public class SenseNodeSummary implements Data {

    private String id;
    private String label;
    private String definition;
    private String pos;
    private String IRI;
    private int hyponymNr;
    private int hypernymNr;
    private int meronymNr;
    private int holonymNr;
    private int incomingSynonymNr;
    private int outcomingSynonymNr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getIRI() {
        return IRI;
    }

    public void setIRI(String IRI) {
        this.IRI = IRI;
    }

    public int getHyponymNr() {
        return hyponymNr;
    }

    public void setHyponymNr(int hyponymNr) {
        this.hyponymNr = hyponymNr;
    }

    public int getHypernymNr() {
        return hypernymNr;
    }

    public void setHypernymNr(int hypernymNr) {
        this.hypernymNr = hypernymNr;
    }

    public int getMeronymNr() {
        return meronymNr;
    }

    public void setMeronymNr(int meronymNr) {
        this.meronymNr = meronymNr;
    }

    public int getHolonymNr() {
        return holonymNr;
    }

    public void setHolonymNr(int holonymNr) {
        this.holonymNr = holonymNr;
    }

    public int getIncomingSynonymNr() {
        return incomingSynonymNr;
    }

    public void setIncomingSynonymNr(int incomingSynonymNr) {
        this.incomingSynonymNr = incomingSynonymNr;
    }

    public int getOutcomingSynonymNr() {
        return outcomingSynonymNr;
    }

    public void setOutcomingSynonymNr(int outcomingSynonymNr) {
        this.outcomingSynonymNr = outcomingSynonymNr;
    }

}
