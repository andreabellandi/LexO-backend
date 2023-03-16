/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the lenght path levels of a relation stating from a lexical sense")
public class RelationPath implements Data {

    @ApiModelProperty(value = "lexical entry")
    private String lexicalEntry;
    @ApiModelProperty(value = "path lenght level")
    private int lenght;
    @ApiModelProperty(value = "target sense")
    private String lexicalSense;

    public String getLexicalEntry() {
        return lexicalEntry;
    }

    public void setLexicalEntry(String lexicalEntry) {
        this.lexicalEntry = lexicalEntry;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public String getLexicalSense() {
        return lexicalSense;
    }

    public void setLexicalSense(String lexicalSense) {
        this.lexicalSense = lexicalSense;
    }

    public RelationPath() {
    }

}
