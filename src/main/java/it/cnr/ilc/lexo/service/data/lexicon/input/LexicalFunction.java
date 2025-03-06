/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing the lexical function")
public class LexicalFunction implements Data {

    @ApiModelProperty(value = "source lexical sense", allowEmptyValue = false)
    private String source;
    @ApiModelProperty(value = "target lexical sense", allowEmptyValue = false)
    private String target;
    @ApiModelProperty(value = "lexical function", allowEmptyValue = false)
    private String lexicalFunction;
    @ApiModelProperty(value = "lexical function type", allowEmptyValue = false)
    private String type;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

}
