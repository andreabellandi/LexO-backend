/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cnr.ilc.lexo.service.data.lexicon.LexicalEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.LexicalEntryItem;
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
        data.setIRI(bs.getBinding("lexicalEntry").getValue().stringValue());
        data.setInstanceName(bs.getBinding("instanceName").getValue().stringValue());
        data.setType(((IRI) bs.getBinding("type").getValue()).getLocalName());
        data.setPos(((IRI) bs.getBinding("pos").getValue()).getLocalName());
        data.setLabel(((Literal) bs.getBinding("label").getValue()).getLabel());
        data.setLanguage(((Literal) bs.getBinding("label").getValue()).getLanguage().get());
        data.setVerified(((bs.getBinding("state") != null) ? Boolean.parseBoolean(bs.getBinding("state").getValue().stringValue()) : false));
        data.setWhoVerified(((bs.getBinding("revisor") != null) ? bs.getBinding("revisor").getValue().stringValue() : ""));
        data.setAuthor(((bs.getBinding("author") != null) ? bs.getBinding("author").getValue().stringValue() : ""));
        data.setNote(((bs.getBinding("note") != null) ? bs.getBinding("note").getValue().stringValue() : ""));
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
