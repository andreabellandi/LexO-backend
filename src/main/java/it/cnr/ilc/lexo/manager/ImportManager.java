/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.conll.SPARQLWriter;
import it.cnr.ilc.lexo.service.data.lexicon.output.ImageDetail;
import it.cnr.ilc.lexo.service.data.lexicon.output.ImportDetail;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author andreabellandi
 */
public class ImportManager implements Manager, Cached {

    private final String idInstancePrefix = LexOProperties.getProperty("repository.instance.id");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ImageDetail createImage(String author, String prefix, String baseIRI, String urlImage, String format, String leid) throws ManagerException {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        String id = idInstancePrefix + tm.toString();
        String created = timestampFormat.format(tm);
        String sparqlPrefix = "PREFIX " + prefix + ": <" + baseIRI + ">";
        String _id = baseIRI + id.replaceAll("\\s+", "").replaceAll(":", "_").replaceAll("\\.", "_");
        RDFQueryUtil.update(SparqlInsertData.CREATE_IMAGE_REFERENCE.replace("_ID_", _id)
                .replace("_IMAGEURL_", _id)
                .replace("_PREFIX_", sparqlPrefix)
                .replace("_FORMAT_", format)
                .replace("_AUTHOR_", author)
                .replace("_CREATED_", created)
                .replace("_LEID_", leid)
                .replace("_MODIFIED_", created));
        return setImageDetail(_id, created, author, urlImage, format, leid);
    }

    private ImageDetail setImageDetail(String id, String created, String author, String imageUrl, String format, String leid) {
        ImageDetail imgd = new ImageDetail();
        imgd.setPublisher(author);
        imgd.setId(id);
        imgd.setConfidence(-1);
        imgd.setType("image");
        imgd.setLexicalEntityIRI(leid);
        imgd.setFormat("image/" + format);
        imgd.setUrl(imageUrl);
        imgd.setLastUpdate(created);
        imgd.setCreationDate(created);
        return imgd;
    }
    
    public ImportDetail uploadStatements(String statements, SPARQLWriter sparql, Boolean drop) throws Exception {
        if (drop) {
            RDFQueryUtil.update("CLEAR DEFAULT");
        }
        String[] chunks = statements.split(SPARQLWriter.SEPARATOR, 0);
        int n = 0;
        for (String chunk: chunks) {
//            System.err.print(String.format("\rPosting... %.0f%%", ++n * 100.0/chunks.length));
            RDFQueryUtil.update(chunk);
        }
        return setImportDetail(sparql);
    }
    
    private ImportDetail setImportDetail(SPARQLWriter sparql) {
        ImportDetail id = new ImportDetail();
        id.setNumberOfTriples(sparql.getNumberOfTriples());
        return id;
    }

}
