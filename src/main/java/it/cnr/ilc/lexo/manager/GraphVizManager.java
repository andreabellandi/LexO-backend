/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.graphViz.EdgeGraphFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.graphViz.HopsFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.graphViz.NodeGraphFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.NodeLinks;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.SenseNodeSummary;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.Cytoscape;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.Edge;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.EdgeData;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.Node;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.NodeData;
import it.cnr.ilc.lexo.sparql.SparqlGraphViz;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;
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

    public TupleQueryResult getNodeGraph(String id, NodeGraphFilter ngf, boolean in) {
        String graph = "", query = "";
        if (ngf.getLenght() == null) {
            if (ngf.getGraph() != null) {
                if (!ngf.getGraph().isEmpty()) {
                    if (ngf.getGraph().equals("implicit")) {
                        graph = "FILTER(regex(str(?graph), \"http://www.ontotext.com/implicit\"))\n";
                    } else {
                        graph = "FILTER(regex(str(?graph), \"http://www.ontotext.com/explicit\"))\n";
                    }
                }
            }
            query = SparqlGraphViz.GRAPH_VIZ_NODE_GRAPH.replaceAll("_NODE_ID_", namespace + id).replace("_RELATION_", ngf.getRelation().trim())
                    .replace("_NODE_VARIABLE_", (in ? "?" + SparqlVariable.TARGET : "?" + SparqlVariable.SOURCE))
                    .replace("_GRAPH_", graph);
        } else {
            if (ngf.getLenght() > 0) {
                if (in) {
                    query = SparqlGraphViz.GRAPH_VIZ_NODE_GRAPH_WITH_LENGHT_INCOMING.replaceAll("_NODE_ID_", namespace + id)
                            .replaceAll("_PATH_LENGHT_", String.valueOf(ngf.getLenght()))
                            .replaceAll("_RELATION_", SparqlPrefix.LEXINFO.getPrefix() + ngf.getRelation().trim());
                } else {
                    query = SparqlGraphViz.GRAPH_VIZ_NODE_GRAPH_WITH_LENGHT_OUTGOING.replaceAll("_NODE_ID_", namespace + id)
                            .replaceAll("_PATH_LENGHT_", String.valueOf(ngf.getLenght()))
                            .replaceAll("_RELATION_", SparqlPrefix.LEXINFO.getPrefix() + ngf.getRelation().trim());
                }

            } else {
                return null;
            }
        }
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getEdgeGraph(EdgeGraphFilter egf) {
        String query = SparqlGraphViz.GRAPH_VIZ_EDGE_GRAPH.replaceAll("_RELATION_", egf.getRelation())
                .replace("_SOURCE_", egf.getSource().trim())
                .replace("_TARGET_", egf.getTarget().trim());
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public Cytoscape getNodeGraph(TupleQueryResult tqr, String relation) {
        HashMap<String, String> usem = new HashMap<>();
        Cytoscape cytoscape = new Cytoscape();
        cytoscape.setNodes(new ArrayList<>());
        cytoscape.setEdges(new ArrayList<>());
        addElementToGraph(tqr, cytoscape, usem, relation);
//        addElementToGraph(out, cytoscape, usem, relation);
        return cytoscape;
    }

    private void addElementToGraph(TupleQueryResult tqr, Cytoscape cytoscape, HashMap<String, String> usem, String relation) {
        if (tqr != null) {
            while (tqr.hasNext()) {
                BindingSet bindingSet = tqr.next();
                // add source node

                if (usem.get(((IRI) bindingSet.getBinding("source").getValue()).getLocalName()) == null) {
                    NodeData ds = new NodeData(((IRI) bindingSet.getBinding("source").getValue()).getLocalName(),
                            ((Literal) bindingSet.getBinding("label").getValue()).getLabel(),
                            bindingSet.getBinding("def").getValue().stringValue(),
                            ((IRI) bindingSet.getBinding("source").getValue()).stringValue(),
                            ((IRI) bindingSet.getBinding("pos").getValue()).getLocalName());
                    Node ns = new Node(((IRI) bindingSet.getBinding("source").getValue()).getLocalName(), ds);
                    cytoscape.getNodes().add(ns);
                    usem.put(((IRI) bindingSet.getBinding("source").getValue()).getLocalName(), ((IRI) bindingSet.getBinding("source").getValue()).getLocalName());
                }

                // add target node
                if (usem.get(((IRI) bindingSet.getBinding("target").getValue()).getLocalName()) == null) {
                    NodeData dt = new NodeData(((IRI) bindingSet.getBinding("target").getValue()).getLocalName(),
                            ((Literal) bindingSet.getBinding("labelTarget").getValue()).getLabel(),
                            bindingSet.getBinding("defTarget").getValue().stringValue(),
                            ((IRI) bindingSet.getBinding("target").getValue()).stringValue(),
                            ((IRI) bindingSet.getBinding("posTarget").getValue()).getLocalName());
                    Node nt = new Node(((IRI) bindingSet.getBinding("target").getValue()).getLocalName(), dt);
                    cytoscape.getNodes().add(nt);
                    usem.put(((IRI) bindingSet.getBinding("target").getValue()).getLocalName(), ((IRI) bindingSet.getBinding("target").getValue()).getLocalName());
                }
                EdgeData e = new EdgeData(((IRI) bindingSet.getBinding("source").getValue()).getLocalName()
                        + " " + relation + " "
                        + ((IRI) bindingSet.getBinding("target").getValue()).getLocalName(),
                        ((IRI) bindingSet.getBinding("source").getValue()).getLocalName(),
                        ((IRI) bindingSet.getBinding("target").getValue()).getLocalName(),
                        ((Literal) bindingSet.getBinding("label").getValue()).getLabel(),
                        ((Literal) bindingSet.getBinding("labelTarget").getValue()).getLabel(),
                        (bindingSet.getBinding("graph") != null ? ((IRI) bindingSet.getBinding("graph").getValue()).getLocalName().contains("implicit") : null));
                e.setRelationType(relation);
                Edge edge = new Edge(e);
                cytoscape.getEdges().add(edge);
            }
        }
    }

    public void createNodeSummary(List<NodeLinks> _links, SenseNodeSummary sns) {
        sns.setLinks(_links);
    }

    public TupleQueryResult getHopsByRel(HopsFilter hf, String direction) {
        String query = SparqlGraphViz.GRAPH_VIZ_HOPS_BY_REL.replaceAll("_SOURCE_", hf.getNode()).replaceAll("_RELATION_", hf.getRelation()).
                replaceAll("_DIRECTION_", direction).replaceAll("_DIRECTIONVALUE_", direction.equals("src") ? "outgoing" : "incoming");
        return RDFQueryUtil.evaluateTQuery(query);
    }
}
