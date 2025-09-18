/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDMeaning;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ECDMeaningHelper extends TripleStoreDataHelper<ECDMeaning> {

    @Override
    public void fillData(ECDMeaning data, BindingSet bs) {
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setSense(getStringValue(bs, SparqlVariable.SENSE));
        data.setPos(getPosList(bs, getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY_POS)));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.CREATOR));
        data.setDefinition(getStringValue(bs, SparqlVariable.SENSE_DEFINITION));
        data.setDictionaryEntry(getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY));
        data.setLanguage((!getLiteralLanguage(bs, SparqlVariable.DICTIONARY_ENTRY_LABEL).isEmpty() ? getLiteralLanguage(bs, SparqlVariable.DICTIONARY_ENTRY_LABEL) : ""));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setSenseLabel(getStringValue(bs, SparqlVariable.DESCRIBE_LABEL));
        data.setLabel(getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY_LABEL)
                + (!getLiteralLanguage(bs, SparqlVariable.DICTIONARY_ENTRY_LABEL).isEmpty() ? "@" + getLiteralLanguage(bs, SparqlVariable.DICTIONARY_ENTRY_LABEL) : ""));

    }

    @Override
    public Class<ECDMeaning> getDataClass() {
        return ECDMeaning.class;
    }

}
