/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.Data;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 * @param <D>
 */
public abstract class TripleStoreDataHelper<D extends Data> extends Helper<D> {

    public abstract void fillData(D data, BindingSet bs);

    public List<D> newDataList(TupleQueryResult res) {
        return res.stream().
                map(bs -> newData(bs)).
                collect(Collectors.toList());
    }

    public D newData(BindingSet bs) {
        try {
            D data = getDataClass().getDeclaredConstructor().newInstance();
            fillData(data, bs);
            return data;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

}
