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
public class SparqlIndex {

    public static final String LEXICAL_ENTRY_INDEX
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "INSERT DATA {\n"
            + "     inst:" + SparqlVariable.LEXICAL_ENTRY_INDEX + " luc:createConnector '''\n"
            + "{\n"
            + "   \"types\": [\n"
            + "     \"http://www.w3.org/ns/lemon/ontolex#LexicalEntry\"\n"
            + "   ],\n"
            + "   \"fields\": [\n"
            + "     {\n"
            + "       \"fieldName\": \"lexicalEntryLabel\",\n"
            + "       \"propertyChain\": [\n"
            + "        \"http://www.w3.org/2000/01/rdf-schema#label\"\n"
            + "       ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "     {   \n"
            + "      \"fieldName\": \"writtenOtherForm\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#otherForm\",\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#writtenRep\"\n"
            + "      ],\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "     {   \n"
            + "      \"fieldName\": \"writtenLexicalForm\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#lexicalForm\",\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#writtenRep\"\n"
            + "      ],\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "      \"fieldName\": \"writtenCanonicalForm\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#canonicalForm\",\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#writtenRep\"\n"
            + "      ],\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "    {\n"
            + "       \"fieldName\": \"lexicalEntryIRI\",\n"
            + "       \"propertyChain\": [\n"
            + "         \"$self\"\n"
            + "       ],\n"
            + "	\"multivalued\": false,\n"
            + "    \"analyzed\": false\n"
            + "    },\n"
            + "    {\n"
            + "         \"fieldName\": \"writtenFormLanguage\",\n"
            + "         \"propertyChain\": [\n"
            + "           \"http://www.w3.org/2000/01/rdf-schema#label\",\n"
            + "           \"lang()\"\n"
            + "         ],\n"
            + "         \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "         \"fieldName\": \"type\",\n"
            + "         \"propertyChain\": [\n"
            + "           \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\",\n"
            + "           \"http://www.w3.org/2000/01/rdf-schema#label\"\n"
            + "         ],\n"
            + "         \"analyzed\": false,\n"
            + "         \"languages\": [\n"
            + "			\"en\"\n"
            + "         ]\n"
            + "     },\n"
            + "     {\n"
            + "         \"fieldName\": \"pos\",\n"
            + "         \"propertyChain\": [\n"
            + "           \"http://www.lexinfo.net/ontology/3.0/lexinfo#partOfSpeech\",\n"
            + "           \"http://www.w3.org/2000/01/rdf-schema#label\"\n"
            + "         ],\n"
            + "         \"analyzed\": false,\n"
            + "         \"languages\": [\n"
            + "			\"en\"\n"
            + "         ]\n"
            + "     },\n"
            + "     {\n"
            + "          \"fieldName\": \"status\",\n"
            + "          \"propertyChain\": [\n"
            + "                \"http://www.w3.org/2003/06/sw-vocab-status/ns#term_status\"\n"
            + "          ]\n"
            + "     },\n"
            + "    {\n"
            + "          \"fieldName\": \"author\",\n"
            + "          \"propertyChain\": [\n"
            + "                \"http://purl.org/dc/terms/creator\"\n"
            + "          ]\n"
            + "     }\n"
            + "   ],\n"
            // lexical entries having not a  rdfs:label are not indexed 
            + "   \"documentFilter\": \"bound(?lexicalEntryLabel)\""
            + " }\n"
            + " ''' .\n"
            + " }";
    
    public static final String DICTIONARY_ENTRY_INDEX
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "INSERT DATA {\n"
            + "     inst:" + SparqlVariable.DICTIONARY_ENTRY_INDEX + " luc:createConnector '''\n"
            + "{\n"
            + "   \"types\": [\n"
            + "     \"http://www.w3.org/ns/lemon/lexicog#Entry\"\n"
            + "   ],\n"
            + "   \"fields\": [\n"
            + "     {\n"
            + "       \"fieldName\": \"dictionaryEntryLabel\",\n"
            + "       \"propertyChain\": [\n"
            + "        \"http://www.w3.org/2000/01/rdf-schema#label\"\n"
            + "       ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "    {\n"
            + "       \"fieldName\": \"dictionaryEntryIRI\",\n"
            + "       \"propertyChain\": [\n"
            + "         \"$self\"\n"
            + "       ],\n"
            + "	\"multivalued\": false,\n"
            + "    \"analyzed\": false\n"
            + "    },\n"
            + "    {\n"
            + "         \"fieldName\": \"entryLanguage\",\n"
            + "         \"propertyChain\": [\n"
            + "           \"http://www.w3.org/2000/01/rdf-schema#label\",\n"
            + "           \"lang()\"\n"
            + "         ],\n"
            + "         \"analyzed\": false\n"
            + "     },\n"
//            + "     {\n"
//            + "         \"fieldName\": \"pos\",\n"
//            + "         \"propertyChain\": [\n"
//            + "           \"http://www.lexinfo.net/ontology/3.0/lexinfo#partOfSpeech\",\n"
//            + "           \"http://www.w3.org/2000/01/rdf-schema#label\"\n"
//            + "         ],\n"
//            + "         \"analyzed\": false,\n"
//            + "         \"languages\": [\n"
//            + "			\"en\"\n"
//            + "         ]\n"
//            + "     },\n"
            + "     {\n"
            + "          \"fieldName\": \"status\",\n"
            + "          \"propertyChain\": [\n"
            + "                \"http://www.w3.org/2003/06/sw-vocab-status/ns#term_status\"\n"
            + "          ]\n"
            + "     },\n"
            + "    {\n"
            + "          \"fieldName\": \"author\",\n"
            + "          \"propertyChain\": [\n"
            + "                \"http://purl.org/dc/terms/creator\"\n"
            + "          ]\n"
            + "     }\n"
            + "   ],\n"
            // dictionary entries having not a  rdfs:label are not indexed 
            + "   \"documentFilter\": \"bound(?dictionaryEntryLabel)\""
            + " }\n"
            + " ''' .\n"
            + " }";

    public static final String FORM_INDEX
            = "PREFIX inst: <http://www.ontotext.com/connectors/lucene/instance#>\n"
            + "PREFIX luc: <http://www.ontotext.com/connectors/lucene#>\n"
            + "INSERT DATA {\n"
            + "     inst:formIndex luc:createConnector '''\n"
            + " {\n"
            + "   \"types\": [\n"
            + "     \"http://www.w3.org/ns/lemon/ontolex#Form\"\n"
            + "   ],\n"
            + "   \"fields\": [\n"
            + "     {   \n"
            + "      \"fieldName\": \"writtenRep\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#writtenRep\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "      \"fieldName\": \"phoneticRep\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#phoneticRep\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "      \"fieldName\": \"pronunciation\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.lexinfo.net/ontology/3.0/lexinfo#pronunciation\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "      \"fieldName\": \"romanization\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.lexinfo.net/ontology/3.0/lexinfo#romanization\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "      \"fieldName\": \"segmentation\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.lexinfo.net/ontology/3.0/lexinfo#segmentation\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "      \"fieldName\": \"transliteration\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.lexinfo.net/ontology/3.0/lexinfo#transliteration\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "    {\n"
            + "       \"fieldName\": \"formIRI\",\n"
            + "       \"propertyChain\": [\n"
            + "         \"$self\"\n"
            + "       ],\n"
            + "	\"multivalued\": false,\n"
            + "    \"analyzed\": false\n"
            + "    },\n"
            + "    {\n"
            + "    \"fieldName\": \"writtenFormLanguage\",\n"
            + "    \"propertyChain\": [\n"
            + "       \"http://www.w3.org/2000/01/rdf-schema#label\",\n"
            + "       \"lang()\"\n"
            + "     ],\n"
            + "     \"analyzed\": false\n"
            + "     }\n"
            + "   ]\n"
            + " }\n"
            + " ''' .\n"
            + " }";

    public static final String LEXICAL_SENSE_INDEX = ""
            + "PREFIX inst: <http://www.ontotext.com/connectors/lucene/instance#>\n"
            + "PREFIX luc: <http://www.ontotext.com/connectors/lucene#>\n"
            + "INSERT DATA {\n"
            + "     inst:lexicalSenseIndex luc:createConnector '''\n"
            + " {\n"
            + "   \"types\": [\n"
            + "     \"http://www.w3.org/ns/lemon/ontolex#LexicalSense\"\n"
            + "   ],\n"
            + "   \"fields\": [\n"
            + "     {   \n"
            + "      \"fieldName\": \"definition\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.w3.org/2004/02/skos/core#definition\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "{   \n"
            + "      \"fieldName\": \"description\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.lexinfo.net/ontology/3.0/lexinfo#description\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "{   \n"
            + "      \"fieldName\": \"etymology\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.lexinfo.net/ontology/3.0/lexinfo#etymology\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "{   \n"
            + "      \"fieldName\": \"explanation\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.lexinfo.net/ontology/3.0/lexinfo#explanation\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "{   \n"
            + "      \"fieldName\": \"gloss\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.lexinfo.net/ontology/3.0/lexinfo#gloss\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "{   \n"
            + "      \"fieldName\": \"senseExample\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.lexinfo.net/ontology/3.0/lexinfo#senseExample\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "    {   \n"
            + "      \"fieldName\": \"senseTranslation\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.lexinfo.net/ontology/3.0/lexinfo#senseTranslation\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "    {\n"
            + "       \"fieldName\": \"lexicalSenseIRI\",\n"
            + "       \"propertyChain\": [\n"
            + "         \"$self\"\n"
            + "       ],\n"
            + "	\"multivalued\": false,\n"
            + "    \"analyzed\": false\n"
            + "    }\n"
            + "   ]\n"
            + " }\n"
            + " ''' .\n"
            + " }";

    public static final String DELETE_INDEX
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "INSERT DATA {\n"
            + "  inst:_INDEX_NAME_ luc:dropConnector \"\" .\n"
            + "}";

    public static final String CONNECTORS
            = SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.CONNECTOR_NAME + " {\n"
            + "  ?connector luc:listConnectors ?" + SparqlVariable.CONNECTOR_NAME + " .\n"
            + "}";

    public static String CONCEPT_REFERENCE_INDEX = "PREFIX inst: <http://www.ontotext.com/connectors/lucene/instance#>\n"
            + "PREFIX luc: <http://www.ontotext.com/connectors/lucene#>\n"
            + "INSERT DATA {\n"
            + "     inst:conceptReferenceIndex luc:createConnector '''\n"
            + " {\n"
            + "   \"types\": [\n"
            + "     \"http://www.w3.org/ns/lemon/ontolex#LexicalSense\"\n"
            + "   ],\n"
            + "   \"fields\": [\n"
            + "    {\n"
            + "       \"fieldName\": \"referencedBy\",\n"
            + "       \"propertyChain\": [\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#reference\"\n"
            + "       ],\n"
            + "    \"analyzed\": false\n"
            + "    },\n"
            + "{\n"
            + "       \"fieldName\": \"lemma\",\n"
            + "       \"propertyChain\": [\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#isSenseOf\",\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#canonicalForm\",\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#writtenRep\",\n"
            + "       ],\n"
            + "\"multivalued\": false,\n"
            + "    \"analyzed\": false\n"
            + "    }\n"
            + "   ]\n"
            + " }\n"
            + " ''' .\n"
            + " }";

    public static String ETYMOLOGY_INDEX = "PREFIX inst: <http://www.ontotext.com/connectors/lucene/instance#>\n"
            + "PREFIX luc: <http://www.ontotext.com/connectors/lucene#>\n"
            + "INSERT DATA {\n"
            + "     inst:etymologyIndex luc:createConnector '''\n"
            + " {\n"
            + "   \"types\": [\n"
            + "     \"http://lari-datasets.ilc.cnr.it/lemonEty#Etymology\"\n"
            + "   ],\n"
            + "   \"fields\": [\n"
            + "     {   \n"
            + "      \"fieldName\": \"label\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.w3.org/2000/01/rdf-schema#label\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "    {\n"
            + "       \"fieldName\": \"etymologyIRI\",\n"
            + "       \"propertyChain\": [\n"
            + "         \"$self\"\n"
            + "       ],\n"
            + "	\"multivalued\": false,\n"
            + "    \"analyzed\": false\n"
            + "    },\n"
            + "     {   \n"
            + "      \"fieldName\": \"author\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://purl.org/dc/terms/creator\"\n"
            + "      ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     }\n"
            + "   ]\n"
            + " }\n"
            + " ''' .\n"
            + " }";

    public static String COMPONENT_INDEX = "PREFIX inst: <http://www.ontotext.com/connectors/lucene/instance#>\n"
            + "PREFIX luc: <http://www.ontotext.com/connectors/lucene#>\n"
            + "INSERT DATA {\n"
            + "     inst:componentIndex luc:createConnector '''\n"
            + " {\n"
            + "   \"types\": [\n"
            + "     \"http://www.w3.org/ns/lemon/decomp#Component\"\n"
            + "   ],\n"
            + "   \"fields\": [\n"
            + "     {\n"
            + "       \"fieldName\": \"componentLabel\",\n"
            + "       \"propertyChain\": [\n"
            + "        \"http://www.w3.org/2000/01/rdf-schema#label\"\n"
            + "       ],\n"
            + "      \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "       \"fieldName\": \"ComponentIRI\",\n"
            + "       \"propertyChain\": [\n"
            + "         \"$self\"\n"
            + "       ],\n"
            + "	\"multivalued\": false,\n"
            + "    \"analyzed\": false\n"
            + "    },\n"
            + "     {\n"
            + "         \"fieldName\": \"morpho\",\n"
            + "         \"propertyChain\": [\n"
            + "           \"http://www.lexinfo.net/ontology/3.0/lexinfo#morphosyntacticProperty\",\n"
            + "           \"http://www.w3.org/2000/01/rdf-schema#label\"\n"
            + "         ],\n"
            + "         \"analyzed\": false,\n"
            + "         \"languages\": [\n"
            + "			\"en\"\n"
            + "         ]\n"
            + "     },\n"
            + "    {\n"
            + "          \"fieldName\": \"author\",\n"
            + "          \"propertyChain\": [\n"
            + "                \"http://purl.org/dc/terms/creator\"\n"
            + "          ]\n"
            + "     }\n"
            + "   ]\n"
            + " }\n"
            + " ''' .\n"
            + " }";

    public static String LEXICAL_CONCEPT_INDEX = "PREFIX inst: <http://www.ontotext.com/connectors/lucene/instance#>\n"
            + "PREFIX luc: <http://www.ontotext.com/connectors/lucene#>\n"
            + "INSERT DATA {\n"
            + "     inst:lexicalConceptIndex luc:createConnector '''\n"
            + " {\n"
            + "   \"types\": [\n"
            + "     \"http://www.w3.org/ns/lemon/ontolex#LexicalConcept\"\n"
            + "   ],\n"
            + "   \"fields\": [\n"
            + "     {\n"
            + "       \"fieldName\": \"label\",\n"
            + "       \"propertyChain\": [\n"
            + "        \"http://www.w3.org/2000/01/rdf-schema#label\"\n"
            + "       ],\n"
            + "     \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "       \"fieldName\": \"prefLabel\",\n"
            + "       \"propertyChain\": [\n"
            + "        \"http://www.w3.org/2004/02/skos/core#prefLabel\"\n"
            + "       ],\n"
            + "     \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "       \"fieldName\": \"altLabel\",\n"
            + "       \"propertyChain\": [\n"
            + "        \"http://www.w3.org/2004/02/skos/core#altLabel\"\n"
            + "       ],\n"
            + "     \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "       \"fieldName\": \"hiddenLabel\",\n"
            + "       \"propertyChain\": [\n"
            + "        \"http://www.w3.org/2004/02/skos/core#hiddenLabel\"\n"
            + "       ],\n"
            + "     \"analyzed\": false\n"
            + "     },\n"
            + "     {   \n"
            + "      \"fieldName\": \"lexicalEntry\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#isEvokedBy\"\n"
            + "      ],\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "     {   \n"
            + "      \"fieldName\": \"inScheme\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.w3.org/2004/02/skos/core#inScheme\"\n"
            + "      ],\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "     {\n"
            + "      \"fieldName\": \"lexicalSense\",\n"
            + "      \"propertyChain\": [\n"
            + "        \"http://www.w3.org/ns/lemon/ontolex#lexicalizedSense\"\n"
            + "      ],\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "    {\n"
            + "       \"fieldName\": \"lexicalConceptIRI\",\n"
            + "       \"propertyChain\": [\n"
            + "         \"$self\"\n"
            + "       ],\n"
            + "	\"multivalued\": false,\n"
            + "    \"analyzed\": false\n"
            + "    },\n"
            + "     {\n"
            + "         \"fieldName\": \"definition\",\n"
            + "         \"propertyChain\": [\n"
            + "           \"http://www.w3.org/2004/02/skos/core#definition\"\n"
            + "         ],\n"
            + "	  \"multivalued\": false,\n"
            + "      \"analyzed\": false\n"
            + "     },\n"
            + "    {\n"
            + "          \"fieldName\": \"author\",\n"
            + "          \"propertyChain\": [\n"
            + "                \"http://purl.org/dc/terms/creator\"\n"
            + "          ],\n"
            + "     \"analyzed\": false\n"
            + "     }\n"
            + "   ]\n"
            + " }\n"
            + " ''' .\n"
            + " }";

    public static String CONCEPT_SET_INDEX = "PREFIX inst: <http://www.ontotext.com/connectors/lucene/instance#>\n"
            + "            PREFIX luc: <http://www.ontotext.com/connectors/lucene#>\n"
            + "            INSERT DATA {\n"
            + "                 inst:conceptSetIndex luc:createConnector '''\n"
            + "             {\n"
            + "               \"types\": [\n"
            + "                 \"http://www.w3.org/ns/lemon/ontolex#ConceptSet\"\n"
            + "               ],\n"
            + "               \"fields\": [\n"
            + "                 {\n"
            + "                   \"fieldName\": \"label\",\n"
            + "                   \"propertyChain\": [\n"
            + "                    \"http://www.w3.org/2000/01/rdf-schema#label\"\n"
            + "                   ],\n"
            + "                 \"analyzed\": false\n"
            + "                 },\n"
            + "                 {\n"
            + "                   \"fieldName\": \"prefLabel\",\n"
            + "                   \"propertyChain\": [\n"
            + "                    \"http://www.w3.org/2004/02/skos/core#prefLabel\"\n"
            + "                   ],\n"
            + "                 \"analyzed\": false\n"
            + "                 },\n"
            + "                 {\n"
            + "                   \"fieldName\": \"altLabel\",\n"
            + "                   \"propertyChain\": [\n"
            + "                    \"http://www.w3.org/2004/02/skos/core#altLabel\"\n"
            + "                   ],\n"
            + "                 \"analyzed\": false\n"
            + "                 },\n"
            + "                 {\n"
            + "                   \"fieldName\": \"hiddenLabel\",\n"
            + "                   \"propertyChain\": [\n"
            + "                    \"http://www.w3.org/2004/02/skos/core#hiddenLabel\"\n"
            + "                   ],\n"
            + "                 \"analyzed\": false\n"
            + "                 },\n"
            + "                 {   \n"
            + "                  \"fieldName\": \"topConcept\",\n"
            + "                  \"propertyChain\": [\n"
            + "                    \"http://www.w3.org/2004/02/skos/core#hasTopConcept\"\n"
            + "                  ],\n"
            + "                  \"analyzed\": false\n"
            + "                 },\n"
            + "                {\n"
            + "                   \"fieldName\": \"conceptSetIRI\",\n"
            + "                   \"propertyChain\": [\n"
            + "                     \"$self\"\n"
            + "                   ],\n"
            + "            	\"multivalued\": false,\n"
            + "                \"analyzed\": false\n"
            + "                },\n"
            + "                 {\n"
            + "                     \"fieldName\": \"definition\",\n"
            + "                     \"propertyChain\": [\n"
            + "                       \"http://www.w3.org/2004/02/skos/core#definition\"\n"
            + "                     ],\n"
            + "            	  \"multivalued\": false,\n"
            + "                  \"analyzed\": false\n"
            + "                 },\n"
            + "                {\n"
            + "                      \"fieldName\": \"author\",\n"
            + "                      \"propertyChain\": [\n"
            + "                            \"http://purl.org/dc/terms/creator\"\n"
            + "                      ],\n"
            + "                 \"analyzed\": false\n"
            + "                 }\n"
            + "               ]\n"
            + "             }\n"
            + "             ''' .\n"
            + "             }";
}
