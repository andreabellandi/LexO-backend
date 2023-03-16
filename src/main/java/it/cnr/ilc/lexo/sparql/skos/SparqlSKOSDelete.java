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
public class SparqlSKOSDelete {

    // TODO: update lastupdate field
    public static final String DELETE_RELATION
            = SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "DELETE {\n"
            + "    <_ID_> <_RELATION_> _TARGET_\n"
            + "} WHERE {\n"
            + "    <_ID_> <_RELATION_> _TARGET_ .\n"
            + "}";

}
