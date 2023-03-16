/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.vocabulary.Value;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class VocabularyValuesHelper extends TripleStoreDataHelper<Value> {

    @Override
    public Class<Value> getDataClass() {
        return Value.class;
    }

    @Override
    public void fillData(Value data, BindingSet bs) {
    }

}
