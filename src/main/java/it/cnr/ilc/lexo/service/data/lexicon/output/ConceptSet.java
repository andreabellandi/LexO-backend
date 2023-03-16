/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.lexicon.output.skos.SKOSClass;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a concept set")
public class ConceptSet extends SKOSClass implements Data {

    @ApiModelProperty(value = "IRI")
    private String conceptSet;
    @ApiModelProperty(value = "short IRI")
    
    private List<GroupedLinkedEntity> entities;

    public String getConceptSet() {
        return conceptSet;
    }

    public void setConceptSet(String conceptSet) {
        this.conceptSet = conceptSet;
    }
    
    public List<GroupedLinkedEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<GroupedLinkedEntity> entities) {
        this.entities = entities;
    }

}
