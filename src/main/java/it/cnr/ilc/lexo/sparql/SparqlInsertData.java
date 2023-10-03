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
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <[ID]> a ontolex:LexicalEntry ;\n"
            + "                   rdfs:label \"[LABEL]\" ;\n"
            + "                   dct:creator \"[AUTHOR]\" ;\n"
            + "                   vs:term_status \"working\" ;\n"
            + "                   dct:created \"[CREATED]\" ;\n"
            + "                   dct:modified \"[MODIFIED]\" . \n"
            + "}";

    public static final String CREATE_LEXICON_LANGUAGE
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA { \n"
            + "<_ID_> a " + SparqlPrefix.LIME.getPrefix() + "Lexicon ;\n"
            + SparqlPrefix.LIME.getPrefix() + "language \"_LANG_\" ;\n"
            + SparqlPrefix.LIME.getPrefix() + "linguisticCatalog <http://www.lexinfo.net/ontologies/3.0/lexinfo> ;\n"
            + SparqlPrefix.DCT.getPrefix() + "creator \"_AUTHOR_\" ;\n"
            + SparqlPrefix.DCT.getPrefix() + "created \"_CREATED_\" ;\n"
            + SparqlPrefix.DCT.getPrefix() + "modified \"_MODIFIED_\" . \n"
            + "}";

    public static final String CREATE_FORM
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a ontolex:Form ;\n"
            + "                   ontolex:writtenRep \"_LABEL_\"@_LANG_ ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "    <_LEID_> ontolex:lexicalForm <_ID_> .\n"
            + "}";

    public static final String CREATE_ETYMOLOGY
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a ety:Etymology ;\n"
            + "                   rdfs:label \"Etymology of: _LABEL_\" ;\n"
//            + "                   lexinfo:confidence 1 ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "    <_LEID_> ety:etymology <_ID_> .\n"
            + "}";

    public static final String CREATE_ETYMOLOGICAL_LINK
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a ety:EtyLink ;\n"
            + "                   ety:etyLinkType \"inheritance\" ;\n"
            + "                   ety:etyTarget <_LEID_> ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "    <_ETID_> ety:hasEtyLink <_ID_> .\n"
            + "}";

    public static final String CREATE_LEXICAL_SENSE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a ontolex:LexicalSense ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "    <_LEID_> ontolex:sense <_ID_> .\n"
            + "}";

    public static final String CREATE_COMPONENT
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.DECOMP.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a decomp:Component ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "    <_LEID_> decomp:constituent <_ID_> .\n"
            + "}";
    
    public static final String CREATE_COLLOCATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.FRAC.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a frac:Collocation ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "                   frac:head <_LEID_> .\n"
            + "}";
    
    public static final String CREATE_LEXICOSEMANTIC_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
             + SparqlPrefix.VARTRANS.getSparqlPrefix() + "\n"
            + SparqlPrefix.DECOMP.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a <_TYPE_> ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   rdfs:label \"_LABEL_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "    <_ID_> vartrans:source <_LEID_> .\n"
            + "}";
    
    public static final String CREATE_TRANSLATIONSET
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
             + SparqlPrefix.VARTRANS.getSparqlPrefix() + "\n"
            + SparqlPrefix.DECOMP.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a vartrans:TranslationSet ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   rdfs:label \"_LABEL_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" . \n"
            + "}";
    
    public static final String CREATE_LEXICAL_CONCEPT
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a ontolex:LexicalConcept ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" ; \n"
            + "                   _LABEL_ . \n"
            + "}";
    
    public static final String CREATE_CONCEPT_SET
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a ontolex:ConceptSet ;\n"
            + "                   dct:creator \"_AUTHOR_\" ;\n"
            + "                   dct:created \"_CREATED_\" ;\n"
            + "                   dct:modified \"_MODIFIED_\" ; \n"
            + "                   _LABEL_ . \n"
            + "}";

    public static final String CREATE_LINGUISTIC_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> dct:modified ?modified . } \n"
            + "INSERT { "
            + "<_ID_> <_RELATION_> <_VALUE_TO_INSERT_> ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }  "
            + "\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified . } }";

    public static final String CREATE_GENERIC_RELATION
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "DELETE { <_ID_> dct:modified ?modified . } \n"
            + "INSERT { "
            + "<_ID_> <_RELATION_> _VALUE_TO_INSERT_ ;\n"
            + "                  dct:modified _LAST_UPDATE_ . }  "
            + "\n"
            + "WHERE {  OPTIONAL { <_ID_> dct:modified ?modified . } }";

    public static final String CREATE_BIBLIOGRAPHIC_REFERENCE
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.VS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a rdf:Description ; \n"
            + "        dct:publisher \"_KEY_\" ;\n"
            + "        _OPTIONAL_"
            + "        dct:title \"_TITLE_\" ;\n"
            + "        dct:date \"_DATE_\" ;\n"
            + "        dct:contributor \"_AUTHORS_\" ;\n  "
            + "        dct:creator \"_AUTHOR_\" ;\n"
            + "        dct:created \"_CREATED_\" ;\n"
            + "        dct:modified \"_MODIFIED_\" . \n"
            + "    <_LEID_> dct:references <_ID_> .\n"
            + "}";

    public static final String CREATE_COGNATE_TYPE
            = SparqlPrefix.ETY.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + "INSERT DATA { "
            + "<_ID_> a ety:Cognate .  "
            + "}";
    
    public static final String CREATE_COMPONENT_POSITION
            = SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + "INSERT DATA {  "
            + "<_IDLE_> rdf:_POSITION <_IDCOMPONENT_>. "
            + "}";
    
    public static final String CREATE_IMAGE_REFERENCE
            // source, rights, title, description, creator
            = SparqlPrefix.DCT.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.FOAF.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "_PREFIX_ \n"
            + "INSERT DATA {\n"
            + "    <_ID_> a foaf:Image ; \n"
            + "        dct:identifier \"_IMAGEURL_\" ;\n"
            + "        dct:format \"image/_FORMAT_\" ;\n"
            + "        dct:type \"image\" ;\n"
            + "        dct:publisher \"_AUTHOR_\" ;\n"
            + "        dct:created \"_CREATED_\" ;\n"
            + "        dct:modified \"_MODIFIED_\" . \n"
            + "    <_LEID_> foaf:depiction <_ID_> .\n"
            + "}";
    
}
