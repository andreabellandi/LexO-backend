/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Etymology;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class EtymologyHelper extends TripleStoreDataHelper<Etymology> {

    @Override
    public void fillData(Etymology data, BindingSet bs) {
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.ETYMOLOGY_CREATION_AUTHOR));
        data.setEtymology(getStringValue(bs, SparqlVariable.ETYMOLOGY));
        data.setEtymologyInstanceName(getLocalName(bs, SparqlVariable.ETYMOLOGY));
        data.setHypothesisOf(getStringValue(bs, SparqlVariable.HYPOTHESIS_OF));
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
    }

    @Override
    public Class<Etymology> getDataClass() {
        return Etymology.class;
    }

}
