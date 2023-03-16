/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input.graphViz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing an edge graph filter")
public class EdgeGraphFilter {

    @ApiModelProperty(value = "realtion name", example = "hyponym", allowEmptyValue = false, allowableValues = "synonym, hyponym, meronym")
    private String relation;
    @ApiModelProperty(value = "id source node", allowEmptyValue = false)
    private String source;
    @ApiModelProperty(value = "id target node", allowEmptyValue = false)
    private String target;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

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

}
