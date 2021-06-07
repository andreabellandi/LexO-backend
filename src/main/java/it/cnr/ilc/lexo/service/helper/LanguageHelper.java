/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Language;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LanguageHelper extends TripleStoreDataHelper<Language> {

    @Override
    public void fillData(Language data, BindingSet bs) {
        data.setLanguage(getStringValue(bs, SparqlVariable.LEXICON));
        data.setLanguageInstanceName(getLocalName(bs, SparqlVariable.LEXICON));
        data.setCreator(getStringValue(bs, SparqlVariable.LEXICON_LANGUAGE_CREATOR));
        data.setLabel(getLiteralLabel(bs, SparqlVariable.LEXICON_LANGUAGE_LABEL));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCatalog(getStringValue(bs, SparqlVariable.LEXICON_LANGUAGE_CATALOG));
        data.setDescription(getStringValue(bs, SparqlVariable.LEXICON_LANGUAGE_DESCRIPTION));
        data.setLexvo(getStringValue(bs, SparqlVariable.LEXICON_LANGUAGE_DESCRIPTION));
        data.setEntries(getIntegerNumber(bs, SparqlVariable.LABEL_COUNT));
    }

    @Override
    public Class<Language> getDataClass() {
        return Language.class;
    }


}
