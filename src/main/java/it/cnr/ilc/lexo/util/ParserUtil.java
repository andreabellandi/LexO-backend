/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.List;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;

/**
 *
 * @author andreabellandi
 */
public class ParserUtil {

    public static final ValueFactory vf = org.eclipse.rdf4j.model.impl.SimpleValueFactory.getInstance();

    public static final IRI LEXICON = vf.createIRI(SparqlPrefix.ONTOLEX.getUri(), "Lexicon");
    public static final IRI LANGUGAGE = vf.createIRI(SparqlPrefix.LIME.getUri(), "language");
    public static final IRI LEXICAL_ENTRY = vf.createIRI(SparqlPrefix.ONTOLEX.getUri(), "LexicalEntry");
    public static final IRI FORM = vf.createIRI(SparqlPrefix.ONTOLEX.getUri(), "Form");
    public static final IRI LEXICAL_SENSE = vf.createIRI(SparqlPrefix.ONTOLEX.getUri(), "LexicalSense");
    public static final IRI WORD = vf.createIRI(SparqlPrefix.ONTOLEX.getUri(), "Word");
    public static final IRI MULTIWORD = vf.createIRI(SparqlPrefix.ONTOLEX.getUri(), "MultiwordExpression");
    public static final IRI CANONICAL_FORM = vf.createIRI(SparqlPrefix.ONTOLEX.getUri(), "canonicalForm");
    public static final IRI OTHER_FORM = vf.createIRI(SparqlPrefix.ONTOLEX.getUri(), "otherForm");
    public static final IRI WRITTEN_REP = vf.createIRI(SparqlPrefix.ONTOLEX.getUri(), "writtenRep");
    public static final IRI SENSE = vf.createIRI(SparqlPrefix.ONTOLEX.getUri(), "sense");
    public static final IRI REFERENCE = vf.createIRI(SparqlPrefix.ONTOLEX.getUri(), "reference");

    public static final IRI DEF_PROP = vf.createIRI(SparqlPrefix.SKOS.getUri(), "definition");
    public static final IRI EXAMPLE_PROP = vf.createIRI(SparqlPrefix.LEXINFO.getUri(), "example");

    // Relazioni semantiche di esempio (aggiungi/rimuovi secondo il tuo lessico)
    public static final List<IRI> SEM_REL = List.of(
            vf.createIRI(SparqlPrefix.LEXINFO.getUri(), "synonym"),
            vf.createIRI(SparqlPrefix.LEXINFO.getUri(), "antonym"),
            vf.createIRI(SparqlPrefix.LEXINFO.getUri(), "hypernym"),
            vf.createIRI(SparqlPrefix.LEXINFO.getUri(), "hyponym"),
            vf.createIRI(SparqlPrefix.SKOS.getUri(), "related"),
            vf.createIRI(SparqlPrefix.SKOS.getUri(), "broader"),
            vf.createIRI(SparqlPrefix.SKOS.getUri(), "narrower")
    );

    public static final String GET_LEXICA
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + "SELECT ?" + SparqlVariable.LEXICON + " \n"
            + "WHERE { ?lex a lime:Lexicon ;\n"
            + "             lime:language ?" + SparqlVariable.LEXICON + " . }";

    public static final String COUNT_LEXICAL_ENTRY_BY_LANGUAGE
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + "SELECT (count(distinct ?" + SparqlVariable.LEXICAL_ENTRY + ") as ?" + SparqlVariable.LABEL_COUNT + ") \n"
            + "WHERE { ?lexicon a lime:Lexicon ;\n"
            + "                 lime:language \"_LANG_\" ;\n"
            + "                 lime:entry " + SparqlVariable.LEXICAL_ENTRY + " . }\n";
    
    public static final String COUNT_FORM_BY_LANGUAGE
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "SELECT (count(distinct ?" + SparqlVariable.FORM + ") as ?" + SparqlVariable.LABEL_COUNT + ") \n"
            + "WHERE { ?lexicon a lime:Lexicon ;\n"
            + "                 lime:language \"_LANG_\" ;\n"
            + "                 lime:entry ?le .\n"
            + "         ?le ?formRelation ?" + SparqlVariable.FORM + " . \n"
            + "         ?" + SparqlVariable.FORM + " a ontolex:Form . }";
    
    public static final String COUNT_LEXICAL_SENSE_BY_LANGUAGE
            = SparqlPrefix.LIME.getSparqlPrefix() + "\n"
            + SparqlPrefix.ONTOLEX.getSparqlPrefix() + "\n"
            + "SELECT (count(distinct ?" + SparqlVariable.SENSE + ") as ?" + SparqlVariable.LABEL_COUNT + ") \n"
            + "WHERE { ?lexicon a lime:Lexicon ;\n"
            + "                 lime:language \"_LANG_\" ;\n"
            + "                 lime:entry ?le .\n"
            + "         ?le ontolex:sense ?" + SparqlVariable.SENSE + " . }";

}
