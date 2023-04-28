/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.service.data.vocabulary.RangedProperty;
import it.cnr.ilc.lexo.service.data.vocabulary.PropertyHierarchy;
import it.cnr.ilc.lexo.service.data.vocabulary.Value;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlSelectLexinfoData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final List<RangedProperty> morpho = new ArrayList<>();
    private final List<RangedProperty> usage = new ArrayList<>();
    private final List<RangedProperty> lexicalCategory = new ArrayList<>();
    private final List<RangedProperty> semanticCategory = new ArrayList<>();
    private final List<RangedProperty> formCategory = new ArrayList<>();
    private final List<Value> representation = new ArrayList<>();
    private final List<PropertyHierarchy> lexicalRel = new ArrayList<>();
    private final List<PropertyHierarchy> senseRel = new ArrayList<>();
    private final List<PropertyHierarchy> formRel = new ArrayList<>();
    private final List<Value> senseDefinition = new ArrayList<>();
    private final Map<String, RangedProperty> morphoHash = new HashMap();
    private final Map<String, RangedProperty> usageHash = new HashMap();
    private final Map<String, RangedProperty> lexicalCategoryHash = new HashMap();
    private final Map<String, RangedProperty> semanticCategoryHash = new HashMap();
    private final Map<String, RangedProperty> formCategoryHash = new HashMap();

    public List<RangedProperty> getMorpho() {
        return morpho;
    }

    public List<PropertyHierarchy> getFormRel() {
        return formRel;
    }

    public List<RangedProperty> getFormCategory() {
        return formCategory;
    }

    public Map<String, RangedProperty> getFormCategoryHash() {
        return formCategoryHash;
    }

    public Map<String, RangedProperty> getMorphoHash() {
        return morphoHash;
    }

    public Map<String, RangedProperty> getUsageHash() {
        return usageHash;
    }

    public List<Value> getSenseProperties() {
        return senseDefinition;
    }

    public List<Value> getRepresentationProperties() {
        return representation;
    }

    public List<RangedProperty> getLexicalCategory() {
        return lexicalCategory;
    }

    public Map<String, RangedProperty> getLexicalCategoryHash() {
        return lexicalCategoryHash;
    }

    public List<PropertyHierarchy> getLexicalRel() {
        return lexicalRel;
    }

    public List<PropertyHierarchy> getSenseRel() {
        return senseRel;
    }

    public List<RangedProperty> getSemanticCategory() {
        return semanticCategory;
    }

    public Map<String, RangedProperty> getSemanticCategoryHash() {
        return semanticCategoryHash;
    }

    public List<RangedProperty> getUsage() {
        return usage;
    }

    LexinfoManager() {
        reloadCache();
        reloadRepresentationCache();
        reloadSensedefinitionCache();
        reloadLexicalRelCache();
        reloadSenseRelCache();
        reloadFormRelCache();
        reloadUsageCache();
        reloadLexicalCategoryCache();
        reloadSemanticCategoryCache();
        reloadFormCategoryCache();
    }

    @Override
    public void reloadCache() {
        reloadMorphoCache();
        reloadRepresentationCache();
        reloadSensedefinitionCache();
        reloadLexicalRelCache();
        reloadSenseRelCache();
        reloadFormRelCache();
        reloadUsageCache();
        reloadLexicalCategoryCache();
        reloadSemanticCategoryCache();
        reloadFormCategoryCache();
    }

    private void reloadMorphoCache() {
        morpho.clear();
        morphoHash.clear();
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectLexinfoData.MORPHOLOGY);
//        try (TupleQueryResult result = tupleQuery.evaluate()) {
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectLexinfoData.MORPHOLOGY)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                RangedProperty mp = new RangedProperty();
                mp.setPropertyId(((bs.getBinding(SparqlVariable.MORPHOLOGY_TRAIT_NAME) != null)
                        ? bs.getBinding(SparqlVariable.MORPHOLOGY_TRAIT_NAME).getValue().stringValue() : ""));
                mp.setPropertyLabel((bs.getBinding(SparqlVariable.LABEL) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                mp.setPropertyDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                List<RangedProperty.RangedValue> values = new ArrayList();
//                Matcher matcher = pattern.matcher(bs.getBinding(SparqlVariable.MORPHOLOGY_TRAIT_VALUE).getValue().stringValue());
//                while (matcher.find()) {
//                    values.add(new MorphologicalProperty.MorphologicalValue(matcher.group(2), matcher.group(3), matcher.group(5)));
//                }
                for (String s : bs.getBinding(SparqlVariable.MORPHOLOGY_TRAIT_VALUE).getValue().stringValue().split("---")) {
                    String[] _s = s.split("<>");
                    values.add(new RangedProperty.RangedValue(_s[0], _s[1], _s[2]));
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

    private void reloadLexicalRelCache() {
        lexicalRel.clear();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectLexinfoData.LEXICAL_REL.replace("_PROPERTY_", SparqlPrefix.VARTRANS.getUri() + "lexicalRel"))) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                PropertyHierarchy ph = new PropertyHierarchy();
                ph.setParentID(bs.getBinding(SparqlVariable.PARENT).getValue().stringValue());
                ph.setPropertyDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                ph.setPropertyId(((bs.getBinding(SparqlVariable.PROPERTY_NAME) != null)
                        ? bs.getBinding(SparqlVariable.PROPERTY_NAME).getValue().stringValue() : ""));
                ph.setPropertyLabel((bs.getBinding(SparqlVariable.LABEL) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                lexicalRel.add(ph);
            }
        } catch (QueryEvaluationException qee) {
        }
    }

    private void reloadSenseRelCache() {
        senseRel.clear();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectLexinfoData.SENSE_REL.replace("_PROPERTY_", SparqlPrefix.VARTRANS.getUri() + "senseRel"))) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                PropertyHierarchy ph = new PropertyHierarchy();
                ph.setParentID(bs.getBinding(SparqlVariable.PARENT).getValue().stringValue());
                ph.setPropertyDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                ph.setPropertyId(((bs.getBinding(SparqlVariable.PROPERTY_NAME) != null)
                        ? bs.getBinding(SparqlVariable.PROPERTY_NAME).getValue().stringValue() : ""));
                ph.setPropertyLabel((bs.getBinding(SparqlVariable.LABEL) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                senseRel.add(ph);
            }
        } catch (QueryEvaluationException qee) {
        }
    }
    
    private void reloadFormRelCache() {
        formRel.clear();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectLexinfoData.FORM_REL)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                PropertyHierarchy ph = new PropertyHierarchy();
                ph.setParentID(bs.getBinding(SparqlVariable.PARENT).getValue().stringValue());
                ph.setPropertyDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                ph.setPropertyId(((bs.getBinding(SparqlVariable.PROPERTY_NAME) != null)
                        ? bs.getBinding(SparqlVariable.PROPERTY_NAME).getValue().stringValue() : ""));
                ph.setPropertyLabel((bs.getBinding(SparqlVariable.LABEL) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                formRel.add(ph);
            }
        } catch (QueryEvaluationException qee) {
        }
    }

    private void reloadUsageCache() {
        usage.clear();
        usageHash.clear();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectLexinfoData.USAGE_PROPERTIES)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                RangedProperty mp = new RangedProperty();
                mp.setPropertyId(((bs.getBinding(SparqlVariable.PROPERTY_NAME) != null)
                        ? bs.getBinding(SparqlVariable.PROPERTY_NAME).getValue().stringValue() : ""));
                mp.setPropertyLabel((bs.getBinding(SparqlVariable.LABEL) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                mp.setPropertyDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                List<RangedProperty.RangedValue> values = new ArrayList();
                for (String s : bs.getBinding(SparqlVariable.USAGE_VALUES).getValue().stringValue().split("---")) {
                    String[] _s = s.split("<>");
                    values.add(new RangedProperty.RangedValue(_s[0], _s[1], _s[2]));
                }
                mp.setPropertyValues(values);
                usage.add(mp);
                usageHash.put(mp.getPropertyId(), mp);
            }
        } catch (QueryEvaluationException qee) {
        }
    }

    private void reloadLexicalCategoryCache() {
        lexicalCategory.clear();
        lexicalCategoryHash.clear();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectLexinfoData.CATEGORY_FOR_LEXICALENTRY)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                RangedProperty mp = new RangedProperty();
                mp.setPropertyId(((bs.getBinding(SparqlVariable.TYPE) != null)
                        ? bs.getBinding(SparqlVariable.TYPE).getValue().stringValue() : ""));
                mp.setPropertyLabel((bs.getBinding(SparqlVariable.LABEL) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                mp.setPropertyDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                List<RangedProperty.RangedValue> values = new ArrayList();
                for (String s : bs.getBinding(SparqlVariable.VALUE).getValue().stringValue().split("---")) {
                    String[] _s = s.split("<>");
                    values.add(new RangedProperty.RangedValue(_s[0], _s[1], _s[2]));
                }
                mp.setPropertyValues(values);
                lexicalCategory.add(mp);
                lexicalCategoryHash.put(mp.getPropertyId(), mp);
            }
        } catch (QueryEvaluationException qee) {
        }
    }

    private void reloadSemanticCategoryCache() {
        semanticCategory.clear();
        semanticCategoryHash.clear();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectLexinfoData.CATEGORY_FOR_SENSE)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                RangedProperty mp = new RangedProperty();
                mp.setPropertyId(((bs.getBinding(SparqlVariable.TYPE) != null)
                        ? bs.getBinding(SparqlVariable.TYPE).getValue().stringValue() : ""));
                mp.setPropertyLabel((bs.getBinding(SparqlVariable.LABEL) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                mp.setPropertyDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                List<RangedProperty.RangedValue> values = new ArrayList();
                for (String s : bs.getBinding(SparqlVariable.VALUE).getValue().stringValue().split("---")) {
                    String[] _s = s.split("<>");
                    values.add(new RangedProperty.RangedValue(_s[0], _s[1], _s[2]));
                }
                mp.setPropertyValues(values);
                semanticCategory.add(mp);
                semanticCategoryHash.put(mp.getPropertyId(), mp);
            }
        } catch (QueryEvaluationException qee) {
        }
    }

    private void reloadFormCategoryCache() {
        formCategory.clear();
        formCategoryHash.clear();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectLexinfoData.CATEGORY_FOR_FORM)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                RangedProperty mp = new RangedProperty();
                mp.setPropertyId(((bs.getBinding(SparqlVariable.TYPE) != null)
                        ? bs.getBinding(SparqlVariable.TYPE).getValue().stringValue() : ""));
                mp.setPropertyLabel((bs.getBinding(SparqlVariable.LABEL) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                mp.setPropertyDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                List<RangedProperty.RangedValue> values = new ArrayList();
                for (String s : bs.getBinding(SparqlVariable.VALUE).getValue().stringValue().split("---")) {
                    String[] _s = s.split("<>");
                    values.add(new RangedProperty.RangedValue(_s[0], _s[1], _s[2]));
                }
                mp.setPropertyValues(values);
                formCategory.add(mp);
                formCategoryHash.put(mp.getPropertyId(), mp);
            }
        } catch (QueryEvaluationException qee) {
        }
    }
}
