/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.sparql.SparqlDeleteData;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlQueryUtil;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.sparql.SparqlUpdateData;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import it.cnr.ilc.lexo.util.StringUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.stream.Collectors;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author andreabellandi
 */
public class ECDDeletionManager implements Manager, Cached {
    
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));
    
    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String deleteRelation(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_LEXICAL_FUNCTION.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }
    
    public String deleteECDForm(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_ECD_FORM.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }
    
    public String deleteECDEntry(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_ECD_ENTRY.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }
    
    public String deleteECDEntryPos(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_ECD_ENTRY_POS.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }
    
    public String deleteECDictionary(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_EC_DICTIONARY.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }
    
    public String deleteECDMeaning(String idECDEntry, String ECDMeaning) throws ManagerException {
        // 1) component che descrivono il senso (sono foglie per ipotesi)
        Set<Resource> r = selectComponentsDescribing(ECDMeaning);
        // 2) genitori che hanno membership rdf:_n -> component
        List<ParentMembership> memberships = selectParentMemberships(r);
        StringBuilder update = new StringBuilder().append(SparqlPrefix.RDF.getSparqlPrefix() + "\n"
                + SparqlPrefix.RDFS.getSparqlPrefix() + "\n"
                + SparqlPrefix.XSD.getSparqlPrefix() + "\n");
        // 3) rimuovi membership precise (DELETE DATA)
        if (!memberships.isEmpty()) {
            update.append(deleteMembershipsData(memberships)).append(";\n");
        }
        // 4) cancella i component foglia (soggetto e incoming)
        update.append(deleteSubjectsWhere(r)).append(";\n");
        update.append(deleteIncomingWhere(r)).append(";\n");
        // 5) cancella il senso (soggetto e incoming)
        update.append(deleteSubjectAndIncoming(ECDMeaning)).append(";\n");
        // 6) rinumera i genitori impattati + riallinea label
        LinkedHashSet<Resource> parents = memberships.stream()
                .map(pm -> pm.parent).collect(Collectors.toCollection(LinkedHashSet::new));
        for (Resource parent : parents) {
            int level = detectLevel(idECDEntry, parent);
            update.append(renumberUpdate(parent, level)).append(";\n");
        }
        RDFQueryUtil.update(update.toString());
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }
    
    private static Set<Resource> selectComponentsDescribing(String ECDMeaning) {
        return RDFQueryUtil.evaluateTQuery(SparqlSelectData.DATA_GET_LEXICOGRAPHIC_COMPONENT_BY_SENSE.replaceAll("_ID_SENSE_", ECDMeaning)).stream()
                .map(bs -> (Resource) bs.getValue(SparqlVariable.LEXICOGRAPHIC_COMPONENT))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
    
    private static List<ParentMembership> selectParentMemberships(Set<Resource> children) {
        if (children.isEmpty()) {
            return Collections.emptyList();
        }
        String values = children.stream().map(r -> "(<" + r.stringValue() + ">)").collect(Collectors.joining(" "));
        return RDFQueryUtil.evaluateTQuery(SparqlSelectData.DATA_GET_PARENT_MEMBERSHIP.replaceAll("_VALUES_", values)).stream()
                .map(bs -> new ParentMembership((Resource) bs.getValue("parent"), (IRI) bs.getValue("m"), (Resource) bs.getValue("child")))
                .collect(Collectors.toList());
    }
    
    private static final class ParentMembership {
        
        final Resource parent;
        final IRI membership;
        final Resource child;
        
        ParentMembership(Resource p, IRI m, Resource c) {
            parent = p;
            membership = m;
            child = c;
        }
    }

    /**
     * livello 1 se parent == entry, livello 2 se entry rdf:_n parent,
     * altrimenti 3
     */
    private static int detectLevel(String entry, Resource parent) {
        if (parent.stringValue().equals(entry)) {
            return 1;
        }
        return RDFQueryUtil.evaluateBQuery(SparqlQueryUtil.ASK_MEMBERSHIP_LEVEL.replaceAll("_ENTRY_", entry).replaceAll("_PARENT_", parent.stringValue())) ? 2 : 3;
    }
    
    private static String deleteMembershipsData(List<ParentMembership> ms) {
        StringBuilder sb = new StringBuilder("DELETE DATA {\n");
        for (ParentMembership pm : ms) {
            sb.append('<').append(pm.parent).append("> <").append(pm.membership).append("> <")
                    .append(pm.child).append("> .\n");
        }
        sb.append("}");
        return sb.toString();
    }
    
    private static String deleteSubjectsWhere(Set<Resource> subjects) {
        String values = subjects.stream().map(r -> "(<" + r.stringValue() + ">)").collect(Collectors.joining(" "));
        return "DELETE { ?s ?p ?o } WHERE {\n"
                + "  VALUES (?s) { " + values + " }\n"
                + "  ?s ?p ?o .\n"
                + "}";
    }
    
    private static String deleteIncomingWhere(Set<Resource> objects) {
        String values = objects.stream().map(r -> "(<" + r.stringValue() + ">)").collect(Collectors.joining(" "));
        return "DELETE { ?s ?p ?o } WHERE {\n"
                + "  VALUES (?o) { " + values + " }\n"
                + "  ?s ?p ?o .\n"
                + "}";
    }
    
    private static String deleteSubjectAndIncoming(String iri) {
        return "DELETE WHERE { <" + iri + "> ?p ?o . };\n"
                + "DELETE WHERE { ?s ?p <" + iri + "> . }";
    }

    /**
     * Rinumerazione + rietichettatura coerente al livello (romani / arabi /
     * lettere)
     */
    private static String renumberUpdate(Resource parent, int level) {
        String parentIri = "<" + parent.stringValue() + ">";
        String valuesRoman = StringUtil.romanValues(50);
        String valuesLetters = StringUtil.letterValues(26);
        String labelBlock = (level == 1)
                ? "  VALUES (?n ?roman) { " + valuesRoman + " }\n"
                + "  FILTER(?n = ?newIdx)\n"
                + "  BIND(?roman AS ?newLabel)\n"
                : (level == 2)
                        ? "  BIND(STR(?newIdx) AS ?newLabel)\n"
                        : "  VALUES (?n ?letter) { " + valuesLetters + " }\n"
                        + "  FILTER(?n = ?newIdx)\n"
                        + "  BIND(?letter AS ?newLabel)\n";
        return SparqlUpdateData.ECD_MEANING_RENUMBERING.replaceAll("_PARENT_IRI_", parentIri).replaceAll("_LABEL_BLOCK_", labelBlock);
    }
    
}
