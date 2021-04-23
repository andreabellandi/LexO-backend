/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cnr.ilc.lexo.Constant;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseItem;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicalSenseFilterHelper extends TripleStoreDataHelper<LexicalSenseItem> {

    @Override
    public void fillData(LexicalSenseItem data, BindingSet bs) {
        String definition = (bs.getBinding(SparqlVariable.SENSE_DEFINITION) != null) ? ((Literal) bs.getBinding(SparqlVariable.SENSE_DEFINITION).getValue()).getLabel() : "";
        String name = (!definition.isEmpty() && definition.length() > Constant.SENSE_NAME_MAX_LENGHT) ? definition.substring(0, Constant.SENSE_NAME_MAX_LENGHT - 1).concat(" ...") : definition;
        data.setLexicalEntry(bs.getBinding(SparqlVariable.LEXICAL_ENTRY).getValue().stringValue());
        data.setLexicalEntryInstanceName(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setPos(((IRI) bs.getBinding(SparqlVariable.LEXICAL_ENTRY_POS).getValue()).getLocalName());
        data.setSense(bs.getBinding(SparqlVariable.SENSE).getValue().stringValue());
        data.setSenseInstanceName(bs.getBinding(SparqlVariable.SENSE_INSTANCE_NAME).getValue().stringValue());
        data.setAuthor(((bs.getBinding(SparqlVariable.AUTHOR) != null) ? bs.getBinding(SparqlVariable.AUTHOR).getValue().stringValue() : ""));
        data.setDefinition(definition);
        data.setLabel(!definition.isEmpty() ? name : Constant.NO_SENSE_DEFINITION);
        data.setNote(((bs.getBinding(SparqlVariable.NOTE) != null) ? bs.getBinding(SparqlVariable.NOTE).getValue().stringValue() : ""));
        data.setUsage(((bs.getBinding(SparqlVariable.SENSE_USAGE) != null) ? bs.getBinding(SparqlVariable.SENSE_USAGE).getValue().stringValue() : ""));
        data.setConcept(((bs.getBinding(SparqlVariable.CONCEPT) != null) ? bs.getBinding(SparqlVariable.CONCEPT).getValue().stringValue() : ""));
        data.setConceptInstanceName(((bs.getBinding(SparqlVariable.CONCEPT_INSTANCE_NAME) != null) ? bs.getBinding(SparqlVariable.CONCEPT_INSTANCE_NAME).getValue().stringValue() : ""));
        data.setHasChildren(false);
    }

    @Override
    public Class<LexicalSenseItem> getDataClass() {
        return LexicalSenseItem.class;
    }

    public LexicalEntryFilter fromJsonFilter(String json) throws HelperException {
        try {
            return objectMapper.readValue(json, LexicalEntryFilter.class);
        } catch (JsonProcessingException ex) {
            throw new HelperException("parsing error");
        }
    }

}
