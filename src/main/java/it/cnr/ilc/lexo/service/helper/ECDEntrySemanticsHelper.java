/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDEntrySemantics;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ECDEntrySemanticsHelper extends TripleStoreDataHelper<ECDEntrySemantics> {

    @Override
    public void fillData(ECDEntrySemantics data, BindingSet bs) {
    }

    @Override
    public Class<ECDEntrySemantics> getDataClass() {
        return ECDEntrySemantics.class;
    }

}
