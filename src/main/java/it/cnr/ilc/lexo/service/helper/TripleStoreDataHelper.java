/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import it.cnr.ilc.lexo.util.StringUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 * @param <D>
 */
public abstract class TripleStoreDataHelper<D extends Data> extends Helper<D> {

    private final String MORPHOLOGY_PATTERN = "(([a-zA-Z-]+):(([a-zA-Z-]+\\s?)+));?";
    private final Pattern morphoPattern = Pattern.compile(MORPHOLOGY_PATTERN);

    private final String TYPE_PATTERN = "#([\\w\\-]+)";
    private final Pattern typePattern = Pattern.compile(TYPE_PATTERN);

    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");

    public abstract void fillData(D data, BindingSet bs);

    public List<D> newDataList(TupleQueryResult res) {
        return res.stream().
                map(bs -> newData(bs)).
                collect(Collectors.toList());
    }

    public D newData(TupleQueryResult res) {
        try {// if res.next == null ?????? //java.util.NoSuchElementException
            D data = getDataClass().getDeclaredConstructor().newInstance();
            fillData(data, res.next());
            return data;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
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

    public String getStringValue(BindingSet bs, String variable) {
        return (((bs.getBinding(variable) != null) ? bs.getBinding(variable).getValue().stringValue() : ""));
    }

    public String getLocalName(BindingSet bs, String variable) {
        return (bs.getBinding(variable) != null) ? ((IRI) bs.getBinding(variable).getValue()).getLocalName() : "";
    }

    public String getLiteralLabel(BindingSet bs, String variable) {
        return (bs.getBinding(variable) != null) ? ((Literal) bs.getBinding(variable).getValue()).getLabel() : "";
    }

    public String getLiteralLanguage(BindingSet bs, String variable) {
        return (bs.getBinding(variable) != null) ? ((((Literal) bs.getBinding(variable).getValue()).getLanguage().isPresent())
                ? ((Literal) bs.getBinding(variable).getValue()).getLanguage().get() : "")
                : "";
    }

    public int getIntegerNumber(BindingSet bs, String variable) {
        return (bs.getBinding(variable) != null) ? Integer.parseInt(bs.getBinding(variable).getValue().stringValue()) : 0;
    }

    public double getDoubleNumber(BindingSet bs, String variable) {
        return (bs.getBinding(variable) != null) ? Double.parseDouble(bs.getBinding(variable).getValue().stringValue()) : 0;
    }

    public boolean isInferred(BindingSet bs, String variable) {
        return (bs.getBinding(variable) != null) ? bs.getBinding(variable).getValue().stringValue().contains("implicit") : false;
    }

    public ArrayList<String> getTypes(BindingSet bs, String _types) {
        ArrayList<String> types = new ArrayList();
        if (!_types.isEmpty()) {
//            Matcher matcher = typePattern.matcher(_types);
//            while (matcher.find()) {
//                types.add(matcher.group(1));
//            }
            for (String t : _types.split(";")) {
                types.add(t.split("#")[1].trim());
            }
        }
        return types;
    }

    public ArrayList<Morphology> getMorphology(BindingSet bs, String morpho) {
        ArrayList<Morphology> morphos = new ArrayList();
        if (!morpho.isEmpty()) {
            Matcher matcher = morphoPattern.matcher(morpho);
            while (matcher.find()) {
                morphos.add(new Morphology(matcher.group(2), matcher.group(3)));
            }
        }
        return morphos;
    }

    public ArrayList<String> getLinkTargets(String t) {
        ArrayList<String> targets = new ArrayList();
        for (String _t : t.split(";")) {
            targets.add(_t);
        }
        return targets;
    }

    public ArrayList<Morphology> getMorphologyWithPoS(BindingSet bs, String morpho, String pos) {
        ArrayList<Morphology> morphos = new ArrayList();
        if (!pos.isEmpty()) {
            morphos.add(new Morphology("partOfSpeech", pos));
        }
        if (!morpho.isEmpty()) {
            Matcher matcher = morphoPattern.matcher(morpho);
            while (matcher.find()) {
                morphos.add(new Morphology(matcher.group(2), matcher.group(3)));
            }
        }
        return morphos;
    }

    public boolean isExternalUri(String uri) {
        if (StringUtil.validateURL(uri)) {
            if (!uri.contains(namespace)) {
                return true;
            }
        }
        return false;
    }

}
