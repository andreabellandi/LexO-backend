/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.BibliographicItem;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class BibliographyHelper extends TripleStoreDataHelper<BibliographicItem> {

    @Override
    public void fillData(BibliographicItem data, BindingSet bs) {
        data.setAuthor(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_AUTHOR));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_CREATOR));
        data.setDate(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_DATE));
        data.setId(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_ID));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setNote(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_NOTE));
        data.setSeeAlsoLink(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_SEE_ALSO_LINK));
        data.setTextualReference(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_TEXTUAL_REF));
        data.setTitle(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_TITLE));
        data.setUrl(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_URL));
        data.setBibliography(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY));
//        data.setBibliographyInstanceName(getLocalName(bs, SparqlVariable.BIBLIOGRAPHY));
    }

    @Override
    public Class<BibliographicItem> getDataClass() {
        return BibliographicItem.class;
    }

}
