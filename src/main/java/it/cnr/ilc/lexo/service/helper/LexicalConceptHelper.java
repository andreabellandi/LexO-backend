/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalConcept;
import it.cnr.ilc.lexo.service.data.output.Label;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicalConceptHelper extends TripleStoreDataHelper<LexicalConcept> {

    @Override
    public void fillData(LexicalConcept data, BindingSet bs) {
        data.setDefaultLabel(getDefaultLabel(getStringValue(bs, SparqlVariable.PREF_LABEL) != null ? getStringValue(bs, SparqlVariable.PREF_LABEL) : null, ";"));
        data.setLanguage(LexOProperties.getProperty("skos.defaultLanguageLabel"));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.LEXICAL_CONCEPT_CREATOR));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setLexicalConcept(getStringValue(bs, SparqlVariable.LEXICAL_CONCEPT));
        data.setDefinition(getStringValue(bs, SparqlVariable.DEFINITION));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setInScheme(getStringValue(bs, SparqlVariable.CONCEPT_SCHEME));
        List<Label> labels = new ArrayList();
        labels.addAll(getLabel(getStringValue(bs, SparqlVariable.PREF_LABEL) != null ? getStringValue(bs, SparqlVariable.PREF_LABEL) : null, "prefLabel", ";"));
        labels.addAll(getLabel(getStringValue(bs, SparqlVariable.ALT_LABEL) != null ? getStringValue(bs, SparqlVariable.ALT_LABEL) : null, "altLabel", ";"));
        labels.addAll(getLabel(getStringValue(bs, SparqlVariable.HIDDEN_LABEL) != null ? getStringValue(bs, SparqlVariable.HIDDEN_LABEL) : null, "hiddenLabel", ";"));
//        labels.addAll(getLabel(getStringValue(bs, SparqlVariable.DEFINITION) != null ? getStringValue(bs, SparqlVariable.DEFINITION) : null, "definition", "-:-"));
        data.setLabels(labels);
        data.setEntities(new ArrayList());
        //GroupedLinkedEntity gle = new GroupedLinkedEntity();
        //List<LinkedEntity> entities = new ArrayList();

    }

    private List<Label> getLabel(String labels, String labelType, String separator) {
        List<Label> label = new ArrayList();
        if (labels != null) {
            if (!labels.isEmpty()) {
                for (String pf : labels.split(separator)) {
                    Label l = new Label();
                    l.setLabel(pf.split("@")[0]);
                    l.setLanguage(pf.split("@")[1]);
                    l.setType(labelType);
                    label.add(l);
                }
            }
        }
        return label;
    }
    
    private String getDefaultLabel(String labels, String separator) {
        if (labels != null) {
            if (!labels.isEmpty()) {
                for (String pf : labels.split(separator)) {
                    if (pf.split("@")[1].equals(LexOProperties.getProperty("skos.defaultLanguageLabel"))) {
                        return pf;
                    }
                }
            }
        }
        return "";
    }

    @Override
    public Class<LexicalConcept> getDataClass() {
        return LexicalConcept.class;
    }

}
