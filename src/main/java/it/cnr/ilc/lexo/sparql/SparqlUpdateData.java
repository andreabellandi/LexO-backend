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
public class SparqlUpdateData {

    public static final String UPDATE_LEXICON_LANGUAGE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { " + SparqlPrefix.LEX.getPrefix() + "_ID_ _RELATION_ _VALUE_TO_DELETE_ ;\n "
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . } \n"
            + "INSERT { " + SparqlPrefix.LEX.getPrefix() + "_ID_ _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified _LAST_UPDATE_ . }\n"
            + "WHERE {  " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . \n"
            + "      OPTIONAL {  " + SparqlPrefix.LEX.getPrefix() + "_ID_ _RELATION_ _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_LEXICAL_ENTRY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "DELETE { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { lex:_ID_ _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { lex:_ID_ dct:modified ?modified .} \n"
            + "         OPTIONAL { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_LEXICAL_ENTRY_BACKWARDING_STATUS
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.VS.getPrefix() + "term_status ?status ;\n "
            + "                  _CURRENT_ROLE_ ?role ;\n"
            + "                  _CURRENT_DATE_ ?date ;\n"
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . }\n"
            + "INSERT { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.VS.getPrefix() + "term_status _STATUS_ ;\n "
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified _LAST_UPDATE_ . } \n"
            + "WHERE { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.VS.getPrefix() + "term_status ?status ;\n "
            + "                  _CURRENT_ROLE_ ?role ;\n"
            + "                  _CURRENT_DATE_ ?date ;\n"
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified ?modified .  }\n";

    public static final String UPDATE_LEXICAL_ENTRY_FOREWARDING_STATUS
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.VS.getPrefix() + "term_status ?status ;\n "
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . }\n"
            + "INSERT { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.VS.getPrefix() + "term_status _STATUS_ ;\n "
            + "                  _NEW_ROLE_ _USER_ ;\n"
            + "                  _NEW_DATE_ _LAST_UPDATE_ ;\n"
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified _LAST_UPDATE_ . } \n"
            + "WHERE { OPTIONAL {" + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.VS.getPrefix() + "term_status ?status ;\n "
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . } }\n";

    public static final String UPDATE_LEXICAL_ENTRY_LANGUAGE
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "DELETE { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "entry " + SparqlPrefix.LEX.getPrefix() + "_ID_ }\n"
            + "WHERE { \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "entry " + SparqlPrefix.LEX.getPrefix() + "_ID_ } \n"
            + "};\n"
            + "INSERT { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "entry " + SparqlPrefix.LEX.getPrefix() + "_ID_ }\n"
            + "WHERE { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "language \"_LANG_\" };\n"
            + "DELETE { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " ;\n"
            + "         " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . } \n"
            + "INSERT { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.RDFS.getPrefix() + "label \"_LABEL_\"@_LANG_ ;\n"
            + "         " + SparqlPrefix.DCT.getPrefix() + "modified _LAST_UPDATE_ . }\n"
            + "WHERE { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " ;\n"
            + "             " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . } \n";

    public static final String UPDATE_FORM
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { lex:_ID_ _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { lex:_ID_ dct:modified ?modified . }\n"
            + "         OPTIONAL { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_ETYMOLOGY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "DELETE { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { lex:_ID_ _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { lex:_ID_ dct:modified ?modified . }\n"
            + "         OPTIONAL { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_ETYMOLOGICAL_LINK
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "DELETE { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { lex:_ID_ _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { lex:_ID_ dct:modified ?modified . }\n"
            + "         OPTIONAL { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_FORM_TYPE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "DELETE { lex:_LEID_ ontolex:lexicalForm lex:_ID_ . \n "
            + "         lex:_LEID_ ontolex:canonicalForm lex:_ID_ . \n "
            + "         lex:_LEID_ ontolex:otherForm lex:_ID_ . \n "
            + "         lex:_LEID_ dct:modified ?modified . } \n"
            + "INSERT { lex:_LEID_ ontolex:_FORM_TYPE_ lex:_ID_ . \n"
            + "         lex:_LEID_ dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { lex:_LEID_ dct:modified ?modified . }\n"
            + "         OPTIONAL { lex:_LEID_ ontolex:lexicalForm lex:_ID_ . }\n "
            + "         OPTIONAL { lex:_LEID_ ontolex:canonicalForm lex:_ID_ . }\n "
            + "         OPTIONAL { lex:_LEID_ ontolex:otherForm lex:_ID_ . }\n"
            + "} ";

    public static final String UPDATE_LEXICAL_SENSE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { lex:_ID_ _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { lex:_ID_ dct:modified ?modified . }\n"
            + "         OPTIONAL { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_LINGUISTIC_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { lex:_ID_ _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { lex:_ID_ dct:modified ?modified . }\n"
            + "         OPTIONAL { lex:_ID_ _RELATION_ _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_GENERIC_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> _RELATION_ _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { <_ID_> _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified . }\n"
            + "         OPTIONAL { <_ID_> _RELATION_ _VALUE_TO_DELETE_ . } }";

    public static final String SYNC_BIBLIOGRAPHY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXBIB.getSparqlPrefix() + "\n"
            + "DELETE {\n"
            + "    lexbib:_IDBIB_ dct:title ?title ;\n"
            + "         dct:date ?date ;\n"
            + "         dct:contributor ?contributor ;\n"
            + "         dct:modified ?modified . }\n"
            + "INSERT {\n"
            + "    lexbib:_IDBIB_ dct:title \"_TITLE_\" ;\n"
            + "         dct:date \"_DATE_\" ;\n"
            + "         dct:contributor \"_CONTRIBUTOR_\" ;\n"
            + "         dct:modified \"_LAST_UPDATE_\" . }\n"
            + "WHERE {\n"
            + "    lexbib:_IDBIB_ dct:title ?title ;\n"
            + "         dct:date ?date ;\n"
            + "         dct:contributor ?contributor ;\n"
            + "         dct:modified ?modified .\n"
            + "}";

}
