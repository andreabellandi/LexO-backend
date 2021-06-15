/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.vocabulary.MorphologicalProperty;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexinfoMorphoHelper extends TripleStoreDataHelper<MorphologicalProperty> {

    @Override
    public Class<MorphologicalProperty> getDataClass() {
        return MorphologicalProperty.class;
    }

    @Override
    public void fillData(MorphologicalProperty data, BindingSet bs) {
    }

}
