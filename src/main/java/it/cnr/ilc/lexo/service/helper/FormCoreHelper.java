/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.FormCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import it.cnr.ilc.lexo.service.data.lexicon.output.Property;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class FormCoreHelper extends TripleStoreDataHelper<FormCore> {

    @Override
    public void fillData(FormCore data, BindingSet bs) {
        data.setForm(getStringValue(bs, SparqlVariable.FORM));
        data.setFormInstanceName(getLocalName(bs, SparqlVariable.FORM));
        data.setCreator(getStringValue(bs, SparqlVariable.FORM_CREATION_AUTHOR));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setType(getLocalName(bs, SparqlVariable.FORM_TYPE));
        data.setMorphology(getMorphology(bs, getStringValue(bs, SparqlVariable.MORPHOLOGY)));
        data.setInheritedMorphology(getMorphology(bs));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.FORM_CREATION_AUTHOR));
        data.setConfidence(getDoubleNumber(bs, "confidence"));
        List<Property> properties = new ArrayList();
        properties.add(new Property(SparqlVariable.WRITTEN_REPRESENTATION, getStringValue(bs, SparqlVariable.WRITTEN_REPRESENTATION)));
        properties.add(new Property(SparqlVariable.PHONETIC_REPRESENTATION, getStringValue(bs, SparqlVariable.PHONETIC_REPRESENTATION)));
        properties.add(new Property(SparqlVariable.TRANSLITERATION, getStringValue(bs, SparqlVariable.TRANSLITERATION)));
        properties.add(new Property(SparqlVariable.SEGMENTATION, getStringValue(bs, SparqlVariable.SEGMENTATION)));
        properties.add(new Property(SparqlVariable.PRONUNCIATION, getStringValue(bs, SparqlVariable.PRONUNCIATION)));
        properties.add(new Property(SparqlVariable.ROMANIZATION, getStringValue(bs, SparqlVariable.ROMANIZATION)));
        data.setLabel(properties);
    }

    private ArrayList<Morphology> getMorphology(BindingSet bs) {
        ArrayList<Morphology> m = new ArrayList();
        if (!getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_POS).isEmpty()) {
            m.add(new Morphology("partOfSpeech",
                    getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_POS)));
        }
        if (!getStringValue(bs, SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_NAME).isEmpty()) {
            m.add(new Morphology(getStringValue(bs, SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_NAME),
                    getStringValue(bs, SparqlVariable.INHERITED_MORPHOLOGY_TRAIT_VALUE)));
        }
        return m;
    }

    @Override
    public Class<FormCore> getDataClass() {
        return FormCore.class;
    }
}
