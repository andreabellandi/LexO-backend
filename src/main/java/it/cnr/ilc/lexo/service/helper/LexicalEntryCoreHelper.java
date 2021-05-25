/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

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
public class LexicalEntryCoreHelper extends TripleStoreDataHelper<LexicalEntryCore> {

    @Override
    public void fillData(LexicalEntryCore data, BindingSet bs) {
        data.setLexicalEntry(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setLexicalEntryInstanceName(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setCreator(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR));
        data.setAuthor(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR));
        data.setConcept(getStringValue(bs, SparqlVariable.CONCEPT));
//        data.setConceptInstanceName(getLocalName(bs, SparqlVariable.CONCEPT));
        data.setLabel(getLiteralLabel(bs, SparqlVariable.LABEL));
        data.setLanguage(getLiteralLanguage(bs, SparqlVariable.LABEL));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setPos(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_POS));
        data.setRevisor(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_REVISOR));
        data.setStatus(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_STATUS));
        data.setType(getLocalName(bs, SparqlVariable.LEXICAL_ENTRY_TYPE));
        data.setMorphology(getMorphology(bs, getStringValue(bs, SparqlVariable.MORPHOLOGY)));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setRevisionDate(getStringValue(bs, SparqlVariable.REVISION_DATE));
        data.setCompletionDate(getStringValue(bs, SparqlVariable.COMPLETION_DATE));
    }

    @Override
    public Class<LexicalEntryCore> getDataClass() {
        return LexicalEntryCore.class;
    }

}
