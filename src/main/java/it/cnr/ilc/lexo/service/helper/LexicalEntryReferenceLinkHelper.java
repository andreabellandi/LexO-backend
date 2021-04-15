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
public class LexicalEntryReferenceLinkHelper extends TripleStoreDataHelper<LexicalEntryElementItem> {

    @Override
    public void fillData(LexicalEntryElementItem data, BindingSet bs) {
        data.setElements(getElements(bs));
        data.setType("Reference");
    }

    @Override
    public Class<LexicalEntryElementItem> getDataClass() {
        return LexicalEntryElementItem.class;
    }

    private ArrayList<LexicalEntryElementItem.Element> getElements(BindingSet bs) {
        ArrayList<LexicalEntryElementItem.Element> elems = new ArrayList();
        elems.add(getElement(bs, SparqlVariable.SEEALSO));
        elems.add(getElement(bs, SparqlVariable.SAMEAS));
        return elems;
    }

    private LexicalEntryElementItem.Element getElement(BindingSet bs, String var) {
        LexicalEntryElementItem.Element e = new LexicalEntryElementItem.Element(var,
                Integer.parseInt(bs.getBinding(var + "Count").getValue().stringValue()) < 0 ? 0
                : Integer.parseInt(bs.getBinding(var + "Count").getValue().stringValue()),
                (Integer.parseInt(bs.getBinding(var + "Count").getValue().stringValue()) > 0));
        return e;
    }

}
