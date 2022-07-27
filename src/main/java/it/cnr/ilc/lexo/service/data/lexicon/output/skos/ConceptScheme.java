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
@ApiModel(description = "Output model representing a Concept Scheme")
public class ConceptScheme extends SKOSClass implements Data {

    @ApiModelProperty(value = "IRI")
    private String conceptScheme;
    @ApiModelProperty(value = "short IRI")
    private String conceptSchemeInstanceName;
    @ApiModelProperty(value = "the list of relations the lexical concept is involved in")
    private List<GroupedLinkedEntity> entities;

    public List<GroupedLinkedEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<GroupedLinkedEntity> entities) {
        this.entities = entities;
    }

    public String getConceptScheme() {
        return conceptScheme;
    }

    public void setConceptScheme(String conceptScheme) {
        this.conceptScheme = conceptScheme;
    }

    public String getConceptSchemeInstanceName() {
        return conceptSchemeInstanceName;
    }

    public void setConceptSchemeInstanceName(String conceptSchemeInstanceName) {
        this.conceptSchemeInstanceName = conceptSchemeInstanceName;
    }

}
