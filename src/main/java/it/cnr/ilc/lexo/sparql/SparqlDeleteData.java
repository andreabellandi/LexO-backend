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
public class SparqlDeleteData {

    public static final String DELETE_LEXICAL_ENTRY
            = SparqlPrefix.LEX + "\n"
            + "DELETE WHERE { lex:_ID_ ?predicate ?object . }";

}
