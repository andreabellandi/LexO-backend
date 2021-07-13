/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;

/**
 *
 * @author andreabellandi
 */

@ApiModel(description = "Input model representing form filter options")
public class FormBySenseFilter implements Data {

    @ApiModelProperty(value = "chars sequence to search", example = "pesca", allowEmptyValue = false)
    private String form;
    @ApiModelProperty(value = "the type of form the serach is performed on", allowableValues = "keyword, lemma", example = "lemma", allowEmptyValue = false)
    private String formType;
    @ApiModelProperty(value = "the lexical entry ID", example = "MUSpescaNOUN", allowEmptyValue = false)
    private String lexicalEntry;
    @ApiModelProperty(value = "senses to be extend to with a lexico semantic relation", example = "USem72095pesca", allowEmptyValue = true)
    private ArrayList<String> senseUris;
    @ApiModelProperty(value = "the lexico-semantic relation of the list of senses, the search has to be extended to", allowableValues = "synonym, hypernym, hyponym", example = "hyponym", allowEmptyValue = true)
    private String extendTo;
    @ApiModelProperty(value = "the depth of the lexico-semantic relation, the search has to get up to  (only 1 for synonym)", allowableValues = "1, 2, 3", example = "3", allowEmptyValue = false)
    private int extensionDegree;

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getLexicalEntry() {
        return lexicalEntry;
    }

    public void setLexicalEntry(String lexicalEntry) {
        this.lexicalEntry = lexicalEntry;
    }

    public ArrayList<String> getSenseUris() {
        return senseUris;
    }

    public void setSenseUris(ArrayList<String> senseUris) {
        this.senseUris = senseUris;
    }

    public String getExtendTo() {
        return extendTo;
    }

    public void setExtendTo(String extendTo) {
        this.extendTo = extendTo;
    }

    public int getExtensionDegree() {
        return extensionDegree;
    }

    public void setExtensionDegree(int extensionDegree) {
        this.extensionDegree = extensionDegree;
    }

}
