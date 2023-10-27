/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Dictionary;
import it.cnr.ilc.lexo.service.data.lexicon.output.Language;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class DictionaryHelper extends TripleStoreDataHelper<Dictionary> {

    @Override
    public void fillData(Dictionary data, BindingSet bs) {
        data.setLanguage(getStringValue(bs, SparqlVariable.LEXICON));
        data.setCreator(getStringValue(bs, SparqlVariable.LEXICON_LANGUAGE_CREATOR));
        data.setLabel(getLiteralLabel(bs, SparqlVariable.LEXICON_LANGUAGE_LABEL));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setDescription(getStringValue(bs, SparqlVariable.LEXICON_LANGUAGE_DESCRIPTION));
        data.setEntries(getIntegerNumber(bs, SparqlVariable.LABEL_COUNT));
    }

    @Override
    public Class<Dictionary> getDataClass() {
        return Dictionary.class;
    }


}
