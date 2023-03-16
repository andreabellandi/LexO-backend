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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        data.setType(getTypes(bs, getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_TYPE)));
        data.setPos(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_POS));
        data.setConfidence(getDoubleNumber(bs, "confidence"));
        data.setLabel(getLiteralLabel(bs, SparqlVariable.LABEL));
        try {
            data.setLanguage(getLiteralLanguage(bs, SparqlVariable.LABEL));
        } catch (Exception ex) {
            Logger.getLogger(LexicalEntryFilterHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        data.setStatus(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_STATUS));
        data.setRevisor(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_REVISOR));
        data.setCreator(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setMorphology(getMorphology(bs, getStringValue(bs, SparqlVariable.MORPHOLOGY)));
        data.setCompletionDate(getStringValue(bs, SparqlVariable.COMPLETION_DATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setRevisionDate(getStringValue(bs, SparqlVariable.REVISION_DATE));
        data.setAuthor(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR));
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
