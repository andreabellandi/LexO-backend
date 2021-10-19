/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.ConceptList;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormBySenseFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryList;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.Morphology;
import it.cnr.ilc.lexo.service.data.lexicon.output.queryExpansion.Form;
import it.cnr.ilc.lexo.sparql.SparqlQueryExpansion;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class QueryExpansionManager implements Manager, Cached {
    
    static final Logger logger = LoggerFactory.getLogger(QueryExpansionManager.class.getName());
    
    private final String namespace = LexOProperties.getProperty("repository.lexicon.namespace");
    private final String ontologyNamespace = LexOProperties.getProperty("repository.ontology.namespace");
    
    public String getNamespace() {
        return namespace;
    }
    
    @Override
    public void reloadCache() {
        
    }
    
    public TupleQueryResult getReferencedLinguisticObject(ConceptList cl) throws ManagerException {
        String conceptsList = "";
        if (cl.getConceptList() == null) {
            throw new ManagerException("concepts list cannot be empty");
        } else {
            if (cl.getConceptList().isEmpty()) {
                throw new ManagerException("concepts list cannot be empty");
            } else {
                for (String c : cl.getConceptList()) {
                    conceptsList = conceptsList + "\\\"" + ontologyNamespace + c + "\\\"" + " OR ";
                }
            }
        }
        String query = SparqlQueryExpansion.QUERY_EXPANSION_REFERENCED_LINGUISTIC_OBJECT.
                replace("[CONCEPTS_LIST]", conceptsList.substring(0, conceptsList.length() - 3));
        return RDFQueryUtil.evaluateTQuery(query);
    }
    
    public TupleQueryResult getForms(LexicalEntryList lel) throws ManagerException {
        String lexicalEntryList = "";
        if (lel.getLexicalEntryList() == null) {
            throw new ManagerException("lexical entries list cannot be empty");
        } else {
            if (lel.getLexicalEntryList().isEmpty()) {
                throw new ManagerException("lexical entries list cannot be empty");
            } else {
                for (String le : lel.getLexicalEntryList()) {
                    lexicalEntryList = lexicalEntryList + "\\\"" + namespace + le + "\\\"" + " OR ";
                }
            }
        }
        String query = SparqlQueryExpansion.QUERY_EXPANSION_FORMS.
                replace("[LEXICAL_ENTRY_LIST]", lexicalEntryList.substring(0, lexicalEntryList.length() - 3));
        return RDFQueryUtil.evaluateTQuery(query);
    }

//    public TupleQueryResult getFilterdForms(String id) throws ManagerException {
//        String query = SparqlQueryExpansion.DATA_FORMS_BY_LEXICAL_SENSE.replace("[IRI]", "\\\"" + namespace + id + "\\\"")
//                .replace("[FORM_CONSTRAINT]", "");
//        return RDFQueryUtil.evaluateTQuery(query);
//    }
    public TupleQueryResult getFilterdForms(String ids) throws ManagerException {
        String query = SparqlQueryExpansion.DATA_FORMS_BY_LEXICAL_SENSE.replace("[IRI]", ids)
                .replace("[FORM_CONSTRAINT]", "");
        return RDFQueryUtil.evaluateTQuery(query);
    }
    
    public TupleQueryResult getFilterdForms(FormBySenseFilter ff) throws ManagerException {
        Manager.validateWithEnum("searchFormTypes", EnumUtil.SearchFormTypes.class, ff.getFormType());
        Manager.validateWithEnum("acceptedSearchFormExtendTo", EnumUtil.AcceptedSearchFormExtendTo.class, ff.getExtendTo());
        Manager.validateWithEnum("acceptedSearchFormExtensionDegree", EnumUtil.AcceptedSearchFormExtensionDegree.class, String.valueOf(ff.getExtensionDegree()));
        String query = (ff.getFormType().equals(EnumUtil.SearchFormTypes.Lemma.toString()))
                ? SparqlSelectData.DATA_FORMS_BY_LEXICAL_ENTRY.replace("[IRI]", "\\\"" + namespace + ff.getLexicalEntry() + "\\\"")
                        .replace("[FORM_CONSTRAINT]", "")
                : ff.getExtensionDegree() == 0
                ? SparqlSelectData.DATA_FORMS_BY_LEXICAL_ENTRY.replace("[IRI]", "\\\"" + namespace + ff.getLexicalEntry() + "\\\"")
                        .replace("[FORM_CONSTRAINT]", "FILTER(regex(str(?" + SparqlVariable.WRITTEN_REPRESENTATION + "), \"^" + ff.getForm().trim() + "$\"))\n")
                : SparqlSelectData.DATA_FORMS_BY_LEXICAL_ENTRY.replace("[IRI]", "\\\"" + namespace + ff.getLexicalEntry() + "\\\"")
                        .replace("[FORM_CONSTRAINT]", "");
        return RDFQueryUtil.evaluateTQuery(query);
    }
    
    public TupleQueryResult getRelationByLenght(String relation, String startNode) {
        String query = SparqlQueryExpansion.DATA_PATH_LENGTH.replace("[START_NODE]", startNode)
                .replace("[START_RELATION]", relation)
                .replace("[MID_RELATION]", relation);
        return RDFQueryUtil.evaluateTQuery(query);
    }
    
    public List<Form> getFormItemListCopy(List<Form> forms) {
        List<Form> _forms = new ArrayList();
        for (Form fi : forms) {
            Form _fi = new Form();
//            _fi.setForm(fi.getForm());
            _fi.setFormType(fi.getFormType());
//            _fi.setFormInstanceName(fi.getFormInstanceName());
            _fi.setDefinition(fi.getDefinition());
            _fi.setLabel(fi.getLabel());
//            _fi.setLexicalEntry(fi.getLexicalEntry());
            _fi.setLexicalEntryInstanceName(fi.getLexicalEntryInstanceName());
            _fi.setMorphology(getMorphologyListCopy(fi.getMorphology()));
//            _fi.setTargetSense(fi.getTargetSense());
            _fi.setTargetSenseInstanceName(fi.getTargetSenseInstanceName());
            _forms.add(_fi);
        }
        return _forms;
    }
    
    private ArrayList<Morphology> getMorphologyListCopy(ArrayList<Morphology> morpho) {
        ArrayList<Morphology> _morpho = new ArrayList();
        for (Morphology m : morpho) {
            Morphology _m = new Morphology(m.getTrait(), m.getValue());
            _morpho.add(_m);
        }
        return _morpho;
    }
    
    public TupleQueryResult getFormsBySenseRelation(FormBySenseFilter ff, String sense) throws ManagerException {
        String query = SparqlSelectData.DATA_FORMS_BY_SENSE_RELATION
                .replace("[RELATION_DISTANCE_PATH]", "lex:" + sense + " lexinfo:" + ff.getExtendTo() + " ?" + SparqlVariable.SENSE + " . \n" + 
                        " ?" + SparqlVariable.SENSE + " skos:definition ?" + SparqlVariable.SENSE_DEFINITION + " . \n");
        return RDFQueryUtil.evaluateTQuery(query);
    }
    
}
