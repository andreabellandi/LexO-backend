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
@ApiModel(description = "Output model representing a lexicographic component")
public class LexicographicComponent extends Entity implements Data {

    @ApiModelProperty(value = "component IRI")
    private String component;
    @ApiModelProperty(value = "position")
    private int position;
    @ApiModelProperty(value = "referred lexical entity")
    private String referredEntity;
    @ApiModelProperty(value = "type of lexical entity (lexical entry or lexical sense)")
    private ArrayList<String> referredEntityType;
    @ApiModelProperty(value = "label")
    private String referredEntityLabel;
    @ApiModelProperty(value = "pos, if entity is a lexical entry")
    private String pos;
    private String status;
    private String revisor;
    private String author;
    private String note;
    private boolean hasChildren;
    private String completionDate;
    private String revisionDate;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getReferredEntity() {
        return referredEntity;
    }

    public void setReferredEntity(String referredEntity) {
        this.referredEntity = referredEntity;
    }

    public ArrayList<String> getReferredEntityType() {
        return referredEntityType;
    }

    public void setReferredEntityType(ArrayList<String> referredEntityType) {
        this.referredEntityType = referredEntityType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRevisor() {
        return revisor;
    }

    public void setRevisor(String revisor) {
        this.revisor = revisor;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(String revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getReferredEntityLabel() {
        return referredEntityLabel;
    }

    public void setReferredEntityLabel(String referredEntityLabel) {
        this.referredEntityLabel = referredEntityLabel;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

}
