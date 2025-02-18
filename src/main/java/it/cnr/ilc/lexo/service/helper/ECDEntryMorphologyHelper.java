/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDEntryMorphology;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ECDEntryMorphologyHelper extends TripleStoreDataHelper<ECDEntryMorphology> {

    @Override
    public void fillData(ECDEntryMorphology data, BindingSet bs) {
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setPos(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_POS));
        data.setType(getStringValue(bs, SparqlVariable.FORM_TYPE));
        data.setLabel(getStringValue(bs, SparqlVariable.WRITTEN_REPRESENTATION)
                + (!getLiteralLanguage(bs, SparqlVariable.WRITTEN_REPRESENTATION).isEmpty() ? "@" + getLiteralLanguage(bs, SparqlVariable.WRITTEN_REPRESENTATION) : ""));
        data.setPhoneticRep(getStringValue(bs, SparqlVariable.PHONETIC_REPRESENTATION));
        data.setForm(getStringValue(bs, SparqlVariable.FORM));
        data.setInheritedMorphology(getECDMorphology(getStringValue(bs, SparqlVariable.INHERITED_MORPHOLOGY)));
        data.setMorphology(getECDMorphology(getStringValue(bs, SparqlVariable.MORPHOLOGY)));
    }

    private ArrayList<Morphology> getECDMorphology(String morphology) {
        ArrayList<Morphology> alm = new ArrayList<>();
        for (String s : morphology.split(";")) {
            String morph[] = s.split("->");
            if (!"none".equals(morph[0])) {
                Morphology m = new Morphology(morph[0], morph[1]);
                alm.add(m);
            }
        }
        return alm;
    }

    @Override
    public Class<ECDEntryMorphology> getDataClass() {
        return ECDEntryMorphology.class;
    }

}
