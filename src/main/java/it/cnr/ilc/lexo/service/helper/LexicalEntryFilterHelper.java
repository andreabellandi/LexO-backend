/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryItem;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicalEntryFilterHelper extends TripleStoreDataHelper<LexicalEntryItem> {

    @Override
    public void fillData(LexicalEntryItem data, BindingSet bs) {
        setTotalHits(getIntegerNumber(bs, SparqlVariable.TOTAL_HITS));
        data.setLexicalEntry(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setLexicalEntryInstanceName(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME));
        data.setType(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_TYPE));
        data.setPos(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_POS));
        data.setLabel(getLiteralLabel(bs, SparqlVariable.LABEL));
        data.setLanguage(getLiteralLanguage(bs, SparqlVariable.LABEL));
        data.setStatus(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_STATUS));
        data.setRevisor(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_REVISOR));
        data.setAuthor(getStringValue(bs, SparqlVariable.AUTHOR));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setMorphology(getMorphology(bs, getStringValue(bs, SparqlVariable.MORPHOLOGY)));
        data.setHasChildren(true);
    }

    @Override
    public Class<LexicalEntryItem> getDataClass() {
        return LexicalEntryItem.class;
    }

    public LexicalEntryFilter fromJsonFilter(String json) throws HelperException {
        try {
            return objectMapper.readValue(json, LexicalEntryFilter.class);
        } catch (JsonProcessingException ex) {
            throw new HelperException("parsing error");
        }
    }

}
