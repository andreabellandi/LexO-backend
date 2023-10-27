/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntityLinksItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntityLinksItem.Link;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicalEntityLinksItemHelper extends TripleStoreDataHelper<LexicalEntityLinksItem> {

    @Override
    public void fillData(LexicalEntityLinksItem data, BindingSet bs) {
        data.setLinks(getLinks(bs));
    }

    @Override
    public Class<LexicalEntityLinksItem> getDataClass() {
        return LexicalEntityLinksItem.class;
    }

    private ArrayList<Link> getLinks(BindingSet bs) {
        ArrayList<Link> links = new ArrayList();
        links.add(getReference(bs));
        links.add(getBibliography(bs));
        links.add(getMultimedia(bs));
        links.add(getAttestation(bs));
        links.add(getOtherLinks(bs));
        return links;
    }

    private Link getReference(BindingSet bs) {
        Link l = new Link();
        l.setType("Reference");
        ArrayList<LexicalEntityLinksItem.Element> elems = new ArrayList();
        elems.add(getElement(bs, SparqlVariable.SEEALSO));
        elems.add(getElement(bs, SparqlVariable.SAMEAS));
        l.setElements(elems);
        return l;
    }

    private Link getBibliography(BindingSet bs) {
        Link l = new Link();
        l.setType("Bibliography");
        ArrayList<LexicalEntityLinksItem.Element> elems = new ArrayList();
        elems.add(getElement(bs, SparqlVariable.BIBLIOGRAPHY));
        l.setElements(elems);
        return l;
    }

    private Link getMultimedia(BindingSet bs) {
        Link l = new Link();
        l.setType("Multimedia");
        l.setElements(new ArrayList());
        return l;
    }

    private Link getAttestation(BindingSet bs) {
        Link l = new Link();
        l.setType("Attestation");
        l.setElements(new ArrayList());
        return l;
    }

    private Link getOtherLinks(BindingSet bs) {
        Link l = new Link();
        l.setType("Other");
        l.setElements(new ArrayList());
        return l;
    }

    private LexicalEntityLinksItem.Element getElement(BindingSet bs, String var) {
        LexicalEntityLinksItem.Element e = new LexicalEntityLinksItem.Element(var,
                Integer.parseInt(bs.getBinding(var + "Count").getValue().stringValue()) < 0 ? 0
                : Integer.parseInt(bs.getBinding(var + "Count").getValue().stringValue()),
                (Integer.parseInt(bs.getBinding(var + "Count").getValue().stringValue()) > 0));
        return e;
    }

}
