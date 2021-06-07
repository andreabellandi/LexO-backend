/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.GraphDbUtil;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.Language;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.Property;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseCore;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

    public Language createLanguage(String author, String lang) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String _id = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
                SparqlInsertData.CREATE_LEXICON_LANGUAGE.replace("_ID_", _id)
                        .replace("_LANG_", _id));
        updateOperation.execute();
        return setLanguage(_id, created, author, lang);
    }
    
    private Language setLanguage(String id, String created, String author, String lang) {
        Language l = new Language();
        l.setCreator(author);
        l.setLanguageInstanceName(id);
        l.setLanguage(getNamespace() + id);
        l.setLabel(lang);
        l.setLastUpdate(created);
        l.setCreationDate(created);
        l.setEntries(0);
        return l;
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
        lec.setType("LexicalEntry");
        lec.setLexicalEntryInstanceName(id);
        lec.setLexicalEntry(getNamespace() + id);
        lec.setStatus("working");
        lec.setLastUpdate(created);
        lec.setCreationDate(created);
        return lec;
    }

    public FormCore createForm(String leID, String author, String lang) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String _id = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
                SparqlInsertData.CREATE_FORM.replaceAll("_ID_", _id)
                        .replace("_LABEL_", _id)
                        .replace("_LANG_", lang)
                        .replace("_AUTHOR_", author)
                        .replace("_CREATED_", created)
                        .replace("_MODIFIED_", created)
                        .replaceAll("_LEID_", leID));
        updateOperation.execute();
        return setForm(_id, created, author);
    }
    
    private FormCore setForm(String id, String created, String author) {
        List<Property> pl = new ArrayList();
        Property p = new Property("writtenRep", id);
        pl.add(p);
        FormCore fc = new FormCore();
        fc.setCreator(author);
        fc.setLabel(pl);
        fc.setType("lexicalForm");
        fc.setFormInstanceName(id);
        fc.setForm(getNamespace() + id);
        fc.setLastUpdate(created);
        fc.setCreationDate(created);
        return fc;
    }

    public LexicalSenseCore createLexicalSense(String leID, String author) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String _id = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
                SparqlInsertData.CREATE_LEXICAL_SENSE.replaceAll("_ID_", _id)
                        .replace("_LABEL_", _id)
                        .replace("_AUTHOR_", author)
                        .replace("_CREATED_", created)
                        .replace("_MODIFIED_", created)
                        .replaceAll("_LEID_", leID));
        updateOperation.execute();
        return setSense(_id, created, author);
    }
    
    private LexicalSenseCore setSense(String id, String created, String author) {
        LexicalSenseCore sc = new LexicalSenseCore();
        sc.setCreator(author);
        sc.setSenseInstanceName(id);
        sc.setSense(getNamespace() + id);
        sc.setLastUpdate(created);
        sc.setCreationDate(created);
        return sc;
    }
    
}
