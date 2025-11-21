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
public class SparqlConvertionData {

    // real concepts: ?sense ontolex:reference ?c
    public static final String GET_REAL_REFERENCED_CONCEPTS
            = SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?c ?entry ?sense ?pos ?def ?lexLang\n"
            + "WHERE {\n"
            + "  ?sense ontolex:reference ?c .\n"
            + "  ?entry ontolex:sense ?sense .\n"
            + "  ?lexicon a lime:Lexicon ; lime:entry ?entry ; lime:language ?lexLang .\n"
            + "  OPTIONAL { ?entry lexinfo:partOfSpeech ?pos . }\n"
            + "  OPTIONAL { ?sense skos:definition ?def . }\n"
            + "  _FILTER_ \n"
            + "}\n"
            + "ORDER BY ?c ?entry\n"
            + "LIMIT _LIMIT_";

    // dummy concepts: lexical senses without ontolex:reference
    public static final String GET_DUMMY_NOT_REFERENCED_CONCEPTS
            = SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?sense ?entry ?pos ?def ?lexLang\n"
            + "WHERE {\n"
            + "  ?entry ontolex:sense ?sense .\n"
            + "  ?lexicon a lime:Lexicon ; lime:entry ?entry ; lime:language ?lexLang .\n"
            + "  FILTER NOT EXISTS { ?sense ontolex:reference ?c . }\n"
            + "  OPTIONAL { ?entry lexinfo:partOfSpeech ?pos . }\n"
            + "  OPTIONAL { ?sense skos:definition ?def . }\n"
            + "  _FILTER_ \n"
            + "}\n"
            + "ORDER BY ?sense ?entry\n"
            + "LIMIT _LIMIT_";

    // real concepts: lexical entries with ontolex:denotes
    public static final String GET_REAL_DENOTED_CONCEPTS
            = SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?c ?entry ?pos ?lexLang\n"
            + "WHERE {\n"
            + "  ?entry ontolex:denotes ?c .\n"
            + "  ?lexicon a lime:Lexicon ; lime:entry ?entry ; lime:language ?lexLang .\n"
            + "  FILTER NOT EXISTS { ?entry ontolex:sense ?s . }\n"
            + "  OPTIONAL { ?entry lexinfo:partOfSpeech ?pos . }\n"
            + "  _FILTER_ \n"
            + "}\n"
            + "ORDER BY ?c ?entry\n"
            + "LIMIT _LIMIT_";

    // dummy concepts: lexical entries only
    public static final String GET_DUMMY_NOT_DENOTED_CONCEPTS
            = SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?c ?entry ?pos ?lexLang\n"
            + "WHERE {\n"
            + "  ?entry ontolex:denotes ?c .\n"
            + "  ?lexicon a lime:Lexicon ; lime:entry ?entry ; lime:language ?lexLang .\n"
            + "  FILTER NOT EXISTS { ?entry ontolex:sense ?s . }\n"
            + "  OPTIONAL { ?entry lexinfo:partOfSpeech ?pos . }\n"
            + "  _FILTER_ \n"
            + "}\n"
            + "ORDER BY ?c ?entry\n"
            + "LIMIT _LIMIT_";

    // forms/labels for a batch of entries
    public static final String GET_FORMS_LABELS_FOR_BATCH_OF_ENTRIES
            = SparqlPrefix.RDF.getSparqlPrefix() + "\n"
            + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.SKOS.getSparqlPrefix() + "\n"
            + SparqlPrefix.LEXINFO.getSparqlPrefix() + "\n"
            + "SELECT ?entry ?kind ?lit\n"
            + "WHERE {\n"
            + " _VALUES_\n"
            + "  {\n"
            + "    ?entry ontolex:canonicalForm ?cForm .\n"
            + "    ?cForm  ontolex:writtenRep ?lit .\n"
            + "    BIND(\"canonical\" AS ?kind)\n"
            + "  }\n"
            + "  UNION\n"
            + "  {\n"
            + "    ?entry ontolex:otherForm ?oForm .\n"
            + "    ?oForm  ontolex:writtenRep ?lit .\n"
            + "    BIND(\"other\" AS ?kind)\n"
            + "  }\n"
            + "  UNION\n"
            + "  {\n"
            + "    ?entry ontolex:lexicalForm ?lForm .\n"
            + "    ?lForm  ontolex:writtenRep ?lit .\n"
            + "    BIND(\"lexical\" AS ?kind)\n"
            + "  }\n"
            + "  UNION\n"
            + "  {\n"
            + "    ?entry rdfs:label ?lit .\n"
            + "    BIND(\"label\" AS ?kind)\n"
            + "  }\n"
            + "}\n"
            + "ORDER BY ?entry ?kind ?lit\n";
}
