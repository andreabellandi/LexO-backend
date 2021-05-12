/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.GraphDbUtil;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.sparql.SparqlUpdateData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.LexInfoEntity;
import it.cnr.ilc.lexo.util.OntoLexEntity;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.UpdateExecutionException;

/**
 *
 * @author andreabellandi
 */
public class LexiconUpdateManager implements Manager, Cached {

    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");


    public String getNamespace() {
        return namespace;
    }

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void validateLexicalEntryType(String type) throws ManagerException {
        Manager.validateWithOntoLexEntity("type", OntoLexEntity.LexicalEntryTypes.class, type);
    }
    
    public void validateLexicalEntryStatus(String status) throws ManagerException {
        Manager.validateWithEnum("status", EnumUtil.LexicalEntryStatus.class, status);
    }
    
    public void validateLexicalEntryPoS(String pos) throws ManagerException {
        Manager.validateWithEnum("pos", LexInfoEntity.LexicalEntryPoS.class, pos);
    }
    
    public String updateLexicalEntry(String id, String relation, String valueToInsert, boolean dataType) throws ManagerException, UpdateExecutionException {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
                SparqlUpdateData.UPDATE_LEXICAL_ENTRY.replaceAll("_ID_", id)
                        .replaceAll("_RELATION_", relation)
                        .replaceAll("_VALUE_TO_INSERT_", dataType ? "\"" + valueToInsert + "\"" : valueToInsert)
                        .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
                        .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        updateOperation.execute();
        return lastupdate;
    }


}
