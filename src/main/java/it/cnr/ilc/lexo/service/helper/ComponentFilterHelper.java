/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ComponentItem;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ComponentFilterHelper extends TripleStoreDataHelper<ComponentItem> {

    @Override
    public void fillData(ComponentItem data, BindingSet bs) {
        setTotalHits(getIntegerNumber(bs, SparqlVariable.TOTAL_HITS));
        data.setCreator(getStringValue(bs, SparqlVariable.COMPONENT_CREATION_AUTHOR));
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setMorphology(getMorphology(bs, getStringValue(bs, SparqlVariable.MORPHOLOGY)));
        data.setComponent(getStringValue(bs, SparqlVariable.COMPONENT));
        data.setPosition(getComponentPosition(getStringValue(bs, SparqlVariable.COMPONENT_POSITION)));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
    }

    @Override
    public Class<ComponentItem> getDataClass() {
        return ComponentItem.class;
    }

}
