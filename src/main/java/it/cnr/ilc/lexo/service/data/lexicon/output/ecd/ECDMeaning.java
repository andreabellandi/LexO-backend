/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.ecd;

import it.cnr.ilc.lexo.service.data.output.Entity;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author andreabellandi
 */
public class ECDMeaning extends Entity implements Data {

    private ArrayList<String> pos;
    private String label;
    private String senseLabel;
    private String note;
    private String language;
    private String dictionaryEntry;
    private String definition;
    private String sense;

    public ECDMeaning() {
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getSense() {
        return sense;
    }

    public void setSense(String sense) {
        this.sense = sense;
    }

    

    public ArrayList<String> getPos() {
        return pos;
    }

    public void setPos(ArrayList<String> pos) {
        this.pos = pos;
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

    public String getSenseLabel() {
        return senseLabel;
    }

    public void setSenseLabel(String senseLabel) {
        this.senseLabel = senseLabel;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDictionaryEntry() {
        return dictionaryEntry;
    }

    public void setDictionaryEntry(String dictionaryEntry) {
        this.dictionaryEntry = dictionaryEntry;
    }

}
