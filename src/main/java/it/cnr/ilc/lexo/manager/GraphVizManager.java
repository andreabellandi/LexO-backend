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

    @Override
    public void reloadCache() {

    }

    public TupleQueryResult getNode(String id) {
        String query = SparqlGraphViz.GRAPH_VIZ_SENSE_SUMMARY.replace("[IRI]", "\\\"" + id + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getLinks(String id) {
        String query = SparqlGraphViz.GRAPH_VIZ_SENSE_LINKS.replace("[IRI]", "\\\"" + id + "\\\"");
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getNodeGraph(String id, NodeGraphFilter ngf, String direction) {
        String query = SparqlGraphViz.GRAPH_VIZ_NODE_GRAPH.replaceAll("_NODE_ID_", id)
                .replaceAll("_PATH_LENGHT_", String.valueOf(ngf.getLenght()))
                .replaceAll("_RELATION_", ngf.getRelation().trim())
                .replaceAll("_DIRECTION_", direction)
                .replaceAll("_LENGHT_", String.valueOf(ngf.getLenght()));
        return RDFQueryUtil.evaluateTQuery(query);
    }



    public TupleQueryResult getEdgeGraph(EdgeGraphFilter egf) {
        String query = SparqlGraphViz.GRAPH_VIZ_EDGE_GRAPH.replaceAll("_RELATION_", egf.getRelation())
                .replace("_SOURCE_", egf.getSource().trim())
                .replace("_TARGET_", egf.getTarget().trim());
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public ArrayList<Cytoscape> splitPaths(TupleQueryResult tqr, boolean inference) {
        ArrayList<Cytoscape> cl = new ArrayList<>();
        if (tqr != null) {
            int pathIndex = 0;
            ArrayList<BindingSet> bs = new ArrayList<>();
            while (tqr.hasNext()) {
                BindingSet bindingSet = tqr.next();
                if ((Integer.parseInt(bindingSet.getBinding(SparqlVariable.PATH_INDEX).getValue().stringValue())) == pathIndex) {
                    bs.add(bindingSet);
                } else {
                    cl.add(getNodeGraph(bs, inference));
                    bs.clear();
                    bs.add(bindingSet);
                    pathIndex++;
                }
            }
            cl.add(getNodeGraph(bs, inference));
            return cl;
        }
        return null;
    }

    private Cytoscape getNodeGraph(ArrayList<BindingSet> bs, boolean inference) {
        HashMap<String, String> usem = new HashMap<>();
        Cytoscape cytoscape = new Cytoscape();
        cytoscape.setNodes(new ArrayList<>());
        cytoscape.setEdges(new ArrayList<>());
        // IMPORTANT: we need to check if the bindings return null. In this case we assume that all the bindings are not null!!!
        for (BindingSet _bs : bs) {
            String sourceLabel = (_bs.getBinding("label") == null ? (inference ? "conceptLabel" : "label") : "label");
            if (usem.get(((IRI) _bs.getBinding("source").getValue()).getLocalName()) == null) {
                NodeData ds = new NodeData(((IRI) _bs.getBinding("source").getValue()).getLocalName(),
                        ((Literal) _bs.getBinding(sourceLabel).getValue()).getLabel(),
                        ((_bs.getBinding("def") == null) ? "" : _bs.getBinding("def").getValue().stringValue()),
                        ((IRI) _bs.getBinding("source").getValue()).stringValue(),
                        ((_bs.getBinding("pos") == null) ? "" : ((IRI) _bs.getBinding("pos").getValue()).getLocalName()));
                Node ns = new Node(((IRI) _bs.getBinding("source").getValue()).getLocalName(), ds);
                cytoscape.getNodes().add(ns);
                usem.put(((IRI) _bs.getBinding("source").getValue()).getLocalName(), ((IRI) _bs.getBinding("source").getValue()).getLocalName());
            }

            // add target node
            String targetLabel = (_bs.getBinding("labelTarget") == null ? (inference ? "conceptLabelTarget" : "labelTarget") : "labelTarget");
            if (usem.get(((IRI) _bs.getBinding("target").getValue()).getLocalName()) == null) {
                NodeData dt = new NodeData(((IRI) _bs.getBinding("target").getValue()).getLocalName(),
                        ((Literal) _bs.getBinding(targetLabel).getValue()).getLabel(),
                        ((_bs.getBinding("defTarget") == null) ? "" : _bs.getBinding("defTarget").getValue().stringValue()),
                        ((IRI) _bs.getBinding("target").getValue()).stringValue(),
                        ((_bs.getBinding("posTarget") == null) ? "" : ((IRI) _bs.getBinding("posTarget").getValue()).getLocalName()));
                Node nt = new Node(((IRI) _bs.getBinding("target").getValue()).getLocalName(), dt);
                cytoscape.getNodes().add(nt);
                usem.put(((IRI) _bs.getBinding("target").getValue()).getLocalName(), ((IRI) _bs.getBinding("target").getValue()).getLocalName());
            }
            EdgeData e = new EdgeData(((IRI) _bs.getBinding("source").getValue()).getLocalName()
                    + " " + _bs.getBinding("relation").getValue().stringValue() + " "
                    + ((IRI) _bs.getBinding("target").getValue()).getLocalName(),
                    ((IRI) _bs.getBinding("source").getValue()).getLocalName(),
                    ((IRI) _bs.getBinding("target").getValue()).getLocalName(),
                    ((Literal) _bs.getBinding(sourceLabel).getValue()).getLabel(),
                    ((Literal) _bs.getBinding(targetLabel).getValue()).getLabel(),
                    (_bs.getBinding("graph") != null ? ((IRI) _bs.getBinding("graph").getValue()).getLocalName().contains("implicit") : null));
            e.setRelationType(_bs.getBinding("relation").getValue().stringValue());
            Edge edge = new Edge(e);
            cytoscape.getEdges().add(edge);
        }
        return cytoscape;
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

    public TupleQueryResult getMinPath(String source, String target, Boolean inference) throws ManagerException {
        String query = "";
        if (inference != null) {
            query = inference ? SparqlGraphViz.GRAPH_VIZ_MIN_PATH_WITH_CONCEPTS.replaceAll("_SRC_NODE_", source).replaceAll("_DST_NODE_", target)
                    .replaceAll("_REPO_", "repository:SIMPLE_ONTO"):
               SparqlGraphViz.GRAPH_VIZ_MIN_PATH.replaceAll("_SRC_NODE_", source).replaceAll("_DST_NODE_", target);     
        } else {
            query = SparqlGraphViz.GRAPH_VIZ_MIN_PATH.replaceAll("_SRC_NODE_", source).replaceAll("_DST_NODE_", target);   
        }
        return RDFQueryUtil.evaluateTQuery(query);
    }
}
