/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.pippo;

import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author andreabellandi
 */
public class Cytoscape implements Data {

    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public Cytoscape() {
    }

    public Cytoscape(ArrayList<Node> nodes, ArrayList<Edge> edges) {
        this.edges = edges;
        this.nodes = nodes;
    }

}
