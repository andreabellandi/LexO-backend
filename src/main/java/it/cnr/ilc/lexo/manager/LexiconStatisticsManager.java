/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.GraphDbUtil;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.sparql.SparqlSelectStatistics;
import it.cnr.ilc.lexo.util.StringUtil;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 */
public class LexiconStatisticsManager implements Manager, Cached {
    
    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
