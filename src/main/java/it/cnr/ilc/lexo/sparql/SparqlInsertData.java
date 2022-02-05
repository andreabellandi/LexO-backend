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

    public static final String CREATE_LEXICON_LANGUAGE
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "INSERT DATA { " + SparqlPrefix.LEX.getPrefix() + "_ID_ a " + SparqlPrefix.LIME.getPrefix() + "Lexicon ;\n"
            + SparqlPrefix.LIME.getPrefix() + "language \"_LANG_\" ;\n"
            + SparqlPrefix.LIME.getPrefix() + "linguisticCatalog <http://www.lexinfo.net/ontologies/3.0/lexinfo> ;\n"
            + SparqlPrefix.DCT.getPrefix() + "creator \"_AUTHOR_\" ;\n"
            + SparqlPrefix.DCT.getPrefix() + "created \"_CREATED_\" ;\n"
            + SparqlPrefix.DCT.getPrefix() + "modified \"_MODIFIED_\" . }";

    public static final String CREATE_FORM
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "INSERT DATA {\n"
            + "    lex:_ID_ a ontolex:Form ;\n"
            + "                   ontolex:writtenRep \"_LABEL_\"@_LANG_ ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "    lex:_LEID_ ontolex:lexicalForm lex:_ID_ .\n"
            + "}";

    public static final String CREATE_ETYMOLOGY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "INSERT DATA {\n"
            + "    lex:_ID_ a ety:Etymology ;\n"
            + "                   rdfs:label \"Etymology of: _LABEL_\" ;\n"
            + "                   lexinfo:confidence 1 ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "    lex:_LEID_ ety:etymology lex:_ID_ .\n"
            + "}";

    public static final String CREATE_ETYMOLOGICAL_LINK
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "INSERT DATA {\n"
            + "    lex:_ID_ a ety:EtyLink ;\n"
            + "                   ety:etyLinkType \"inheritance\" ;\n"
            + "                   ety:etyTarget lex:_LEID_ ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "    lex:_ETID_ ety:hasEtyLink lex:_ID_ .\n"
            + "}";

    public static final String CREATE_LEXICAL_SENSE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "INSERT DATA {\n"
            + "    lex:_ID_ a ontolex:LexicalSense ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "    lex:_LEID_ ontolex:sense lex:_ID_ .\n"
            + "}";

    public static final String CREATE_COMPONENT
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "INSERT DATA {\n"
            + "    lex:_ID_ a decomp:Composition ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "    lex:_LEID_ decomp:constituent lex:_ID_ .\n"
            + "}";

    public static final String CREATE_LINGUISTIC_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { lex:_ID_ dct:modified ?modified . } \n"
            + "INSERT { lex:_ID_ _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { lex:_ID_ dct:modified ?modified . } }";

    public static final String CREATE_GENERIC_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> dct:modified ?modified . } \n"
            + "INSERT { <_ID_> _RELATION_ _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified . } }";

    public static final String CREATE_BIBLIOGRAPHIC_REFERENCE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXBIB.getSparqlPrefix() + "\n"
            + "INSERT DATA {\n"
            + "    lexbib:_ID_ a rdf:Description ; \n"
            + "        dct:publisher \"_KEY_\" ;\n"
            + "        _OPTIONAL_"
            + "        dct:title \"_TITLE_\" ;\n"
            + "        dct:date \"_DATE_\" ;\n"
            + "        dct:contributor \"_AUTHORS_\" ;\n  "
            + "        dct:creator \"_AUTHOR_\" ;\n"
            + "        dct:created \"_CREATED_\" ;\n"
            + "        dct:modified \"_MODIFIED_\" . \n"
            + "    lex:_LEID_ dct:references lexbib:_ID_ .\n"
            + "}";

    public static final String CREATE_COGNATE_TYPE
            = SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + "INSERT DATA {  _ID_ a ety:Cognate . }";
}
