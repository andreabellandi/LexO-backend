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
            = SparqlPrefix.DCT + "\n"
            + SparqlPrefix.LEX + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.RDFS + "\n"
            + "INSERT DATA {\n"
            + "    lex:[ID] a ontolex:LexicalEntry ;\n"
            + "                   rdfs:label \"[LABEL]\" ;\n"
            + "                   dc:contributor \"[AUTHOR]\" ;\n"
            + "                   dc:valid \"working\" ;\n"
            + "                   dc:created \"[CREATED]\" ;\n"
            + "                   dc:modified \"[MODIFIED]\" . \n"
            + "}";

}
