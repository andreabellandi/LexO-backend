/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.LexicographicComponent;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LexicographicComponentHelper extends TripleStoreDataHelper<LexicographicComponent> {

    @Override
    public void fillData(LexicographicComponent data, BindingSet bs) {
        data.setCreator(getStringValue(bs, SparqlVariable.CREATOR));
        data.setAuthor(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setPos(getPosList(bs, getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_POS)));
        data.setRevisor(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_REVISOR));
        data.setStatus(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_STATUS));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setType(getTypes(bs));
        data.setComponent(getStringValue(bs, SparqlVariable.LEXICOGRAPHIC_COMPONENT));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setRevisionDate(getStringValue(bs, SparqlVariable.REVISION_DATE));
        data.setCompletionDate(getStringValue(bs, SparqlVariable.COMPLETION_DATE));
//        data.setHasChildren((getIntegerNumber(bs, SparqlVariable.CHILD)) > 0);
        data.setPosition(Integer.parseInt(getLocalName(bs, SparqlVariable.COMPONENT_POSITION).replace("_", "")));
        data.setReferredEntity(getStringValue(bs, SparqlVariable.LEXICAL_ENTITY));
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL) + 
                (!getLiteralLanguage(bs, SparqlVariable.LABEL).isEmpty() ? "@" + getLiteralLanguage(bs, SparqlVariable.LABEL) : ""));
    }
    
    private ArrayList<String> getTypes(BindingSet bs) {
        ArrayList<String> types = new ArrayList();
        for (String _type : getStringValue(bs, SparqlVariable.TYPE).split(";")) {
            types.add(_type.split("#")[1]);
        }
        return types;
    }

    @Override
    public Class<LexicographicComponent> getDataClass() {
        return LexicographicComponent.class;
    }


}
