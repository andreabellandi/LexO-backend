/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.GraphDbUtil;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;

/**
 *
 * @author andreabellandi
 */
public class LexiconCreationManager implements Manager, Cached {

    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");
    private final String idInstancePrefix = LexOProperties.getProperty("repository.instance.id");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

    public String getNamespace() {
        return namespace;
    }

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LexicalEntryCore createLexicalEntry(String author) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String _id = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
                SparqlInsertData.CREATE_LEXICAL_ENTRY.replace("[ID]", _id)
                        .replace("[LABEL]", _id)
                        .replace("[AUTHOR]", author)
                        .replace("[CREATED]", created)
                        .replace("[MODIFIED]", created));
        updateOperation.execute();
        return setLexicalEntry(_id, created, author);
    }

    private LexicalEntryCore setLexicalEntry(String id, String created, String author) {
        LexicalEntryCore lec = new LexicalEntryCore();
        lec.setAuthor(author);
        lec.setLabel(id);
        lec.setLexicalEntryInstanceName(id);
        lec.setLexicalEntry(getNamespace() + id);
        lec.setStatus("working");
        lec.setLastUpdate(created);
        lec.setCreationDate(created);
        return lec;
    }

    public LexicalEntryCore createForm(String leID, String author) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String _id = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
                SparqlInsertData.CREATE_LEXICAL_ENTRY.replace("[ID]", _id)
                        .replace("[LABEL]", _id)
                        .replace("[AUTHOR]", author)
                        .replace("[CREATED]", created)
                        .replace("[MODIFIED]", created)
                        .replaceAll("[LE_ID]", leID));
        updateOperation.execute();
        return setLexicalEntry(_id, created, author);
    }

}
