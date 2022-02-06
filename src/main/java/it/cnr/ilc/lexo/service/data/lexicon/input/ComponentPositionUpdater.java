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
@ApiModel(description = "Input model representing a component position updater")
public class ComponentPositionUpdater {

    @ApiModelProperty(value = "the type of relation", allowEmptyValue = false,
            allowableValues = "decomp")
    private String type;
    @ApiModelProperty(value = "type of position relation", example= "rdf:_n", allowEmptyValue = false)
    private String relation;
    @ApiModelProperty(value = "component id", allowEmptyValue = false)
    private String component;
    @ApiModelProperty(value = "position as integer", allowEmptyValue = false)
    private int position;
    @ApiModelProperty(value = "current position as integer (empty for component position creation)", example = "seeAlso", allowEmptyValue = false)
    private int currentPosition;

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

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
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
