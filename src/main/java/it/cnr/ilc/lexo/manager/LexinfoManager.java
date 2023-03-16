/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.service.data.vocabulary.MorphologicalProperty;
import it.cnr.ilc.lexo.service.data.vocabulary.RepresentationProperty;
import it.cnr.ilc.lexo.service.data.vocabulary.SenseProperty;
import it.cnr.ilc.lexo.service.data.vocabulary.Value;
import it.cnr.ilc.lexo.sparql.SparqlSelectLexinfoData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 */
public final class LexinfoManager implements Manager, Cached {

    private final String MORPHOLOGY_PATTERN = "(([a-zA-Z-]+):(([a-zA-Z-]+\\s?)+)<>([^;]+)?);?";
    private final Pattern pattern = Pattern.compile(MORPHOLOGY_PATTERN);
    private final List<MorphologicalProperty> morpho = new ArrayList<>();
    private final List<Value> representation = new ArrayList<>();
    private final List<Value> senseDefinition = new ArrayList<>();
    private final Map<String, MorphologicalProperty> morphoHash = new HashMap();

    public List<MorphologicalProperty> getMorpho() {
        return morpho;
    }

    public Map<String, MorphologicalProperty> getMorphoHash() {
        return morphoHash;
    }

    public List<Value> getSenseProperties() {
        return senseDefinition;
    }

    public List<Value> getRepresentationProperties() {
        return representation;
    }

    LexinfoManager() {
        reloadCache();
        reloadRepresentationCache();
        reloadSensedefinitionCache();
    }

    @Override
    public void reloadCache() {
        reloadMorphoCache();
        reloadRepresentationCache();
        reloadSensedefinitionCache();
    }

    private void reloadMorphoCache() {
        morpho.clear();
        morphoHash.clear();
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectLexinfoData.MORPHOLOGY);
//        try (TupleQueryResult result = tupleQuery.evaluate()) {
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectLexinfoData.MORPHOLOGY)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                MorphologicalProperty mp = new MorphologicalProperty();
                mp.setPropertyId(((bs.getBinding(SparqlVariable.MORPHOLOGY_TRAIT_NAME) != null)
                        ? bs.getBinding(SparqlVariable.MORPHOLOGY_TRAIT_NAME).getValue().stringValue() : ""));
                mp.setPropertyLabel((bs.getBinding(SparqlVariable.LABEL) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                mp.setPropertyDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                List<MorphologicalProperty.MorphologicalValue> values = new ArrayList();
//                Matcher matcher = pattern.matcher(bs.getBinding(SparqlVariable.MORPHOLOGY_TRAIT_VALUE).getValue().stringValue());
//                while (matcher.find()) {
//                    values.add(new MorphologicalProperty.MorphologicalValue(matcher.group(2), matcher.group(3), matcher.group(5)));
//                }
                for (String s : bs.getBinding(SparqlVariable.MORPHOLOGY_TRAIT_VALUE).getValue().stringValue().split("---")) {
                    String[] _s = s.split("<>");
                    values.add(new MorphologicalProperty.MorphologicalValue(_s[0], _s[1], _s[2]));
                }
                mp.setPropertyValues(values);
                morpho.add(mp);
                morphoHash.put(mp.getPropertyId(), mp);
            }
        } catch (QueryEvaluationException qee) {
        }
    }

    private void reloadRepresentationCache() {
        representation.clear();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectLexinfoData.REPRESENTATION)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                Value v = new Value();
                v.setValueId(((bs.getBinding(SparqlVariable.PROPERTY_NAME) != null)
                        ? bs.getBinding(SparqlVariable.PROPERTY_NAME).getValue().stringValue() : ""));
                v.setValueLabel((bs.getBinding(SparqlVariable.LABEL) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                v.setValueDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                representation.add(v);
            }
        } catch (QueryEvaluationException qee) {
        }
    }

    private void reloadSensedefinitionCache() {
        senseDefinition.clear();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectLexinfoData.SENSE_DEFINITION)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                Value v = new Value();
                v.setValueId(((bs.getBinding(SparqlVariable.PROPERTY_NAME) != null)
                        ? bs.getBinding(SparqlVariable.PROPERTY_NAME).getValue().stringValue() : ""));
                v.setValueLabel((bs.getBinding(SparqlVariable.LABEL) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                v.setValueDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                senseDefinition.add(v);
            }
        } catch (QueryEvaluationException qee) {
        }
    }
}
