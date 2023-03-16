/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
public class ReferencedLinguisticObject implements Data {

    private String lexicalEntry;
    private String pos;
    private String lemma;
    private String definition;
    private String conceptInstanceName;
    private String traitInstanceName;

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getLexicalEntry() {
        return lexicalEntry;
    }

    public void setLexicalEntry(String lexicalEntry) {
        this.lexicalEntry = lexicalEntry;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getConceptInstanceName() {
        return conceptInstanceName;
    }

    public void setConceptInstanceName(String conceptInstanceName) {
        this.conceptInstanceName = conceptInstanceName;
    }

    public String getTraitInstanceName() {
        return traitInstanceName;
    }

    public void setTraitInstanceName(String traitInstanceName) {
        this.traitInstanceName = traitInstanceName;
    }

}
