/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.FormItem;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class FormItemsHelper extends TripleStoreDataHelper<FormItem> {

    @Override
    public void fillData(FormItem data, BindingSet bs) {
        setTotalHits(getIntegerNumber(bs, SparqlVariable.TOTAL_HITS));
        data.setCreator(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR));
        data.setForm(getStringValue(bs, SparqlVariable.FORM));
        data.setLabel(getStringValue(bs, SparqlVariable.WRITTEN_REPRESENTATION));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setPhoneticRep(getStringValue(bs, SparqlVariable.PHONETIC_REPRESENTATION));
        data.setType(getLocalName(bs, SparqlVariable.FORM_TYPE));
        data.setMorphology(getMorphologyWithPoS(bs, getStringValue(bs, SparqlVariable.MORPHOLOGY), 
                getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_POS)));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setLexicalEntry(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setTargetSense(getStringValue(bs, SparqlVariable.TARGET));
        data.setConfidence(getDoubleNumber(bs, "confidence"));
    }

    @Override
    public Class<FormItem> getDataClass() {
        return FormItem.class;
    }

}
