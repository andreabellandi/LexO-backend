/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing a linguistic relation updater")
public class LinguisticRelationUpdater {

    @ApiModelProperty(value = "the type of relation", example = "morphology", allowEmptyValue = false,
            allowableValues = "morphology, conceptRef, lexicalRel, senseRel, conceptRel")
    private String type;
    @ApiModelProperty(value = "realtion name", example = "partOfSpeech", allowEmptyValue = false)
    private String relation;
    @ApiModelProperty(value = "realtion value", example = "noun", allowEmptyValue = false)
    private String value;
    @ApiModelProperty(value = "previous realtion/attribute value (leave empty, if a new relation value is needed)", example = "oldValue", allowEmptyValue = true)
    private String currentValue;

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
