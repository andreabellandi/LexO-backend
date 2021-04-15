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
public class SparqlSelectData {

    public static final String DATA_LEXICAL_ENTRIES
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
            + " ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME
            + " ?" + SparqlVariable.LEXICAL_ENTRY_STATUS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_TYPE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.AUTHOR
            + " ?" + SparqlVariable.NOTE
            + "\n"
            + "(GROUP_CONCAT(concat(str(?traitType),\":\",str(?traitValue));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "FROM onto:explicit WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?totalHits ;\n"
            + "      luc:orderBy \"lexicalEntryLabel\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "  ?" + SparqlVariable.LEXICAL_ENTRY + " rdf:type ?" + SparqlVariable.LEXICAL_ENTRY_TYPE + " ;\n"
            + "          lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " ;\n"
            + "          dct:contributor ?" + SparqlVariable.AUTHOR + " ;\n"
            + "          rdfs:label ?" + SparqlVariable.LABEL + " .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " rdfs:comment ?" + SparqlVariable.NOTE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " loc:rev ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:valid ?" + SparqlVariable.LEXICAL_ENTRY_STATUS + "} .\n"
            + "   OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ?morphoTrait ?morphoValue . } . \n"
            + "   BIND(strafter(str(?" + SparqlVariable.LEXICAL_ENTRY + "),str(lex:)) as ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME + ") \n"
            + "   FILTER(STRSTARTS(STR(?" + SparqlVariable.LEXICAL_ENTRY_TYPE + "), str(ontolex:)))\n"
            + "   FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:)))\n"
            + "   FILTER(STRSTARTS(STR(?morphoValue), str(lexinfo:)))\n"
            + "   BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?traitType)\n"
            + "   BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?traitValue)\n"
            + "} GROUP BY ?"
            + SparqlVariable.LEXICAL_ENTRY + " ?"
            + SparqlVariable.LEXICAL_ENTRY_TYPE + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.LEXICAL_ENTRY_REVISOR + " ?"
            + SparqlVariable.LEXICAL_ENTRY_POS + " ?"
            + SparqlVariable.LEXICAL_ENTRY_STATUS + " ?"
            + SparqlVariable.AUTHOR + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.NOTE + " ?"
            + SparqlVariable.TOTAL_HITS + " ?"
            + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME;

    public static final String DATA_LEXICAL_ENTRY_ELEMENTS
            = SparqlPrefix.SYNSEM + "\n"
            + SparqlPrefix.INST + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.DECOMP + "\n"
            + SparqlPrefix.LUC + "\n"
            + "SELECT (count(?" + SparqlVariable.FORM + ") as ?" + SparqlVariable.FORM + "Count) "
            + "(count(?" + SparqlVariable.SENSE + ") as ?" + SparqlVariable.SENSE + "Count) "
            + "(count(?" + SparqlVariable.FRAME + ") as ?" + SparqlVariable.FRAME + "Count) "
            + "(count(?" + SparqlVariable.LEXICAL_CONCEPT + ") as ?" + SparqlVariable.LEXICAL_CONCEPT + "Count) "
            + "(count(?" + SparqlVariable.CONCEPT + ") as ?" + SparqlVariable.CONCEPT + "Count)\n"
            + "(count(?" + SparqlVariable.LEXICAL_ENTRY_SUBTERM + ") as ?" + SparqlVariable.LEXICAL_ENTRY_SUBTERM + "Count)\n"
            + "(count(?" + SparqlVariable.LEXICAL_ENTRY_CONSTITUENT + ") as ?" + SparqlVariable.LEXICAL_ENTRY_CONSTITUENT + "Count)\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
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
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " decomp:subterm+ ?" + SparqlVariable.LEXICAL_ENTRY_SUBTERM + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " decomp:consituent+ ?" + SparqlVariable.LEXICAL_ENTRY_CONSTITUENT + " . }\n"
            + "}";

    public static final String DATA_FORMS
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
            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
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
            + "   BIND(strafter(str(?" + SparqlVariable.FORM + "), str(lex:)) as ?" + SparqlVariable.FORM_INSTANCE_NAME + ")\n"
            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),str(lexinfo:)) as ?tn)\n"
            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "),str(lexinfo:)) as ?tv)\n"
            + "} GROUP BY ?"
            + SparqlVariable.FORM
            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.NOTE + "\n"
            + "  ORDER BY ?"
            + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.FORM_INSTANCE_NAME;

    public static final String DATA_LEXICAL_SENSES
            = SparqlPrefix.DCT + "\n"
            + SparqlPrefix.INST + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.RDFS + "\n"
            + SparqlPrefix.SKOS + "\n"
            + SparqlPrefix.LUC + "\n"
            + SparqlPrefix.LEX + "\n"
            + SparqlPrefix.ONTO + "\n"
            + SparqlPrefix.LOC + "\n"
            + SparqlPrefix.LEXINFO + "\n"
            + SparqlPrefix.ONTOLOGY + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.SENSE
            + " ?" + SparqlVariable.AUTHOR
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.SENSE_INSTANCE_NAME
            + " ?" + SparqlVariable.CONCEPT
            + " ?" + SparqlVariable.CONCEPT_INSTANCE_NAME
            + " ?" + SparqlVariable.SENSE_USAGE + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "    ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "              luc:query \"[FILTER]\" ;\n"
            + "              luc:totalHits ?totalHits ;\n"
            + "              luc:orderBy \"lexicalEntryLabel\" ;\n"
            + "              luc:offset \"[OFFSET]\" ;\n"
            + "              luc:limit \"[LIMIT]\" ;\n"
            + "              luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " ;\n"
            + "    	lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:contributor ?" + SparqlVariable.AUTHOR + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " rdfs:comment ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " ontolex:reference ?" + SparqlVariable.CONCEPT + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " ontolex:usage [ rdf:value ?" + SparqlVariable.SENSE_USAGE + " ] . }\n"
            + "    BIND(strafter(str(?" + SparqlVariable.SENSE + "), str(lex:)) as ?" + SparqlVariable.SENSE_INSTANCE_NAME + ")\n"
            + "    BIND(strafter(str(?" + SparqlVariable.CONCEPT + "), str(ontology:)) as ?" + SparqlVariable.CONCEPT_INSTANCE_NAME + ")\n"
            + "}\n"
            + " ";

    public static final String DATA_FRAMES
            = SparqlPrefix.DCT + "\n"
            + SparqlPrefix.INST + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.RDF + "\n"
            + SparqlPrefix.RDFS + "\n"
            + SparqlPrefix.SYNSEM + "\n"
            + SparqlPrefix.SKOS + "\n"
            + "SELECT ?frame ?author ?pattern ?note (count(?arg) as ?argCount)\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
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

    public static final String DATA_LEXICAL_ENTRY_CORE
            = SparqlPrefix.DCT + "\n"
            + SparqlPrefix.INST + "\n"
            + SparqlPrefix.LEX + "\n"
            + SparqlPrefix.LEXINFO + "\n"
            + SparqlPrefix.LOC + "\n"
            + SparqlPrefix.ONTO + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.SKOS + "\n"
            + SparqlPrefix.RDFS + "\n"
            + SparqlPrefix.LUC + "\n"
            + SparqlPrefix.SESAME + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_TYPE
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_STATUS
            + " ?" + SparqlVariable.CONCEPT
            + " ?" + SparqlVariable.AUTHOR
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.LEXICAL_CONCEPT
            + " ?" + SparqlVariable.CONCEPT_SCHEME + "\n"
            + " ?" + SparqlVariable.SEEALSO + "\n"
            + " ?" + SparqlVariable.SAMEAS + "\n"
            + "(GROUP_CONCAT(concat(str(?traitType),\":\",str(?traitValue));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "  ?" + SparqlVariable.LEXICAL_ENTRY + " sesame:directType ?" + SparqlVariable.LEXICAL_ENTRY_TYPE + " ;\n"
            + "                rdfs:label ?" + SparqlVariable.LABEL + ".\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " rdfs:comment ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " rdfs:seeAlso ?" + SparqlVariable.SEEALSO + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " owl:sameAs ?" + SparqlVariable.SAMEAS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:denotes ?" + SparqlVariable.CONCEPT + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:evokes ?" + SparqlVariable.LEXICAL_CONCEPT + " . \n"
            + "               ?" + SparqlVariable.CONCEPT + " skos:inScheme ?" + SparqlVariable.CONCEPT_SCHEME + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " dct:valid ?" + SparqlVariable.LEXICAL_ENTRY_STATUS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " dct:contributor ?" + SparqlVariable.AUTHOR + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " loc:rev ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ?morphoTrait ?morphoValue . }\n"
            + "    FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:)))\n"
            + "    FILTER(STRSTARTS(STR(?morphoValue), str(lexinfo:)))\n"
            + "    FILTER(STRSTARTS(STR(?" + SparqlVariable.LEXICAL_ENTRY_TYPE + "), str(ontolex:)))\n"
            + "    BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?traitType)\n"
            + "    BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?traitValue)\n"
            + "} GROUP BY ?"
            + SparqlVariable.LEXICAL_ENTRY + " ?"
            + SparqlVariable.SEEALSO + " ?"
            + SparqlVariable.SAMEAS + " ?"
            + SparqlVariable.LEXICAL_ENTRY_TYPE + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.LEXICAL_ENTRY_REVISOR + " ?"
            + SparqlVariable.LEXICAL_ENTRY_POS + " ?"
            + SparqlVariable.LEXICAL_ENTRY_STATUS + " ?"
            + SparqlVariable.AUTHOR + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.NOTE + " ?"
            + SparqlVariable.CONCEPT + " ?"
            + SparqlVariable.LEXICAL_CONCEPT + " ?"
            + SparqlVariable.CONCEPT_SCHEME;
    
    public static final String DATA_LEXICAL_ENTRY_REFERENCE_LINKS
            = SparqlPrefix.LUC + "\n"
            + SparqlPrefix.INST + "\n"
            + SparqlPrefix.RDFS + "\n"
            + SparqlPrefix.OWL + "\n"
            + "\n"
            + "SELECT (count(?" + SparqlVariable.SEEALSO + ") as ?" + SparqlVariable.SEEALSO + "Count) "
            + "(count(?" + SparqlVariable.SAMEAS + ") as ?" + SparqlVariable.SAMEAS + "Count)\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " rdfs:seeAlso ?" + SparqlVariable.SEEALSO + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " owl:sameAs ?" + SparqlVariable.SAMEAS + " . }\n"
            + "}";
}
