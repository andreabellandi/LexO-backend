/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.RelationDeleter;
import it.cnr.ilc.lexo.sparql.SparqlDeleteData;
import it.cnr.ilc.lexo.util.OntoLexEntity;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import it.cnr.ilc.lexo.util.StringUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author andreabellandi
 */
public class ECDDeletionManager implements Manager, Cached {

    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String deleteRelation(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICAL_FUNCTION.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }
    
    public String deleteECDForm(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_ECD_FORM.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }
    
    public String deleteECDEntry(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_ECD_ENTRY.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }
    
    public String deleteECDEntryPos(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_ECD_ENTRY_POS.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }
    
    public String deleteECDictionary(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_EC_DICTIONARY.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

}
