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
            //            + " ?" + SparqlVariable.LEXICON_LANGUAGE_CATALOG
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_DESCRIPTION
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_CREATOR
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + "(count(distinct(?" + SparqlVariable.LEXICAL_ENTRY + ")) AS ?" + SparqlVariable.LABEL_COUNT + ")\n"
            + "(GROUP_CONCAT(distinct(str(?_" + SparqlVariable.LEXICON_LANGUAGE_CATALOG + "));SEPARATOR=\";\") AS ?" + SparqlVariable.LEXICON_LANGUAGE_CATALOG + ")"
            + "WHERE {\n"
            + "        ?" + SparqlVariable.LEXICON + " a " + SparqlPrefix.LIME.getPrefix() + "Lexicon ;\n"
            + "             " + SparqlPrefix.LIME.getPrefix() + "language ?" + SparqlVariable.LEXICON_LANGUAGE_LABEL + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.DCT.getPrefix() + "language ?" + SparqlVariable.LEXICON_LANGUAGE_LEXVO + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "linguisticCatalog ?_" + SparqlVariable.LEXICON_LANGUAGE_CATALOG + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.DCT.getPrefix() + "description ?" + SparqlVariable.LEXICON_LANGUAGE_DESCRIPTION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.DCT.getPrefix() + "creator ?" + SparqlVariable.LEXICON_LANGUAGE_CREATOR + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.DCT.getPrefix() + "created ?" + SparqlVariable.CREATION_DATE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.DCT.getPrefix() + "modified ?" + SparqlVariable.LAST_UPDATE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "entry ?" + SparqlVariable.LEXICAL_ENTRY + " . }\n"
            + "} GROUP BY ?" + SparqlVariable.LEXICON + " ?" + SparqlVariable.LEXICON_LANGUAGE_LABEL
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_LEXVO
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_DESCRIPTION
            + " ?" + SparqlVariable.LEXICON_LANGUAGE_CREATOR
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE;

    public static final String DATA_LEXICAL_ENTRIES
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.FOAF.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            //            + " ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME
            + " ?" + SparqlVariable.LEXICAL_ENTRY_STATUS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.IMAGE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR
            + " ?" + SparqlVariable.REVISION_DATE
            + " ?" + SparqlVariable.COMPLETION_DATE
            + " ?confidence"
            + "\n"
            + "(GROUP_CONCAT(distinct concat(str(?morphoTrait),\"<>\",str(?morphoValue));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "(GROUP_CONCAT(distinct str(?tmp_type);SEPARATOR=\";\") AS ?type)\n"
            + "(GROUP_CONCAT(distinct str(?_" + SparqlVariable.IMAGE + ");SEPARATOR=\";\") AS ?" + SparqlVariable.IMAGE + ")\n"
            + "FROM onto:explicit WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?totalHits ;\n"
            + "      luc:orderBy \"lexicalEntryLabel\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "  ?" + SparqlVariable.LEXICAL_ENTRY + " rdf:type ?tmp_type ;\n"
            + "          rdfs:label ?" + SparqlVariable.LABEL + " .\n"
            + "   ?tmp_type rdfs:label ?_type .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + "} .\n"
            + "   OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:confidence ?confidence . }\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:created ?" + SparqlVariable.CREATION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:modified ?" + SparqlVariable.LAST_UPDATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:author ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateAccepted ?" + SparqlVariable.REVISION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateSubmitted ?" + SparqlVariable.COMPLETION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " skos:note ?" + SparqlVariable.NOTE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " loc:rev ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " vs:term_status ?" + SparqlVariable.LEXICAL_ENTRY_STATUS + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " foaf:depiction ?_" + SparqlVariable.IMAGE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " ?morphoTrait ?morphoValue . \n"
            + "              ?morphoTrait rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?morphoTrait), \"morphosyntacticProperty\")) }\n"
            //            + "              BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?traitType)\n"
            //            + "              BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?traitValue)\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:)))\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoValue), str(lexinfo:))) } \n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.LEXICAL_ENTRY + "),str(lex:)) as ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME + ") \n"
            + "   FILTER(!STRSTARTS(STR(?tmp_type), str(owl:)))\n"
            + "   FILTER(regex(str(?_type), \"_TYPE_\"))"
            + "} GROUP BY ?"
            + SparqlVariable.LEXICAL_ENTRY + " ?"
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
            + SparqlVariable.COMPLETION_DATE
            //            + " ?"
            //            + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME 
            + " ?confidence";

    public static final String DATA_LEXICAL_ENTRY_ELEMENTS
            = SparqlPrefix.SYNSEM.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.DECOMP.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "SELECT (count(?" + SparqlVariable.FORM + ") as ?" + SparqlVariable.FORM + "Count) "
            + "(count(?" + SparqlVariable.SENSE + ") as ?" + SparqlVariable.SENSE + "Count) "
            + "(count(?" + SparqlVariable.FRAME + ") as ?" + SparqlVariable.FRAME + "Count) "
            + "(count(?" + SparqlVariable.LEXICAL_CONCEPT + ") as ?" + SparqlVariable.LEXICAL_CONCEPT + "Count) "
            + "(count(?" + SparqlVariable.CONCEPT + ") as ?" + SparqlVariable.CONCEPT + "Count)\n"
            + "(count(?" + SparqlVariable.LEXICAL_ENTRY_SUBTERM + ") as ?" + SparqlVariable.LEXICAL_ENTRY_SUBTERM + "Count)\n"
            + "(count(?" + SparqlVariable.LEXICAL_ENTRY_CONSTITUENT + ") as ?" + SparqlVariable.LEXICAL_ENTRY_CONSTITUENT + "Count)\n"
            + "(count(?" + SparqlVariable.LEXICAL_ENTRY_ETYMOLOGY + ") as ?" + SparqlVariable.LEXICAL_ENTRY_ETYMOLOGY + "Count)\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:lexicalForm ?" + SparqlVariable.FORM + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " synsem:synBehavior ?" + SparqlVariable.FRAME + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:evokes ?" + SparqlVariable.LEXICAL_CONCEPT + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:denotes ?" + SparqlVariable.CONCEPT + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " decomp:subterm ?" + SparqlVariable.LEXICAL_ENTRY_SUBTERM + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " decomp:constituent ?" + SparqlVariable.LEXICAL_ENTRY_CONSTITUENT + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " ety:etymology ?" + SparqlVariable.LEXICAL_ENTRY_ETYMOLOGY + " . }\n"
            + "}";

    public static final String DATA_FORMS
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.FORM
            + " ?" + SparqlVariable.FORM_CREATION_AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.PRONUNCIATION
            + " ?" + SparqlVariable.SEGMENTATION
            + " ?" + SparqlVariable.TRANSLITERATION
            + " ?" + SparqlVariable.ROMANIZATION
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.NOTE
            //            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
            + " ?confidence"
            + " (GROUP_CONCAT(concat(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),\"<>\",str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.FORM_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?totalHits ;\n"
            + "      luc:orderBy \"writtenRep\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.FORM + " .\n"
            + "   ?" + SparqlVariable.FORM + " ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "   ?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.FORM_TYPE + " ?" + SparqlVariable.FORM + " .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " } .\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ontolex:phoneticRep ?" + SparqlVariable.PHONETIC_REPRESENTATION + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " lexinfo:confidence ?confidence . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " lexinfo:pronunciation ?" + SparqlVariable.PRONUNCIATION + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " lexinfo:romanization ?" + SparqlVariable.ROMANIZATION + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " lexinfo:transliteration ?" + SparqlVariable.TRANSLITERATION + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " lexinfo:segmentation ?" + SparqlVariable.SEGMENTATION + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " dct:created ?" + SparqlVariable.CREATION_DATE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " dct:creator ?" + SparqlVariable.FORM_CREATION_AUTHOR + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + " .\n"
            + "              ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), \"morphosyntacticProperty\")) }\n"
            //            + "              FILTER(STRSTARTS(STR(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), str(lexinfo:))) }\n"
            + "   VALUES (?formType) { (ontolex:lexicalForm) (ontolex:canonicalForm) (ontolex:otherForm) } .\n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.FORM + "), str(lex:)) as ?" + SparqlVariable.FORM_INSTANCE_NAME + ")\n"
            //            + "   BIND(strafter(str(?posTag), str(lexinfo:)) as ?" + SparqlVariable.LEXICAL_ENTRY_POS + ")\n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),str(lexinfo:)) as ?tn)\n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "),str(lexinfo:)) as ?tv)\n"
            + "} GROUP BY ?"
            + SparqlVariable.FORM
            //            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.FORM_CREATION_AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.PRONUNCIATION
            + " ?" + SparqlVariable.SEGMENTATION
            + " ?" + SparqlVariable.TRANSLITERATION
            + " ?" + SparqlVariable.ROMANIZATION
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?confidence"
            + " ?" + SparqlVariable.NOTE;

    public static final String DATA_LEXICAL_SENSES_BY_FORM
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLOGY.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.SENSE
            + " ?" + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.CREATION_DATE
            //            + " ?" + SparqlVariable.SENSE_INSTANCE_NAME
            + " ?" + SparqlVariable.CONCEPT
            + " ?" + SparqlVariable.SENSE_DESCRIPTION
            + " ?" + SparqlVariable.SENSE_EXAMPLE
            + " ?" + SparqlVariable.SENSE_GLOSS
            + " ?" + SparqlVariable.SENSE_TRANSLATION
            //            + " ?" + SparqlVariable.CONCEPT_INSTANCE_NAME
            + " ?" + SparqlVariable.SENSE_USAGE + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?" + SparqlVariable.TOTAL_HITS + " ;\n"
            + "      luc:orderBy \"lexicalEntryLabel\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . \n"
            //            + "               BIND(strafter(str(?" + SparqlVariable.SENSE + "), str(lex:)) as ?" + SparqlVariable.SENSE_INSTANCE_NAME + ")"
            + " }\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " rdfs:label ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:created ?" + SparqlVariable.CREATION_DATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " lexinfo:description ?" + SparqlVariable.SENSE_DESCRIPTION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " lexinfo:gloss ?" + SparqlVariable.SENSE_GLOSS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " lexinfo:senseExample ?" + SparqlVariable.SENSE_EXAMPLE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " lexinfo:senseTranslation ?" + SparqlVariable.SENSE_TRANSLATION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " ontolex:reference ?" + SparqlVariable.CONCEPT + " . } \n"
            //            + "               BIND(strafter(str(?" + SparqlVariable.CONCEPT + "), str(ontology:)) as ?" + SparqlVariable.CONCEPT_INSTANCE_NAME + ") }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " ontolex:usage [ rdf:value ?" + SparqlVariable.SENSE_USAGE + " ] . }\n"
            + "}\n"
            + " ";

    public static final String DATA_LEXICAL_SENSES_FILTER
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLOGY.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.SENSE
            + " ?" + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.SENSE_CREATION_AUTHOR
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.CREATION_DATE
            //            + " ?" + SparqlVariable.SENSE_INSTANCE_NAME
            + " ?" + SparqlVariable.CONCEPT
            //            + " ?" + SparqlVariable.CONCEPT_INSTANCE_NAME
            + " ?" + SparqlVariable.SENSE_USAGE
            + " ?" + SparqlVariable.SENSE_DESCRIPTION
            + " ?" + SparqlVariable.SENSE_EXAMPLE
            + " ?" + SparqlVariable.SENSE_GLOSS
            + " ?confidence"
            + " ?" + SparqlVariable.SENSE_TRANSLATION + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_SENSE_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?" + SparqlVariable.TOTAL_HITS + " ;\n"
            + "      luc:orderBy \"definition\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.SENSE + " .\n"
            //            + "    BIND(strafter(str(?" + SparqlVariable.SENSE + "), str(lex:)) as ?" + SparqlVariable.SENSE_INSTANCE_NAME + ") \n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:canonicalForm [ ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " ] . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:creator ?" + SparqlVariable.SENSE_CREATION_AUTHOR + " . } \n"
            // custom variabile
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:confidence ?confidence . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:created ?" + SparqlVariable.CREATION_DATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:description ?" + SparqlVariable.SENSE_DESCRIPTION + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:gloss ?" + SparqlVariable.SENSE_GLOSS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:senseExample ?" + SparqlVariable.SENSE_EXAMPLE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:senseTranslation ?" + SparqlVariable.SENSE_TRANSLATION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " ontolex:reference ?" + SparqlVariable.CONCEPT + " . }\n"
            //            + "               BIND(strafter(str(?" + SparqlVariable.CONCEPT + "), str(ontology:)) as ?" + SparqlVariable.CONCEPT_INSTANCE_NAME + ") }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " ontolex:usage [ rdf:value ?" + SparqlVariable.SENSE_USAGE + " ] . }\n"
            + "} ORDER BY ?" + SparqlVariable.LEXICAL_ENTRY + "\n"
            + " ";

    public static final String DATA_LEXICAL_SENSES
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLOGY.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.SENSE
            + " ?" + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.SENSE_CREATION_AUTHOR
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.CREATION_DATE
            //            + " ?" + SparqlVariable.SENSE_INSTANCE_NAME
            + " ?" + SparqlVariable.CONCEPT
            //            + " ?" + SparqlVariable.CONCEPT_INSTANCE_NAME
            + " ?" + SparqlVariable.SENSE_USAGE
            + " ?" + SparqlVariable.SENSE_DESCRIPTION
            + " ?" + SparqlVariable.SENSE_EXAMPLE
            + " ?" + SparqlVariable.SENSE_GLOSS
            + " ?confidence"
            + " ?" + SparqlVariable.SENSE_TRANSLATION + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?" + SparqlVariable.TOTAL_HITS + " ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            //            + "    BIND(strafter(str(?" + SparqlVariable.SENSE + "), str(lex:)) as ?" + SparqlVariable.SENSE_INSTANCE_NAME + ") \n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:canonicalForm [ ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " ] . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:creator ?" + SparqlVariable.SENSE_CREATION_AUTHOR + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:confidence ?confidence . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:created ?" + SparqlVariable.CREATION_DATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:description ?" + SparqlVariable.SENSE_DESCRIPTION + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:gloss ?" + SparqlVariable.SENSE_GLOSS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:senseExample ?" + SparqlVariable.SENSE_EXAMPLE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:senseTranslation ?" + SparqlVariable.SENSE_TRANSLATION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " ontolex:reference ?" + SparqlVariable.CONCEPT + " . } \n"
            //            + "               BIND(strafter(str(?" + SparqlVariable.CONCEPT + "), str(ontology:)) as ?" + SparqlVariable.CONCEPT_INSTANCE_NAME + ") }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " ontolex:usage [ rdf:value ?" + SparqlVariable.SENSE_USAGE + " ] . }\n"
            + "} ORDER BY ?" + SparqlVariable.LEXICAL_ENTRY + "\n"
            + " ";

    public static final String DATA_ETYMOLOGIES
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLOGY.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.ETYMOLOGY_CREATION_AUTHOR
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.HYPOTHESIS_OF
            + " ?" + SparqlVariable.CONFIDENCE
            //            + " ?" + SparqlVariable.ETY_LINK_TYPE
            + " ?" + SparqlVariable.ETYMOLOGY
            + " ?" + SparqlVariable.LABEL + "\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?" + SparqlVariable.TOTAL_HITS + " ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ety:etymology ?" + SparqlVariable.ETYMOLOGY + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETYMOLOGY + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETYMOLOGY + " rdfs:label ?" + SparqlVariable.LABEL + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETYMOLOGY + " rdfs:comment ?" + SparqlVariable.HYPOTHESIS_OF + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.ETYMOLOGY + " lexinfo:confidence ?" + SparqlVariable.CONFIDENCE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.ETYMOLOGY + " dct:created ?" + SparqlVariable.CREATION_DATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.ETYMOLOGY + " dct:creator ?" + SparqlVariable.ETYMOLOGY_CREATION_AUTHOR + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.ETYMOLOGY + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . } \n"
            //            + "    OPTIONAL { ?" + SparqlVariable.ETYMOLOGY + " ety:hasEtyLink ?" + SparqlVariable.ETY_LINK + " . } \n"
            //            + "    OPTIONAL { ?" + SparqlVariable.ETY_LINK + " ety:etyLinkType ?" + SparqlVariable.ETY_LINK_TYPE + " . } \n"
            + "} ORDER BY ?" + SparqlVariable.LEXICAL_ENTRY + "\n"
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
            + "  ?lexicalEntry synsem:synBehavior ?frame .\n"
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
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ITANT.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_TYPE
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_STATUS
            + " ?stemType"
            + " ?confidence"
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
            + "(GROUP_CONCAT(concat(str(?morphoTrait),\"<>\",str(?morphoValue));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "  ?" + SparqlVariable.LEXICAL_ENTRY + " rdf:type ?" + SparqlVariable.LEXICAL_ENTRY_TYPE + " ;\n"
            + "                rdfs:label ?" + SparqlVariable.LABEL + ".\n"
            + "    FILTER(!STRSTARTS(STR(?" + SparqlVariable.LEXICAL_ENTRY_TYPE + "), str(owl:)))\n"
            + "    FILTER(!STRSTARTS(STR(?type), \"http://www.ontologydesignpatterns.org/cp/owl/semiotics.owl#Expression\"))"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:created ?" + SparqlVariable.CREATION_DATE + "} .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:confidence ?confidence . }\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " itant:stemType ?stemType} .\n"
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
            + "              ?morphoTrait rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?morphoTrait), \"morphosyntacticProperty\")) }\n"
            //            + "    FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:)))\n"
            //            + "    FILTER(STRSTARTS(STR(?morphoValue), str(lexinfo:)))\n"
            //            + "    BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?traitType)\n"
            //            + "    BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?traitValue) }\n"
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
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?confidence"
            + " ?stemType";

    public static final String DATA_LEXICAL_ENTRY_DIRECT_VARTRANS
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.VARTRANS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LEXICAL_ENTITY + " \n"
            + "(GROUP_CONCAT(?_type;SEPARATOR=\";\") as ?" + SparqlVariable.TYPE + ")\n"
            + "?" + SparqlVariable.LABEL + " ?" + SparqlVariable.GRAPH + "\n"
            + "FROM NAMED onto:implicit\n"
            + "FROM NAMED onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:lexicalEntryIndex ;\n"
            + "      luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    OPTIONAL { GRAPH ?" + SparqlVariable.GRAPH + " { ?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LEXICAL_ENTITY + " } .\n"
            + "               ?" + SparqlVariable.PROPERTY_NAME + " rdfs:subPropertyOf vartrans:lexicalRel . \n"
            + "               OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTITY + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "               OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTITY + " sesame:directType ?_type }\n"
            + "               FILTER(!STRSTARTS(STR(?" + SparqlVariable.PROPERTY_NAME + "), \"http://www.w3.org/ns/lemon/vartrans#lexicalRel\"))\n"
            + "    }\n"
            + "               FILTER(BOUND(?" + SparqlVariable.LEXICAL_ENTITY + "))\n"
            + "} \n"
            + "GROUP BY ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LEXICAL_ENTITY + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.GRAPH + "\n"
            + "ORDER BY ?" + SparqlVariable.PROPERTY_NAME;

    public static final String DATA_LEXICAL_ENTRY_INDIRECT_VARTRANS
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.VARTRANS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT \n"
            + "?" + SparqlVariable.REIFIED_RELATION + " ?" + SparqlVariable.CATEGORY + " ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.SOURCE
            + " ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.TARGET_LABEL + " ?" + SparqlVariable.DEFINITION + " ?" + SparqlVariable.NOTE + " ?" + SparqlVariable.LABEL + "\n"
            + "(GROUP_CONCAT(concat(str(?reifiedRelationPredicate),\"<>\",str(?reifiedRelationObject),\"<>\",str(?graph));SEPARATOR=\"---\") AS ?extra)\n"
            + "FROM NAMED onto:implicit\n"
            + "FROM NAMED onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:lexicalEntryIndex ;\n"
            + "      luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.SOURCE + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " vartrans:source ?" + SparqlVariable.SOURCE + " .\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " vartrans:category ?" + SparqlVariable.CATEGORY + " }\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " skos:note ?" + SparqlVariable.NOTE + " }\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " sesame:directType ?" + SparqlVariable.TYPE + " .\n"
            + "        FILTER(!STRSTARTS(STR(?" + SparqlVariable.TYPE + "), \"http://www.w3.org/ns/lemon/lexicog#\")) }\n"
            + "    	OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " vartrans:target ?" + SparqlVariable.TARGET + " }\n"
            + "    	OPTIONAL { ?" + SparqlVariable.TARGET + " rdfs:label ?" + SparqlVariable.TARGET_LABEL + " ;\n"
            + "    					skos:definition ?" + SparqlVariable.DEFINITION + " .\n"
            + "    	}\n"
            + "    	OPTIONAL { \n"
            + "        	GRAPH ?graph { ?" + SparqlVariable.REIFIED_RELATION + " ?reifiedRelationPredicate ?reifiedRelationObject . } \n"
            + "        	FILTER(!STRSTARTS(STR(?reifiedRelationPredicate), \"http://www.w3.org/ns/lemon/vartrans#\"))\n"
            + "        	FILTER(!STRSTARTS(STR(?reifiedRelationPredicate), \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\"))\n"
            + "    	}\n"
            + "    }\n"
            + "    FILTER(BOUND(?" + SparqlVariable.REIFIED_RELATION + "))\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.REIFIED_RELATION + " ?" + SparqlVariable.CATEGORY + " ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.SOURCE
            + " ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.TARGET_LABEL + " ?" + SparqlVariable.DEFINITION + " ?" + SparqlVariable.NOTE + " ?" + SparqlVariable.LABEL + "\n"
            + "ORDER BY ?" + SparqlVariable.REIFIED_RELATION;

    public static final String DATA_FORM_CORE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
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
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LABEL
            + " ?confidence"
            + " (GROUP_CONCAT(concat(str(?traitName),\"<>\",str(?traitValue));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ") "
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
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " lexinfo:confidence ?confidence . }\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.FORM_TYPE + " ?" + SparqlVariable.FORM + " ;\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " " + SparqlPrefix.LEXINFO.getPrefix() + "partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
            //            + "    ?" + SparqlVariable.FORM_TYPE + " " + SparqlPrefix.RDFS.getPrefix() + "subPropertyOf  " + SparqlPrefix.ONTOLEX.getPrefix() + "lexicalForm .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_VALUE + " .\n"
            + "              FILTER(!regex(STR(?" + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_NAME + "), \"partOfSpeech\"))\n"
            + "              ?" + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_NAME + " rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?" + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_NAME + "), \"morphosyntacticProperty\")) }\n"
            //            + "              FILTER(STRSTARTS(STR(?inheritedMorphoTrait), str(" + SparqlPrefix.LEXINFO.getPrefix() + ")))\n"
            //            + "              FILTER(STRSTARTS(STR(?inheritedMorphoValue), str(" + SparqlPrefix.LEXINFO.getPrefix() + ")))\n"
            //            + "              BIND(strafter(str(?inheritedMorphoTrait),str(" + SparqlPrefix.LEXINFO.getPrefix() + ")) as ?" + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_NAME + ")\n"
            //            + "              BIND(strafter(str(?inheritedMorphoValue),str(" + SparqlPrefix.LEXINFO.getPrefix() + ")) as ?" + SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_VALUE + ")\n"
            //            + "    }\n"
            + "    OPTIONAL { ?" + SparqlVariable.FORM + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + " .\n"
            + "              ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), \"morphosyntacticProperty\")) }\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoTrait), str(" + SparqlPrefix.LEXINFO.getPrefix() + ")))\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoValue), str(" + SparqlPrefix.LEXINFO.getPrefix() + ")))\n"
            //            + "              BIND(strafter(str(?morphoTrait),str(" + SparqlPrefix.LEXINFO.getPrefix() + ")) as ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + ")\n"
            //            + "              BIND(strafter(str(?morphoValue),str(" + SparqlPrefix.LEXINFO.getPrefix() + ")) as ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + ")\n"
            //            + "    }\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " " + SparqlPrefix.ONTOLEX.getPrefix() + "lexicalForm|" + SparqlPrefix.ONTOLEX.getPrefix() + "canonicalForm|" + SparqlPrefix.ONTOLEX.getPrefix() + "otherForm ?" + SparqlVariable.FORM + " ;\n"
            + "    " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " .\n"
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
            + " ?confidence"
            + " ?" + SparqlVariable.FORM_CREATION_AUTHOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LABEL;

    
    public static final String DATA_FORM_DIRECT_VARTRANS
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.VARTRANS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.FORM + " \n"
            + "(GROUP_CONCAT(?_type;SEPARATOR=\";\") as ?" + SparqlVariable.TYPE + ")\n"
            + "?" + SparqlVariable.LABEL + " ?" + SparqlVariable.GRAPH + "\n"
            + "FROM NAMED onto:implicit\n"
            + "FROM NAMED onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:formIndex ;\n"
            + "      luc:query \"formIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.FORM + " .\n"
            + "    OPTIONAL { GRAPH ?" + SparqlVariable.GRAPH + " { ?" + SparqlVariable.FORM + " ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LEXICAL_ENTITY + " } .\n"
            + "               ?" + SparqlVariable.PROPERTY_NAME + " rdfs:subPropertyOf vartrans:lexicalRel . \n"
            + "               OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTITY + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "               OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTITY + " sesame:directType ?_type }\n"
            + "               FILTER(!STRSTARTS(STR(?" + SparqlVariable.PROPERTY_NAME + "), \"http://www.w3.org/ns/lemon/vartrans#lexicalRel\"))\n"
            + "    }\n"
            + "               FILTER(BOUND(?" + SparqlVariable.LEXICAL_ENTITY + "))\n"
            + "} \n"
            + "GROUP BY ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LEXICAL_ENTITY + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.GRAPH + "\n"
            + "ORDER BY ?" + SparqlVariable.PROPERTY_NAME;
    
    public static final String DATA_FORM_INDIRECT_VARTRANS
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.VARTRANS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT \n"
            + "?" + SparqlVariable.REIFIED_RELATION + " ?" + SparqlVariable.CATEGORY + " ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.SOURCE
            + " ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.TARGET_LABEL + " ?" + SparqlVariable.NOTE + " ?" + SparqlVariable.LABEL + "\n"
            + "(GROUP_CONCAT(concat(str(?reifiedRelationPredicate),\"<>\",str(?reifiedRelationObject),\"<>\",str(?graph));SEPARATOR=\"---\") AS ?extra)\n"
            + "FROM NAMED onto:implicit\n"
            + "FROM NAMED onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:formIndex ;\n"
            + "      luc:query \"formIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.SOURCE + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " vartrans:source ?" + SparqlVariable.SOURCE + " .\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " vartrans:category ?" + SparqlVariable.CATEGORY + " }\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " skos:note ?" + SparqlVariable.NOTE + " }\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " sesame:directType ?" + SparqlVariable.TYPE + " .\n"
            + "        FILTER(!STRSTARTS(STR(?" + SparqlVariable.TYPE + "), \"http://www.w3.org/ns/lemon/lexicog#\")) }\n"
            + "    	OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " vartrans:target ?" + SparqlVariable.TARGET + " }\n"
            + "    	OPTIONAL { ?" + SparqlVariable.TARGET + " ontolex:writtenRep ?" + SparqlVariable.TARGET_LABEL + " .\n"
            + "    	}\n"
            + "    	OPTIONAL { \n"
            + "        	GRAPH ?graph { ?" + SparqlVariable.REIFIED_RELATION + " ?reifiedRelationPredicate ?reifiedRelationObject . } \n"
            + "        	FILTER(!STRSTARTS(STR(?reifiedRelationPredicate), \"http://www.w3.org/ns/lemon/vartrans#\"))\n"
            + "        	FILTER(!STRSTARTS(STR(?reifiedRelationPredicate), \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\"))\n"
            + "    	}\n"
            + "    }\n"
            + "    FILTER(BOUND(?" + SparqlVariable.REIFIED_RELATION + "))\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.REIFIED_RELATION + " ?" + SparqlVariable.CATEGORY + " ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.SOURCE
            + " ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.TARGET_LABEL + " ?" + SparqlVariable.NOTE + " ?" + SparqlVariable.LABEL + "\n"
            + "ORDER BY ?" + SparqlVariable.REIFIED_RELATION;
    
    public static final String DATA_LEXICAL_SENSE_CORE
            = SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
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
            + " ?confidence"
            + " ?" + SparqlVariable.LAST_UPDATE + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a " + SparqlPrefix.INST.getPrefix() + SparqlVariable.LEXICAL_SENSE_INDEX + " ;\n"
            + "      " + SparqlPrefix.LUC.getPrefix() + "query \"lexicalSenseIRI:[IRI]\" ;\n"
            + "      " + SparqlPrefix.LUC.getPrefix() + "entities ?" + SparqlVariable.SENSE + " .\n"
            // custom variable
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " " + SparqlPrefix.LEXINFO.getPrefix() + "confidence ?confidence . }\n"
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

    public static final String DATA_LEXICAL_SENSE_DIRECT_VARTRANS
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.VARTRANS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LEXICAL_ENTITY + " \n"
            + "(GROUP_CONCAT(?_type;SEPARATOR=\";\") as ?" + SparqlVariable.TYPE + ")\n"
            + "?" + SparqlVariable.LABEL + " ?" + SparqlVariable.GRAPH + "\n"
            + "FROM NAMED onto:implicit\n"
            + "FROM NAMED onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:lexicalSenseIndex ;\n"
            + "      luc:query \"lexicalSenseIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.SENSE + " .\n"
            + "    OPTIONAL { GRAPH ?" + SparqlVariable.GRAPH + " { ?" + SparqlVariable.SENSE + " ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LEXICAL_ENTITY + " } .\n"
            + "               ?" + SparqlVariable.PROPERTY_NAME + " rdfs:subPropertyOf vartrans:senseRel . \n"
            + "               OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTITY + " rdfs:label|skos:definition ?" + SparqlVariable.LABEL + " }\n"
            + "               OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTITY + " sesame:directType ?_type }\n"
            + "               FILTER(!STRSTARTS(STR(?" + SparqlVariable.PROPERTY_NAME + "), \"http://www.w3.org/ns/lemon/vartrans#senseRel\"))\n"
            + "    }\n"
            + "               FILTER(BOUND(?" + SparqlVariable.LEXICAL_ENTITY + "))\n"
            + "} \n"
            + "GROUP BY ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LEXICAL_ENTITY + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.GRAPH + "\n"
            + "ORDER BY ?" + SparqlVariable.PROPERTY_NAME;

    public static final String DATA_LEXICAL_SENSE_INDIRECT_VARTRANS
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.VARTRANS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT \n"
            + "?" + SparqlVariable.REIFIED_RELATION + " ?" + SparqlVariable.CATEGORY + " ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.SOURCE
            + " ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.TARGET_LABEL + " ?" + SparqlVariable.DEFINITION + " ?" + SparqlVariable.NOTE + " ?" + SparqlVariable.LABEL + "\n"
            + "(GROUP_CONCAT(concat(str(?reifiedRelationPredicate),\"<>\",str(?reifiedRelationObject),\"<>\",str(?graph));SEPARATOR=\"---\") AS ?extra)\n"
            + "FROM NAMED onto:implicit\n"
            + "FROM NAMED onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:lexicalSenseIndex ;\n"
            + "      luc:query \"lexicalSenseIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.SOURCE + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " vartrans:source ?" + SparqlVariable.SOURCE + " .\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " vartrans:category ?" + SparqlVariable.CATEGORY + " }\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " skos:note ?" + SparqlVariable.NOTE + " }\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "        OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " sesame:directType ?" + SparqlVariable.TYPE + " .\n"
            + "        FILTER(!STRSTARTS(STR(?" + SparqlVariable.TYPE + "), \"http://www.w3.org/ns/lemon/lexicog#\")) }\n"
            + "    	OPTIONAL { ?" + SparqlVariable.REIFIED_RELATION + " vartrans:target ?" + SparqlVariable.TARGET + " }\n"
            + "    	OPTIONAL { ?" + SparqlVariable.TARGET + " rdfs:label ?" + SparqlVariable.TARGET_LABEL + " ;\n"
            + "    					skos:definition ?" + SparqlVariable.DEFINITION + " .\n"
            + "    	}\n"
            + "    	OPTIONAL { \n"
            + "        	GRAPH ?graph { ?" + SparqlVariable.REIFIED_RELATION + " ?reifiedRelationPredicate ?reifiedRelationObject . } \n"
            + "        	FILTER(!STRSTARTS(STR(?reifiedRelationPredicate), \"http://www.w3.org/ns/lemon/vartrans#\"))\n"
            + "        	FILTER(!STRSTARTS(STR(?reifiedRelationPredicate), \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\"))\n"
            + "    	}\n"
            + "    }\n"
            + "    FILTER(BOUND(?" + SparqlVariable.REIFIED_RELATION + "))\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.REIFIED_RELATION + " ?" + SparqlVariable.CATEGORY + " ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.SOURCE
            + " ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.TARGET_LABEL + " ?" + SparqlVariable.DEFINITION + " ?" + SparqlVariable.NOTE + " ?" + SparqlVariable.LABEL + "\n"
            + "ORDER BY ?" + SparqlVariable.REIFIED_RELATION;

    public static final String DATA_ETYMOLOGY
            = SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.ETYMOLOGY
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.CONFIDENCE
            + " ?" + SparqlVariable.HYPOTHESIS_OF
            + " ?" + SparqlVariable.ETYMOLOGY_CREATION_AUTHOR
            + "\n"
            + "WHERE {\n"
            + "  ?search a " + SparqlPrefix.INST.getPrefix() + SparqlVariable.ETYMOLOGY_INDEX + " ;\n"
            + "      " + SparqlPrefix.LUC.getPrefix() + "query \"etymologyIRI:[IRI]\" ;\n"
            + "      " + SparqlPrefix.LUC.getPrefix() + "entities ?" + SparqlVariable.ETYMOLOGY + " .\n"
            + "  ?" + SparqlVariable.ETYMOLOGY + " rdfs:label ?" + SparqlVariable.LABEL + " ;\n"
            + "             dct:creator ?" + SparqlVariable.ETYMOLOGY_CREATION_AUTHOR + " ;\n"
            + "             dct:modified ?" + SparqlVariable.LAST_UPDATE + " ;\n"
            + "             dct:created ?" + SparqlVariable.CREATION_DATE + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETYMOLOGY + " lexinfo:confidence ?" + SparqlVariable.CONFIDENCE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETYMOLOGY + " rdfs:comment ?" + SparqlVariable.HYPOTHESIS_OF + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETYMOLOGY + " skos:note ?" + SparqlVariable.NOTE + " }\n"
            + "}";

    public static final String DATA_ETYMOLOGY_ETY_LINKS_LIST
            = SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.ETYMOLOGY_CREATION_AUTHOR
            + " ?" + SparqlVariable.ETY_LINK
            + " ?" + SparqlVariable.ETY_LINK_LABEL
            + " ?" + SparqlVariable.ETY_LINK_TYPE
            + " ?" + SparqlVariable.ETY_TARGET
            + " ?" + SparqlVariable.ETY_TARGET_LABEL
            + " ?" + SparqlVariable.ETY_SOURCE
            + " ?" + SparqlVariable.ETY_SOURCE_LABEL + " ?confidence"
            + "\n"
            + "WHERE {\n"
            + "  ?search a " + SparqlPrefix.INST.getPrefix() + SparqlVariable.ETYMOLOGY_INDEX + " ;\n"
            + "      " + SparqlPrefix.LUC.getPrefix() + "query \"etymologyIRI:[IRI]\" ;\n"
            + "      " + SparqlPrefix.LUC.getPrefix() + "entities ?" + SparqlVariable.ETYMOLOGY + " .\n"
            + "  ?" + SparqlVariable.ETYMOLOGY + " ety:hasEtyLink ?" + SparqlVariable.ETY_LINK + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETY_LINK + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " }\n"
            + "   OPTIONAL { ?" + SparqlVariable.ETY_LINK + " lexinfo:confidence ?confidence . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETY_LINK + " dct:created ?" + SparqlVariable.CREATION_DATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETY_LINK + " dct:creator ?" + SparqlVariable.ETYMOLOGY_CREATION_AUTHOR + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETY_LINK + " ety:etyLinkType ?" + SparqlVariable.ETY_LINK_TYPE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETY_LINK + " skos:note ?" + SparqlVariable.NOTE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETY_LINK + " rdfs:label ?" + SparqlVariable.ETY_LINK_LABEL + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETY_LINK + " ety:etyTarget ?" + SparqlVariable.ETY_TARGET + " . OPTIONAL { ?"
            + SparqlVariable.ETY_TARGET + " rdfs:label ?" + SparqlVariable.ETY_TARGET_LABEL + " } }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ETY_LINK + " ety:etySource ?" + SparqlVariable.ETY_SOURCE + " . OPTIONAL { ?"
            + SparqlVariable.ETY_SOURCE + " rdfs:label ?" + SparqlVariable.ETY_SOURCE_LABEL + " } }"
            + "}";

    public static final String DATA_LEXICAL_ENTRY_REFERENCE_LINKS
            = SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + "\n"
            + "SELECT (count(?" + SparqlVariable.SEEALSO + ") as ?" + SparqlVariable.SEEALSO + "Count) "
            // owl:sameAs reasoning disabled
            //            + "((count(?" + SparqlVariable.SAMEAS + ") - 1) as ?" + SparqlVariable.SAMEAS + "Count)\n"
            + "(count(?" + SparqlVariable.SAMEAS + ") as ?" + SparqlVariable.SAMEAS + "Count)\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " rdfs:seeAlso ?" + SparqlVariable.SEEALSO + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " owl:sameAs ?" + SparqlVariable.SAMEAS + " . }\n"
            + "}";

    public static final String DATA_LEXICAL_ENTITY_LINKS
            = SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + "\n"
            + "SELECT (count(?" + SparqlVariable.SEEALSO + ") as ?" + SparqlVariable.SEEALSO + "Count) "
            // owl:sameAs reasoning disabled -- it must be disabled from the GraphDB repo configuration
            //            + "((count(?" + SparqlVariable.SAMEAS + ") - 1) as ?" + SparqlVariable.SAMEAS + "Count)\n"
            + "(count(?" + SparqlVariable.SAMEAS + ") as ?" + SparqlVariable.SAMEAS + "Count) "
            + "(count(?" + SparqlVariable.BIBLIOGRAPHY + ") as ?" + SparqlVariable.BIBLIOGRAPHY + "Count)\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " rdfs:seeAlso ?" + SparqlVariable.SEEALSO + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " owl:sameAs ?" + SparqlVariable.SAMEAS + " . }\n"
            + " UNION \n"
            + "    { ?" + SparqlVariable.LEXICAL_ENTRY + " dct:references ?" + SparqlVariable.BIBLIOGRAPHY + " . }\n"
            + "}";

    public static final String DATA_COMPONENT = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.COMPONENT
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.NOTE
            + " ?confidence"
            + " ?" + SparqlVariable.COMPONENT_CREATION_AUTHOR
            + " ?" + SparqlVariable.COMPONENT_POSITION
            + "\n"
            + "(GROUP_CONCAT(concat(str(?morphoTrait),\"<>\",str(?morphoValue));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.COMPONENT_INDEX + " ;\n"
            + "      luc:query \"ComponentIRI:[IRI]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.COMPONENT + " .\n"
            + "    OPTIONAL {?" + SparqlVariable.COMPONENT + " rdfs:label ?" + SparqlVariable.LABEL + "} .\n"
            + "   OPTIONAL { ?" + SparqlVariable.COMPONENT + " lexinfo:confidence ?confidence . }\n"
            + "    OPTIONAL {?" + SparqlVariable.COMPONENT + " dct:created ?" + SparqlVariable.CREATION_DATE + "} .\n"
            + "    OPTIONAL {?" + SparqlVariable.COMPONENT + " dct:modified ?" + SparqlVariable.LAST_UPDATE + "} .\n"
            + "    OPTIONAL { ?" + SparqlVariable.COMPONENT + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.COMPONENT + " dct:creator ?" + SparqlVariable.COMPONENT_CREATION_AUTHOR + " . }\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " ?_position ?" + SparqlVariable.COMPONENT + " .\n"
            + "              BIND(strafter(str(?_position), str(rdf:)) as ?" + SparqlVariable.COMPONENT_POSITION + ")\n"
            + "              FILTER(STRSTARTS(STR(?_position), str(rdf:))) }\n"
            + "   OPTIONAL {?" + SparqlVariable.COMPONENT + " ?morphoTrait ?morphoValue . \n"
            + "              ?morphoTrait rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?morphoTrait), \"morphosyntacticProperty\")) }\n"
            //            + "              BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?traitType)\n"
            //            + "              BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?traitValue)\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:)))\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoValue), str(lexinfo:))) } \n"
            + "} GROUP BY ?"
            + SparqlVariable.COMPONENT + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.CREATION_DATE + " ?"
            + SparqlVariable.LAST_UPDATE + " ?"
            + SparqlVariable.COMPONENT_CREATION_AUTHOR + " ?"
            + SparqlVariable.COMPONENT_POSITION + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.NOTE + " ?confidence";

    public static final String DATA_LINGUISTIC_RELATION
            = SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + "SELECT ?graph ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.LINK + " (GROUP_CONCAT(distinct concat(str(?_type));SEPARATOR=\";\") AS ?" + SparqlVariable.TYPE + ")\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "explicit\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "implicit\n"
            + "   { \n"
            + "      GRAPH ?graph { <_ID_> ?relation ?" + SparqlVariable.TARGET + " . \n"
            + "                                             FILTER (regex(str(?relation), \"_RELATION_\")) }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " " + SparqlPrefix.RDFS.getPrefix() + "label|" + SparqlPrefix.SKOS.getPrefix() + "definition ?" + SparqlVariable.LABEL + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " " + SparqlPrefix.SESAME.getPrefix() + "directType ?_type . \n"
            + "    FILTER (!regex(str(?_type), \"http://www.w3.org/2000/01/rdf-schema#|http://www.w3.org/1999/02/22-rdf-syntax-ns#|http://www.w3.org/2002/07/owl#\")) }\n"
            + "    FILTER (!regex(str(?relation), \"http://www.ontologydesignpatterns.org/cp/owl/semiotics.owl#\"))\n"
            + "    BIND(?relation AS ?" + SparqlVariable.LINK + ")\n"
            + "   } GROUP BY ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.LABEL + " ?graph ?" + SparqlVariable.LINK + "\n"
            + "ORDER BY ?graph";

    public static final String DATA_LEXICAL_CONCEPT_RELATION
            = SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + "SELECT ?graph ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.LABEL
            + " (GROUP_CONCAT(distinct concat(str(?_type));SEPARATOR=\";\") AS ?" + SparqlVariable.TYPE + ")\n"
            + "?" + SparqlVariable.LINK + "\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "explicit\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "implicit\n"
            + "   { \n"
            + "VALUES (?" + SparqlVariable.LINK + ") { (\"_RELATION_\") }\n"
            + "      GRAPH ?graph { <_ID_> ?relation ?" + SparqlVariable.TARGET + " . \n"
            + "                                             FILTER (regex(str(?relation), ?" + SparqlVariable.LINK + ")) }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " " + SparqlPrefix.RDFS.getPrefix() + "label|" + SparqlPrefix.SKOS.getPrefix() + "definition ?" + SparqlVariable.LABEL + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " " + SparqlPrefix.RDF.getPrefix() + "type ?_type . \n"
            + "    FILTER (!regex(str(?_type), \"http://www.w3.org/2000/01/rdf-schema#|http://www.w3.org/1999/02/22-rdf-syntax-ns#|http://www.w3.org/2002/07/owl#|http://www.ontologydesignpatterns.org/cp/owl/semiotics.owl#\")) }\n"
            + "    FILTER (!regex(str(?relation), \"http://www.ontologydesignpatterns.org/cp/owl/semiotics.owl#\"))\n"
            + "   } GROUP BY ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.LABEL + " ?graph\n" + "?" + SparqlVariable.LINK + "\n"
            + "ORDER BY ?graph";

    public static final String DATA_GENERIC_RELATION
            = SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + "SELECT ?graph ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.LABEL
            + " (GROUP_CONCAT(distinct concat(str(?_type));SEPARATOR=\";\") AS ?" + SparqlVariable.TYPE + ")\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "explicit\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "implicit\n"
            + "   { \n"
            + "      GRAPH ?graph { <_ID_> ?relation ?" + SparqlVariable.TARGET + " . \n"
            + "                                             FILTER (regex(str(?relation), \"_RELATION_\")) }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " " + SparqlPrefix.RDFS.getPrefix() + "label|" + SparqlPrefix.SKOS.getPrefix() + "definition ?" + SparqlVariable.LABEL + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " " + SparqlPrefix.SESAME.getPrefix() + "directType ?_type . \n"
            + "    FILTER (!regex(str(?_type), \"http://www.w3.org/2000/01/rdf-schema#|http://www.w3.org/1999/02/22-rdf-syntax-ns#|http://www.w3.org/2002/07/owl#\")) }\n"
            + "   } GROUP BY ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.LABEL + " ?graph\n"
            + "ORDER BY ?graph";

    public static final String DATA_FORMS_BY_SENSE_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
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
            //            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
            + " ?" + SparqlVariable.SENSE
            + " ?confidence"
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " (GROUP_CONCAT(concat(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),\"<>\",str(?"
            + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ") \n"
            + "FROM NAMED onto:explicit\n"
            + "FROM NAMED onto:implicit   \n"
            + "{\n"
            + "    GRAPH ?g { [RELATION_DISTANCE_PATH] }\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " .\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.FORM_TYPE + " ?" + SparqlVariable.FORM + " ;\n"
            + "       lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " .\n"
            + "    ?" + SparqlVariable.FORM + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " ;\n"
            + "       ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ontolex:phoneticRep ?" + SparqlVariable.PHONETIC_REPRESENTATION + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " lexinfo:confidence ?confidence . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "   OPTIONAL { GRAPH ?g { ?" + SparqlVariable.FORM + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + " . }  \n"
            + "              ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), \"morphosyntacticProperty\")) }\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:))) }\n"
            + "   ?" + SparqlVariable.FORM_TYPE + " sesame:directSubPropertyOf ontolex:lexicalForm .\n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.FORM + "), str(lex:)) as ?" + SparqlVariable.FORM_INSTANCE_NAME + ")\n"
            //            + "   BIND(strafter(str(?posTag),str(lexinfo:)) as ?" + SparqlVariable.LEXICAL_ENTRY_POS + ")\n"
            //            + "   BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + ")\n"
            //            + "   BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + ")\n"
            + "   FILTER(regex(str(?g), \"explicit\")) \n"
            + "} GROUP BY ?"
            + SparqlVariable.FORM
            //            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.SENSE
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?confidence"
            + " ?" + SparqlVariable.NOTE + "\n"
            + "  ORDER BY ?"
            + SparqlVariable.WRITTEN_REPRESENTATION;
//            + " ?" + SparqlVariable.FORM_INSTANCE_NAME;

    public static final String DATA_SENSE_BY_CONCEPT
            = SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLOGY.getSparqlPrefix() + "\n"
            + "\n"
            + "SELECT ?" + SparqlVariable.WRITTEN_REPRESENTATION + " ?"
            + SparqlVariable.LEXICAL_ENTRY + " ?"
            + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.SENSE_DEFINITION + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "    ?" + SparqlVariable.SENSE + " ontolex:reference ontology:_CONCEPT_ ;\n"
            + "           skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " .\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " ;\n"
            + "        lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " ;\n"
            + "        ontolex:canonicalForm ?" + SparqlVariable.FORM + " . \n"
            + "    ?" + SparqlVariable.FORM + " ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "} ORDER BY ?" + SparqlVariable.WRITTEN_REPRESENTATION;

    public static final String DATA_FORMS_BY_LEXICAL_ENTRY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
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
            + " ?confidence"
            //            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
            + " (GROUP_CONCAT(concat(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),\"<>\",str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "             luc:query \"lexicalEntryIRI:[IRI]\" ;\n"
            + "             luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "   ?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.FORM_TYPE + " ?" + SparqlVariable.FORM + " .\n"
            + "   OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " }\n"
            + "   ?" + SparqlVariable.FORM + " ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ontolex:phoneticRep ?" + SparqlVariable.PHONETIC_REPRESENTATION + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " lexinfo:confidence ?confidence . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " dct:created ?" + SparqlVariable.CREATION_DATE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + " .\n"
            + "              ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), \"morphosyntacticProperty\")) }\n"
            //            + "              FILTER(STRSTARTS(STR(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), str(lexinfo:))) }\n"
            + "   ?" + SparqlVariable.FORM_TYPE + " rdfs:subPropertyOf* ontolex:lexicalForm .\n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.FORM + "), str(lex:)) as ?" + SparqlVariable.FORM_INSTANCE_NAME + ")\n"
            //            + "   BIND(strafter(str(?posTag), str(lexinfo:)) as ?" + SparqlVariable.LEXICAL_ENTRY_POS + ")\n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),str(lexinfo:)) as ?tn)\n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "),str(lexinfo:)) as ?tv)\n"
            + "   [FORM_CONSTRAINT]"
            + "} GROUP BY ?"
            + SparqlVariable.FORM
            //            + " ?" + SparqlVariable.FORM_INSTANCE_NAME
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?confidence"
            + " ?" + SparqlVariable.NOTE + "\n"
            + "  ORDER BY ?"
            + SparqlVariable.WRITTEN_REPRESENTATION;
//            + " ?" + SparqlVariable.FORM_INSTANCE_NAME;

    public static final String LEXICON_ENTRY_LANGUAGE
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.LABEL + "\n"
            + "where { <_ID_> " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " .\n"
            + "}";

    public static final String LEXICON_ENTRY_STATUS
            = SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.LABEL + "\n"
            + "where { <_ID_> " + SparqlPrefix.VS.getPrefix() + "term_status ?" + SparqlVariable.LABEL + " .\n"
            + "}";

    public static final String LEXICAL_ENTITY_BIBLIOGRAPHY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.BIBLIOGRAPHY
            + " ?" + SparqlVariable.BIBLIOGRAPHY_AUTHOR
            + " ?" + SparqlVariable.BIBLIOGRAPHY_DATE
            + " ?" + SparqlVariable.BIBLIOGRAPHY_ID
            + " ?" + SparqlVariable.BIBLIOGRAPHY_NOTE
            + " ?" + SparqlVariable.BIBLIOGRAPHY_SEE_ALSO_LINK
            + " ?" + SparqlVariable.BIBLIOGRAPHY_TEXTUAL_REF
            + " ?" + SparqlVariable.BIBLIOGRAPHY_TITLE
            + " ?" + SparqlVariable.BIBLIOGRAPHY_URL
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.BIBLIOGRAPHY_CREATOR
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.CONFIDENCE
            + "WHERE {\n"
            + "    <_ID_> dct:references ?" + SparqlVariable.BIBLIOGRAPHY + " .\n"
            + "   ?" + SparqlVariable.BIBLIOGRAPHY + " dct:publisher ?" + SparqlVariable.BIBLIOGRAPHY_ID + " .\n"
            + "   OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " rdfs:seeAlso ?" + SparqlVariable.BIBLIOGRAPHY_SEE_ALSO_LINK + " } .\n"
            + "   OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " dct:identifier ?" + SparqlVariable.BIBLIOGRAPHY_URL + " } .\n"
            + "   OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " dct:title ?" + SparqlVariable.BIBLIOGRAPHY_TITLE + " } .\n"
            + "   OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " rdfs:label ?" + SparqlVariable.BIBLIOGRAPHY_TEXTUAL_REF + " } .\n"
            + "   OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " dct:contributor ?" + SparqlVariable.BIBLIOGRAPHY_AUTHOR + " } .\n"
            + "   OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " dct:date ?" + SparqlVariable.BIBLIOGRAPHY_DATE + " } .\n"
            + "   OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " skos:note ?" + SparqlVariable.BIBLIOGRAPHY_NOTE + " } .\n"
            + "   OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " dct:created ?" + SparqlVariable.CREATION_DATE + " } . \n"
            + "   OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " dct:creator ?" + SparqlVariable.BIBLIOGRAPHY_CREATOR + " } . \n"
            + "   OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " } . \n"
            + "   OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " lexinfo:confidence ?" + SparqlVariable.CONFIDENCE + " } . \n"
            + "}";

    public static final String LEXICAL_ENTITIES_BY_BIBLIOGRAPHY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTITY + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.BIBLIOGRAPHY_CREATOR + " ?" + SparqlVariable.BIBLIOGRAPHY_DATE + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.CONFIDENCE + "\n"
            + "(GROUP_CONCAT(str(?_type);SEPARATOR=\";\") AS ?" + SparqlVariable.TYPE + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE { ?" + SparqlVariable.LEXICAL_ENTITY + " dct:references ?" + SparqlVariable.BIBLIOGRAPHY + " ;\n"
            + "                       rdf:type ?_type ;\n"
            + "                       rdfs:label|ontolex:writtenRep|skos:definition ?" + SparqlVariable.LABEL + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + "  dct:publisher ?" + SparqlVariable.BIBLIOGRAPHY_ID + "  } .\n"
            + "    OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + "  dct:creator ?" + SparqlVariable.BIBLIOGRAPHY_CREATOR + "  } .\n"
            + "    OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + "  dct:created ?" + SparqlVariable.BIBLIOGRAPHY_DATE + "  } .\n"
            + "    OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + "  dct:modified ?" + SparqlVariable.LAST_UPDATE + "  } .\n"
            + "    OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + "  lexinfo:confidence ?" + SparqlVariable.CONFIDENCE + "  } .\n"
            + "    FILTER(!STRSTARTS(str(?_type), str(owl:)))\n"
            + "    FILTER(regex(str(?" + SparqlVariable.BIBLIOGRAPHY_ID + " ), \"_ID_\"))\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.LEXICAL_ENTITY + "  ?" + SparqlVariable.LABEL
            + "  ?" + SparqlVariable.BIBLIOGRAPHY_CREATOR + "  ?" + SparqlVariable.BIBLIOGRAPHY_DATE
            + "  ?" + SparqlVariable.LAST_UPDATE + "  ?" + SparqlVariable.CONFIDENCE + " ";

    public static final String BIBLIOGRAPHY_LIST
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.BIBLIOGRAPHY_TITLE + " ?" + SparqlVariable.BIBLIOGRAPHY_AUTHOR
            + " ?" + SparqlVariable.BIBLIOGRAPHY_DATE + " ?" + SparqlVariable.BIBLIOGRAPHY_ID
            + " (count(distinct ?" + SparqlVariable.BIBLIOGRAPHY + ") as ?" + SparqlVariable.LABEL_COUNT + ")\n"
            + "WHERE { ?lexicalEntity dct:references ?" + SparqlVariable.BIBLIOGRAPHY + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " dct:title ?" + SparqlVariable.BIBLIOGRAPHY_TITLE + "}\n"
            + "    OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " dct:date ?" + SparqlVariable.BIBLIOGRAPHY_DATE + "}\n"
            + "    OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " dct:contributor ?" + SparqlVariable.BIBLIOGRAPHY_AUTHOR + "}\n"
            + "    OPTIONAL { ?" + SparqlVariable.BIBLIOGRAPHY + " dct:publisher ?" + SparqlVariable.BIBLIOGRAPHY_ID + "}\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.BIBLIOGRAPHY_TITLE + " ?" + SparqlVariable.BIBLIOGRAPHY_AUTHOR
            + " ?" + SparqlVariable.BIBLIOGRAPHY_DATE + " ?" + SparqlVariable.BIBLIOGRAPHY_ID + "\n"
            + "ORDER BY ?" + SparqlVariable.BIBLIOGRAPHY_TITLE;

    public static final String DATA_SUBTERMS
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
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
            + SparqlPrefix.DECOMP.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY
            //            + " ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME
            + " ?" + SparqlVariable.LEXICAL_ENTRY_STATUS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR
            + " ?" + SparqlVariable.REVISION_DATE
            + " ?" + SparqlVariable.COMPLETION_DATE + " ?confidence"
            + "\n"
            + "(GROUP_CONCAT(distinct concat(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),\"<>\",str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "(GROUP_CONCAT(distinct str(?tmp_type);SEPARATOR=\";\") AS ?type)\n"
            + "FROM onto:explicit WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:orderBy \"lexicalEntryLabel\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?_le .\n"
            + "  ?_le decomp:subterm ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "  ?" + SparqlVariable.LEXICAL_ENTRY + " rdf:type ?tmp_type ;\n"
            + "          rdfs:label ?" + SparqlVariable.LABEL + " .\n"
            + "   ?tmp_type rdfs:label ?_type .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:created ?" + SparqlVariable.CREATION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:modified ?" + SparqlVariable.LAST_UPDATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:confidence ?confidence} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:author ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateAccepted ?" + SparqlVariable.REVISION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateSubmitted ?" + SparqlVariable.COMPLETION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " skos:note ?" + SparqlVariable.NOTE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " loc:rev ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " vs:term_status ?" + SparqlVariable.LEXICAL_ENTRY_STATUS + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + " . \n"
            + "              ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), \"morphosyntacticProperty\")) }\n"
            //            + "              BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?traitType)\n"
            //            + "              BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?traitValue)\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:)))\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoValue), str(lexinfo:))) } \n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.LEXICAL_ENTRY + "),str(lex:)) as ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME + ") \n"
            + "   FILTER(!STRSTARTS(STR(?tmp_type), str(owl:)))\n"
            + "   FILTER(regex(str(?_type), \"_TYPE_\"))"
            + "} GROUP BY ?"
            + SparqlVariable.LEXICAL_ENTRY + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.LEXICAL_ENTRY_REVISOR + " ?"
            + SparqlVariable.LEXICAL_ENTRY_POS + " ?"
            + SparqlVariable.LEXICAL_ENTRY_STATUS + " ?"
            + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.NOTE + " ?"
            + SparqlVariable.CREATION_DATE + " ?"
            + SparqlVariable.LAST_UPDATE + " ?"
            + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR + " ?"
            + SparqlVariable.REVISION_DATE + " ?"
            + SparqlVariable.COMPLETION_DATE
            //            + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME 
            + " ?confidence";

    public static final String DATA_CORRESPONDS_TO
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.DECOMP.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            //            + " ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME
            + " ?" + SparqlVariable.LEXICAL_ENTRY_STATUS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR
            + " ?" + SparqlVariable.REVISION_DATE
            + " ?" + SparqlVariable.COMPLETION_DATE + " ?confidence"
            + "\n"
            + "(GROUP_CONCAT(distinct concat(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),\"<>\",str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "(GROUP_CONCAT(distinct str(?tmp_type);SEPARATOR=\";\") AS ?type)\n"
            + "FROM onto:explicit WHERE {\n"
            + "  ?search a inst:" + SparqlVariable.COMPONENT_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?totalHits ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?_le .\n"
            + "  ?_le decomp:correspondsTo ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "  ?" + SparqlVariable.LEXICAL_ENTRY + " rdf:type ?tmp_type ;\n"
            + "          rdfs:label ?" + SparqlVariable.LABEL + " .\n"
            + "   ?tmp_type rdfs:label ?_type .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:created ?" + SparqlVariable.CREATION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:modified ?" + SparqlVariable.LAST_UPDATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " lexinfo:confidence ?confidence} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:author ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateAccepted ?" + SparqlVariable.REVISION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateSubmitted ?" + SparqlVariable.COMPLETION_DATE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " skos:note ?" + SparqlVariable.NOTE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " loc:rev ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " vs:term_status ?" + SparqlVariable.LEXICAL_ENTRY_STATUS + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + " . \n"
            + "              ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), \"morphosyntacticProperty\")) }\n"
            //            + "              BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?traitType)\n"
            //            + "              BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?traitValue)\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:)))\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoValue), str(lexinfo:))) } \n"
            //            + "   BIND(strafter(str(?" + SparqlVariable.LEXICAL_ENTRY + "),str(lex:)) as ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME + ") \n"
            + "   FILTER(!STRSTARTS(STR(?tmp_type), str(owl:)))\n"
            + "   FILTER(regex(str(?_type), \"_TYPE_\"))"
            + "} GROUP BY ?"
            + SparqlVariable.LEXICAL_ENTRY + " ?"
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
            //            + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME 
            + " ?confidence";

    public static final String DATA_COMPONENTS
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
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
            + SparqlPrefix.DECOMP.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.COMPONENT
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.COMPONENT_CREATION_AUTHOR
            + " ?" + SparqlVariable.NOTE
            + " ?confidence"
            + " ?" + SparqlVariable.COMPONENT_POSITION
            + " ?" + SparqlVariable.MORPHOLOGY
            + "\n"
            + "(GROUP_CONCAT(distinct concat(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "),\"<>\",str(?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + "));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY + ")\n"
            + "(GROUP_CONCAT(distinct str(?tmp_type);SEPARATOR=\";\") AS ?type)\n"
            + "FROM onto:explicit WHERE {\n"
            + "  ?search a inst:[INDEX_NAME] ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?totalHits ;\n"
            + "      luc:orderBy \"[ORDER]\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?entity .\n"
            + "  ?entity decomp:constituent ?" + SparqlVariable.COMPONENT + " .\n"
            + "   OPTIONAL {?" + SparqlVariable.COMPONENT + " rdfs:label ?" + SparqlVariable.LABEL + "} .\n"
            + "   OPTIONAL { ?" + SparqlVariable.COMPONENT + " lexinfo:confidence ?confidence . }\n"
            + "   OPTIONAL {?" + SparqlVariable.COMPONENT + " dct:creator ?" + SparqlVariable.COMPONENT_CREATION_AUTHOR + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.COMPONENT + " skos:note ?" + SparqlVariable.NOTE + "} .\n"
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " ?_position ?" + SparqlVariable.COMPONENT + " .\n"
            + "              BIND(strafter(str(?_position), str(rdf:)) as ?" + SparqlVariable.COMPONENT_POSITION + ")\n"
            + "              FILTER(STRSTARTS(STR(?_position), str(rdf:))) }\n"
            + "   OPTIONAL {?" + SparqlVariable.COMPONENT + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + " . \n"
            + "              ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " rdfs:subPropertyOf lexinfo:morphosyntacticProperty . "
            + "              FILTER(!regex(str(?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "), \"morphosyntacticProperty\")) }\n"
            //            + "              BIND(strafter(str(?morphoTrait),str(lexinfo:)) as ?traitType)\n"
            //            + "              BIND(strafter(str(?morphoValue),str(lexinfo:)) as ?traitValue)\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoTrait), str(lexinfo:)))\n"
            //            + "              FILTER(STRSTARTS(STR(?morphoValue), str(lexinfo:))) } \n"
            + "} GROUP BY ?"
            + SparqlVariable.COMPONENT + " ?"
            + SparqlVariable.LABEL + " ?"
            + SparqlVariable.COMPONENT_CREATION_AUTHOR + " ?"
            + SparqlVariable.NOTE + " ?"
            + SparqlVariable.COMPONENT_POSITION + " ?"
            + SparqlVariable.TOTAL_HITS + " ?confidence";

    public static final String DATA_LEXICAL_CONCEPTS_CHILDREN
            = SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + "SELECT DISTINCT\n"
            + "?" + SparqlVariable.CHILD + " (count(?" + SparqlVariable.GCHILD + ") as ?" + SparqlVariable.NGCHILD + ") \n"
            + "?" + SparqlVariable.LABEL + "\n"
            + "?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR + " ?" + SparqlVariable.LAST_UPDATE + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.CONFIDENCE + "\n"
            + "WHERE {\n"
            + "      ?search a inst:lexicalConceptIndex ;\n"
            + "      luc:query \"lexicalConceptIRI:\\\"_LEXICALCONCEPT_\\\"\" ;\n"
            + "      luc:offset \"0\" ;\n"
            + "      luc:limit \"200\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_CONCEPT + " .\n"
            + "    ?child skos:narrower ?" + SparqlVariable.LEXICAL_CONCEPT + " .\n"
            + "  OPTIONAL { ?child <_LABELPROPERTY_> ?" + SparqlVariable.LABEL + " .\n"
            + "    FILTER (lang(?" + SparqlVariable.LABEL + ") = \"_DEFAULTLANGUAGE_\") }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:creator ?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:created ?" + SparqlVariable.CREATION_DATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " lexinfo:confidence ?" + SparqlVariable.CONFIDENCE + " }\n"
            + "  OPTIONAL { \n"
            + "    ?" + SparqlVariable.GCHILD + " skos:narrower ?" + SparqlVariable.CHILD + " .\n"
            + "  }\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.CHILD + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR
            + " ?" + SparqlVariable.LAST_UPDATE + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.CONFIDENCE + "\n"
            + "ORDER BY ?" + SparqlVariable.LABEL + "";

    public static final String DATA_LEXICAL_CONCEPTS_ROOT
            = SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + "SELECT \n"
            + "?" + SparqlVariable.CHILD + " (count(?" + SparqlVariable.GCHILD + ") as ?" + SparqlVariable.NGCHILD + ") \n"
            + "?" + SparqlVariable.LABEL + "\n"
            + "?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.CONFIDENCE + "\n"
            + "WHERE {\n"
            + "    ?search a inst:lexicalConceptIndex ;\n"
            + "      luc:query \"label:*\" ;\n"
            + "      luc:offset \"0\" ;\n"
            + "      luc:limit \"200\" ;\n"
            + "      luc:entities ?" + SparqlVariable.CHILD + " .\n"
            + "  OPTIONAL { ?" + SparqlVariable.CHILD + " <_LABELPROPERTY_> ?" + SparqlVariable.LABEL + " .\n"
            + "    FILTER (lang(?" + SparqlVariable.LABEL + ") = \"_DEFAULTLANGUAGE_\") }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:creator ?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:created ?" + SparqlVariable.CREATION_DATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " lexinfo:confidence ?" + SparqlVariable.CONFIDENCE + " }\n"
            + "  OPTIONAL { \n"
            + "    ?" + SparqlVariable.GCHILD + " skos:narrower ?" + SparqlVariable.CHILD + "\n"
            + "  }\n"
            + "    FILTER NOT EXISTS { ?" + SparqlVariable.CHILD + " skos:narrower ?parent . } \n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.CHILD + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR
            + " ?" + SparqlVariable.LAST_UPDATE + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.CONFIDENCE + "\n"
            + "ORDER BY ?" + SparqlVariable.LABEL + "";

    public static final String DATA_TOP_LEXICAL_CONCEPT_OF_A_CONCEPT_SET
            = SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + "SELECT \n"
            + "?" + SparqlVariable.CHILD + " (count(?" + SparqlVariable.GCHILD + ") as ?" + SparqlVariable.NGCHILD + ") \n"
            + "?" + SparqlVariable.LABEL + "\n"
            + "?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR + " ?" + SparqlVariable.LAST_UPDATE + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.CONFIDENCE + "\n"
            + "WHERE {\n"
            + "    ?search a inst:lexicalConceptIndex ;\n"
            + "      luc:query \"inScheme:\\\"_LEXICALCONCEPT_\\\"\";\n"
            + "      luc:offset \"0\" ;\n"
            + "      luc:limit \"200\" ;\n"
            + "      luc:entities ?" + SparqlVariable.CHILD + " .\n"
            + "  OPTIONAL { ?" + SparqlVariable.CHILD + " <_LABELPROPERTY_> ?" + SparqlVariable.LABEL + " .\n"
            + "    FILTER (lang(?" + SparqlVariable.LABEL + ") = \"_DEFAULTLANGUAGE_\") }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:creator ?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:created ?" + SparqlVariable.CREATION_DATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " lexinfo:confidence ?" + SparqlVariable.CONFIDENCE + " }\n"
            + "  OPTIONAL { \n"
            + "    ?" + SparqlVariable.GCHILD + " skos:narrower ?" + SparqlVariable.CHILD + "\n"
            + "  }\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.CHILD + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR
            + " ?" + SparqlVariable.LAST_UPDATE + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.CONFIDENCE + "\n"
            + "ORDER BY ?" + SparqlVariable.LABEL + "";

    public static final String DATA_CONCEPT_SETS
            = SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "SELECT\n"
            + "?" + SparqlVariable.ROOT + " (count(?" + SparqlVariable.GCHILD + ") as ?" + SparqlVariable.NGCHILD + ")\n"
            + "?" + SparqlVariable.LABEL + "\n"
            + "?" + SparqlVariable.CONCEPT_SET_CREATOR + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.CONFIDENCE + "\n"
            + "WHERE {\n"
            + "?search a inst:conceptSetIndex ;\n"
            + "      luc:query \"label:*\" ;\n"
            + "      luc:offset \"0\" ;\n"
            + "      luc:limit \"200\" ;\n"
            + "      luc:entities ?" + SparqlVariable.ROOT + " .\n"
            + "  ?" + SparqlVariable.ROOT + " a ontolex:ConceptSet .\n"
            + "  OPTIONAL { ?" + SparqlVariable.ROOT + " <_LABELPROPERTY_> ?" + SparqlVariable.LABEL + " .\n"
            + "    FILTER (lang(?" + SparqlVariable.LABEL + ") = \"_DEFAULTLANGUAGE_\") }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ROOT + " dct:creator ?" + SparqlVariable.CONCEPT_SET_CREATOR + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ROOT + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ROOT + " dct:created ?" + SparqlVariable.CREATION_DATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.ROOT + " lexinfo:confidence ?" + SparqlVariable.CONFIDENCE + " }\n"
            + "  OPTIONAL { \n"
            + "    ?" + SparqlVariable.CHILD + " skos:inScheme ?" + SparqlVariable.ROOT + " .\n"
            + "    ?" + SparqlVariable.GCHILD + " skos:narrower+ ?" + SparqlVariable.CHILD + " .\n"
            + "  }\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.ROOT + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.CONCEPT_SET_CREATOR + " ?" + SparqlVariable.LAST_UPDATE + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.CONFIDENCE + " \n"
            + "ORDER BY ?" + SparqlVariable.LABEL + "";

    public static final String DATA_LEXICAL_CONCEPT
            = SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + "SELECT\n"
            + "?" + SparqlVariable.LEXICAL_CONCEPT + "\n"
            + " ?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR
            + " ?" + SparqlVariable.DEFINITION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.CONCEPT_SCHEME
            + " ?" + SparqlVariable.LAST_UPDATE + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.CONFIDENCE + "\n"
            + "(group_concat(distinct(concat(?_prefLabel,\"@\",lang(?_prefLabel)));separator=\";\") as ?" + SparqlVariable.PREF_LABEL + ")\n"
            + "(group_concat(distinct(concat(?_altLabel,\"@\",lang(?_altLabel)));separator=\";\") as ?" + SparqlVariable.ALT_LABEL + ")\n"
            + "(group_concat(distinct(concat(?_hiddenLabel,\"@\",lang(?_hiddenLabel)));separator=\";\") as ?" + SparqlVariable.HIDDEN_LABEL + ")\n"
            //            + "(group_concat(distinct(concat(?_definition,\"@\",lang(?_definition), \"-:-\"))) as ?" + SparqlVariable.DEFINITION + ")\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "      ?search a inst:lexicalConceptIndex ;\n"
            + "      luc:query \"lexicalConceptIRI:\\\"_ID_\\\"\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_CONCEPT + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " skos:inScheme ?" + SparqlVariable.CONCEPT_SCHEME + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " skos:prefLabel ?_prefLabel }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " skos:altLabel ?_altLabel }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " skos:hiddenLabel ?_hiddenLabel }\n"
            //            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " skos:definition ?_definition }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " dct:creator ?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " dct:created ?" + SparqlVariable.CREATION_DATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " lexinfo:confidence ?" + SparqlVariable.CONFIDENCE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " skos:note ?" + SparqlVariable.NOTE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " skos:definition ?" + SparqlVariable.DEFINITION + " }\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.LEXICAL_CONCEPT + " ?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR
            + " ?" + SparqlVariable.LAST_UPDATE + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.CONFIDENCE
            + " ?" + SparqlVariable.NOTE + " ?" + SparqlVariable.DEFINITION
            + " ?" + SparqlVariable.CONCEPT_SCHEME;

    public static final String DATA_LEXICAL_CONCEPTS_FILTER
            = SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + "SELECT \n"
            + "?" + SparqlVariable.CHILD + " (count(?" + SparqlVariable.GCHILD + ") as ?" + SparqlVariable.NGCHILD + ") \n"
            + "?" + SparqlVariable.LABEL + "\n"
            + "?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.CONFIDENCE
            + " ?" + SparqlVariable.TOTAL_HITS + "\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_CONCEPT_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?" + SparqlVariable.TOTAL_HITS + " ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.CHILD + " .\n"
            + "  OPTIONAL { ?" + SparqlVariable.CHILD + " <_LABELPROPERTY_> ?" + SparqlVariable.LABEL + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:creator ?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " dct:created ?" + SparqlVariable.CREATION_DATE + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.CHILD + " lexinfo:confidence ?" + SparqlVariable.CONFIDENCE + " }\n"
            + "  OPTIONAL { \n"
            + "    ?" + SparqlVariable.GCHILD + " skos:narrower ?" + SparqlVariable.CHILD + "\n"
            + "  }\n"
            + "}\n"
            + "GROUP BY ?" + SparqlVariable.CHILD + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.LEXICAL_CONCEPT_CREATOR
            + " ?" + SparqlVariable.LAST_UPDATE + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.CONFIDENCE + " ?" + SparqlVariable.TOTAL_HITS + "\n"
            + "ORDER BY ?" + SparqlVariable.LABEL + "";

    public static final String DATA_IMAGES
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLOGY.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.IMAGE
            + " ?" + SparqlVariable.TITLE
            + " ?" + SparqlVariable.DESCRIPTION
            + " ?" + SparqlVariable.PUBLISHER
            + " ?" + SparqlVariable.FORMAT
            + " ?" + SparqlVariable.TYPE
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.IDENTIFIER
            + " ?" + SparqlVariable.RIGHTS
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.SOURCE
            + " ?" + SparqlVariable.IMAGE_CREATOR
            + " ?confidence" + "\n"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "    <_LEID_> foaf:depiction ?" + SparqlVariable.IMAGE + " .\n"
            + "    BIND (<_LEID_> AS ?" + SparqlVariable.LEXICAL_ENTITY + ") .\n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " dct:title ?" + SparqlVariable.TITLE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " dct:description ?" + SparqlVariable.DESCRIPTION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " dct:publisher ?" + SparqlVariable.PUBLISHER + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " lexinfo:confidence ?confidence . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " dct:format ?" + SparqlVariable.FORMAT + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " dct:type ?" + SparqlVariable.TYPE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " dct:identifier ?" + SparqlVariable.IDENTIFIER + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " dct:rights ?" + SparqlVariable.RIGHTS + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " dct:created ?" + SparqlVariable.CREATION_DATE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " dct:source ?" + SparqlVariable.SOURCE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.IMAGE + " dct:creator ?" + SparqlVariable.IMAGE_CREATOR + " . } \n"
            + "} ORDER BY ?" + SparqlVariable.IMAGE + "\n"
            + " ";
}
