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

    public static final String DATA_LEXICON_LANGUAGES
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICON
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_LABEL
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_LEXVO
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_CATALOG
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_DESCRIPTION
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_CREATOR
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " (count(?" + SparqlVariable.LEXICAL_ENTRY + ") AS ?" + SparqlVariable.LABEL_COUNT + ")\n"
            + "WHERE {\n"
            + "        ?" + SparqlVariable.LEXICON + " a " + SparqlPrefix.LIME.getPrefix() + "Lexicon ;\n"
            + "             " + SparqlPrefix.LIME.getPrefix() + "language ?" + SparqlVariable.LEXICON_LANGUAGE_LABEL + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.DCT.getPrefix() + "language ?" + SparqlVariable.LEXICON_LANGUAGE_LEXVO + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "linguisticCatalog ?" + SparqlVariable.LEXICON_LANGUAGE_CATALOG + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.DCT.getPrefix() + "description ?" + SparqlVariable.LEXICON_LANGUAGE_DESCRIPTION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.DCT.getPrefix() + "creator ?" + SparqlVariable.LEXICON_LANGUAGE_CREATOR + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.DCT.getPrefix() + "created ?" + SparqlVariable.CREATION_DATE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.DCT.getPrefix() + "modified ?" + SparqlVariable.LAST_UPDATE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "entry ?" + SparqlVariable.LEXICAL_ENTRY + " . }\n"
            + "} GROUP BY ?" + SparqlVariable.LEXICON + " ?" + SparqlVariable.LEXICON_LANGUAGE_LABEL
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_LEXVO
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_CATALOG
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_DESCRIPTION
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_CREATOR
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE;

    public static final String DATA_LEXICAL_ENTRIES
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME
            + " ?" + SparqlVariable.LEXICAL_ENTRY_STATUS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_TYPE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR
            + " ?" + SparqlVariable.REVISION_DATE
            + " ?" + SparqlVariable.COMPLETION_DATE
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
            + "          rdfs:label ?" + SparqlVariable.LABEL + " .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:created ?" + SparqlVariable.CREATION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:modified ?" + SparqlVariable.LAST_UPDATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:author ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateAccepted ?" + SparqlVariable.REVISION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateSubmitted ?" + SparqlVariable.COMPLETION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " skos:note ?" + SparqlVariable.NOTE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " loc:rev ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " vs:term_status ?" + SparqlVariable.LEXICAL_ENTRY_STATUS + "} .\n"
            + "   OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ?morphoTrait ?morphoValue . \n"
            + "              BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?traitType)\n"
            + "              BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?traitValue)\n"
            + "              FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:)))\n"
            + "              FILTER(STRSTARTS(STR(?morphoValue), str(lexinfo:))) } \n"
            + "   BIND(strafter(str(?" + SparqlVariable.LEXICAL_ENTRY + "),str(lex:)) as ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME + ") \n"
            + "   FILTER(STRSTARTS(STR(?" + SparqlVariable.LEXICAL_ENTRY_TYPE + "), str(ontolex:)))\n"
            + "} GROUP BY ?"
            + SparqlVariable.LEXICAL_ENTRY + " ?"
            + SparqlVariable.LEXICAL_ENTRY_TYPE + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.LEXICAL_ENTRY_REVISOR + " ?"
            + SparqlVariable.LEXICAL_ENTRY_POS + " ?"
            + SparqlVariable.LEXICAL_ENTRY_STATUS + " ?"
            + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.NOTE + " ?"
            + SparqlVariable.TOTAL_HITS + " ?"
            + SparqlVariable.CREATION_DATE + " ?"
            + SparqlVariable.LAST_UPDATE + " ?"
            + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR + " ?"
            + SparqlVariable.REVISION_DATE + " ?"
            + SparqlVariable.COMPLETION_DATE + " ?"
            + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME;

    public static final String DATA_LEXICAL_ENTRY_ELEMENTS
            = SparqlPrefix.SYNSEM.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.DECOMP.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
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
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.FORM
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
            + " (GROUP_CONCAT(concat(str(?tn),\":\",str(?tv));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "             luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "             luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "   ?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.FORM_TYPE + " ?" + SparqlVariable.FORM + " .\n"
            + "   OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?posTag . }\n"
            + "   ?" + SparqlVariable.FORM + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " ;\n"
            + "       ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ontolex:phoneticRep ?" + SparqlVariable.PHONETIC_REPRESENTATION + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " dct:created ?" + SparqlVariable.CREATION_DATE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + " .\n"
            + "              FILTER(STRSTARTS(STR(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), str(lexinfo:))) }\n"
            + "   ?" + SparqlVariable.FORM_TYPE + " rdfs:subPropertyOf* ontolex:lexicalForm .\n"
            + "   BIND(strafter(str(?" + SparqlVariable.FORM + "), str(lex:)) as ?" + SparqlVariable.FORM_INSTANCE_NAME + ")\n"
            + "   BIND(strafter(str(?posTag), str(lexinfo:)) as ?" + SparqlVariable.LEXICAL_ENTRY_POS + ")\n"
            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),str(lexinfo:)) as ?tn)\n"
            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "),str(lexinfo:)) as ?tv)\n"
            + "   [FORM_CONSTRAINT]"
            + "} GROUP BY ?"
            + SparqlVariable.FORM
            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.NOTE + "\n"
            + "  ORDER BY ?"
            + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.FORM_INSTANCE_NAME;

    public static final String DATA_LEXICAL_SENSES_BY_FORM
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLOGY.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.SENSE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.SENSE_INSTANCE_NAME
            + " ?" + SparqlVariable.CONCEPT
            + " ?" + SparqlVariable.CONCEPT_INSTANCE_NAME
            + " ?" + SparqlVariable.SENSE_USAGE + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?totalHits ;\n"
            + "      luc:orderBy \"lexicalEntryLabel\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . \n"
            + "               BIND(strafter(str(?" + SparqlVariable.SENSE + "), str(lex:)) as ?" + SparqlVariable.SENSE_INSTANCE_NAME + ") }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:canonicalForm ?" + SparqlVariable.FORM + " . \n"
            + "               ?" + SparqlVariable.FORM + " ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:created ?" + SparqlVariable.CREATION_DATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " ontolex:reference ?" + SparqlVariable.CONCEPT + " . \n"
            + "               BIND(strafter(str(?" + SparqlVariable.CONCEPT + "), str(ontology:)) as ?" + SparqlVariable.CONCEPT_INSTANCE_NAME + ") }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " ontolex:usage [ rdf:value ?" + SparqlVariable.SENSE_USAGE + " ] . }\n"
            + "}\n"
            + " ";

    public static final String DATA_LEXICAL_SENSES
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLOGY.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.SENSE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.SENSE_INSTANCE_NAME
            + " ?" + SparqlVariable.CONCEPT
            + " ?" + SparqlVariable.CONCEPT_INSTANCE_NAME
            + " ?" + SparqlVariable.SENSE_USAGE + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?totalHits ;\n"
            + "      luc:orderBy \"lexicalEntryLabel\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . \n"
            + "               BIND(strafter(str(?" + SparqlVariable.SENSE + "), str(lex:)) as ?" + SparqlVariable.SENSE_INSTANCE_NAME + ") }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:canonicalForm ?" + SparqlVariable.FORM + " . \n"
            + "               ?" + SparqlVariable.FORM + " ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:created ?" + SparqlVariable.CREATION_DATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " ontolex:reference ?" + SparqlVariable.CONCEPT + " . \n"
            + "               BIND(strafter(str(?" + SparqlVariable.CONCEPT + "), str(ontology:)) as ?" + SparqlVariable.CONCEPT_INSTANCE_NAME + ") }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " ontolex:usage [ rdf:value ?" + SparqlVariable.SENSE_USAGE + " ] . }\n"
            + "}\n"
            + " ";

    public static final String DATA_FRAMES
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SYNSEM.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT ?frame ?author ?pattern ?note (count(?arg) as ?argCount)\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      :query \"lexicalEntryIRI:*mylexicon*sbandare*2\" ;\n"
            + "      :entities ?lexicalEntry .\n"
            + "  ?lexicalEntry synsem:synBehaviour ?frame .\n"
            + "  ?frame dct:creator ?author ;\n"
            + "         rdf:type ?type .\n"
            + "    FILTER(STRSTARTS(STR(?type), \"http://www.lexinfo.net/ontology/3.0/lexinfo#\"))\n"
            + "    OPTIONAL { ?frame skos:note ?note . }\n"
            + "    OPTIONAL { ?frame skos:example ?pattern . }\n"
            + "    OPTIONAL { ?frame synsem:synArg ?arg . }\n"
            + "} GROUP BY ?frame ?author ?pattern ?note";

    public static final String DATA_LEXICAL_ENTRY_CORE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_TYPE
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_STATUS
            //            + " ?" + SparqlVariable.CONCEPT
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR
            + " ?" + SparqlVariable.COMPLETION_DATE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.REVISION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.NOTE
            //            + " ?" + SparqlVariable.LEXICAL_CONCEPT
            //            + " ?" + SparqlVariable.CONCEPT_SCHEME 
            + "\n"
            + "(GROUP_CONCAT(concat(str(?traitType),\":\",str(?traitValue));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "  ?" + SparqlVariable.LEXICAL_ENTRY + " sesame:directType ?" + SparqlVariable.LEXICAL_ENTRY_TYPE + " ;\n"
            + "                rdfs:label ?" + SparqlVariable.LABEL + ".\n"
            + "    FILTER(STRSTARTS(STR(?" + SparqlVariable.LEXICAL_ENTRY_TYPE + "), str(ontolex:)))\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:created ?" + SparqlVariable.CREATION_DATE + "} .\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateSubmitted ?" + SparqlVariable.COMPLETION_DATE + "} .\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateAccepted ?" + SparqlVariable.REVISION_DATE + "} .\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:modified ?" + SparqlVariable.LAST_UPDATE + "} .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            //            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:denotes ?" + SparqlVariable.CONCEPT + " . }\n"
            //            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:evokes ?" + SparqlVariable.LEXICAL_CONCEPT + " . \n"
            //            + "               ?" + SparqlVariable.CONCEPT + " skos:inScheme ?" + SparqlVariable.CONCEPT_SCHEME + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " vs:term_status ?" + SparqlVariable.LEXICAL_ENTRY_STATUS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " dct:author ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " loc:rev ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ?morphoTrait ?morphoValue . \n"
            + "    FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:)))\n"
            + "    FILTER(STRSTARTS(STR(?morphoValue), str(lexinfo:)))\n"
            + "    BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?traitType)\n"
            + "    BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?traitValue) }\n"
            + "} GROUP BY ?"
            + SparqlVariable.LEXICAL_ENTRY + " ?"
            + SparqlVariable.LEXICAL_ENTRY_TYPE + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.LEXICAL_ENTRY_REVISOR + " ?"
            + SparqlVariable.LEXICAL_ENTRY_POS + " ?"
            + SparqlVariable.LEXICAL_ENTRY_STATUS + " ?"
            + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " ?"
            + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.NOTE
            //            + " ?"
            //            + SparqlVariable.CONCEPT + " ?"
            //            + SparqlVariable.LEXICAL_CONCEPT + " ?"
            //            + SparqlVariable.CONCEPT_SCHEME
            + " ?" + SparqlVariable.COMPLETION_DATE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.REVISION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE;

    public static final String DATA_FORM_CORE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "SELECT ?"
            + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.FORM
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_NAME
            + " ?" + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_VALUE
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.TRANSLITERATION
            + " ?" + SparqlVariable.SEGMENTATION
            + " ?" + SparqlVariable.PRONUNCIATION
            + " ?" + SparqlVariable.ROMANIZATION
            + " (GROUP_CONCAT(concat(str(?traitName),\":\",str(?traitValue));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ") "
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.FORM_CREATION_AUTHOR
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a " + SparqlPrefix.INST.getPrefix() + SparqlVariable.FORM_INDEX + " ;\n"
            + "      " + SparqlPrefix.LUC.getPrefix() + "query \"formIRI:[IRI]\" ;\n"
            + "      " + SparqlPrefix.LUC.getPrefix() + "totalHits ?" + SparqlVariable.TOTAL_HITS + " ;\n"
            + "      " + SparqlPrefix.LUC.getPrefix() + "entities ?" + SparqlVariable.FORM + " .\n"
            + "    ?" + SparqlVariable.FORM + " " + SparqlPrefix.ONTOLEX.getPrefix() + "writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "    ?le ?" + SparqlVariable.FORM_TYPE + " ?" + SparqlVariable.FORM + " ;\n"
            + "    OPTIONAL { ?le " + SparqlPrefix.LEXINFO.getPrefix() + "partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
            //            + "    ?" + SparqlVariable.FORM_TYPE + " " + SparqlPrefix.RDFS.getPrefix() + "subPropertyOf  " + SparqlPrefix.ONTOLEX.getPrefix() + "lexicalForm .\n"
            + "    OPTIONAL { ?le ?inheritedMorphoTrait ?inheritedMorphoValue .\n"
            + "              FILTER(!regex(STR(?inheritedMorphoTrait), \"partOfSpeech\"))\n"
            + "              FILTER(STRSTARTS(STR(?inheritedMorphoTrait), str(" + SparqlPrefix.LEXINFO.getPrefix() + ")))\n"
            + "              FILTER(STRSTARTS(STR(?inheritedMorphoValue), str(" + SparqlPrefix.LEXINFO.getPrefix() + ")))\n"
            + "              BIND(strafter(str(?inheritedMorphoTrait),str(" + SparqlPrefix.LEXINFO.getPrefix() + ")) as ?" + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_NAME + ")\n"
            + "              BIND(strafter(str(?inheritedMorphoValue),str(" + SparqlPrefix.LEXINFO.getPrefix() + ")) as ?" + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_VALUE + ")\n"
            + "    }\n"
            + "    OPTIONAL { ?" + SparqlVariable.FORM + " ?morphoTrait ?morphoValue .\n"
            + "              FILTER(STRSTARTS(STR(?morphoTrait), str(" + SparqlPrefix.LEXINFO.getPrefix() + ")))\n"
            + "              FILTER(STRSTARTS(STR(?morphoValue), str(" + SparqlPrefix.LEXINFO.getPrefix() + ")))\n"
            + "              BIND(strafter(str(?morphoTrait),str(" + SparqlPrefix.LEXINFO.getPrefix() + ")) as ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + ")\n"
            + "              BIND(strafter(str(?morphoValue),str(" + SparqlPrefix.LEXINFO.getPrefix() + ")) as ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + ")\n"
            + "    }\n"
            + "    OPTIONAL { ?" + SparqlVariable.FORM + " " + SparqlPrefix.ONTOLEX.getPrefix() + "phoneticRep ?" + SparqlVariable.PHONETIC_REPRESENTATION + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.FORM + " " + SparqlPrefix.LEXINFO.getPrefix() + "transliteration ?" + SparqlVariable.TRANSLITERATION + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.FORM + " " + SparqlPrefix.LEXINFO.getPrefix() + "romanization ?" + SparqlVariable.ROMANIZATION + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.FORM + " " + SparqlPrefix.LEXINFO.getPrefix() + "segmentation ?" + SparqlVariable.SEGMENTATION + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.FORM + " " + SparqlPrefix.LEXINFO.getPrefix() + "pronunciation ?" + SparqlVariable.PRONUNCIATION + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.FORM + " " + SparqlPrefix.SKOS.getPrefix() + "note ?" + SparqlVariable.NOTE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.FORM + " " + SparqlPrefix.DCT.getPrefix() + "creator ?" + SparqlVariable.FORM_CREATION_AUTHOR + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.FORM + " " + SparqlPrefix.DCT.getPrefix() + "modified ?" + SparqlVariable.LAST_UPDATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.FORM + " " + SparqlPrefix.DCT.getPrefix() + "created ?" + SparqlVariable.CREATION_DATE + " }\n"
            + "} GROUP BY ?"
            + SparqlVariable.FORM + " ?"
            + SparqlVariable.FORM_TYPE + " ?"
            + SparqlVariable.WRITTEN_REPRESENTATION + " ?"
            + SparqlVariable.LEXICAL_ENTRY_POS + " ?"
            + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_NAME + " ?"
            + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_VALUE + " ?"
            + SparqlVariable.PHONETIC_REPRESENTATION + " ?"
            + SparqlVariable.TRANSLITERATION + " ?"
            + SparqlVariable.SEGMENTATION + " ?"
            + SparqlVariable.PRONUNCIATION + " ?"
            + SparqlVariable.ROMANIZATION + " ?"
            + SparqlVariable.TOTAL_HITS + " ?"
            + SparqlVariable.NOTE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.FORM_CREATION_AUTHOR;

    public static final String DATA_LEXICAL_SENSE_CORE
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.SENSE
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.CONCEPT
            + " ?" + SparqlVariable.SENSE_USAGE
            + " ?" + SparqlVariable.SENSE_DESCRIPTION
            + " ?" + SparqlVariable.SENSE_EXPLANATION
            + " ?" + SparqlVariable.SENSE_GLOSS
            + " ?" + SparqlVariable.SENSE_EXAMPLE
            + " ?" + SparqlVariable.SENSE_TRANSLATION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.SENSE_CREATION_AUTHOR
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.SENSE_TOPIC
            + " ?" + SparqlVariable.LAST_UPDATE + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a " + SparqlPrefix.INST.getPrefix() + SparqlVariable.LEXICAL_SENSE_INDEX + " ;\n"
            + "      " + SparqlPrefix.LUC.getPrefix() + "query \"lexicalSenseIRI:[IRI]\" ;\n"
            + "      " + SparqlPrefix.LUC.getPrefix() + "entities ?" + SparqlVariable.SENSE + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.SKOS.getPrefix() + "definition ?" + SparqlVariable.SENSE_DEFINITION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.ONTOLEX.getPrefix() + "reference ?" + SparqlVariable.CONCEPT + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.ONTOLEX.getPrefix() + "usage [ " + SparqlPrefix.RDF.getPrefix() + "value ?" + SparqlVariable.SENSE_USAGE + " ] . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.LEXINFO.getPrefix() + "description ?" + SparqlVariable.SENSE_DESCRIPTION + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.LEXINFO.getPrefix() + "explanation ?" + SparqlVariable.SENSE_EXPLANATION + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.LEXINFO.getPrefix() + "gloss ?" + SparqlVariable.SENSE_GLOSS + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.LEXINFO.getPrefix() + "senseExample ?" + SparqlVariable.SENSE_EXAMPLE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.LEXINFO.getPrefix() + "senseTranslation ?" + SparqlVariable.SENSE_TRANSLATION + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.SKOS.getPrefix() + "note ?" + SparqlVariable.NOTE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.DCT.getPrefix() + "creator ?" + SparqlVariable.SENSE_CREATION_AUTHOR + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.DCT.getPrefix() + "modified ?" + SparqlVariable.LAST_UPDATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.DCT.getPrefix() + "created ?" + SparqlVariable.CREATION_DATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.DCT.getPrefix() + "subject ?" + SparqlVariable.SENSE_TOPIC + " }\n"
            + "}";

    public static final String DATA_LEXICAL_ENTRY_REFERENCE_LINKS
            = SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + "\n"
            + "SELECT (count(?" + SparqlVariable.SEEALSO + ") as ?" + SparqlVariable.SEEALSO + "Count) "
            + "((count(?" + SparqlVariable.SAMEAS + ") - 1) as ?" + SparqlVariable.SAMEAS + "Count)\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " rdfs:seeAlso ?" + SparqlVariable.SEEALSO + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " owl:sameAs ?" + SparqlVariable.SAMEAS + " . }\n"
            + "}";

    public static final String DATA_LINGUISTIC_RELATION
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + "SELECT ?graph ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.TYPE + "\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "explicit\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "implicit\n"
            + "   { \n"
            + "      GRAPH ?graph { " + SparqlPrefix.LEX.getPrefix() + "_ID_ ?relation ?" + SparqlVariable.TARGET + " . \n"
            + "                                             FILTER (regex(str(?relation), \"_RELATION_\")) }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " " + SparqlPrefix.RDFS.getPrefix() + "label|" + SparqlPrefix.SKOS.getPrefix() + "definition ?" + SparqlVariable.LABEL + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " " + SparqlPrefix.SESAME.getPrefix() + "directType ?" + SparqlVariable.TYPE + " . \n"
            + "    FILTER (!regex(str(?" + SparqlVariable.TYPE + "), \"http://www.w3.org/2000/01/rdf-schema#|http://www.w3.org/1999/02/22-rdf-syntax-ns#|http://www.w3.org/2002/07/owl#\")) }\n"
            + "    FILTER (!regex(str(?relation), \"http://www.ontologydesignpatterns.org/cp/owl/semiotics.owl#\"))\n"
            + "   } ORDER BY ?graph";

    public static final String DATA_GENERIC_RELATION
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + "SELECT ?graph ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.TYPE + "\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "explicit\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "implicit\n"
            + "   { \n"
            + "      GRAPH ?graph { " + SparqlPrefix.LEX.getPrefix() + "_ID_ ?relation ?" + SparqlVariable.TARGET + " . \n"
            + "                                             FILTER (regex(str(?relation), \"_RELATION_\")) }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " " + SparqlPrefix.RDFS.getPrefix() + "label|" + SparqlPrefix.SKOS.getPrefix() + "definition ?" + SparqlVariable.LABEL + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " " + SparqlPrefix.SESAME.getPrefix() + "directType ?" + SparqlVariable.TYPE + " . \n"
            + "    FILTER (!regex(str(?" + SparqlVariable.TYPE + "), \"http://www.w3.org/2000/01/rdf-schema#|http://www.w3.org/1999/02/22-rdf-syntax-ns#|http://www.w3.org/2002/07/owl#\")) }\n"
            + "   } ORDER BY ?graph";

    public static final String DATA_FORMS_BY_SENSE_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.FORM
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
            + " ?" + SparqlVariable.TARGET
            + " (GROUP_CONCAT(concat(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),\":\",str(?"
            + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ") \n"
            + "FROM NAMED onto:explicit\n"
            + "FROM NAMED onto:implicit   \n"
            + "{\n"
            + "    GRAPH ?g { [RELATION_DISTANCE_PATH] }\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.TARGET + " .\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.FORM_TYPE + " ?" + SparqlVariable.FORM + " ;\n"
            + "       lexinfo:partOfSpeech ?posTag .\n"
            + "    ?" + SparqlVariable.FORM + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " ;\n"
            + "       ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ontolex:phoneticRep ?" + SparqlVariable.PHONETIC_REPRESENTATION + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "   OPTIONAL { GRAPH ?g { ?" + SparqlVariable.FORM + " ?morphoTrait ?morphoValue . }  \n"
            + "              FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:))) }\n"
            + "   ?" + SparqlVariable.FORM_TYPE + " sesame:directSubPropertyOf ontolex:lexicalForm .\n"
            + "   BIND(strafter(str(?" + SparqlVariable.FORM + "), str(lex:)) as ?" + SparqlVariable.FORM_INSTANCE_NAME + ")\n"
            + "   BIND(strafter(str(?posTag),str(lexinfo:)) as ?" + SparqlVariable.LEXICAL_ENTRY_POS + ")\n"
            + "   BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + ")\n"
            + "   BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + ")\n"
            + "   FILTER(regex(str(?g), \"explicit\")) \n"
            + "} GROUP BY ?"
            + SparqlVariable.FORM
            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.TARGET
            + " ?" + SparqlVariable.NOTE + "\n"
            + "  ORDER BY ?"
            + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.FORM_INSTANCE_NAME;

    public static final String DATA_SENSE_BY_CONCEPT
            = SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLOGY.getSparqlPrefix() + "\n"
            + "\n"
            + "SELECT ?" + SparqlVariable.WRITTEN_REPRESENTATION + " ?" + SparqlVariable.LEXICAL_ENTRY_POS + " ?" + SparqlVariable.SENSE_DEFINITION + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "    ?" + SparqlVariable.SENSE + " ontolex:reference ontology:_CONCEPT_ ;\n"
            + "           skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " .\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " ;\n"
            + "        lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " ;\n"
            + "        ontolex:canonicalForm ?" + SparqlVariable.FORM + " . \n"
            + "    ?" + SparqlVariable.FORM + " ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "} ORDER BY ?" + SparqlVariable.WRITTEN_REPRESENTATION;

    public static final String DATA_PATH_LENGTH
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "SELECT ?lexicalEntry ?" + SparqlVariable.IRI + " (count(?mid) as ?lenght) { \n"
            + "  lex:[START_NODE] lexinfo:[START_RELATION]* ?mid .\n"
            + "  ?mid lexinfo:[MID_RELATION]+ ?" + SparqlVariable.IRI + " .\n"
            + "  ?lexicalEntry ontolex:sense ?" + SparqlVariable.IRI + " .\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.IRI + " ?lexicalEntry \n"
            + "ORDER BY ?lenght";

    public static final String DATA_RELATION
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "SELECT ?g ?target ?id\n"
            + "FROM NAMED onto:explicit\n"
            + "FROM NAMED onto:implicit \n"
            + "{ \n"
            + "  GRAPH ?g { lex:[NODE] lexinfo:[RELATION] ?target . \n"
            + "        [FILTER] } \n"
            + "  ?id [ID_RELATION] ?target .\n"
            + "}";

    public static final String LEXICON_ENTRY_LANGUAGE
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.LABEL + "\n"
            + "where { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " .\n"
            + "}";

    public static final String LEXICON_ENTRY_STATUS
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.LABEL + "\n"
            + "where { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.VS.getPrefix() + "term_status ?" + SparqlVariable.LABEL + " .\n"
            + "}";
}
