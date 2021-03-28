/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.GraphDbUtil;
import it.cnr.ilc.lexo.LexoProperties;
import it.cnr.ilc.lexo.service.data.lexicon.LexicalEntryFilter;
import it.cnr.ilc.lexo.sparql.SparqlSelect;
import it.cnr.ilc.lexo.util.StringUtil;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 */
public class LexiconManager implements Manager, Cached {

    private final String namespace = StringUtil.escapeMetaCharacters(LexoProperties.getProperty("repository.lexicon.namespace"));
    
    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public TupleQueryResult getFilterdLexicalEntries(LexicalEntryFilter lef) {
        String filter = createFilter(lef);
        int limit = lef.getLimit();
        int offset = lef.getOffset();
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelect.LEXICAL_ENTRIES.replace("[FILTER]", filter)
                        .replace("[LIMIT]", String.valueOf(limit))
                        .replace("[OFFSET]", String.valueOf(offset)));
        return tupleQuery.evaluate();
    }

    private String createFilter(LexicalEntryFilter lef) {
        String filter = "";
        String text = lef.getText().isEmpty() ? "*" : lef.getText();
        filter = filter + (lef.isStartsWith() ? "writtenForm:" + text + "*"
                : (lef.isContains() ? "writtenForm:*" + text + "*"
                : (lef.isEnds() ? "writtenForm:*" + text : "writtenForm:" + text + "*")));
        filter = filter + (!lef.getLang().isEmpty() ? " AND writtenFormLanguage:" + lef.getLang() : "");
        filter = filter + (!lef.getAuthor().isEmpty() ? " AND author:" + lef.getAuthor() : "");
        filter = filter + (!lef.getPos().isEmpty() ? " AND pos:" + lef.getPos() : "");
        filter = filter + (!lef.getType().isEmpty() ? " AND type:" + lef.getType() : "");
        filter = filter + (lef.isVerified() ? " AND state:true" : " AND state:false");
        return filter;
    }

    public TupleQueryResult getElements(String lexicalEntryID) {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelect.LEXICAL_ENTRY.replace("[IRI]", namespace + lexicalEntryID));
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getForms(String lexicalEntryID) {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
                SparqlSelect.FORMS.replace("[IRI]", namespace + lexicalEntryID));
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getTypes() {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelect.TYPES);
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getPos() {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelect.POS);
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getStates() {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelect.STATES);
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getAuthors() {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelect.AUTHORS);
        return tupleQuery.evaluate();
    }

    public TupleQueryResult getLanguages() {
        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelect.LANGUAGES);
        return tupleQuery.evaluate();
    }
}
