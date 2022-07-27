/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.lexicon.output.skos.SKOSClass;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a lexical concept as a tree item")
public class LexicalConceptItem extends SKOSClass implements Data {

    @ApiModelProperty(value = "IRI")
    private String lexicalConcept;
    @ApiModelProperty(value = "short IRI")
    private String lexicalConceptInstanceName;
    private boolean hasChildren;
    private int children;
    private String defaultLabel;

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

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public String getDefaultLabel() {
        return defaultLabel;
    }

    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }

}
