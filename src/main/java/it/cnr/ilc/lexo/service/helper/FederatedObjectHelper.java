/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.FormItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.federation.FederatedObject;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class FederatedObjectHelper extends TripleStoreDataHelper<FederatedObject> {

    @Override
    public void fillData(FederatedObject data, BindingSet bs) {
    }

    @Override
    public Class<FederatedObject> getDataClass() {
        return FederatedObject.class;
    }

}
