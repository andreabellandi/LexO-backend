/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Class representing the morphological traits of a form taken from lexinfo ontology v3.0")
public class Morphology {

    @ApiModelProperty(value = "trait name", example = "tense")
    private String trait;
    @ApiModelProperty(value = "trait value", example = "finite")
    private String value;

    public Morphology(String trait, String value) {
        this.trait = trait;
        this.value = value;
    }
    
    public String getTrait() {
        return trait;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
