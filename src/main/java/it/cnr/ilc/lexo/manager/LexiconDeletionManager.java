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

    public String deleteDictionary(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_DICTIONARY.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteLexicalEntry(String id) throws ManagerException {
        if (!ManagerFactory.getManager(UtilityManager.class).hasLexicalEntryChildren(id)) {
            RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICAL_ENTRY.replaceAll("_ID_", id));
            return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        } else {
            throw new ManagerException("The lexical item cannot be eliminated because it has forms and/or senses, or it is described by a dictionary entry.");
        }
    }

    public String deleteDictionaryEntry(String id) throws ManagerException {
        if (!ManagerFactory.getManager(UtilityManager.class).hasDictionaryEntryComponents(id)) {
            RDFQueryUtil.update(SparqlDeleteData.DELETE_DICTIONARY_ENTRY.replaceAll("_ID_", id));
            return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        } else {
            throw new ManagerException("The dictionary entry component cannot be deleted. Remove its components first.");
        }
    }

    public String deleteLexicographicComponent(String superLexComp, TreeMap<Integer, String> map, String id) throws ManagerException {
        if (!ManagerFactory.getManager(UtilityManager.class).hasDictionaryEntryComponents(id)) {
            String toDelete = "", toInsert = "";
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                toDelete = toDelete + " <" + superLexComp + "> rdf:_" + entry.getKey() + " <" + entry.getValue() + "> .\n";
            }
            TreeMap<Integer, String> _map = filterMap(map, id);
            int position = 1;
            for (Map.Entry<Integer, String> entry : _map.entrySet()) {
                toInsert = toInsert + " <" + superLexComp + "> rdf:_" + position + " <" + entry.getValue() + "> .\n";
                position++;
            }
            RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICOGRAPHIC_COMPONENT.replaceAll("_ID_", id).replaceAll("_TO_DELETE_", toDelete).replaceAll("_TO_INSERT_", toInsert));
            return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        } else {
            throw new ManagerException("The lexicographic component cannot be deleted. Remove its components first.");
        }
    }
    
    private TreeMap<Integer, String> filterMap(TreeMap<Integer, String> map, String id) {
        TreeMap<Integer, String> _map = new TreeMap();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (!entry.getValue().equals(id)) {
                _map.put(entry.getKey(), entry.getValue());
            }
        }
        return _map;
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

    public String deleteCollocation(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_COLLOCATION.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteFormRestriction(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_FORM_RESTRICTION.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteLexicoSemanticRelation(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICOSEMANTIC_RELATION.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteTranslationSet(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_TRANSLATION_SET.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteImage(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_IMAGE.replaceAll("_ID_", id));
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
