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
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a translation set according to the vartrans module")
public class TranslationSet extends Entity implements Data {

    @ApiModelProperty(value = "translation set IRI")
    private String translationSet;
    @ApiModelProperty(value = "translation set label")
    private String label;
    @ApiModelProperty(value = "lexical entry types")
    private List<ReifiedRelation> translations;
    @ApiModelProperty(value = "translation set note")
    private String note;

    public String getTranslationSet() {
        return translationSet;
    }

    public void setTranslationSet(String translationSet) {
        this.translationSet = translationSet;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ReifiedRelation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<ReifiedRelation> translations) {
        this.translations = translations;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    
}
