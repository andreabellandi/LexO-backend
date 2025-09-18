/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.sparql.SparqlIndex;
import it.cnr.ilc.lexo.sparql.SparqlQueryUtil;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Namespace;
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

    public boolean existsSKOSRelation(String id, String relation, String value) throws QueryEvaluationException {
        String query = SparqlQueryUtil.ASK_ENTITY_SKOS_RELATION.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", relation)
                .replaceAll("_VALUE_", value);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean existsLabel(String id, String labelRelation, String language) {
        String query = SparqlQueryUtil.ASK_LABEL_RELATION.replaceAll("_ID_", id)
                .replaceAll("_RELATION_", labelRelation)
                .replaceAll("_LANGUAGE_", language);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public ArrayList<String> getConnectors() throws QueryEvaluationException {
        String query = SparqlIndex.CONNECTORS;
        ArrayList<String> connectors = new ArrayList();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                if (bs.getBinding(SparqlVariable.CONNECTOR_NAME) != null) {
                    connectors.add(bs.getBinding(SparqlVariable.CONNECTOR_NAME).getValue().stringValue());
                }
            }
        } catch (QueryEvaluationException qee) {
        }
        return connectors;
    }

    public String getLanguage(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.LANGUAGE.replaceAll("_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LEXICON_LANGUAGE) != null) ? bs.getBinding(SparqlVariable.LEXICON_LANGUAGE).getValue().stringValue() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public String getLexiconIDByLanguage(String lang) throws QueryEvaluationException {
        String query = SparqlQueryUtil.LEXICON_BY_LANGUAGE.replaceAll("_LANG_", lang);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LEXICON) != null) ? bs.getBinding(SparqlVariable.LEXICON).getValue().stringValue() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public String getDictionaryIDByLanguage(String lang) throws QueryEvaluationException {
        String query = SparqlQueryUtil.DICITONARY_BY_LANGUAGE.replaceAll("_LANG_", lang);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.DICTIONARY) != null) ? bs.getBinding(SparqlVariable.DICTIONARY).getValue().stringValue() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public Map<String, Integer> getECDMeaningsCountOfEntity(String idDe, String pos) throws QueryEvaluationException {
        Map<String, Integer> meaningsCount = new HashMap();
        String query = SparqlQueryUtil.GET_ECD_MEANINGS_COUNT_OF_DICTIONARY_ENTRY.replaceAll("_ID_DE_", idDe).replaceAll("_POS_", pos);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                meaningsCount.put((bs.getBinding(SparqlVariable.LEXICAL_ENTRY) != null) ? bs.getBinding(SparqlVariable.LEXICAL_ENTRY).getValue().stringValue() : "", 
                        (bs.getBinding(SparqlVariable.LABEL_COUNT) != null) ? Integer.parseInt(bs.getBinding(SparqlVariable.LABEL_COUNT).getValue().stringValue()) : 0);            }
        } catch (QueryEvaluationException qee) {
        }
        return meaningsCount.size() > 0 ? meaningsCount : null;
    }

    public Map<String, String> getLexicalSensePoS(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.GET_POS_OF_LEXICAL_SENSE.replaceAll("_ID_", id);
        Map<String, String> lepos = new HashMap();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                lepos.put((bs.getBinding(SparqlVariable.LEXICAL_ENTRY) != null) ? bs.getBinding(SparqlVariable.LEXICAL_ENTRY).getValue().stringValue()
                        : "",
                        (bs.getBinding(SparqlVariable.LEXICAL_ENTRY_POS) != null) ? bs.getBinding(SparqlVariable.LEXICAL_ENTRY_POS).getValue().stringValue()
                        : "");
            }
        } catch (QueryEvaluationException qee) {
        }
        return lepos;
    }

    public String getFormLanguage(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.FORM_LANGUAGE.replaceAll("_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LEXICON_LANGUAGE) != null) ? bs.getBinding(SparqlVariable.LEXICON_LANGUAGE).getValue().stringValue() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public String getDictLanguage(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.DICTIONARY_LANGUAGE.replaceAll("_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.DICT_LANGUAGE) != null) ? bs.getBinding(SparqlVariable.DICT_LANGUAGE).getValue().stringValue() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public String getLabel(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.LEXICAL_ENTRY_LABEL.replaceAll("_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LABEL) != null) ? ((Literal) bs.getBinding(SparqlVariable.LABEL).getValue()).getLabel() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public String getLexicalEntryByForm(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.LEXICAL_ENTRY_BY_FORM.replaceAll("_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LEXICAL_ENTRY) != null)
                        ? (bs.getBinding(SparqlVariable.LEXICAL_ENTRY).getValue().toString()) : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public String getLexicalEntryType(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.LEXICALENTRY_TYPE.replaceAll("_ID_", id);
        ArrayList<String> types = new ArrayList();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LEXICAL_ENTRY_TYPE) != null)
                        ? (bs.getBinding(SparqlVariable.LEXICAL_ENTRY_TYPE).getValue().toString()) : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public Map<String, String> getLexicalSensesByLexicalEntry(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.LEXICAL_SENSES_BY_LEXICAL_ENTRY.replaceAll("_ID_", id);
        Map<String, String> senses = new HashMap();
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                senses.put(bs.getBinding(SparqlVariable.SENSE).getValue().stringValue(),
                        (bs.getBinding(SparqlVariable.CREATOR) != null) ? bs.getBinding(SparqlVariable.CREATOR).getValue().stringValue()
                        : "unknown");
            }
        } catch (QueryEvaluationException qee) {
        }
        return senses;
    }

    public ArrayList<String> getDictionaryEntryByLexicalEntry(String id) throws QueryEvaluationException {
        ArrayList<String> des = new ArrayList();
        String query = SparqlQueryUtil.GET_DICTIONARY_ENTRY_ASSOCIATED_TO_LEXICAL_ENTRY.replaceAll("_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                des.add(bs.getBinding(SparqlVariable.DICTIONARY_ENTRY).getValue().toString());
            }
        } catch (QueryEvaluationException qee) {
        }
        return des;
    }

    public String getLexicographicComponentBySense(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.GET_LEXICOGRAPHIC_COMPONENT_BY_DESCRIBE.replaceAll("_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LEXICOGRAPHIC_COMPONENT).getValue().toString());
            }
        } catch (QueryEvaluationException qee) {
        }
        return "";
    }

    public int getNumberOfOrderedSenses(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.GET_NUMBER_OF_RDF_MEMBERS_OF_LEXICAL_ENTRY.replaceAll("_LE_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return Integer.parseInt(bs.getBinding(SparqlVariable.LABEL_COUNT).getValue().stringValue());
            }
        } catch (QueryEvaluationException qee) {
        }
        return 0;
    }

    public boolean isDictionaryEntry(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_DICTIONARYENTRY_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public String bibliographyById(String id, String itemKey) throws QueryEvaluationException {
        String query = SparqlQueryUtil.BIBLIOGRAFY_BY_ITEMKEY.replaceAll("_ID_", id).replaceAll("_ITEMKEY_", itemKey);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.BIBLIOGRAPHY_ID) != null)
                        ? (bs.getBinding(SparqlVariable.BIBLIOGRAPHY_ID).getValue().toString()) : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public String getImageUrl(String imgID) {
        String query = SparqlQueryUtil.IMAGE_URL.replaceAll("_ID_", imgID);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.IDENTIFIER).getValue().toString());
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public int lexicalEntriesNumberByLanguage(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.LEXICAL_ENTRY_NUMBER_BY_LANGUAGE.replaceAll("_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LABEL_COUNT) != null)
                        ? ((Literal) bs.getBinding(SparqlVariable.LABEL_COUNT).getValue()).intValue() : 1;
            }
        } catch (QueryEvaluationException qee) {
        }
        return 1;
    }

    public boolean dictionaryHasEntry(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.DICTIONARY_HAS_ENTRY.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isLexiconLanguage(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_LEXICON_LANGUAGE.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isDictionary(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_DICTIONARY.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean hasLexicalEntryChildren(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.HAS_LEXICALENTRY_CHILDREN.replaceAll("_ID_", id);
        boolean b = RDFQueryUtil.evaluateBQuery(query);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean hasDictionaryEntryComponents(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.HAS_DICTIONARYENTRY_COMPONENTS.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public String lexicalEntryWithSenses(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.COMPONENT_DESCRIBES_LEXICALENTRY_WITH_SENSES.replaceAll("_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return bs.getBinding(SparqlVariable.LEXICAL_ENTRY) != null ? bs.getBinding(SparqlVariable.LEXICAL_ENTRY).getValue().stringValue() : null;
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public boolean isLexicalEntry(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_LEXICALENTRY_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isDictEntry(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_DICTENTRY_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isLexicographicComponent(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_LEXICOGRAPHIC_COMPONENT_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean lexicalEntryInDictionaryEntry(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_LEXICALENTRY_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public TreeMap<Integer, String> getLexicographicComponetsPositions(String id) {
        TreeMap<Integer, String> map = new TreeMap();
        String query = SparqlQueryUtil.LEXICOGRAPHIC_COMPONENTS_POSITIONS.replaceAll("_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                map.put(Integer.parseInt(((IRI) bs.getBinding(SparqlVariable.PROPERTY_NAME).getValue()).getLocalName().replace("_", "")), bs.getBinding(SparqlVariable.TARGET).getValue().toString());
            }
        } catch (QueryEvaluationException qee) {
        }
        return map;
    }

    public String getSuperLexicographicComponent(String id) {
        String query = SparqlQueryUtil.SUPER_LEXICOGRAPHIC_COMPONENT.replaceAll("_ID_", id);
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(query)) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                return (bs.getBinding(SparqlVariable.LEXICOGRAPHIC_COMPONENT).getValue().toString());
            }
        } catch (QueryEvaluationException qee) {
        }
        return null;
    }

    public boolean isConceptSet(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_CONCEPT_SET_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isLexicalEntryOrComponent(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_LEXICALENTRY_ID_OR_COMPONENT_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isAdmissibleHeadOfCollocation(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_ADMISSIBLE_HEAD_OF_COLLOCATION_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isComponent(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_COMPONENT_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isCollocation(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_COLLOCATION_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isCorpusFrequency(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_CORPUS_FREQUENCY_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isFormRestriction(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_FORMRESTRICTION_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isLexicoSemanticRelation(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_LEXICOSEMANTIC_RELATION_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isTranslationSet(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_TRANSLATIONSET_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isLexicalConcept(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_LEXICAL_CONCEPT_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean exists(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.EXISTS_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean existsTyped(String id, String type) throws QueryEvaluationException {
        String query = SparqlQueryUtil.EXISTS_TYPE_ID.replaceAll("_ID_", id).replace("_TYPE_", type);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean languageExists(String lang) throws QueryEvaluationException {
        String query = SparqlQueryUtil.EXISTS_LANGUAGE.replaceAll("_LANG_", lang);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean dictionaryLanguageExists(String lang) throws QueryEvaluationException {
        String query = SparqlQueryUtil.EXISTS_DICT_LANGUAGE.replaceAll("_LANG_", lang);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isForm(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_FORM_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isLexicalSense(String id) throws QueryEvaluationException {
        String query = SparqlQueryUtil.IS_LEXICALSENSE_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isEtymology(String id) throws QueryEvaluationException {
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

    public boolean isUniqueID(String id) {
        String query = SparqlQueryUtil.UNIQUE_ID.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean hasSubLexicographicComponent(String id) {
        String query = SparqlQueryUtil.LEXICOGRAPHIC_COMPONENT_LEAF.replaceAll("_ID_", id);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public boolean isLexicalEntryJustAssociatedToDictionaryEntry(String deid, String leid) {
        String query = SparqlQueryUtil.IS_LEXICAL_ENTRY_JUST_ASSOCIATED_TO_DICT_ENTRY.replaceAll("_DEID_", deid).replaceAll("_LEID_", leid);
        return RDFQueryUtil.evaluateBQuery(query);
    }

    public String getIndirectRelationType(String id) {
        String type = null;
        try ( TupleQueryResult result = RDFQueryUtil.evaluateTQuery(SparqlQueryUtil.INDIRECT_RELATION_TYPE.replace("_ID_", id))) {
            while (result.hasNext()) {
                BindingSet bs = result.next();
                type = bs.getBinding(SparqlVariable.TYPE).getValue().stringValue();
            }
        } catch (QueryEvaluationException qee) {
        }
        return type;
    }

    public void validateNamespace(String prefix, String baseIRI) throws ManagerException {
        if (prefix == null || baseIRI == null) {
            throw new ManagerException("prefix and  base IRI have to be defined");
        }
        if (prefix.isEmpty()) {
            throw new ManagerException("prefix cannot be empty");
        }
        if (baseIRI.isEmpty()) {
            throw new ManagerException("base IRI cannot be empty");
        }
        String namepsace = RDFQueryUtil.getNamespace(prefix);
        String found = "";
        if (namepsace == null) {
            Iterator<Namespace> nsit = RDFQueryUtil.getNamespaces().iterator();
            while (nsit.hasNext()) {
                Namespace ns = nsit.next();
                if (ns.getName().equals(baseIRI)) {
                    found = ns.getPrefix();
                    break;
                }
            }
            if (!found.isEmpty()) {
                throw new ManagerException(prefix + " corresponds to a different base IRI");
            }
        } else {
            if (!namepsace.equals(baseIRI)) {
                throw new ManagerException(namepsace + " already exists, but it corresponds to a different prefix");
            }
        }
    }

    public boolean existsNamespace(String baseIRI) throws ManagerException {
        Iterator<Namespace> nsit = RDFQueryUtil.getNamespaces().iterator();
        while (nsit.hasNext()) {
            if (baseIRI.contains(nsit.next().getName())) {
                return true;
            }
        }
        return false;
    }

}
