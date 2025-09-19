/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDFormDetail;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDMeaning;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ECDFormHelper extends TripleStoreDataHelper<ECDFormDetail> {

    @Override
    public void fillData(ECDFormDetail data, BindingSet bs) {
        
    }

    @Override
    public Class<ECDFormDetail> getDataClass() {
        return ECDFormDetail.class;
    }

}
