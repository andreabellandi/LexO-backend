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
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntityLinksItem.Link;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the core data of a form")
public class FormCore extends Entity implements Data {

    @ApiModelProperty(value = "form IRI", example = "namespace:MUSabbacchiareVERB_PHUabbacchiammo_P1IR")
    private String form;
    @ApiModelProperty(value = "lexical entry IRI", example = "namespace:MUSabbacchiareVERB")
    private String lexicalEntry;
    @ApiModelProperty(value = "lexical entry label", example = "label for accedere")
    private String lexicalEntryLabel;
    @ApiModelProperty(value = "a list of representation properties")
    private List<Property> label;
    @ApiModelProperty(value = "language", example = "it", reference = "https://www.loc.gov/standards/iso639-2/php/code_list.php")
    private String language;
    @ApiModelProperty(value = "form types", example = "canonicalForm", allowableValues = "lexicalForm, canonicalForm, otherForm")
    private String type;
    @ApiModelProperty(value = "morphological traits of the form")
    private ArrayList<Morphology> morphology;
    @ApiModelProperty(value = "morphological traits inherited by the lexical entry")
    private ArrayList<Morphology> inheritedMorphology;
    @ApiModelProperty(value = "the counting of the links of a lexical entry and their type: Reference (see Also, same As) Bilbiography, Multimedia, Attestation, Other)")
    private ArrayList<Link> links;
    @ApiModelProperty(value = "form note", example = "some note ...")
    private String note;

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public List<Property> getLabel() {
        return label;
    }

    public void setLabel(List<Property> label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Morphology> getMorphology() {
        return morphology;
    }

    public void setMorphology(ArrayList<Morphology> morphology) {
        this.morphology = morphology;
    }

    public ArrayList<Morphology> getInheritedMorphology() {
        return inheritedMorphology;
    }

    public void setInheritedMorphology(ArrayList<Morphology> inheritedMorphology) {
        this.inheritedMorphology = inheritedMorphology;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLexicalEntry() {
        return lexicalEntry;
    }

    public void setLexicalEntry(String lexicalEntry) {
        this.lexicalEntry = lexicalEntry;
    }

    public String getLexicalEntryLabel() {
        return lexicalEntryLabel;
    }

    public void setLexicalEntryLabel(String lexicalEntryLabel) {
        this.lexicalEntryLabel = lexicalEntryLabel;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
