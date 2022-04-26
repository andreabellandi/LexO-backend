/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.FormByLexicalEntry;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class FormByLexicalEntryHelper extends TripleStoreDataHelper<FormByLexicalEntry> {

    @Override
    public void fillData(FormByLexicalEntry data, BindingSet bs) {
        data.setForm(getStringValue(bs, SparqlVariable.FORM));
        data.setFormInstanceName(getLocalName(bs, SparqlVariable.FORM));
        data.setLabel(getStringValue(bs, SparqlVariable.WRITTEN_REPRESENTATION));
        data.setMorphology(getMorphologyWithPoS(bs, getStringValue(bs, SparqlVariable.MORPHOLOGY), 
                getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_POS)));
        data.setLexicalEntry(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setLexicalEntryInstanceName(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setConfidence(getDoubleNumber(bs, "confidence"));
    }

    @Override
    public Class<FormByLexicalEntry> getDataClass() {
        return FormByLexicalEntry.class;
    }

}
