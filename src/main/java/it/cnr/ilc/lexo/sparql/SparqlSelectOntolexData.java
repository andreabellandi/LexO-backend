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
public class SparqlSelectOntolexData {

    public static final String LEXICAL_ENTRY_TYPE
            = SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY_TYPE + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.CLASS_COMMENT + " \n"
            + "WHERE {\n"
            + "	?" + SparqlVariable.LEXICAL_ENTRY_TYPE + " " + SparqlPrefix.RDFS.getPrefix() + "subClassOf " + SparqlPrefix.ONTOLEX.getPrefix() + "LexicalEntry ;\n"
            + "       " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " .\n"
            + "       OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY_TYPE + " " + SparqlPrefix.RDFS.getPrefix() + "comment ?" + SparqlVariable.CLASS_COMMENT + " }\n"
            + "    FILTER(STRSTARTS(str(?" + SparqlVariable.LEXICAL_ENTRY_TYPE + "), str(" + SparqlPrefix.ONTOLEX.getPrefix() + ") ))\n"
            + "}";

    public static final String ETYMOLOGICAL_ENTRY_TYPE
            = SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SESAME.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICAL_ENTRY_TYPE + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.CLASS_COMMENT + " \n"
            + "WHERE {\n"
            + "	?" + SparqlVariable.LEXICAL_ENTRY_TYPE + " " + SparqlPrefix.SESAME.getPrefix() + "directSubClassOf " + SparqlPrefix.ONTOLEX.getPrefix() + "LexicalEntry ;\n"
            + "       " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " .\n"
            + "       OPTIONAL { ?" + SparqlVariable.LEXICAL_ENTRY_TYPE + " " + SparqlPrefix.RDFS.getPrefix() + "comment ?" + SparqlVariable.CLASS_COMMENT + " }\n"
            + "    FILTER(STRSTARTS(str(?" + SparqlVariable.LEXICAL_ENTRY_TYPE + "), str(" + SparqlPrefix.ETY.getPrefix() + ") ))\n"
            + "}";

    public static final String FORM_TYPE
            = SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.FORM_TYPE + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT + " \n"
            + "WHERE {\n"
            + "	?" + SparqlVariable.FORM_TYPE + " " + SparqlPrefix.RDFS.getPrefix() + "subPropertyOf " + SparqlPrefix.ONTOLEX.getPrefix() + "lexicalForm ;\n"
            + "       " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " .\n"
            + "       OPTIONAL { ?" + SparqlVariable.FORM_TYPE + " " + SparqlPrefix.RDFS.getPrefix() + "comment ?" + SparqlVariable.PROPERTY_COMMENT + " }\n"
            //        c   + "    FILTER(STRSTARTS(str(?" + SparqlVariable.LEXICAL_ENTRY_TYPE + "), str(" + SparqlPrefix.ONTOLEX.getPrefix() + ") ))\n"
            + "}";

    public static final String TRANSLATION_TYPE
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.TRANSLATION_TYPE + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT + " \n"
            + "{ \n"
            + "    { ?" + SparqlVariable.TRANSLATION_TYPE + " rdfs:label \"direct equivalent\"@en ;\n"
            + "    	rdfs:label ?" + SparqlVariable.LABEL + " ;\n"
            + "        rdfs:comment ?" + SparqlVariable.PROPERTY_COMMENT + " . }\n"
            + "    UNION\n"
            + "    { ?" + SparqlVariable.TRANSLATION_TYPE + " rdfs:label \"cultural equivalent\"@en ;\n"
            + "    	rdfs:label ?" + SparqlVariable.LABEL + " ;\n"
            + "        rdfs:comment ?" + SparqlVariable.PROPERTY_COMMENT + " . }\n"
            + "    UNION\n"
            + "    { ?" + SparqlVariable.TRANSLATION_TYPE + " rdfs:label \"lexical equivalent\"@en ;\n"
            + "    	rdfs:label ?" + SparqlVariable.LABEL + " ;\n"
            + "        rdfs:comment ?" + SparqlVariable.PROPERTY_COMMENT + " . }\n"
            + "}\n"
            + "";

    public static final String REPRESENTATION
            = SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.FORM_REPRESENTATION + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT + " \n"
            + "WHERE {\n"
            + "	?" + SparqlVariable.FORM_REPRESENTATION + " " + SparqlPrefix.RDFS.getPrefix() + "subPropertyOf " + SparqlPrefix.ONTOLEX.getPrefix() + "representation ;\n"
            + "       " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " .\n"
            + "       OPTIONAL { ?" + SparqlVariable.FORM_REPRESENTATION + " " + SparqlPrefix.RDFS.getPrefix() + "comment ?" + SparqlVariable.PROPERTY_COMMENT + " }\n"
            + "       FILTER(!regex(str(?" + SparqlVariable.FORM_REPRESENTATION + "), \"representation\"))\n"
            + "       FILTER(!regex(str(?" + SparqlVariable.FORM_REPRESENTATION + "), \"lexinfo\"))\n"
            + "}";

}
