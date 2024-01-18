/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.Property;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicalSenseCoreHelper extends TripleStoreDataHelper<LexicalSenseCore> {

    @Override
    public void fillData(LexicalSenseCore data, BindingSet bs) {
        data.setConcept(getStringValue(bs, SparqlVariable.CONCEPT));
//        data.setConceptInstanceName(getLocalName(bs, SparqlVariable.CONCEPT));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.SENSE_CREATION_AUTHOR));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setSense(getStringValue(bs, SparqlVariable.SENSE));
        data.setUsage(getStringValue(bs, SparqlVariable.SENSE_USAGE));
        data.setTopic(getStringValue(bs, SparqlVariable.SENSE_TOPIC));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setLexicalEntry(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        String label = getLiteralLabel(bs, SparqlVariable.LABEL);
        String lang = getLiteralLanguage(bs, SparqlVariable.LABEL);
        data.setLexicalEntryLabel(label.isEmpty() ? "" : label + (lang.isEmpty() ? "" : "@" + lang));
        List<Property> properties = new ArrayList();
        properties.add(new Property(SparqlVariable.SENSE_DEFINITION, getStringValue(bs, SparqlVariable.SENSE_DEFINITION)));
        properties.add(new Property(SparqlVariable.SENSE_DESCRIPTION, getStringValue(bs, SparqlVariable.SENSE_DESCRIPTION)));
        properties.add(new Property(SparqlVariable.SENSE_EXAMPLE, getStringValue(bs, SparqlVariable.SENSE_EXAMPLE)));
        properties.add(new Property(SparqlVariable.SENSE_GLOSS, getStringValue(bs, SparqlVariable.SENSE_GLOSS)));
        properties.add(new Property(SparqlVariable.SENSE_TRANSLATION, getStringValue(bs, SparqlVariable.SENSE_TRANSLATION)));
        data.setDefinition(properties);
    }

    @Override
    public Class<LexicalSenseCore> getDataClass() {
        return LexicalSenseCore.class;
    }

}
