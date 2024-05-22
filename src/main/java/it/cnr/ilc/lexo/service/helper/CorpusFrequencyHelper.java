/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Collocation;
import it.cnr.ilc.lexo.service.data.lexicon.output.CorpusFrequency;
import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.TreeMap;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class CorpusFrequencyHelper extends TripleStoreDataHelper<CorpusFrequency> {

    @Override
    public void fillData(CorpusFrequency data, BindingSet bs) {
        data.setCorpusFrequency(getStringValue(bs, SparqlVariable.CORPUS_FREQUENCY));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setFrequency(getIntegerNumber(bs, SparqlVariable.FREQUENCY));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.CREATOR));
        data.setCorpus(getStringValue(bs, SparqlVariable.SOURCE));
    }

    
    @Override
    public Class<CorpusFrequency> getDataClass() {
        return CorpusFrequency.class;
    }

}
