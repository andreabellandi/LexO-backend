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
import java.util.TreeMap;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a collocation")
public class Collocation extends Entity implements Data {

    @ApiModelProperty(value = "IRI of the collocation")
    private String collocation;
    @ApiModelProperty(value = "collocation type")
    private String type;
    @ApiModelProperty(value = "collocation description")
    private String description;
    @ApiModelProperty(value = "collocation note")
    private String note;
    @ApiModelProperty(value = "collocation text")
    private String example;
    @ApiModelProperty(value = "IRI of the head")
    private String head;
    @ApiModelProperty(value = "collocation absolute frequency")
    private int frequency;
    @ApiModelProperty(value = "collocation components")
    private TreeMap<Integer, LinkedEntity> components;

    public String getCollocation() {
        return collocation;
    }

    public void setCollocation(String collocation) {
        this.collocation = collocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public TreeMap<Integer, LinkedEntity> getComponents() {
        return components;
    }

    public void setComponents(TreeMap<Integer, LinkedEntity> components) {
        this.components = components;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
