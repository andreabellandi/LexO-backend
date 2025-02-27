/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDEntryMorphology;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDLexicalFunction;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class ECDLexicalFunctionHelper extends TripleStoreDataHelper<ECDLexicalFunction> {

    @Override
    public void fillData(ECDLexicalFunction data, BindingSet bs) {
        data.setPos(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY_POS));
        data.setType(getStringValue(bs, SparqlVariable.TYPE));
        data.setDefinition(getStringValue(bs, SparqlVariable.DEFINITION));
        data.setDictionaryEntryLabel(getStringValue(bs, "de" + SparqlVariable.LABEL)
                + (!getLiteralLanguage(bs, "de" + SparqlVariable.LABEL).isEmpty() ? "@" + getLiteralLanguage(bs, "de" + SparqlVariable.LABEL) : ""));
        data.setFusedElement(true);
        data.setGovPattern(getStringValue(bs, SparqlVariable.GOV_PATTERN));
        data.setId(getStringValue(bs, SparqlVariable.LF_IRI));
        data.setLexicalEntryLabel(getStringValue(bs, "le" + SparqlVariable.LABEL)
                + (!getLiteralLanguage(bs, "le" + SparqlVariable.LABEL).isEmpty() ? "@" + getLiteralLanguage(bs, "le" + SparqlVariable.LABEL) : ""));
        data.setLexicalFunction(getStringValue(bs, SparqlVariable.LF));
        data.setSenseTarget(getStringValue(bs, SparqlVariable.TARGET));
    }

    @Override
    public Class<ECDLexicalFunction> getDataClass() {
        return ECDLexicalFunction.class;
    }

}
