/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input.graphViz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing hops filter")
public class HopsFilter {

    @ApiModelProperty(value = "relation name", example = "synonym", allowEmptyValue = false)
    private String relation;
    @ApiModelProperty(value = "node IRI", allowEmptyValue = false)
    private String node;
    @ApiModelProperty(value = "if the relation is incoming or outgoing w.r.t. the given node", example = "incoming", allowEmptyValue = false, allowableValues = "incoming, outgoing")
    private String direction;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
