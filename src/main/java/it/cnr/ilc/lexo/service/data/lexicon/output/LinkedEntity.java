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
@ApiModel(description = "Output model representing label and IRI of a linked entity")
public class LinkedEntity implements Data {

    @ApiModelProperty(value = "lexical entity IRI")
    private String entity;
    @ApiModelProperty(value = "lexical entity label")
    private String label;
    @ApiModelProperty(value = "lexical entity type")
    private List<String> entityType;
    @ApiModelProperty(value = "explicit or implicit link")
    private boolean inferred;
    @ApiModelProperty(value = "the type of linked the lexical entity", allowableValues = "internal, external")
    private String linkType;
    @ApiModelProperty(value = "the name of the link")
    private String link;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getEntityType() {
        return entityType;
    }

    public void setEntityType(List<String> entityType) {
        this.entityType = entityType;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public boolean isInferred() {
        return inferred;
    }

    public void setInferred(boolean inferred) {
        this.inferred = inferred;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
