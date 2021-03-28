/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.sparql;

/**
 *
 * @author andreabellandi
 */
public class SparqlPrefix {

    // OntoLex
    public static final String ONTOLEX = "PREFIX ontolex: <http://www.w3.org/ns/lemon/ontolex#>";
    public static final String LIME = "PREFIX lime: <http://www.w3.org/ns/lemon/lime#>";
    public static final String DECOMP = "PREFIX decomp: <http://www.w3.org/ns/lemon/decomp#>";
    public static final String VARTRANS = "PREFIX vartrans: <http://www.w3.org/ns/lemon/vartrans#>";
    public static final String SYNSEM = "PREFIX synsem: <http://www.w3.org/ns/lemon/synsem#>";
    public static final String TRCAT = "PREFIX trcat: <http://purl.org/net/translation-categories#>";

    // metadata
    public static final String DCT = "PREFIX dct: <http://purl.org/dc/terms/>";
    public static final String LOC = "PREFIX loc: <http://id.loc.gov/vocabulary/relators/>";

    // linguistic categories
    public static final String LEXINFO = "PREFIX lexinfo: <http://www.lexinfo.net/ontology/3.0/lexinfo#>";

    // GraphDB
    public static final String LUC = "PREFIX luc: <http://www.ontotext.com/connectors/lucene#>";
    public static final String ONTO = "PREFIX onto: <http://www.ontotext.com/>";
    public static final String SESAME = "PREFIX sesame: <http://www.openrdf.org/schema/sesame#>";
    public static final String INST = "PREFIX inst: <http://www.ontotext.com/connectors/lucene/instance#>";

    // Ontologies
    public static final String RDFS = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
    public static final String RDF = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    public static final String SKOS = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>";

    // data
    public static final String LEX = "PREFIX lex: <http://lexica/mylexicon#>";

}
