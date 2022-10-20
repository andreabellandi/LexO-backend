/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ReferencedLinguisticObject;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ReferencedLinguisticObjectListHelper extends TripleStoreDataHelper<ReferencedLinguisticObject> {

    @Override
    public void fillData(ReferencedLinguisticObject data, BindingSet bs) {
        data.setLexicalEntry(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setLexicalEntryInstanceName(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setPos(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_POS));
        data.setConceptInstanceName(getLocalName(bs, SparqlVariable.CONCEPT));
        data.setLemma(getStringValue(bs, SparqlVariable.WRITTEN_REPRESENTATION));
        data.setDefinition(getStringValue(bs, SparqlVariable.SENSE_DEFINITION));
        data.setTraitInstanceName(getLocalName(bs, SparqlVariable.TRAIT));
    }

    @Override
    public Class<ReferencedLinguisticObject> getDataClass() {
        return ReferencedLinguisticObject.class;
    }


}
