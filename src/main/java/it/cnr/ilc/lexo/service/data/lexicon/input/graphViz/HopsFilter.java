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

    @ApiModelProperty(value = "list of comma separated realtion name(s)", example = "[\"synonym\", \"hyponym\", \"partMeronym\"]", allowEmptyValue = false)
    private List<String> relation;
    @ApiModelProperty(value = "node IRI", example = "explicit", allowEmptyValue = false)
    private String node;

    public List<String> getRelation() {
        return relation;
    }

    public void setRelation(List<String> relation) {
        this.relation = relation;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

}
