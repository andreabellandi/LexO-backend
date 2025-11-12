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
import it.cnr.ilc.lexo.manager.ECDUpdateManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.UtilityManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDEntryUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDFormUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDMeaningOrdering;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDMeaningUpdater;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Level;
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
            @HeaderParam("Authorization") String key, 
            @ApiParam(
                    name = "id",
                    required = true)
            @QueryParam("id") String id,
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
            if (!utilityManager.isDictEntry(_id)) {
                log(Level.ERROR, "ecd/update/ECDEntry: " + _id + " is not a ECD Entry");
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("ecd/update/ECDEntry: " + _id + " is not a ECD Entry").build();
            }
            return Response.ok(ecdManager.updateECDEntry(id, ecdeu, author))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException | UnsupportedEncodingException ex) {
            log(Level.ERROR, "ecd/update/ECDEntry: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "ecd/update/ECDEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("ECDForm")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "ECDForm",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "ECD form update",
            notes = "This method updates the ECD form according to the input updater")
    public Response ECDForm(
            @HeaderParam("Authorization") String key, @QueryParam("id") String id,
            @ApiParam(
                    name = "author",
                    value = "if LexO user management is disabled, the account that is updating the status of the lexical entry",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            ECDFormUpdater ecdfu) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            if (!utilityManager.isForm(_id)) {
                log(Level.ERROR, "ecd/update/ECDForm: " + _id + " is not an ECD form");
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("ecd/update/ECDForm: " + _id + " is not an ECD form").build();
            }
            return Response.ok(ecdManager.updateECDForm(id, ecdfu, author))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException | UnsupportedEncodingException ex) {
            log(Level.ERROR, "ecd/update/ECDForm: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "ecd/update/ECDForm: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }
    
    @POST
    @Path("ECDMeaning")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "ECDMeaning",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "ECD meaning update",
            notes = "This method updates the ECD meaning according to the input updater")
    public Response ECDMeaning(
            @HeaderParam("Authorization") String key, @QueryParam("id") String id,
            @ApiParam(
                    name = "author",
                    value = "if LexO user management is disabled, the account that is updating the status of the lexical entry",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            ECDMeaningUpdater ecdmu) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            if (!utilityManager.isLexicalSense(_id)) {
                log(Level.ERROR, "ecd/update/ECDMeaning: " + _id + " is not an ECD meaning");
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("ecd/update/ECDMeaning: " + _id + " is not an ECD meaning").build();
            }
            return Response.ok(ecdManager.updateECDMeaning(id, ecdmu, author))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException | UnsupportedEncodingException ex) {
            log(Level.ERROR, "ecd/update/ECDMeaning: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "ecd/update/ECDMeaning: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }
    
//    @POST
//    @Path("ECDMeaningOrdering")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.POST,
//            value = "ECDMeaningOrdering",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "ECD meaning ordering update",
//            notes = "This method updates the ECD meaning ordering according to the input updater")
//    public Response ECDMeaningOrdering(
//            @HeaderParam("Authorization") String key, 
//            @ApiParam(
//                    value = "ECD entry id",
//                    required = true)
//            @QueryParam("id") String id,
//            @ApiParam(
//                    name = "author",
//                    value = "if LexO user management is disabled, the account that is updating the status of the lexical entry",
//                    example = "user7",
//                    required = true)
//            @QueryParam("author") String author,
//            ECDMeaningOrdering ecdmo) {
//        try {
//            checkKey(key);
//            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
//            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
//            if (!utilityManager.isDictionaryEntry(_id)) {
//                log(Level.ERROR, "ecd/update/ECDMeaningOrdering: " + _id + " is not an ECD entry");
//                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("ecd/update/ECDMeaningOrdering: " + _id + " is not an ECD entry").build();
//            }
//            
//            return Response.ok(ecdManager.updateECDEntry(id, ecdeu, author))
//                    .type(MediaType.TEXT_PLAIN)
//                    .header("Access-Control-Allow-Headers", "content-type")
//                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
//                    .build();
//        } catch (UpdateExecutionException | UnsupportedEncodingException ex) {
//            log(Level.ERROR, "ecd/update/ECDMeaningOrdering: " + ex.getMessage());
//            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
//        } catch (AuthorizationException | ServiceException ex) {
//            log(Level.ERROR, "ecd/update/ECDMeaningOrdering: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
//            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
//        }
//    }

}
