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
import it.cnr.ilc.lexo.manager.ECDDeletionManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.UtilityManager;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("ecd/delete")
@Api("Explanatory Combinatorial Dictionary")
public class ECDDeletion extends Service {

    private static final Logger logger = LoggerFactory.getLogger(ECDDeletion.class);

    private final ECDDeletionManager ecdManager = ManagerFactory.getManager(ECDDeletionManager.class);

    private void userCheck(String key) throws AuthorizationException, ServiceException {
        if (LexOProperties.getProperty("keycloack.freeViewer") != null) {
            if (!LexOProperties.getProperty("keycloack.freeViewer").equals("true")) {
                checkKey(key);
            }
        }
    }

    @GET
    @Path("lexicalFunction")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalFunction",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical function deletion",
            notes = "This method deletes a lexical function relation")
    public Response lexicalFunction(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "lexical function ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            log(Level.INFO, "ecd/delete/lexicalFunction <" + _id + ">");
            return Response.ok(ecdManager.deleteRelation(_id))
                    .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException | ManagerException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "ecd/delete/lexicalFunction: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("ECDForm")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "ECDForm",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "ECD form deletion",
            notes = "This method deletes a form of a ECD entry")
    public Response ECDForm(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "form IRI",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            log(Level.INFO, "ecd/delete/ECDForm <" + _id + ">");
            return Response.ok(ecdManager.deleteECDForm(_id))
                    .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException | ManagerException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "ecd/delete/ECDForm: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("ECDEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "ECDEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "ECD entry deletion",
            notes = "This method deletes an ECD entry")
    public Response ECDEntry(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "ECD entry IRI",
                    required = true)
            @QueryParam("id") String id,
            @ApiParam(
                    name = "force",
                    value = "true or false depending on the entry must be deleted also if it has some components",
                    required = false)
            @QueryParam("force") Boolean force) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            log(Level.INFO, "ecd/delete/ECDEntry <" + _id + ">");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            if (force == null || !force) {
                if (utilityManager.hasECDEntryComponents(_id)) {
                    log(Level.ERROR, "ecd/delete/ECDEntry: Entry cannot be deleted because is not empty");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ecd/delete/ECDEntry: Entry cannot be deleted because is not empty").build();
                }
            }
            return Response.ok(ecdManager.deleteECDEntry(_id))
                    .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException | ManagerException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "ecd/delete/ECDEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("ECDEntryPoS")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "ECDEntryPoS",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "ECD entry pos deletion",
            notes = "This method deletes an ECD entry pos")
    public Response ECDEntryPoS(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "ECD entry IRI",
                    required = true)
            @QueryParam("id") String id,
            @ApiParam(
                    name = "pos",
                    value = "ECD entry part of speech",
                    required = true)
            @QueryParam("pos") String pos) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            log(Level.INFO, "ecd/delete/ECDEntryPoS <" + _id + ">");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            String le = utilityManager.getLexicalEntryByECDPoS(_id, pos);
            if (le == null) {
                log(Level.ERROR, "ecd/delete/ECDEntryPoS: " + _id + " has not " + pos + " as part of speech");
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("ecd/delete/ECDEntryPoS: " + _id + " has not " + pos + " as part of speech").build();
            }
            if (utilityManager.hasLexicalEntryFormsOrSenses(le)) {
                log(Level.ERROR, "ecd/delete/ECDEntryPoS: " + _id + " has associated some forms and/or senses. Delete them first");
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("ecd/delete/ECDEntryPoS: " + _id + " has associated some forms and/or senses. Delete them first").build();
            }
            return Response.ok(ecdManager.deleteECDEntryPos(le))
                    .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException | ManagerException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "ecd/delete/ECDEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("ECDictionary")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "ECDictionary",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "EC Dictionary deletion",
            notes = "This method deletes an EC Dictionary")
    public Response ECDictionary(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "EC Dictionary IRI",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            log(Level.INFO, "ecd/delete/ECDictionary <" + _id + ">");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            if (utilityManager.dictionaryHasEntry(_id)) {
                log(Level.ERROR, "ecd/delete/ECDictionary: Entry cannot be deleted because is not empty");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ecd/delete/ECDictionary: Dictionary cannot be deleted because is not empty").build();
            }
            return Response.ok(ecdManager.deleteECDictionary(_id))
                    .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException | ManagerException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "ecd/delete/ECDictionary: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("ECDMeaning")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "ECDMeaning",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "ECD meaning deletion",
            notes = "This method deletes an ECD meaning")
    public Response ECDMeaning(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "idECDEntry",
                    value = "EC Dictionary IRI",
                    required = true)
            @QueryParam("idECDEntry") String idECDEntry,
            @ApiParam(
                    name = "idECDMeaning",
                    value = "ECD meaning IRI",
                    required = true)
            @QueryParam("idECDMeaning") String idECDMeaning) {
        try {
            checkKey(key);
            String _idECDEntry = URLDecoder.decode(idECDEntry, StandardCharsets.UTF_8.name());
            String _idECDMeaning = URLDecoder.decode(idECDMeaning, StandardCharsets.UTF_8.name());
            log(Level.INFO, "ecd/delete/ECDMeaning:  ECD entry: <" + _idECDEntry + "> - ECD meaning: <" + _idECDMeaning + ">");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            if (!utilityManager.isDictionaryEntry(_idECDEntry)) {
                log(Level.ERROR, "ecd/delete/ECDMeaning: <" + _idECDEntry + "> is not a dictionary entry");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ecd/delete/ECDMeaning: <" + _idECDEntry + "> is not a dictionary entry").build();
            }
            if (!utilityManager.isLexicalSense(_idECDMeaning)) {
                log(Level.ERROR, "ecd/delete/ECDMeaning: <" + _idECDMeaning + "> is not a dictionary meaning");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ecd/delete/ECDMeaning: <" + _idECDMeaning + "> is not a dictionary meaning").build();
            }
            return Response.ok(ecdManager.deleteECDMeaning(_idECDEntry, _idECDMeaning))
                    .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException | ManagerException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "ecd/delete/ECDMeaning: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

}
