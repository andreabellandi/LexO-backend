/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.GraphDbUtil;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalFilter;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.EnumUtil.FormTypes;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalEntryStatus;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalEntryTypes;
import it.cnr.ilc.lexo.util.StringUtil;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 */
public class LexiconDataManager implements Manager, Cached {

    private final String namespace = StringUtil.escapeMetaCharacters(LexOProperties.getProperty("repository.lexicon.namespace"));

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public TupleQueryResult getFilterdLexicalEntries(LexicalFilter lef) throws ManagerException {
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

    private String createFilter(LexicalFilter lef) {
        String text = lef.getText().isEmpty() ? "*" : lef.getText();
        String filter = "(" + (lef.getSearchMode().equals(EnumUtil.SearchModes.Equals.toString()) ? getSearchField(lef.getFormType(), text)
                : (lef.getSearchMode().equals(EnumUtil.SearchModes.StartsWith.toString()) ? getSearchField(lef.getFormType(), text + "*")
                : (lef.getSearchMode().equals(EnumUtil.SearchModes.Contains.toString()) ? getSearchField(lef.getFormType(), "*" + text + "*")
                : getSearchField(lef.getFormType(), "*" + text)))) + ")";
        filter = filter + (!lef.getLang().isEmpty() ? " AND writtenFormLanguage:" + lef.getLang() : "");
        filter = filter + (!lef.getAuthor().isEmpty() ? " AND author:" + lef.getAuthor() : "");
        filter = filter + (!lef.getPos().isEmpty() ? " AND pos:" + lef.getPos() : "");
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
                SparqlSelectData.DATA_LEXICAL_ENTRY_ELEMENTS.replace("[IRI]", namespace + lexicalEntryID));
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getForms(String lexicalEntryID) {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelectData.DATA_FORMS.replace("[IRI]", namespace + lexicalEntryID));
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getFilterdLexicalSenses(LexicalFilter lef) throws ManagerException {
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
                SparqlSelectData.DATA_LEXICAL_ENTRY_CORE.replace("[IRI]", namespace + lexicalEntryID));
        return tupleQuery.evaluate();
    }

}
