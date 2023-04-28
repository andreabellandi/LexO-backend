/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ImportDetail;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ImportDetailHelper extends TripleStoreDataHelper<ImportDetail> {

    @Override
    public Class<ImportDetail> getDataClass() {
        return ImportDetail.class;
    }

    @Override
    public void fillData(ImportDetail data, BindingSet bs) {
    }
    
    
}
