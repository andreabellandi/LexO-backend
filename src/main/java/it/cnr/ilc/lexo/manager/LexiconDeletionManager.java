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

/**
 *
 * @author andreabellandi
 */
public class LexiconDeletionManager implements Manager, Cached {

    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));
    
    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String deleteLexiconLanguage(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICON_LANGUAGE.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteLexicalEntry(String id) throws ManagerException {
        if (!ManagerFactory.getManager(UtilityManager.class).hasLexicalEntryChildren(id)) {
            RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICAL_ENTRY.replaceAll("_ID_", id));
            return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        } else {
            throw new ManagerException("The lexical entry cannot be deleted. Remove its forms and/or senses first.");
        }

    }

    public String deleteForm(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_FORM.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteLexicalSense(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICAL_SENSE.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteComponent(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_COMPONENT.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteEtymologicalLink(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_ETYMOLOGICAL_LINK.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteEtymology(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_ETYMOLOGY.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteRelation(String id, RelationDeleter rd) throws ManagerException {
        if (rd.getRelation() != null && rd.getValue() != null) {
            if (!rd.getRelation().isEmpty() && !rd.getValue().isEmpty()) {
                RDFQueryUtil.update(SparqlDeleteData.DELETE_RELATION
                        .replaceAll("_ID_", id)
                        .replaceAll("_TARGET_", rd.getValue())
                        .replaceAll("_RELATION_", rd.getRelation()));
                if (rd.getRelation().equals(OntoLexEntity.LexicalRel.Cognate.toString())) {
                    if (StringUtil.existsIRI(rd.getValue())) {
                        if (!ManagerFactory.getManager(UtilityManager.class).isCognate(rd.getValue(), 1)) {
                            // value is involved in this cognate relation only, so its Cognate type is removed
                            RDFQueryUtil.update(SparqlDeleteData.DELETE_COGNATE_TYPE.replaceAll("_ID_", rd.getValue()));
                        }
                    }
                }
                return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
            } else {
                throw new ManagerException("Relation and current value cannot be empty");
            }
        } else {
            throw new ManagerException("Relation and current value cannot be null");
        }
    }

}
