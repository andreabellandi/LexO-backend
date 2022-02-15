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
public class SparqlQueryUtil {

    public static final String ENTITY_RELATION
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.OWL.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.VALUE + "\n"
            + "FROM " + SparqlPrefix.ONTO.getPrefix() + "explicit\n"
            + "WHERE {\n"
            + "    " + SparqlPrefix.LEX.getPrefix() + "_ID_ a ?" + SparqlVariable.TYPE + " .\n"
            + "    OPTIONAL { " + SparqlPrefix.LEX.getPrefix() + "_ID_ _RELATION_ ?" + SparqlVariable.VALUE + " . }\n"
            + "    FILTER(!STRSTARTS(STR(?type), str(" + SparqlPrefix.OWL.getPrefix() + ")))\n"
            + "}";

    public static final String ASK_ENTITY_LINGUISTIC_RELATION
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "ASK { lex:_ID_ _RELATION_ _VALUE_ }";

    public static final String ASK_ENTITY_GENERIC_RELATION
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "ASK { <_ID_> _RELATION_ _VALUE_ }";

    public static final String BIBLIOGRAFY_BY_ITEMKEY
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.BIBLIOGRAPHY_ID + "\n"
            + "WHERE { \n"
            + "       lex:_ID_ dct:references ?" + SparqlVariable.BIBLIOGRAPHY_ID + " .\n"
            + "       ?" + SparqlVariable.BIBLIOGRAPHY_ID + " dct:publisher \"_ITEMKEY_\" }";

    public static final String IS_LEXICON_LANGUAGE
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + "ASK { " + SparqlPrefix.LEX.getPrefix() + "_ID_ a " + SparqlPrefix.LIME.getPrefix() + "Lexicon }";

    public static final String IS_LEXICALENTRY_ID
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "ASK { " + SparqlPrefix.LEX.getPrefix() + "_ID_ a " + SparqlPrefix.ONTOLEX.getPrefix() + "LexicalEntry }";
    
    public static final String IS_COMPONENT_ID
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.DECOMP.getSparqlPrefix() + "\n"
            + "ASK { " + SparqlPrefix.LEX.getPrefix() + "_ID_ a " + SparqlPrefix.DECOMP.getPrefix() + "Component }";
    
    public static final String IS_LEXICALENTRY_ID_OR_COMPONENT_ID
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
             + SparqlPrefix.DECOMP.getSparqlPrefix() + "\n"
            + "ASK { \n" 
            + "{ " + SparqlPrefix.LEX.getPrefix() + "_ID_ a " + SparqlPrefix.ONTOLEX.getPrefix() + "LexicalEntry }\n"
            + " UNION \n"
            + "{ " + SparqlPrefix.LEX.getPrefix() + "_ID_ a " + SparqlPrefix.DECOMP.getPrefix() + "Component }\n"
            + "}";

    public static final String IS_ETYMON_ID
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "ASK { " + SparqlPrefix.LEX.getPrefix() + "_ID_ a " + SparqlPrefix.ETY.getPrefix() + "Etymon }";

    public static final String HAS_LEXICALENTRY_CHILDREN
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "ASK {" + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.ONTOLEX.getPrefix() + "sense|" + SparqlPrefix.ONTOLEX.getPrefix() + "lexicalForm ?f}";

    public static final String EXISTS_ID
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "ASK { " + SparqlPrefix.LEX.getPrefix() + "_ID_ a ?type }";
    
    public static final String EXISTS_TYPE_ID
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "ASK { " + SparqlPrefix.LEX.getPrefix() + "_ID_ a <_TYPE_> }";

    public static final String EXISTS_LANGUAGE
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + "ASK { ?lex a " + SparqlPrefix.LIME.getPrefix() + "Lexicon ;"
            + "      " + SparqlPrefix.LIME.getPrefix() + "language \"_LANG_\" . }";

    public static final String IS_FORM_ID
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "ASK { " + SparqlPrefix.LEX.getPrefix() + "_ID_ a " + SparqlPrefix.ONTOLEX.getPrefix() + "Form }";

    public static final String IS_ETYMOLOGY_ID
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "ASK { " + SparqlPrefix.LEX.getPrefix() + "_ID_ a " + SparqlPrefix.ETY.getPrefix() + "Etymology }";

    public static final String IS_ETYMOLOGICAL_LINK_ID
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "ASK { " + SparqlPrefix.LEX.getPrefix() + "_ID_ a " + SparqlPrefix.ETY.getPrefix() + "EtyLink }";

    public static final String IS_LEXICALSENSE_ID
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "ASK { " + SparqlPrefix.LEX.getPrefix() + "_ID_ a " + SparqlPrefix.ONTOLEX.getPrefix() + "LexicalSense }";

    public static final String NUMBER_OF_STATEMENTS
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.STATEMENTS_NUMBER + " \n"
            + "FROM NAMED <http://www.ontotext.com/count>\n"
            + "{ " + SparqlPrefix.LEX.getPrefix() + "_ID_ ?" + SparqlVariable.STATEMENTS_NUMBER + " ?o}";

    public static final String LANGUAGE
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICON_LANGUAGE + " \n"
            + "WHERE { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "entry " + SparqlPrefix.LEX.getPrefix() + "_ID_ ;\n"
            + "        " + SparqlPrefix.LIME.getPrefix() + "language ?" + SparqlVariable.LEXICON_LANGUAGE + " }";

    public static final String LEXICAL_ENTRY_BY_FORM
            = SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME + " \n"
            + "WHERE { ?" + SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME + " " + SparqlPrefix.ONTOLEX.getPrefix() + "lexicalForm "
            + SparqlPrefix.LEX.getPrefix() + "_ID_ }";

    public static final String LEXICAL_ENTRY_NUMBER_BY_LANGUAGE
            = SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + "SELECT (count(?count) as ?" + SparqlVariable.LABEL_COUNT + ") \n"
            + "WHERE { lex:_ID_ " + SparqlPrefix.LIME.getPrefix() + "entry ?count }";

    public static final String LEXICAL_ENTRY_LABEL
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LABEL + " \n"
            + "WHERE { lex:_ID_ " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " }";

    public static final String IS_COGNATE
            = SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "ASK WHERE { \n"
            + "  {\n"
            + "     SELECT (count(?" + SparqlVariable.LEXICAL_ENTRY + ") as ?" + SparqlVariable.LABEL_COUNT + ")\n"
            + "     WHERE {\n"
            + "        ?" + SparqlVariable.LEXICAL_ENTRY + " ety:cognate _ID_ \n"
            + "     }\n"
            + "  }\n"
            + "  FILTER( ?" + SparqlVariable.LABEL_COUNT + " > _COG_NUMBER_ )\n"
            + "}";
}
