/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalFunction;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author andreabellandi
 */
public class ECDCreationManager implements Manager, Cached {

    private final String idInstancePrefix = LexOProperties.getProperty("repository.instance.id");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String createLexicalFunction(String prefix, String baseIRI, String author, String desiredID, LexicalFunction lf) throws ManagerException {
        if (lf.getType() == null || lf.getLexicalFunction() == null || lf.getSource() == null || lf.getTarget() == null
                || lf.getType().isEmpty() || lf.getLexicalFunction().isEmpty() || lf.getSource().isEmpty() || lf.getTarget().isEmpty()) {
            throw new ManagerException("Input data incomplete");
        }
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.IDAlreadyExists(baseIRI + desiredID) ? null : desiredID) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) {
            throw new ManagerException("ID " + desiredID + " already exists");
        }
        String created = timestampFormat.format(tm);
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        RDFQueryUtil.update(SparqlInsertData.CREATE_LEXICAL_FUNCTION.replace("_ID_", _id)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_TYPE_", lf.getType())
                .replace("_LF_", lf.getLexicalFunction())
                .replace("_SOURCE_", lf.getSource())
                .replace("_TARGET_", lf.getTarget())
                .replace("_FUSED_", "false")
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created));
        return _id;
    }

}
