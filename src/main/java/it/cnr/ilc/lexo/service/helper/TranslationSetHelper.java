/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.TranslationSet;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class TranslationSetHelper extends TripleStoreDataHelper<TranslationSet> {

    @Override
    public void fillData(TranslationSet data, BindingSet bs) {
       data.setConfidence(getDoubleNumber(bs, "confidence"));
       data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
       data.setCreator(getStringValue(bs, SparqlVariable.COMPONENT_CREATION_AUTHOR));
       data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
    }

    @Override
    public Class<TranslationSet> getDataClass() {
        return TranslationSet.class;
    }

}
