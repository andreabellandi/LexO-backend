/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryElementItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryElementItem.Element;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicalEntryElementHelper extends TripleStoreDataHelper<LexicalEntryElementItem> {

    @Override
    public void fillData(LexicalEntryElementItem data, BindingSet bs) {
        data.setElements(getElements(bs));
        data.setType("OntoLex-Lemon model");
    }

    @Override
    public Class<LexicalEntryElementItem> getDataClass() {
        return LexicalEntryElementItem.class;
    }

    private ArrayList<Element> getElements(BindingSet bs) {
        ArrayList<Element> elems = new ArrayList();
        elems.add(getElement(bs, SparqlVariable.FORM));
        elems.add(getElement(bs, SparqlVariable.SENSE));
        elems.add(getElement(bs, SparqlVariable.FRAME));
        elems.add(getElement(bs, SparqlVariable.LEXICAL_CONCEPT));
        elems.add(getElement(bs, SparqlVariable.CONCEPT));
        elems.add(getElement(bs, SparqlVariable.LEXICAL_ENTRY_SUBTERM));
        elems.add(getElement(bs, SparqlVariable.LEXICAL_ENTRY_CONSTITUENT));
        return elems;
    }
    
    private Element getElement(BindingSet bs, String var) {
        Element e = new Element(var,
                Integer.parseInt(bs.getBinding(var + "Count").getValue().stringValue()), 
                (Integer.parseInt(bs.getBinding(var + "Count").getValue().stringValue()) > 0));
        return e;
    }
}
