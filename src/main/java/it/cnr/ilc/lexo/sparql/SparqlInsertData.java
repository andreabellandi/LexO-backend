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
public class SparqlInsertData {

    public static final String CREATE_LEXICAL_ENTRY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "INSERT DATA {\n"
            + "    lex:[ID] a ontolex:LexicalEntry ;\n"
            + "                   rdfs:label \"[LABEL]\" ;\n"
            + "                   dct:creator \"[AUTHOR]\" ;\n"
            + "                   vs:term_status \"working\" ;\n"
            + "                   dct:created \"[CREATED]\" ;\n"
            + "                   dct:modified \"[MODIFIED]\" . \n"
            + "}";

    public static final String CREATE_LEXICAL_ENTRY_LANGUAGE
            = SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + "INSERT { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "entry " + SparqlPrefix.LEX.getPrefix() + "_ID_ }\n"
            + "WHERE { ?" + SparqlVariable.LEXICON + " " + SparqlPrefix.LIME.getPrefix() + "language \"_LANG_\" . };\n"
            + "DELETE { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " }\n"
            + "INSERT { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.RDFS.getPrefix() + "label \"_LABEL_\"@_LANG_ } \n"
            + "WHERE { " + SparqlPrefix.LEX.getPrefix() + "_ID_ " + SparqlPrefix.RDFS.getPrefix() + "label ?" + SparqlVariable.LABEL + " } ";
    
    public static final String CREATE_FORM
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "INSERT DATA {\n"
            + "    lex:[ID] a ontolex:Form ;\n"
            + "                   ontolex:writtenRep \"[LABEL]\"@_LANG_ ;\n"
            + "                   dct:creator \"[AUTHOR]\" ;\n"
            + "                   dct:created \"[CREATED]\" ;\n"
            + "                   dct:modified \"[MODIFIED]\" . \n"
            + "    lex:[LE_ID] ontolex:lexicalForm lex:[ID] .\n"
            + "}";
}
