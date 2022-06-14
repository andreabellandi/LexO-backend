/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.output.HitsDataList;
import it.cnr.ilc.lexo.service.data.lexicon.output.federation.FederatedObject;
import java.util.ArrayList;
import org.eclipse.rdf4j.federated.FedXFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class FederationManager implements Manager, Cached {

    static final Logger logger = LoggerFactory.getLogger(FederationManager.class.getName());

    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");

    public String getNamespace() {
        return namespace;
    }

    @Override
    public void reloadCache() {

    }

    public HitsDataList getFederatedResult(String endpoint, String query) throws ManagerException {
        ArrayList<FederatedObject> fedList = new ArrayList();
        int resultCount = 0;
        Repository repository = FedXFactory.newFederation()
                .withSparqlEndpoint(endpoint)
                .create();
        try ( RepositoryConnection conn = repository.getConnection()) {
            TupleQuery tq = conn.prepareTupleQuery(query);
            try ( TupleQueryResult tqRes = tq.evaluate()) {
                while (tqRes.hasNext()) {
                    resultCount++;
                    BindingSet b = tqRes.next();
                    for (String var : b.getBindingNames()) {
                        FederatedObject fo = new FederatedObject();
                        fo.setKey(var);
                        fo.setValue(b.getValue(var).stringValue());
                        fedList.add(fo);
                    }
                }
            }
        } catch (RepositoryException|MalformedQueryException|QueryEvaluationException e) {
            throw new ManagerException(e.getMessage());
        }
        repository.shutDown();
        return new HitsDataList(resultCount, fedList);
    }

}
