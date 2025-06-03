/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.manager.ECDCreationManager;
import it.cnr.ilc.lexo.manager.ECDDataManager;
import it.cnr.ilc.lexo.manager.Manager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.UtilityManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalFunction;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDEntry;
import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDLexicalFunction;
import it.cnr.ilc.lexo.service.helper.DictionaryEntryHelper;
import it.cnr.ilc.lexo.service.helper.ECDLexicalFunctionHelper;
import it.cnr.ilc.lexo.util.OntoLexEntity;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Level;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("ecd/create")
@Api("Explanatory Combinatorial Dictionary")
public class ECDCreation extends Service {

    private final ECDCreationManager ecdManager = ManagerFactory.getManager(ECDCreationManager.class);
    private final ECDDataManager ecdDataManager = ManagerFactory.getManager(ECDDataManager.class);
    private final ECDLexicalFunctionHelper ecdLexicalFunctionHelper = new ECDLexicalFunctionHelper();
    private final DictionaryEntryHelper ecdDictionaryEntryHelper = new DictionaryEntryHelper();

    @POST
    @Path("lexicalFunction")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "lexicalFunction",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical function instance creation",
            notes = "This method creates a new lexical function instance and returns it")
    public Response lexicalFunction(
            //            @ApiParam(
            //                    name = "id",
            //                    value = "the source lexical sense id",
            //                    required = true)
            //            @QueryParam("id") String id,
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the lexical function (if LexO user management disabled)",
                    example = "user7",
                    required = false)
            @QueryParam("author") String author,
            @ApiParam(
                    name = "desiredID",
                    value = "the ID name to assign to the created entity",
                    example = "idName",
                    required = false)
            @QueryParam("desiredID") String desiredID,
            LexicalFunction lexicalFunction,
            @ApiParam(
                    name = "prefix",
                    value = "prefix of the namespace",
                    example = "myprefix",
                    required = true)
            @QueryParam("prefix") String prefix,
            @ApiParam(
                    name = "baseIRI",
                    value = "base IRI of the entity",
                    example = "http://mydata.com#",
                    required = true)
            @QueryParam("baseIRI") String baseIRI) {

        try {
            checkKey(key);
            log(Level.INFO, "ecd/create/lexicalFunction with source " + lexicalFunction.getSource() + " and target " + lexicalFunction.getTarget());
            String _id = URLDecoder.decode(lexicalFunction.getSource(), StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                String lfID = ecdManager.createLexicalFunction(prefix, baseIRI, author, desiredID, lexicalFunction);
                log(Level.INFO, "Lexical function " + lfID + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                TupleQueryResult lfs = ecdDataManager.getLexicalFunctions(_id);
                String json = "";
                ECDLexicalFunction lf = null;
                for (ECDLexicalFunction _lf : ecdLexicalFunctionHelper.newDataList(lfs)) {
                    if (_lf.getId().equals(lfID)) {
                        lf = ecdDataManager.setECDLexicalFunction(_lf);
                    }
                }
                json = ecdLexicalFunctionHelper.toJson(lf);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/bibliography: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "create/bibliography: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/bibliography: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }

    }

    @POST
    @Path("ECDEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "ECDEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "ECD entry creation",
            notes = "This method creates a new dictionary entry and returns it")
    public Response ECDEntry(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the dictionary entry(if LexO user management disabled)",
                    example = "user7",
                    required = false)
            @QueryParam("author") String author,
            @ApiParam(
                    name = "desiredID",
                    value = "the ID name to assign to the created entity",
                    example = "idName",
                    required = false)
            @QueryParam("desiredID") String desiredID,
            @ApiParam(
                    name = "prefix",
                    value = "prefix of the namespace",
                    example = "myprefix",
                    required = true)
            @QueryParam("prefix") String prefix,
            @ApiParam(
                    name = "baseIRI",
                    value = "base IRI of the entity",
                    example = "http://mydata.com#",
                    required = true)
            @QueryParam("baseIRI") String baseIRI,
            ECDEntry ecdEntry) {
        try {
            checkKey(key);
            log(Level.INFO, "create/ECDEntry " + ecdEntry.getLabel() + " with type " + ecdEntry.getType());
            try {
                if (ecdEntry.getLabel() == null || ecdEntry.getLanguage() == null || ecdEntry.getType() == null
                        || ecdEntry.getPos() == null) {
                    log(Level.ERROR, "create/ECDEntry: missing parameter");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("create/ECDEntry: missing parameter").build();
                }
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                Manager.validateWithEnum("type", OntoLexEntity.LexicalEntryTypes.class, ecdEntry.getType());
                String dictID = utilityManager.getDictionaryIDByLanguage(ecdEntry.getLanguage());
                String lexiconID = utilityManager.getLexiconIDByLanguage(ecdEntry.getLanguage());
                if (dictID == null) {
                    log(Level.ERROR, "create/ECDEntry: dictionary language " + ecdEntry.getLanguage() + " does not exist");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("create/ECDEntry: dictionary language " + ecdEntry.getLanguage() + " does not exist").build();
                }
                if (lexiconID == null) {
                    log(Level.ERROR, "create/ECDEntry: lexicon language " + ecdEntry.getLanguage() + " does not exist");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("create/ECDEntry: lexicon language " + ecdEntry.getLanguage() + " does not exist").build();
                }
                DictionaryEntryCore dec = ecdManager.createDictionaryEntry(author, prefix, baseIRI, desiredID, ecdEntry, dictID, lexiconID);
                String json = ecdDictionaryEntryHelper.toJson(dec);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/bibliography: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/bibliography: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }

    }

}
