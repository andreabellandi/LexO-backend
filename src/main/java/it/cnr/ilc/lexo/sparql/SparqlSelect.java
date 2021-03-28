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
public class SparqlSelect {

    public static final String LEXICAL_ENTRIES
            = SparqlPrefix.DCT + "\n"
            + SparqlPrefix.INST + "\n"
            + SparqlPrefix.LEX + "\n"
            + SparqlPrefix.LEXINFO + "\n"
            + SparqlPrefix.LOC + "\n"
            + SparqlPrefix.ONTO + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.RDF + "\n"
            + SparqlPrefix.RDFS + "\n"
            + SparqlPrefix.SESAME + "\n"
            + SparqlPrefix.LUC + "\n"
            + "SELECT ?" + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.INSTANCE_NAME
            + " ?" + SparqlVariable.LEXICAL_ENTRY_STATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_TYPE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.AUTHOR
            + " ?" + SparqlVariable.NOTE
            + "\n"
            + "FROM onto:explicit WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?totalHits ;\n"
            + "      luc:orderBy \"writtenForm\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "  ?" + SparqlVariable.LEXICAL_ENTRY + " rdf:type ?" + SparqlVariable.LEXICAL_ENTRY_TYPE + " ;\n"
            + "          lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " ;\n"
            + "          dct:contributor ?" + SparqlVariable.AUTHOR + " ;\n"
            + "          rdfs:label ?" + SparqlVariable.LABEL + " .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " rdfs:comment ?" + SparqlVariable.NOTE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " loc:rev ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:valid ?" + SparqlVariable.LEXICAL_ENTRY_STATE + "} .\n"
            + "   BIND(strafter(str(?" + SparqlVariable.LEXICAL_ENTRY + "),str(lex:)) as ?" + SparqlVariable.INSTANCE_NAME + ") \n"
            + "   FILTER(STRSTARTS(STR(?" + SparqlVariable.LEXICAL_ENTRY_TYPE + "), str(ontolex:)))\n"
            + "}";

    public static final String LEXICAL_ENTRY
            = SparqlPrefix.SYNSEM + "\n"
            + SparqlPrefix.INST + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.LUC + "\n"
            + "SELECT (count(?" + SparqlVariable.FORM + ") as ?" + SparqlVariable.FORM + "Count) "
            + "(count(?" + SparqlVariable.SENSE + ") as ?" + SparqlVariable.SENSE + "Count) "
            + "(count(?" + SparqlVariable.FRAME + ") as ?" + SparqlVariable.FRAME + "Count) "
            + "(count(?" + SparqlVariable.LEXICAL_CONCEPT + ") as ?" + SparqlVariable.LEXICAL_CONCEPT + "Count) "
            + "(count(?" + SparqlVariable.CONCEPT + ") as ?" + SparqlVariable.CONCEPT + "Count)\n"
            + "WHERE {\n"
            + "  ?search a inst:lexicalEntryIndex ;\n"
            + "      luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:lexicalForm ?" + SparqlVariable.FORM + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " synsem:synBehaviour ?" + SparqlVariable.FRAME + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:evokes ?" + SparqlVariable.LEXICAL_CONCEPT + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:denotes ?" + SparqlVariable.CONCEPT + " . }\n"
            + "}";

    public static final String FORMS
            = SparqlPrefix.DCT + "\n"
            + SparqlPrefix.INST + "\n"
            + SparqlPrefix.ONTO + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.RDFS + "\n"
            + SparqlPrefix.LUC + "\n"
            + SparqlPrefix.LEX + "\n"
            + SparqlPrefix.LEXINFO + "\n"
            + "SELECT ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.FORM
            + " ?" + SparqlVariable.AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.INSTANCE_NAME
            + " (GROUP_CONCAT(concat(str(?tn),\":\",str(?tv));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "             luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "             luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "   ?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.FORM_TYPE + " ?" + SparqlVariable.FORM + " .\n"
            + "   ?" + SparqlVariable.FORM + " dct:contributor ?" + SparqlVariable.AUTHOR + " ;\n"
            + "       ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ontolex:phoneticRep ?" + SparqlVariable.PHONETIC_REPRESENTATION + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " rdfs:comment ?" + SparqlVariable.NOTE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + " .\n"
            + "              FILTER(STRSTARTS(STR(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), str(lexinfo:))) }\n"
            + "   ?" + SparqlVariable.FORM_TYPE + " rdfs:subPropertyOf* ontolex:lexicalForm .\n"
            + "   BIND(strafter(str(?" + SparqlVariable.FORM + "), str(lex:)) as ?" + SparqlVariable.INSTANCE_NAME + ")\n"
            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),str(lexinfo:)) as ?tn)\n"
            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "),str(lexinfo:)) as ?tv)\n"
            + "} GROUP BY ?"
            + SparqlVariable.FORM
            + " ?" + SparqlVariable.INSTANCE_NAME
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.NOTE + "\n"
            + "  ORDER BY ?"
            + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.INSTANCE_NAME;

    public static final String LEXICAL_SENSES
            = SparqlPrefix.DCT + "\n"
            + SparqlPrefix.INST + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.RDFS + "\n"
            + SparqlPrefix.SKOS + "\n"
            + SparqlPrefix.LUC + "\n"
            + SparqlPrefix.LEX + "\n"
            + "SELECT ?sense ?author ?definition ?note ?instanceName\n"
            + "WHERE {\n"
            + "  ?search a inst:lexicalEntryIndex ;\n"
            + "      luc:query \"lexicalEntryIRI:*mylexicon*accedere*\" ;\n"
            + "      luc:entities ?lexicalEntry .\n"
            + "  ?lexicalEntry ontolex:sense ?sense .\n"
            + "  ?sense dct:contributor ?author .\n"
            + "    OPTIONAL { ?sense skos:definition ?definition . }\n"
            + "    OPTIONAL { ?sense rdfs:comment ?note . }\n"
            + " BIND(strafter(str(?sense), str(lex:)) as ?instanceName)\n"
            + "}";

    public static final String FRAMES
            = SparqlPrefix.DCT + "\n"
            + SparqlPrefix.INST + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.RDF + "\n"
            + SparqlPrefix.RDFS + "\n"
            + SparqlPrefix.SYNSEM + "\n"
            + SparqlPrefix.SKOS + "\n"
            + "SELECT ?frame ?author ?pattern ?note (count(?arg) as ?argCount)\n"
            + "WHERE {\n"
            + "  ?search a inst:lexicalEntryIndex ;\n"
            + "      :query \"lexicalEntryIRI:*mylexicon*sbandare*2\" ;\n"
            + "      :entities ?lexicalEntry .\n"
            + "  ?lexicalEntry synsem:synBehaviour ?frame .\n"
            + "  ?frame dct:contributor ?author ;\n"
            + "         rdf:type ?type .\n"
            + "    FILTER(STRSTARTS(STR(?type), \"http://www.lexinfo.net/ontology/3.0/lexinfo#\"))\n"
            + "    OPTIONAL { ?frame rdfs:comment ?note . }\n"
            + "    OPTIONAL { ?frame skos:example ?pattern . }\n"
            + "    OPTIONAL { ?frame synsem:synArg ?arg . }\n"
            + "} GROUP BY ?frame ?author ?pattern ?note";

    public static final String AUTHORS
            = SparqlPrefix.INST + "\n"
            + SparqlPrefix.LUC + "\n"
            + "SELECT ?"
            + SparqlVariable.LABEL
            + " ?"
            + SparqlVariable.LABEL_COUNT
            + " WHERE {\n"
            + "  ?r a inst:lexicalEntryIndex ;\n"
            + "    luc:facetFields \"author\" ;\n"
            + "    luc:facets _:f .\n"
            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
            + "} order by ?" + SparqlVariable.LABEL;

    public static final String TYPES
            = SparqlPrefix.INST + "\n"
            + SparqlPrefix.LUC + "\n"
            + "SELECT ?"
            + SparqlVariable.LABEL
            + " ?"
            + SparqlVariable.LABEL_COUNT
            + " WHERE {\n"
            + "  ?r a inst:lexicalEntryIndex ;\n"
            + "    luc:facetFields \"type\" ;\n"
            + "    luc:facets _:f .\n"
            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
            + "  FILTER (regex(str(?" + SparqlVariable.LABEL + "), \"word|multi-word expression|affix\"))"
            + "}";

    public static final String LANGUAGES
            = SparqlPrefix.INST + "\n"
            + SparqlPrefix.LUC + "\n"
            + "SELECT ?"
            + SparqlVariable.LABEL
            + " ?"
            + SparqlVariable.LABEL_COUNT
            + " WHERE {\n"
            + "  ?r a inst:lexicalEntryIndex ;\n"
            + "    luc:facetFields \"writtenFormLanguage\" ;\n"
            + "    luc:facets _:f .\n"
            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
            + "} order by ?" + SparqlVariable.LABEL;

    public static final String POS
            = SparqlPrefix.INST + "\n"
            + SparqlPrefix.LUC + "\n"
            + "SELECT ?"
            + SparqlVariable.LABEL
            + " ?"
            + SparqlVariable.LABEL_COUNT
            + " WHERE {\n"
            + "  ?r a inst:lexicalEntryIndex ;\n"
            + "    luc:facetFields \"pos\" ;\n"
            + "    luc:facets _:f .\n"
            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
            + "  FILTER NOT EXISTS { \n"
            + "      FILTER (regex(str(?" + SparqlVariable.LABEL + "), \"word|multi-word expression|affix\")) . \n"
            + "  }\n"
            + "}";

    public static final String STATES
            = SparqlPrefix.INST + "\n"
            + SparqlPrefix.LUC + "\n"
            + "SELECT ?"
            + SparqlVariable.LABEL
            + " ?"
            + SparqlVariable.LABEL_COUNT
            + " WHERE {\n"
            + "  ?r a inst:lexicalEntryIndex ;\n"
            + "    luc:facetFields \"state\" ;\n"
            + "    luc:facets _:f .\n"
            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
            + "} order by ?" + SparqlVariable.LABEL;

}
