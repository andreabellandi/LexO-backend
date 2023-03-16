/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.skos.SKOSDeleter;
import it.cnr.ilc.lexo.service.data.lexicon.input.skos.SKOSUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.output.ConceptSet;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalConcept;
import it.cnr.ilc.lexo.service.data.output.Label;
import it.cnr.ilc.lexo.sparql.SparqlDeleteData;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.sparql.skos.SparqlSKOSData;
import it.cnr.ilc.lexo.sparql.skos.SparqlSKOSDelete;
import it.cnr.ilc.lexo.sparql.skos.SparqlSKOSInsert;
import it.cnr.ilc.lexo.sparql.skos.SparqlSKOSUpdate;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import it.cnr.ilc.lexo.util.SKOSEntity;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 */
public class SKOSManager implements Manager, Cached {

    private final String idInstancePrefix = LexOProperties.getProperty("repository.instance.id");
    private final String lexicalizationModel = LexOProperties.getProperty("skos.lexicalizationModel");
    private final String defaultLanguageLabel = LexOProperties.getProperty("skos.defaultLanguageLabel");

    private final List<String> allowedLanguages = Arrays.asList("it", "en", "es", "fr");

    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

    private static final ValueFactory skosFactory = SimpleValueFactory.getInstance();
    private static final UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void validateSemanticRelation(String rel) throws ManagerException {
        Manager.validateWithEnum("rel", SKOSEntity.SemanticRelation.class, rel);
    }

    public void validateLexicalRelation(String rel) throws ManagerException {
        Manager.validateWithEnum("rel", SKOSEntity.LexicalLabel.class, rel);
    }

    public void validateSchemeRelation(String rel) throws ManagerException {
        Manager.validateWithEnum("rel", SKOSEntity.SchemeProperty.class, rel);
    }

    public void validateNotation(String rel) throws ManagerException {
        Manager.validateWithEnum("rel", SKOSEntity.Notation.class, rel);
    }

    public void validateMappingRelation(String rel) throws ManagerException {
        Manager.validateWithEnum("rel", SKOSEntity.MappingProperty.class, rel);
    }

    public void validateNoteRelation(String rel) throws ManagerException {
        Manager.validateWithEnum("rel", SKOSEntity.NoteProperty.class, rel);
    }

    public void validateLexicalRelationXL(String rel) throws ManagerException {
        Manager.validateWithEnum("rel", SKOSEntity.SKOSXLLexicalLabel.class, rel);
    }

    public void validateLexicalizationModel(String rel) throws ManagerException {
        switch (lexicalizationModel) {
            case "label":
                if (!rel.equals("label")) {
                    throw new ManagerException(rel + " not recognized in the lexicalization model choosen (" + lexicalizationModel + ")");
                }
                break;
            case "skos":
                validateLexicalRelation(rel);
                break;
            case "skos-xl":
                validateLexicalRelationXL(rel);
                break;
        }
    }

    public void validateScheme(String scheme, String referenceScheme) throws ManagerException {
        if (referenceScheme == null) {
            if (!scheme.equals(SparqlPrefix.SKOS.getUri())) {
                throw new ManagerException(scheme + " not recognized");
            }
        } else {
            if (!scheme.equals(referenceScheme)) {
                throw new ManagerException(scheme + " not recognized in the lexicalization model choosen ("
                        + (lexicalizationModel.equals("label") ? SparqlPrefix.RDFS.getUri() : (lexicalizationModel.equals("skos")
                        ? SparqlPrefix.SKOS.getUri() : SparqlPrefix.SKOS_XL)) + ")");
            }
        }
    }

    public void validateLanguage(String lang) throws ManagerException {
        if (!allowedLanguages.contains(lang)) {
            throw new ManagerException(lang + " not allowed");
        }
    }

    public LexicalConcept createLexicalConcept(String author, String prefix, String baseIRI) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        String label = lexicalizationModel.equals("label") ? "rdfs:label" : (lexicalizationModel.equals("skos") ? "skos:prefLabel" : "skos:prefLabel");
        RDFQueryUtil.update(SparqlInsertData.CREATE_LEXICAL_CONCEPT.replaceAll("_ID_", _id)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_MODIFIED_", created)
                .replace("_LABEL_", label + " \"" + _id + "\"@" + defaultLanguageLabel));
        return setLexicalConcept(_id, created, author);
    }

    public ConceptSet createConceptSet(String author, String prefix, String baseIRI) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        String label = lexicalizationModel.equals("label") ? "rdfs:label" : (lexicalizationModel.equals("skos") ? "skos:prefLabel" : "label");
        RDFQueryUtil.update(SparqlInsertData.CREATE_CONCEPT_SET.replaceAll("_ID_", _id)
                .replace("_AUTHOR_", author)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created)
                .replace("_LABEL_", label + " \"" + _id + "\"@" + defaultLanguageLabel));
        return setConceptSet(_id, created, author);
    }

    private LexicalConcept setLexicalConcept(String id, String created, String author) {
        LexicalConcept lc = new LexicalConcept();
        lc.setCreator(author);
        lc.setLexicalConcept(id);
        lc.setLastUpdate(created);
        lc.setConfidence(-1.0);
        lc.setCreationDate(created);
        lc.setDefaultLabel(id);
        lc.setLanguage(defaultLanguageLabel);
        return lc;
    }

    private ConceptSet setConceptSet(String id, String created, String author) {
        ConceptSet cs = new ConceptSet();
        cs.setCreator(author);
        cs.setConceptSet(id);
        cs.setLastUpdate(created);
        cs.setCreationDate(created);
        cs.setConfidence(-1.0);
        cs.setDefaultLabel(id);
        cs.setLanguage(defaultLanguageLabel);
        return cs;
    }

    public String deleteLexicalConcept(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICAL_CONCEPT.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    public String deleteConceptSet(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_CONCEPT_SET.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    private String getTarget(SKOSUpdater su) {
        return (su.getOldTarget() != null) ? (!su.getOldTarget().isEmpty()) ? su.getOldTarget() : su.getTarget() : su.getTarget();
    }

    public String updateSemanticRelation(SKOSUpdater su) throws ManagerException {
        // all the values of SKOSUpdater are full IRIs
        if (!inputCheck(su, false)) {
            throw new ManagerException("input json does not contain all the needed fields");
        }

        if (utilityManager.existsSKOSRelation(su.getSource(), su.getRelation(), "<" + getTarget(su) + ">")) {
            // update
            entityCheck(su.getOldTarget(), SKOSEntity.SKOSClass.Concept.toString());
            return update(su);
        } else {
            // new
            relationCheck(su.getRelation());
//            IRI relation = skosFactory.createIRI(su.getRelation());
//            validateScheme(relation.getNamespace(), null);
            validateSemanticRelation(su.getRelation());
            entityCheck(su.getSource(), SKOSEntity.SKOSClass.Concept.toString());
            entityCheck(su.getTarget(), SKOSEntity.SKOSClass.Concept.toString());
            return create(su);
        }
    }

    public String updateSchemeProperty(SKOSUpdater su) throws ManagerException {
        // all the values of SKOSUpdater are full IRIs
        if (!inputCheck(su, false)) {
            throw new ManagerException("input json does not contain all the needed fields");
        }
        if (utilityManager.existsSKOSRelation(su.getSource(), su.getRelation(), "<" + getTarget(su) + ">")) {
            // update
            entityCheck(su.getOldTarget(),
                    su.getRelation().equals(SKOSEntity.SchemeProperty.hasTopConcept.toString())
                    ? SKOSEntity.SKOSClass.Concept.toString() : SKOSEntity.SKOSClass.ConceptScheme.toString());
            return update(su);
        } else {
            // new
            relationCheck(su.getRelation());
//            IRI relation = skosFactory.createIRI(su.getRelation());
//            validateScheme(relation.getNamespace(), null);
            validateSchemeRelation(su.getRelation());
            entityCheck(su.getSource(), su.getRelation().equals(SKOSEntity.SchemeProperty.hasTopConcept.toString())
                    ? SKOSEntity.SKOSClass.ConceptScheme.toString() : SKOSEntity.SKOSClass.Concept.toString());
            entityCheck(su.getTarget(), su.getRelation().equals(SKOSEntity.SchemeProperty.hasTopConcept.toString())
                    ? SKOSEntity.SKOSClass.Concept.toString() : SKOSEntity.SKOSClass.ConceptScheme.toString());
            return create(su);
        }
    }

    public String updateMappingProperty(SKOSUpdater su) throws ManagerException {
        // all the values of SKOSUpdater are full IRIs
        if (!inputCheck(su, false)) {
            throw new ManagerException("input json does not contain all the needed fields");
        }
        if (utilityManager.existsSKOSRelation(su.getSource(), su.getRelation(), "<" + getTarget(su) + ">")) {
            // update
            entityCheck(su.getOldTarget(), SKOSEntity.SKOSClass.Concept.toString());
            return update(su);
        } else {
            // new
            relationCheck(su.getRelation());
//            IRI relation = skosFactory.createIRI(su.getRelation());
//            validateScheme(relation.getNamespace(), null);
            validateMappingRelation(su.getRelation());
            entityCheck(su.getSource(), SKOSEntity.SKOSClass.Concept.toString());
            entityCheck(su.getTarget(), SKOSEntity.SKOSClass.Concept.toString());
            return create(su);
        }
    }

    public String updateNotation(SKOSUpdater su) throws ManagerException {
        // all the values of SKOSUpdater are full IRIs
        if (!inputCheck(su, true)) {
            throw new ManagerException("input json does not contain all the needed fields");
        }
        if (utilityManager.existsSKOSRelation(su.getSource(), su.getRelation(), "\"" + getTarget(su) + "\"@" + su.getTargetLanguage())) {
            // update
            return updateLiteral(su);
        } else {
            // new
            relationCheck(su.getRelation());
//            IRI relation = skosFactory.createIRI(su.getRelation());
//            validateScheme(relation.getNamespace(), null);
            validateNotation(su.getRelation());
            validateLanguage(su.getTargetLanguage());
            return createLiteral(su);
        }
    }

    public String updateNoteProperty(SKOSUpdater su) throws ManagerException {
        // all the values of SKOSUpdater are full IRIs
        if (!inputCheck(su, true)) {
            throw new ManagerException("input json does not contain all the needed fields");
        }
        if (utilityManager.existsSKOSRelation(su.getSource(), su.getRelation(), "\"" + getTarget(su) + "\"@" + su.getTargetLanguage())) {
            // update
            return updateLiteral(su);
        } else {
            // new
            relationCheck(su.getRelation());
//            IRI relation = skosFactory.createIRI(su.getRelation());
//            validateScheme(relation.getNamespace(), null);
            validateNoteRelation(su.getRelation());
            validateLanguage(su.getTargetLanguage());
            return createLiteral(su);
        }
    }

    public String updateLexicalProperty(SKOSUpdater su) throws ManagerException {
        // all the values of SKOSUpdater are full IRIs
        // label and skos supported only (skos-xl to be implemented)
        if (!inputCheck(su, true)) {
            throw new ManagerException("input json does not contain all the needed fields");
        }
        switch (lexicalizationModel) {
            case "label":
                return updateLexicalPropertyLabel(su);
            case "skos":
                return updateLexicalPropertySKOS(su);
            case "skos-xl":
                return updateLexicalPropertySKOSXL(su);
            default:
        }
        return null;
    }

    private String updateLexicalPropertyLabel(SKOSUpdater su) throws ManagerException, IllegalArgumentException {
        if (su.getOldTarget() != null && su.getOldTargetLanguage() != null) {
            if (!su.getOldTarget().isEmpty() && !su.getOldTargetLanguage().isEmpty()) {
                // update 
                if (utilityManager.existsSKOSRelation(su.getSource(), su.getRelation(), "\"" + su.getOldTarget() + "\"@" + su.getOldTargetLanguage())) {
                    return updateLiteral(su);
                } else {
                    throw new ManagerException(su.getSource() + " " + su.getRelation() + " \"" + su.getOldTarget() + "\"@" + su.getOldTargetLanguage() + " does not exist");
                }
            } else {
                throw new ManagerException("oldTarget or oldTargetLanguage field cannot be empty");
            }
        } else {
            // create 
            relationCheck(su.getRelation());
            IRI relation = skosFactory.createIRI(su.getRelation());
            validateScheme(relation.getNamespace(), SparqlPrefix.RDFS.getUri());
            validateLexicalizationModel(relation.getLocalName());
            validateLanguage(su.getTargetLanguage());
            if (utilityManager.existsLabel(su.getSource(), su.getRelation(), su.getTargetLanguage())) {
                throw new ManagerException("one label for each language is allowed only");
            }
            return createLiteral(su);
        }
    }

    private String updateLexicalPropertySKOS(SKOSUpdater su) throws ManagerException {
        if (su.getOldTarget() != null && su.getOldTargetLanguage() != null) {
            if (!su.getOldTarget().isEmpty() && !su.getOldTargetLanguage().isEmpty()) {
                // update 
                if (utilityManager.existsSKOSRelation(su.getSource(), su.getRelation(), "\"" + su.getOldTarget() + "\"@" + su.getOldTargetLanguage())) {
                    return updateLiteral(su);
                } else {
                    throw new ManagerException(su.getSource() + " " + su.getRelation() + " \"" + su.getOldTarget() + "\"@" + su.getOldTargetLanguage() + " does not exist");
                }
            } else {
                throw new ManagerException("oldTarget or oldTargetLanguage field cannot be empty");
            }
        } else {
            // create 
            relationCheck(su.getRelation());
            IRI relation = skosFactory.createIRI(su.getRelation());
            validateScheme(relation.getNamespace(), SparqlPrefix.SKOS.getUri());
            validateLexicalizationModel(relation.getLocalName());
            validateLanguage(su.getTargetLanguage());
            if (utilityManager.existsLabel(su.getSource(), su.getRelation(), su.getTargetLanguage())) {
                throw new ManagerException("one label for each language is allowed only");
            }
            return createLiteral(su);
        }
    }

    private String updateLexicalPropertySKOSXL(SKOSUpdater su) throws ManagerException {
        throw new ManagerException("to implement");
    }

    private String update(SKOSUpdater su) {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlSKOSUpdate.UPDATE_RELATION.replaceAll("_ID_", "<" + su.getSource() + ">")
                .replaceAll("_RELATION_", su.getRelation())
                .replaceAll("_VALUE_TO_INSERT_", "<" + su.getTarget() + ">")
                .replaceAll("_VALUE_TO_DELETE_", "<" + su.getOldTarget() + ">")
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    private String updateLiteral(SKOSUpdater su) {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlSKOSUpdate.UPDATE_RELATION.replaceAll("_ID_", "<" + su.getSource() + ">")
                .replaceAll("_RELATION_", su.getRelation())
                .replaceAll("_VALUE_TO_INSERT_", "\"" + su.getTarget() + "\"@" + su.getTargetLanguage())
                .replaceAll("_VALUE_TO_DELETE_", "\"" + su.getOldTarget() + "\"@" + su.getOldTargetLanguage())
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    private String create(SKOSUpdater su) {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlSKOSInsert.CREATE_RELATION.replaceAll("_ID_", "<" + su.getSource() + ">")
                .replaceAll("_RELATION_", su.getRelation())
                .replaceAll("_VALUE_TO_INSERT_", "<" + su.getTarget() + ">")
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    private String createLiteral(SKOSUpdater su) {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlSKOSInsert.CREATE_RELATION.replaceAll("_ID_", "<" + su.getSource() + ">")
                .replaceAll("_RELATION_", su.getRelation())
                .replaceAll("_VALUE_TO_INSERT_", "\"" + su.getTarget() + "\"@" + su.getTargetLanguage())
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    private boolean inputCheck(SKOSUpdater su, boolean literal) {
        if (su.getRelation() != null && su.getSource() != null && su.getTarget() != null) {
            if (!su.getRelation().isEmpty() && !su.getSource().isEmpty() && !su.getTarget().isEmpty()) {
                if (literal) {
                    if (su.getTargetLanguage() != null) {
                        if (!su.getTargetLanguage().isEmpty()) {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean inputCheck(SKOSDeleter sd) {
        if (sd.getRelation() != null && sd.getSource() != null && sd.getTarget() != null) {
            if (!sd.getRelation().isEmpty() && !sd.getSource().isEmpty() && !sd.getTarget().isEmpty()) {
            }
        }
        return false;
    }

    private void relationCheck(String rel) throws ManagerException {
        if (rel == null) {
            throw new ManagerException("relation field cannot be null");
        }
        if (rel.isEmpty()) {
            throw new ManagerException("relation field cannot be empty");
        }
    }

    private void entityCheck(String val, String type) throws ManagerException {
        if (val == null) {
            throw new ManagerException("source or target fields cannot be null");
        }
        if (val.isEmpty()) {
            throw new ManagerException("source or target fields cannot be empty");
        }
        if (!utilityManager.existsTyped(val, type)) {
            throw new ManagerException(val + " does not exist or it has no " + type + " type");
        }
    }

    public void deleteRelation(SKOSDeleter sd) throws ManagerException {
        inputCheck(sd);
        RDFQueryUtil.update(SparqlSKOSDelete.DELETE_RELATION
                .replaceAll("_ID_", sd.getSource())
                .replaceAll("_TARGET_", (sd.getLanguage() != null && !sd.getLanguage().isEmpty())
                        ? "\"" + sd.getTarget() + "\"@" + sd.getLanguage()
                        : "<" + sd.getTarget() + ">")
                .replaceAll("_RELATION_", sd.getRelation()));
    }

    public TupleQueryResult getConceptSchemes(String type) throws ManagerException {
        String labelRelation = "";
        String labelQuery = "";
        String index = "";
        String query = SparqlSKOSData.DATA_CONCEPT_SCHEMES.replace("_LABEL_QUERY_", labelQuery)
                .replace("_LABEL_RELATION_", labelRelation)
                .replace("_INDEX_", index);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLexicalConceptChildren(String id) throws ManagerException {
        String query = "";
        if (id.equals("root")) {
            query = SparqlSelectData.DATA_LEXICAL_CONCEPTS_ROOT
                    .replace("_LABELPROPERTY_", lexicalizationModel.equals("label") ? SparqlPrefix.RDFS.getUri() + "label" : SparqlPrefix.SKOS.getUri() + "prefLabel")
                    .replace("_DEFAULTLANGUAGE_", defaultLanguageLabel);
        } else {
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            if (utilityManager.isConceptSet(id)) {
                query = SparqlSelectData.DATA_TOP_LEXICAL_CONCEPT_OF_A_CONCEPT_SET.replace("_LEXICALCONCEPT_", id)
                        .replace("_LABELPROPERTY_", lexicalizationModel.equals("label") ? SparqlPrefix.RDFS.getUri() + "label" : SparqlPrefix.SKOS.getUri() + "prefLabel")
                        .replace("_DEFAULTLANGUAGE_", defaultLanguageLabel);
            } else if (utilityManager.isLexicalConcept(id)) {
                query = SparqlSelectData.DATA_LEXICAL_CONCEPTS_CHILDREN.replace("_LEXICALCONCEPT_", id)
                        .replace("_LABELPROPERTY_", lexicalizationModel.equals("label") ? SparqlPrefix.RDFS.getUri() + "label" : SparqlPrefix.SKOS.getUri() + "prefLabel")
                        .replace("_DEFAULTLANGUAGE_", defaultLanguageLabel);
            } else {
                throw new ManagerException(id + " is an unknown entity");
            }
        }
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getConceptSets() throws ManagerException {
        String query = SparqlSelectData.DATA_CONCEPT_SETS.replaceAll("_DEFAULTLANGUAGE_", defaultLanguageLabel)
                .replace("_LABELPROPERTY_", lexicalizationModel.equals("label") ? SparqlPrefix.RDFS.getUri() + "label" : SparqlPrefix.SKOS.getUri() + "prefLabel");
        return RDFQueryUtil.evaluateTQuery(query);
    }
    
    public TupleQueryResult getLexicalConcept(String id) throws ManagerException {
        String query = SparqlSelectData.DATA_LEXICAL_CONCEPT.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateTQuery(query);
    }


}
