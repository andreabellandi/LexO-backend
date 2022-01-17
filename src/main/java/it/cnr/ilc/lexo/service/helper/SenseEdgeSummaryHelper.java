/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.SenseEdgeSummary;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class SenseEdgeSummaryHelper extends TripleStoreDataHelper<SenseEdgeSummary> {

    @Override
    public void fillData(SenseEdgeSummary data, BindingSet bs) {
        data.setInferred(isInferred(bs, SparqlVariable.GRAPH));
        data.setId(getLocalName(bs, SparqlVariable.SOURCE) + " " + getLocalName(bs, SparqlVariable.RELATION) + " " + getLocalName(bs, SparqlVariable.TARGET));
        data.setLabelSource(getLiteralLabel(bs, SparqlVariable.SOURCE_LABEL));
        data.setTarget(getLocalName(bs, SparqlVariable.TARGET));
        data.setLabelTarget(getLiteralLabel(bs, SparqlVariable.TARGET_LABEL));
        data.setSource(getLocalName(bs, SparqlVariable.SOURCE));
        data.setRelationType(getLocalName(bs, SparqlVariable.RELATION));
    }

    @Override
    public Class<SenseEdgeSummary> getDataClass() {
        return SenseEdgeSummary.class;
    }


}
