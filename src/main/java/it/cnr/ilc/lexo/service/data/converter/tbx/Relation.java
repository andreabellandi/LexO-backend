/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.converter.tbx;

/**
 *
 * @author andreabellandi
 */
public class Relation {

    public final String type; // "synonym" | "hypernym"
    public final String relatedConceptIRI;

    public Relation(String type, String relatedConceptIRI) {
        this.type = type;
        this.relatedConceptIRI = relatedConceptIRI;
    }
}
