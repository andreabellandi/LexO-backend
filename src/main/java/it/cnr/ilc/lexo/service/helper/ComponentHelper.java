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
       
    }

    @Override
    public Class<Component> getDataClass() {
        return Component.class;
    }

}
