/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.manager.ECDDataManager;
import it.cnr.ilc.lexo.manager.ECDDeletionManager;
import it.cnr.ilc.lexo.manager.ECDUpdateManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.UtilityManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.DictionaryEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.DictionaryEntryUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.RelationDeleter;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDEntryUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDComponent;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDEntryItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDEntrySemantics;
import it.cnr.ilc.lexo.service.data.lexicon.output.HitsDataList;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDEntryMorphology;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDLexicalFunction;
import it.cnr.ilc.lexo.service.helper.ECDComponentHelper;
import it.cnr.ilc.lexo.service.helper.ECDEntryFilterHelper;
import it.cnr.ilc.lexo.service.helper.ECDEntryMorphologyHelper;
import it.cnr.ilc.lexo.service.helper.ECDEntrySemanticsHelper;
import it.cnr.ilc.lexo.service.helper.ECDLexicalFunctionHelper;
import it.cnr.ilc.lexo.service.helper.HelperException;
import it.cnr.ilc.lexo.util.LogUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Level;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("ecd/update")
@Api("Explanatory Combinatorial Dictionary")
public class ECDUpdate extends Service {

    private static final Logger logger = LoggerFactory.getLogger(ECDUpdate.class);

    private final ECDUpdateManager ecdManager = ManagerFactory.getManager(ECDUpdateManager.class);

    private void userCheck(String key) throws AuthorizationException, ServiceException {
        if (LexOProperties.getProperty("keycloack.freeViewer") != null) {
            if (!LexOProperties.getProperty("keycloack.freeViewer").equals("true")) {
                checkKey(key);
            }
        }
    }

    @POST
    @Path("ECDEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "ECDEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "ECD entry update",
            notes = "This method updates the ECD entry according to the input updater")
    public Response ECDEntry(
            @HeaderParam("Authorization") String key, @QueryParam("id") String id,
            @ApiParam(
                    name = "author",
                    value = "if LexO user management is disabled, the account that is updating the status of the lexical entry",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            ECDEntryUpdater ecdeu) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            return Response.ok(ecdManager.updateECDEntry(id, ecdeu, author))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException | UnsupportedEncodingException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/dictionaryEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }
    
}
