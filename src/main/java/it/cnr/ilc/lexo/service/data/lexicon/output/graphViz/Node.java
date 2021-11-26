/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.graphViz;

import it.cnr.ilc.lexo.service.data.Data;
import java.util.Map;

/**
 *
 * @author andreabellandi
 */
public class Node implements Data {

    private String classes;
    private NodeData data;

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public NodeData getData() {
        return data;
    }

    public void setData(NodeData data) {
        this.data = data;
    }

    public Node() {
    }

    public Node(String classes, NodeData data) {
        this.data = data;
        this.classes = classes;
    }
}
