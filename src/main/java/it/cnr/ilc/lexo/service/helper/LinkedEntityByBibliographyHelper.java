/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntityByBibliography;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class LinkedEntityByBibliographyHelper extends TripleStoreDataHelper<LinkedEntityByBibliography> {

    @Override
    public void fillData(LinkedEntityByBibliography data, BindingSet bs) {
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_CREATOR));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setLinkType("internal");
        data.setEntityType(getTypes(bs, getStringValue(bs, SparqlVariable.TYPE)));
        data.setInferred(false);
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL));
        data.setEntity(getStringValue(bs, SparqlVariable.LEXICAL_ENTITY));
        data.setLink(SparqlPrefix.DCT.getUri() + "references");
    }

    @Override
    public Class<LinkedEntityByBibliography> getDataClass() {
        return LinkedEntityByBibliography.class;
    }

}
