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

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the core data of a lexical entry")
public class LexicalEntryCore implements Data {

    @ApiModelProperty(value = "lexcial entry IRI", example = "namespace:MUSaccedereVERB")
    private String lexicalEntry;
    @ApiModelProperty(value = "lexcial entry short IRI", example = "MUSaccedereVERB")
    private String lexicalEntryInstanceName;
    @ApiModelProperty(value = "lexcial entry label", example = "label for accedere")
    private String label;
    @ApiModelProperty(value = "lexcial entry types", example = "word", allowableValues = "word, multi-word expression, affix")
    private String type;
    @ApiModelProperty(value = "part of speech", reference = "https://www.lexinfo.net/", example = "verb")
    private String pos;
    @ApiModelProperty(value = "language", example = "it", reference = "https://www.loc.gov/standards/iso639-2/php/code_list.php")
    private String language;
    @ApiModelProperty(value = "common morphological traits of a lexical entry")
    private ArrayList<Morphology> morphology;
    @ApiModelProperty(value = "IRI of the concept denoted by", example = "namespace:conceptID", allowableValues = "word, multi-word expression, affix")
    private String concept;
    @ApiModelProperty(value = "short IRI of the concept denoted by", example = "conceptID")
    private String conceptInstanceName;
    @ApiModelProperty(value = "lexcial entry author", example = "user3")
    private String author;
    @ApiModelProperty(value = "lexcial entry revisor", example = "rev2")
    private String revisor;
    @ApiModelProperty(value = "lexical entry status", example = "working", allowableValues = "working, completed, reviewed")
    private String status;
    @ApiModelProperty(value = "lexcial entry note", example = "some note ...")
    private String note;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

}
