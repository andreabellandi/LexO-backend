/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDEntryItem;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ECDEntryFilterHelper extends TripleStoreDataHelper<ECDEntryItem> {

    @Override
    public void fillData(ECDEntryItem data, BindingSet bs) {
        setTotalHits(getIntegerNumber(bs, SparqlVariable.TOTAL_HITS));
        data.setDictionaryEntry(getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY));
        data.setPos(getPosList(bs, getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY_POS)));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setLabel(getLiteralLabel(bs, SparqlVariable.LABEL));
        try {
            data.setLanguage(getLiteralLanguage(bs, SparqlVariable.LABEL));
        } catch (Exception ex) {
            Logger.getLogger(ECDEntryFilterHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        data.setSeeAlso(getReferencesList(getStringValue(bs, SparqlVariable.SEEALSO)));
        data.setType(getTypes(bs, getStringValue(bs, SparqlVariable.TYPE)));
        data.setHasChildren((getIntegerNumber(bs, SparqlVariable.CHILD)) > 0);
        data.setStatus(getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY_STATUS));
        data.setRevisor(getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY_REVISOR));
        data.setCreator(getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY_CREATION_AUTHOR));
        data.setCompletionDate(getStringValue(bs, SparqlVariable.COMPLETION_DATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setRevisionDate(getStringValue(bs, SparqlVariable.REVISION_DATE));
        data.setAuthor(getStringValue(bs, SparqlVariable.DICTIONARY_ENTRY_COMPLETING_AUTHOR));
    }

    @Override
    public Class<ECDEntryItem> getDataClass() {
        return ECDEntryItem.class;
    }

    private Map<String, String> getReferencesList(String refList) {
        Map<String, String> sameAsList = new HashMap();
        if (!refList.isEmpty()) {
            for (String sa : refList.split(";")) {
                String _sa[] = sa.split("---");
                sameAsList.put(_sa[0], _sa[1]);
            }
        }
        return sameAsList;
    }

}
