/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.sparql.SparqlQueryUtil;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 */
public final class UtilityManager implements Manager, Cached {

    @Override
    public void reloadCache() {

    }

    // ret values: 
    // null = id does not exist
    // empty = attribute value does not exist
    // string = attribute value
    public String getEntityAttribute(String id, String relation) throws QueryEvaluationException {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlQueryUtil.ENTITY_RELATION.replaceAll("_ID_", id)
//                        .replaceAll("_RELATION_", relation));
//        try (TupleQueryResult result = tupleQuery.evaluate()) {
        String query = SparqlQueryUtil.ENTITY_RELATION.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation);
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.TYPE) != null)
                        ? ((bs.getBinding(SparqlVariable.VALUE) != null) ? bs.getBinding(SparqlVariable.VALUE).getValue().stringValue() : "") : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public boolean existsLinguisticRelation(String id, String relation, String value) throws QueryEvaluationException {
        String query = SparqlQueryUtil.ASK_ENTITY_LINGUISTIC_RELATION.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_", value);
        return RDFQueryUtil.evaluateBQuery(query);
    }
    
    public boolean existsGenericRelation(String id, String relation, String value) throws QueryEvaluationException {
        String query = SparqlQueryUtil.ASK_ENTITY_GENERIC_RELATION.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_", value);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public int getNumberOfStatements(String id) throws QueryEvaluationException {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlQueryUtil.NUMBER_OF_STATEMENTS.replaceAll("_ID_", id));
//        try (TupleQueryResult result = tupleQuery.evaluate()) {
        String query = SparqlQueryUtil.NUMBER_OF_STATEMENTS.replaceAll("_ID_", id);
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.STATEMENTS_NUMBER) != null) ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).intValue() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return 0;
    }

    public String getLanguage(String id) throws QueryEvaluationException {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlQueryUtil.LANGUAGE.replaceAll("_ID_", id));
//        try (TupleQueryResult result = tupleQuery.evaluate()) {
        String query = SparqlQueryUtil.LANGUAGE.replaceAll("_ID_", id);
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LEXICON_LANGUAGE) != null) ? bs.getBinding(SparqlVariable.LEXICON_LANGUAGE).getValue().stringValue() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }
    
    public String getLabel(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.LEXICAL_ENTRY_LABEL.replaceAll("_ID_", id);
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LABEL) != null) ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public String getLexicalEntryByForm(String id) throws QueryEvaluationException {
//        TupleQuery tupleQuery = GraphDbUtil.getConnection().prepareTupleQuery(QueryLanguage.SPARQL,
//                SparqlQueryUtil.LEXICAL_ENTRY_BY_FORM.replaceAll("_ID_", id));
//        try (TupleQueryResult result = tupleQuery.evaluate()) {
        String query = SparqlQueryUtil.LEXICAL_ENTRY_BY_FORM.replaceAll("_ID_", id);
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME) != null)
                        ? ((IRI) bs.getBinding(SparqlVariable.LEXICAL_ENTRY_INSTANCE_NAME).getValue()).getLocalName() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }
    
    public String bibliographyById(String id, String itemKey) throws QueryEvaluationException {
        String query = SparqlQueryUtil.BIBLIOGRAFY_BY_ITEMKEY.replaceAll("_ID_", id).replaceAll("_ITEMKEY_", itemKey);
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.BIBLIOGRAPHY_ID) != null)
                        ? ((IRI) bs.getBinding(SparqlVariable.BIBLIOGRAPHY_ID).getValue()).getLocalName() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public int lexicalEntriesNumberByLanguage(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.LEXICAL_ENTRY_NUMBER_BY_LANGUAGE.replaceAll("_ID_", id);
        try (TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LABEL_COUNT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL_COUNT).getValue()).intValue() : 1;
            }
        } catch (QueryEvaluationException qee) {
        }
        return 1;
    }

    public boolean isLexiconLanguage(String id) throws QueryEvaluationException {
//        BooleanQuery ask = GraphDbUtil.getConnection().prepareBooleanQuery(QueryLanguage.SPARQL,
//                SparqlQueryUtil.IS_LEXICON_LANGUAGE.replaceAll("_ID_", id));
        String query = SparqlQueryUtil.IS_LEXICON_LANGUAGE.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean hasLexicalEntryChildren(String id) throws QueryEvaluationException {
//        BooleanQuery ask = GraphDbUtil.getConnection().prepareBooleanQuery(QueryLanguage.SPARQL,
//                SparqlQueryUtil.HAS_LEXICALENTRY_CHILDREN.replaceAll("_ID_", id));
        String query = SparqlQueryUtil.HAS_LEXICALENTRY_CHILDREN.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isLexicalEntry(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_LEXICALENTRY_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }
    
    public boolean isLexicalEntryOrComponent(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_LEXICALENTRY_ID_OR_COMPONENT_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }
    
    public boolean isComponent(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_COMPONENT_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }
    
    public boolean exists(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.EXISTS_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }
    
    public boolean existsTyped(String id, String type) throws QueryEvaluationException {
        String query = SparqlQueryUtil.EXISTS_TYPE_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean languageExists(String lang) throws QueryEvaluationException {
//        BooleanQuery ask = GraphDbUtil.getConnection().prepareBooleanQuery(QueryLanguage.SPARQL,
//                SparqlQueryUtil.EXISTS_LANGUAGE.replaceAll("_LANG_", lang));
        String query = SparqlQueryUtil.EXISTS_LANGUAGE.replaceAll("_LANG_", lang);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isForm(String id) throws QueryEvaluationException {
//        BooleanQuery ask = GraphDbUtil.getConnection().prepareBooleanQuery(QueryLanguage.SPARQL,
//                SparqlQueryUtil.IS_FORM_ID.replaceAll("_ID_", id));
        String query = SparqlQueryUtil.IS_FORM_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isLexicalSense(String id) throws QueryEvaluationException {
//        BooleanQuery ask = GraphDbUtil.getConnection().prepareBooleanQuery(QueryLanguage.SPARQL,
//                SparqlQueryUtil.IS_LEXICALSENSE_ID.replaceAll("_ID_", id));
        String query = SparqlQueryUtil.IS_LEXICALSENSE_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }
    
    public boolean isEtymology(String id) throws QueryEvaluationException {
//        BooleanQuery ask = GraphDbUtil.getConnection().prepareBooleanQuery(QueryLanguage.SPARQL,
//                SparqlQueryUtil.IS_FORM_ID.replaceAll("_ID_", id));
        String query = SparqlQueryUtil.IS_ETYMOLOGY_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }
    
    public boolean isEtymologicalLink(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_ETYMOLOGICAL_LINK_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }
    
    public boolean isCognate(String id, int n) {
        String query = SparqlQueryUtil.IS_COGNATE.replaceAll("_ID_", id).replace("_COG_NUMBER_", String.valueOf(n));
        return RDFQueryUtil.evaluateBQuery(query);
    }

}
