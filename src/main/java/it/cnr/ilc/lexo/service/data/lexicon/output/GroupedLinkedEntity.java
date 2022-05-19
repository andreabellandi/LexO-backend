/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing label and IRI of a linked entity grouped by relation type")
public class GroupedLinkedEntity implements Data {

    @ApiModelProperty(value = "the name of the relation group")
    private String relationGroup;
    @ApiModelProperty(value = "the related entities")
    private List<LinkedEntity> entities;

    public String getRelationGroup() {
        return relationGroup;
    }

    public void setRelationGroup(String relationGroup) {
        this.relationGroup = relationGroup;
    }

    public List<LinkedEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<LinkedEntity> entities) {
        this.entities = entities;
    }
    
    public GroupedLinkedEntity(String relationGroup, List<LinkedEntity> entities) {
        this.relationGroup = relationGroup;
        this.entities = entities;
    }

}
