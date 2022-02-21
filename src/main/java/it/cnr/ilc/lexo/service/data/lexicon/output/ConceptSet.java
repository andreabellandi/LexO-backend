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
@ApiModel(description = "Output model representing a concept set")
public class ConceptSet implements Data {

    @ApiModelProperty(value = "concept set IRI")
    private String conceptSet;
    @ApiModelProperty(value = "concept set short IRI")
    private String conceptSetInstanceName;
    @ApiModelProperty(value = "concept set label")
    private String label;
    @ApiModelProperty(value = "concept set definition")
    private String definition;
    @ApiModelProperty(value = "concept set creator", example = "user3")
    private String creator;
    @ApiModelProperty(value = "concept set note", example = "some note ...")
    private String note;
    @ApiModelProperty(value = "concept set creation date")
    private String creationDate;
    @ApiModelProperty(value = "concept set last update")
    private String lastUpdate;

    public String getConceptSet() {
        return conceptSet;
    }

    public void setConceptSet(String conceptSet) {
        this.conceptSet = conceptSet;
    }

    public String getConceptSetInstanceName() {
        return conceptSetInstanceName;
    }

    public void setConceptSetInstanceName(String conceptSetInstanceName) {
        this.conceptSetInstanceName = conceptSetInstanceName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
