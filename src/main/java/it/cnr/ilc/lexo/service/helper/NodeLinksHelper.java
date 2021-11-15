/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.graphviz.NodeLinks;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class NodeLinksHelper extends TripleStoreDataHelper<NodeLinks> {

    @Override
    public Class<NodeLinks> getDataClass() {
        return NodeLinks.class;
    }

    @Override
    public void fillData(NodeLinks data, BindingSet bs) {
        data.setHolonymNr(getIntegerNumber(bs, SparqlVariable.HOLONYM));
        data.setHypernymNr(getIntegerNumber(bs, SparqlVariable.HYPERNYM));
        data.setIncomingSynonymNr(getIntegerNumber(bs, "in" + SparqlVariable.SYNONYM + "Count"));
        data.setMeronymNr(getIntegerNumber(bs, SparqlVariable.MERONYM));
        data.setOutcomingSynonymNr(getIntegerNumber(bs, "out" + SparqlVariable.SYNONYM + "Count"));
    }
    
    
}
