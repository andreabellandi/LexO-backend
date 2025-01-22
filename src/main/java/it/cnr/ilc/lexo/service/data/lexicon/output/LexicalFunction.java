/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;




@ApiModel(description = "Output model representing a lexical function")
public class LexicalFunction implements Data {

    @ApiModelProperty(value = "IRI")
    private String lexicalFunction;
    

    public String getLexicalFunction() {
        return lexicalFunction;
    }

    public void setLexicalFunction(String lexicalFunction) {
        this.lexicalFunction = lexicalFunction;
    }

   
}
