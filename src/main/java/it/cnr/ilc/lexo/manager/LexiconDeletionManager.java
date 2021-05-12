/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.GraphDbUtil;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryItem;
import it.cnr.ilc.lexo.sparql.SparqlDeleteData;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;

/**
 *
 * @author andreabellandi
 */
public class LexiconDeletionManager implements Manager, Cached {

    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");
    private final String idInstancePrefix = LexOProperties.getProperty("repository.instance.id");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");


    public String getNamespace() {
        return namespace;
    }

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteLexicalEntry(String id) throws ManagerException {
        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
                SparqlDeleteData.DELETE_LEXICAL_ENTRY.replace("_ID_", id));
        updateOperation.execute();
    }

}
