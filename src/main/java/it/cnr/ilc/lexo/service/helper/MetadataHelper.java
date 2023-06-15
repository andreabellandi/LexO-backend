/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Metadata;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class MetadataHelper extends TripleStoreDataHelper<Metadata> {

    @Override
    public void fillData(Metadata data, BindingSet bs) {
       data.setAuthor(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_COMPLETING_AUTHOR));
       data.setCompletionDate(getStringValue(bs, SparqlVariable.COMPLETION_DATE));
       data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
       data.setCreator(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_CREATION_AUTHOR));
       data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
       data.setLexicalEntity(getStringValue(bs, SparqlVariable.LEXICAL_ENTITY));
       data.setNote(getStringValue(bs, SparqlVariable.NOTE));
       data.setReviewer(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_REVISOR));
       data.setRevisionDate(getStringValue(bs, SparqlVariable.REVISION_DATE));
       data.setStatus(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_STATUS));
       data.setProvenance(getStringValue(bs, SparqlVariable.SOURCE));
       data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
    }

    @Override
    public Class<Metadata> getDataClass() {
        return Metadata.class;
    }


}
