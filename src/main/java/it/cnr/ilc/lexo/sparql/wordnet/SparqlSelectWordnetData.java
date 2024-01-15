/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.sparql.wordnet;

import it.cnr.ilc.lexo.sparql.*;

/**
 *
 * @author andreabellandi
 */
public class SparqlSelectWordnetData {

    public static final String DATA_LEXICAL_ENTRIES
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.WORDNET.getSparqlPrefix() + "\n"
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
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " wn:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + "} .\n"
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
            + SparqlPrefix.WORDNET.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.FORM
            + " ?" + SparqlVariable.FORM_CREATION_AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.NOTE
            + " ?confidence"
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
            + "   OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " wn:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " } .\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " ontolex:phoneticRep ?" + SparqlVariable.PHONETIC_REPRESENTATION + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " lexinfo:confidence ?confidence . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " dct:created ?" + SparqlVariable.CREATION_DATE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " dct:creator ?" + SparqlVariable.FORM_CREATION_AUTHOR + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . }\n"
            + "   OPTIONAL { ?" + SparqlVariable.FORM + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "   VALUES (?formType) { (ontolex:lexicalForm) (ontolex:canonicalForm) (ontolex:otherForm) } .\n"
            + "} GROUP BY ?"
            + SparqlVariable.FORM
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.FORM_CREATION_AUTHOR
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
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
            + SparqlPrefix.WORDNET.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.SENSE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LEXICAL_CONCEPT
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:orderBy \"lexicalEntryLabel\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . \n"
            + " }\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " rdfs:label ?" + SparqlVariable.WRITTEN_REPRESENTATION + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " wn:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " dct:created ?" + SparqlVariable.CREATION_DATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.SENSE + " ontolex:isLexicalizedSenseOf ?" + SparqlVariable.LEXICAL_CONCEPT + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " . ?" + SparqlVariable.LEXICAL_CONCEPT + " ontolex:lexicalizedSense ?" + SparqlVariable.SENSE + " . }\n"
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
            + SparqlPrefix.WORDNET.getSparqlPrefix() + "\n"
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
            + " ?" + SparqlVariable.LEXICAL_CONCEPT
            + " ?confidence"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_SENSE_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:totalHits ?" + SparqlVariable.TOTAL_HITS + " ;\n"
            + "      luc:orderBy \"definition\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.SENSE + " .\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:canonicalForm [ ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " ] . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " wn:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:creator ?" + SparqlVariable.SENSE_CREATION_AUTHOR + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:confidence ?confidence . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:created ?" + SparqlVariable.CREATION_DATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " ontolex:isLexicalizedSenseOf ?" + SparqlVariable.LEXICAL_CONCEPT + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " ontolex:lexicalizedSense ?" + SparqlVariable.SENSE + " . }\n"
            + "} ORDER BY ?" + SparqlVariable.LEXICAL_ENTRY + "\n";

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
            + SparqlPrefix.WORDNET.getSparqlPrefix() + "\n"
            + "SELECT"
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.SENSE
            + " ?" + SparqlVariable.SENSE_CREATION_AUTHOR
            + " ?" + SparqlVariable.SENSE_DEFINITION
            + " ?" + SparqlVariable.NOTE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.LEXICAL_CONCEPT
            + " ?confidence"
            + "FROM onto:explicit\n"
            + "WHERE {\n"
            + "   ?search a inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " ;\n"
            + "      luc:query \"[FILTER]\" ;\n"
            + "      luc:offset \"[OFFSET]\" ;\n"
            + "      luc:limit \"[LIMIT]\" ;\n"
            + "      luc:entities ?" + SparqlVariable.LEXICAL_ENTRY + " .\n"
            + "    ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:sense ?" + SparqlVariable.SENSE + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " ontolex:canonicalForm [ ontolex:writtenRep ?" + SparqlVariable.WRITTEN_REPRESENTATION + " ] . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " wn:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:creator ?" + SparqlVariable.SENSE_CREATION_AUTHOR + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " lexinfo:confidence ?confidence . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:modified ?" + SparqlVariable.LAST_UPDATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " dct:created ?" + SparqlVariable.CREATION_DATE + " . } \n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " ontolex:isLexicalizedSenseOf ?" + SparqlVariable.LEXICAL_CONCEPT + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_CONCEPT + " ontolex:lexicalizedSense ?" + SparqlVariable.SENSE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.SENSE + " ontolex:usage ?" + SparqlVariable.CONCEPT + " . }\n"
            + "} ORDER BY ?" + SparqlVariable.LEXICAL_ENTRY;

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
            + SparqlPrefix.WORDNET.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LEXICAL_ENTRY_TYPE
            + " ?" + SparqlVariable.LABEL
            + " ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.LEXICAL_ENTRY_STATUS
            + " ?confidence"
            + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR
            + " ?" + SparqlVariable.COMPLETION_DATE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.REVISION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?" + SparqlVariable.NOTE
            + "\n"
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
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateSubmitted ?" + SparqlVariable.COMPLETION_DATE + "} .\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:dateAccepted ?" + SparqlVariable.REVISION_DATE + "} .\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTRY + " dct:modified ?" + SparqlVariable.LAST_UPDATE + "} .\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " wn:partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " vs:term_status ?" + SparqlVariable.LEXICAL_ENTRY_STATUS + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " dct:author ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR + " . }\n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " loc:rev ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR + " . }\n"
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
            + " ?" + SparqlVariable.COMPLETION_DATE
            + " ?" + SparqlVariable.CREATION_DATE
            + " ?" + SparqlVariable.REVISION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE
            + " ?confidence";

    public static final String DATA_FORM_CORE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + SparqlPrefix.WORDNET.getSparqlPrefix() + "\n"
            + "SELECT ?"
            + SparqlVariable.TOTAL_HITS
            + " ?" + SparqlVariable.FORM
            + " ?" + SparqlVariable.FORM_TYPE
            + " ?" + SparqlVariable.WRITTEN_REPRESENTATION
            + " ?" + SparqlVariable.LEXICAL_ENTRY_POS
            + " ?" + SparqlVariable.PHONETIC_REPRESENTATION
            + " ?" + SparqlVariable.TRANSLITERATION
            + " ?" + SparqlVariable.SEGMENTATION
            + " ?" + SparqlVariable.PRONUNCIATION
            + " ?" + SparqlVariable.ROMANIZATION
            + " ?" + SparqlVariable.LEXICAL_ENTRY
            + " ?" + SparqlVariable.LABEL
            + " ?confidence"
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
            + "    OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY + " " + SparqlPrefix.WORDNET.getPrefix() + "partOfSpeech ?" + SparqlVariable.LEXICAL_ENTRY_POS + " . }\n"
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

    // ----> QUI
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
            + SparqlPrefix.WORDNET.getSparqlPrefix() + "\n"
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
            + "               OPTIONAL { "
            + "                               GRAPH ?_graph { ?" + SparqlVariable.LEXICAL_ENTITY + " skos:definition ?" + SparqlVariable.LABEL + " }\n"
            + "                               FILTER(STRSTARTS(STR(?_graph), \"http://www.ontotext.com/explicit\")) "
            + "                        }\n"
            + "               OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTITY + " sesame:directType ?_type .\n"
            + "                          FILTER(!STRSTARTS(STR(?_type), \"node\")) }\n"
            + "               FILTER(!STRSTARTS(STR(?" + SparqlVariable.PROPERTY_NAME + "), \"http://www.w3.org/ns/lemon/vartrans#senseRel\"))\n"
            + "    }\n"
            + "               FILTER(BOUND(?" + SparqlVariable.LEXICAL_ENTITY + "))\n"
            + "} \n"
            + "GROUP BY ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LEXICAL_ENTITY + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.GRAPH + "\n"
            + "ORDER BY ?" + SparqlVariable.PROPERTY_NAME;

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

    public static final String NEW___DATA_LINGUISTIC_RELATION
            = SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + "SELECT ?graph ?" + SparqlVariable.TARGET + " ?" + SparqlVariable.LINK + " \n"
            + "(GROUP_CONCAT(distinct concat(str(?_type));SEPARATOR=\";\") AS ?" + SparqlVariable.TYPE + ")\n"
            + "(GROUP_CONCAT(distinct concat(str(?property),\"-:-\",str(?_label), \"@\", lang(?_label));SEPARATOR=\"-;-\") AS ?" + SparqlVariable.LABEL + ")\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "explicit\n"
            + "FROM NAMED " + SparqlPrefix.ONTO.getPrefix() + "implicit\n"
            + "   { \n"
            + "      GRAPH ?graph { <_ID_> ?relation ?" + SparqlVariable.TARGET + " . \n"
            + "        FILTER (regex(str(?relation), \"_RELATION_\")) }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " ?property ?_label . \n"
            + "        FILTER(regex(str(?property), \"" + SparqlPrefix.RDFS.getPrefix() + "label|" + SparqlPrefix.SKOS.getPrefix() + "definition\")) }  \n"
            + "    OPTIONAL { ?" + SparqlVariable.TARGET + " sesame:directType ?_type . \n"
            + "    FILTER (!regex(str(?_type), \"" + SparqlPrefix.RDFS.getPrefix() + "|" + SparqlPrefix.RDF.getPrefix() + "|" + SparqlPrefix.OWL.getPrefix() + "\")) }\n"
            + "    FILTER (!regex(str(?relation), \"http://www.ontologydesignpatterns.org/cp/owl/semiotics.owl#\"))\n"
            + "    BIND(?relation AS ?" + SparqlVariable.LINK + ")\n"
            + "   } GROUP BY ?" + SparqlVariable.TARGET + " ?graph ?" + SparqlVariable.LINK + "\n"
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
            + "      luc:limit \"500\" ;\n"
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
            + "      luc:limit \"500\" ;\n"
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
            + "      luc:limit \"500\" ;\n"
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
            + "      luc:limit \"500\" ;\n"
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
