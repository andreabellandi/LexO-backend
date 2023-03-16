/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.sparql.skos;

import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlVariable;

/**
 *
 * @author andreabellandi
 */
public class SparqlSKOSData {

    public static final String DATA_CONCEPT_SCHEMES
            = SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.CONCEPT_SCHEME_CREATOR + " ?" + SparqlVariable.LAST_UPDATE + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.CONCEPT_SCHEME + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.CONCEPT_SCHEME_STATUS + "\n"
            + "FROM onto:explicit\n"
            + "?search a inst:_INDEX_ ;\n"
            + "      :query \"_LABEL_QUERY_\" ;\n"
            + "      :totalHits ?totalHits ;\n"
            + "      :entities ?" + SparqlVariable.CONCEPT_SCHEME + " ."
            + "WHERE {\n"
            + "    OPTIONAL { ?" + SparqlVariable.CONCEPT_SCHEME + " _LABEL_RELATION_ ?" + SparqlVariable.LABEL + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CONCEPT_SCHEME + " dct:creator ?" + SparqlVariable.CONCEPT_SCHEME_CREATOR + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.CONCEPT_SCHEME + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.CONCEPT_SCHEME + " dct:created ?" + SparqlVariable.CREATION_DATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.CONCEPT_SCHEME + " vs:term_status ?" + SparqlVariable.CONCEPT_SCHEME_STATUS + " . } \n"
            + "} ORDER BY " + SparqlVariable.LABEL;

}
