/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.Bibliography;
import it.cnr.ilc.lexo.service.data.lexicon.output.BibliographicItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.service.zotero.ZoteroClient;
import it.cnr.ilc.lexo.sparql.SparqlDeleteData;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import it.cnr.ilc.lexo.sparql.SparqlQueryUtil;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.sparql.SparqlUpdateData;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 */
public class BibliographyManager implements Manager, Cached {

    private final String idInstancePrefix = LexOProperties.getProperty("repository.instance.id");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));
//    private final ZoteroClient zoteroClient = ManagerFactory.getManager(ZoteroClient.class);

    @Override
    public void reloadCache() {
    }

    public BibliographicItem createBibliographyReference(String leID, String author, Bibliography bibliography, String prefix, String baseIRI, String desiredID) throws ManagerException {
        setBibliography(bibliography);
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String idBib = (desiredID != null ? (!desiredID.isEmpty() ? (Manager.IDAlreadyExists(baseIRI + desiredID) ? null : desiredID) : idInstancePrefix + tm.toString()) : idInstancePrefix + tm.toString());
        if (idBib == null) throw new ManagerException("ID " + desiredID + " already exists");
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _idBib = baseIRI + idBib.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        String optional = "";
        if (bibliography.getUrl() != null && !bibliography.getUrl().isEmpty()) {
            optional = "        dc:identifier \"" + bibliography.getUrl() + "\" ;\n";
        }
        if (bibliography.getSeeAlsoLink() != null && !bibliography.getSeeAlsoLink().isEmpty()) {
            optional = optional + "        rdfs:seeAlso \"" + bibliography.getSeeAlsoLink() + "\" ;\n";
        }
        RDFQueryUtil.update(SparqlInsertData.CREATE_BIBLIOGRAPHIC_REFERENCE
                .replaceAll("_ID_", _idBib)
                .replace("_KEY_", bibliography.getId())
                .replace("_AUTHOR_", author)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created)
                .replace("_OPTIONAL_", optional)
                .replace("_TITLE_", bibliography.getTitle())
                .replace("_DATE_", bibliography.getDate())
                .replace("_AUTHORS_", bibliography.getAuthor())
                .replaceAll("_LEID_", leID));
        return setBibliographicItem(_idBib, created, author, bibliography);
    }

    private void setBibliography(Bibliography bibliography) {
        if (bibliography.getAuthor() == null || bibliography.getAuthor().isEmpty()) {
            bibliography.setAuthor("Not available");
        }
        if (bibliography.getTitle() == null || bibliography.getTitle().isEmpty()) {
            bibliography.setTitle("Not available");
        }
        if (bibliography.getDate() == null || bibliography.getDate().isEmpty()) {
            bibliography.setDate("Not available");
        }
    }

    private BibliographicItem setBibliographicItem(String id, String created, String author, Bibliography bibliography) {
        BibliographicItem b = new BibliographicItem();
        b.setAuthor(bibliography.getAuthor());
        b.setConfidence(-1);
//        b.setBibliography(SparqlPrefix.LEXBIB.getUri() + id);
        b.setBibliography(id);
//        b.setBibliographyInstanceName(id);
        b.setDate(bibliography.getDate());
        b.setId(bibliography.getId());
        b.setSeeAlsoLink((bibliography.getSeeAlsoLink() == null || bibliography.getSeeAlsoLink().isEmpty()) ? "" : bibliography.getSeeAlsoLink());
        b.setTextualReference("");
        b.setTitle(bibliography.getTitle());
        b.setUrl((bibliography.getUrl() == null || bibliography.getUrl().isEmpty()) ? "" : bibliography.getUrl());
        b.setCreator(author);
        b.setLastUpdate(created);
        b.setCreationDate(created);
        b.setNote("");
        return b;
    }

    public TupleQueryResult getBibliography(String id) {
        if (id != null) {
            String query = SparqlSelectData.LEXICAL_ENTITY_BIBLIOGRAPHY.replaceAll("_ID_", id);
            return RDFQueryUtil.evaluateTQuery(query);
        } else {
            String query = SparqlSelectData.BIBLIOGRAPHY_LIST;
            return RDFQueryUtil.evaluateTQuery(query);
        }
    }
    
     public TupleQueryResult getLexicalEntitiesByBibliography(String id) {
            String query = SparqlSelectData.LEXICAL_ENTITIES_BY_BIBLIOGRAPHY.replaceAll("_ID_", id);
            return RDFQueryUtil.evaluateTQuery(query);
    }

    public String deleteBibliography(String id) throws ManagerException {
        RDFQueryUtil.update(SparqlDeleteData.DELETE_BIBLIOGRAPHY.replaceAll("_ID_", id));
        return timestampFormat.format(new Timestamp(System.currentTimeMillis()));

    }

    public String getBibliographyID(String leID, String itemKey) {
        UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
        return utilityManager.bibliographyById(leID, itemKey);
    }

    public String synchronizeBibliography(String bibID, String itemKey) throws RuntimeException {
        String lastUpdate = "";
        ZoteroClient zc = new ZoteroClient();
        zc.setUrl(itemKey);
        zc.setConn();
        try {
            JsonNode bib = zc.getItem();
            lastUpdate = updateBibliography(bibID, bib);
            zc.disconnect();
        } catch (RuntimeException e) {
            throw e;
        }
        return lastUpdate;
    }

    private String updateBibliography(String bibID, JsonNode bib) {
        String title = (bib.get("data") != null) ? (bib.get("data").get("title") != null
                ? (!bib.get("data").get("title").textValue().isEmpty() ? bib.get("data").get("title").textValue() : "Not available") : "Not available") : "Not available";
        String date = (bib.get("data") != null) ? (bib.get("data").get("date") != null
                ? (!bib.get("data").get("date").textValue().isEmpty() ? bib.get("data").get("date").textValue() : "Not available") : "Not available") : "Not available";
        String author = getContributor(bib.get("data") != null ? bib.get("data") : null);
        String lastupdate = timestampFormat.format(new Timestamp(System.currentTimeMillis()));
        RDFQueryUtil.update(SparqlUpdateData.SYNC_BIBLIOGRAPHY.replaceAll("_IDBIB_", bibID)
                .replaceAll("_TITLE_", title)
                .replaceAll("_DATE_", date)
                .replaceAll("_CONTRIBUTOR_", author)
                .replaceAll("_LAST_UPDATE_", "\"" + lastupdate + "\""));
        return lastupdate;
    }

    private static String getContributor(JsonNode contributors) {
        String contributor = "";
        if (contributors.get("creators") != null) {
            ArrayNode arrayNode = (ArrayNode) contributors.get("creators");
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode arrayElement = arrayNode.get(i);
                Iterator<Map.Entry<String, JsonNode>> iter = arrayElement.fields();
                boolean author = false;
                while (iter.hasNext()) {
                    Map.Entry<String, JsonNode> entry = iter.next();
                    if (entry.getKey().equals("creatorType")) {
                        if (entry.getValue().textValue().equals("author")) {
                            author = true;
                        } else {
                            author = false;
                        }
                    } else {
                        if (author) {
                            if (entry.getKey().equals("lastName")) {
                                if (!entry.getValue().textValue().isEmpty()) {
                                    contributor = contributor.isEmpty() ? contributor + entry.getValue().textValue() : contributor + ", " + entry.getValue().textValue();
                                }
                            }
                        }
                    }
                }
            }
        }
        return contributor.isEmpty() ? "Not available" : contributor;
    }

}
