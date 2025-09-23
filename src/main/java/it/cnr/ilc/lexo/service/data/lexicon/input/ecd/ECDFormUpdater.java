/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input.ecd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing an ECD form updater")
public class ECDFormUpdater implements Data {

    @ApiModelProperty(value = "sense relation/attribute", example = "definition", allowEmptyValue = false,
            allowableValues = "http://www.lexinfo.net/ontology/3.0/lexinfo#partOfSpeech")
    private String relation;
    @ApiModelProperty(value = "realtion/attribute value", example = "test", allowEmptyValue = false)
    private String value;
    private String oldPoS;

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

    public String getOldPoS() {
        return oldPoS;
    }

    public void setOldPoS(String oldPoS) {
        this.oldPoS = oldPoS;
    }

}
