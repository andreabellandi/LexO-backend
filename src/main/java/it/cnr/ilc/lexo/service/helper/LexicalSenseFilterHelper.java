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
import it.cnr.ilc.lexo.service.data.lexicon.output.Property;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.List;
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
        setTotalHits(getIntegerNumber(bs, SparqlVariable.TOTAL_HITS));
        String definition = (bs.getBinding(SparqlVariable.SENSE_DEFINITION) != null) ? ((Literal) bs.getBinding(SparqlVariable.SENSE_DEFINITION).getValue()).getLabel() : "";
        String name = (!definition.isEmpty() && definition.length() > Constant.SENSE_NAME_MAX_LENGHT) ? definition.substring(0, Constant.SENSE_NAME_MAX_LENGHT - 1).concat(" ...") : definition;
        data.setLexicalEntry(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setLexicalEntryInstanceName(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setPos(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_POS));
        data.setSense(getStringValue(bs, SparqlVariable.SENSE));
        data.setSenseInstanceName(getStringValue(bs, SparqlVariable.SENSE_INSTANCE_NAME));
        data.setCreator(((bs.getBinding(SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR) != null) ? bs.getBinding(SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR).getValue().stringValue() : ""));
        data.setDefinition(definition);
        data.setLabel(!definition.isEmpty() ? name : Constant.NO_SENSE_DEFINITION);
        data.setNote(((bs.getBinding(SparqlVariable.NOTE) != null) ? bs.getBinding(SparqlVariable.NOTE).getValue().stringValue() : ""));
        data.setUsage(((bs.getBinding(SparqlVariable.SENSE_USAGE) != null) ? bs.getBinding(SparqlVariable.SENSE_USAGE).getValue().stringValue() : ""));
        data.setConcept(((bs.getBinding(SparqlVariable.CONCEPT) != null) ? bs.getBinding(SparqlVariable.CONCEPT).getValue().stringValue() : ""));
        data.setConceptInstanceName(((bs.getBinding(SparqlVariable.CONCEPT_INSTANCE_NAME) != null) ? bs.getBinding(SparqlVariable.CONCEPT_INSTANCE_NAME).getValue().stringValue() : ""));
        data.setHasChildren(false);
        data.setLemma(getStringValue(bs, SparqlVariable.WRITTEN_REPRESENTATION));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setDefinition(getStringValue(bs, SparqlVariable.SENSE_DEFINITION));
        data.setDescription(getStringValue(bs, SparqlVariable.SENSE_DESCRIPTION));
        data.setSenseExample(getStringValue(bs, SparqlVariable.SENSE_EXAMPLE));
        data.setGloss(getStringValue(bs, SparqlVariable.SENSE_GLOSS));
        data.setSenseTranslation(getStringValue(bs, SparqlVariable.SENSE_TRANSLATION));
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
