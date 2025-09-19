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
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDForm;
import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDFormDetail;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDLexicalFunction;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDMeaning;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDictionary;
import it.cnr.ilc.lexo.service.helper.DictionaryEntryHelper;
import it.cnr.ilc.lexo.service.helper.ECDFormHelper;
import it.cnr.ilc.lexo.service.helper.ECDLexicalFunctionHelper;
import it.cnr.ilc.lexo.service.helper.ECDMeaningHelper;
import it.cnr.ilc.lexo.service.helper.ECDictionaryHelper;
import it.cnr.ilc.lexo.util.OntoLexEntity;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
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
    private final ECDMeaningHelper ecdMeaningHelper = new ECDMeaningHelper();
    private final ECDLexicalFunctionHelper ecdLexicalFunctionHelper = new ECDLexicalFunctionHelper();
    private final DictionaryEntryHelper ecdDictionaryEntryHelper = new DictionaryEntryHelper();
    private final ECDFormHelper ecdFormHelper = new ECDFormHelper();
    private final ECDictionaryHelper ecdDictionaryHelper = new ECDictionaryHelper();

    
    @GET
    @Path("ECDictionary")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "ECDictionary",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Explanatory Combinatorial Dictionary creation",
            notes = "This method creates a new EC Dictionary according to Melchuck theory")
    public Response ECDictionary(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "lang",
                    value = "language code (2 or 3 digits)",
                    example = "en",
                    required = true)
            @QueryParam("lang") String lang,
            @ApiParam(
                    name = "desiredID",
                    value = "the ID name to assign to the created entity",
                    example = "idName",
                    required = false)
            @QueryParam("desiredID") String desiredID,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the dictionary (if LexO user management disabled)",
                    example = "user7",
                    required = false)
            @QueryParam("author") String author,
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
            log(Level.INFO, "create/ECDictionary: lang=" + lang);
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            ECDictionary d = ecdManager.createECDictionary(author, prefix, baseIRI, desiredID, lang);
            String json = ecdDictionaryHelper.toJson(d);
            log(Level.INFO, "Explanatory Combinatorial Dictionary for language " + lang + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "create/ECDictionary: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/ECDictionary: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }
    
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
                log(Level.ERROR, "create/lexicalFunction: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "create/lexicalFunction: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/lexicalFunction: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
                log(Level.ERROR, "create/ECDEntry: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/ECDEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }

    }
    
    @POST
    @Path("ECDForm")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "ECDForm",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "ECD form creation",
            notes = "This method creates a new form of a ECD entry and returns it")
    public Response ECDForm(
            @ApiParam(
                    name = "ECDEntry",
                    value = "the ECD entry the form belongs to",
                    required = true)
            @QueryParam("lexicalEntryID") String ECDEntry,
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
            ECDForm ecdForm) {
        try {
            checkKey(key);
            log(Level.INFO, "create/ECDForm " + ecdForm.getLabel() + " with type " + ecdForm.getType());
            try {
                String ecdEntry = URLDecoder.decode(ECDEntry, StandardCharsets.UTF_8.name());
                if (ecdForm.getLabel() == null || ecdForm.getLanguage() == null || ecdForm.getType() == null
                        || ecdForm.getPos() == null) {
                    log(Level.ERROR, "create/ECDForm: missing parameter");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("create/ECDForm: missing parameter").build();
                }
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                Manager.validateWithEnum("type", OntoLexEntity.FormTypes.class, ecdForm.getType());
                String lang = utilityManager.getLanguage(ecdEntry);
                if (!ecdForm.getLanguage().equals(lang)) {
                    log(Level.ERROR, "create/ECDForm: wrong language parameter: " + ecdForm.getLanguage() + " instead of " + lang);
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("create/ECDForm: wrong language parameter: " + ecdForm.getLanguage() + " instead of " + lang).build();
                }
                Map<String, String> les = new HashMap();
                for (String _pos : ecdForm.getPos()) {
                    les.put(utilityManager.getLexicalEntryByECDPoS(ecdEntry, _pos), _pos);
                }
                List<ECDFormDetail> fl = ecdManager.createECDForm(author, prefix, baseIRI, desiredID, ecdForm, les);
                String json = ecdFormHelper.toJson(fl);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/ECDForm: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ECDCreation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/ECDForm: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }

    }

    @GET
    @Path("ECDMeaning")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "ECDMeaning",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "ECD meaning creation",
            notes = "This method creates a new dictionary entry meaning and returns it")
    public Response ECDMeaning(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the dictionary entry meaning (if LexO user management disabled)",
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
            @ApiParam(
                    name = "pos",
                    value = "the full IRI of the meaning part of speech",
                    required = true)
            @QueryParam("pos") String pos,
            @ApiParam(
                    name = "DictEntryID",
                    value = "the full IRI of the dictionary entry",
                    required = true)
            @QueryParam("DictEntryID") String DictEntryID) {
        try {
            checkKey(key);
            log(Level.INFO, "create/ECDMeaning with pos " + pos + " and dictionary entry " + DictEntryID);
            String json = "";
            try {
                if (DictEntryID == null || pos == null) {
                    log(Level.ERROR, "create/ECDMeaning: missing parameter");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("create/ECDMeaning: missing parameter").build();
                }
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);

                if (utilityManager.isDictionaryEntry(DictEntryID)) {
                    Map<String, Integer> meanings = utilityManager.getECDMeaningsCountOfEntity(DictEntryID, pos);
                    if (meanings != null) {
                        for (Map.Entry<String, Integer> entry : meanings.entrySet()) {
                            ECDMeaning meaning = ecdManager.createMeaning(author, prefix, baseIRI, desiredID, entry.getKey(), DictEntryID, entry.getValue() + 1, pos);
                            json = ecdMeaningHelper.toJson(meaning);
                        }
                    } else {
                        log(Level.ERROR, "create/ECDMeaning: " + DictEntryID + " has not the required pos " + pos);
                        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("create/ECDMeaning: " + DictEntryID + " has not the required pos " + pos).build();
                    }
                }
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/ECDMeaning: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/ECDMeaning: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }

    }

}
