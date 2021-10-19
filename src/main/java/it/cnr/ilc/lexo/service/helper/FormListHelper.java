/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.queryExpansion.FormList;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class FormListHelper extends TripleStoreDataHelper<FormList> {

    @Override
    public Class<FormList> getDataClass() {
        return FormList.class;
    }

    @Override
    public void fillData(FormList data, BindingSet bs) {
    }

}
