/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.FormItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.queryExpansion.Form;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class FormHelper extends TripleStoreDataHelper<Form> {

    @Override
    public void fillData(Form data, BindingSet bs) {
        setTotalHits(getIntegerNumber(bs, SparqlVariable.TOTAL_HITS));
//        data.setForm(getStringValue(bs, SparqlVariable.FORM));
//        data.setFormInstanceName(getLocalName(bs, SparqlVariable.FORM));
        data.setLabel(getStringValue(bs, SparqlVariable.WRITTEN_REPRESENTATION));
        data.setMorphology(getMorphologyWithPoS(bs, getStringValue(bs, SparqlVariable.MORPHOLOGY), 
                getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_POS)));
//        data.setLexicalEntry(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setLexicalEntryInstanceName(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setTargetSenseInstanceName(getLocalName(bs, SparqlVariable.SENSE));
//        data.setTargetSense(getStringValue(bs, SparqlVariable.SENSE));
data.setConfidence(getDoubleNumber(bs, "confidence"));
        data.setFormType(getLocalName(bs, SparqlVariable.FORM_TYPE));
        if (getLocalName(bs, SparqlVariable.FORM_TYPE).equals("canonicalForm")) {
            data.setDefinition(getStringValue(bs, SparqlVariable.SENSE_DEFINITION));
        }
    }

    @Override
    public Class<Form> getDataClass() {
        return Form.class;
    }

}
