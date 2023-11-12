/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.manager.BibliographyManager;
import it.cnr.ilc.lexo.manager.LexiconCreationManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.SKOSManager;
import it.cnr.ilc.lexo.manager.UtilityManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.Bibliography;
import it.cnr.ilc.lexo.service.data.lexicon.output.BibliographicItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.Collocation;
import it.cnr.ilc.lexo.service.data.lexicon.output.Component;
import it.cnr.ilc.lexo.service.data.lexicon.output.ConceptSet;
import it.cnr.ilc.lexo.service.data.lexicon.output.Dictionary;
import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryComponent;
import it.cnr.ilc.lexo.service.data.lexicon.output.EtymologicalLink;
import it.cnr.ilc.lexo.service.data.lexicon.output.Etymology;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormRestriction;
import it.cnr.ilc.lexo.service.data.lexicon.output.Language;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalConcept;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.ReifiedRelation;
import it.cnr.ilc.lexo.service.data.lexicon.output.TranslationSet;
import it.cnr.ilc.lexo.service.helper.BibliographyHelper;
import it.cnr.ilc.lexo.service.helper.CollocationHelper;
import it.cnr.ilc.lexo.service.helper.ComponentHelper;
import it.cnr.ilc.lexo.service.helper.ConceptSetHelper;
import it.cnr.ilc.lexo.service.helper.DictionaryEntryComponentHelper;
import it.cnr.ilc.lexo.service.helper.DictionaryHelper;
import it.cnr.ilc.lexo.service.helper.EtymologicalLinkHelper;
import it.cnr.ilc.lexo.service.helper.EtymologyHelper;
import it.cnr.ilc.lexo.service.helper.FormCoreHelper;
import it.cnr.ilc.lexo.service.helper.LanguageHelper;
import it.cnr.ilc.lexo.service.helper.LexicalConceptHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryCoreHelper;
import it.cnr.ilc.lexo.service.helper.LexicalSenseCoreHelper;
import it.cnr.ilc.lexo.service.helper.DirectRelationHelper;
import it.cnr.ilc.lexo.service.helper.FormRestrictionHelper;
import it.cnr.ilc.lexo.service.helper.IndirectRelationHelper;
import it.cnr.ilc.lexo.service.helper.TranslationSetHelper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("lexicon/creation")
@Api("Lexicon creation")
public class LexiconCreation extends Service {

    private final LexiconCreationManager lexiconManager = ManagerFactory.getManager(LexiconCreationManager.class);

    private final BibliographyManager bibliographyManager = ManagerFactory.getManager(BibliographyManager.class);
    private final SKOSManager skosManager = ManagerFactory.getManager(SKOSManager.class);
    private final LexicalEntryCoreHelper lexicalEntryCoreHelper = new LexicalEntryCoreHelper();
    private final LanguageHelper languageHelper = new LanguageHelper();
    private final DictionaryHelper dictionaryHelper = new DictionaryHelper();
    private final FormCoreHelper formCoreHelper = new FormCoreHelper();
    private final EtymologyHelper etymologyHelper = new EtymologyHelper();
    private final EtymologicalLinkHelper etymologicalLinkHelper = new EtymologicalLinkHelper();
    private final BibliographyHelper bibliographyHelper = new BibliographyHelper();
    private final LexicalSenseCoreHelper lexicalSenseCoreHelper = new LexicalSenseCoreHelper();
    private final ComponentHelper componentHelper = new ComponentHelper();
    private final CollocationHelper collocationHelper = new CollocationHelper();
    private final FormRestrictionHelper formRestrictionHelper = new FormRestrictionHelper();
    private final IndirectRelationHelper indirectLexicalRelationHelper = new IndirectRelationHelper();
    private final TranslationSetHelper translationSetHelper = new TranslationSetHelper();
    private final LexicalConceptHelper lexicalConceptHelper = new LexicalConceptHelper();
    private final ConceptSetHelper conceptSetHelper = new ConceptSetHelper();
    private final DictionaryEntryComponentHelper dictionaryEntryComponentHelper = new DictionaryEntryComponentHelper();

    @GET
    @Path("dictionary")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "dictionary",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Dictionary creation",
            notes = "This method creates a new dictionary according to lexicog module")
    public Response dictionary(
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
            log(Level.INFO, "lexicon/creation/dictionary: lang=" + lang);
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            if (!isUniqueID(baseIRI + desiredID)) {
                log(Level.ERROR, "ID " + desiredID + " already exists");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
            }
            if (utilityManager.dictionaryLanguageExists(lang)) {
                log(Level.INFO, "Language label " + lang + " already exists");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Language label " + lang + " already exists").build();
            }
            Dictionary d = lexiconManager.createDictionary(prefix, baseIRI, author, lang);
            String json = dictionaryHelper.toJson(d);
            log(Level.INFO, "Dictionary for language " + lang + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "lexicon/creation/dictionary: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/dictionary: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("language")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "language",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Language creation",
            notes = "This method creates a new language and returns its id and some metadata")
    public Response language(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "lang",
                    value = "language code (2 or 3 digits)",
                    example = "en",
                    required = true)
            @QueryParam("lang") String lang,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the language (if LexO user management disabled)",
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/language: lang=" + lang);
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            if (!isUniqueID(baseIRI + desiredID)) {
                log(Level.ERROR, "ID " + desiredID + " already exists");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
            }
            if (utilityManager.languageExists(lang)) {
                log(Level.INFO, "Language label " + lang + " already exists");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Language label " + lang + " already exists").build();
            }
            Language l = lexiconManager.createLanguage(prefix, baseIRI, author, lang);
            String json = languageHelper.toJson(l);
            log(Level.INFO, "Language " + lang + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "lexicon/creation/language: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/language: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }

    }

    @GET
    @Path("lexicalEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry creation",
            notes = "This method creates a new lexical entry and returns its id and some metadata")
    public Response lexicalEntry(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the lexical entry (if LexO user management disabled)",
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/lexicalEntry");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            if (!isUniqueID(baseIRI + desiredID)) {
                log(Level.ERROR, "ID " + desiredID + " already exists");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
            }
            LexicalEntryCore lec = lexiconManager.createLexicalEntry(author, prefix, baseIRI);
            String json = lexicalEntryCoreHelper.toJson(lec);
            log(Level.INFO, "Lexical entry " + lec.getLabel() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "lexicon/creation/lexicalEntry: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/lexicalEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("dictionaryEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "dictionaryEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Dictionary entry creation",
            notes = "This method creates a new dictionary entry according to lexicog module, and returns its id and some metadata")
    public Response dictionaryEntry(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the lexical entry (if LexO user management disabled)",
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/dictionaryEntry");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            if (!isUniqueID(baseIRI + desiredID)) {
                log(Level.ERROR, "ID " + desiredID + " already exists");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
            }
            DictionaryEntryComponent dec = lexiconManager.createDictionaryEntry(author, prefix, baseIRI);
            String json = dictionaryEntryComponentHelper.toJson(dec);
            log(Level.INFO, "Dictionary entry " + dec.getLabel() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "lexicon/creation/dictionaryEntry: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/dictionaryEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("form")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "form",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Form creation",
            notes = "This method creates a new form and returns its id and some metadata")
    public Response form(
            @ApiParam(
                    name = "lexicalEntryID",
                    value = "the lexical entry id, the form belongs to",
                    required = true)
            @QueryParam("lexicalEntryID") String lexicalEntryID,
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the form (if LexO user management disabled)",
                    example = "user7")
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            log(Level.INFO, "lexicon/creation/form of the lexical entry: " + URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name()));
            checkKey(key);
//            if (checkPermissions(UserRightManager.Operation.WRITE)) {
            String _lexicalEntryID = URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!isUniqueID(baseIRI + desiredID)) {
                    log(Level.ERROR, "ID " + desiredID + " already exists");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
                }
                if (!utilityManager.isLexicalEntry(_lexicalEntryID)) {
                    log(Level.ERROR, "lexicon/creation/form: " + "IRI " + _lexicalEntryID + " does not exist");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _lexicalEntryID + " does not exist").build();
                }
                String lang = utilityManager.getLanguage(_lexicalEntryID);
                if (lang == null) {
                    log(Level.ERROR, "lexicon/creation/form: form cannot be created: the lexical entry language must be set first");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("form cannot be created: the lexical entry language must be set first").build();
                }
                FormCore fc = lexiconManager.createForm(_lexicalEntryID, getUser(author), lang, prefix, baseIRI);
                String json = formCoreHelper.toJson(fc);
                log(Level.INFO, "Form " + fc.getLabel() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "lexicon/creation/form: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
//            } else {
//                log(Level.ERROR, "lexicon/creation/form: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
//                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
//            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "lexicon/creation/form: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/form: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("lexicalSense")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalSense",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical sense creation",
            notes = "This method creates a new lexical sense and returns its id and some metadata")
    public Response lexicalSense(
            @ApiParam(
                    name = "lexicalEntryID",
                    value = "the lexical entry id, the lexical sense belongs to",
                    example = "MUStestNOUN",
                    required = true)
            @QueryParam("lexicalEntryID") String lexicalEntryID,
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the sense (if LexO user management disabled)",
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/lexicalSense of the lexical entry: " + URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name()));
            String _lexicalEntryID = URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!isUniqueID(baseIRI + desiredID)) {
                    log(Level.ERROR, "ID " + desiredID + " already exists");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
                }
                if (!utilityManager.isLexicalEntry(_lexicalEntryID)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _lexicalEntryID + " does not exist").build();
                }
                LexicalSenseCore sc = lexiconManager.createLexicalSense(_lexicalEntryID, author, prefix, baseIRI);
                String json = lexicalSenseCoreHelper.toJson(sc);
                log(Level.INFO, "Lexical sense " + sc.getSense() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "lexicon/creation/lexicalSense: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "lexicon/creation/lexicalSense: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/bibliography: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("component")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "component",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Multiword component creation",
            notes = "This method creates a new component of a multiword and returns its id and some metadata")
    public Response component(
            @ApiParam(
                    name = "id",
                    value = "the lexical entry or the component id, the component is being created belongs to",
                    required = true)
            @QueryParam("id") String id,
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the component (if LexO user management disabled)",
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/component of the lexical entry: " + URLDecoder.decode(id, StandardCharsets.UTF_8.name()));
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!isUniqueID(baseIRI + desiredID)) {
                    log(Level.ERROR, "ID " + desiredID + " already exists");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
                }
                if (!utilityManager.isLexicalEntryOrComponent(_id)) {
                    log(Level.ERROR, "lexicon/creation/component: " + "IRI " + _id + " is not neither a lexical entry nor a component");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " is not neither a lexical entry nor a component").build();
                }
                Component comp = lexiconManager.createComponent(_id, author, prefix, baseIRI);
                String json = componentHelper.toJson(comp);
                log(Level.INFO, "Component " + comp.getComponent() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "lexicon/creation/component: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "lexicon/creation/component: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/component: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("collocation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "collocation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Collocation creation",
            notes = "This method creates a new collocation by using the frac module of ontolex")
    public Response collocation(
            @ApiParam(
                    name = "id",
                    value = "the lexical entity that is the head of the collocation",
                    required = true)
            @QueryParam("id") String id,
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the component (if LexO user management disabled)",
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/collocation having the following lexical entity as head: " + URLDecoder.decode(id, StandardCharsets.UTF_8.name()));
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!isUniqueID(baseIRI + desiredID)) {
                    log(Level.ERROR, "ID " + desiredID + " already exists");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
                }
                if (!utilityManager.isAdmissibleHeadOfCollocation(_id)) {
                    log(Level.ERROR, "lexicon/creation/collocation: " + "IRI " + _id + " is not an admissible head of collocation");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " is not an admissible head of collocation").build();
                }
                Collocation col = lexiconManager.createCollocation(_id, author, prefix, baseIRI);
                String json = collocationHelper.toJson(col);
                log(Level.INFO, "Collocation " + col.getCollocation() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "lexicon/creation/collocation: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "lexicon/creation/collocation: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/collocation: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("etymology")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "etymology",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Etimology creation",
            notes = "This method creates a new etymology and returns its id and some metadata")
    public Response etymology(
            @ApiParam(
                    name = "lexicalEntryID",
                    value = "the lexical entry id, the etymology belongs to",
                    example = "MUStestNOUN",
                    required = true)
            @QueryParam("lexicalEntryID") String lexicalEntryID,
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the etymology (if LexO user management disabled)",
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/etymology of the lexical entry: " + URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name()));
            String _lexicalEntryID = URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!isUniqueID(baseIRI + desiredID)) {
                    log(Level.ERROR, "ID " + desiredID + " already exists");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
                }
                if (!utilityManager.isLexicalEntry(_lexicalEntryID)) {
                    log(Level.ERROR, "lexicon/creation/etymology: " + "IRI " + _lexicalEntryID + " does not exist");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _lexicalEntryID + " does not exist").build();
                }
                String label = utilityManager.getLabel(_lexicalEntryID);
                if (label == null) {
                    log(Level.ERROR, "lexicon/creation/etymology: " + "etymology cannot be created: the lexical entry label is not definied");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("etymology cannot be created: the lexical entry label is not definied").build();
                }
                Etymology e = lexiconManager.createEtymology(_lexicalEntryID, author, label, prefix, baseIRI);
                String json = etymologyHelper.toJson(e);
                log(Level.INFO, "Etymology " + e.getEtymology() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "lexicon/creation/etymology: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "lexicon/creation/etymology: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/etymology: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("etymologicalLink")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "etymologicalLink",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Etimological link creation",
            notes = "This method creates a new etymological link")
    public Response etymologicalLink(
            @ApiParam(
                    name = "lexicalEntryID",
                    value = "the lexical entry id, the etymology belongs to",
                    example = "MUStestNOUN",
                    required = true)
            @QueryParam("lexicalEntryID") String lexicalEntryID,
            @ApiParam(
                    name = "etymologyID",
                    value = "the etymology id of the lexical entry",
                    required = true)
            @QueryParam("etymologyID") String etymologyID,
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the etymological link (if LexO user management disabled)",
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/etymologicalLink of the lexical entry: " + URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name()));
            String _lexicalEntryID = URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!isUniqueID(baseIRI + desiredID)) {
                    log(Level.ERROR, "ID " + desiredID + " already exists");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
                }
                if (!utilityManager.isLexicalEntry(_lexicalEntryID)) {
                    log(Level.ERROR, "lexicon/creation/etymologicalLink: " + "IRI " + _lexicalEntryID + " does not exist");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _lexicalEntryID + " does not exist").build();
                }
                String label = utilityManager.getLabel(_lexicalEntryID);
                if (label == null) {
                    log(Level.ERROR, "lexicon/creation/etymologicalLink: " + "etymology cannot be created: the lexical entry label is not definied");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("etymology cannot be created: the lexical entry label is not definied").build();
                }
                String _etymologyID = URLDecoder.decode(etymologyID, StandardCharsets.UTF_8.name());
                if (!utilityManager.isEtymology(_etymologyID)) {
                    log(Level.ERROR, "lexicon/creation/etymologicalLink: " + "IRI " + _etymologyID + " does not exist");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _etymologyID + " does not exist").build();
                }
                EtymologicalLink el = lexiconManager.createEtymologicalLink(_lexicalEntryID, author, _etymologyID, prefix, baseIRI);
                String json = etymologicalLinkHelper.toJson(el);
                log(Level.INFO, "Etymological link " + el.getEtymologicalLink() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "lexicon/creation/etymologicalLink: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "lexicon/creation/etymologicalLink: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/etymologicalLink: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("bibliography")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "bibliography",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Bibliographic reference creation",
            notes = "This method creates a new bibliography for a lexical entity and returns it")
    public Response bibliography(
            @ApiParam(
                    name = "id",
                    value = "the lexical entity id, the bibliography refers to",
                    required = true)
            @QueryParam("id") String id,
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the bibliographic link (if LexO user management disabled)",
                    example = "user7",
                    required = false)
            @QueryParam("author") String author,
            @ApiParam(
                    name = "desiredID",
                    value = "the ID name to assign to the created entity",
                    example = "idName",
                    required = false)
            @QueryParam("desiredID") String desiredID,
            Bibliography bibliography,
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
            log(Level.INFO, "lexicon/creation/bibliography of the entity: " + URLDecoder.decode(id, StandardCharsets.UTF_8.name()));
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!isUniqueID(baseIRI + desiredID)) {
                    log(Level.ERROR, "ID " + desiredID + " already exists");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
                }
                if ((bibliography.getId() == null || bibliography.getId().isEmpty())) {
                    log(Level.ERROR, "lexicon/creation/bibliography: " + "id of bibliography must be defined");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("id of bibliography must be defined").build();
                }
                BibliographicItem bi = bibliographyManager.createBibliographyReference(_id, author, bibliography, prefix, baseIRI);
                String json = bibliographyHelper.toJson(bi);
                log(Level.INFO, "Bibliography " + bi.getBibliography() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "lexicon/creation/bibliography: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "lexicon/creation/bibliography: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/bibliography: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }

    }

    @GET
    @Path("lexicalConcept")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalConcept",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical Concept creation",
            notes = "This method creates a new lexical concept and returns its id and some metadata")
    public Response lexicalConcept(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the lexical concept (if LexO user management disabled)",
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/lexicalConcept");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            if (!isUniqueID(baseIRI + desiredID)) {
                log(Level.ERROR, "ID " + desiredID + " already exists");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
            }
            LexicalConcept lc = skosManager.createLexicalConcept(author, prefix, baseIRI);
            String json = lexicalConceptHelper.toJson(lc);
            log(Level.INFO, "Lexical concept " + lc.getLexicalConcept() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "lexicon/creation/lexicalConcept: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/lexicalConcept: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("conceptSet")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "conceptSet",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Concept Set creation",
            notes = "This method creates a new concept set and returns its id and some metadata")
    public Response conceptSet(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the concept set (if LexO user management disabled)",
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/conceptSet");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            if (!isUniqueID(baseIRI + desiredID)) {
                log(Level.ERROR, "ID " + desiredID + " already exists");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
            }
            ConceptSet cs = skosManager.createConceptSet(author, prefix, baseIRI);
            String json = conceptSetHelper.toJson(cs);
            log(Level.INFO, "Concept set " + cs.getConceptSet() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "lexicon/creation/conceptSet: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/bibliography: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("lexicoSemanticRelation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicoSemanticRelation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Creation of a reified Lexico-Semantic Relation",
            notes = "This method creates a reified Lexico-Semantic Relation according to the vartrans ontolex module")
    public Response lexicoSemanticRelation(
            @ApiParam(
                    name = "id",
                    value = "the lexical entity that is the source of the relation",
                    required = true)
            @QueryParam("id") String id,
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the relation (if LexO user management disabled)",
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
                    name = "type",
                    value = "the full IRI of the relation type",
                    example = "http://www.w3.org/ns/lemon/vartrans#LexicalRelation",
                    required = true)
            @QueryParam("type") String type,
            @ApiParam(
                    name = "prefix",
                    value = "prefix of the namespace",
                    example = "myprefix",
                    required = true)
            @QueryParam("prefix") String prefix,
            @ApiParam(
                    name = "baseIRI",
                    value = "base IRI of the relation",
                    example = "http://mydata.com#",
                    required = true)
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/lexicoSemanticRelation of the lexical entity: " + URLDecoder.decode(id, StandardCharsets.UTF_8.name()));
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            String _type = URLDecoder.decode(type, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!isUniqueID(baseIRI + desiredID)) {
                    log(Level.ERROR, "ID " + desiredID + " already exists");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
                }
                ReifiedRelation rr = lexiconManager.createLexicoSemanticRelation(_id, _type, author, prefix, baseIRI);
                String json = indirectLexicalRelationHelper.toJson(rr);
                log(Level.INFO, "lexicoSemanticRelation " + rr.getRelation() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "lexicon/creation/lexicoSemanticRelation: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "lexicon/creation/lexicoSemanticRelation: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/lexicoSemanticRelation: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("translationSet")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "translationSet",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Creation of a translation set",
            notes = "This method creates a translation set according to the vartrans ontolex module")
    public Response translationSet(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the translation set (if LexO user management disabled)",
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
                    value = "base IRI of the relation",
                    example = "http://mydata.com#",
                    required = true)
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/translationSet");
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!isUniqueID(baseIRI + desiredID)) {
                    log(Level.ERROR, "ID " + desiredID + " already exists");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
                }
                TranslationSet ts = lexiconManager.createTranslationSet(author, prefix, baseIRI);
                String json = translationSetHelper.toJson(ts);
                log(Level.INFO, "translationSet " + ts.getTranslationSet() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "lexicon/creation/translationSet: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/translationSet: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("formRestriction")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "formRestriction",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Creation of a form restriction",
            notes = "This method creates a form restriction according to the lexicog ontolex module")
    public Response formRestriction(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "the lexical sense to which the restriction is applied",
                    required = true)
            @QueryParam("id") String id,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the translation set (if LexO user management disabled)",
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
                    value = "base IRI of the relation",
                    example = "http://mydata.com#",
                    required = true)
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            log(Level.INFO, "lexicon/creation/formRestriction of the following lexical sense: " + URLDecoder.decode(id, StandardCharsets.UTF_8.name()));
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!isUniqueID(baseIRI + desiredID)) {
                    log(Level.ERROR, "ID " + desiredID + " already exists");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
                }
                FormRestriction fr = lexiconManager.createFormRestriction(_id, author, prefix, baseIRI);
                String json = formRestrictionHelper.toJson(fr);
                log(Level.INFO, "Form restriction " + fr.getFormRestriction() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "lexicon/creation/formRestriction: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "lexicon/creation/formRestriction: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/formRestriction: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }

    }

    @GET
    @Path("lexicographicComponent")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicographicComponent",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Dictionary entry component creation",
            notes = "This method creates a new component of a dictionary entry according to lexcog module, and returns its id and some metadata")
    public Response lexicographicComponent(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the component (if LexO user management disabled)",
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
            @QueryParam("baseIRI") String baseIRI) {
        try {
            checkKey(key);
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!isUniqueID(baseIRI + desiredID)) {
                    log(Level.ERROR, "ID " + desiredID + " already exists");
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("ID " + desiredID + " already exists").build();
                }
                DictionaryEntryComponent dec = lexiconManager.createDictionaryEntryComponent(author, prefix, baseIRI);
                String json = dictionaryEntryComponentHelper.toJson(dec);
                log(Level.INFO, "Lexicographic component " + dec.getComponent() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "lexicon/creation/lexicographicComponent: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/lexicographicComponent: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    private boolean isUniqueID(String desiredID) {
        UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
        return utilityManager.isUniqueID(desiredID);
    }

}
