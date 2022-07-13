/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.graphViz;

import it.cnr.ilc.lexo.service.data.Data;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
public class Hop implements Data {

    private int lenght;
    private List<String> hops;

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public List<String> getHops() {
        return hops;
    }

    public void setHops(List<String> hops) {
        this.hops = hops;
    }

}
