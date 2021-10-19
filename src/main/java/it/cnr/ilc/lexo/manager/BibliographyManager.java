/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.Bibliography;
import it.cnr.ilc.lexo.service.data.lexicon.input.LinguisticRelationUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.RelationDeleter;
import it.cnr.ilc.lexo.service.data.lexicon.output.BibliographicItem;
import it.cnr.ilc.lexo.sparql.SparqlDeleteData;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author andreabellandi
 */
public class BibliographyManager implements Manager, Cached {

    private final String idInstancePrefix = LexOProperties.getProperty("repository.instance.id");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

    @Override
    public void reloadCache() {
    }

    public BibliographicItem createBibliographyReference(String leID, String author, Bibliography bibliography) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String idBib = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String _idBib = idBib.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
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
                .replace("_CREATED_", created)
                .replace("_MODIFIED_", created)
                .replace("_OPTIONAL_", optional)
                .replace("_TITLE_", bibliography.getTitle())
                .replace("_DATE_", bibliography.getDate())
                .replace("_AUTHORS_", bibliography.getAuthor())
                .replaceAll("_LEID_", leID));
        return setBibliographicItem(_idBib, created, author, bibliography);
    }

    private BibliographicItem setBibliographicItem(String id, String created, String author, Bibliography bibliography) {
        BibliographicItem b = new BibliographicItem();
        b.setAuthor(bibliography.getAuthor());
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

}
