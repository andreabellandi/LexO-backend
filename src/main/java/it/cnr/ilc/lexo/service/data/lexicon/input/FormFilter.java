/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input;

import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;

/**
 *
 * @author andreabellandi { "searchType": "keyWord", "lexicalEntry" : IRI",
 * "senses": [ "http://lexica/mylexicon#USem72095pesca",
 * "http://lexica/mylexicon#USem72096pesca" ], "extend": "iperonimia",
 * "distance": 1, }
 */
public class FormFilter implements Data {

    private String form;
    private String formType;
    private String lexicalEntry;
    private ArrayList<String> senseUris;
    private String extendTo;
    private int extensionDegree;

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getLexicalEntry() {
        return lexicalEntry;
    }

    public void setLexicalEntry(String lexicalEntry) {
        this.lexicalEntry = lexicalEntry;
    }

    public ArrayList<String> getSenseUris() {
        return senseUris;
    }

    public void setSenseUris(ArrayList<String> senseUris) {
        this.senseUris = senseUris;
    }

    public String getExtendTo() {
        return extendTo;
    }

    public void setExtendTo(String extendTo) {
        this.extendTo = extendTo;
    }

    public int getExtensionDegree() {
        return extensionDegree;
    }

    public void setExtensionDegree(int extensionDegree) {
        this.extensionDegree = extensionDegree;
    }

}
