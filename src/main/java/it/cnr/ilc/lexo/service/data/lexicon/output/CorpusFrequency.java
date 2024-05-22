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
import java.util.TreeMap;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a corpus frequency")
public class CorpusFrequency extends Entity implements Data {

    @ApiModelProperty(value = "IRI of the corpus frequency")
    private String corpusFrequency;
    @ApiModelProperty(value = "corpus")
    private String corpus;
    @ApiModelProperty(value = "note")
    private String note;
    @ApiModelProperty(value = "frequency")
    private int frequency;

    public String getCorpusFrequency() {
        return corpusFrequency;
    }

    public void setCorpusFrequency(String corpusFrequency) {
        this.corpusFrequency = corpusFrequency;
    }

    public String getCorpus() {
        return corpus;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

}
