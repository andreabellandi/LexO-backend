/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import static com.ontotext.trree.plugin.sparqltemplate.SparqlTemplatePlugin.VF;
import it.cnr.ilc.lexo.GraphDbUtil;
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
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;

/**
 *
 * @author andreabellandi
 */
public class ECDDeletionManager implements Manager, Cached {

    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

    private enum ChildLevel {
        LEVEL1, // children of Entry
        LEVEL2, // children of 1st-level component
        LEVEL3  // children of 2nd-level component
    }

    private static class ParentLink {

        final Resource parent;
        final IRI ordinalPred;
        final int index;

        ParentLink(Resource parent, IRI ordinalPred, int index) {
            this.parent = parent;
            this.ordinalPred = ordinalPred;
            this.index = index;
        }
    }

    private static class ChildInfo {

        final IRI child;
        final IRI oldPred;
        final int oldIndex;
        final String oldLabel;

        ChildInfo(IRI child, IRI oldPred, int oldIndex, String oldLabel) {
            this.child = child;
            this.oldPred = oldPred;
            this.oldIndex = oldIndex;
            this.oldLabel = oldLabel;
        }
    }

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

    public String deleteECDMeaning(String ECDMeaning) throws ManagerException {
        RepositoryConnection conn = GraphDbUtil.getConnection();
        try {
            List<IRI> components = findComponentsDescribingSense(conn, VF.createIRI(ECDMeaning));
            if (components.isEmpty()) {
                conn.commit();
                return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
            }
            // delete all triples involving the sense
            deleteAllTriplesAbout(conn, VF.createIRI(ECDMeaning));

            // for each leaf component, delete and rebalance
            for (IRI comp : components) {
                deleteComponentRecursive(conn, comp);
            }
            conn.commit();
        } catch (RepositoryException e) {
            conn.rollback();
            throw new RuntimeException("Error while deleting sense " + ECDMeaning, e);
        } finally {
            GraphDbUtil.releaseConnection(conn);
        }
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    private static List<IRI> findComponentsDescribingSense(RepositoryConnection conn, IRI sense) {
        List<IRI> result = new ArrayList<>();
        try ( RepositoryResult<Statement> stmts
                = conn.getStatements(null, VF.createIRI(SparqlPrefix.LEXICOG.getUri(), "describes"), sense, false)) {
            while (stmts.hasNext()) {
                Statement st = stmts.next();
                if (st.getSubject() instanceof IRI) {
                    result.add((IRI) st.getSubject());
                }
            }
        }
        return result;
    }

    private static void deleteAllTriplesAbout(RepositoryConnection conn, Resource res) {
        conn.remove(res, null, null);
        conn.remove((Resource) null, null, res);
    }

    /**
     * Recursively delete a component and rebalance its siblings. If the parent
     * becomes childless and is a component, delete parent too and rebalance one
     * level up.
     */
    private static void deleteComponentRecursive(RepositoryConnection conn, IRI comp) {
        // 1. Find parent link (parent, rdf:_k, comp)
        ParentLink parentLink = findParentLink(conn, comp);

        // 2. Delete all triples about the component itself
        deleteAllTriplesAbout(conn, comp);

        // 3. If it has no parent (or no rdf:_n parent), stop here
        if (parentLink == null) {
            return;
        }

        Resource parent = parentLink.parent;

        // 4. Remove the parent -> comp ordinal link
        conn.remove(parent, parentLink.ordinalPred, comp);

        // 5. Collect remaining children of this parent (after removing comp)
        List<ChildInfo> children = getChildren(conn, parent);

        if (children.isEmpty()) {
            // If parent is a component and now has no children, delete it too
            if (conn.hasStatement(parent, RDF.TYPE, VF.createIRI(SparqlPrefix.LEXICOG.getUri(), "LexicographicComponent"), false)) {
                if (parent instanceof IRI) {
                    // Recursive deletion and rebalance one level up
                    deleteComponentRecursive(conn, (IRI) parent);
                } else {
                    // parent is a bnode: just remove its triples
                    deleteAllTriplesAbout(conn, parent);
                }
            }
            // If parent is an Entry, we don't delete it even if childless
            return;
        }

        // 6. Parent still has children -> rebalance its level
        ChildLevel level = determineChildLevel(conn, parent);
        rebalanceChildren(conn, parent, children, level);
    }

    private static ParentLink findParentLink(RepositoryConnection conn, IRI comp) {
        try ( RepositoryResult<Statement> stmts
                = conn.getStatements(null, null, comp, false)) {
            while (stmts.hasNext()) {
                Statement st = stmts.next();
                IRI pred = st.getPredicate();
                if (isOrdinalPredicate(pred)) {
                    Resource parent = st.getSubject();
                    int index = parseOrdinalIndex(pred);
                    return new ParentLink(parent, pred, index);
                }
            }
        }
        return null;
    }

    private static List<ChildInfo> getChildren(RepositoryConnection conn, Resource parent) {
        List<ChildInfo> children = new ArrayList<>();
        try ( RepositoryResult<Statement> stmts
                = conn.getStatements(parent, null, null, false)) {
            while (stmts.hasNext()) {
                Statement st = stmts.next();
                IRI pred = st.getPredicate();
                if (!isOrdinalPredicate(pred)) {
                    continue;
                }
                Value obj = st.getObject();
                if (!(obj instanceof IRI)) {
                    continue;
                }
                IRI child = (IRI) obj;

                if (!conn.hasStatement(child, RDF.TYPE, VF.createIRI(SparqlPrefix.LEXICOG.getUri(), "LexicographicComponent"), false)) {
                    continue;
                }

                int oldIndex = parseOrdinalIndex(pred);
                String oldLabel = null;
                try ( RepositoryResult<Statement> lbls
                        = conn.getStatements(child, RDFS.LABEL, null, false)) {
                    if (lbls.hasNext()) {
                        oldLabel = lbls.next().getObject().stringValue();
                    }
                }
                children.add(new ChildInfo(child, pred, oldIndex, oldLabel));
            }
        }
        children.sort(Comparator.comparingInt(c -> c.oldIndex));
        return children;
    }

    private static ChildLevel determineChildLevel(RepositoryConnection conn, Resource parent) {
        if (conn.hasStatement(parent, RDF.TYPE, VF.createIRI(SparqlPrefix.LEXICOG.getUri(), "Entry"), false)) {
            return ChildLevel.LEVEL1;
        }

        boolean hasComponentParent = false;
        boolean hasEntryParent = false;
        try ( RepositoryResult<Statement> stmts
                = conn.getStatements(null, null, parent, false)) {
            while (stmts.hasNext()) {
                Statement st = stmts.next();
                IRI pred = st.getPredicate();
                if (!isOrdinalPredicate(pred)) {
                    continue;
                }
                Resource gp = st.getSubject();
                if (conn.hasStatement(gp, RDF.TYPE, VF.createIRI(SparqlPrefix.LEXICOG.getUri(), "LexicographicComponent"), false)) {
                    hasComponentParent = true;
                }
                if (conn.hasStatement(gp, RDF.TYPE, VF.createIRI(SparqlPrefix.LEXICOG.getUri(), "Entry"), false)) {
                    hasEntryParent = true;
                }
            }
        }

        if (hasComponentParent) {
            return ChildLevel.LEVEL3;
        }
        if (hasEntryParent) {
            return ChildLevel.LEVEL2;
        }
        return ChildLevel.LEVEL1;
    }

    private static void rebalanceChildren(RepositoryConnection conn,
            Resource parent,
            List<ChildInfo> children,
            ChildLevel level) {

        String parentLabel = null;
        if (level == ChildLevel.LEVEL2 || level == ChildLevel.LEVEL3) {
            try ( RepositoryResult<Statement> lbls
                    = conn.getStatements(parent, RDFS.LABEL, null, false)) {
                if (lbls.hasNext()) {
                    parentLabel = lbls.next().getObject().stringValue();
                } else {
                    parentLabel = "";
                }
            }
        }

        int newIndex = 1;
        for (ChildInfo c : children) {
            IRI child = c.child;

            // remove old ordinal and label
            conn.remove(parent, c.oldPred, child);
            if (c.oldLabel != null) {
                conn.remove(child, RDFS.LABEL, VF.createLiteral(c.oldLabel));
            }

            IRI newPred = VF.createIRI(RDF.NAMESPACE + "_" + newIndex);
            String newLabel;
            switch (level) {
                case LEVEL1:
                    newLabel = toRoman(newIndex);
                    break;
                case LEVEL2:
                    newLabel = (parentLabel == null ? "" : parentLabel) + newIndex;
                    break;
                case LEVEL3:
                    char letter = (char) ('a' + (newIndex - 1));
                    newLabel = (parentLabel == null ? "" : parentLabel) + letter;
                    break;
                default:
                    newLabel = c.oldLabel != null ? c.oldLabel : "";
            }

            conn.add(parent, newPred, child);
            conn.add(child, RDFS.LABEL, VF.createLiteral(newLabel));

            newIndex++;
        }
    }

    private static boolean isOrdinalPredicate(IRI pred) {
        if (!RDF.NAMESPACE.equals(pred.getNamespace())) {
            return false;
        }
        String local = pred.getLocalName();
        if (!local.startsWith("_")) {
            return false;
        }
        try {
            Integer.parseInt(local.substring(1));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int parseOrdinalIndex(IRI pred) {
        String local = pred.getLocalName();
        if (local.startsWith("_")) {
            return Integer.parseInt(local.substring(1));
        }
        throw new IllegalArgumentException("Not an ordinal predicate: " + pred);
    }

    private static String toRoman(int n) {
        switch (n) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return Integer.toString(n); // extend if needed
            }
    }
}
