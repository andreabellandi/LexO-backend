/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.GraphDbUtil;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.service.data.lexicon.input.ExportSetting;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Simone Marchi
 */
public class RDFQueryUtil {

    static final Logger logger = LoggerFactory.getLogger(RDFQueryUtil.class.getName());

    public static String getNamespace(String prefix) {
        RepositoryConnection conn = GraphDbUtil.getConnection();
        try {
            if (null != conn) {
                return conn.getNamespace(prefix);
            }
        } catch (MalformedQueryException | QueryEvaluationException | RepositoryException e) {
            logger.error("", e);
        } finally {
            GraphDbUtil.releaseConnection(conn);
        }
        return null;
    }
    
    public static RepositoryResult<Namespace> getNamespaces() {
        RepositoryConnection conn = GraphDbUtil.getConnection();
        try {
            if (null != conn) {
                return conn.getNamespaces();
            }
        } catch (MalformedQueryException | QueryEvaluationException | RepositoryException e) {
            logger.error("", e);
        } finally {
            GraphDbUtil.releaseConnection(conn);
        }
        return null;
    }

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
    
    public static GraphQueryResult evaluateGQuery(String query) {
        GraphQueryResult gqr = null;

        RepositoryConnection conn = GraphDbUtil.getConnection();
        try {
            if (null != conn) {
                GraphQuery graphQuery = conn.prepareGraphQuery(QueryLanguage.SPARQL,
                        query);
                gqr = graphQuery.evaluate();
            }
        } catch (MalformedQueryException | QueryEvaluationException | RepositoryException e) {
            logger.error("", e);
        } finally {
            GraphDbUtil.releaseConnection(conn);
        }
        return gqr;
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

    public static File export(ExportSetting set) throws ManagerException {
        RepositoryConnection conn = GraphDbUtil.getConnection();
        try {
            if (null != conn) {
                File _export = new File(set.getFileName() + getFileExtension(set.getFormat()));
                RDFFormat format = getRDFFormat(set.getFormat());
                FileOutputStream outputStream = new FileOutputStream(_export);
                RDFWriter writer = Rio.createWriter(format, outputStream);
                conn.exportStatements(set.getSubject() != null ? Values.iri(set.getSubject()) : null,
                        set.getObject() != null ? Values.iri(set.getObject()) : null,
                        set.getPredicate() != null ? Values.iri(set.getPredicate()) : null,
                        set.isInferred(),
                        writer,
                        getContexts(set.getContext() != null ? (set.getContext().get(0).isEmpty() ? null : set.getContext()) : null));
                IOUtils.closeQuietly(outputStream);
                return _export;
            }
        } catch (RepositoryException | FileNotFoundException e) {
            logger.error("", e);
            throw new ManagerException(e.getMessage());
        } finally {
            GraphDbUtil.releaseConnection(conn);
        }
        return null;
    }

    private static RDFFormat getRDFFormat(String format) {
        switch (format) {
            case "xml":
                return RDFFormat.RDFXML;
            case "jsonld":
                return RDFFormat.JSONLD;
            case "n3":
                return RDFFormat.N3;
            case "ntriples":
                return RDFFormat.NTRIPLES;
            case "nquads":
                return RDFFormat.NQUADS;
            case "turtle":
                return RDFFormat.TURTLE;
            default:
                return RDFFormat.RDFXML;
        }
    }

    private static String getFileExtension(String format) {
        switch (format) {
            case "xml":
                return ".owl";
            case "jsonld":
                return ".jsonld";
            case "n3":
                return ".n3";
            case "ntriples":
                return ".nt";
            case "nquads":
                return ".nq";
            case "turtle":
                return ".ttl";
            default:
                return null;
        }
    }

    private static Resource[] getContexts(ArrayList<String> contexts) {
        if (contexts != null) {
            Resource[] _contexts = new Resource[contexts.size()];
            for (int i = 0; i < contexts.size(); i++) {
                _contexts[i] = Values.iri(contexts.get(i));
            }
            return _contexts;
        } else {
            return new Resource[0];
        }

    }

}
