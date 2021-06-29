/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.GenericRelationUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LanguageUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalSenseUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LinguisticRelationUpdater;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.sparql.SparqlUpdateData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.OntoLexEntity;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import it.cnr.ilc.lexo.util.StringUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.UpdateExecutionException;

/**
 *
 * @author andreabellandi
 */
public final class LexiconUpdateManager implements Manager, Cached {

    public final String URL_PATTERN = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
    public final Pattern pattern = Pattern.compile(URL_PATTERN);
    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");
    private final String lexvoPrefix = "https://id.loc.gov/vocabulary/";
    private final String libraryOfCongressPrefix = "http://id.loc.gov/vocabulary/";
    private final String lexinfoCatalog = "http://www.lexinfo.net/ontologies/3.0/lexinfo";
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

    public String getNamespace() {
        return namespace;
    }

    @Override
    public void reloadCache() {
    }

    public void validateLexiconLanguageAttribute(String attribute) throws ManagerException {
        Manager.validateWithEnum("attribute", EnumUtil.LanguageAttributes.class, attribute);
    }

    public void validateLexicalEntryAttribute(String attribute) throws ManagerException {
        Manager.validateWithEnum("attribute", EnumUtil.LexicalEntryAttributes.class, attribute);
    }

    public void validateLexicalEntryType(String type) throws ManagerException {
        Manager.validateWithOntoLexEntity("type", OntoLexEntity.LexicalEntryTypes.class, type);
    }

    public void validateFormType(String type) throws ManagerException {
        Manager.validateWithOntoLexEntity("type", OntoLexEntity.FormTypes.class, type);
    }

    public void validateConceptRef(String type) throws ManagerException {
        Manager.validateWithOntoLexEntity("type", OntoLexEntity.ReferenceTypes.class, type);
    }

    public void validateLexicalEntryStatus(String status) throws ManagerException {
        Manager.validateWithEnum("status", EnumUtil.LexicalEntryStatus.class, status);
    }

    public void validateLexicalEntryLanguage(String lang) throws ManagerException {
        Manager.validateLanguage(lang);
    }

    public void validateURL(String url, String... urlPrefix) throws ManagerException {
        if (!StringUtil.validateURL(url)) {
            throw new ManagerException(url + " is not a valid url");
        } else {
            if (!StringUtil.prefixedURL(url, urlPrefix)) {
                throw new ManagerException(url + " is not an admissible url");
            }
        }
    }

    public void languageUpdatePermission(String id) throws ManagerException {
        if (ManagerFactory.getManager(UtilityManager.class).lexicalEntriesNumberByLanguage(id) > 0) {
            throw new ManagerException(" Language cannot be modified or deleted. Remove all its entries first");
        }
    }

    public void validateMorphology(String trait, String value) throws ManagerException {
        Manager.validateMorphology(trait, value);
    }

    public void validateFormAttribute(String attribute) throws ManagerException {
        Manager.validateWithEnum("attribute", EnumUtil.FormAttributes.class, attribute);
    }

    public void validateLexicalSenseAttribute(String attribute) throws ManagerException {
        Manager.validateWithEnum("attribute", EnumUtil.LexicalSenseAttributes.class, attribute);
    }

    public String updateLexiconLanguage(String id, LanguageUpdater lu, String user) throws ManagerException {
        validateLexiconLanguageAttribute(lu.getRelation());
        if (lu.getRelation().equals(EnumUtil.LanguageAttributes.Lexvo.toString())) {
            validateURL(lu.getValue(), lexvoPrefix, libraryOfCongressPrefix);
            return updateLexiconLanguage(id, SparqlPrefix.DCT.getPrefix() + "language", "<" + lu.getValue() + ">");
        } else if (lu.getRelation().equals(EnumUtil.LanguageAttributes.Description.toString())) {
            return updateLexiconLanguage(id, SparqlPrefix.DCT.getPrefix() + lu.getRelation(), "\"" + lu.getValue() + "\"");
        } else if (lu.getRelation().equals(EnumUtil.LanguageAttributes.Language.toString())) {
            languageUpdatePermission(id);
            return updateLexiconLanguage(id, SparqlPrefix.LIME.getPrefix() + lu.getRelation(), "\"" + lu.getValue() + "\"");
        } else {
            return null;
        }
    }

    public String updateLexiconLanguage(String id, String relation, String valueToInsert) throws ManagerException, UpdateExecutionException {
        if (valueToInsert.isEmpty()) {
            throw new ManagerException("value cannot be empty");
        }
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlUpdateData.UPDATE_LEXICON_LANGUAGE.replaceAll("_ID_", id)
//                        .replaceAll("_RELATION_", relation)
//                        .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
//                        .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
//                        .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_LEXICON_LANGUAGE.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));

        return lastupdate;
    }

    public String updateLexicalEntry(String id, String relation, String valueToInsert, String valueToDelete) throws ManagerException, UpdateExecutionException {
        if (valueToInsert.isEmpty()) {
            throw new ManagerException("value cannot be empty");
        }
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlUpdateData.UPDATE_LEXICAL_ENTRY.replaceAll("_ID_", id)
//                        .replaceAll("_RELATION_", relation)
//                        .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
//                        .replaceAll("_VALUE_TO_DELETE_", valueToDelete)
//                        .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_LEXICAL_ENTRY.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", valueToDelete)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    public String updateLexicalEntry(String id, LexicalEntryUpdater leu, String user) throws ManagerException {
        validateLexicalEntryAttribute(leu.getRelation());
        if (leu.getRelation().equals(EnumUtil.LexicalEntryAttributes.Label.toString())) {
            String lang = ManagerFactory.getManager(UtilityManager.class).getLanguage(id);
            return updateLexicalEntry(id, SparqlPrefix.RDFS.getPrefix() + leu.getRelation(),
                    (lang != null) ? ("\"" + leu.getValue() + "\"@" + lang) : "\"" + leu.getValue() + "\"", "?" + SparqlVariable.TARGET);
        } else if (leu.getRelation().equals(EnumUtil.LexicalEntryAttributes.Type.toString())) {
            validateLexicalEntryType(leu.getValue());
            return updateLexicalEntry(id, SparqlPrefix.RDF.getPrefix() + leu.getRelation(), SparqlPrefix.ONTOLEX.getPrefix() + leu.getValue(), "?" + SparqlVariable.TARGET);
        } else if (leu.getRelation().equals(EnumUtil.LexicalEntryAttributes.Status.toString())) {
            validateLexicalEntryStatus(leu.getValue());
            if (leu.getValue().isEmpty()) {
                throw new ManagerException("status cannot be empty");
            }
            return updateStatus(id, leu, user);
        } else if (leu.getRelation().equals(EnumUtil.LexicalEntryAttributes.Language.toString())) {
            validateLexicalEntryLanguage(leu.getValue());
            return updateLanguage(id, leu);
        } else if (leu.getRelation().equals(EnumUtil.LexicalEntryAttributes.Note.toString())) {
            return updateLexicalEntry(id, SparqlPrefix.SKOS.getPrefix() + leu.getRelation(), "\"" + leu.getValue() + "\"", "?" + SparqlVariable.TARGET);
        } else {
            return null;
        }
    }

    private String updateLanguage(String id, String label, String lang) {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlUpdateData.UPDATE_LEXICAL_ENTRY_LANGUAGE.replaceAll("_ID_", id)
//                        .replaceAll("_LABEL_", label)
//                        .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\"")
//                        .replaceAll("_LANG_", lang));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_LEXICAL_ENTRY_LANGUAGE.replaceAll("_ID_", id)
                .replaceAll("_LABEL_", label)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\"")
                .replaceAll("_LANG_", lang));

        return lastupdate;
    }

    private String updateLanguage(String id, LexicalEntryUpdater leu) throws QueryEvaluationException {
        String label = getLabel(id);
        if (label != null) {
            return updateLanguage(id, label, leu.getValue());
        }
        return null;
    }

    public String getLabel(String id) throws QueryEvaluationException {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.LEXICON_ENTRY_LANGUAGE.replaceAll("_ID_", id));
//        try (TupleQueryResult result = tupleQuery.evaluate()) {
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectData.LEXICON_ENTRY_LANGUAGE.replaceAll("_ID_", id))) {

            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LABEL) != null) ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    private String getStatus(String id) {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.LEXICON_ENTRY_STATUS.replaceAll("_ID_", id));
//        try (TupleQueryResult result = tupleQuery.evaluate()) {
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectData.LEXICON_ENTRY_STATUS.replaceAll("_ID_", id))) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LABEL) != null) ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    private String updateStatus(String id, LexicalEntryUpdater leu, String user) throws QueryEvaluationException, ManagerException {
        String status = getStatus(id);
        if (status != null) {
            if (checkStatus(status, leu.getValue())) {
                if (compareAdmissibleStatus(status, leu.getValue()) == 1) {
                    return statusForewarding(id, leu.getValue(), status, user);
                } else {
                    return statusBackwarding(id, leu.getValue(), status, user);
                }
            } else {
                throw new ManagerException("The current status " + status + " cannot be changed to " + leu.getValue());
            }
        }
        return null;
    }

    private String statusForewarding(String id, String status, String currentStatus, String user) throws QueryEvaluationException {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlUpdateData.UPDATE_LEXICAL_ENTRY_FOREWARDING_STATUS.replaceAll("_ID_", id)
//                        .replaceAll("_NEW_ROLE_", getRoleName(status))
//                        .replaceAll("_NEW_DATE_", getDateName(status))
//                        .replaceAll("_USER_", "\"" + user + "\"")
//                        .replaceAll("_STATUS_", "\"" + status + "\"")
//                        .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_LEXICAL_ENTRY_FOREWARDING_STATUS.replaceAll("_ID_", id)
                .replaceAll("_NEW_ROLE_", getRoleName(status))
                .replaceAll("_NEW_DATE_", getDateName(status))
                .replaceAll("_USER_", "\"" + user + "\"")
                .replaceAll("_STATUS_", "\"" + status + "\"")
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));

        return lastupdate;
    }

    private String statusBackwarding(String id, String status, String currentStatus, String user) throws QueryEvaluationException {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlUpdateData.UPDATE_LEXICAL_ENTRY_BACKWARDING_STATUS.replaceAll("_ID_", id)
//                        .replaceAll("_CURRENT_ROLE_", getRoleName(currentStatus))
//                        .replaceAll("_CURRENT_DATE_", getDateName(currentStatus))
//                        .replaceAll("_NEW_ROLE_", getRoleName(status))
//                        .replaceAll("_NEW_DATE_", getDateName(status))
//                        .replaceAll("_USER_", "\"" + user + "\"")
//                        .replaceAll("_STATUS_", "\"" + status + "\"")
//                        .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_LEXICAL_ENTRY_BACKWARDING_STATUS.replaceAll("_ID_", id)
                .replaceAll("_CURRENT_ROLE_", getRoleName(currentStatus))
                .replaceAll("_CURRENT_DATE_", getDateName(currentStatus))
                .replaceAll("_NEW_ROLE_", getRoleName(status))
                .replaceAll("_NEW_DATE_", getDateName(status))
                .replaceAll("_USER_", "\"" + user + "\"")
                .replaceAll("_STATUS_", "\"" + status + "\"")
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    private String getRoleName(String status) {
        if (status.equals(EnumUtil.LexicalEntryStatus.Completed.toString())) {
            return SparqlPrefix.DCT.getPrefix() + "author";
        } else if (status.equals(EnumUtil.LexicalEntryStatus.Reviewed.toString())) {
            return SparqlPrefix.LOC.getPrefix() + "rev";
        } else if (status.equals(EnumUtil.LexicalEntryStatus.Working.toString())) {
            return SparqlPrefix.DCT.getPrefix() + "creator";
        } else {
            return null;
        }
    }

    private String getDateName(String status) {
        if (status.equals(EnumUtil.LexicalEntryStatus.Completed.toString())) {
            return SparqlPrefix.DCT.getPrefix() + "dateSubmitted";
        } else if (status.equals(EnumUtil.LexicalEntryStatus.Reviewed.toString())) {
            return SparqlPrefix.DCT.getPrefix() + "dateAccepted";
        } else if (status.equals(EnumUtil.LexicalEntryStatus.Working.toString())) {
            return SparqlPrefix.DCT.getPrefix() + "created";
        } else {
            return null;
        }
    }

    private boolean checkStatus(String current, String next) {
        if (current.equals(next)) {
            return false;
        } else if (current.equals(EnumUtil.LexicalEntryStatus.Working.toString())
                && (next.equals(EnumUtil.LexicalEntryStatus.Reviewed.toString()))) {
            return false;
        } else if (current.equals(EnumUtil.LexicalEntryStatus.Reviewed.toString())
                && (next.equals(EnumUtil.LexicalEntryStatus.Working.toString()))) {
            return false;
        } else {
            return true;
        }
    }

    // 1 next > current; -1 current > next
    private int compareAdmissibleStatus(String current, String next) {
        if (current.equals(EnumUtil.LexicalEntryStatus.Working.toString())) {
            if (next.equals(EnumUtil.LexicalEntryStatus.Completed.toString())) {
                return 1;
            } else if (next.equals(EnumUtil.LexicalEntryStatus.Reviewed.toString())) {
                return 1;
            }
        } else if (current.equals(EnumUtil.LexicalEntryStatus.Completed.toString())) {
            if (next.equals(EnumUtil.LexicalEntryStatus.Working.toString())) {
                return -1;
            } else if (next.equals(EnumUtil.LexicalEntryStatus.Reviewed.toString())) {
                return 1;
            }
        } else if (current.equals(EnumUtil.LexicalEntryStatus.Reviewed.toString())) {
            if (next.equals(EnumUtil.LexicalEntryStatus.Completed.toString())) {
                return -1;
            } else if (next.equals(EnumUtil.LexicalEntryStatus.Working.toString())) {
                return -1;
            }
        }
        return 0;
    }

    public String updateForm(String id, FormUpdater fu, String user) throws ManagerException {
        UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
        validateFormAttribute(fu.getRelation());
        if (fu.getRelation().equals(EnumUtil.FormAttributes.Type.toString())) {
            validateFormType(fu.getValue());
            String leid = utilityManager.getLexicalEntryByForm(id);
            return updateFormType(id, leid, fu.getValue());
        } else if (fu.getRelation().equals(EnumUtil.FormAttributes.Note.toString())) {
            return updateForm(id, SparqlPrefix.SKOS.getPrefix() + fu.getRelation(), "\"" + fu.getValue() + "\"");
        } else if (fu.getRelation().equals(EnumUtil.FormAttributes.WrittenRep.toString())) {
            String lang = utilityManager.getLanguage(id);
            return updateForm(id, SparqlPrefix.ONTOLEX.getPrefix() + fu.getRelation(),
                    (lang != null) ? ("\"" + fu.getValue() + "\"@" + lang) : "\"" + fu.getValue() + "\"");
        } else if (fu.getRelation().equals(EnumUtil.FormAttributes.PhoneticRep.toString())) {
            String lang = utilityManager.getLanguage(id);
            return updateForm(id, SparqlPrefix.ONTOLEX.getPrefix() + fu.getRelation(),
                    (lang != null) ? ("\"" + fu.getValue() + "\"@" + lang) : "\"" + fu.getValue() + "\"");
        } else if (fu.getRelation().equals(EnumUtil.FormAttributes.Transliteration.toString())) {
            String lang = utilityManager.getLanguage(id);
            return updateForm(id, SparqlPrefix.LEXINFO.getPrefix() + fu.getRelation(),
                    (lang != null) ? ("\"" + fu.getValue() + "\"@" + lang) : "\"" + fu.getValue() + "\"");
        } else if (fu.getRelation().equals(EnumUtil.FormAttributes.Segmentation.toString())) {
            String lang = utilityManager.getLanguage(id);
            return updateForm(id, SparqlPrefix.LEXINFO.getPrefix() + fu.getRelation(),
                    (lang != null) ? ("\"" + fu.getValue() + "\"@" + lang) : "\"" + fu.getValue() + "\"");
        } else if (fu.getRelation().equals(EnumUtil.FormAttributes.Pronunciation.toString())) {
            String lang = utilityManager.getLanguage(id);
            return updateForm(id, SparqlPrefix.LEXINFO.getPrefix() + fu.getRelation(),
                    (lang != null) ? ("\"" + fu.getValue() + "\"@" + lang) : "\"" + fu.getValue() + "\"");
        } else if (fu.getRelation().equals(EnumUtil.FormAttributes.Romanization.toString())) {
            String lang = utilityManager.getLanguage(id);
            return updateForm(id, SparqlPrefix.LEXINFO.getPrefix() + fu.getRelation(),
                    (lang != null) ? ("\"" + fu.getValue() + "\"@" + lang) : "\"" + fu.getValue() + "\"");
        } else {
            return null;
        }
    }

    public String updateForm(String id, String relation, String valueToInsert) throws ManagerException, UpdateExecutionException {
        if (valueToInsert.isEmpty()) {
            throw new ManagerException("value cannot be empty");
        }
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlUpdateData.UPDATE_FORM.replaceAll("_ID_", id)
//                        .replaceAll("_RELATION_", relation)
//                        .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
//                        .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
//                        .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_FORM.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));

        return lastupdate;
    }

    public String updateFormType(String id, String leid, String formType) throws ManagerException, UpdateExecutionException {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlUpdateData.UPDATE_FORM_TYPE.replaceAll("_ID_", id)
//                        .replaceAll("_LEID_", leid)
//                        .replaceAll("_FORM_TYPE_", formType)
//                        .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_FORM_TYPE.replaceAll("_ID_", id)
                .replaceAll("_LEID_", leid)
                .replaceAll("_FORM_TYPE_", formType)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    public String updateLexicalSense(String id, LexicalSenseUpdater lsu, String user) throws ManagerException {
        validateLexicalSenseAttribute(lsu.getRelation());
        if (lsu.getRelation().equals(EnumUtil.LexicalSenseAttributes.Note.toString())) {
            return updateLexicalSense(id, SparqlPrefix.SKOS.getPrefix() + lsu.getRelation(), "\"" + lsu.getValue() + "\"");
        } else if (lsu.getRelation().equals(EnumUtil.LexicalSenseAttributes.Definition.toString())) {
            return updateLexicalSense(id, SparqlPrefix.SKOS.getPrefix() + lsu.getRelation(), "\"" + lsu.getValue() + "\"");
        } else if (lsu.getRelation().equals(EnumUtil.LexicalSenseAttributes.Description.toString())) {
            return updateLexicalSense(id, SparqlPrefix.LEXINFO.getPrefix() + lsu.getRelation(), "\"" + lsu.getValue() + "\"");
        } else if (lsu.getRelation().equals(EnumUtil.LexicalSenseAttributes.Explanation.toString())) {
            return updateLexicalSense(id, SparqlPrefix.LEXINFO.getPrefix() + lsu.getRelation(), "\"" + lsu.getValue() + "\"");
        } else if (lsu.getRelation().equals(EnumUtil.LexicalSenseAttributes.Gloss.toString())) {
            return updateLexicalSense(id, SparqlPrefix.LEXINFO.getPrefix() + lsu.getRelation(), "\"" + lsu.getValue() + "\"");
        } else if (lsu.getRelation().equals(EnumUtil.LexicalSenseAttributes.Reference.toString())) {
            validateURL(lsu.getValue());
            return updateLexicalSense(id, SparqlPrefix.ONTOLEX.getPrefix() + lsu.getRelation(), "<" + lsu.getValue() + ">");
        } else if (lsu.getRelation().equals(EnumUtil.LexicalSenseAttributes.SenseExample.toString())) {
            return updateLexicalSense(id, SparqlPrefix.LEXINFO.getPrefix() + lsu.getRelation(), "\"" + lsu.getValue() + "\"");
        } else if (lsu.getRelation().equals(EnumUtil.LexicalSenseAttributes.SenseTranslation.toString())) {
            return updateLexicalSense(id, SparqlPrefix.LEXINFO.getPrefix() + lsu.getRelation(), "\"" + lsu.getValue() + "\"");
        } else if (lsu.getRelation().equals(EnumUtil.LexicalSenseAttributes.Topic.toString())) {
            validateURL(lsu.getValue());
            return updateLexicalSense(id, SparqlPrefix.DCT.getPrefix() + lsu.getRelation(), "<" + lsu.getValue() + ">");
        } else if (lsu.getRelation().equals(EnumUtil.LexicalSenseAttributes.Usage.toString())) {
            return updateLexicalSense(id, SparqlPrefix.ONTOLEX.getPrefix() + lsu.getRelation(), "[ rdf:value \"" + lsu.getValue() + "\" ]");
        } else {
            return null;
        }
    }

    public String updateLexicalSense(String id, String relation, String valueToInsert) throws ManagerException, UpdateExecutionException {
        if (valueToInsert.isEmpty()) {
            throw new ManagerException("value cannot be empty");
        }
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlUpdateData.UPDATE_LEXICAL_SENSE.replaceAll("_ID_", id)
//                        .replaceAll("_RELATION_", relation)
//                        .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
//                        .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
//                        .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_LEXICAL_SENSE.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    public String updateLinguisticRelation(String id, LinguisticRelationUpdater lru) throws ManagerException {
        if (lru.getType().equals(EnumUtil.LinguisticRelation.Morphology.toString())) {
            validateMorphology(lru.getRelation(), lru.getValue());
            setPrefixes(lru, SparqlPrefix.LEXINFO.getUri(), SparqlPrefix.LEXINFO.getUri(), SparqlPrefix.LEXINFO.getUri());
        } else if (lru.getType().equals(EnumUtil.LinguisticRelation.ConceptRef.toString())) {
            // currently the property ranges over an external url only 
            validateURL(lru.getValue());
            validateConceptRef(lru.getRelation());
            setPrefixes(lru, SparqlPrefix.ONTOLEX.getUri(), "", "");
        } else {
            throw new ManagerException(lru.getType() + " is not a valid relation type");
        }
        return (lru.getCurrentValue() != null)
                ? (lru.getCurrentValue().isEmpty()
                ? createLinguisticRelation(id, lru.getRelation(), lru.getValue())
                : updateLinguisticRelation(id, lru.getRelation(), lru.getValue(), lru.getCurrentValue()))
                : createLinguisticRelation(id, lru.getRelation(), lru.getValue());
    }

    private void setPrefixes(LinguisticRelationUpdater lru, String... prefix) {
        lru.setRelation("<" + prefix[0] + lru.getRelation() + ">");
        lru.setValue("<" + prefix[1] + lru.getValue() + ">");
        if (lru.getCurrentValue() != null) {
            if (!lru.getCurrentValue().isEmpty()) {
                lru.setCurrentValue("<" + prefix[2] + lru.getCurrentValue() + ">");
            }
        }
    }

    public String createLinguisticRelation(String id, String relation, String valueToInsert) throws ManagerException, UpdateExecutionException {
        return updateLinguisticRelation(SparqlInsertData.CREATE_LINGUISTIC_RELATION, id, relation,
                valueToInsert, "?" + SparqlVariable.TARGET);
    }

    public String updateLinguisticRelation(String id, String relation, String valueToInsert, String currentValue) throws ManagerException, UpdateExecutionException {
        if (ManagerFactory.getManager(UtilityManager.class).existsLinguisticRelation(id, relation, currentValue)) {
            return updateLinguisticRelation(SparqlUpdateData.UPDATE_LINGUISTIC_RELATION, id, relation,
                    valueToInsert, currentValue);
        } else {
            throw new ManagerException("IRI " + id + " does not exist or <" + id + ", " + relation + ", " + currentValue + "> does not exist");
        }
    }

    public String updateLinguisticRelation(String query, String id, String relation, String valueToInsert, String valueToDelete) throws ManagerException, UpdateExecutionException {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                query.replaceAll("_ID_", id)
//                        .replaceAll("_RELATION_", relation)
//                        .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
//                        .replaceAll("_VALUE_TO_DELETE_", valueToDelete)
//                        .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
//        updateOperation.execute();
        RDFQueryUtil.update(query.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", valueToDelete)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    public String updateGenericRelation(String id, GenericRelationUpdater gru) throws ManagerException {
        UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
        if (gru.getType().equals(EnumUtil.GenericRelation.Reference.toString())) {
            if (!utilityManager.exists(id)) {
                throw new ManagerException(id + " does not exist in the lexicon");
            } else {
                validateURL(gru.getValue());
            }
            return updateGenericRelation(id, SparqlPrefix.RDFS.getPrefix() + gru.getRelation(), gru.getValue());
        } else {
            throw new ManagerException(gru.getType() + " is not a valid relation type");
        }
    }

    public String updateGenericRelation(String id, String relation, String valueToInsert) throws ManagerException, UpdateExecutionException {
        if (valueToInsert.isEmpty()) {
            throw new ManagerException("value cannot be empty");
        }
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        Update updateOperation = GraphDbUtil.getConnection().prepareUpdate(QueryLanguage.SPARQL,
//                SparqlUpdateData.UPDATE_GENERIC_RELATION.replaceAll("_ID_", id)
//                        .replaceAll("_RELATION_", relation)
//                        .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
//                        .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
//                        .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
//        updateOperation.execute();
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_GENERIC_RELATION.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }
}
