/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Etymology;
import it.cnr.ilc.lexo.service.data.lexicon.output.EtymologyItem;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class EtymologyFilterHelper extends TripleStoreDataHelper<EtymologyItem> {

    @Override
    public void fillData(EtymologyItem data, BindingSet bs) {
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.ETYMOLOGY_CREATION_AUTHOR));
        data.setEtymology(getStringValue(bs, SparqlVariable.ETYMOLOGY));
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setLexicalEntry(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setHypothesisOf(getStringValue(bs, SparqlVariable.HYPOTHESIS_OF));
    }

    @Override
    public Class<EtymologyItem> getDataClass() {
        return EtymologyItem.class;
    }

}
