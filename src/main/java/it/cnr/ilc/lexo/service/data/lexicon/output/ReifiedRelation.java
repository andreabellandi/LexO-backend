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

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a reified relation")
public class ReifiedRelation extends Entity implements Data {

    @ApiModelProperty(value = "individual IRI representing the reified relation")
    private String relation;
    @ApiModelProperty(value = "relation type")
    private String type;
    @ApiModelProperty(value = "source IRI of the relation")
    private String source;
    @ApiModelProperty(value = "target IRI of the relation")
    private String target;
    @ApiModelProperty(value = "label of the target IRI")
    private String targetLabel;
    @ApiModelProperty(value = "definition of the target IRI")
    private String targetDefinition;
    @ApiModelProperty(value = "a note of the relation")
    private String note;
    @ApiModelProperty(value = "a label of the relation")
    private String label;
    @ApiModelProperty(value = "list of relations associated with the reification")
    private ArrayList<LinkedEntity> properties;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public ArrayList<LinkedEntity> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<LinkedEntity> properties) {
        this.properties = properties;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTargetLabel() {
        return targetLabel;
    }

    public void setTargetLabel(String targetLabel) {
        this.targetLabel = targetLabel;
    }

    public String getTargetDefinition() {
        return targetDefinition;
    }

    public void setTargetDefinition(String targetDefinition) {
        this.targetDefinition = targetDefinition;
    }

}
