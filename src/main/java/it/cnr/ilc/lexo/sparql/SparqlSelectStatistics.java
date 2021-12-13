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
            + "  ?r a inst:lexicalEntryIndex ;\n"
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
            + "  FILTER (regex(str(?" + SparqlVariable.LABEL + "), \"word|multi-word expression|affix|etymon|cognate\"))"
            + "}";

    public static final String STATISTICS_LANGUAGES
            = SparqlPrefix.INST.getSparqlPrefix() + "\n"
            + SparqlPrefix.LUC.getSparqlPrefix() + "\n"
            + "SELECT ?"
            + SparqlVariable.LABEL
            + " ?"
            + SparqlVariable.LABEL_COUNT
            + " WHERE {\n"
            + "  ?r a inst:lexicalEntryIndex ;\n"
            + "    luc:facetFields \"writtenFormLanguage\" ;\n"
            + "    luc:facets _:f .\n"
            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
            + "} order by ?" + SparqlVariable.LABEL;

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
            + "  ?r a inst:lexicalEntryIndex ;\n"
            + "    luc:facetFields \"status\" ;\n"
            + "    luc:facets _:f .\n"
            + "  _:f luc:facetValue ?" + SparqlVariable.LABEL + " .\n"
            + "  _:f luc:facetCount ?" + SparqlVariable.LABEL_COUNT + " .\n"
            + "} order by ?" + SparqlVariable.LABEL;

    public static final String STATISTICS_LANGUAGES_LIST
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICON_LANGUAGE + "\n"
            + "WHERE { ?" + SparqlVariable.LEXICON + " a " + SparqlPrefix.LIME.getPrefix() + "Lexicon ;\n"
            + "                 " + SparqlPrefix.LIME.getPrefix() + "language ?" + SparqlVariable.LEXICON_LANGUAGE + " . }\n"
            + "ORDER BY ?" + SparqlVariable.LEXICON_LANGUAGE + "";
}
