/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.LexicalEntryElement;
import it.cnr.ilc.lexo.service.data.lexicon.LexicalEntryElement.Element;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicalEntryElementHelper extends TripleStoreDataHelper<LexicalEntryElement> {

    @Override
    public void fillData(LexicalEntryElement data, BindingSet bs) {
        data.setElements(getElements(bs));
    }

    @Override
    public Class<LexicalEntryElement> getDataClass() {
        return LexicalEntryElement.class;
    }

    private ArrayList<Element> getElements(BindingSet bs) {
        ArrayList<Element> elems = new ArrayList();
        elems.add(getElement(bs, SparqlVariable.FORM));
        elems.add(getElement(bs, SparqlVariable.SENSE));
        elems.add(getElement(bs, SparqlVariable.FRAME));
        elems.add(getElement(bs, SparqlVariable.LEXICAL_CONCEPT));
        elems.add(getElement(bs, SparqlVariable.CONCEPT));
        return elems;
    }
    
    private Element getElement(BindingSet bs, String var) {
        Element e = new Element(var,
                Integer.parseInt(bs.getBinding(var + "Count").getValue().stringValue()), 
                (Integer.parseInt(bs.getBinding(var + "Count").getValue().stringValue()) > 0));
        return e;
    }
}
