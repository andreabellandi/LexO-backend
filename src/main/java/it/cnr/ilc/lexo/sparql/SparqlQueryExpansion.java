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
public class SparqlQueryExpansion {

    public static final String QUERY_EXPANSION_REFERENCED_LINGUISTIC_OBJECT
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.CONCEPT
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + "\n"
            + "WHERE {\n"
            + "?search a inst:" + SparqlVariable.CONCEPT_REFERENCE_INDEX + " ;\n"
            + "      luc:query \"referencedBy:[CONCEPTS_LIST]\" ;\n"
            + "      luc:orderBy \"lemma\" ;\n"
            + "      luc:entities ?" + SparqlVariable.SENSE + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " . }\n"
            + "    ?" + SparqlVariable.SENSE + " ontolex:isSenseOf ?" + SparqlVariable.LEXICAL_ENTRY + " ;\n"
            + "                  ontolex:reference ?" + SparqlVariable.CONCEPT + " .\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:canonicalForm [ ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " ] . \n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " .\n"
            + "}";
}
