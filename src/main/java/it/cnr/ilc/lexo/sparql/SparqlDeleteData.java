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
public class SparqlDeleteData {

    public static final String DELETE_LEXICON_LANGUAGE
            = "DELETE { <_ID_> ?predicate ?object . } \n"
            + "WHERE { <_ID_> ?predicate ?object . }";

    public static final String DELETE_DICTIONARY
            = "DELETE { <_ID_> ?predicate ?object . } \n"
            + "WHERE { <_ID_> ?predicate ?object . }";

    public static final String DELETE_LEXICAL_ENTRY
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_DICTIONARY_ENTRY
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_LEXICOGRAPHIC_COMPONENT
            = "DELETE { <_ID_> ?predicate ?object . ?subject ?_predicate <_ID_> . \n"
            + "_TO_DELETE_ }\n"
            + "INSERT { _TO_INSERT_ }\n"
            + "WHERE { <_ID_> ?predicate ?object . ?subject ?_predicate <_ID_> . \n"
            + "_TO_DELETE_ }";

    public static final String DELETE_LEXICOGRAPHIC_COMPONENT_AND_POSITION
            = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "DELETE { \n"
            + "    <_ID_> ?propName ?lexComp .\n"
            + "    ?lexComp ?p ?o .\n"
            + "    ?s ?_p ?lexComp .\n"
            + "}\n"
            + "WHERE {\n"
            + "    <_ID_> ?propName ?lexComp .\n"
            + "    FILTER (strstarts(str(?propName), str(rdf:_)))\n"
            + "    ?lexComp ?p ?o .\n"
            + "    ?s ?_p ?lexComp .\n"
            + "}";

    public static final String DELETE_DICTIONARY_ENTRY_ASSOCIATION
            = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "DELETE { \n"
            + "    ?de ?propName <_ID_> .\n"
            + "    <_ID_> ?p ?o .\n"
            + "    ?s ?_p <_ID_> .\n"
            + "}\n"
            + "WHERE {\n"
            + "    ?de ?propName <_ID_> .\n"
            + "    FILTER (strstarts(str(?propName), str(rdf:_)))\n"
            + "    <_ID_> ?p ?o .\n"
            + "    ?s ?_p <_ID_> .\n"
            + "}";

    public static final String DELETE_FORM
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_LEXICAL_SENSE
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_COMPONENT
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_COLLOCATION
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_CORPUS_FREQUENCY
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_FORM_RESTRICTION
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_LEXICOSEMANTIC_RELATION
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_TRANSLATION_SET
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_IMAGE
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_LEXICAL_CONCEPT
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_CONCEPT_SET
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_ETYMOLOGICAL_LINK
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_ETYMOLOGY
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_BIBLIOGRAPHY
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    // TODO: update lastupdate field
    public static final String DELETE_RELATION
            = "DELETE {\n"
            + "    <_ID_> ?relation ?target\n"
            + "} WHERE {\n"
            + "    <_ID_> ?relation ?target .\n"
            + "    FILTER(regex(str(?relation), \"_RELATION_$\"))\n"
            + "    FILTER(regex(str(?target), \"_TARGET_$\"))\n"
            + "}";

    public static final String DELETE_COGNATE_TYPE
            = SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + "DELETE DATA {  <_ID_> a ety:Cognate . }";

    /*--------------*/
 /* Melcuk Model */
 /*--------------*/
    public static final String DELETE_LEXICAL_FUNCTION
            = SparqlPrefix.LFREL.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> ?p ?o }\n"
            + "WHERE {  <_ID_> ?p ?o }";

    public static final String DELETE_ECD_FORM
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

//    public static final String DELETE_ECD_ENTRY
//            = "DELETE { <_ID_> ?predicate ?object . \n"
//            + "         ?subject ?_predicate <_ID_> . }\n"
//            + "WHERE { <_ID_> ?predicate ?object . \n"
//            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";
    
    public static final String DELETE_ECD_ENTRY
            = SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXICOG.getSparqlPrefix() + "\n"
            + "\n"
            + "DELETE {\n"
            + "  # Selected de: all outgoing and incoming triples\n"
            + "  ?de ?p_de ?o_de .\n"
            + "  ?s_de ?p2_de ?de .\n"
            + "\n"
            + "  # Components (top-level and descendants): all triples\n"
            + "  ?topComp ?cp ?co .\n"
            + "  ?anyComp ?cp2 ?co2 .\n"
            + "\n"
            + "  # Lexical entries described by ?de: all triples\n"
            + "  ?le ?lp ?lo .\n"
            + "\n"
            + "  # Forms linked to the lexical entries: all triples\n"
            + "  ?form ?fp ?fo .\n"
            + "}\n"
            + "WHERE {\n"
            + "  # Replace ex:de with your specific de\n"
            + "  VALUES ?de { ex:de }\n"
            + "\n"
            + "  # --- remove :de (outgoing and incoming) ---\n"
            + "  OPTIONAL { ?de  ?p_de  ?o_de . }\n"
            + "  OPTIONAL { ?s_de ?p2_de ?de  . }\n"
            + "\n"
            + "  # --- component structure connected to ?de via rdf:_n ---\n"
            + "  OPTIONAL {\n"
            + "    ?de ?m ?topComp .\n"
            + "    FILTER( STRSTARTS(STR(?m), STR(rdf:_)) )\n"
            + "\n"
            + "    OPTIONAL { ?topComp ?cp ?co . }\n"
            + "\n"
            + "    OPTIONAL {\n"
            + "      # Traverse all sub-components via rdf:_n (extend the list if needed)\n"
            + "      ?topComp (rdf:_1|rdf:_2|rdf:_3|rdf:_4|rdf:_5|rdf:_6|rdf:_7|rdf:_8|rdf:_9|rdf:_10)* ?anyComp .\n"
            + "      OPTIONAL { ?anyComp ?cp2 ?co2 . }\n"
            + "    }\n"
            + "  }\n"
            + "\n"
            + "  # --- lexical entries described by ?de and their forms ---\n"
            + "  OPTIONAL {\n"
            + "    ?de lexicog:describes ?le .\n"
            + "    OPTIONAL { ?le ?lp ?lo . }\n"
            + "    OPTIONAL { ?ls ?_lp ?le . }   # possible incoming triples to the lexical entry\n"
            + "\n"
            + "    OPTIONAL {\n"
            + "      ?le (ontolex:canonicalForm|ontolex:otherForm|ontolex:lexicalForm) ?form .\n"
            + "      OPTIONAL { ?form ?fp ?fo . }\n"
            + "      OPTIONAL { ?fs ?_fp ?form . }  # possible incoming triples to the form\n"
            + "    }\n"
            + "        \n"
            + "    OPTIONAL {\n"
            + "      ?le ontolex:sense ?sense .\n"
            + "      OPTIONAL { ?sense ?sp ?so . }\n"
            + "      OPTIONAL { ?ss ?_sp ?sense . }  # possible incoming triples to the sense\n"
            + "    }\n"
            + "  }\n"
            + "}";

    public static final String DELETE_ECD_ENTRY_POS
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_EC_DICTIONARY
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

    public static final String DELETE_ECD_MEANINGS
            = SparqlPrefix.LEXICOG.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "\n"
            + "DELETE {\n"
            + "  # Detach top-level components from the target de (only rdf:_n membership)\n"
            + "  <_ID_> ?m ?topComp .\n"
            + "\n"
            + "  # Delete all triples of the components (top-level and descendants)\n"
            + "  ?topComp ?cp0 ?co0 .\n"
            + "  ?anyComp ?cp  ?co  .\n"
            + "}\n"
            + "WHERE {\n"
            + "  # Components directly linked from ?de via rdf:_n indices\n"
            + "  <_ID_> ?m ?topComp .\n"
            + "  FILTER( STRSTARTS(STR(?m), STR(rdf:_)) )\n"
            + "\n"
            + "  # Triples of the top-level component\n"
            + "  OPTIONAL { ?topComp ?cp0 ?co0 . }\n"
            + "\n"
            + "  # Traverse all sub-components via rdf:_n\n"
            + "  OPTIONAL {\n"
            + "    ?topComp (rdf:_1|rdf:_2|rdf:_3|rdf:_4|rdf:_5|rdf:_6|rdf:_7|rdf:_8|rdf:_9|rdf:_10)* ?anyComp .\n"
            + "    OPTIONAL { ?anyComp ?cp ?co . }\n"
            + "  }\n"
            + "}";
}
