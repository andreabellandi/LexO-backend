/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.sparql.skos;

import it.cnr.ilc.lexo.sparql.SparqlPrefix;

/**
 *
 * @author andreabellandi
 */
public class SparqlSKOSUpdate {
    
    public static final String UPDATE_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "DELETE { _ID_ _RELATION_ _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { _ID_ _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { _ID_ dct:modified ?modified . }\n"
            + "         OPTIONAL { _ID_ _RELATION_ _VALUE_TO_DELETE_ . } }";
}
