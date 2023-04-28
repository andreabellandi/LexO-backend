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
@ApiModel(description = "Input model representing a lexical entry updater")
public class LexicalEntryUpdater implements Data {

    @ApiModelProperty(value = "lexical entry relation/attribute", example = "label", allowEmptyValue = false,
            allowableValues = "label, type, language, status, note, confidence")
    private String relation;
    @ApiModelProperty(value = "realtion/attribute value", example = "test", allowEmptyValue = false)
    private String value;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
