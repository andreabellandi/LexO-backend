/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.FormRestriction;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class FormRestrictionHelper extends TripleStoreDataHelper<FormRestriction> {

    @Override
    public void fillData(FormRestriction data, BindingSet bs) {
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL));
        data.setFormRestriction(getStringValue(bs, SparqlVariable.FORM_RESTRICTION));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setExample(getStringValue(bs, SparqlVariable.EXAMPLE));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.CREATOR));
        data.setMorphology(getMorphology(bs, getStringValue(bs, SparqlVariable.MORPHOLOGY)));
    }

    
    @Override
    public Class<FormRestriction> getDataClass() {
        return FormRestriction.class;
    }

}
