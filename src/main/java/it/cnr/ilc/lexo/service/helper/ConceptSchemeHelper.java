/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.output.GroupedLinkedEntity;
import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.service.data.lexicon.output.skos.ConceptScheme;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ConceptSchemeHelper extends TripleStoreDataHelper<ConceptScheme> {

    private final String skosDefaultLangLabel = LexOProperties.getProperty("skos.defaultLanguageLabel");
    private final List<String> lexicalLabels = Arrays.asList(
            SparqlVariable.LABEL + "Grouped", 
            SparqlVariable.PREF_LABEL + "Grouped", 
            SparqlVariable.ALT_LABEL + "Grouped", 
            SparqlVariable.HIDDEN_LABEL + "Grouped");
    private final List<String> noteProperties = Arrays.asList(
            SparqlVariable.NOTE + "Grouped", 
            SparqlVariable.DEFINITION + "Grouped", 
            SparqlVariable.HISTORY_NOTE + "Grouped", 
            SparqlVariable.SCOPE_NOTE + "Grouped",
            SparqlVariable.CHANGE_NOTE + "Grouped", 
            SparqlVariable.EDITORIAL_NOTE + "Grouped", 
            SparqlVariable.EXAMPLE + "Grouped");

    @Override
    public void fillData(ConceptScheme data, BindingSet bs) {
        // add count hits when CS index will be available
        data.setConceptScheme(getStringValue(bs, SparqlVariable.CONCEPT_SCHEME));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.CONCEPT_SCHEME_CREATOR));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        ArrayList<GroupedLinkedEntity> glel = new ArrayList();
        glel.add(new GroupedLinkedEntity("Lexical Labels", getEntitiesByRelationGroup(lexicalLabels, bs)));
        glel.add(new GroupedLinkedEntity("Note Properties", getEntitiesByRelationGroup(noteProperties, bs)));
        data.setEntities(glel);
    }

    private List<LinkedEntity> getEntitiesByRelationGroup(List<String> list, BindingSet bs) {
        ArrayList<LinkedEntity> lel = new ArrayList();
        for (String el : list) {
            for (String s : getStringValue(bs, el).split(";")) {
                if (!s.isEmpty()) {
                    if (s.split(":")[1].equals(skosDefaultLangLabel)) {
                        LinkedEntity le = new LinkedEntity();
                        le.setLabel(s.split(":")[0] + "@" + s.split(":")[1]);
                        le.setLink(el.replace("Grouped", ""));
                        le.setInferred(false);
                        lel.add(le);
                    }
                }
            }
        }
        return lel;
    }

    @Override
    public Class<ConceptScheme> getDataClass() {
        return ConceptScheme.class;
    }

}
