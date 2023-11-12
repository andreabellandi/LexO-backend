/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.output.Collocation;
import it.cnr.ilc.lexo.service.data.lexicon.output.Component;
import it.cnr.ilc.lexo.service.data.lexicon.output.Dictionary;
import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryComponent;
import it.cnr.ilc.lexo.service.data.lexicon.output.EtymologicalLink;
import it.cnr.ilc.lexo.service.data.lexicon.output.Etymology;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormRestriction;
import it.cnr.ilc.lexo.service.data.lexicon.output.Language;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.Property;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.ReifiedRelation;
import it.cnr.ilc.lexo.service.data.lexicon.output.TranslationSet;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.util.OntoLexEntity;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
public class LexiconCreationManager implements Manager, Cached {

    private final String idInstancePrefix = LexOProperties.getProperty("repository.instance.id");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Dictionary createDictionary(String prefix, String baseIRI, String author, String lang, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        RDFQueryUtil.update(SparqlInsertData.CREATE_DICTIONARY.replace("_ID_", _id)
                .replace("_LANG_", lang)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created));
        return setDictionary(_id, created, author, lang);
    }

    private Dictionary setDictionary(String id, String created, String author, String lang) {
        Dictionary d = new Dictionary();
        d.setCreator(author);
        d.setConfidence(-1);
        d.setLanguage(id);
        d.setLabel(lang);
        d.setLastUpdate(created);
        d.setCreationDate(created);
        d.setEntries(0);
        return d;
    }

    public Language createLanguage(String prefix, String baseIRI, String author, String lang, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_LEXICON_LANGUAGE.replace("_ID_", _id)
                .replace("_LANG_", lang)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created));
        return setLanguage(_id, created, author, lang);
    }

    private Language setLanguage(String id, String created, String author, String lang) {
        Language l = new Language();
        ArrayList<String> cat = new ArrayList();
        cat.add("http://www.lexinfo.net/ontology/3.0/lexinfo#");
        l.setCreator(author);
        l.setConfidence(-1);
        l.setLanguage(id);
        l.setLabel(lang);
        l.setCatalog(cat);
        l.setLastUpdate(created);
        l.setCreationDate(created);
        l.setEntries(0);
        return l;
    }

    public LexicalEntryCore createLexicalEntry(String author, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String idLabel = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        String _id = baseIRI + idLabel;
        RDFQueryUtil.update(SparqlInsertData.CREATE_LEXICAL_ENTRY.replace("[ID]", _id)
                .replace("[LABEL]", idLabel)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("[AUTHOR]", author)
                .replace("[CREATED]", created)
                .replace("[MODIFIED]", created));
        return setLexicalEntry(_id, idLabel, created, author);
    }

    public DictionaryEntryComponent createDictionaryEntry(String author, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String idLabel = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        String _id = baseIRI + idLabel;
        RDFQueryUtil.update(SparqlInsertData.CREATE_DICTIONARY_ENTRY.replace("[ID]", _id)
                .replace("[LABEL]", idLabel)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("[AUTHOR]", author)
                .replace("[CREATED]", created)
                .replace("[MODIFIED]", created));
        return setDictionaryEntry(_id, idLabel, created);
    }

    private LexicalEntryCore setLexicalEntry(String id, String label, String created, String author) {
        ArrayList<String> types = new ArrayList<>();
        types.add("LexicalEntry");
        LexicalEntryCore lec = new LexicalEntryCore();
        lec.setAuthor(author);
        lec.setLabel(label);
        lec.setConfidence(-1);
        lec.setType(types);
        lec.setLexicalEntry(id);
        lec.setStatus("working");
        lec.setLastUpdate(created);
        lec.setCreationDate(created);
        return lec;
    }

    private DictionaryEntryComponent setDictionaryEntry(String id, String label, String created) {
        DictionaryEntryComponent dec = new DictionaryEntryComponent();
        dec.setLabel(label);
        dec.setConfidence(-1);
        dec.setComponent(id);
        dec.setLastUpdate(created);
        dec.setCreationDate(created);
        return dec;
    }

    public FormCore createForm(String leID, String author, String lang, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String idLabel = id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        String _id = baseIRI + idLabel;
        RDFQueryUtil.update(SparqlInsertData.CREATE_FORM.replaceAll("_ID_", _id)
                .replace("_LABEL_", idLabel)
                .replace("_LANG_", lang)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created)
                .replaceAll("_LEID_", leID));
        return setForm(_id, idLabel, created, author);
    }

    private FormCore setForm(String id, String label, String created, String author) {
        List<Property> pl = new ArrayList();
        Property p = new Property("writtenRep", label);
        pl.add(p);
        FormCore fc = new FormCore();
        fc.setCreator(author);
        fc.setLabel(pl);
        fc.setConfidence(-1);
        fc.setType("lexicalForm");
        fc.setForm(id);
        fc.setLastUpdate(created);
        fc.setCreationDate(created);
        return fc;
    }

    public Etymology createEtymology(String leID, String author, String label, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_ETYMOLOGY.replaceAll("_ID_", _id)
                .replace("_LABEL_", label)
                .replace("_AUTHOR_", author)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created)
                .replaceAll("_LEID_", leID));
        return setEtymology(_id, created, author, label);
    }

    private Etymology setEtymology(String id, String created, String author, String label) {
        Etymology e = new Etymology();
        e.setCreator(author);
        e.setConfidence(-1);
        e.setLabel("Etymology of " + label);
        e.setEtymology(id);
        e.setLastUpdate(created);
        e.setCreationDate(created);
        return e;
    }

    public EtymologicalLink createEtymologicalLink(String leID, String author, String etymologyID, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_ETYMOLOGICAL_LINK.replaceAll("_ID_", _id)
                .replace("_ETID_", etymologyID)
                .replace("_AUTHOR_", author)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created)
                .replaceAll("_LEID_", leID));
        return setEtymologicalLink(_id, created, author, leID);
    }

    private EtymologicalLink setEtymologicalLink(String id, String created, String author, String leID) {
        EtymologicalLink el = new EtymologicalLink();
        el.setEtyLinkType("inheritance");
//        el.setEtyTargetInstanceName(leID);
        el.setConfidence(-1);
        el.setEtyTarget(leID);
        el.setEtymologicalLink(id);
//        el.setEtymologicalLinkInstanceName(id);
        el.setCreator(author);
        el.setLastUpdate(created);
        el.setCreationDate(created);
        return el;
    }

    public LexicalSenseCore createLexicalSense(String leID, String author, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_LEXICAL_SENSE.replaceAll("_ID_", _id)
                .replace("_AUTHOR_", author)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created)
                .replaceAll("_LEID_", leID));
        return setSense(_id, created, author);
    }

    public Component createComponent(String leID, String author, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_COMPONENT.replaceAll("_ID_", _id)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_LEID_", leID)
                .replace("_MODIFIED_", created));
        return setComponent(_id, created, author);
    }

    public DictionaryEntryComponent createDictionaryEntryComponent(String author, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_DICTIONARY_ENTRY_COMPONENT.replaceAll("_ID_", _id)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_MODIFIED_", created));
        return setDictionaryEntryComponent(_id, created, author);
    }

    public Collocation createCollocation(String leID, String author, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_COLLOCATION.replaceAll("_ID_", _id)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_LEID_", leID)
                .replace("_MODIFIED_", created));
        return setCollocation(_id, leID, created, author);
    }

    public ReifiedRelation createLexicoSemanticRelation(String leID, String type, String author, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Manager.validateWithEnum("type", OntoLexEntity.VartransRelationClasses.class, type);
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_LEXICOSEMANTIC_RELATION.replaceAll("_ID_", _id)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_LABEL_", type)
                .replace("_LEID_", leID)
                .replace("_TYPE_", type)
                .replace("_MODIFIED_", created));
        return setLexicoSemanticRelation(_id, created, author, type, leID);
    }

    public TranslationSet createTranslationSet(String author, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_TRANSLATIONSET.replaceAll("_ID_", _id)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_LABEL_", _id)
                .replace("_MODIFIED_", created));
        return setTranslationSet(_id, created, author);
    }

    private TranslationSet setTranslationSet(String id, String created, String author) {
        TranslationSet ts = new TranslationSet();
        ts.setCreator(author);
        ts.setTranslationSet(id);
        ts.setLabel(id);
        ts.setConfidence(-1);
        ts.setLastUpdate(created);
        ts.setCreationDate(created);
        return ts;
    }

    private ReifiedRelation setLexicoSemanticRelation(String id, String created, String author, String type, String leID) {
        ReifiedRelation rr = new ReifiedRelation();
        rr.setCreator(author);
        rr.setConfidence(-1);
        rr.setRelation(id);
        rr.setSource(leID);
        rr.setType(type);
        rr.setLabel(type);
        rr.setLastUpdate(created);
        rr.setCreationDate(created);
        rr.setCategory(null);
        return rr;
    }

    private LexicalSenseCore setSense(String id, String created, String author) {
        LexicalSenseCore sc = new LexicalSenseCore();
        sc.setCreator(author);
        sc.setConfidence(-1);
        sc.setSense(id);
        sc.setLastUpdate(created);
        sc.setCreationDate(created);
        return sc;
    }

    private Component setComponent(String id, String created, String author) {
        Component sc = new Component();
        sc.setCreator(author);
        sc.setConfidence(-1);
        sc.setPosition(-1);
        sc.setComponent(id);
        sc.setLastUpdate(created);
        sc.setCreationDate(created);
        return sc;
    }

    private DictionaryEntryComponent setDictionaryEntryComponent(String id, String created, String author) {
        DictionaryEntryComponent dec = new DictionaryEntryComponent();
        dec.setCreator(author);
        dec.setConfidence(-1);
        dec.setComponent(id);
        dec.setLastUpdate(created);
        dec.setCreationDate(created);
        dec.setComponent(id);
        return dec;
    }

    private Collocation setCollocation(String id, String leID, String created, String author) {
        Collocation col = new Collocation();
        col.setCreator(author);
        col.setConfidence(-1);
        col.setCollocation(id);
        col.setHead(leID);
        col.setLastUpdate(created);
        col.setCreationDate(created);
        return col;
    }

    public FormRestriction createFormRestriction(String lexID, String author, String prefix, String baseIRI, String desiredID) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.getID(baseIRI + desiredID) ? baseIRI + desiredID : null) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (id == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_FORMRESTRICTION.replaceAll("_ID_", _id)
                .replace("_LEXID_", lexID)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_LABEL_", _id)
                .replace("_MODIFIED_", created));
        return setFormRestriction(_id, created, author);
    }

    private FormRestriction setFormRestriction(String id, String created, String author) {
        FormRestriction fr = new FormRestriction();
        fr.setCreator(author);
        fr.setConfidence(-1);
        fr.setFormRestriction(id);
        fr.setLastUpdate(created);
        fr.setCreationDate(created);
        return fr;
    }

}
