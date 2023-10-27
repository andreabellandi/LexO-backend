/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Dictionary;
import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryComponent;
import it.cnr.ilc.lexo.service.data.lexicon.output.Language;
import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.sparql.Namespace;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlQueryUtil;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import java.util.ArrayList;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class DictionaryEntryComponentHelper extends TripleStoreDataHelper<DictionaryEntryComponent> {

    @Override
    public void fillData(DictionaryEntryComponent data, BindingSet bs) {
        data.setCreator(getStringValue(bs, SparqlVariable.LEXICON_LANGUAGE_CREATOR));
        data.setLabel(getLiteralLabel(bs, SparqlVariable.LEXICON_LANGUAGE_LABEL));
        data.setLastUpdate(getStringValue(bs, SparqlVariable.LAST_UPDATE));
        data.setCreationDate(getStringValue(bs, SparqlVariable.CREATION_DATE));
        data.setConfidence(getDoubleNumber(bs, SparqlVariable.CONFIDENCE));
        data.setLabel(getStringValue(bs, SparqlVariable.LABEL));
        data.setOrderedMemebers(getList(getStringValue(bs, SparqlVariable.ORDERED_MEMBER)));
        data.setUnorderedMembers(getList(getStringValue(bs, SparqlVariable.UNORDERED_MEMBER)));
        data.setDescribes(getDescribes(bs));
    }
    
    private LinkedEntity getDescribes(BindingSet bs) {
        LinkedEntity le = new LinkedEntity();
        le.setEntity(getStringValue(bs, SparqlVariable.DESCRIBE));
        le.setLabel(getStringValue(bs, SparqlVariable.DESCRIBE_LABEL));
        ArrayList<String> al = new ArrayList();
        al.add(getStringValue(bs, SparqlVariable.TYPE));
        le.setEntityType(al);
        le.setInferred(false);
        le.setLinkType("internal");
        le.setLink(SparqlPrefix.LEXICOG.getUri() + "describes");
        return le;
    }
    
    private ArrayList<String> getList(String s) {
        ArrayList<String> al = new ArrayList();
        for (String _s :s.split(";")) {
            al.add(_s);
        }
        return al;
    }

    @Override
    public Class<DictionaryEntryComponent> getDataClass() {
        return DictionaryEntryComponent.class;
    }


}
