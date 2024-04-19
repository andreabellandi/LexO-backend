/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing a component position updater")
public class LexicographicComponentPositionUpdater {

    @ApiModelProperty(value = "the type of relation", allowEmptyValue = false,
            allowableValues = "lexicog")
    private String type;
    @ApiModelProperty(value = "position relation", allowEmptyValue = false, allowableValues = "_n, member")
    private String relation;
    @ApiModelProperty(value = "component id", allowEmptyValue = false)
    private String component;
    @ApiModelProperty(value = "position as integer", allowEmptyValue = false)
    private int position;
    @ApiModelProperty(value = "array of couples of position and id", allowEmptyValue = false)
    private Map<String, Integer> ordering;

    public Map<String, Integer> getOrdering() {
        return ordering;
    }

    public void setOrdering(Map<String, Integer> ordering) {
        this.ordering = ordering;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

}
