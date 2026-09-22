/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.sparql;

/**
 *
 * @author Abdou
 */
public class SparqlSelectLexfom {

    /* ------ Abdou ------------ */ 
     public static final String GET_ALL_LEXICAL_FUNCTIONS 
            = SparqlPrefix.LFREL.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + "SELECT DISTINCT ?" + SparqlVariable.Lexical_Function + " ?type \n"
            + "WHERE {\n"
            + "  ?lf lfrel:hasLexicalFunction ?" + SparqlVariable.Lexical_Function + " ;\n"
            + "      rdf:type ?type .\n"
            + "  FILTER(STRSTARTS(STR(?type), STR(lfrel:)))\n"
            + "}";

    public static final String GET_FILTER_LEXICAL_FUNCTIONS 
            = SparqlPrefix.LFREL.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + "SELECT DISTINCT ?" + SparqlVariable.Lexical_Function + "\n"
            + "WHERE {\n"
            + "  ?lf lfrel:hasLexicalFunction ?" + SparqlVariable.Lexical_Function + " ;\n"
            + "      rdf:type ?type .\n"
            + "  FILTER(?type = lfrel:[typeLF])\n"
            + "}";
}
