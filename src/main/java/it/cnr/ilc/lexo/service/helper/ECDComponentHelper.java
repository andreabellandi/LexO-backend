/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ECDComponent;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.Arrays;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ECDComponentHelper extends TripleStoreDataHelper<ECDComponent> {

    @Override
    public void fillData(ECDComponent data, BindingSet bs) {
        data.setCreator(getStringValue(bs, SparqlVariable.CREATOR));
        data.setAuthor(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setPos(getPosList(bs, getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_POS)));
        data.setRevisor(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_REVISOR));
        data.setStatus(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_STATUS));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setType((getStringValue(bs, SparqlVariable.TYPE).isEmpty()) ? new ArrayList<String>(Arrays.asList("LOCAL ROOT")) : getTypes(bs));
//        data.setSenseNumber(getStringValue(bs, SparqlVariable.ECD_SENSE_LABEL));
        data.setComponent(getStringValue(bs, SparqlVariable.ECD_COMPONENT));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setRevisionDate(getStringValue(bs, SparqlVariable.REVISION_DATE));
        data.setCompletionDate(getStringValue(bs, SparqlVariable.COMPLETION_DATE));
        data.setLexicalConcepts(getLexicalConcepts(bs));
        data.setPosition(Integer.parseInt(getLocalName(bs, SparqlVariable.COMPONENT_POSITION).replace("_", "")));
        data.setReferredEntity(getStringValue(bs, SparqlVariable.LEXICAL_ENTITY));
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL)
                + (!getLiteralLanguage(bs, SparqlVariable.LABEL).isEmpty() ? "@" + getLiteralLanguage(bs, SparqlVariable.LABEL) : ""));
    }

    private ArrayList<String> getTypes(BindingSet bs) {
        ArrayList<String> types = new ArrayList();
        for (String _type : getStringValue(bs, SparqlVariable.TYPE).split(";")) {
            types.add(_type.split("#")[1]);
        }
        return types;
    }

    private ArrayList<String> getLexicalConcepts(BindingSet bs) {
        ArrayList<String> lcs = new ArrayList();
        for (String _lc : getStringValue(bs, SparqlVariable.LEXICAL_CONCEPT).split(";")) {
            if (!_lc.isEmpty()) {
                lcs.add(_lc);
            }
        }
        return lcs;
    }

    @Override
    public Class<ECDComponent> getDataClass() {
        return ECDComponent.class;
    }

}
