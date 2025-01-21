/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ECDEntryTree;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ECDEntryTreeHelper extends TripleStoreDataHelper<ECDEntryTree> {

    @Override
    public void fillData(ECDEntryTree data, BindingSet bs) {
    }

    @Override
    public Class<ECDEntryTree> getDataClass() {
        return ECDEntryTree.class;
    }

}
