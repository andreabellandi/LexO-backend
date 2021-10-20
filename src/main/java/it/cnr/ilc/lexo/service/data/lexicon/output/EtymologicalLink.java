/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the etymological link of an etymology")
public class EtymologicalLink implements Data {

    @ApiModelProperty(value = "etymological link IRI")
    private String etymologicalLink;
    @ApiModelProperty(value = "etymological link short IRI")
    private String etymologicalLinkInstanceName;
    @ApiModelProperty(value = "type of the etymological link")
    private String etyLinkType;
    @ApiModelProperty(value = "the source of the etymology (etymon)")
    private String etySource;
    @ApiModelProperty(value = "the target IRI of the etymology (lexical entry)")
    private String etyTarget;
    @ApiModelProperty(value = "the target short IRI of the etymology (lexical entry)")
    private String etyTargetInstanceName;
    @ApiModelProperty(value = "etymology creator")
    private String creator;
    @ApiModelProperty(value = "etymology note", example = "some note ...")
    private String note;
    @ApiModelProperty(value = "etymology creation date")
    private String creationDate;
    @ApiModelProperty(value = "etymology last update")
    private String lastUpdate;

    public String getEtymologicalLink() {
        return etymologicalLink;
    }

    public void setEtymologicalLink(String etymologicalLink) {
        this.etymologicalLink = etymologicalLink;
    }

    public String getEtymologicalLinkInstanceName() {
        return etymologicalLinkInstanceName;
    }

    public void setEtymologicalLinkInstanceName(String etymologicalLinkInstanceName) {
        this.etymologicalLinkInstanceName = etymologicalLinkInstanceName;
    }

    public String getEtyLinkType() {
        return etyLinkType;
    }

    public void setEtyLinkType(String etyLinkType) {
        this.etyLinkType = etyLinkType;
    }

    public String getEtySource() {
        return etySource;
    }

    public void setEtySource(String etySource) {
        this.etySource = etySource;
    }

    public String getEtyTargetInstanceName() {
        return etyTargetInstanceName;
    }

    public void setEtyTargetInstanceName(String etyTargetInstanceName) {
        this.etyTargetInstanceName = etyTargetInstanceName;
    }

    public String getEtyTarget() {
        return etyTarget;
    }

    public void setEtyTarget(String etyTarget) {
        this.etyTarget = etyTarget;
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
