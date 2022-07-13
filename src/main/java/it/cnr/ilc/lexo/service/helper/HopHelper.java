/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.Hop;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class HopHelper extends TripleStoreDataHelper<Hop> {

    @Override
    public Class<Hop> getDataClass() {
        return Hop.class;
    }

    @Override
    public void fillData(Hop data, BindingSet bs) {
        data.setLenght(Integer.parseInt(bs.getBinding(SparqlVariable.LENGHT).getValue().stringValue()));
        ArrayList<String> hops = new ArrayList();
        for (String st : bs.getBinding(SparqlVariable.HOPS).getValue().stringValue().split(";")) {
            String nodes[] = st.replace("<<", "").replace(">>", "").split("\\s");
                if (!hops.contains(nodes[0])) hops.add(nodes[0]);
                if (!hops.contains(nodes[2])) hops.add(nodes[2]);
        }
        data.setHops(hops);
    }
    
    
}
