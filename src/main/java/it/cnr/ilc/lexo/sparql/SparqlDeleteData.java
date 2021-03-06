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
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "DELETE { lex:_ID_ ?predicate ?object . } \n"
            + "WHERE { lex:_ID_ ?predicate ?object . }";

    public static final String DELETE_LEXICAL_ENTRY
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "DELETE { lex:_ID_ ?predicate ?object . \n"
            + "         ?subject ?_predicate lex:_ID_ . }\n"
            + "WHERE { lex:_ID_ ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate lex:_ID_ . } }";

    public static final String DELETE_FORM
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "DELETE { lex:_ID_ ?predicate ?object . \n"
            + "         ?subject ?_predicate lex:_ID_ . }\n"
            + "WHERE { lex:_ID_ ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate lex:_ID_ . } }";

    public static final String DELETE_LEXICAL_SENSE
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "DELETE { lex:_ID_ ?predicate ?object . \n"
            + "         ?subject ?_predicate lex:_ID_ . }\n"
            + "WHERE { lex:_ID_ ?predicate ?object . \n"
            + "         OPTIONAL { ?subject ?_predicate lex:_ID_ . } }";

    // TODO: update lastupdate field
    public static final String DELETE_RELATION
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "DELETE {\n"
            + "    lex:_ID_ ?relation ?target\n"
            + "} WHERE {\n"
            + "    lex:_ID_ ?relation ?target .\n"
            + "    FILTER(regex(str(?relation), \"_RELATION_$\"))\n"
            + "    FILTER(regex(str(?target), \"_TARGET_$\"))\n"
            + "}";

}
