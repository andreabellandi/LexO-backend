/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ConceptSetItem;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ConceptSetItemHelper extends TripleStoreDataHelper<ConceptSetItem> {

    @Override
    public void fillData(ConceptSetItem data, BindingSet bs) {
        // add count hits when CS index will be available
        data.setConceptSet(getStringValue(bs, SparqlVariable.CONCEPT_SCHEME));
        data.setConceptSetInstanceName(getLocalName(bs, SparqlVariable.CONCEPT_SCHEME));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.CONCEPT_SCHEME_CREATOR));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setDefaultLabel(getLiteralLabel(bs, SparqlVariable.LABEL) != null ? getLiteralLabel(bs, SparqlVariable.LABEL) : getLocalName(bs, SparqlVariable.ROOT));
        data.setConceptSet(getStringValue(bs, SparqlVariable.ROOT));
        data.setConceptSetInstanceName(getLocalName(bs, SparqlVariable.ROOT));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
    }
    
    @Override
    public Class<ConceptSetItem> getDataClass() {
        return ConceptSetItem.class;
    }

}
