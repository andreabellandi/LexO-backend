/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.NodeLinks;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.Arrays;
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

        for (String link : new ArrayList<>(Arrays.asList(
                SparqlVariable.HYPERNYM,
                SparqlVariable.HYPONYM,
                SparqlVariable.SYNONYM,
                SparqlVariable.MERONYM,
                SparqlVariable.MERONYM_TERM))) {
            NodeLinks._Links _links = new NodeLinks._Links();
            _links.setLinkType("incoming");
            _links.setLinkName(link);
            _links.setTargets(getLinkTargets(getStringValue(bs, "in" + link + "Grouped")));
            _links.setLinkCount(_links.getTargets().size());
            data.getNodeLinks().add(_links);
        }
        
        for (String link : new ArrayList<>(Arrays.asList(
                SparqlVariable.HYPERNYM,
                SparqlVariable.HYPONYM,
                SparqlVariable.SYNONYM,
                SparqlVariable.MERONYM,
                SparqlVariable.MERONYM_TERM))) {
            NodeLinks._Links _links = new NodeLinks._Links();
            _links.setLinkType("outgoing");
            _links.setLinkName(link);
            _links.setTargets(getLinkTargets(getStringValue(bs, "out" + link + "Grouped")));
            _links.setLinkCount(_links.getTargets().size());
            data.getNodeLinks().add(_links);
        }

    }

}
