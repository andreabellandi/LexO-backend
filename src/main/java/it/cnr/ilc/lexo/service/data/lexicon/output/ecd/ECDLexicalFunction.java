/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.ecd;

import it.cnr.ilc.lexo.service.data.output.Entity;
import io.swagger.annotations.ApiModel;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the list of lexical functions of a sense")
public class ECDLexicalFunction extends Entity implements Data {

    private String id;
    private String lexicalFunction;
    private String type;
    private String govPattern;
    private boolean fusedElement;
    private String senseTarget;
    private String definition;
    private String pos;
    private String lexicalEntryLabel;
    private String dictionaryEntryLabel;

    public ECDLexicalFunction() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLexicalFunction() {
        return lexicalFunction;
    }

    public void setLexicalFunction(String lexicalFunction) {
        this.lexicalFunction = lexicalFunction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGovPattern() {
        return govPattern;
    }

    public void setGovPattern(String govPattern) {
        this.govPattern = govPattern;
    }

    public boolean isFusedElement() {
        return fusedElement;
    }

    public void setFusedElement(boolean fusedElement) {
        this.fusedElement = fusedElement;
    }

    public String getSenseTarget() {
        return senseTarget;
    }

    public void setSenseTarget(String senseTarget) {
        this.senseTarget = senseTarget;
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

    public String getLexicalEntryLabel() {
        return lexicalEntryLabel;
    }

    public void setLexicalEntryLabel(String lexicalEntryLabel) {
        this.lexicalEntryLabel = lexicalEntryLabel;
    }

    public String getDictionaryEntryLabel() {
        return dictionaryEntryLabel;
    }

    public void setDictionaryEntryLabel(String dictionaryEntryLabel) {
        this.dictionaryEntryLabel = dictionaryEntryLabel;
    }

}
