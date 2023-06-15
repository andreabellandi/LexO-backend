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
            + "    ?inst a ?range .\n"
            + "OPTIONAL { ?inst rdfs:label ?strInstLabel }\n"
            + "OPTIONAL { ?inst rdfs:comment ?_" + SparqlVariable.INST_COMMENT + " }\n"
            + "BIND(COALESCE(?_" + SparqlVariable.INST_COMMENT + ", \"No comment.\") AS ?" + SparqlVariable.INST_COMMENT + ")\n"
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

    public static final String LEXICAL_REL
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.PARENT + " ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT + " where { \n"
            + "  VALUES ?rootProperty { <_PROPERTY_> } \n"
            + "  ?rootProperty ^rdfs:subPropertyOf* ?" + SparqlVariable.PARENT + " .\n"
            + "  ?" + SparqlVariable.PARENT + " ^rdfs:subPropertyOf ?" + SparqlVariable.PROPERTY_NAME + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.PROPERTY_NAME + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.PROPERTY_NAME + " rdfs:comment ?" + SparqlVariable.PROPERTY_COMMENT + " }\n"
            + "} ORDER BY ?rootProperty";

    public static final String SENSE_REL
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.PARENT + " ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT + " where { \n"
            + "  VALUES ?rootProperty { <_PROPERTY_> } \n"
            + "  ?rootProperty ^rdfs:subPropertyOf* ?" + SparqlVariable.PARENT + " .\n"
            + "  ?" + SparqlVariable.PARENT + " ^rdfs:subPropertyOf ?" + SparqlVariable.PROPERTY_NAME + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.PROPERTY_NAME + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.PROPERTY_NAME + " rdfs:comment ?" + SparqlVariable.PROPERTY_COMMENT + " }\n"
            + "} ORDER BY ?rootProperty";
    
    public static final String FORM_REL
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
             + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.PARENT + " ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT + " where { \n"
            + "  VALUES (?" + SparqlVariable.PARENT + ") { (lexinfo:formCaseVariant)\n"
            + "                         (lexinfo:formDegreeVariant)\n"
            + "                         (lexinfo:formMoodVariant)\n"
            + "                         (lexinfo:formNegativeVariant)\n"
            + "                         (lexinfo:formNumberVariant)\n"
            + "                         (lexinfo:formPersonVariant)\n"
            + "                         (lexinfo:formTenseVariant) } \n"
            + "  ?" + SparqlVariable.PARENT + " ^rdfs:subPropertyOf ?" + SparqlVariable.PROPERTY_NAME + " .\n"
            + "    OPTIONAL { ?" + SparqlVariable.PROPERTY_NAME + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.PROPERTY_NAME + " rdfs:comment ?" + SparqlVariable.PROPERTY_COMMENT + " }\n"
            + "} ORDER BY ?" + SparqlVariable.PARENT + "";

    public static final String USAGE_PROPERTIES
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.PROPERTY_NAME + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT + "\n"
            + "(GROUP_CONCAT(concat(str(?inst),\"<>\",str(?strInstLabel),\"<>\",str(?instComment));SEPARATOR=\"---\") AS ?" + SparqlVariable.USAGE_VALUES + ")\n"
            + "WHERE { \n"
            + "  ?" + SparqlVariable.PROPERTY_NAME + " rdfs:subPropertyOf ontolex:usage ;\n"
            + "                rdfs:range ?range .\n"
            + "    ?inst rdf:type ?range .\n"
            + "    OPTIONAL { ?" + SparqlVariable.PROPERTY_NAME + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.PROPERTY_NAME + " rdfs:comment ?" + SparqlVariable.PROPERTY_COMMENT + " }\n"
            + "    OPTIONAL { ?inst rdfs:label ?strInstLabel }\n"
            + "    OPTIONAL { ?inst rdfs:comment ?instComment }\n"
            + "} \n"
            + "GROUP BY ?" + SparqlVariable.PROPERTY_NAME + " ?range ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT + "\n"
            + "ORDER BY ?" + SparqlVariable.PROPERTY_NAME;

    public static final String CATEGORY_FOR_SENSE
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT + "\n"
            + "(GROUP_CONCAT(concat(str(?inst),\"<>\",str(?strInstLabel),\"<>\",str(?instComment));SEPARATOR=\"---\") AS ?" + SparqlVariable.VALUE + ")\n"
            + "WHERE {\n"
            + "    VALUES (?" + SparqlVariable.TYPE + ") { \n"
            + "        (lexinfo:TemporalQualifier)\n"
            + "        (lexinfo:Dating)\n"
            + "        (lexinfo:Register)\n"
            + "        (lexinfo:Frequency) } \n"
            + "    ?inst a ?" + SparqlVariable.TYPE + "\n"
            + "    OPTIONAL { ?inst rdfs:label ?strInstLabel }\n"
            + "    OPTIONAL { ?inst rdfs:comment ?instComment }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TYPE + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TYPE + " rdfs:label ?" + SparqlVariable.PROPERTY_COMMENT + " }\n"
            + "} GROUP BY ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT;

    public static final String CATEGORY_FOR_LEXICALENTRY
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT + "\n"
            + "(GROUP_CONCAT(concat(str(?inst),\"<>\",str(?strInstLabel),\"<>\",str(?instComment));SEPARATOR=\"---\") AS ?" + SparqlVariable.VALUE + ")\n"
            + "WHERE {\n"
            + "    VALUES (?" + SparqlVariable.TYPE + ") { \n"
            + "        (lexinfo:Dating)\n"
            + "        (lexinfo:Frequency)\n"
            + "        (lexinfo:Register)\n"
            + "        (lexinfo:TemporalQualifier)\n"
            + "        (lexinfo:TermElement)\n"
            + "        (lexinfo:TermType)} \n"
            + "    ?inst a ?" + SparqlVariable.TYPE + "\n"
            + "    OPTIONAL { ?inst rdfs:label ?strInstLabel }\n"
            + "    OPTIONAL { ?inst rdfs:comment ?instComment }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TYPE + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TYPE + " rdfs:label ?" + SparqlVariable.PROPERTY_COMMENT + " }\n"
            + "} GROUP BY ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT;
    
    public static final String CATEGORY_FOR_FORM
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT + "\n"
            + "(GROUP_CONCAT(concat(str(?inst),\"<>\",str(?strInstLabel),\"<>\",str(?instComment));SEPARATOR=\"---\") AS ?" + SparqlVariable.VALUE + ")\n"
            + "WHERE {\n"
            + "    VALUES (?" + SparqlVariable.TYPE + ") { \n"
            + "        (lexinfo:Dating)\n"
            + "        (lexinfo:Definiteness)\n"
            + "        (lexinfo:Degree)\n"
            + "        (lexinfo:Finiteness)\n"
            + "        (lexinfo:Frequency)\n"
            + "        (lexinfo:TemporalQualifier) \n"
            + "        (lexinfo:TermType) \n"
            + "        (lexinfo:Voice) } \n"
            + "    ?inst a ?" + SparqlVariable.TYPE + "\n"
            + "    OPTIONAL { ?inst rdfs:label ?strInstLabel }\n"
            + "    OPTIONAL { ?inst rdfs:comment ?instComment }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TYPE + " rdfs:label ?" + SparqlVariable.LABEL + " }\n"
            + "    OPTIONAL { ?" + SparqlVariable.TYPE + " rdfs:label ?" + SparqlVariable.PROPERTY_COMMENT + " }\n"
            + "} GROUP BY ?" + SparqlVariable.TYPE + " ?" + SparqlVariable.LABEL + " ?" + SparqlVariable.PROPERTY_COMMENT;
}
