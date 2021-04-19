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
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicalEntryFilterHelper extends TripleStoreDataHelper<LexicalEntryItem> {

    @Override
    public void fillData(LexicalEntryItem data, BindingSet bs) {
        data.setLexicalEntry(bs.getBinding(SparqlVariable.LEXICAL_ENTRY).getValue().stringValue());
        data.setLexicalEntryInstanceName(bs.getBinding(SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME).getValue().stringValue());
        data.setType(((IRI) bs.getBinding(SparqlVariable.LEXICAL_ENTRY_TYPE).getValue()).getLocalName());
        data.setPos(((IRI) bs.getBinding(SparqlVariable.LEXICAL_ENTRY_POS).getValue()).getLocalName());
        data.setLabel(((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel());
        data.setLanguage(((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLanguage().get());
        data.setStatus(((bs.getBinding(SparqlVariable.LEXICAL_ENTRY_STATUS) != null) ? bs.getBinding(SparqlVariable.LEXICAL_ENTRY_STATUS).getValue().stringValue() : ""));
        data.setRevisor(((bs.getBinding(SparqlVariable.LEXICAL_ENTRY_REVISOR) != null) ? bs.getBinding(SparqlVariable.LEXICAL_ENTRY_REVISOR).getValue().stringValue() : ""));
        data.setAuthor(((bs.getBinding(SparqlVariable.AUTHOR) != null) ? bs.getBinding(SparqlVariable.AUTHOR).getValue().stringValue() : ""));
        data.setNote(((bs.getBinding(SparqlVariable.NOTE) != null) ? bs.getBinding(SparqlVariable.NOTE).getValue().stringValue() : ""));
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
