/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input.ecd;

import it.cnr.ilc.lexo.service.data.lexicon.input.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing a EC Dictionary entry updater")
public class ECDEntryUpdater implements Data {

    @ApiModelProperty(value = "dictioanry entry relation/attribute", example = "label", allowEmptyValue = false,
            allowableValues = "http://www.w3.org/2000/01/rdf-schema#label, http://www.w3.org/2004/02/skos/core#note, "
            + "http://www.lexinfo.net/ontology/3.0/lexinfo#confidence, http://www.w3.org/2003/06/sw-vocab-status/ns#status, "
            + "http://www.lexinfo.net/ontology/3.0/lexinfo#pos")
    private String relation;
    @ApiModelProperty(allowEmptyValue = false)
    private String value;
    @ApiModelProperty(allowEmptyValue = true)
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
