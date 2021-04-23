/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.GraphDbUtil;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryElementItem;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.EnumUtil.AcceptedSearchFormExtendTo;
import it.cnr.ilc.lexo.util.EnumUtil.AcceptedSearchFormExtensionDegree;
import it.cnr.ilc.lexo.util.EnumUtil.FormTypes;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalEntryStatus;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalEntryTypes;
import it.cnr.ilc.lexo.util.EnumUtil.SearchFormTypes;
import java.util.ArrayList;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 */
public class LexiconDataManager implements Manager, Cached {

//    private final String namespace = StringUtil.escapeMetaCharacters(LexOProperties.getProperty("repository.lexicon.namespace"));
    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");

    public String getNamespace() {
        return namespace;
    }

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Counting getTotalHits(String label, int value) {
        Counting counting = new Counting();
        counting.setLabel(label);
        counting.setCount(value);
        return counting;
    }

    public TupleQueryResult getFilterdLexicalEntries(LexicalEntryFilter lef) throws ManagerException {
        Manager.validateWithEnum("formType", FormTypes.class, lef.getFormType());
        Manager.validateWithEnum("status", LexicalEntryStatus.class, lef.getStatus());
        Manager.validateWithEnum("type", LexicalEntryTypes.class, lef.getType());
        String filter = createFilter(lef);
        int limit = lef.getLimit();
        int offset = lef.getOffset();
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelectData.DATA_LEXICAL_ENTRIES.replace("[FILTER]", filter)
                        .replace("[LIMIT]", String.valueOf(limit))
                        .replace("[OFFSET]", String.valueOf(offset)));
        return tupleQuery.evaluate();
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

    private String getSearchField(String formType, String textSearch) {
        return formType.isEmpty() ? "lexicalEntryLabel:" + textSearch + " OR writtenCanonicalForm:" + textSearch + " OR writtenOtherForm:" + textSearch
                : (formType.equals(EnumUtil.FormTypes.Entry.toString()) ? "lexicalEntryLabel:" + textSearch
                : (formType.equals(EnumUtil.FormTypes.Flexed.toString()) ? "writtenCanonicalForm:" + textSearch + " OR writtenOtherForm:" + textSearch : ""));
    }

    public TupleQueryResult getElements(String lexicalEntryID) {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelectData.DATA_LEXICAL_ENTRY_ELEMENTS.replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\""));
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getFilterdForms(FormFilter ff) throws ManagerException {
        Manager.validateWithEnum("searchFormTypes", SearchFormTypes.class, ff.getFormType());
        Manager.validateWithEnum("acceptedSearchFormExtendTo", AcceptedSearchFormExtendTo.class, ff.getExtendTo());
        Manager.validateWithEnum("acceptedSearchFormExtensionDegree", AcceptedSearchFormExtensionDegree.class, String.valueOf(ff.getExtensionDegree()));
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                (ff.getFormType().equals(EnumUtil.SearchFormTypes.Lemma.toString()))
                ? SparqlSelectData.DATA_FORMS.replace("[IRI]", "\\\"" + namespace + ff.getLexicalEntry() + "\\\"")
                        .replace("[FORM_CONSTRAINT]", "")
                : SparqlSelectData.DATA_FORMS.replace("[IRI]", "\\\"" + namespace + ff.getLexicalEntry() + "\\\"")
                        .replace("[FORM_CONSTRAINT]", "FILTER(regex(str(?" + SparqlVariable.WRITTEN_REPRESENTATION + "), \"^" + ff.getForm().trim() + "$\"))\n"));
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getForms(String lexicalEntryID) {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelectData.DATA_FORMS.replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\"")
                        .replace("[FORM_CONSTRAINT]", ""));
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getFormsBySenseRelation(FormFilter ff, String sense) throws ManagerException {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelectData.DATA_FORMS_BY_SENSE_RELATION
                        .replace("[RELATION_DISTANCE_PATH]", "lex:" + sense + " lexinfo:" + ff.getExtendTo() + " ?" + SparqlVariable.TARGET + " . "));
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getFormsBySenseRelation(FormFilter ff, String sense, int distance) throws ManagerException {
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
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelectData.DATA_FORMS_BY_SENSE_RELATION.replace("[RELATION_DISTANCE_PATH]", relationDistancePath));
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getFilterdLexicalSenses(LexicalEntryFilter lef) throws ManagerException {
        Manager.validateWithEnum("formType", FormTypes.class, lef.getFormType());
        Manager.validateWithEnum("status", LexicalEntryStatus.class, lef.getStatus());
        Manager.validateWithEnum("type", LexicalEntryTypes.class, lef.getType());
        String filter = createFilter(lef);
        int limit = lef.getLimit();
        int offset = lef.getOffset();
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelectData.DATA_LEXICAL_SENSES.replace("[FILTER]", filter)
                        .replace("[LIMIT]", String.valueOf(limit))
                        .replace("[OFFSET]", String.valueOf(offset)));
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getLexicalEntry(String lexicalEntryID, String aspect) throws ManagerException {
        Manager.validateWithEnum("aspect", EnumUtil.LexicalAspects.class, aspect);
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelectData.DATA_LEXICAL_ENTRY_CORE.replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\""));
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getLexicalEntryReferenceLinks(String lexicalEntryID) {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelectData.DATA_LEXICAL_ENTRY_REFERENCE_LINKS.replace("[IRI]", "\\\"" + namespace + lexicalEntryID + "\\\""));
        return tupleQuery.evaluate();
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

}
