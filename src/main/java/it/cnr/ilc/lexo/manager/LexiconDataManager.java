/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormBySenseFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalConceptFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalSenseFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.data.lexicon.output.EtymologicalLink;
import it.cnr.ilc.lexo.service.data.lexicon.output.Etymology;
import it.cnr.ilc.lexo.service.data.lexicon.output.EtymologyTree;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalConcept;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntityLinksItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntityLinksItem.Link;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import it.cnr.ilc.lexo.service.data.lexicon.output.ReifiedRelation;
import it.cnr.ilc.lexo.service.data.lexicon.output.VarTransData;
import it.cnr.ilc.lexo.service.data.output.Label;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.EnumUtil.FormTypes;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalConceptSearchFilter;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalEntryStatus;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalEntryTypes;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalSenseSearchFilter;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class LexiconDataManager implements Manager, Cached {

    static final Logger logger = LoggerFactory.getLogger(LexiconDataManager.class.getName());

    private final String lexicalizationModel = LexOProperties.getProperty("skos.lexicalizationModel");
    private final String defaultLanguageLabel = LexOProperties.getProperty("skos.defaultLanguageLabel");

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
        String query = SparqlSelectData.DATA_LEXICAL_ENTRIES.replace("[FILTER]", filter)
                .replace("_TYPE_", lef.getType())
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
        filter = filter + (!lef.getType().isEmpty() ? " AND type:" + "\\\"" + lef.getType() + "\\\"" : "");
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
        String filter = "(" + (lsf.getSearchMode().equals(EnumUtil.SearchModes.Equals.toString()) ? getSearchLexicalSenseField(lsf.getField(), text)
                : (lsf.getSearchMode().equals(EnumUtil.SearchModes.StartsWith.toString()) ? getSearchLexicalSenseField(lsf.getField(), text + "*")
                : (lsf.getSearchMode().equals(EnumUtil.SearchModes.Contains.toString()) ? getSearchLexicalSenseField(lsf.getField(), "*" + text + "*")
                : getSearchLexicalSenseField(lsf.getField(), "*" + text)))) + ")";
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

    private String createFilter(LexicalConceptFilter lcf) {
        String text = lcf.getText().isEmpty() ? "*" : lcf.getText();
        String filter = "(" + (lcf.getSearchMode().equals(EnumUtil.SearchModes.Equals.toString()) ? getSearchLexicalConceptField(lcf.getLabelType(), text)
                : (lcf.getSearchMode().equals(EnumUtil.SearchModes.StartsWith.toString()) ? getSearchLexicalConceptField(lcf.getLabelType(), text + "*")
                : (lcf.getSearchMode().equals(EnumUtil.SearchModes.Contains.toString()) ? getSearchLexicalConceptField(lcf.getLabelType(), "*" + text + "*")
                : getSearchLexicalConceptField(lcf.getLabelType(), "*" + text)))) + ")";
//        filter = filter + (!ff.getLang().isEmpty() ? " AND writtenFormLanguage:" + ff.getLang() : "");
        filter = filter + (!lcf.getAuthor().isEmpty() ? " AND author:" + lcf.getAuthor() : "");
        return filter;
    }

    private String createFilter(String lexicalEntryID) {
        return "lexicalEntryIRI:" + "\\\"" + lexicalEntryID + "\\\"";
    }

    private String createComponentFilter(String compID) {
        return "ComponentIRI:" + "\\\"" + compID + "\\\"";
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

    private String getSearchLexicalConceptField(String labelType, String textSearch) {
        return (labelType.isEmpty() || labelType.equals(EnumUtil.LexicalConceptSearchFilter.prefLabel.toString())) ? "prefLabel:" + textSearch
                : (labelType.equals(EnumUtil.LexicalConceptSearchFilter.altLabel.toString()) ? "altLabel:" + textSearch
                : (labelType.equals(EnumUtil.LexicalConceptSearchFilter.hiddenLabel.toString()) ? "hiddenLabel:" + textSearch : ""));
    }

    public TupleQueryResult getElements(String lexicalEntryID) {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_LEXICAL_ENTRY_ELEMENTS.replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\""));
//        return tupleQuery.evaluate();
        String query = SparqlSelectData.DATA_LEXICAL_ENTRY_ELEMENTS
                .replace("[IRI]", "\\\"" + lexicalEntryID + "\\\"");
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

    public TupleQueryResult getForms(String lexicalEntryID) {
        String query = SparqlSelectData.DATA_FORMS_BY_LEXICAL_ENTRY.replace("[IRI]", "\\\"" + lexicalEntryID + "\\\"")
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

    public TupleQueryResult getEtymologies(String lexicalEntryID) {
        String query = SparqlSelectData.DATA_ETYMOLOGIES.replace("[FILTER]", createFilter(lexicalEntryID))
                .replace("[LIMIT]", String.valueOf(500))
                .replace("[OFFSET]", String.valueOf(0));
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getSubTerms(String lexicalEntryID) {
        String query = SparqlSelectData.DATA_SUBTERMS.replace("[FILTER]", createFilter(lexicalEntryID))
                .replace("_TYPE_", "")
                .replace("[LIMIT]", "1")
                .replace("[OFFSET]", "0");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getCorrespondsTo(String compID) {
        String query = SparqlSelectData.DATA_CORRESPONDS_TO.replace("[FILTER]", createComponentFilter(compID))
                .replace("_TYPE_", "")
                .replace("[LIMIT]", "1")
                .replace("[OFFSET]", "0");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getComponents(String id, String index, String order) {
        String query = SparqlSelectData.DATA_COMPONENTS.replace("[FILTER]", createFilter(id))
                .replace("[ORDER]", order)
                .replace("[INDEX_NAME]", index)
                .replace("[LIMIT]", "1")
                .replace("[OFFSET]", "0");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLexicalSensesByConcept(String conceptID) {
        String query = SparqlSelectData.DATA_SENSE_BY_CONCEPT.replace("_CONCEPT_", conceptID);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getFormsBySenseRelation(FormBySenseFilter ff, String sense, int distance) throws ManagerException {
        String relationDistancePath = "";
        switch (distance) {
            case 1:
                relationDistancePath = sense + " " + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . \n";
//                relationDistancePath = "lex:" + sense + " lexinfo:" + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . \n";
                break;
            case 2:
//                relationDistancePath = "lex:" + sense + " lexinfo:" + ff.getExtendTo() + " ?t1 .\n"
                relationDistancePath = sense + " " + ff.getExtendTo() + " ?t1 .\n"
                        + "?t1 " + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . \n";
//                + "?t1 lexinfo:" + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . \n";
                break;
            case 3:
//                relationDistancePath = "lex:" + sense + " lexinfo:" + ff.getExtendTo() + " ?t1 .\n"
                relationDistancePath = sense + " " + ff.getExtendTo() + " ?t1 .\n"
                        //                        + "?t1 lexinfo:" + ff.getExtendTo() + " ?t2 . \n"
                        //                        + "?t2 lexinfo:" + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . \n";
                        + "?t1 " + ff.getExtendTo() + " ?t2 . \n"
                        + "?t2 " + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . \n";
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
        return SparqlSelectData.DATA_LEXICAL_SENSES_FILTER.replace("[FILTER]", filter)
                .replace("[LIMIT]", String.valueOf(limit))
                .replace("[OFFSET]", String.valueOf(offset));
    }

    public TupleQueryResult getLexicalEntry(String lexicalEntryID) throws ManagerException {
        String query = SparqlSelectData.DATA_LEXICAL_ENTRY_CORE.replace("[IRI]", "\\\"" + lexicalEntryID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLexicalEntryVarTrans(String lexicalEntryID, boolean directRelations) throws ManagerException {
        String query = "";
        if (directRelations) {
            query = SparqlSelectData.DATA_LEXICAL_ENTRY_DIRECT_VARTRANS.replace("[IRI]", "\\\"" + lexicalEntryID + "\\\"");
        } else
            query = SparqlSelectData.DATA_LEXICAL_ENTRY_INDIRECT_VARTRANS.replace("[IRI]", "\\\"" + lexicalEntryID + "\\\"");;
        return RDFQueryUtil.evaluateTQuery(query);
    }
    
    public VarTransData addLexicalRelations(List<LinkedEntity> les, List<ReifiedRelation> rrs) {
        VarTransData vtd = new VarTransData();
        vtd.setDirectRelations(les);
        vtd.setIndirectRelations(rrs);
        return vtd;
    }

    public TupleQueryResult getComponent(String componentID) throws ManagerException {
        String query = SparqlSelectData.DATA_COMPONENT.replace("[IRI]", "\\\"" + componentID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLinguisticRelation(String lexicalEntryID, String property) throws ManagerException {
        String query = SparqlSelectData.DATA_LINGUISTIC_RELATION
                .replace("_ID_", lexicalEntryID)
                .replace("_RELATION_", property);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLexicalConceptRelation(String id, String property) throws ManagerException {
        String query = SparqlSelectData.DATA_LEXICAL_CONCEPT_RELATION
                .replace("_ID_", id)
                .replace("_RELATION_", property);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getGenericRelation(String lexicalEntryID, String property) throws ManagerException {
        String query = SparqlSelectData.DATA_GENERIC_RELATION
                .replace("_ID_", lexicalEntryID)
                .replace("_RELATION_", property);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLexicalEntityLinks(String lexicalEntryID) {
        String query = SparqlSelectData.DATA_LEXICAL_ENTITY_LINKS.replace("[IRI]", "\\\"" + lexicalEntryID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public LexicalEntryCore getLexicalEntityTypes(List<LexicalEntryCore> lecs) {
        if (lecs.size() > 1) {
            ArrayList<String> types = new ArrayList();
            for (LexicalEntryCore lec : lecs) {
                types.add(lec.getType().get(0));
            }
            lecs.get(0).setType(types);
        }
        return lecs.get(0);
    }

    public void addLexicalEntityLink(LexicalEntryCore lec, LexicalEntityLinksItem links) {
        ArrayList<Link> _links = new ArrayList();
        for (Link link : links.getLinks()) {
            _links.add(link);
        }
        lec.setLinks(_links);
    }

    public void addLexicalEntityLink(FormCore fc, LexicalEntityLinksItem links) {
        ArrayList<Link> _links = new ArrayList();
        for (Link link : links.getLinks()) {
            _links.add(link);
        }
        fc.setLinks(_links);
    }

    public void addLexicalEntityLink(LexicalSenseCore lsc, LexicalEntityLinksItem links) {
        ArrayList<Link> _links = new ArrayList();
        for (Link link : links.getLinks()) {
            _links.add(link);
        }
        lsc.setLinks(_links);
    }

    public void addLexicalEntityLink(Etymology e, LexicalEntityLinksItem links) {
        if (links != null) {
            ArrayList<Link> _links = new ArrayList();
            for (Link link : links.getLinks()) {
                _links.add(link);
            }
            e.setLinks(_links);
        } else {
            e.setLinks(new ArrayList());
        }
    }

    public EtymologyTree getEtymologyTree(Etymology e, List<EtymologicalLink> etyLinks) {
        EtymologyTree et = new EtymologyTree();
        et.setEtymology(e);
        et.setEtyLinks(etyLinks);
        return et;
    }

    public TupleQueryResult getForm(String formID, String module) throws ManagerException {
        if (!module.equals(EnumUtil.LexicalAspects.Core.toString()) && !module.equals(EnumUtil.LexicalAspects.VarTrans.toString())) {
            throw new ManagerException(module + " does not allowed for lexical forms");
        }
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_FORM_CORE.replace("[IRI]", "\\\"" + namespace + formID + "\\\""));
        String query = SparqlSelectData.DATA_FORM_CORE.replace("[IRI]", "\\\"" + formID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public FormCore getMorphologyInheritance(List<FormCore> fc) {
        List<Morphology> inhml = new ArrayList();
        for (int i = 1; i < fc.size(); i++) {
            for (Morphology m : fc.get(i).getInheritedMorphology()) {
//                if (!m.getTrait().equals("partOfSpeech")) {
                if (!m.getTrait().contains("partOfSpeech")) {
                    inhml.add(m);
                }
            }
        }
        fc.get(0).getInheritedMorphology().addAll(inhml);
        return fc.get(0);
    }

    public TupleQueryResult getLexicalSense(String senseID, String module) throws ManagerException {
        if (!module.equals(EnumUtil.LexicalAspects.Core.toString())
                && !module.equals(EnumUtil.LexicalAspects.VarTrans.toString())
                && !module.equals(EnumUtil.LexicalAspects.SynSem.toString())) {
            throw new ManagerException(module + " does not allowed for lexical senses");
        }
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_LEXICAL_SENSE_CORE.replace("[IRI]", "\\\"" + namespace + senseID + "\\\""));
        String query = SparqlSelectData.DATA_LEXICAL_SENSE_CORE.replace("[IRI]", "\\\"" + senseID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getEtymology(String etymologyID) throws ManagerException {
        String query = SparqlSelectData.DATA_ETYMOLOGY.replace("[IRI]", "\\\"" + etymologyID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getEtymologicalLinks(String etymologyID) throws ManagerException {
        String query = SparqlSelectData.DATA_ETYMOLOGY_ETY_LINKS_LIST.replace("[IRI]", "\\\"" + etymologyID + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLexiconLanguages() throws ManagerException {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlSelectData.DATA_LEXICON_LANGUAGES);
        String query = SparqlSelectData.DATA_LEXICON_LANGUAGES;
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public void setDefaultLanguage(LexicalConcept lc) {
        for (Label l : lc.getLabels()) {
            if (l.getType().equals(lexicalizationModel.equals("skos") ? "prefLabel" : "label")
                    && l.getLanguage().equals(defaultLanguageLabel)) {
                lc.setDefaultLabel(l.getLabel());
            }
        }
    }

    public TupleQueryResult getImages(String lexicalEntityID) {
        String query = SparqlSelectData.DATA_IMAGES.replaceAll("_LEID_", lexicalEntityID);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getFilterdLexicalConcepts(LexicalConceptFilter lcf) throws ManagerException {
        Manager.validateWithEnum("field", LexicalConceptSearchFilter.class, lcf.getLabelType());
        String filter = createFilter(lcf);
        int limit = lcf.getLimit();
        int offset = lcf.getOffset();
        String query = SparqlSelectData.DATA_LEXICAL_CONCEPTS_FILTER.replace("[FILTER]", filter)
                .replace("_LABELPROPERTY_", SparqlPrefix.SKOS.getUri() + lcf.getLabelType())
                .replace("[LIMIT]", String.valueOf(limit))
                .replace("[OFFSET]", String.valueOf(offset));
        return RDFQueryUtil.evaluateTQuery(query);
    }

}
