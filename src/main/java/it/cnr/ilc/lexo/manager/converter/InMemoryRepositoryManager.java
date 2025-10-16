/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager.converter;

import it.cnr.ilc.lexo.manager.Manager;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.ParserUtil;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.query.*;

/**
 *
 * @author andreabellandi
 */
public class InMemoryRepositoryManager implements Manager {

    public Map<String, Object> getInfo(Path src) throws IOException {
        Map<String, Object> info = new LinkedHashMap();
        Repository repo = new SailRepository(new MemoryStore());
        repo.init();
        try ( RepositoryConnection cx = repo.getConnection()) {
            ArrayList<String> languages = getLanguages(cx);
            if (languages != null) {
                for (String lang : getLanguages(cx)) {
                    info.put("[" + lang + "] Lexical entries", getLexicalTypes(cx, ParserUtil.LEXICAL_ENTRY, lang));
                    info.put("[" + lang + "] Forms", getLexicalTypes(cx, ParserUtil.FORM, lang));
                    info.put("[" + lang + "] Lexical Senses", getLexicalTypes(cx, ParserUtil.LEXICAL_SENSE, lang));
                }
            }
        } finally {
            repo.shutDown();
        }
        return info.size() > 0 ? info : null;
    }

    private String getLexicalTypes(RepositoryConnection cx, IRI type, String lang) {
        String query = "";
        if (type.getLocalName().equals(ParserUtil.LEXICAL_ENTRY.getLocalName())) {
            query = ParserUtil.COUNT_LEXICAL_ENTRY_BY_LANGUAGE.replace("_LANG_", lang);
        } else if (type.getLocalName().equals(ParserUtil.FORM.getLocalName())) {
            query = ParserUtil.COUNT_FORM_BY_LANGUAGE.replace("_LANG_", lang);
        } else if (type.getLocalName().equals(ParserUtil.LEXICAL_SENSE.getLocalName())) {
            query = ParserUtil.COUNT_LEXICAL_SENSE_BY_LANGUAGE.replace("_LANG_", lang);
        }
        TupleQuery tq = cx.prepareTupleQuery(QueryLanguage.SPARQL, query);
        try ( TupleQueryResult res = tq.evaluate()) {
            while (res.hasNext()) {
                BindingSet bs = res.next();
                return bs.getValue(SparqlVariable.LABEL_COUNT).stringValue();
            }
        }
        return "0";
    }

    private ArrayList<String> getLanguages(RepositoryConnection cx) {
        ArrayList<String> languages = new ArrayList();
        String query = ParserUtil.GET_LEXICA;
        TupleQuery tq = cx.prepareTupleQuery(QueryLanguage.SPARQL, query);
        try ( TupleQueryResult res = tq.evaluate()) {
            while (res.hasNext()) {
                BindingSet bs = res.next();
                languages.add(bs.getValue(SparqlVariable.LEXICON).stringValue());
            }
        }
        return languages.size() > 0 ? languages : null;
    }

}
