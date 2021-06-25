/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.GraphDbUtil;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Simone Marchi
 */
public class RDFQueryUtil {

    static final Logger logger = LoggerFactory.getLogger(RDFQueryUtil.class.getName());

    public static TupleQueryResult evaluateTQuery(String query) {
        TupleQueryResult tqr = null;

        RepositoryConnection conn = GraphDbUtil.getConnection();
        try {
            if (null != conn) {
                TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL,
                        query);
                tqr = tupleQuery.evaluate();
            }
        } catch (MalformedQueryException | QueryEvaluationException | RepositoryException e) {
            logger.error("", e);
        } finally {
            GraphDbUtil.releaseConnection(conn);
        }
        return tqr;
    }

    public static boolean evaluateBQuery(String query) {
        boolean res = false;

        RepositoryConnection conn = GraphDbUtil.getConnection();
        try {
            if (null != conn) {
                BooleanQuery bqr = conn.prepareBooleanQuery(QueryLanguage.SPARQL,
                        query);
                res = bqr.evaluate();
            }
        } catch (MalformedQueryException | QueryEvaluationException | RepositoryException e) {
            logger.error("", e);
        } finally {
            GraphDbUtil.releaseConnection(conn);
        }
        return res;
    }

    public static void update(String query) {

        RepositoryConnection conn = GraphDbUtil.getConnection();
        try {
            if (null != conn) {
                Update updateOperation = conn.prepareUpdate(QueryLanguage.SPARQL,
                        query);
                updateOperation.execute();
            }
        } catch (MalformedQueryException | QueryEvaluationException | RepositoryException e) {
            logger.error("", e);
        } finally {
            GraphDbUtil.releaseConnection(conn);
        }
    }
}
