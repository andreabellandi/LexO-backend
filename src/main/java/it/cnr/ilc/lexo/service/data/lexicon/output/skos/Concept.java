/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.skos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.lexicon.output.GroupedLinkedEntity;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a Concept")
public class Concept extends SKOSClass implements Data {

    @ApiModelProperty(value = "IRI")
    private String concept;
    @ApiModelProperty(value = "the list of relations the lexical concept is involved in")
    private List<GroupedLinkedEntity> entities;

    public List<GroupedLinkedEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<GroupedLinkedEntity> entities) {
        this.entities = entities;
    }   

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }
}
