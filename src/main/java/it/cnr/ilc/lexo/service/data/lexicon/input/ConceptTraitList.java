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
@ApiModel(description = "Input model representing a list of concepts and traits")
public class ConceptTraitList implements Data {

    @ApiModelProperty(value = "list of concepts", allowEmptyValue = false)
    private List<String> conceptList;
    @ApiModelProperty(value = "list of traits", allowEmptyValue = false)
    private List<String> traitList;

    public List<String> getTraitList() {
        return traitList;
    }

    public void setTraitList(List<String> traitList) {
        this.traitList = traitList;
    }

    public List<String> getConceptList() {
        return conceptList;
    }

    public void setConceptList(List<String> conceptList) {
        this.conceptList = conceptList;
    }

}
