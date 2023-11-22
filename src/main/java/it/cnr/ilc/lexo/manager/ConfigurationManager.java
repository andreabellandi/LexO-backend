/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import com.github.jsonldjava.shaded.com.google.common.io.Files;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.RepositoryData;
import it.cnr.ilc.lexo.sparql.SparqlIndex;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.sparql.SparqlRepositoryConfiguration;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

/**
 *
 * @author andreabellandi
 */
public class ConfigurationManager implements Manager, Cached {

    private final DomainManager domainManager = ManagerFactory.getDomainManager();
    private final String repoName = LexOProperties.getProperty("GraphDb.repository");

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Response restCall(String restUrl, String restType, RepositoryData rd) throws URISyntaxException, IOException, ManagerException {
        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target(restUrl);
        switch (restType) {
            case "GET": {
                return wt.request().get();
            }
            case "POST": {
                if (rd == null) {
                    return wt.request().post(null);
                } else {
                    File file = getAttachedFile(rd);
                    try {
                        MultiPart multiPart = new MultiPart();
//                    multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
                        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", file, MediaType.TEXT_PLAIN_TYPE);
                        multiPart.bodyPart(fileDataBodyPart);
                        return wt.register(MultiPartFeature.class)
                                .request(MediaType.APPLICATION_JSON_TYPE)
                                .post(Entity.entity(multiPart, multiPart.getMediaType()));
                    } finally {
                        file.delete();
                    }
                }
            }
            case "PUT": {

            }
            case "DELETE": {
                return wt.request().delete();
            }
            default:
        }
        return null;
    }

    private File getAttachedFile(RepositoryData rd) throws ManagerException, IOException {
        validateRuleset(rd.getRuleset());
        String content = SparqlRepositoryConfiguration.REPOSITORY_CONFIGURATION.replace("_REPO_ID_", rd.getRepoID())
                .replace("_REPO_LABEL_", rd.getLabelID())
                .replace("_BASE_URL_", rd.getBaseUrl())
                .replace("_RULESET_", rd.getRuleset());
//        InputStream stream = new ByteArrayInputStream(file.getBytes(StandardCharsets.UTF_8));
        File file = new File("pippo.ttl");
        Files.write(content.getBytes(), file);
        return file;
    }

    public void validateRuleset(String rs) throws ManagerException {
        Manager.validateWithEnum("rel", EnumUtil.RulesetRepositoryConfiguration.class, rs);
    }

    
    private void deleteIndex(ArrayList<String> connectors, String connector) {
        if (connectors.contains(connector)) {
                RDFQueryUtil.update(SparqlIndex.DELETE_INDEX.replace("_INDEX_NAME_", connector));
            }
    }
    
    public void updateIndex(String repo) {
        ArrayList<String> connectors = ManagerFactory.getManager(UtilityManager.class).getConnectors();
        if (!connectors.isEmpty()) {
            deleteIndex(connectors, SparqlVariable.LEXICAL_ENTRY_INDEX);
            deleteIndex(connectors, SparqlVariable.FORM_INDEX);
            deleteIndex(connectors, SparqlVariable.LEXICAL_SENSE_INDEX);
            deleteIndex(connectors, SparqlVariable.LEXICAL_CONCEPT_INDEX);
            deleteIndex(connectors, SparqlVariable.ETYMOLOGY_INDEX);
            deleteIndex(connectors, SparqlVariable.CONCEPT_REFERENCE_INDEX);
            deleteIndex(connectors, SparqlVariable.COMPONENT_INDEX);
            deleteIndex(connectors, SparqlVariable.CONCEPT_SET_INDEX);
        }
        RDFQueryUtil.update(SparqlIndex.LEXICAL_ENTRY_INDEX);
        RDFQueryUtil.update(SparqlIndex.FORM_INDEX);
        RDFQueryUtil.update(SparqlIndex.LEXICAL_SENSE_INDEX);
        RDFQueryUtil.update(SparqlIndex.COMPONENT_INDEX);
        RDFQueryUtil.update(SparqlIndex.CONCEPT_REFERENCE_INDEX);
        RDFQueryUtil.update(SparqlIndex.CONCEPT_SET_INDEX);
        RDFQueryUtil.update(SparqlIndex.ETYMOLOGY_INDEX);
        RDFQueryUtil.update(SparqlIndex.LEXICAL_CONCEPT_INDEX);

    }
}
