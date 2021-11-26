/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.SenseNodeSummary;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class SenseNodeSummaryHelper extends TripleStoreDataHelper<SenseNodeSummary> {

    @Override
    public void fillData(SenseNodeSummary data, BindingSet bs) {
        data.setDefinition(getStringValue(bs, SparqlVariable.SENSE_DEFINITION));
        data.setIRI(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setId(getLocalName(bs, SparqlVariable.SENSE));
        data.setLabel(getLiteralLabel(bs, SparqlVariable.LABEL));
        data.setPos(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_POS));
    }

    @Override
    public Class<SenseNodeSummary> getDataClass() {
        return SenseNodeSummary.class;
    }


}
