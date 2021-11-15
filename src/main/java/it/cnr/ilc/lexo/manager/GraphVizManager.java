/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.ConceptList;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormBySenseFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryList;
import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphviz.NodeLinks;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphviz.SenseNodeSummary;
import it.cnr.ilc.lexo.service.data.lexicon.output.queryExpansion.Form;
import it.cnr.ilc.lexo.sparql.SparqlGraphViz;
import it.cnr.ilc.lexo.sparql.SparqlQueryExpansion;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class GraphVizManager implements Manager, Cached {

    static final Logger logger = LoggerFactory.getLogger(GraphVizManager.class.getName());

    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");
    private final String ontologyNamespace = LexOProperties.getProperty("repository.ontology.namespace");

    public String getNamespace() {
        return namespace;
    }

    @Override
    public void reloadCache() {

    }

    public TupleQueryResult getNode(String id) {
        String query = SparqlGraphViz.GRAPH_VIZ_SENSE_SUMMARY.replace("[IRI]", "\\\"" + namespace + id + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLinks(String id) {
        String query = SparqlGraphViz.GRAPH_VIZ_SENSE_LINKS.replace("[IRI]", "\\\"" + namespace + id + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public void createNodeSummary(NodeLinks _links, SenseNodeSummary sns) {
        sns.setHypernymNr(_links.getHypernymNr());
        sns.setHyponymNr(_links.getHyponymNr());
        sns.setHolonymNr(_links.getHolonymNr());
        sns.setMeronymNr(_links.getMeronymNr());
        sns.setIncomingSynonymNr(_links.getIncomingSynonymNr());
        sns.setOutcomingSynonymNr(_links.getOutcomingSynonymNr());
    }
}
