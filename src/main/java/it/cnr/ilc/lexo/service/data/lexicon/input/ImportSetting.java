/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing import settings")
public class ImportSetting implements Data {

    @ApiModelProperty(value = "who has created the input conll file")
    private String creator;
    @ApiModelProperty(value = "the ISO-code of the lexicon language is being created")
    private String language;
    @ApiModelProperty(value = "the prefix of the lexicon")
    private String prefix;
    @ApiModelProperty(value = "the base IRI of the lexicon")
    private String baseIRI;
    @ApiModelProperty(value = "if the import drops or not the exisitng data")
    private Boolean drop;

    public ImportSetting() {
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Boolean getDrop() {
        return drop;
    }

    public void setDrop(Boolean drop) {
        this.drop = drop;
    }

    

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getBaseIRI() {
        return baseIRI;
    }

    public void setBaseIRI(String baseIRI) {
        this.baseIRI = baseIRI;
    }

}
