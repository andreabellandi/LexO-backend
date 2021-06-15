/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.vocabulary.MorphologicalProperty;
import it.cnr.ilc.lexo.service.data.vocabulary.PropertyHierarchy;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexinfoPropertyHierarchyHelper extends TripleStoreDataHelper<PropertyHierarchy> {

    @Override
    public Class<PropertyHierarchy> getDataClass() {
        return PropertyHierarchy.class;
    }
    @Override
    public void fillData(PropertyHierarchy data, BindingSet bs) {
   
    }

}
