/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.FormItem;
import it.cnr.ilc.lexo.service.data.lexicon.Morphology;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class FormsListHelper extends TripleStoreDataHelper<FormItem> {

    private final String MORPHOLOGY_PATTERN = "(([a-zA-Z]+)\\:([a-zA-Z]+));?";
    private final Pattern pattern = Pattern.compile(MORPHOLOGY_PATTERN);

    @Override
    public void fillData(FormItem data, BindingSet bs) {
        data.setAuthor(((bs.getBinding(SparqlVariable.AUTHOR) != null) ? bs.getBinding(SparqlVariable.AUTHOR).getValue().stringValue() : ""));
        data.setIRI(bs.getBinding(SparqlVariable.FORM).getValue().stringValue());
        data.setInstanceName(bs.getBinding(SparqlVariable.INSTANCE_NAME).getValue().stringValue());
        data.setLabel(bs.getBinding(SparqlVariable.WRITTEN_REPRESENTATION).getValue().stringValue());
        data.setNote(((bs.getBinding(SparqlVariable.NOTE) != null) ? bs.getBinding(SparqlVariable.NOTE).getValue().stringValue() : ""));
        data.setPhoneticRep(((bs.getBinding(SparqlVariable.PHONETIC_REPRESENTATION) != null) ? bs.getBinding(SparqlVariable.PHONETIC_REPRESENTATION).getValue().stringValue() : ""));
        data.setType(bs.getBinding(SparqlVariable.FORM_TYPE).getValue().stringValue());
        data.setMorphology(getMorphology(bs.getBinding(SparqlVariable.MORPHOLOGY) != null ? bs.getBinding(SparqlVariable.MORPHOLOGY).getValue().stringValue() : ""));
    }

    @Override
    public Class<FormItem> getDataClass() {
        return FormItem.class;
    }

    private ArrayList<Morphology> getMorphology(String morpho) {
        ArrayList<Morphology> morphos = new ArrayList();
        if (!morpho.isEmpty()) {
            Matcher matcher = pattern.matcher(morpho);
            while (matcher.find()) {
                morphos.add(new Morphology(matcher.group(2), matcher.group(3)));
            }
        }
        return morphos;
    }

}
