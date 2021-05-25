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
            + "(GROUP_CONCAT(concat(str(?strInst),\":\",str(?strInstLabel),\"<>\",str(?instComment));SEPARATOR=\";\") AS ?" + SparqlVariable.MORPHOLOGY_TRAIT_VALUE + ")\n"
            + "WHERE { \n"
            + "	?tn rdfs:subPropertyOf lexinfo:morphosyntacticProperty ;\n"
            + "       rdfs:range ?range ;\n"
            + "       rdfs:label ?" + SparqlVariable.LABEL + " ;\n"
            + "       rdfs:comment ?" + SparqlVariable.PROPERTY_COMMENT + " .\n"
            + "    ?inst a ?range ;\n"
            + "          rdfs:label ?strInstLabel ;\n"
            + "          rdfs:comment ?" + SparqlVariable.INST_COMMENT + " .\n"
            + "    BIND(strafter(str(?inst),str(lexinfo:)) as ?strInst)\n"
            + "    BIND(strafter(str(?tn),str(lexinfo:)) as ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + ")\n"
            + "} GROUP BY ?" 
            + SparqlVariable.MORPHOLOGY_TRAIT_NAME + " ?" 
            + SparqlVariable.LABEL 
            + " ?" + SparqlVariable.PROPERTY_COMMENT 
            + " ?range\n"
            + "ORDER BY ?" + SparqlVariable.MORPHOLOGY_TRAIT_NAME + "";
}
