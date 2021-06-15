/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a property")
public class Property {

    @ApiModelProperty(value = "property id", example = "writtenRep")
    private String propertyID;
    @ApiModelProperty(value = "property value", example = "abbacchiammo")
    private String propertyValue;

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
    
    public Property(String propertyID, String propertyValue) {
        this.propertyID = propertyID;
        this.propertyValue = propertyValue;
    }

}
