/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import it.cnr.ilc.lexo.service.data.Data;
import java.util.List;

/**
 *
 * @author andreabellandi
 * @param <D>
 */
public class HitsDataList<D extends Data> implements Data {

    private int totalHits;
    private List<D> list;

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public List<D> getList() {
        return list;
    }

    public void setList(List<D> list) {
        this.list = list;
    }

    public HitsDataList() {
    }

    public HitsDataList(int totalHits, List<D> list) {
        this.totalHits = totalHits;
        this.list = list;
    }

}
