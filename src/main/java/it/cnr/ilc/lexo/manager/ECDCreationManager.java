/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalFunction;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDEntry;
import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryCore;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

    public DictionaryEntryCore createDictionaryEntry(String author, String prefix, String baseIRI, String desiredID, ECDEntry ecdEntry, String dictID, String lexiconID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.IDAlreadyExists(baseIRI + desiredID) ? null : desiredID) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) {
            throw new ManagerException("ID " + desiredID + " already exists");
        }
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String idLabel = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        String _id = baseIRI + idLabel;
        String lexicalEntryQuery = "";
        ArrayList<String> leids = new ArrayList();
        for (String pos : ecdEntry.getPos()) {
            Timestamp _tm = new Timestamp((new Timestamp(System.currentTimeMillis())).getTime() + (long)(Math.random() * 5));
            String leid = baseIRI + idInstancePrefix + _tm.toString().replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
            leids.add(leid);
            lexicalEntryQuery = lexicalEntryQuery + SparqlInsertData.CREATE_LEXICAL_ENTRY_FOR_ECD_ENTRY.replace("[ID]", leid)
                    .replace("[LEX]", lexiconID)
                    .replace("[TYPE]", ecdEntry.getType())
                    .replace("[POS]", pos)
                    .replace("[LABEL]", ecdEntry.getLabel())
                    .replace("[AUTHOR]", author)
                    .replace("[LANG]", ecdEntry.getLanguage())
                    .replace("[CREATED]", created)
                    .replace("[MODIFIED]", created);
        }
        String describes = "";
        for (String lexID : leids) {
            describes = describes + "<" + _id + "> " + "<" + SparqlPrefix.LEXICOG.getUri() + "describes> <" + lexID + "> . \n";
        }
        RDFQueryUtil.update(SparqlInsertData.CREATE_ECD_ENTRY.replace("[ID]", _id)
                .replace("[DICT]", dictID)
                .replace("[LABEL]", ecdEntry.getLabel())
                .replace("_PREFIX_", sparqlPrefix)
                .replace("[AUTHOR]", author)
                .replace("[LANG]", ecdEntry.getLanguage())
                .replace("[CREATED]", created)
                .replace("[MODIFIED]", created)
                .replace("_LEXICAL_ENTRY_DESCRIBES_", describes)
                .replace("_LEXICAL_ENTRY_", lexicalEntryQuery));
        return setDictionaryEntry(_id, created, author, ecdEntry);
    }

    private DictionaryEntryCore setDictionaryEntry(String id, String created, String author, ECDEntry ecdEntry) {
        ArrayList<String> types = new ArrayList<>();
        types.add("Entry");
        types.add(ecdEntry.getType().split("#")[1]);
        ArrayList<String> pos = new ArrayList<>();
        for (String _pos : ecdEntry.getPos()) {
            pos.add(_pos);
        }
        DictionaryEntryCore dec = new DictionaryEntryCore();
        dec.setHasChildren(false);
        dec.setAuthor(author);
        dec.setLanguage(ecdEntry.getLanguage());
        dec.setLabel(ecdEntry.getLabel());
        dec.setConfidence(-1);
        dec.setType(types);
        dec.setStatus("working");
        dec.setLastUpdate(created);
        dec.setCreationDate(created);
        dec.setDictionaryEntry(id);
        dec.setPos(pos);
        return dec;
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
