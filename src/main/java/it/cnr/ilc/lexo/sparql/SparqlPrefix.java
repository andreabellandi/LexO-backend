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
    public static Namespace ONTOLEX = new Namespace("ontolex:", "http://www.w3.org/ns/lemon/ontolex#");
    public static Namespace LIME = new Namespace("lime:", "http://www.w3.org/ns/lemon/lime#");
    public static Namespace DECOMP = new Namespace("decomp:", "http://www.w3.org/ns/lemon/decomp#");
    public static Namespace VARTRANS = new Namespace("vartrans:", "http://www.w3.org/ns/lemon/vartrans#");
    public static Namespace SYNSEM = new Namespace("synsem:", "http://www.w3.org/ns/lemon/synsem#");
    public static Namespace ETY = new Namespace("ety:", "http://lari-datasets.ilc.cnr.it/lemonEty#");
    public static Namespace TRCAT = new Namespace("trcat:", "http://purl.org/net/translation-categories#");

    // metadata
    public static Namespace DCT = new Namespace("dct:", "http://purl.org/dc/terms/");
    public static Namespace LOC = new Namespace("loc:", "http://id.loc.gov/vocabulary/relators/");
    public static Namespace VS = new Namespace("vs:", "http://www.w3.org/2003/06/sw-vocab-status/ns#");
    

    // linguistic categories
    public static Namespace LEXINFO = new Namespace("lexinfo:", "http://www.lexinfo.net/ontology/3.0/lexinfo#");

    // GraphDB
    public static Namespace LUC = new Namespace("luc:", "http://www.ontotext.com/connectors/lucene#");
    public static Namespace ONTO = new Namespace("onto:", "http://www.ontotext.com/");
    public static Namespace SESAME = new Namespace("sesame:", "http://www.openrdf.org/schema/sesame#");
    public static Namespace INST = new Namespace("inst:", "http://www.ontotext.com/connectors/lucene/instance#");

    // Ontologies
    public static Namespace RDFS = new Namespace("rdfs:", "http://www.w3.org/2000/01/rdf-schema#");
    public static Namespace RDF = new Namespace("rdf:", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    public static Namespace SKOS = new Namespace("skos:", "http://www.w3.org/2004/02/skos/core#");
    public static Namespace OWL = new Namespace("owl:", "http://www.w3.org/2002/07/owl#");

    // data
    public static Namespace LEX = new Namespace("lex:", "http://lexica/mylexicon#");
    public static Namespace ONTOLOGY = new Namespace("ontology:", "http://simpleOntology#");

}
