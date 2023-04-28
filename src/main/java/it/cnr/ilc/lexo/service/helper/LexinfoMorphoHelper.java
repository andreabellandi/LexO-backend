/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.vocabulary.RangedProperty;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexinfoMorphoHelper extends TripleStoreDataHelper<RangedProperty> {

    @Override
    public Class<RangedProperty> getDataClass() {
        return RangedProperty.class;
    }

    @Override
    public void fillData(RangedProperty data, BindingSet bs) {
    }

}
