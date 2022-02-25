/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ConceptSet;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalConcept;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ConceptSetHelper extends TripleStoreDataHelper<ConceptSet> {

    @Override
    public void fillData(ConceptSet data, BindingSet bs) {
       
    }

    @Override
    public Class<ConceptSet> getDataClass() {
        return ConceptSet.class;
    }

}
