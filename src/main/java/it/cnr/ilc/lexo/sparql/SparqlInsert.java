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
public class SparqlInsert {

    public static final String CREATE_LEXICAL_ENTRY
            = SparqlPrefix.DCT + "\n"
            + SparqlPrefix.LEX + "\n"
            + SparqlPrefix.ONTOLEX + "\n"
            + SparqlPrefix.RDF + "\n"
            + SparqlPrefix.RDFS + "\n"
            + SparqlPrefix.LEXINFO + "\n"
            + SparqlPrefix.LOC + "\n"
            + "INSERT DATA {\n"
            + "    lex:[IRI] rdf:type [TYPE] .\n"
            + "    lex:[IRI] lexinfo:partOfSpeech [POS] .\n"
            + "    lex:[IRI] rdfs:label \"[LABEL]\"@[LANG] .\n"
            + "    lex:[IRI] dct:contributor \"[AUTHOR]\" .\n"
            + "    [OPTIONAL_TRIPLES] "
            + "}";

}
