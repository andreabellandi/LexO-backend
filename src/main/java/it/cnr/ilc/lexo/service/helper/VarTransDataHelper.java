/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import it.cnr.ilc.lexo.service.data.lexicon.output.VarTransData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class VarTransDataHelper extends TripleStoreDataHelper<VarTransData> {

    @Override
    public void fillData(VarTransData data, BindingSet bs) {
    }

    @Override
    public Class<VarTransData> getDataClass() {
        return VarTransData.class;
    }

}
