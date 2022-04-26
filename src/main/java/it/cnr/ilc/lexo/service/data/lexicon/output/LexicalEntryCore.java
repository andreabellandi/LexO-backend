/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntityLinksItem.Link;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the core data of a lexical entry")
public class LexicalEntryCore implements Data {

    @ApiModelProperty(value = "lexical entry IRI", example = "namespace:MUSaccedereVERB")
    private String lexicalEntry;
    @ApiModelProperty(value = "lexical entry short IRI", example = "MUSaccedereVERB")
    private String lexicalEntryInstanceName;
    @ApiModelProperty(value = "lexical entry label", example = "label for accedere")
    private String label;
    @ApiModelProperty(value = "lexical entry types", example = "word", allowableValues = "word, multi-word expression, affix, etymon, cognate")
    private List<String> type;
    @ApiModelProperty(value = "part of speech", reference = "https://www.lexinfo.net/", example = "verb")
    private String pos;
    @ApiModelProperty(value = "language", example = "it", reference = "https://www.loc.gov/standards/iso639-2/php/code_list.php")
    private String language;
    // TEMPORARY SOLUTION: custom field
    @ApiModelProperty(value = "stemType")
    private String stemType;
    @ApiModelProperty(value = "common morphological traits of a lexical entry")
    private ArrayList<Morphology> morphology;
//    @ApiModelProperty(value = "IRI of the concept denoted by", example = "namespace:conceptID")
//    private String concept;
//    @ApiModelProperty(value = "short IRI of the concept denoted by", example = "conceptID")
//    private String conceptInstanceName;

    @ApiModelProperty(value = "the counting of the links of a lexical entry and their type: Reference (see Also, same As) Bilbiography, Multimedia, Attestation, Other)")
//    private ArrayList<LexicalEntryElementItem> links;
    private ArrayList<Link> links;
    @ApiModelProperty(value = "lexical entry author (who completes the entry) ", example = "user9")
    private String author;
    @ApiModelProperty(value = "lexical entry creator (who creates the entry) ", example = "user3")
    private String creator;
    @ApiModelProperty(value = "lexical entry revisor(who reviews the entry) ", example = "rev2")
    private String revisor;
    @ApiModelProperty(value = "lexical entry status", example = "working", allowableValues = "working, completed, reviewed")
    private String status;
    @ApiModelProperty(value = "lexical entry note", example = "some note ...")
    private String note;
    @ApiModelProperty(value = "lexical entry creation date")
    private String creationDate;
    @ApiModelProperty(value = "lexical entry last update")
    private String lastUpdate;
    @ApiModelProperty(value = "lexical entry completed status date")
    private String completionDate;
    @ApiModelProperty(value = "lexical entry reviewed status date")
    private String revisionDate;
    
    @ApiModelProperty(value = "if the lexical entry is certain")
    private double confidence;

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getLexicalEntry() {
        return lexicalEntry;
    }

    public void setLexicalEntry(String lexicalEntry) {
        this.lexicalEntry = lexicalEntry;
    }

    public String getLexicalEntryInstanceName() {
        return lexicalEntryInstanceName;
    }

    public void setLexicalEntryInstanceName(String lexicalEntryInstanceName) {
        this.lexicalEntryInstanceName = lexicalEntryInstanceName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList<Morphology> getMorphology() {
        return morphology;
    }

    public void setMorphology(ArrayList<Morphology> morphology) {
        this.morphology = morphology;
    }

//    public String getConcept() {
//        return concept;
//    }
//
//    public void setConcept(String concept) {
//        this.concept = concept;
//    }
//
//    public String getConceptInstanceName() {
//        return conceptInstanceName;
//    }
//
//    public void setConceptInstanceName(String conceptInstanceName) {
//        this.conceptInstanceName = conceptInstanceName;
//    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRevisor() {
        return revisor;
    }

    public void setRevisor(String revisor) {
        this.revisor = revisor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
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

    public String getStemType() {
        return stemType;
    }

    public void setStemType(String stemType) {
        this.stemType = stemType;
    }
    
}
