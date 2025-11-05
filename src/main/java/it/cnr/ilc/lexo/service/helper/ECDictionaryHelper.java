/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDictionary;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ECDictionaryHelper extends TripleStoreDataHelper<ECDictionary> {

    @Override
    public void fillData(ECDictionary data, BindingSet bs) {
        data.setDescription(getStringValue(bs, SparqlVariable.DESCRIPTION));
        data.setDictionary(getStringValue(bs, SparqlVariable.DICTIONARY));
        data.setEntries(getIntegerNumber(bs, SparqlVariable.DICT_ELEMENT));
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL));
        data.setLanguage(getStringValue(bs, SparqlVariable.DICT_LANGUAGE));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.CREATOR));
    }

    @Override
    public Class<ECDictionary> getDataClass() {
        return ECDictionary.class;
    }

}
