/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.skos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.output.Entity;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a SKOS class")
public class SKOSClass extends Entity implements Data {

    @ApiModelProperty(value = "label class")
    private String defaultLabel;
    @ApiModelProperty(value = "label language")
    private String language;

    public String getDefaultLabel() {
        return defaultLabel;
    }

    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
