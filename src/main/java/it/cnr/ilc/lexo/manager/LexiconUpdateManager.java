/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.EtymologicalLinkUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.EtymologyUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.GenericRelationUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LanguageUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalSenseUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LinguisticRelationUpdater;
import it.cnr.ilc.lexo.sparql.Namespace;
import it.cnr.ilc.lexo.sparql.SparqlDeleteData;
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
import java.util.Arrays;
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
    private final String lexvoPrefixWWW = "http://www.lexvo.org/";
    private final String libraryOfCongressPrefixWWW = "http://www.id.loc.gov/vocabulary/";
    private final String lexvoPrefix = "http://lexvo.org/";
    private final String libraryOfCongressPrefix = "http://id.loc.gov/vocabulary/";
    private final String lexinfoCatalog = "http://www.lexinfo.net/ontologies/3.0/lexinfo";
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

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

    public void validateLexicalRel(String rel) throws ManagerException {
        Manager.validateWithEnum("rel", EnumUtil.LexicalRel.class, rel);
    }

    public void validateLexicalEntryStatus(String status) throws ManagerException {
        Manager.validateWithEnum("status", EnumUtil.LexicalEntryStatus.class, status);
    }

    public void validateLexicalEntryLanguage(String lang) throws ManagerException {
        Manager.validateLanguage(lang);
    }

    public void validateGenericReferenceRelation(String relation) throws ManagerException {
        Manager.validateWithEnum("relation", EnumUtil.GenericRelationReference.class, relation);
    }

    public void validateGenericBibliographyRelation(String relation) throws ManagerException {
        Manager.validateWithEnum("relation", EnumUtil.GenericRelationBibliography.class, relation);
    }

    public void validateEtyLink(String type) throws ManagerException {
        Manager.validateWithOntoLexEntity("type", OntoLexEntity.EtyLinkTypes.class, type);
    }

    public boolean validateIRI(String id) throws ManagerException {
        UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
        if (!utilityManager.exists(id)) {
            return false;
        }
        return true;
    }

    public void validateURL(String url, String... urlPrefix) throws ManagerException {
        if (!StringUtil.validateURL(url)) {
            throw new ManagerException(url + " is not a valid url");
        } else {
            if (urlPrefix.length > 0) {
                if (!StringUtil.prefixedURL(url, urlPrefix)) {
                    throw new ManagerException(url + " is not an admissible url");
                }
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

    public void validateEtymologyAttribute(String attribute) throws ManagerException {
        Manager.validateWithEnum("attribute", EnumUtil.EtymologyAttributes.class, attribute);
    }

    public void validateEtymologicalLinkAttribute(String attribute) throws ManagerException {
        Manager.validateWithEnum("attribute", EnumUtil.EtymologicalLinkAttributes.class, attribute);
    }

    public String updateLexiconLanguage(String id, LanguageUpdater lu, String user) throws ManagerException {
        validateLexiconLanguageAttribute(lu.getRelation());
        if (lu.getRelation().equals(EnumUtil.LanguageAttributes.Lexvo.toString())) {
            validateURL(lu.getValue(), lexvoPrefix, libraryOfCongressPrefix, lexvoPrefixWWW, libraryOfCongressPrefixWWW);
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
            return updateLexicalEntry(id, SparqlPrefix.RDF.getPrefix() + leu.getRelation(),
                    (!leu.getValue().equals(OntoLexEntity.LexicalEntryTypes.Etymon.toString())) ? SparqlPrefix.ONTOLEX.getPrefix() + leu.getValue() : SparqlPrefix.ETY.getPrefix() + leu.getValue(),
                    "?" + SparqlVariable.TARGET);
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
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectData.LEXICON_ENTRY_LANGUAGE.replaceAll("_ID_", id))) {

            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LABEL) != null) ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    private String getStatus(String id) {
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectData.LEXICON_ENTRY_STATUS.replaceAll("_ID_", id))) {
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
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_FORM.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));

        return lastupdate;
    }

    public String updateFormType(String id, String leid, String formType) throws ManagerException, UpdateExecutionException {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
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
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_LEXICAL_SENSE.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    public String updateEtymology(String id, EtymologyUpdater eu, String user) throws ManagerException {
        validateEtymologyAttribute(eu.getRelation());
        if (eu.getRelation().equals(EnumUtil.EtymologyAttributes.Note.toString())) {
            return updateEtymology(id, SparqlPrefix.SKOS.getPrefix() + eu.getRelation(), "\"" + eu.getValue() + "\"");
        } else if (eu.getRelation().equals(EnumUtil.EtymologyAttributes.Confidence.toString())) {
            return updateEtymology(id, SparqlPrefix.LEXINFO.getPrefix() + eu.getRelation(), Float.toString(Float.parseFloat(eu.getValue())));
        } else if (eu.getRelation().equals(EnumUtil.EtymologyAttributes.Label.toString())) {
            return updateEtymology(id, SparqlPrefix.RDFS.getPrefix() + eu.getRelation(), "\"" + eu.getValue() + "\"");
        } else if (eu.getRelation().equals(EnumUtil.EtymologyAttributes.HypothesisOf.toString())) {
            return updateEtymology(id, SparqlPrefix.RDFS.getPrefix() + "comment", //eu.getRelation(),
                    "\"" + eu.getValue() + "\"");
        } else {
            return null;
        }
    }

    public String updateEtymologicalLink(String id, EtymologicalLinkUpdater elu, String user) throws ManagerException {
        validateEtymologicalLinkAttribute(elu.getRelation());
        if (elu.getRelation().equals(EnumUtil.EtymologicalLinkAttributes.Note.toString())) {
            return updateEtymologicalLink(id, SparqlPrefix.SKOS.getPrefix() + elu.getRelation(), "\"" + elu.getValue() + "\"");
        } else if (elu.getRelation().equals(EnumUtil.EtymologicalLinkAttributes.Type.toString())) {
            return updateEtymologicalLink(id, SparqlPrefix.ETY.getPrefix() + elu.getRelation(), "\"" + elu.getValue() + "\"");
        } else if (elu.getRelation().equals(EnumUtil.EtymologicalLinkAttributes.Label.toString())) {
            return updateEtymologicalLink(id, SparqlPrefix.RDFS.getPrefix() + elu.getRelation(), "\"" + elu.getValue() + "\"");
        } else {
            return null;
        }
    }

    public String updateEtymology(String id, String relation, String valueToInsert) throws ManagerException, UpdateExecutionException {
        if (valueToInsert.isEmpty()) {
            throw new ManagerException("value cannot be empty");
        }
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_ETYMOLOGY.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    public String updateEtymologicalLink(String id, String relation, String valueToInsert) throws ManagerException, UpdateExecutionException {
        if (valueToInsert.isEmpty()) {
            throw new ManagerException("value cannot be empty");
        }
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_ETYMOLOGICAL_LINK.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", "?" + SparqlVariable.TARGET)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    public String updateLinguisticRelation(String id, LinguisticRelationUpdater lru) throws ManagerException {
        if (lru.getType().equals(EnumUtil.LinguisticRelation.Morphology.toString())) {
            validateMorphology(lru.getRelation(), lru.getValue());
            setMorphologyPrefixes(lru, SparqlPrefix.LEXINFO.getUri(), SparqlPrefix.LEXINFO.getUri(), SparqlPrefix.LEXINFO.getUri());
        } else if (lru.getType().equals(EnumUtil.LinguisticRelation.ConceptRef.toString())) {
            // currently the property ranges over an external url only 
            validateURL(lru.getValue());
            validateConceptRef(lru.getRelation());
            if (lru.getValue().contains(SparqlPrefix.LEX.getUri())) {
                throw new ManagerException("External links supported only");
            } else {
                setPrefixes(lru, SparqlPrefix.ONTOLEX.getUri(), "", "");
            }
        } else if (lru.getType().equals(EnumUtil.LinguisticRelation.EtymologicalLink.toString())) {
            validateEtyLink(lru.getRelation());
            if (validateIRI(lru.getValue())) {
                setPrefixes(lru, SparqlPrefix.ETY.getUri(), SparqlPrefix.LEX.getUri(), SparqlPrefix.LEX.getUri());
            } else {
                validateURL(lru.getValue());
                setPrefixes(lru, SparqlPrefix.ETY.getUri(), "", SparqlPrefix.LEX.getUri());
            }
        } else if (lru.getType().equals(EnumUtil.LinguisticRelation.LexicalRel.toString())) {
            // currently only cognate
            validateLexicalRel(lru.getRelation());
            if (validateIRI(lru.getValue())) {
                setPrefixes(lru, SparqlPrefix.ETY.getUri(), SparqlPrefix.LEX.getUri(),
                        SparqlPrefix.LEX.getUri());
            } else {
                validateURL(lru.getValue());
                setPrefixes(lru, SparqlPrefix.ETY.getUri(), "",
                        SparqlPrefix.LEX.getUri());
            }
        } else {
            throw new ManagerException(lru.getType() + " is not a valid relation type");
        }
        return (lru.getCurrentValue() != null)
                ? (lru.getCurrentValue().isEmpty()
                ? createLinguisticRelation(id, lru.getRelation(), lru.getValue())
                : updateLinguisticRelation(id, lru.getRelation(), lru.getValue(), lru.getCurrentValue()))
                : createLinguisticRelation(id, lru.getRelation(), lru.getValue());
    }

    private void setPrefixes(LinguisticRelationUpdater lru, String... prefix) throws ManagerException {
        lru.setRelation("<" + prefix[0] + lru.getRelation() + ">");
        lru.setValue("<" + prefix[1] + lru.getValue() + ">");
        if (lru.getCurrentValue() != null) {
            if (!lru.getCurrentValue().isEmpty()) {
                if (validateIRI(lru.getCurrentValue())) {
                    lru.setCurrentValue("<" + prefix[2] + lru.getCurrentValue() + ">");
                } else {
                    lru.setCurrentValue("<" + lru.getCurrentValue() + ">");
                }
            }
        }
    }

    private void setMorphologyPrefixes(LinguisticRelationUpdater lru, String... prefix) throws ManagerException {
        lru.setRelation("<" + prefix[0] + lru.getRelation() + ">");
        lru.setValue("<" + prefix[1] + lru.getValue() + ">");
        if (lru.getCurrentValue() != null) {
            if (!lru.getCurrentValue().isEmpty()) {
                lru.setCurrentValue("<" + prefix[2] + lru.getCurrentValue() + ">");
            }
        }
    }

    public String createLinguisticRelation(String id, String relation, String valueToInsert) throws ManagerException, UpdateExecutionException {
        // TODO: it should be verified if the relation can be applied to the id type (for example, denotes requires the id type to be LexicalEntry
        if (relation.contains(SparqlPrefix.ETY.getUri() + EnumUtil.LexicalRel.Cognate.toString())) {
            createCognate(valueToInsert);
        }
        return updateLinguisticRelation(SparqlInsertData.CREATE_LINGUISTIC_RELATION, id, relation,
                valueToInsert, "?" + SparqlVariable.TARGET);
    }

    private void createCognate(String valueToInsert) throws ManagerException {
        if (valueToInsert.contains(SparqlPrefix.LEX.getUri())) {
            RDFQueryUtil.update(SparqlInsertData.CREATE_COGNATE_TYPE.replace("_ID_", valueToInsert));
        }
    }

    public String updateLinguisticRelation(String id, String relation, String valueToInsert, String currentValue) throws ManagerException, UpdateExecutionException {
        if (relation.contains(SparqlPrefix.ETY.getUri() + EnumUtil.LexicalRel.Cognate.toString())) {
            updateCognate(valueToInsert, currentValue);
        }
        if (ManagerFactory.getManager(UtilityManager.class).existsLinguisticRelation(id, relation, currentValue)) {
            return updateLinguisticRelation(SparqlUpdateData.UPDATE_LINGUISTIC_RELATION, id, relation,
                    valueToInsert, currentValue);
        } else {
            throw new ManagerException("IRI " + id + " does not exist or <" + id + ", " + relation + ", " + currentValue + "> does not exist");
        }
    }

    private void updateCognate(String valueToInsert, String currentValue) throws ManagerException {
        //String iri = currentValue.replace("<", "").replace(">", "");
        if (currentValue.contains(SparqlPrefix.LEX.getUri())) {
            if (!ManagerFactory.getManager(UtilityManager.class).isCognate(currentValue, 1)) {
                // currentValue is involved in this cognate relation only, so its Cognate type is removed
                RDFQueryUtil.update(SparqlDeleteData.DELETE_COGNATE_TYPE.replaceAll("_ID_", currentValue));
            }
            if (valueToInsert.contains(SparqlPrefix.LEX.getUri())) {
                createCognate(valueToInsert);
            }
        } else {
            if (valueToInsert.contains(SparqlPrefix.LEX.getUri())) {
                createCognate(valueToInsert);
            }
        }
    }

    public String updateLinguisticRelation(String query, String id, String relation, String valueToInsert, String valueToDelete) throws ManagerException, UpdateExecutionException {
        if (valueToInsert.isEmpty() || valueToDelete.isEmpty()) {
            throw new ManagerException("values cannot be empty");
        }
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(query.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", valueToDelete)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    public String updateGenericRelation(String id, GenericRelationUpdater gru) throws ManagerException {
        String _id = "";
        if (gru.getType().equals(EnumUtil.GenericRelation.Reference.toString())) {
            validateGenericReferenceRelation(gru.getRelation());
            if (validateIRI(gru.getValue())) {
                // CONSTRAINT: sameAs holds with external links only
                if (gru.getRelation().equals(EnumUtil.GenericRelationReference.sameAs.toString())) {
                    throw new ManagerException(gru.getRelation() + " links to external links only");
                }
                String pf = "";
                if (gru.getCurrentValue() != null) {
                    pf = validateIRI(gru.getCurrentValue()) ? SparqlPrefix.LEX.getUri() : "";
                }
                setPrefixes(gru, false,
                        gru.getRelation().equals(EnumUtil.GenericRelationReference.sameAs.toString()) ? SparqlPrefix.OWL.getUri() : SparqlPrefix.RDFS.getUri(),
                        SparqlPrefix.LEX.getUri(),
                        pf);
            } else {
                validateURL(gru.getValue());
                String pf = "";
                if (gru.getCurrentValue() != null) {
                    pf = (validateIRI(gru.getCurrentValue()) ? SparqlPrefix.LEX.getUri() : "");
                }
                setPrefixes(gru, false, gru.getRelation().equals(EnumUtil.GenericRelationReference.sameAs.toString()) ? SparqlPrefix.OWL.getUri() : SparqlPrefix.RDFS.getUri(),
                        "",
                        pf);
            }
            _id = SparqlPrefix.LEX.getUri() + id;
        } else if (gru.getType().equals(EnumUtil.GenericRelation.Bibliography.toString())) {
            validateGenericBibliographyRelation(gru.getRelation());
            setPrefixes(gru, true,
                    gru.getRelation().equals(EnumUtil.GenericRelationBibliography.textualReference.toString()) ? SparqlPrefix.RDFS.getUri() : SparqlPrefix.SKOS.getUri(),
                    "",
                    "");
            _id = SparqlPrefix.LEXBIB.getUri() + id;
        } else {
            throw new ManagerException(gru.getType() + " is not a valid relation type");
        }
        return (gru.getCurrentValue() != null)
                ? (gru.getCurrentValue().isEmpty()
                ? createGenericRelation(_id, gru.getRelation(), gru.getValue())
                : updateGenericRelation(_id, gru.getRelation(), gru.getValue(), gru.getCurrentValue()))
                : createGenericRelation(_id, gru.getRelation(), gru.getValue());
    }

    private void setPrefixes(GenericRelationUpdater gru, boolean literal, String... prefix) {
        gru.setRelation("<" + prefix[0] + gru.getRelation() + ">");
        gru.setValue((literal ? "\"" : "<") + prefix[1] + gru.getValue() + (literal ? "\"" : ">"));
        if (gru.getCurrentValue() != null) {
            if (!gru.getCurrentValue().isEmpty()) {
                gru.setCurrentValue((literal ? "\"" : "<") + prefix[2] + gru.getCurrentValue() + (literal ? "\"" : ">"));
            }
        }
    }

    public String createGenericRelation(String id, String relation, String valueToInsert) throws ManagerException, UpdateExecutionException {
        // TODO: it should be verified if the relation can be applied to the id type (for example, sameAs must link same types)
        return updateGenericRelation(SparqlInsertData.CREATE_GENERIC_RELATION, id, relation,
                valueToInsert, "?" + SparqlVariable.TARGET);
    }

    public String updateGenericRelation(String id, String relation, String valueToInsert, String currentValue) throws ManagerException, UpdateExecutionException {
        if (ManagerFactory.getManager(UtilityManager.class).existsGenericRelation(id, relation, currentValue)) {
            return updateGenericRelation(SparqlUpdateData.UPDATE_GENERIC_RELATION, id, relation,
                    valueToInsert, currentValue);
        } else {
            throw new ManagerException("IRI " + id + " does not exist or <" + id + ", " + relation + ", " + currentValue + "> does not exist");
        }
    }

    public String updateGenericRelation(String query, String id, String relation, String valueToInsert, String valueToDelete) throws ManagerException, UpdateExecutionException {
        if (valueToInsert.isEmpty() || valueToDelete.isEmpty()) {
            throw new ManagerException("values cannot be empty");
        }
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(query.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", valueToDelete)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }
}
