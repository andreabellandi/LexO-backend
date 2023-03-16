/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalConceptItem;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicalConceptItemHelper extends TripleStoreDataHelper<LexicalConceptItem> {

    @Override
    public void fillData(LexicalConceptItem data, BindingSet bs) {
       data.setHasChildren(getIntegerNumber(bs, SparqlVariable.NGCHILD) > 0);
       data.setChildren(getIntegerNumber(bs, SparqlVariable.NGCHILD));
       data.setLexicalConcept(getStringValue(bs, SparqlVariable.CHILD));
       data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
       data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
       data.setCreator(getStringValue(bs, SparqlVariable.LEXICAL_CONCEPT_CREATOR));
       data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
       data.setLanguage(getLiteralLanguage(bs, SparqlVariable.LABEL));
       data.setDefaultLabel(getLiteralLabel(bs, SparqlVariable.LABEL) != null ? getLiteralLabel(bs, SparqlVariable.LABEL) : getLocalName(bs, SparqlVariable.CHILD));
    }

    @Override
    public Class<LexicalConceptItem> getDataClass() {
        return LexicalConceptItem.class;
    }

}
