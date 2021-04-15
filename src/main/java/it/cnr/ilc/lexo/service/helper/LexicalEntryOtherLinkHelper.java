/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryElementItem;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicalEntryOtherLinkHelper extends TripleStoreDataHelper<LexicalEntryElementItem> {

    @Override
    public void fillData(LexicalEntryElementItem data, BindingSet bs) {
        data.setElements(new ArrayList());
        data.setType("Other");
    }

     @Override
    public Class<LexicalEntryElementItem> getDataClass() {
        return LexicalEntryElementItem.class;
    }
    
}
