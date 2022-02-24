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
@ApiModel(description = "Output model representing a lexical concept")
public class LexicalConcept extends SKOSClass implements Data {

    @ApiModelProperty(value = "IRI")
    private String lexicalConcept;
    @ApiModelProperty(value = "short IRI")
    private String lexicalConceptInstanceName;

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

}
