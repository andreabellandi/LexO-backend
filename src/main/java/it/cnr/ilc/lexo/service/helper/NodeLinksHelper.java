/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.NodeLinks;
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
        data.setInferred(isInferred(bs, SparqlVariable.GRAPH));
        data.setInHypernym(getIntegerNumber(bs, "in" + SparqlVariable.HYPERNYM + "Count"));
        data.setInHyponym(getIntegerNumber(bs, "in" + SparqlVariable.HYPONYM + "Count"));
        data.setInPartMeronym(getIntegerNumber(bs, "in" + SparqlVariable.PART_MERONYM + "Count"));
        data.setInSynonym(getIntegerNumber(bs, "in" + SparqlVariable.SYNONYM + "Count"));
        data.setInMeronymTerm(getIntegerNumber(bs, "in" + SparqlVariable.MERONYM_TERM + "Count"));
        data.setOutHypernym(getIntegerNumber(bs, "out" + SparqlVariable.HYPERNYM + "Count"));
        data.setOutHyponym(getIntegerNumber(bs, "out" + SparqlVariable.HYPONYM + "Count"));
        data.setOutMeronymTerm(getIntegerNumber(bs, "out" + SparqlVariable.MERONYM_TERM + "Count"));
        data.setOutPartMeronym(getIntegerNumber(bs, "out" + SparqlVariable.PART_MERONYM + "Count"));
        data.setOutSynonym(getIntegerNumber(bs, "out" + SparqlVariable.SYNONYM + "Count"));
    }
    
    
}
