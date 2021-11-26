/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.pippo;

import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
public class NodeLinks implements Data {

    private boolean inferred;
    private int inHyponym;
    private int outHyponym;
    private int inHypernym;
    private int outHypernym;
    private int inSynonym;
    private int outSynonym;
    private int inMeronymTerm;
    private int outMeronymTerm;
    private int inPartMeronym;
    private int outPartMeronym;

    public boolean isInferred() {
        return inferred;
    }

    public void setInferred(boolean inferred) {
        this.inferred = inferred;
    }

    public int getInHyponym() {
        return inHyponym;
    }

    public void setInHyponym(int inHyponym) {
        this.inHyponym = inHyponym;
    }

    public int getOutHyponym() {
        return outHyponym;
    }

    public void setOutHyponym(int outHyponym) {
        this.outHyponym = outHyponym;
    }

    public int getInHypernym() {
        return inHypernym;
    }

    public void setInHypernym(int inHypernym) {
        this.inHypernym = inHypernym;
    }

    public int getOutHypernym() {
        return outHypernym;
    }

    public void setOutHypernym(int outHypernym) {
        this.outHypernym = outHypernym;
    }

    public int getInSynonym() {
        return inSynonym;
    }

    public void setInSynonym(int inSynonym) {
        this.inSynonym = inSynonym;
    }

    public int getOutSynonym() {
        return outSynonym;
    }

    public void setOutSynonym(int outSynonym) {
        this.outSynonym = outSynonym;
    }

    public int getInMeronymTerm() {
        return inMeronymTerm;
    }

    public void setInMeronymTerm(int inMeronymTerm) {
        this.inMeronymTerm = inMeronymTerm;
    }

    public int getOutMeronymTerm() {
        return outMeronymTerm;
    }

    public void setOutMeronymTerm(int outMeronymTerm) {
        this.outMeronymTerm = outMeronymTerm;
    }

    public int getInPartMeronym() {
        return inPartMeronym;
    }

    public void setInPartMeronym(int inPartMeronym) {
        this.inPartMeronym = inPartMeronym;
    }

    public int getOutPartMeronym() {
        return outPartMeronym;
    }

    public void setOutPartMeronym(int outPartMeronym) {
        this.outPartMeronym = outPartMeronym;
    }

}
