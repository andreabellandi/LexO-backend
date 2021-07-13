/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormBySenseFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalSenseFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryElementItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.EnumUtil.AcceptedSearchFormExtendTo;
import it.cnr.ilc.lexo.util.EnumUtil.AcceptedSearchFormExtensionDegree;
import it.cnr.ilc.lexo.util.EnumUtil.FormTypes;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalEntryStatus;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalEntryTypes;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalSenseSearchFilter;
import it.cnr.ilc.lexo.util.EnumUtil.SearchFormTypes;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class LexiconDataManager implements Manager, Cached {

    static final Logger logger = LoggerFactory.getLogger(LexiconDataManager.class.getName());

    // --- Cache
    private final Map<String, String> lexicalEntries = new HashMap();
    // ---

    public Map<String, String> getLexicalEntries() {
        return lexicalEntries;
    }

    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");

    public String getNamespace() {
        return namespace;
    }

    @Override
    public void reloadCache() {

    }

    public Counting getTotalHits(String label, int value) {
        Counting counting = new Counting();
        counting.setLabel(label);
        counting.setCount(value);
        return counting;
    }

    public TupleQueryResult getFilterdLexicalEntries(LexicalEntryFilter lef) throws ManagerException {
        logger.info(lef.toString());
        Manager.validateWithEnum("formType", FormTypes.class, lef.getFormType());
        Manager.validateWithEnum("status", LexicalEntryStatus.class, lef.getStatus());
        Manager.validateWithEnum("type", LexicalEntryTypes.class, lef.getType());
        String filter = createFilter(lef);
        int limit = lef.getLimit();
        int offset = lef.getOffset();
//        TupleQueryResult tqr = null;
//        RepositoryConnection conn = GraphDbUtil.getConnection();
//        try {
//            TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL,
//                    SparqlSelectData.DATA_LEXICAL_ENTRIES.replace("[FILTER]", filter)
//                            .replace("[LIMIT]", String.valueOf(limit))
//                            .replace("[OFFSET]", String.valueOf(offset)));
//            tqr = tupleQuery.evaluate();
//        } catch (MalformedQueryException | QueryEvaluationException | RepositoryException e) {
//            logger.error("", e);
//        } finally {
//            GraphDbUtil.releaseConnection(conn);
//        }
        String query = SparqlSelectData.DATA_LEXICAL_ENTRIES.replace("[FILTER]", filter)
                .replace("[LIMIT]", String.valueOf(limit))
                .replace("[OFFSET]", String.valueOf(offset));

        return RDFQueryUtil.evaluateTQuery(query);
    }

    private String createFilter(LexicalEntryFilter lef) {
        String text = lef.getText().isEmpty() ? "*" : lef.getText();
        String filter = "(" + (lef.getSearchMode().equals(EnumUtil.SearchModes.Equals.toString()) ? getSearchField(lef.getFormType(), text)
                : (lef.getSearchMode().equals(EnumUtil.SearchModes.StartsWith.toString()) ? getSearchField(lef.getFormType(), text + "*")
                : (lef.getSearchMode().equals(EnumUtil.SearchModes.Contains.toString()) ? getSearchField(lef.getFormType(), "*" + text + "*")
                : getSearchField(lef.getFormType(), "*" + text)))) + ")";
        filter = filter + (!lef.getLang().isEmpty() ? " AND writtenFormLanguage:" + lef.getLang() : "");
        filter = filter + (!lef.getAuthor().isEmpty() ? " AND author:" + lef.getAuthor() : "");
        filter = filter + (!lef.getPos().isEmpty() ? " AND pos:" + "\\\"" + lef.getPos() + "\\\"" : "");
        filter = filter + (!lef.getType().isEmpty() ? " AND type:" + lef.getType() : "");
        filter = filter + (!lef.getStatus().isEmpty() ? " AND status:" + lef.getStatus() : "");
        return filter;
    }

    private String createFilterByForm(LexicalSenseFilter lsf) {
        String text = lsf.getText().isEmpty() ? "*" : lsf.getText();
        String filter = "(" + (lsf.getSearchMode().equals(EnumUtil.SearchModes.Equals.toString()) ? getSearchField(lsf.getFormType(), text)
                : (lsf.getSearchMode().equals(EnumUtil.SearchModes.StartsWith.toString()) ? getSearchField(lsf.getFormType(), text + "*")
                : (lsf.getSearchMode().equals(EnumUtil.SearchModes.Contains.toString()) ? getSearchField(lsf.getFormType(), "*" + text + "*")
                : getSearchField(lsf.getFormType(), "*" + text)))) + ")";
        filter = filter + (!lsf.getLang().isEmpty() ? " AND writtenFormLanguage:" + lsf.getLang() : "");
        filter = filter + (!lsf.getAuthor().isEmpty() ? " AND author:" + lsf.getAuthor() : "");
        filter = filter + (!lsf.getPos().isEmpty() ? " AND pos:" + "\\\"" + lsf.getPos() + "\\\"" : "");
        filter = filter + (!lsf.getType().isEmpty() ? " AND type:" + "\\\"" + lsf.getType() + "\\\"" : "");
        filter = filter + (!lsf.getStatus().isEmpty() ? " AND status:" + lsf.getStatus() : "");
        return filter;
    }

    private String createFilter(LexicalSenseFilter lsf) {
        String text = lsf.getText().isEmpty() ? "*" : lsf.getText();
        String filter = "(" + (lsf.getSearchMode().equals(EnumUtil.SearchModes.Equals.toString()) ? getSearchLexicalSenseField(lsf.getFormType(), text)
                : (lsf.getSearchMode().equals(EnumUtil.SearchModes.StartsWith.toString()) ? getSearchLexicalSenseField(lsf.getFormType(), text + "*")
                : (lsf.getSearchMode().equals(EnumUtil.SearchModes.Contains.toString()) ? getSearchLexicalSenseField(lsf.getFormType(), "*" + text + "*")
                : getSearchLexicalSenseField(lsf.getFormType(), "*" + text)))) + ")";
        filter = filter + (!lsf.getLang().isEmpty() ? " AND senseLanguage:" + lsf.getLang() : "");
        filter = filter + (!lsf.getAuthor().isEmpty() ? " AND author:" + lsf.getAuthor() : "");
        filter = filter + (!lsf.getPos().isEmpty() ? " AND pos:" + "\\\"" + lsf.getPos() + "\\\"" : "");
        filter = filter + (!lsf.getType().isEmpty() ? " AND type:" + "\\\"" + lsf.getType() + "\\\"" : "");
        filter = filter + (!lsf.getStatus().isEmpty() ? " AND status:" + lsf.getStatus() : "");
        return filter;
    }
    
    private String createFilter(FormFilter ff) {
        String text = ff.getText().isEmpty() ? "*" : ff.getText();
        String filter = "(" + (ff.getSearchMode().equals(EnumUtil.SearchModes.Equals.toString()) ? getSearchFormField(ff.getRepresentationType(), text)
                : (ff.getSearchMode().equals(EnumUtil.SearchModes.StartsWith.toString()) ? getSearchFormField(ff.getRepresentationType(), text + "*")
                : (ff.getSearchMode().equals(EnumUtil.SearchModes.Contains.toString()) ? getSearchFormField(ff.getRepresentationType(), "*" + text + "*")
                : getSearchFormField(ff.getRepresentationType(), "*" + text)))) + ")";
//        filter = filter + (!ff.getLang().isEmpty() ? " AND writtenFormLanguage:" + ff.getLang() : "");
        filter = filter + (!ff.getAuthor().isEmpty() ? " AND author:" + ff.getAuthor() : "");
        return filter;
    }

    private String createFilter(String lexicalEntryID) {
        return "lexicalEntryIRI:" + "\\\"" + namespace + lexicalEntryID + "\\\"";
    }

    private String getSearchField(String formType, String textSearch) {
        return formType.isEmpty() ? "lexicalEntryLabel:" + textSearch + " OR writtenCanonicalForm:" + textSearch + " OR writtenOtherForm:" + textSearch
                : (formType.equals(EnumUtil.FormTypes.Entry.toString()) ? "lexicalEntryLabel:" + textSearch
                : (formType.equals(EnumUtil.FormTypes.Flexed.toString()) ? "writtenCanonicalForm:" + textSearch + " OR writtenOtherForm:" + textSearch : ""));
    }
    
    private String getSearchLexicalSenseField(String lsField, String textSearch) {
        return lsField.isEmpty() ? "definition:" + textSearch + " OR description:" + textSearch + " OR etymology:" + textSearch
                 + " OR explanation:" + textSearch + " OR gloss:" + textSearch + " OR senseExample:" + textSearch + " OR senseTranslation:" + textSearch
                : (lsField.equals(EnumUtil.LexicalSenseSearchFilter.Definition.toString()) ? "definition:" + textSearch
                : (lsField.equals(EnumUtil.LexicalSenseSearchFilter.Description.toString()) ? "description:" + textSearch
                : (lsField.equals(EnumUtil.LexicalSenseSearchFilter.Explanation.toString()) ? "explanation:" + textSearch
                : (lsField.equals(EnumUtil.LexicalSenseSearchFilter.Gloss.toString()) ? "gloss:" + textSearch
                : (lsField.equals(EnumUtil.LexicalSenseSearchFilter.SenseExample.toString()) ? "definition:" + textSearch
                : (lsField.equals(EnumUtil.LexicalSenseSearchFilter.SenseTranslation.toString()) ? "definition:" + textSearch : ""))))));
    }
    
    private String getSearchFormField(String formField, String textSearch) {
        return formField.isEmpty() ? "writtenRep:" + textSearch + " OR phoneticRep:" + textSearch + " OR transliteration:" + textSearch
                 + " OR segmentation:" + textSearch + " OR romanization:" + textSearch + " OR pronunciation:" + textSearch
                : (formField.equals(EnumUtil.FormRepresentationType.PhoneticRepresentation.toString()) ? "phoneticRep:" + textSearch
                : (formField.equals(EnumUtil.FormRepresentationType.Pronunciation.toString()) ? "pronunciation:" + textSearch
                : (formField.equals(EnumUtil.FormRepresentationType.Romanization.toString()) ? "romanization:" + textSearch
                : (formField.equals(EnumUtil.FormRepresentationType.Segmentation.toString()) ? "segmentation:" + textSearch
                : (formField.equals(EnumUtil.FormRepresentationType.Transliteration.toString()) ? "transliteration:" + textSearch
                : (formField.equals(EnumUtil.FormRepresentationType.WrittenRepresentation.toString()) ? "writtenRep:" + textSearch : ""))))));
    }

    public TupleQueryResult getElements(String lexicalEntryID) {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_LEXICAL_ENTRY_ELEMENTS.replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\""));
//        return tupleQuery.evaluate();
        String query = SparqlSelectData.DATA_LEXICAL_ENTRY_ELEMENTS
                .replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public List<FormItem> getFormItemListCopy(List<FormItem> forms) {
        List<FormItem> _forms = new ArrayList();
        for (FormItem fi : forms) {
            FormItem _fi = new FormItem();
            _fi.setCreator(fi.getCreator());
            _fi.setForm(fi.getForm());
            _fi.setFormInstanceName(fi.getFormInstanceName());
            _fi.setLabel(fi.getLabel());
            _fi.setNote(fi.getNote());
            _fi.setPhoneticRep(fi.getPhoneticRep());
            _fi.setType(fi.getType());
            _fi.setLexicalEntry(fi.getLexicalEntry());
            _fi.setLexicalEntryInstanceName(fi.getLexicalEntryInstanceName());
            _fi.setMorphology(getMorphologyListCopy(fi.getMorphology()));
            _forms.add(_fi);
        }
        return _forms;
    }

    private ArrayList<Morphology> getMorphologyListCopy(ArrayList<Morphology> morpho) {
        ArrayList<Morphology> _morpho = new ArrayList();
        for (Morphology m : morpho) {
            Morphology _m = new Morphology(m.getTrait(), m.getValue());
            _morpho.add(_m);
        }
        return _morpho;
    }

    public TupleQueryResult getFilterdForms(FormBySenseFilter ff) throws ManagerException {
        Manager.validateWithEnum("searchFormTypes", SearchFormTypes.class, ff.getFormType());
        Manager.validateWithEnum("acceptedSearchFormExtendTo", AcceptedSearchFormExtendTo.class, ff.getExtendTo());
        Manager.validateWithEnum("acceptedSearchFormExtensionDegree", AcceptedSearchFormExtensionDegree.class, String.valueOf(ff.getExtensionDegree()));
        String query = (ff.getFormType().equals(EnumUtil.SearchFormTypes.Lemma.toString()))
                ? SparqlSelectData.DATA_FORMS_BY_LEXICAL_ENTRY.replace("[IRI]", "\\\"" + namespace + ff.getLexicalEntry() + "\\\"")
                        .replace("[FORM_CONSTRAINT]", "")
                : ff.getExtensionDegree() == 0
                ? SparqlSelectData.DATA_FORMS_BY_LEXICAL_ENTRY.replace("[IRI]", "\\\"" + namespace + ff.getLexicalEntry() + "\\\"")
                        .replace("[FORM_CONSTRAINT]", "FILTER(regex(str(?" + SparqlVariable.WRITTEN_REPRESENTATION + "), \"^" + ff.getForm().trim() + "$\"))\n")
                : SparqlSelectData.DATA_FORMS_BY_LEXICAL_ENTRY.replace("[IRI]", "\\\"" + namespace + ff.getLexicalEntry() + "\\\"")
                        .replace("[FORM_CONSTRAINT]", "");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getFilterdForms(FormFilter ff) throws ManagerException {
        Manager.validateWithEnum("formRepresentationType", EnumUtil.FormRepresentationType.class, ff.getRepresentationType());
        String filter = createFilter(ff);
        int limit = ff.getLimit();
        int offset = ff.getOffset();
        String query = SparqlSelectData.DATA_FORMS.replace("[FILTER]", filter)
                .replace("[LIMIT]", String.valueOf(limit))
                .replace("[OFFSET]", String.valueOf(offset));
        return RDFQueryUtil.evaluateTQuery(query);
    }
    
    public TupleQueryResult getFilterdForms(String id) throws ManagerException {
        String query = SparqlSelectData.DATA_FORMS_BY_LEXICAL_ENTRY.replace("[IRI]", "\\\"" + namespace + id + "\\\"")
                .replace("[FORM_CONSTRAINT]", "");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getRelationByLenght(String relation, String startNode) {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_PATH_LENGTH.replace("[START_NODE]", startNode)
//                        .replace("[START_RELATION]", relation)
//                        .replace("[MID_RELATION]", relation));
//        return tupleQuery.evaluate();
        String query = SparqlSelectData.DATA_PATH_LENGTH.replace("[START_NODE]", startNode)
                .replace("[START_RELATION]", relation)
                .replace("[MID_RELATION]", relation);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getForms(String lexicalEntryID) {
        String query = SparqlSelectData.DATA_FORMS_BY_LEXICAL_ENTRY.replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\"")
                .replace("[FORM_CONSTRAINT]", "");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLexicalSenses(String lexicalEntryID) {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_LEXICAL_SENSES.replace("[FILTER]", createFilter(lexicalEntryID))
//                        .replace("[LIMIT]", String.valueOf(500))
//                        .replace("[OFFSET]", String.valueOf(0)));
//        return tupleQuery.evaluate();
        String query = SparqlSelectData.DATA_LEXICAL_SENSES.replace("[FILTER]", createFilter(lexicalEntryID))
                .replace("[LIMIT]", String.valueOf(500))
                .replace("[OFFSET]", String.valueOf(0));
        return RDFQueryUtil.evaluateTQuery(query);
    }
    
    public TupleQueryResult getLexicalSensesByConcept(String conceptID) {
        String query = SparqlSelectData.DATA_SENSE_BY_CONCEPT.replace("_CONCEPT_", conceptID);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getFormsBySenseRelation(FormBySenseFilter ff, String sense) throws ManagerException {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_FORMS_BY_SENSE_RELATION
//                        .replace("[RELATION_DISTANCE_PATH]", "lex:" + sense + " lexinfo:" + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . "));
//        return tupleQuery.evaluate();
        String query = SparqlSelectData.DATA_FORMS_BY_SENSE_RELATION
                .replace("[RELATION_DISTANCE_PATH]", "lex:" + sense + " lexinfo:" + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . ");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getFormsBySenseRelation(FormBySenseFilter ff, String sense, int distance) throws ManagerException {
        String relationDistancePath = "";
        switch (distance) {
            case 1:
                relationDistancePath = "lex:" + sense + " lexinfo:" + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . \n";
                break;
            case 2:
                relationDistancePath = "lex:" + sense + " lexinfo:" + ff.getExtendTo() + " ?t1 .\n"
                        + "?t1 lexinfo:" + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . \n";
                break;
            case 3:
                relationDistancePath = "lex:" + sense + " lexinfo:" + ff.getExtendTo() + " ?t1 .\n"
                        + "?t1 lexinfo:" + ff.getExtendTo() + " ?t2 . \n"
                        + "?t2 lexinfo:" + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . \n";
                break;
        }
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_FORMS_BY_SENSE_RELATION.replace("[RELATION_DISTANCE_PATH]", relationDistancePath));
//        return tupleQuery.evaluate();
        String query = SparqlSelectData.DATA_FORMS_BY_SENSE_RELATION.replace("[RELATION_DISTANCE_PATH]", relationDistancePath);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getFilterdLexicalSenses(LexicalSenseFilter lsf) throws ManagerException {
        Manager.validateWithEnum("status", LexicalEntryStatus.class, lsf.getStatus());
        Manager.validateWithEnum("type", LexicalEntryTypes.class, lsf.getType());
        if (lsf.getFormType() != null && !lsf.getFormType().isEmpty()) {
            String query = filterLexicalSensesByForm(lsf);
            return RDFQueryUtil.evaluateTQuery(query);
        } else {
            if (lsf.getField() != null && !lsf.getField().isEmpty()) {
                String query = filterLexicalSenses(lsf);
                return RDFQueryUtil.evaluateTQuery(query);
            } else {
                throw new ManagerException("formType or field parametrs must be set");
            }
        }
    }

    private String filterLexicalSensesByForm(LexicalSenseFilter lsf) throws ManagerException {
        Manager.validateWithEnum("formType", FormTypes.class, lsf.getFormType());
        String filter = createFilterByForm(lsf);
        int limit = lsf.getLimit();
        int offset = lsf.getOffset();
        return SparqlSelectData.DATA_LEXICAL_SENSES_BY_FORM.replace("[FILTER]", filter)
                .replace("[LIMIT]", String.valueOf(limit))
                .replace("[OFFSET]", String.valueOf(offset));
    }

    private String filterLexicalSenses(LexicalSenseFilter lsf) throws ManagerException {
        Manager.validateWithEnum("field", LexicalSenseSearchFilter.class, lsf.getField());
        String filter = createFilter(lsf);
        int limit = lsf.getLimit();
        int offset = lsf.getOffset();
        return SparqlSelectData.DATA_LEXICAL_SENSES.replace("[FILTER]", filter)
                .replace("[LIMIT]", String.valueOf(limit))
                .replace("[OFFSET]", String.valueOf(offset));
    }

    public TupleQueryResult getLexicalEntry(String lexicalEntryID, String aspect) throws ManagerException {
        Manager.validateWithEnum("aspect", EnumUtil.LexicalAspects.class, aspect);
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_LEXICAL_ENTRY_CORE.replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\""));
//        return tupleQuery.evaluate();
        String query = SparqlSelectData.DATA_LEXICAL_ENTRY_CORE.replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLinguisticRelation(String lexicalEntryID, String property) throws ManagerException {
        String query = SparqlSelectData.DATA_LINGUISTIC_RELATION
                .replace("_ID_", lexicalEntryID)
                .replace("_RELATION_", property);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getGenericRelation(String lexicalEntryID, String property) throws ManagerException {
        String query = SparqlSelectData.DATA_GENERIC_RELATION
                .replace("_ID_", lexicalEntryID)
                .replace("_RELATION_", property);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLexicalEntryReferenceLinks(String lexicalEntryID) {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_LEXICAL_ENTRY_REFERENCE_LINKS.replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\""));
//        return tupleQuery.evaluate();
        String query = SparqlSelectData.DATA_LEXICAL_ENTRY_REFERENCE_LINKS.replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public void addLexicalEntryLinks(LexicalEntryCore lec, LexicalEntryElementItem... links) {
        if (lec.getLinks() != null) {
            addLinks(lec.getLinks(), links);
        } else {
            ArrayList<LexicalEntryElementItem> otherLinksList = new ArrayList<>();
            lec.setLinks(otherLinksList);
            addLinks(lec.getLinks(), links);
        }
    }

    private void addLinks(ArrayList<LexicalEntryElementItem> leec, LexicalEntryElementItem... links) {
        for (LexicalEntryElementItem link : links) {
            leec.add(link);
        }
    }

    public TupleQueryResult getForm(String formID, String aspect) throws ManagerException {
        if (!aspect.equals(EnumUtil.LexicalAspects.Core.toString()) && !aspect.equals(EnumUtil.LexicalAspects.VarTrans.toString())) {
            throw new ManagerException(aspect + " does not allowed for lexical forms");
        }
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_FORM_CORE.replace("[IRI]", "\\\"" + namespace + formID + "\\\""));
        String query = SparqlSelectData.DATA_FORM_CORE.replace("[IRI]", "\\\"" + namespace + formID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public FormCore getMorphologyInheritance(List<FormCore> fc) {
        List<Morphology> inhml = new ArrayList();
        for (int i = 1; i < fc.size(); i++) {
            for (Morphology m : fc.get(i).getInheritedMorphology()) {
                if (!m.getTrait().equals("partOfSpeech")) {
                    inhml.add(m);
                }
            }
        }
        fc.get(0).getInheritedMorphology().addAll(inhml);
        return fc.get(0);
    }

    public TupleQueryResult getLexicalSense(String senseID, String aspect) throws ManagerException {
        if (!aspect.equals(EnumUtil.LexicalAspects.Core.toString())
                && !aspect.equals(EnumUtil.LexicalAspects.VarTrans.toString())
                && !aspect.equals(EnumUtil.LexicalAspects.SynSem.toString())) {
            throw new ManagerException(aspect + " does not allowed for lexical senses");
        }
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_LEXICAL_SENSE_CORE.replace("[IRI]", "\\\"" + namespace + senseID + "\\\""));
        String query = SparqlSelectData.DATA_LEXICAL_SENSE_CORE.replace("[IRI]", "\\\"" + namespace + senseID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLexiconLanguages() throws ManagerException {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_LEXICON_LANGUAGES);
        String query = SparqlSelectData.DATA_LEXICON_LANGUAGES;
        return RDFQueryUtil.evaluateTQuery(query);
    }
}
