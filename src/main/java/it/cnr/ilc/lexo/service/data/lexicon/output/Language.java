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
@ApiModel(description = "Output model representing a lexicon language")
public class Language extends Entity implements Data {

    @ApiModelProperty(value = "language IRI", example = "namespace:langID")
    private String language;
    @ApiModelProperty(value = "language label", example = "en")
    private String label;
    @ApiModelProperty(value = "lexvo url", example = "http://id.loc.gov/vocabulary/iso639-2/en")
    private String lexvo;
    @ApiModelProperty(value = "linguistic catalog", example = "http://www.lexinfo.net/ontologies/3.0/lexinfo")
    private ArrayList<String> catalog;
    @ApiModelProperty(value = "description for the language", example = "description")
    private String description;
    @ApiModelProperty(value = "number of entries")
    private int entries;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLexvo() {
        return lexvo;
    }

    public void setLexvo(String lexvo) {
        this.lexvo = lexvo;
    }

    public ArrayList<String> getCatalog() {
        return catalog;
    }

    public void setCatalog(ArrayList<String> catalog) {
        this.catalog = catalog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEntries() {
        return entries;
    }

    public void setEntries(int entries) {
        this.entries = entries;
    }

}
