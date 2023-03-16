/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Component;
import it.cnr.ilc.lexo.service.data.lexicon.output.ComponentItem;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ComponentHelper extends TripleStoreDataHelper<Component> {

    @Override
    public void fillData(Component data, BindingSet bs) {
       data.setComponent(getStringValue(bs, SparqlVariable.COMPONENT));
       data.setConfidence(getDoubleNumber(bs, "confidence"));
       data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
       data.setCreator(getStringValue(bs, SparqlVariable.COMPONENT_CREATION_AUTHOR));
       data.setLabel(getStringValue(bs, SparqlVariable.LABEL));
       data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
       data.setNote(getStringValue(bs, SparqlVariable.NOTE));
       data.setPosition(getIntegerNumber(bs, SparqlVariable.COMPONENT_POSITION));
       data.setMorphology(getMorphology(bs, SparqlVariable.MORPHOLOGY));
    }

    @Override
    public Class<Component> getDataClass() {
        return Component.class;
    }

}
