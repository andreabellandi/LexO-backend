/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.EtymologicalLink;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class EtymologicalLinkHelper extends TripleStoreDataHelper<EtymologicalLink> {

    @Override
    public void fillData(EtymologicalLink data, BindingSet bs) {
    }

    @Override
    public Class<EtymologicalLink> getDataClass() {
        return EtymologicalLink.class;
    }

}