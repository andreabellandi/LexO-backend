/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Property;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class PropertyValueHelper extends TripleStoreDataHelper<Property> {

    @Override
    public void fillData(Property data, BindingSet bs) {
        data.setPropertyID(getStringValue(bs, "subject"));
        data.setPropertyValue(getStringValue(bs, "object"));
    }

    @Override
    public Class<Property> getDataClass() {
        return Property.class;
    }

}
