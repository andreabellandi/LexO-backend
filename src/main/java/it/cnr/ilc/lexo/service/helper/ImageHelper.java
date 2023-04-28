/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ImageDetail;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ImageHelper extends TripleStoreDataHelper<ImageDetail> {

    @Override
    public void fillData(ImageDetail data, BindingSet bs) {
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.IMAGE_CREATOR));
        data.setDescription(getStringValue(bs, SparqlVariable.DESCRIPTION));
        data.setFormat(getStringValue(bs, SparqlVariable.FORMAT));
        data.setId(getStringValue(bs, SparqlVariable.IMAGE));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setLexicalEntityIRI(getStringValue(bs, SparqlVariable.LEXICAL_ENTITY));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setPublisher(getStringValue(bs, SparqlVariable.PUBLISHER));
        data.setRights(getStringValue(bs, SparqlVariable.RIGHTS));
        data.setSource(getStringValue(bs, SparqlVariable.SOURCE));
        data.setTitle(getStringValue(bs, SparqlVariable.TITLE));
        data.setType(getStringValue(bs, SparqlVariable.TYPE));
        data.setUrl(getStringValue(bs, SparqlVariable.IDENTIFIER));
    }

    @Override
    public Class<ImageDetail> getDataClass() {
        return ImageDetail.class;
    }

}
