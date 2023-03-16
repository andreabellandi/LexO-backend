/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import it.cnr.ilc.lexo.service.data.output.Entity;
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
public class EtymologicalLink extends Entity implements Data {

    @ApiModelProperty(value = "etymological link IRI")
    private String etymologicalLink;
//    @ApiModelProperty(value = "etymological link short IRI")
//    private String etymologicalLinkInstanceName;
    @ApiModelProperty(value = "if the IRI is external", allowableValues = "true, false")
    private boolean externalIRI;
    @ApiModelProperty(value = "type of the etymological link")
    private String etyLinkType;
    @ApiModelProperty(value = "the source of the etymology (etymon)")
    private String etySource;
//    @ApiModelProperty(value = "the source short IRI of the etymology (etymon)")
//    private String etySourceInstanceName;
    @ApiModelProperty(value = "label of the source of the etymology (etymon)")
    private String etySourceLabel;
    @ApiModelProperty(value = "the target IRI of the etymology (lexical entry)")
    private String etyTarget;
    @ApiModelProperty(value = "label of the target of the etymology (lexical entry)")
    private String etyTargetLabel;
    @ApiModelProperty(value = "etymological link label (the default value it should be equal to that of the etySourceLabel)")
    private String label;
//    @ApiModelProperty(value = "the target short IRI of the etymology (lexical entry)")
//    private String etyTargetInstanceName;
    @ApiModelProperty(value = "etymology note", example = "some note ...")
    private String note;


    public boolean isExternalIRI() {
        return externalIRI;
    }

    public void setExternalIRI(boolean externalIRI) {
        this.externalIRI = externalIRI;
    }

    public String getEtymologicalLink() {
        return etymologicalLink;
    }

    public void setEtymologicalLink(String etymologicalLink) {
        this.etymologicalLink = etymologicalLink;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getEtyTarget() {
        return etyTarget;
    }

    public void setEtyTarget(String etyTarget) {
        this.etyTarget = etyTarget;
    }
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEtySourceLabel() {
        return etySourceLabel;
    }

    public void setEtySourceLabel(String etySourceLabel) {
        this.etySourceLabel = etySourceLabel;
    }

    public String getEtyTargetLabel() {
        return etyTargetLabel;
    }

    public void setEtyTargetLabel(String etyTargetLabel) {
        this.etyTargetLabel = etyTargetLabel;
    }

}
