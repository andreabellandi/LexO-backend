/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDMeaning;
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
    }

    @Override
    public Class<ECDictionary> getDataClass() {
        return ECDictionary.class;
    }

}
