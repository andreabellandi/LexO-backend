/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ReifiedRelation;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class IndirectLexicalRelationHelper extends TripleStoreDataHelper<ReifiedRelation> {

    @Override
    public void fillData(ReifiedRelation data, BindingSet bs) {
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setProperties(getLinkedEntities(bs, getStringValue(bs, SparqlVariable.EXTRA)));
        data.setRelation(getStringValue(bs, SparqlVariable.REIFIED_RELATION));
        data.setSource(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setTarget(getStringValue(bs, SparqlVariable.TARGET));
        data.setTargetDefinition(getStringValue(bs, SparqlVariable.DEFINITION));
        data.setTargetLabel(getStringValue(bs, SparqlVariable.TARGET_LABEL));
        data.setType(getStringValue(bs, SparqlVariable.CATEGORY));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.COMPONENT_CREATION_AUTHOR));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
    }

    @Override
    public Class<ReifiedRelation> getDataClass() {
        return ReifiedRelation.class;
    }

}
