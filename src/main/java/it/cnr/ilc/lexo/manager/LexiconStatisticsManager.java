/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.GraphDbUtil;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.vocabulary.MorphologicalProperty;
import it.cnr.ilc.lexo.sparql.SparqlSelectStatistics;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 */
public final class LexiconStatisticsManager implements Manager, Cached {

    private final List<String> languages = new ArrayList<>();

    public List<String> getLexiconLanguages() {
        return languages;
    }
    
    LexiconStatisticsManager() {
        reloadCache();
    }

    @Override
    public void reloadCache() {
        languages.clear();
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectStatistics.STATISTICS_LANGUAGES_LIST);
        try (TupleQueryResult result = tupleQuery.evaluate()) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                if (bs.getBinding(SparqlVariable.LEXICON_LANGUAGE) != null) {
                    languages.add(bs.getBinding(SparqlVariable.LEXICON_LANGUAGE).getValue().stringValue());
                }
            }
        } catch (QueryEvaluationException qee) {
        }
    }

    public TupleQueryResult getTypes() {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectStatistics.STATISTICS_TYPES);
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getPos() {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectStatistics.STATISTICS_POS);
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getStates() {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectStatistics.STATISTICS_STATUS);
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getAuthors() {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectStatistics.STATISTICS_AUTHORS);
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getLanguages() {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectStatistics.STATISTICS_LANGUAGES);
        return tupleQuery.evaluate();
    }
}
