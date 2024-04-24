/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class DictionaryEntryHelper extends TripleStoreDataHelper<DictionaryEntryCore> {

    @Override
    public void fillData(DictionaryEntryCore data, BindingSet bs) {
        data.setCreator(getStringValue(bs, SparqlVariable.CREATOR));
        data.setDictionaryEntry(getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY));
        data.setAuthor(getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY_COMPLETING_AUTHOR));
        data.setNote(getNotes(bs));
        data.setPos(getPosList(bs, getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY_POS)));
        data.setRevisor(getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY_REVISOR));
        data.setStatus(getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY_STATUS));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setType(getTypes(bs));
        data.setImages(getImages(getStringValue(bs, SparqlVariable.IMAGE)));
        data.setHasChildren((getIntegerNumber(bs, SparqlVariable.CHILD)) > 0);
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setRevisionDate(getStringValue(bs, SparqlVariable.REVISION_DATE));
        data.setCompletionDate(getStringValue(bs, SparqlVariable.COMPLETION_DATE));
        data.setHasChildren((getIntegerNumber(bs, SparqlVariable.CHILD)) > 0);
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL)
                + (getLiteralLanguage(bs, SparqlVariable.LABEL) != null ? "@" + getLiteralLanguage(bs, SparqlVariable.LABEL) : ""));
        try {
            data.setLanguage(getLiteralLanguage(bs, SparqlVariable.LABEL));
        } catch (Exception ex) {
            Logger.getLogger(DictionaryEntryFilterHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        data.setSeeAlso(getLinkedEntities(bs, getStringValue(bs, SparqlVariable.SEEALSO)));
        data.setSameDictionaryEntryAs(getLinkedEntities(bs, getStringValue(bs, SparqlVariable.SAMEAS)));
    }
    
    private ArrayList<String> getTypes(BindingSet bs) {
        ArrayList<String> types = new ArrayList();
        for (String _type : getStringValue(bs, SparqlVariable.TYPE).split(";")) {
            types.add(_type.split("#")[1]);
        }
        return types;
    }

    private ArrayList<String> getNotes(BindingSet bs) {
        ArrayList<String> notes = new ArrayList();
        for (String _n : getStringValue(bs, SparqlVariable.NOTE).split("_NOTE_SEPARATOR_")) {
            notes.add(_n);
        }
        return notes;
    }

    @Override
    public Class<DictionaryEntryCore> getDataClass() {
        return DictionaryEntryCore.class;
    }

}
