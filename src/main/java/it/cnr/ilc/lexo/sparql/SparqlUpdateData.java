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
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ ;\n "
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . } \n"
            + "INSERT { <_ID_> <_RELATION_> _VALUE_TO_INSERT_ ;\n"
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified .} \n"
            + "      OPTIONAL { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_LEXICAL_ENTRY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { <_ID_> <_RELATION_> _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified .} \n"
            + "         OPTIONAL { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_DICTIONARY_ENTRY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { <_ID_> <_RELATION_> _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified .} \n"
            + "         OPTIONAL { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_LEXICAL_ENTRY_BACKWARDING_STATUS
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> " + SparqlPrefix.VS.getPrefix() + "term_status ?status ;\n "
            + "                  _CURRENT_ROLE_ ?role ;\n"
            + "                  _CURRENT_DATE_ ?date ;\n"
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . }\n"
            + "INSERT { <_ID_> " + SparqlPrefix.VS.getPrefix() + "term_status _STATUS_ ;\n "
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified _LAST_UPDATE_ . } \n"
            + "WHERE { <_ID_> " + SparqlPrefix.VS.getPrefix() + "term_status ?status ;\n "
            + "                  _CURRENT_ROLE_ ?role ;\n"
            + "                  _CURRENT_DATE_ ?date ;\n"
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified ?modified .  }\n";

    public static final String UPDATE_LEXICAL_ENTRY_FOREWARDING_STATUS
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> " + SparqlPrefix.VS.getPrefix() + "term_status ?status ;\n "
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . }\n"
            + "INSERT { <_ID_> " + SparqlPrefix.VS.getPrefix() + "term_status _STATUS_ ;\n "
            + "                  _NEW_ROLE_ _USER_ ;\n"
            + "                  _NEW_DATE_ _LAST_UPDATE_ ;\n"
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified _LAST_UPDATE_ . } \n"
            + "WHERE { OPTIONAL { <_ID_> " + SparqlPrefix.VS.getPrefix() + "term_status ?status ;\n "
            + "                  " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . } }\n";

    public static final String UPDATE_LEXICAL_ENTRY_LANGUAGE
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "DELETE { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "entry <_ID_> }\n"
            + "WHERE { \n"
            + "    OPTIONAL { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "entry <_ID_> } \n"
            + "};\n"
            + "INSERT { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "entry <_ID_> }\n"
            + "WHERE { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "language \"_LANG_\" };\n"
            + "DELETE { <_ID_> " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " ;\n"
            + "         " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . } \n"
            + "INSERT { <_ID_> " + SparqlPrefix.RDFS.getPrefix() + "label \"_LABEL_\"@_LANG_ ;\n"
            + "         " + SparqlPrefix.DCT.getPrefix() + "modified _LAST_UPDATE_ . }\n"
            + "WHERE { <_ID_> " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " ;\n"
            + "             " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . } \n";

    public static final String UPDATE_DICTIONARY_ENTRY_LANGUAGE
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXICOG.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "DELETE { ?" + SparqlVariable.DICT_ELEMENT + " " + SparqlPrefix.LEXICOG.getPrefix() + "entry <_ID_> }\n"
            + "WHERE { \n"
            + "    OPTIONAL { ?" + SparqlVariable.DICT_ELEMENT + " " + SparqlPrefix.LEXICOG.getPrefix() + "entry <_ID_> } \n"
            + "};\n"
            + "INSERT { ?" + SparqlVariable.DICT_ELEMENT + " " + SparqlPrefix.LEXICOG.getPrefix() + "entry <_ID_> }\n"
            + "WHERE { ?" + SparqlVariable.DICT_ELEMENT + " " + SparqlPrefix.DCT.getPrefix() + "language \"_LANG_\" };\n"
            + "DELETE { <_ID_> " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " ;\n"
            + "         " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . } \n"
            + "INSERT { <_ID_> " + SparqlPrefix.RDFS.getPrefix() + "label \"_LABEL_\"@_LANG_ ;\n"
            + "         " + SparqlPrefix.DCT.getPrefix() + "modified _LAST_UPDATE_ . }\n"
            + "WHERE { <_ID_> " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " ;\n"
            + "             " + SparqlPrefix.DCT.getPrefix() + "modified ?modified . } \n";

    public static final String UPDATE_FORM
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { <_ID_> <_RELATION_> _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified . }\n"
            + "         OPTIONAL { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_ETYMOLOGY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { <_ID_> <_RELATION_> _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified . }\n"
            + "         OPTIONAL { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_ETYMOLOGICAL_LINK
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { <_ID_> <_RELATION_> _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified . }\n"
            + "         OPTIONAL { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_FORM_TYPE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "DELETE { <_LEID_> ontolex:lexicalForm <_ID_> . \n "
            + "         <_LEID_> ontolex:canonicalForm <_ID_> . \n "
            + "         <_LEID_> ontolex:otherForm <_ID_> . \n "
            + "         <_LEID_> dct:modified ?modified . } \n"
            + "INSERT { <_LEID_> <_FORM_TYPE_> <_ID_> . \n"
            + "         <_LEID_> dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_LEID_> dct:modified ?modified . }\n"
            + "         OPTIONAL { <_LEID_> ontolex:lexicalForm <_ID_> . }\n "
            + "         OPTIONAL { <_LEID_> ontolex:canonicalForm <_ID_> . }\n "
            + "         OPTIONAL { <_LEID_> ontolex:otherForm <_ID_> . }\n"
            + "} ";

    public static final String UPDATE_LEXICAL_SENSE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { <_ID_> <_RELATION_> _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified . }\n"
            + "         OPTIONAL { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ . } }";

    public static final String UPDATE_LINGUISTIC_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> <_RELATION_> <_VALUE_TO_DELETE_> ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { <_ID_> <_RELATION_> <_VALUE_TO_INSERT_> ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified . }\n"
            + "         OPTIONAL { <_ID_> <_RELATION_> <_VALUE_TO_DELETE_> . } }";

    public static final String UPDATE_GENERIC_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { <_ID_> <_RELATION_> _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified . }\n"
            + "         OPTIONAL { <_ID_> <_RELATION_> _VALUE_TO_DELETE_ . } }";

    public static final String SYNC_BIBLIOGRAPHY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "DELETE {\n"
            + "    <_IDBIB_> dct:title ?title ;\n"
            + "         dct:date ?date ;\n"
            + "         dct:contributor ?contributor ;\n"
            + "         dct:modified ?modified . }\n"
            + "INSERT {\n"
            + "    <_IDBIB_> dct:title \"_TITLE_\" ;\n"
            + "         dct:date \"_DATE_\" ;\n"
            + "         dct:contributor \"_CONTRIBUTOR_\" ;\n"
            + "         dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {\n"
            + "    <_IDBIB_> dct:title ?title ;\n"
            + "         dct:date ?date ;\n"
            + "         dct:contributor ?contributor ;\n"
            + "         dct:modified ?modified .\n"
            + "}";

    public static final String UPDATE_COMPONENT_POSITION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> rdf:_CURR_POS <_IDCOMPONENT_> ;\n "
            + "                  dct:modified ?modified . } \n"
            + "INSERT { <_ID_> rdf:_POSITION <_IDCOMPONENT_> ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified . }\n"
            + "         OPTIONAL { <_ID_> rdf:_CURR_POS <_IDCOMPONENT_> . } }";

    public static final String ADD_COMPONENT_MEMBER
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "INSERT { <_ID_> rdfs:member <_IDCOMPONENT_> ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n";

    public static final String ADD_SENSE_TO_LEXICOGRAPHIC_COMPONENT
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
             + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
             + SparqlPrefix.LEXICOG.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> dct:modified ?modified . } \n"
            + "INSERT { \n"
            + "    <_LCID_> lexicog:describes <_SENSEID_> .\n"
            + "    <_ID_> rdf:__POSITION_ <_LCID_> .\n"
            + "    <_ID_> dct:modified _LAST_UPDATE_ . \n"
            + "}\n "
            + "WHERE { <_ID_> dct:modified ?modified . } \n";

}
