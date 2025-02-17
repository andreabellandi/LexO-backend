/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.ecd;

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
@ApiModel(description = "Output model representing the hierarchical semantic structure of an ECD dictionary entry")
public class ECDEntrySemantics extends Entity implements Data {

    private String id;
    private String referredEntity;
    private ArrayList<String> type = new ArrayList();
    private String label;
    private ArrayList<String> pos = new ArrayList();
    private String definition;
    private List<ECDEntrySemantics> children;

    public ECDEntrySemantics() {
    }

    public ECDEntrySemantics(ECDComponent component) {
        id = component.getComponent();
        referredEntity = component.getReferredEntity();
        type = component.getType();
        pos = component.getPos();
        label = component.getLabel();
        definition = component.getDefinition();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferredEntity() {
        return referredEntity;
    }

    public void setReferredEntity(String referredEntity) {
        this.referredEntity = referredEntity;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<String> getPos() {
        return pos;
    }

    public void setPos(ArrayList<String> pos) {
        this.pos = pos;
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

    public List<ECDEntrySemantics> getChildren() {
        return children;
    }

    public void setChildren(List<ECDEntrySemantics> children) {
        this.children = children;
    }

}
