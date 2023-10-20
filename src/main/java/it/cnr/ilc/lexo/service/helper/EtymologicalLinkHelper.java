/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.EtymologicalLink;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class EtymologicalLinkHelper extends TripleStoreDataHelper<EtymologicalLink> {

    @Override
    public void fillData(EtymologicalLink data, BindingSet bs) {
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setCreator(getStringValue(bs, SparqlVariable.ETYMOLOGY_CREATION_AUTHOR));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setNote(getStringValue(bs, SparqlVariable.NOTE));
        data.setEtymologicalLink(getStringValue(bs, SparqlVariable.ETY_LINK));
//        data.setEtymologicalLinkInstanceName(getLocalName(bs, SparqlVariable.ETY_LINK));
        data.setEtyLinkType(getStringValue(bs, SparqlVariable.ETY_LINK_TYPE));
        data.setEtySource(getStringValue(bs, SparqlVariable.ETY_SOURCE));
//        data.setEtySourceInstanceName(isExternalUri(getStringValue(bs, SparqlVariable.ETY_SOURCE)) ? "" : getLocalName(bs, SparqlVariable.ETY_SOURCE));
        data.setEtySourceLabel(getLiteralLabel(bs, SparqlVariable.ETY_SOURCE_LABEL) + "@" + getLiteralLanguage(bs, SparqlVariable.ETY_SOURCE_LABEL));
        data.setEtyTarget(getStringValue(bs, SparqlVariable.ETY_TARGET));
//        data.setEtyTargetInstanceName(getLocalName(bs, SparqlVariable.ETY_TARGET));
        data.setEtyTargetLabel(getLiteralLabel(bs, SparqlVariable.ETY_TARGET_LABEL) + "@" + getLiteralLanguage(bs, SparqlVariable.ETY_TARGET_LABEL));
        data.setLabel(getStringValue(bs, SparqlVariable.ETY_LINK_LABEL));
        data.setExternalIRI(isExternalUri(getStringValue(bs, SparqlVariable.ETY_SOURCE)));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
    }

    @Override
    public Class<EtymologicalLink> getDataClass() {
        return EtymologicalLink.class;
    }

}
