/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing a list of concepts")
public class ConceptList implements Data {

    @ApiModelProperty(value = "list of concepts", allowEmptyValue = false)
    private List<String> conceptList;

    public List<String> getConceptList() {
        return conceptList;
    }

    public void setConceptList(List<String> conceptList) {
        this.conceptList = conceptList;
    }

}
