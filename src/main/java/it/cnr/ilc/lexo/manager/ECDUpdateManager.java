/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDEntryUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDFormUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDMeaningUpdater;
import it.cnr.ilc.lexo.service.data.output.Entity;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.sparql.SparqlUpdateData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.OntoLexEntity;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class ECDUpdateManager implements Manager, Cached {

    static final Logger logger = LoggerFactory.getLogger(ECDUpdateManager.class.getName());
    private final String idInstancePrefix = LexOProperties.getProperty("repository.instance.id");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

    @Override
    public void reloadCache() {

    }

    public void validateECDEntryAttribute(String attribute) throws ManagerException {
        Manager.validateWithEnum("attribute", OntoLexEntity.DictionaryEntryAttributes.class, attribute);
    }

    public void validateECDMeaningAttribute(String attribute) throws ManagerException {
        Manager.validateWithEnum("attribute", OntoLexEntity.ECDMeaningAttributes.class, attribute);
    }

    public void validateECDFormAttribute(String attribute) throws ManagerException {
        Manager.validateWithEnum("attribute", OntoLexEntity.ECDFormAttributes.class, attribute);
    }

    public void validateDictionaryEntryLanguage(String lang) throws ManagerException {
        Manager.validateDictLanguage(lang);
    }

    public void validateLexicalEntryStatus(String status) throws ManagerException {
        Manager.validateWithEnum("status", EnumUtil.LexicalEntryStatus.class, status);
    }

    private void validateConfidenceValue(String value) throws ManagerException {
        double v = Double.parseDouble(value);
        if ((v < 0.0) || (v > 1.0)) {
            throw new ManagerException("the confidence value is not valid");
        }
    }

    public String updateECDEntry(String id, ECDEntryUpdater ecdeu, String user) throws ManagerException {
        if (!ecdeu.getValue().isEmpty()) {
            validateECDEntryAttribute(ecdeu.getRelation());
            if (ecdeu.getRelation().equals(OntoLexEntity.DictionaryEntryAttributes.Label.toString())) {
                String lang = ManagerFactory.getManager(UtilityManager.class).getDictLanguage(id);
                if (lang == null) {
                    throw new ManagerException("The language of the ECD entry could not be found");
                }
                return updateECDEntry(id, ecdeu.getRelation(), "\"" + ecdeu.getValue() + "\"@" + lang, "?" + SparqlVariable.TARGET);
            } else if (ecdeu.getRelation().equals(OntoLexEntity.DictionaryEntryAttributes.PoS.toString())) {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                String le = utilityManager.getLexicalEntryByECDPoS(id, ecdeu.getOldPoS());
                if (ecdeu.getOldPoS() != null) {
                    // update the pos
                    if (le != null) {
                        return updateECDEntryPoS(le, ecdeu.getValue(), ecdeu.getOldPoS());
                    } else {
                        throw new ManagerException("The required part of speech " + ecdeu.getOldPoS() + " does not exist");
                    }
                } else {
                    // add new pos 
                    String lang = ManagerFactory.getManager(UtilityManager.class).getDictLanguage(id);
                    if (lang == null) {
                        throw new ManagerException("The language of the ECD entry could not be found");
                    }
                    String type = utilityManager.getLexicalEntryType(le);
                    String lexicon = utilityManager.getLexiconIDByLexicalEntry(le);
                    String label = utilityManager.getLabel(le);
                    Entity metadata = utilityManager.getMetadata(le);
                    if (type == null) {
                        throw new ManagerException("The type of the lexical entry " + le + " is not defined");
                    }
                    if (lexicon == null) {
                        throw new ManagerException("The lexical entry " + le + " does not belong to any lexicon");
                    }
                    if (label == null) {
                        throw new ManagerException("The lexical entry " + le + " has not label");
                    }
                    return createLexicalEntryForECDEntryNewPoS(id, le, lexicon, type, ecdeu.getValue(), label, metadata, lang);
                }
            } else if (ecdeu.getRelation().equals(OntoLexEntity.DictionaryEntryAttributes.Note.toString())) {
                return updateECDEntry(id, ecdeu.getRelation(), "\"" + ecdeu.getValue() + "\"", "?" + SparqlVariable.TARGET);
            } else if (ecdeu.getRelation().equals(OntoLexEntity.DictionaryEntryAttributes.Status.toString())) {
                validateLexicalEntryStatus(ecdeu.getValue());
                if (ecdeu.getValue().isEmpty()) {
                    throw new ManagerException("status cannot be empty");
                }
                return updateStatus(id, ecdeu, user);
            } else if (ecdeu.getRelation().equals(OntoLexEntity.LanguageAttributes.Confidence.toString())) {
                validateConfidenceValue(ecdeu.getValue());
                return updateECDEntry(id, ecdeu.getRelation(),
                        ecdeu.getValue(),
                        "?" + SparqlVariable.TARGET);
            } else {
                return null;
            }
        } else {
            throw new ManagerException("The relation value cannot be empty");
        }
    }

    public String updateECDEntry(String id, String relation, String valueToInsert, String valueToDelete) throws ManagerException, UpdateExecutionException {
        if (valueToInsert.isEmpty()) {
            throw new ManagerException("value cannot be empty");
        }
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_ECD_ENTRY.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_TO_INSERT_", valueToInsert)
                .replaceAll("_VALUE_TO_DELETE_", valueToDelete)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    public String updateECDEntryPoS(String le, String newPoS, String oldPoS) throws ManagerException, UpdateExecutionException {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_ECD_ENTRY_POS.replaceAll("_ID_LE_", le)
                .replaceAll("_VALUE_TO_INSERT_", newPoS)
                .replaceAll("_VALUE_TO_DELETE_", oldPoS)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

//    public String updateECDMeaningPoS(String newLe, String oldLe, String pos) throws ManagerException, UpdateExecutionException {
//        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        RDFQueryUtil.update(SparqlUpdateData.UPDATE_ECD_ENTRY_POS.replaceAll("_ID_LE_", le)
//                .replaceAll("_VALUE_TO_INSERT_", newPoS)
//                .replaceAll("_VALUE_TO_DELETE_", oldPoS)
//                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
//        return lastupdate;
//    }
    public String createLexicalEntryForECDEntryNewPoS(String de, String le, String lexicon, String leType, String lePoS, String label, Entity metadata, String lang) throws ManagerException, UpdateExecutionException {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlInsertData.CREATE_LEXICAL_ENTRY_POS_FOR_ECD_ENTRY.replaceAll("_ID_", le)
                .replaceAll("_LE_TYPE_", leType)
                .replaceAll("_POS_", lePoS)
                .replaceAll("_LANG_", lang)
                .replaceAll("_ID_LEXICON_", lexicon)
                .replaceAll("_ID_DE_", de)
                .replaceAll("[AUTHOR]", metadata.getCreator())
                .replaceAll("[CREATED]", metadata.getCreationDate())
                .replaceAll("[MODIFIED]", metadata.getLastUpdate())
                .replaceAll("[LABEL]", label));
        return lastupdate;
    }

    private String updateStatus(String id, ECDEntryUpdater deu, String user) throws QueryEvaluationException, ManagerException {
        String status = getStatus(id);
        if (status != null) {
            if (checkStatus(status, deu.getValue())) {
                if (compareAdmissibleStatus(status, deu.getValue()) == 1) {
                    return statusForewarding(id, deu.getValue(), status, user);
                } else {
                    return statusBackwarding(id, deu.getValue(), status, user);
                }
            } else {
                throw new ManagerException("The current status " + status + " cannot be changed to " + deu.getValue());
            }
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

    public String updateECDForm(String idForm, ECDFormUpdater ecdfu, String user) throws ManagerException {
        if (!ecdfu.getValue().isEmpty()) {
            validateECDFormAttribute(ecdfu.getRelation());
            if (ecdfu.getRelation().equals(OntoLexEntity.ECDFormAttributes.PoS.toString())) {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                String oldLe = utilityManager.getLexicalEntryByForm(idForm);
                String newLe = utilityManager.getLexicalEntryForUpdatingPoSForm(oldLe, ecdfu.getOldPoS(), ecdfu.getValue());
                if (ecdfu.getOldPoS() != null) {
                    // update the pos
                    if (oldLe != null) {
                        return updateECDFormPos(idForm, newLe, oldLe);
                    } else {
                        throw new ManagerException("The required part of speech " + ecdfu.getOldPoS() + " does not exist");
                    }
                } else {
                    // add new pos 
                    String lang = ManagerFactory.getManager(UtilityManager.class).getLanguage(oldLe);
                    if (lang == null) {
                        throw new ManagerException("The language of the lexical entry associated to the form " + idForm + " could not be found");
                    }
                    String label = utilityManager.getLabel(idForm);
                    String type = utilityManager.getType(idForm);
                    return createECDFormPos(newLe, label, lang, type, user);
                }
            } else {
                return null;
            }
        } else {
            throw new ManagerException("The relation value cannot be empty");
        }
    }

    public String updateECDFormPos(String idForm, String newLe, String oldLe) throws ManagerException {
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlUpdateData.UPDATE_ECD_MEANING_POS.replaceAll("_ID_FORM_", idForm)
                .replaceAll("_ID_OLD_LE_", oldLe)
                .replaceAll("_ID_NEW_LE_", newLe));
        return lastupdate;
    }

    public String createECDFormPos(String le, String label, String lang, String type, String user) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String idLabel = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        String _id = LexOProperties.getProperty("repository.lexicon.namespace") + idLabel;
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlInsertData.CREATE_ECD_FORM_POS_FOR_ECD_ENTRY.replaceAll("_ID_FORM_", _id)
                .replaceAll("_LANG_", lang)
                .replaceAll("_ID_LE_", le)
                .replaceAll("_FORM_TYPE_", type)
                .replaceAll("[MODIFIED]", created)
                .replaceAll("[CREATED]", created)
                .replaceAll("[AUTHOR]", user)
                .replaceAll("[LABEL]", label));
        return lastupdate;
    }

    public String updateECDMeaning(String id, ECDMeaningUpdater ecdmu, String user) throws ManagerException {
        if (!ecdmu.getValue().isEmpty()) {
            validateECDMeaningAttribute(ecdmu.getRelation());
            if (ecdmu.getRelation().equals(OntoLexEntity.ECDMeaningAttributes.PoS.toString())) {
                if (ecdmu.getOldPoS() != null) {
                    // update the pos
                    // TODO
                    throw new ManagerException("TODO!!!");
//                    UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
//                    Map<String, String> oldLe = utilityManager.getLexicalSensePoS(id);
//                    if (oldLe != null) {
//                        String newLe = utilityManager.getLexicalEntryForUpdatingPoSMeaning(oldLe, ecdmu.getOldPoS(), ecdmu.getValue());
//                        return updateECDMeaningPoS(idForm, newLe, oldLe);
//                    } else {
//                        throw new ManagerException("The required part of speech " + ecdmu.getOldPoS() + " does not exist");
//                    }
                } else {
                    throw new ManagerException("Part of speech parameter is missing");
                }
            } else {
                return null;
            }
        } else {
            throw new ManagerException("The relation value cannot be empty");
        }
    }

//    public String updateECDMeaningPos(String idForm, String newLe, String oldLe) throws ManagerException {
//        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        RDFQueryUtil.update(SparqlUpdateData.UPDATE_ECD_MEANING_POS.replaceAll("_ID_FORM_", idForm)
//                .replaceAll("_ID_OLD_LE_", oldLe)
//                .replaceAll("_ID_NEW_LE_", newLe));
//        return lastupdate;
//    }
//    
//    public String createECDMeaningPos(String idForm, String oldLe) throws ManagerException {
//        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
//        RDFQueryUtil.update(SparqlInsertData.CREATE_LEXICAL_ENTRY_POS_FOR_ECD_ENTRY.replaceAll("_ID_FORM_", idForm)
//                .replaceAll("_ID_OLD_LE_", oldLe)
//                .replaceAll("_ID_NEW_LE_", newLe));
//        return lastupdate;
//    }
}
