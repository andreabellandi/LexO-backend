/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.LinguisticRelationUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.RelationDeleter;
import it.cnr.ilc.lexo.sparql.SparqlDeleteData;
import it.cnr.ilc.lexo.util.RDFQueryUtil;

/**
 *
 * @author andreabellandi
 */
public class LexiconDeletionManager implements Manager, Cached {

    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");

    public String getNamespace() {
        return namespace;
    }

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteLexiconLanguage(String id) throws ManagerException {
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlDeleteData.DELETE_LEXICON_LANGUAGE.replaceAll("_ID_", id));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICON_LANGUAGE.replaceAll("_ID_", id));
    }

    public void deleteLexicalEntry(String id) throws ManagerException {
        if (!ManagerFactory.getManager(UtilityManager.class).hasLexicalEntryChildren(id)) {
//            Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                    SparqlDeleteData.DELETE_LEXICAL_ENTRY.replaceAll("_ID_", id));
//            updateOperation.execute();
            RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICAL_ENTRY.replaceAll("_ID_", id));
        } else {
            throw new ManagerException("The lexical entry cannot be deleted. Remove its forms and/or senses first.");
        }

    }

    public void deleteForm(String id) throws ManagerException {
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlDeleteData.DELETE_FORM.replaceAll("_ID_", id));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlDeleteData.DELETE_FORM.replaceAll("_ID_", id));
    }

    public void deleteLexicalSense(String id) throws ManagerException {
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlDeleteData.DELETE_LEXICAL_SENSE.replaceAll("_ID_", id));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICAL_SENSE.replaceAll("_ID_", id));
    }

    public void deleteEtymologicalLink(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICAL_SENSE.replaceAll("_ID_", id));
    }

    public void deleteRelation(String id, RelationDeleter rd) throws ManagerException {
        if (rd.getRelation() != null && rd.getValue() != null) {
            if (!rd.getRelation().isEmpty() && !rd.getValue().isEmpty()) {
//                Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                        SparqlDeleteData.DELETE_LINGUISTIC_RELATION
//                                .replaceAll("_ID_", id)
//                                .replaceAll("_ID_TARGET_", lru.getCurrentValue())
//                                .replaceAll("_RELATION_", lru.getRelation()));
//                updateOperation.execute();
                RDFQueryUtil.update(SparqlDeleteData.DELETE_RELATION
                        .replaceAll("_ID_", id)
                        .replaceAll("_TARGET_", rd.getValue())
                        .replaceAll("_RELATION_", rd.getRelation()));

            } else {
                throw new ManagerException("Relation and current value cannot be empty");
            }
        } else {
            throw new ManagerException("Relation and current value cannot be null");
        }
    }

}
