/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class CountingHelper extends TripleStoreDataHelper<Counting> {

    @Override
    public Class<Counting> getDataClass() {
        return Counting.class;
    }

    @Override
    public void fillData(Counting data, BindingSet bs) {
        data.setLabel(bs.getBinding(SparqlVariable.LABEL).getValue().stringValue());
        data.setCount(Integer.parseInt(bs.getBinding(SparqlVariable.LABEL_COUNT).getValue().stringValue()));
    }
    
    
}
