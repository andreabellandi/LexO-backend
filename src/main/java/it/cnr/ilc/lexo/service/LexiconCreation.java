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
import it.cnr.ilc.lexo.manager.LexiconUpdateManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.SKOSManager;
import it.cnr.ilc.lexo.manager.UtilityManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.Bibliography;
import it.cnr.ilc.lexo.service.data.lexicon.output.BibliographicItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.Collocation;
import it.cnr.ilc.lexo.service.data.lexicon.output.Component;
import it.cnr.ilc.lexo.service.data.lexicon.output.ConceptSet;
import it.cnr.ilc.lexo.service.data.lexicon.output.CorpusFrequency;
import it.cnr.ilc.lexo.service.data.lexicon.output.Dictionary;
import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.EtymologicalLink;
import it.cnr.ilc.lexo.service.data.lexicon.output.Etymology;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormRestriction;
import it.cnr.ilc.lexo.service.data.lexicon.output.Language;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalConcept;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicographicComponent;
import it.cnr.ilc.lexo.service.data.lexicon.output.ReifiedRelation;
import it.cnr.ilc.lexo.service.data.lexicon.output.TranslationSet;
import it.cnr.ilc.lexo.service.helper.BibliographyHelper;
import it.cnr.ilc.lexo.service.helper.CollocationHelper;
import it.cnr.ilc.lexo.service.helper.ComponentHelper;
import it.cnr.ilc.lexo.service.helper.ConceptSetHelper;
import it.cnr.ilc.lexo.service.helper.CorpusFrequencyHelper;
import it.cnr.ilc.lexo.service.helper.DictionaryEntryHelper;
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
import it.cnr.ilc.lexo.service.helper.IndirectLexicalRelationHelper;
import it.cnr.ilc.lexo.service.helper.LexicographicComponentHelper;
import it.cnr.ilc.lexo.service.helper.TranslationSetHelper;
import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
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
@Path("create")
@Api("Creation")
public class LexiconCreation extends Service {

    private final LexiconCreationManager lexiconManager = ManagerFactory.getManager(LexiconCreationManager.class);
    private final LexiconUpdateManager lexiconUpdateManager = ManagerFactory.getManager(LexiconUpdateManager.class);

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
    private final CorpusFrequencyHelper corpusFrequencyHelper = new CorpusFrequencyHelper();
    private final FormRestrictionHelper formRestrictionHelper = new FormRestrictionHelper();
    private final IndirectLexicalRelationHelper indirectLexicalRelationHelper = new IndirectLexicalRelationHelper();
    private final TranslationSetHelper translationSetHelper = new TranslationSetHelper();
    private final LexicalConceptHelper lexicalConceptHelper = new LexicalConceptHelper();
    private final ConceptSetHelper conceptSetHelper = new ConceptSetHelper();
    private final LexicographicComponentHelper lexicographicComponentHelper = new LexicographicComponentHelper();
    private final DictionaryEntryHelper dictionaryEntryHelper = new DictionaryEntryHelper();

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
            log(Level.INFO, "create/dictionary: lang=" + lang);
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            if (utilityManager.dictionaryLanguageExists(lang)) {
                log(Level.INFO, "Language label " + lang + " already exists");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Language label " + lang + " already exists").build();
            }
            Dictionary d = lexiconManager.createDictionary(prefix, baseIRI, getUser(author), lang, desiredID);
            String json = dictionaryHelper.toJson(d);
            log(Level.INFO, "Dictionary for language " + lang + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "create/dictionary: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/dictionary: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("lexicon")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicon",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexicon language creation",
            notes = "This method creates a new lexicon language and returns its id and some metadata")
    public Response lexicon(
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
            log(Level.INFO, "create/lexicon/language: lang=" + lang);
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            if (utilityManager.languageExists(lang)) {
                log(Level.INFO, "Language label " + lang + " already exists");
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Language label " + lang + " already exists").build();
            }
            Language l = lexiconManager.createLanguage(prefix, baseIRI, author, lang, desiredID);
            String json = languageHelper.toJson(l);
            log(Level.INFO, "Language " + lang + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "create/lexicon/language: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/lexicon/language: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/lexicalEntry");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            LexicalEntryCore lec = lexiconManager.createLexicalEntry(author, prefix, baseIRI, desiredID);
            String json = lexicalEntryCoreHelper.toJson(lec);
            log(Level.INFO, "Lexical entry " + lec.getLabel() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "create/lexicalEntry: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/lexicalEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/dictionaryEntry");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            DictionaryEntryCore dec = lexiconManager.createDictionaryEntry(author, prefix, baseIRI, desiredID);
            String json = dictionaryEntryHelper.toJson(dec);
            log(Level.INFO, "Dictionary entry " + dec.getLabel() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "create/dictionaryEntry: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/dictionaryEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/form of the lexical entry: " + URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name()));
            checkKey(key);
//            if (checkPermissions(UserRightManager.Operation.WRITE)) {
            String _lexicalEntryID = URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!utilityManager.isLexicalEntry(_lexicalEntryID)) {
                    log(Level.ERROR, "create/form: " + "IRI " + _lexicalEntryID + " does not exist");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _lexicalEntryID + " does not exist").build();
                }
                String lang = utilityManager.getLanguage(_lexicalEntryID);
                if (lang == null) {
                    log(Level.ERROR, "create/form: form cannot be created: the lexical entry language must be set first");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("form cannot be created: the lexical entry language must be set first").build();
                }
                FormCore fc = lexiconManager.createForm(_lexicalEntryID, getUser(author), lang, prefix, baseIRI, desiredID);
                String json = formCoreHelper.toJson(fc);
                log(Level.INFO, "Form " + fc.getLabel() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/form: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
//            } else {
//                log(Level.ERROR, "create/form: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
//                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
//            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "create/form: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/form: " + ex.getMessage());
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
            log(Level.INFO, "create/lexicalSense of the lexical entry: " + URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name()));
            String _lexicalEntryID = URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!utilityManager.isLexicalEntry(_lexicalEntryID)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _lexicalEntryID + " does not exist").build();
                }
                LexicalSenseCore sc = lexiconManager.createLexicalSense(_lexicalEntryID, author, prefix, baseIRI, desiredID);
                String json = lexicalSenseCoreHelper.toJson(sc);
                log(Level.INFO, "Lexical sense " + sc.getSense() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                // check if updating sense ordering (for dictionary management purposes) is needed
                ArrayList<String> de = utilityManager.getDictionaryEntryByLexicalEntry(_lexicalEntryID);
                if (!de.isEmpty()) {
                    // lexical entry is associated with a dictionary entry
                    int senseNumber = utilityManager.getNumberOfOrderedSenses(_lexicalEntryID);
                    LexicographicComponent lc = lexiconManager.createLexicographicComponent(author, prefix, baseIRI, desiredID);
                    lexiconUpdateManager.addLexicographicComponentOfSense(_lexicalEntryID, sc.getSense(), lc.getComponent(), senseNumber + 1);
                }
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/lexicalSense: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "create/lexicalSense: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/bibliography: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/component of the lexical entry: " + URLDecoder.decode(id, StandardCharsets.UTF_8.name()));
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!utilityManager.isLexicalEntryOrComponent(_id)) {
                    log(Level.ERROR, "create/component: " + "IRI " + _id + " is not neither a lexical entry nor a component");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " is not neither a lexical entry nor a component").build();
                }
                Component comp = lexiconManager.createComponent(_id, author, prefix, baseIRI, desiredID);
                String json = componentHelper.toJson(comp);
                log(Level.INFO, "Component " + comp.getComponent() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/component: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "create/component: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/component: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/collocation having the following lexical entity as head: " + URLDecoder.decode(id, StandardCharsets.UTF_8.name()));
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!utilityManager.isAdmissibleHeadOfCollocation(_id)) {
                    log(Level.ERROR, "create/collocation: " + "IRI " + _id + " is not an admissible head of collocation");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " is not an admissible head of collocation").build();
                }
                Collocation col = lexiconManager.createCollocation(_id, author, prefix, baseIRI, desiredID);
                String json = collocationHelper.toJson(col);
                log(Level.INFO, "Collocation " + col.getCollocation() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/collocation: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "create/collocation: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/collocation: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("corpusFrequency")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "corpusFrequency",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Corpus frequency creation",
            notes = "This method creates a new corpus frequency by using the frac module of ontolex")
    public Response corpusFrequency(
            @ApiParam(
                    name = "id",
                    value = "the lexical entity having a frequency in a corpus",
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
            log(Level.INFO, "create/corpusFrequency creates a corpus frequency of the lexical entity: " + URLDecoder.decode(id, StandardCharsets.UTF_8.name()));
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!utilityManager.exists(id)) {
                    log(Level.ERROR, "create/corpusFrequency: " + "IRI " + _id + " is not a lexical entity");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " is not a lexical entity").build();
                }
                CorpusFrequency cf = lexiconManager.createCorpusFrequency(_id, author, prefix, baseIRI, desiredID);
                String json = corpusFrequencyHelper.toJson(cf);
                log(Level.INFO, "CorpusFrequency " + cf.getCorpusFrequency() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/corpusFrequency: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "create/corpusFrequency: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/corpusFrequency: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/etymology of the lexical entry: " + URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name()));
            String _lexicalEntryID = URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!utilityManager.isLexicalEntry(_lexicalEntryID)) {
                    log(Level.ERROR, "create/etymology: " + "IRI " + _lexicalEntryID + " does not exist");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _lexicalEntryID + " does not exist").build();
                }
                String label = utilityManager.getLabel(_lexicalEntryID);
                if (label == null) {
                    log(Level.ERROR, "create/etymology: " + "etymology cannot be created: the lexical entry label is not definied");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("etymology cannot be created: the lexical entry label is not definied").build();
                }
                Etymology e = lexiconManager.createEtymology(_lexicalEntryID, author, label, prefix, baseIRI, desiredID);
                String json = etymologyHelper.toJson(e);
                log(Level.INFO, "Etymology " + e.getEtymology() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/etymology: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "create/etymology: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/etymology: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/etymologicalLink of the lexical entry: " + URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name()));
            String _lexicalEntryID = URLDecoder.decode(lexicalEntryID, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if (!utilityManager.isLexicalEntry(_lexicalEntryID)) {
                    log(Level.ERROR, "create/etymologicalLink: " + "IRI " + _lexicalEntryID + " does not exist");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _lexicalEntryID + " does not exist").build();
                }
                String label = utilityManager.getLabel(_lexicalEntryID);
                if (label == null) {
                    log(Level.ERROR, "create/etymologicalLink: " + "etymology cannot be created: the lexical entry label is not definied");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("etymology cannot be created: the lexical entry label is not definied").build();
                }
                String _etymologyID = URLDecoder.decode(etymologyID, StandardCharsets.UTF_8.name());
                if (!utilityManager.isEtymology(_etymologyID)) {
                    log(Level.ERROR, "create/etymologicalLink: " + "IRI " + _etymologyID + " does not exist");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _etymologyID + " does not exist").build();
                }
                EtymologicalLink el = lexiconManager.createEtymologicalLink(_lexicalEntryID, author, _etymologyID, prefix, baseIRI, desiredID);
                String json = etymologicalLinkHelper.toJson(el);
                log(Level.INFO, "Etymological link " + el.getEtymologicalLink() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/etymologicalLink: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "create/etymologicalLink: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/etymologicalLink: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/bibliography of the entity: " + URLDecoder.decode(id, StandardCharsets.UTF_8.name()));
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                if ((bibliography.getId() == null || bibliography.getId().isEmpty())) {
                    log(Level.ERROR, "create/bibliography: " + "id of bibliography must be defined");
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("id of bibliography must be defined").build();
                }
                BibliographicItem bi = bibliographyManager.createBibliographyReference(_id, author, bibliography, prefix, baseIRI, desiredID);
                String json = bibliographyHelper.toJson(bi);
                log(Level.INFO, "Bibliography " + bi.getBibliography() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
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
            log(Level.INFO, "create/lexicalConcept");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            LexicalConcept lc = skosManager.createLexicalConcept(author, prefix, baseIRI, desiredID);
            String json = lexicalConceptHelper.toJson(lc);
            log(Level.INFO, "Lexical concept " + lc.getLexicalConcept() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "create/lexicalConcept: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/lexicalConcept: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/conceptSet");
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            utilityManager.validateNamespace(prefix, baseIRI);
            ConceptSet cs = skosManager.createConceptSet(author, prefix, baseIRI, desiredID);
            String json = conceptSetHelper.toJson(cs);
            log(Level.INFO, "Concept set " + cs.getConceptSet() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "create/conceptSet: " + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/bibliography: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/lexicoSemanticRelation of the lexical entity: " + URLDecoder.decode(id, StandardCharsets.UTF_8.name()));
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            String _type = URLDecoder.decode(type, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);
                ReifiedRelation rr = lexiconManager.createLexicoSemanticRelation(_id, _type, author, prefix, baseIRI, desiredID);
                String json = indirectLexicalRelationHelper.toJson(rr);
                log(Level.INFO, "lexicoSemanticRelation " + rr.getRelation() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/lexicoSemanticRelation: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "create/lexicoSemanticRelation: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/lexicoSemanticRelation: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/translationSet");
            try {
                TranslationSet ts = lexiconManager.createTranslationSet(author, prefix, baseIRI, desiredID);
                String json = translationSetHelper.toJson(ts);
                log(Level.INFO, "translationSet " + ts.getTranslationSet() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/translationSet: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/translationSet: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.INFO, "create/formRestriction of the following lexical sense: " + URLDecoder.decode(id, StandardCharsets.UTF_8.name()));
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                utilityManager.validateNamespace(prefix, baseIRI);

                FormRestriction fr = lexiconManager.createFormRestriction(_id, author, prefix, baseIRI, desiredID);
                String json = formRestrictionHelper.toJson(fr);
                log(Level.INFO, "Form restriction " + fr.getFormRestriction() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/formRestriction: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            log(Level.ERROR, "create/formRestriction: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/formRestriction: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
    @ApiOperation(value = "Lexicographic component creation",
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
                LexicographicComponent dec = lexiconManager.createLexicographicComponent(author, prefix, baseIRI, desiredID);
                String json = lexicographicComponentHelper.toJson(dec);
                log(Level.INFO, "Lexicographic component " + dec.getComponent() + " created (prefix=" + prefix + " baseIRI=" + baseIRI);
                return Response.ok(json)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/lexicographicComponent: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/lexicographicComponent: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("lexicographicAssociation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicographicAssociation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexicographic association creation",
            notes = "This method associates a lexical entry to a dictionary entry and it returns the lexicographic component that represents the association")
    public Response lexicographicAssociation(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the association (if LexO user management disabled)",
                    example = "user7",
                    required = false)
            @QueryParam("author") String author,
            @ApiParam(
                    name = "dictionaryEntryID",
                    value = "the dictionary entry ID to which the lexical entry has to be associated with",
                    required = true)
            @QueryParam("dictionaryEntryID") String dictionaryEntryID,
            @ApiParam(
                    name = "lexicalEntryID",
                    value = "prefthe lexical entry ID to associate to the dictionary entry",
                    required = true)
            @QueryParam("lexicalEntryID") String lexicalEntryID,
            @ApiParam(
                    name = "prefix",
                    value = "prefix of the namespace of the entity that represents the association",
                    example = "myprefix",
                    required = true)
            @QueryParam("prefix") String prefix,
            @ApiParam(
                    name = "baseIRI",
                    value = "base IRI of the entity that represents the association",
                    example = "http://mydata.com#",
                    required = true)
            @QueryParam("baseIRI") String baseIRI,
            @ApiParam(
                    name = "position",
                    value = "lexical entry position within the dictionary entry",
                    example = "1",
                    required = true)
            @QueryParam("position") int position) {
        try {
            checkKey(key);
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                // duplicated association check 
                if (utilityManager.isLexicalEntryJustAssociatedToDictionaryEntry(dictionaryEntryID, lexicalEntryID)) {
                    return Response.status(Response.Status.CONFLICT).type(MediaType.TEXT_PLAIN).entity("The required association already exists").build();
                }
                // required position check 
                if (position != 0) {
                    if (utilityManager.existsGenericRelation(dictionaryEntryID, SparqlPrefix.RDF.getUri() + "_" + position, "?x")) {
                        return Response.status(Response.Status.CONFLICT).type(MediaType.TEXT_PLAIN).entity(
                                dictionaryEntryID + " has just a component in position #" + position + ". Please, first remove it").build();
                    }
                } else {
                    return Response.status(Response.Status.CONFLICT).type(MediaType.TEXT_PLAIN).entity("Position cannot be set to 0").build();
                }
                if (!utilityManager.isDictEntry(dictionaryEntryID)) {
                    return Response.status(Response.Status.CONFLICT).type(MediaType.TEXT_PLAIN).entity(dictionaryEntryID + " is not a dictionary entry").build();
                }
                if (!utilityManager.isLexicalEntry(lexicalEntryID)) {
                    return Response.status(Response.Status.CONFLICT).type(MediaType.TEXT_PLAIN).entity(lexicalEntryID + " is not a lexical entry").build();
                }
                // check if lexical entry has some senses. If yes, they have been put in an ordered list
                int sensePosition = 1;
                for (Map.Entry<String, String> entry : utilityManager.getLexicalSensesByLexicalEntry(lexicalEntryID).entrySet()) {
                    lexiconManager.createFlatOrderedSenseList(entry.getValue(), prefix, baseIRI, lexicalEntryID, entry.getKey(), sensePosition);
                    sensePosition = sensePosition + 1;
                }
                LexicographicComponent lc = lexiconManager.createLexicographicAssociation(author, prefix, baseIRI, dictionaryEntryID, lexicalEntryID, position);
                log(Level.INFO, "Lexicographic association created (dictEntry=" + dictionaryEntryID + " lexEntry=" + lexicalEntryID + " in position " + position);
                return Response.ok(lc)
                        .type(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                log(Level.ERROR, "create/lexicographicAssociation: " + ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "create/lexicographicAssociation: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

}
