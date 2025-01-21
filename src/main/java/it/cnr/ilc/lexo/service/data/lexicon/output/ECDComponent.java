/*
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
@ApiModel(description = "Output model representing an ECD component")
public class ECDComponent extends Entity implements Data {

    @ApiModelProperty(value = "component IRI")
    private String component;
    @ApiModelProperty(value = "position of the component")
    private int position;
    @ApiModelProperty(value = "referred lexical entity")
    private String referredEntity;
    @ApiModelProperty(value = "type of lexical entity (lexical entry or lexical sense)")
    private ArrayList<String> type;
    @ApiModelProperty(value = "label or definition depending on the referred entity type")
    private String label;
    @ApiModelProperty(value = "pos, if referred entity is a lexical entry")
    private ArrayList<String> pos;
//    private String senseNumber;
    private String status;
    private String revisor;
    private String author;
    private String note;
    private String completionDate;
    private String revisionDate;
    private ArrayList<String> lexicalConcepts;

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

    public ArrayList<String> getLexicalConcepts() {
        return lexicalConcepts;
    }

    public void setLexicalConcepts(ArrayList<String> lexicalConcepts) {
        this.lexicalConcepts = lexicalConcepts;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
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

//    public boolean isHasChildren() {
//        return hasChildren;
//    }
//
//    public void setHasChildren(boolean hasChildren) {
//        this.hasChildren = hasChildren;
//    }

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<String> getPos() {
        return pos;
    }

    public void setPos(ArrayList<String> pos) {
        this.pos = pos;
    }
    
//        public String getSenseNumber() {
//        return senseNumber;
//    }
//
//    public void setSenseNumber(String senseNumber) {
//        this.senseNumber = senseNumber;
//    }

}
