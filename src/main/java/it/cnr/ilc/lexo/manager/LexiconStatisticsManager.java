/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.sparql.SparqlSelectStatistics;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
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
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectStatistics.STATISTICS_LANGUAGES_LIST)) {
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
        return RDFQueryUtil.evaluateTQuery(SparqlSelectStatistics.STATISTICS_TYPES);
    }

    public TupleQueryResult getPos() {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectStatistics.STATISTICS_POS);
        return RDFQueryUtil.evaluateTQuery(SparqlSelectStatistics.STATISTICS_POS);
    }

    public TupleQueryResult getStates() {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectStatistics.STATISTICS_STATUS);
        return RDFQueryUtil.evaluateTQuery(SparqlSelectStatistics.STATISTICS_STATUS.replace("_INDEX_", SparqlVariable.LEXICAL_ENTRY_INDEX));
    }

    public TupleQueryResult getAuthors() {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectStatistics.STATISTICS_AUTHORS);
        return RDFQueryUtil.evaluateTQuery(SparqlSelectStatistics.STATISTICS_AUTHORS.replace("_INDEX_", SparqlVariable.LEXICAL_ENTRY_INDEX));
    }

    public TupleQueryResult getLanguages() {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectStatistics.STATISTICS_LANGUAGES);
        return RDFQueryUtil.evaluateTQuery(SparqlSelectStatistics.STATISTICS_LANGUAGES.replace("_INDEX_", SparqlVariable.LEXICAL_ENTRY_INDEX)
        .replace("_LANG_FIELD_", "writtenFormLanguage"));
    }
}
