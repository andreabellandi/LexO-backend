/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.federation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andrea
 */
@ApiModel(description = "Output model representing the returned object by a federated query")
public class FederatedObject implements Data {

    @ApiModelProperty(value = "key name is the name of the sparql variable")
    private String key;
    @ApiModelProperty(value = "value is bound to the sparql key-variable value")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
