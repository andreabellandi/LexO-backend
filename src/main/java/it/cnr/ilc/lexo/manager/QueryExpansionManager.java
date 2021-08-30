/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.ConceptList;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormBySenseFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryList;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalSenseFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryElementItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import it.cnr.ilc.lexo.sparql.SparqlQueryExpansion;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.EnumUtil.AcceptedSearchFormExtendTo;
import it.cnr.ilc.lexo.util.EnumUtil.AcceptedSearchFormExtensionDegree;
import it.cnr.ilc.lexo.util.EnumUtil.FormTypes;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalEntryStatus;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalEntryTypes;
import it.cnr.ilc.lexo.util.EnumUtil.LexicalSenseSearchFilter;
import it.cnr.ilc.lexo.util.EnumUtil.SearchFormTypes;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class QueryExpansionManager implements Manager, Cached {

    static final Logger logger = LoggerFactory.getLogger(QueryExpansionManager.class.getName());

    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");
    private final String ontologyNamespace = LexOProperties.getProperty("repository.ontology.namespace");

    public String getNamespace() {
        return namespace;
    }

    @Override
    public void reloadCache() {

    }

    public TupleQueryResult getReferencedLinguisticObject(ConceptList cl) throws ManagerException {
        String conceptsList = "";
        if (cl.getConceptList() == null) {
            throw new ManagerException("concepts list cannot be empty");
        } else {
            if (cl.getConceptList().isEmpty()) {
                throw new ManagerException("concepts list cannot be empty");
            } else {
                for (String c : cl.getConceptList()) {
                    conceptsList = conceptsList + "\\\"" + ontologyNamespace + c + "\\\"" + " OR ";
                }
            }
        }
        String query = SparqlQueryExpansion.QUERY_EXPANSION_REFERENCED_LINGUISTIC_OBJECT.
                replace("[CONCEPTS_LIST]", conceptsList.substring(0, conceptsList.length() - 3));
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getForms(LexicalEntryList lel) throws ManagerException {
        String lexicalEntryList = "";
        if (lel.getLexicalEntryList() == null) {
            throw new ManagerException("lexical entries list cannot be empty");
        } else {
            if (lel.getLexicalEntryList().isEmpty()) {
                throw new ManagerException("lexical entries list cannot be empty");
            } else {
                for (String le : lel.getLexicalEntryList()) {
                    lexicalEntryList = lexicalEntryList + "\\\"" + namespace + le + "\\\"" + " OR ";
                }
            }
        }
        String query = SparqlQueryExpansion.QUERY_EXPANSION_FORMS.
                replace("[LEXICAL_ENTRY_LIST]", lexicalEntryList.substring(0, lexicalEntryList.length() - 3));
        return RDFQueryUtil.evaluateTQuery(query);
    }

}
