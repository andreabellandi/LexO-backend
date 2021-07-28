/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.service.data.vocabulary.Value;
import it.cnr.ilc.lexo.sparql.SparqlSelectOntolexData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 */
public final class OntolexManager implements Manager, Cached {

    private final List<Value> lexicalEntryTypes = new ArrayList<>();
    private final List<Value> formTypes = new ArrayList<>();
    private final List<Value> etymologicalEntryTypes = new ArrayList<>();

    public List<Value> getFormTypes() {
        return formTypes;
    }

    public List<Value> getLexicalEntryTypes() {
        return lexicalEntryTypes;
    }

    public List<Value> getEtymologicalEntryTypes() {
        return etymologicalEntryTypes;
    }

    OntolexManager() {
        reloadCache();
    }

    @Override
    public void reloadCache() {
        reloadLexicalEntryTypeCache();
        reloadFormTypeCache();
        reloadEtymologicalEntryTypeCache();
    }

    private void reloadLexicalEntryTypeCache() {
        lexicalEntryTypes.clear();
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectOntolexData.LEXICAL_ENTRY_TYPE);
//        try (TupleQueryResult result = tupleQuery.evaluate()) {
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectOntolexData.LEXICAL_ENTRY_TYPE)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                Value value = new Value();
                value.setValueDescription((bs.getBinding(SparqlVariable.CLASS_COMMENT) != null) ? ((Literal) bs.getBinding(SparqlVariable.CLASS_COMMENT).getValue()).getLabel() : "");
                value.setValueId((bs.getBinding(SparqlVariable.LEXICAL_ENTRY_TYPE) != null) ? ((IRI) bs.getBinding(SparqlVariable.LEXICAL_ENTRY_TYPE).getValue()).getLocalName() : "");
                value.setValueLabel((bs.getBinding(SparqlVariable.LABEL) != null) ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                lexicalEntryTypes.add(value);
            }
        } catch (QueryEvaluationException qee) {
        }
    }

    
    private void reloadEtymologicalEntryTypeCache() {
        etymologicalEntryTypes.clear();
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectOntolexData.ETYMOLOGICAL_ENTRY_TYPE)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                Value value = new Value();
                value.setValueDescription((bs.getBinding(SparqlVariable.CLASS_COMMENT) != null) ? ((Literal) bs.getBinding(SparqlVariable.CLASS_COMMENT).getValue()).getLabel() : "");
                value.setValueId((bs.getBinding(SparqlVariable.LEXICAL_ENTRY_TYPE) != null) ? ((IRI) bs.getBinding(SparqlVariable.LEXICAL_ENTRY_TYPE).getValue()).getLocalName() : "");
                value.setValueLabel((bs.getBinding(SparqlVariable.LABEL) != null) ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                etymologicalEntryTypes.add(value);
            }
        } catch (QueryEvaluationException qee) {
        }
    }
    
    
    private void reloadFormTypeCache() {
        formTypes.clear();
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, SparqlSelectOntolexData.FORM_TYPE);
//        try (TupleQueryResult result = tupleQuery.evaluate()) {
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlSelectOntolexData.FORM_TYPE)) {

            while (result.hasNext()) {
                BindingSet bs = result.next();
                Value value = new Value();
                value.setValueDescription((bs.getBinding(SparqlVariable.PROPERTY_COMMENT) != null) ? ((Literal) bs.getBinding(SparqlVariable.PROPERTY_COMMENT).getValue()).getLabel() : "");
                value.setValueId((bs.getBinding(SparqlVariable.FORM_TYPE) != null) ? ((IRI) bs.getBinding(SparqlVariable.FORM_TYPE).getValue()).getLocalName() : "");
                value.setValueLabel((bs.getBinding(SparqlVariable.LABEL) != null) ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : "");
                formTypes.add(value);
            }
        } catch (QueryEvaluationException qee) {
        }
    }

}
