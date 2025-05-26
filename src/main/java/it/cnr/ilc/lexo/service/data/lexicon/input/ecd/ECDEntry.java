/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input.ecd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing the ECD entry data")
public class ECDEntry implements Data {

    @ApiModelProperty(value = "entry label", allowEmptyValue = false)
    private String label;
    @ApiModelProperty(value = "entry type", allowEmptyValue = false)
    private String type;
    @ApiModelProperty(value = "entry language", allowEmptyValue = false)
    private String language;
    @ApiModelProperty(value = "list of one or many pos", allowEmptyValue = false)
    private ArrayList<String> pos;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList<String> getPos() {
        return pos;
    }

    public void setPos(ArrayList<String> pos) {
        this.pos = pos;
    }
    
}
