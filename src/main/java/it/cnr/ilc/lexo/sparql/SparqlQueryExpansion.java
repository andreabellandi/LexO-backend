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

    public static final String QUERY_EXPANSION_FORMS
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.FORM
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " (GROUP_CONCAT(concat(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),\"<>\",str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "             luc:query \"lexicalEntryIRI:[LEXICAL_ENTRY_LIST]\" ;\n"
            + "             luc:orderBy \"lexicalEntryLabel\" ;\n"
            + "             luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "   ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:canonicalForm|ontolex:otherForm ?" + SparqlVariable.FORM + " .\n"
            + "   ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " .\n"
            + "   ?" + SparqlVariable.FORM + " ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + " .\n"
            + "              ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), \"morphosyntacticProperty\")) }\n"
            //            + "              FILTER(STRSTARTS(STR(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), str(lexinfo:))) }\n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),str(lexinfo:)) as ?tn)\n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "),str(lexinfo:)) as ?tv)\n"
            + "   } GROUP BY ?" + SparqlVariable.FORM + " ?" + SparqlVariable.LEXICAL_ENTRY_POS + " ?" + SparqlVariable.WRITTEN_REPRESENTATION + " ?" + SparqlVariable.LEXICAL_ENTRY + "\n"
            + "  ";

    public static final String DATA_FORMS_BY_LEXICAL_SENSE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT "
            + " ?" + SparqlVariable.FORM
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.SENSE
            + " (GROUP_CONCAT(concat(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),\"<>\",str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_SENSE_INDEX + " ;\n"
            + "             luc:query \"lexicalSenseIRI:[IRI]\" ;\n"
            + "             luc:entities ?" + SparqlVariable.SENSE + " .\n"
            + "   ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " .\n"
            + "   ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " .\n"
            + "   ?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.FORM_TYPE + " ?" + SparqlVariable.FORM + " .\n"
            + "   FILTER(STRSTARTS(STR(?" + SparqlVariable.FORM_TYPE + "), str(ontolex:)))"
            + "   OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
            + "   ?" + SparqlVariable.FORM + " ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + " .\n"
            + "              ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), \"morphosyntacticProperty\")) }\n"
            //            + "              FILTER(STRSTARTS(STR(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), str(lexinfo:))) }\n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),str(lexinfo:)) as ?tn)\n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "),str(lexinfo:)) as ?tv)\n"
            //            + "   BIND(strafter(str(?_pos), str(lexinfo:)) as ?" + SparqlVariable.LEXICAL_ENTRY_POS + ") "
            + "   [FORM_CONSTRAINT]"
            + "} GROUP BY ?"
            + SparqlVariable.FORM
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.SENSE
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + "\n"
            + "  ORDER BY ?"
            + SparqlVariable.WRITTEN_REPRESENTATION;

    public static final String DATA_PATH_LENGTH
            = SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "SELECT ?lexicalEntry ?" + SparqlVariable.IRI + " (count(?mid) as ?lenght) { \n"
            + "  <[START_NODE]> <START_RELATION>* ?mid .\n"
            //            + "  <[START_NODE]> lexinfo:[START_RELATION]* ?mid .\n"
            + "  ?mid lexinfo:<MID_RELATION>+ ?" + SparqlVariable.IRI + " .\n"
            //            + "  ?mid lexinfo:[MID_RELATION]+ ?" + SparqlVariable.IRI + " .\n"
            + "  ?lexicalEntry ontolex:sense ?" + SparqlVariable.IRI + " .\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.IRI + " ?lexicalEntry \n"
            + "ORDER BY ?lenght";

    // for test purposes only !!
    public static final String QUERY_EXPANSION_REFERENCED_LINGUISTIC_OBJECT2
            = SparqlPrefix.ONTOLOGY.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.WRITTEN_REPRESENTATION + " ?" + SparqlVariable.SENSE_DEFINITION + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.CONCEPT + " ?" + SparqlVariable.TRAIT + " ?" + SparqlVariable.LEXICAL_ENTRY_POS + "\n"
            + "WHERE {\n"
            + "  _CONCEPTS_"
            + "  ?" + SparqlVariable.SENSE + " a ontolex:LexicalSense ;\n"
            + "    ontolex:reference ?" + SparqlVariable.CONCEPT + " ;\n"
            + "    skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " ;\n"
            + "    ontolex:isSenseOf ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "        ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:canonicalForm [ ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " ] .\n"
            + "        ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " .\n"
            + "  }\n"
            + "    UNION\n"
            + "  _TRAITS_"
            + "  ?" + SparqlVariable.SENSE + " a ontolex:LexicalSense ;\n"
            + "    ontology:trait ?" + SparqlVariable.TRAIT + " ;\n"
            + "    skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " ;\n"
            + "    ontolex:isSenseOf ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "        ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:canonicalForm [ ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " ] .\n"
            + "        ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " .\n"
            + "  }\n"
            + "} ORDER BY ?" + SparqlVariable.WRITTEN_REPRESENTATION + "";
}
