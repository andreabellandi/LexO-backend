/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.graphViz;

import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
public class Edge implements Data {

    private EdgeData data;

    public EdgeData getData() {
        return data;
    }

    public void setData(EdgeData data) {
        this.data = data;
    }

    public Edge() {
    }

    public Edge(EdgeData data) {
        this.data = data;
    }
}
