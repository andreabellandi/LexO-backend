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
public class SparqlSelectLexinfoData {

    public static final String MORPHOLOGY
            = SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "SELECT ?"
            + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?"
            + SparqlVariable.PROPERTY_COMMENT + " ?"
            + SparqlVariable.LABEL + " \n"
            + "(GROUP_CONCAT(concat(str(?inst),\"<>\",str(?strInstLabel),\"<>\",str(?instComment));SEPARATOR=\"---\") AS ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + ")\n"
            + "WHERE { \n"
            + "	?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " rdfs:subPropertyOf lexinfo:morphosyntacticProperty ;\n"
            + "       rdfs:range ?range ;\n"
            + "       rdfs:label ?" + SparqlVariable.LABEL + " ;\n"
            + "       rdfs:comment ?" + SparqlVariable.PROPERTY_COMMENT + " .\n"
            + "    ?inst a ?range ;\n"
            + "          rdfs:label ?strInstLabel ;\n"
            + "          rdfs:comment ?" + SparqlVariable.INST_COMMENT + " .\n"
            //            + "    BIND(strafter(str(?inst),str(lexinfo:)) as ?strInst)\n"
            //            + "    BIND(strafter(str(?tn),str(lexinfo:)) as ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + ")\n"
            + "} GROUP BY ?"
            + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?"
            + SparqlVariable.LABEL
            + " ?" + SparqlVariable.PROPERTY_COMMENT
            + " ?range\n"
            + "ORDER BY ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "";

    public static final String REPRESENTATION
            = SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.PROPERTY_COMMENT + " ?" + SparqlVariable.LABEL + "\n"
            + "WHERE { \n"
            + "	?" + SparqlVariable.PROPERTY_NAME + " rdfs:subPropertyOf ontolex:representation ;\n"
            + "       rdfs:label ?" + SparqlVariable.LABEL + " ;\n"
            + "       rdfs:comment ?" + SparqlVariable.PROPERTY_COMMENT + " .\n"
            + "    FILTER(!regex(str(?" + SparqlVariable.PROPERTY_NAME + "), \"ontolex\"))\n"
            + "}\n"
            + "ORDER BY ?" + SparqlVariable.PROPERTY_NAME;

    public static final String SENSE_DEFINITION
            = SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.PROPERTY_COMMENT + " ?" + SparqlVariable.LABEL + "\n"
            + "WHERE { \n"
            + "	?" + SparqlVariable.PROPERTY_NAME + " rdfs:subPropertyOf skos:definition ;\n"
            + "       rdfs:label ?" + SparqlVariable.LABEL + " ;\n"
            + "       rdfs:comment ?" + SparqlVariable.PROPERTY_COMMENT + " .\n"
            + "}\n"
            + "ORDER BY ?" + SparqlVariable.PROPERTY_NAME;
}
