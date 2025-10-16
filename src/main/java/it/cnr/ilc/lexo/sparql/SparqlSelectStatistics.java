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
public class SparqlSelectStatistics {

    public static final String STATISTICS_AUTHORS
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "SELECT ?"
            + SparqlVariable.LABEL
            + " ?"
            + SparqlVariable.LABEL_COUNT
            + " WHERE {\n"
            + "  ?r a inst:_INDEX_ ;\n"
            + "    luc:facetFields \"author\" ;\n"
            + "    luc:facets _:f .\n"
            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
            + "} order by ?" + SparqlVariable.LABEL;

    public static final String STATISTICS_TYPES
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "SELECT ?"
            + SparqlVariable.LABEL
            + " ?"
            + SparqlVariable.LABEL_COUNT
            + " WHERE {\n"
            + "  ?r a inst:lexicalEntryIndex ;\n"
            + "    luc:facetFields \"type\" ;\n"
            + "    luc:facets _:f .\n"
            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
            + "  FILTER (regex(str(?" + SparqlVariable.LABEL + "), \"lexical entry|word|multi-word expression|affix|etymon|cognate\"))"
            + "}";

//    public static final String STATISTICS_LANGUAGES
//            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
//            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
//            + "SELECT ?"
//            + SparqlVariable.LABEL
//            + " ?"
//            + SparqlVariable.LABEL_COUNT
//            + " WHERE {\n"
//            + "  ?r a inst:_INDEX_ ;\n"
//            + "    luc:facetFields \"_LANG_FIELD_\" ;\n"
//            + "    luc:facets _:f .\n"
//            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
//            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
//            + "} order by ?" + SparqlVariable.LABEL;
    public static final String STATISTICS_LANGUAGES
            = SparqlPrefix.LEXICOG.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LABEL + " (count(distinct ?entry) as ?" + SparqlVariable.LABEL_COUNT + ") WHERE {\n"
            + "  ?dict a lexicog:LexicographicResource ;\n"
            + "        dct:language ?" + SparqlVariable.LABEL + " .\n"
            + "  OPTIONAL { ?dict lexicog:entry ?entry }\n"
            + "} GROUP BY ?" + SparqlVariable.LABEL + "";

    public static final String STATISTICS_POS
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "SELECT ?"
            + SparqlVariable.LABEL
            + " ?"
            + SparqlVariable.LABEL_COUNT
            + " WHERE {\n"
            + "  ?r a inst:lexicalEntryIndex ;\n"
            + "    luc:facetFields \"pos\" ;\n"
            + "    luc:facets _:f .\n"
            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
            + "  FILTER NOT EXISTS { \n"
            + "      FILTER (regex(str(?" + SparqlVariable.LABEL + "), \"word|multi-word expression|affix\")) . \n"
            + "  }\n"
            + "}";

    public static final String STATISTICS_STATUS
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "SELECT ?"
            + SparqlVariable.LABEL
            + " ?"
            + SparqlVariable.LABEL_COUNT
            + " WHERE {\n"
            + "  ?r a inst:_INDEX_ ;\n"
            + "    luc:facetFields \"status\" ;\n"
            + "    luc:facets _:f .\n"
            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
            + "  FILTER(regex(str(?" + SparqlVariable.LABEL + "),\"working|reviewed|completed\"))\n"
            + "} order by ?" + SparqlVariable.LABEL;

    public static final String STATISTICS_LANGUAGES_LIST
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICON_LANGUAGE + "\n"
            + "WHERE { ?" + SparqlVariable.LEXICON + " a " + SparqlPrefix.LIME.getPrefix() + "Lexicon ;\n"
            + "                 " + SparqlPrefix.LIME.getPrefix() + "language ?" + SparqlVariable.LEXICON_LANGUAGE + " . }\n"
            + "ORDER BY ?" + SparqlVariable.LEXICON_LANGUAGE + "";

    public static final String STATISTICS_DICT_LANGUAGES_LIST
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXICOG.getSparqlPrefix() + "\n"
            + "SELECT DISTINCT ?" + SparqlVariable.DICT_LANGUAGE + "\n"
            + "WHERE { ?" + SparqlVariable.DICT_ELEMENT + " a " + SparqlPrefix.LEXICOG.getPrefix() + "LexicographicResource ;\n"
            + "                 " + SparqlPrefix.DCT.getPrefix() + "language ?" + SparqlVariable.DICT_LANGUAGE + " . }\n"
            + "ORDER BY ?" + SparqlVariable.DICT_LANGUAGE + "";

    public static final String METADATA
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LOC.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTITY + " ?" + SparqlVariable.CREATION_DATE + " ?" + SparqlVariable.COMPLETION_DATE + " ?" + SparqlVariable.REVISION_DATE
            + " ?" + SparqlVariable.LAST_UPDATE + " ?" + SparqlVariable.NOTE + " ?" + SparqlVariable.CONFIDENCE
            + " ?" + SparqlVariable.LEXICAL_ENTRY_STATUS + " ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR
            + " ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR + " ?" + SparqlVariable.SOURCE + "\n"
            + "WHERE {\n"
            + "    BIND (<_ID_> AS ?" + SparqlVariable.LEXICAL_ENTITY + ")  .\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTITY + " dct:created ?" + SparqlVariable.CREATION_DATE + "} .\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTITY + " lexinfo:confidence ?confidence . }\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTITY + " dct:dateSubmitted ?" + SparqlVariable.COMPLETION_DATE + "} .\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTITY + " dct:dateAccepted ?" + SparqlVariable.REVISION_DATE + "} .\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTITY + " dct:modified ?" + SparqlVariable.LAST_UPDATE + "} .\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTITY + " skos:note ?" + SparqlVariable.NOTE + " . }\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTITY + " vs:term_status ?" + SparqlVariable.LEXICAL_ENTRY_STATUS + " . }\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTITY + " dct:source ?" + SparqlVariable.SOURCE + " . }\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTITY + " dct:creator ?" + SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR + " . }\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTITY + " dct:author ?" + SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR + " . }\n"
            + "    OPTIONAL {?" + SparqlVariable.LEXICAL_ENTITY + " loc:rev ?" + SparqlVariable.LEXICAL_ENTRY_REVISOR + " . }\n"
            + "}";

}
