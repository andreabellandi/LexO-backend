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
@ApiModel(description = "Input model representing a node graph filter")
public class NodeGraphFilter {

    @ApiModelProperty(value = "realtion name", example = "hyponym", allowEmptyValue = false, allowableValues = "synonym, hyponym, meronym")
    private String relation;
//    @ApiModelProperty(value = "type of the named graph", example = "explicit", allowEmptyValue = true, allowableValues = "explicit, implicit, (empty for both explicit and implicit)")
//    private String graph;
    @ApiModelProperty(value = "the lenght of the relation path", example = "2", allowEmptyValue = true)
    private Integer lenght;
    @ApiModelProperty(value = "if the relation is incoming or outgoing w.r.t. the given node", example = "incoming", allowEmptyValue = false, allowableValues = "incoming, outgoing")
    private String direction;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

//    public String getGraph() {
//        return graph;
//    }
//
//    public void setGraph(String graph) {
//        this.graph = graph;
//    }

    public Integer getLenght() {
        return lenght;
    }

    public void setLenght(Integer lenght) {
        this.lenght = lenght;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
