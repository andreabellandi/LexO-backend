/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input.graphViz;

import it.cnr.ilc.lexo.service.data.lexicon.input.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing a node graph filter")
public class NodeGraphFilter {

    @ApiModelProperty(value = "realtion name", example = "hyponym", allowEmptyValue = false, allowableValues = "synonym, hyponym, meronym")
    private String relation;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    
}
