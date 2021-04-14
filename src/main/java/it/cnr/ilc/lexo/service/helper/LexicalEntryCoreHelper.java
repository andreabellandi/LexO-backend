/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicalEntryCoreHelper extends TripleStoreDataHelper<LexicalEntryCore> {

    @Override
    public void fillData(LexicalEntryCore data, BindingSet bs) {
        data.setLexicalEntry(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setLexicalEntryInstanceName(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setAuthor(getStringValue(bs, SparqlVariable.AUTHOR));
        data.setConcept(getStringValue(bs, SparqlVariable.CONCEPT));
        data.setConceptInstanceName(getLocalName(bs, SparqlVariable.CONCEPT));
        data.setLabel(getLiteralLabel(bs, SparqlVariable.LABEL));
        data.setLanguage(getLiteralLanguage(bs, SparqlVariable.LABEL));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setPos(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_POS));
        data.setRevisor(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_REVISOR));
        data.setStatus(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_STATUS));
        data.setType(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_TYPE));
        data.setMorphology(getMorphology(bs, getStringValue(bs, SparqlVariable.MORPHOLOGY)));
    }
    
    @Override
    public Class<LexicalEntryCore> getDataClass() {
        return LexicalEntryCore.class;
    }
    
}
