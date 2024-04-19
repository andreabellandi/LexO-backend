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
@ApiModel(description = "Output model representing a dictionary entry component")
public class DictionaryEntryComponent extends Entity implements Data {

    @ApiModelProperty(value = "dictionary entry component IRI")
    private String component;
    @ApiModelProperty(value = "dictionary entry component label")
    private String label;
    @ApiModelProperty(value = "if the component is a dictionary entry, language can be present")
    private String language;
    @ApiModelProperty(value = "the entity which the component refers to")
    private LinkedEntity describes;
    @ApiModelProperty(value = "the list of ordered members")
    private ArrayList<String> orderedMemebers;
    @ApiModelProperty(value = "the list of unordered members")
    private ArrayList<String> unorderedMembers;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LinkedEntity getDescribes() {
        return describes;
    }

    public void setDescribes(LinkedEntity describes) {
        this.describes = describes;
    }

    public ArrayList<String> getOrderedMemebers() {
        return orderedMemebers;
    }

    public void setOrderedMemebers(ArrayList<String> orderedMemebers) {
        this.orderedMemebers = orderedMemebers;
    }

    public ArrayList<String> getUnorderedMembers() {
        return unorderedMembers;
    }

    public void setUnorderedMembers(ArrayList<String> unorderedMembers) {
        this.unorderedMembers = unorderedMembers;
    }

}
