/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Collocation;
import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.TreeMap;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class CollocationHelper extends TripleStoreDataHelper<Collocation> {

    @Override
    public void fillData(Collocation data, BindingSet bs) {
        data.setCollocation(getStringValue(bs, SparqlVariable.COLLOCATION));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setDescription(getStringValue(bs, SparqlVariable.DESCRIPTION));
        data.setExample(getStringValue(bs, SparqlVariable.EXAMPLE));
        data.setFrequency(getIntegerNumber(bs, SparqlVariable.FREQUENCY));
        data.setHead(getStringValue(bs, SparqlVariable.HEAD));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setType(getStringValue(bs, SparqlVariable.LABEL));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.CREATOR));
        data.setComponents(getComponents(getStringValue(bs, "components")));
    }

    private TreeMap<Integer, LinkedEntity> getComponents(String comps) {
        // comps = iri, label, type, position ---
        TreeMap tm = new TreeMap<Integer, LinkedEntity>();
        if (comps == null) {
            return tm;
        } else if (comps.isEmpty()) {
            return tm;
        } else {
            if (!comps.contains("---")) {
                LinkedEntity le = new LinkedEntity();
                setLinkedEntity(le, comps);
                tm.put(Integer.parseInt(comps.split(";")[3]), le);
            } else {
                for (String c : comps.split("---")) {
                    LinkedEntity le = new LinkedEntity();
                    setLinkedEntity(le, c);
                    tm.put(Integer.parseInt(c.split(";")[3]), le);
                }
            }
        }
        return tm;
    }

    private void setLinkedEntity(LinkedEntity le, String c) {
        le.setEntity(c.split(";")[0]);
        ArrayList<String> al = new ArrayList<>();
        al.add(c.split(";")[2]);
        le.setEntityType(al);
        le.setInferred(false);
        if ((c.split(";")[1]).split("@").length == 2) {
            le.setLabel(c.split(";")[1]);
        } else {
            le.setLabel(c.split(";")[1].replace("@", ""));
        }
        le.setLink("http://www.w3.org/1999/02/22-rdf-syntax-ns#_");
        le.setLinkType("internal");
    }

    @Override
    public Class<Collocation> getDataClass() {
        return Collocation.class;
    }

}
