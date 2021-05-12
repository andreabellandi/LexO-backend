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
public class SparqlUpdateData {

    public static final String UPDATE_LEXICAL_ENTRY
            = SparqlPrefix.DCT + "\n"
            + SparqlPrefix.LEX + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.RDFS + "\n"
            + SparqlPrefix.LEXINFO + "\n"
            + "DELETE { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { lex:_ID_ _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  lex:_ID_ dct:modified ?modified . \n"
            + "      OPTIONAL {  lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ . } }";

}
