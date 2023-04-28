/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing some details of the file imported")
public class ImportDetail implements Data {

    private int numberOfTriples;

    public int getNumberOfTriples() {
        return numberOfTriples;
    }

    public void setNumberOfTriples(int numberOfTriples) {
        this.numberOfTriples = numberOfTriples;
    }

    public ImportDetail() {
    }

}
