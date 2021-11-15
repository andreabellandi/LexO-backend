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
public class SparqlGraphViz {

    public static final String GRAPH_VIZ_SENSE_SUMMARY
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.SENSE
            + "\n"
            + "WHERE { \n"
            + "    ?search a inst:" + SparqlVariable.LEXICAL_SENSE_INDEX + " ;\n"
            + "      luc:query \"lexicalSenseIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.SENSE + " .\n"
            + "    ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " ; \n"
            + "        ontolex:isSenseOf ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " ;\n"
            + "        rdfs:label ?" + SparqlVariable.LABEL + " .\n"
            + "} ";

    public static final String GRAPH_VIZ_SENSE_LINKS
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT (count(?" + SparqlVariable.HYPERNYM + ") as ?" + SparqlVariable.HYPERNYM + "Count) "
            + "(count(?" + SparqlVariable.HYPONYM + ") as ?" + SparqlVariable.HYPONYM + "Count) "
            + "(count(?" + SparqlVariable.HOLONYM + ") as ?" + SparqlVariable.HOLONYM + "Count) "
            + "(count(?" + SparqlVariable.MERONYM + ") as ?" + SparqlVariable.MERONYM + "Count) "
            + "(count(?out" + SparqlVariable.SYNONYM + ") as ?out" + SparqlVariable.SYNONYM + "Count) "
            + "(count(?in" + SparqlVariable.SYNONYM + ") as ?in" + SparqlVariable.SYNONYM + "Count)\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_SENSE_INDEX + " ;\n"
            + "      luc:query \"lexicalSenseIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.SENSE + " .\n"
            + "    { ?" + SparqlVariable.SENSE + " lexinfo:hypernym ?" + SparqlVariable.HYPERNYM + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.SENSE + " lexinfo:hyponym ?" + SparqlVariable.HYPONYM + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.SENSE + " lexinfo:holonym ?" + SparqlVariable.HOLONYM + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.SENSE + " lexinfo:meronymTerm ?" + SparqlVariable.MERONYM + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.SENSE + " lexinfo:synonym ?out" + SparqlVariable.SYNONYM + " . }\n"
            + " UNION \n"
            + "    { ?in" + SparqlVariable.SYNONYM + " lexinfo:synonym ?" + SparqlVariable.SENSE + " . }\n"
            + "}";

}
