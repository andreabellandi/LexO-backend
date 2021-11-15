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
public class NodeLinks implements Data {

    private int hyponymNr;
    private int hypernymNr;
    private int meronymNr;
    private int holonymNr;
    private int incomingSynonymNr;
    private int outcomingSynonymNr;


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
