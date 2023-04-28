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

    public static final String DELETE_LEXICAL_ENTRY
            = "DELETE { <_ID_> ?predicate ?object . \n"
            + "         ?subject ?_predicate <_ID_> . }\n"
            + "WHERE { <_ID_> ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate <_ID_> . } }";

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
}
