/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Etymology;
import it.cnr.ilc.lexo.service.data.lexicon.output.EtymologyTree;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class EtymologyTreeHelper extends TripleStoreDataHelper<EtymologyTree> {

    @Override
    public void fillData(EtymologyTree data, BindingSet bs) {
    }

    @Override
    public Class<EtymologyTree> getDataClass() {
        return EtymologyTree.class;
    }

}
