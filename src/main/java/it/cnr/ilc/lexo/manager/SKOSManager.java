/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.output.ConceptSet;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalConcept;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author andreabellandi
 */
public class SKOSManager implements Manager, Cached {

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

     
    public LexicalConcept createLexicalConcept(String author) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String _id = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_LEXICAL_CONCEPT.replaceAll("_ID_", _id)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created));
        return setLexicalConcept(_id, created, author);
    }
    
    public ConceptSet createConceptSet(String author) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String _id = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_CONCEPT_SET.replaceAll("_ID_", _id)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created));
        return setConceptSet(_id, created, author);
    }

    
    private LexicalConcept setLexicalConcept(String id, String created, String author) {
        LexicalConcept lc = new LexicalConcept();
        lc.setCreator(author);
        lc.setLexicalConceptInstanceName(id);
        lc.setLexicalConcept(getNamespace() + id);
        lc.setLastUpdate(created);
        lc.setCreationDate(created);
        return lc;
    }
    
    private ConceptSet setConceptSet(String id, String created, String author) {
        ConceptSet cs = new ConceptSet();
        cs.setCreator(author);
        cs.setConceptSetInstanceName(id);
        cs.setConceptSet(getNamespace() + id);
        cs.setLastUpdate(created);
        cs.setCreationDate(created);
        return cs;
    }

}
