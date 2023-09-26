/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.BibliographicItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.Bibliography;
import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class BibliographyListHelper extends TripleStoreDataHelper<Bibliography> {

    @Override
    public void fillData(Bibliography data, BindingSet bs) {
        data.setAuthor(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_AUTHOR));
        data.setDate(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_DATE));
        data.setKey(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_ID));
        data.setReferences(getIntegerNumber(bs, SparqlVariable.LABEL_COUNT));
        data.setTitle(getStringValue(bs, SparqlVariable.BIBLIOGRAPHY_TITLE));
        
    }

    @Override
    public Class<Bibliography> getDataClass() {
        return Bibliography.class;
    }
    
}
