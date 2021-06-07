/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LinkedEntityHelper extends TripleStoreDataHelper<LinkedEntity> {

    @Override
    public void fillData(LinkedEntity data, BindingSet bs) {
        data.setLexicalType(getLocalName(bs, SparqlVariable.TYPE));
        data.setInferred(getStringValue(bs, SparqlVariable.GRAPH).contains("implicit"));
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL));
        data.setLexicalEntity(getStringValue(bs, SparqlVariable.TARGET));
        data.setLexicalEntityInstanceName(getLocalName(bs, SparqlVariable.TARGET));
        data.setLinkType("");
    }

    @Override
    public Class<LinkedEntity> getDataClass() {
        return LinkedEntity.class;
    }


}
