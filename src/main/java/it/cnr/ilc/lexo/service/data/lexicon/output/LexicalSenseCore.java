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
@ApiModel(description = "Output model representing the core data of a sense")
public class LexicalSenseCore implements Data {

    @ApiModelProperty(value = "sense IRI", example = "namespace:USem73621abolizione")
    private String sense;
    @ApiModelProperty(value = "sense short IRI", example = "USem73621abolizione")
    private String senseInstanceName;
    @ApiModelProperty(value = "a list of definition properties")
    private List<Property> definition;
    @ApiModelProperty(value = "example of usage")
    private String usage;
    @ApiModelProperty(value = "sense topic", example = "concept url")
    private String topic;
    @ApiModelProperty(value = "the counting of the links of a sense and their type: Reference (see Also, same As, bilbiography, ...), Multimedia, Attestation, Other)")
    private ArrayList<LexicalEntryElementItem> links;
    @ApiModelProperty(value = "sense creator (who creates the entry) ", example = "user3")
    private String creator;
    @ApiModelProperty(value = "sense note", example = "some note ...")
    private String note;
    @ApiModelProperty(value = "sense creation date")
    private String creationDate;
    @ApiModelProperty(value = "sense last update")
    private String lastUpdate;
    @ApiModelProperty(value = "IRI of the concept denoted by", example = "namespace:conceptID")
    private String concept;
    @ApiModelProperty(value = "short IRI of the concept denoted by", example = "conceptID")
    private String conceptInstanceName;

    private String description;
    private String explanation;
    private String gloss;
    private String senseExample;
    private String senseTranslation;

    public String getSense() {
        return sense;
    }

    public void setSense(String sense) {
        this.sense = sense;
    }

    public String getSenseInstanceName() {
        return senseInstanceName;
    }

    public void setSenseInstanceName(String senseInstanceName) {
        this.senseInstanceName = senseInstanceName;
    }

    public List<Property> getDefinition() {
        return definition;
    }

    public void setDefinition(List<Property> definition) {
        this.definition = definition;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public ArrayList<LexicalEntryElementItem> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<LexicalEntryElementItem> links) {
        this.links = links;
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

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getConceptInstanceName() {
        return conceptInstanceName;
    }

    public void setConceptInstanceName(String conceptInstanceName) {
        this.conceptInstanceName = conceptInstanceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getGloss() {
        return gloss;
    }

    public void setGloss(String gloss) {
        this.gloss = gloss;
    }

    public String getSenseExample() {
        return senseExample;
    }

    public void setSenseExample(String senseExample) {
        this.senseExample = senseExample;
    }

    public String getSenseTranslation() {
        return senseTranslation;
    }

    public void setSenseTranslation(String senseTranslation) {
        this.senseTranslation = senseTranslation;
    }

}
