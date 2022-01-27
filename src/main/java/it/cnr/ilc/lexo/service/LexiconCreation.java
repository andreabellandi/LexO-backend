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
import it.cnr.ilc.lexo.manager.UtilityManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.Bibliography;
import it.cnr.ilc.lexo.service.data.lexicon.output.BibliographicItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.EtymologicalLink;
import it.cnr.ilc.lexo.service.data.lexicon.output.Etymology;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.Language;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseCore;
import it.cnr.ilc.lexo.service.helper.BibliographyHelper;
import it.cnr.ilc.lexo.service.helper.EtymologicalLinkHelper;
import it.cnr.ilc.lexo.service.helper.EtymologyHelper;
import it.cnr.ilc.lexo.service.helper.FormCoreHelper;
import it.cnr.ilc.lexo.service.helper.LanguageHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryCoreHelper;
import it.cnr.ilc.lexo.service.helper.LexicalSenseCoreHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    private final LexicalEntryCoreHelper lexicalEntryCoreHelper = new LexicalEntryCoreHelper();
    private final LanguageHelper languageHelper = new LanguageHelper();
    private final FormCoreHelper formCoreHelper = new FormCoreHelper();
    private final EtymologyHelper etymologyHelper = new EtymologyHelper();
    private final EtymologicalLinkHelper etymologicalLinkHelper = new EtymologicalLinkHelper();
    private final BibliographyHelper bibliographyHelper = new BibliographyHelper();
    private final LexicalSenseCoreHelper lexicalSenseCoreHelper = new LexicalSenseCoreHelper();

    @GET
    @Path("language")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "language",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Language creation",
            notes = "This method creates a new language and returns its id and some metadata")
    public Response lexicalEntry(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "lang",
                    value = "language code (2 or 3 digits)",
                    example = "en",
                    required = true)
            @QueryParam("lang") String lang,
            @ApiParam(
                    name = "author",
                    value = "the account is being creating the language",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author) {
        if (key.equals("PRINitant19")) {
            try {
                //        log(Level.INFO, "get lexicon entries types");
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (utilityManager.languageExists(lang)) {
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Language label " + lang + " already exists").build();
                }
                Language l = lexiconManager.createLanguage(author, lang);
                String json = languageHelper.toJson(l);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                Logger.getLogger(LexiconCreation.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
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
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "author",
                    value = "the account is being creating the lexical entry",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author) {
        if (key.equals("PRINitant19")) {
            try {
                //        log(Level.INFO, "get lexicon entries types");
                LexicalEntryCore lec = lexiconManager.createLexicalEntry(author);
                String json = lexicalEntryCoreHelper.toJson(lec);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                Logger.getLogger(LexiconCreation.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
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
                    example = "MUStestNOUN",
                    required = true)
            @QueryParam("lexicalEntryID") String lexicalEntryID,
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "author",
                    value = "the account is being creating the form",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author) {
        if (key.equals("PRINitant19")) {
            try {
                //        log(Level.INFO, "get lexicon entries types");
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexicalEntry(lexicalEntryID)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + lexicalEntryID + " does not exist").build();
                }
                String lang = utilityManager.getLanguage(lexicalEntryID);
                if (lang == null) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("form cannot be created: the lexical entry language must be set first").build();
                }
                FormCore fc = lexiconManager.createForm(lexicalEntryID, author, lang);
                String json = formCoreHelper.toJson(fc);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                Logger.getLogger(LexiconCreation.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
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
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "author",
                    value = "the account is being creating the sense",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author) {
        if (key.equals("PRINitant19")) {
            try {
                //        log(Level.INFO, "get lexicon entries types");
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexicalEntry(lexicalEntryID)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + lexicalEntryID + " does not exist").build();
                }
                LexicalSenseCore sc = lexiconManager.createLexicalSense(lexicalEntryID, author);
                String json = lexicalSenseCoreHelper.toJson(sc);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                Logger.getLogger(LexiconCreation.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
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
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "author",
                    value = "the account is being creating the etymology",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author) {
        if (key.equals("PRINitant19")) {
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexicalEntry(lexicalEntryID)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + lexicalEntryID + " does not exist").build();
                }
                String label = utilityManager.getLabel(lexicalEntryID);
                if (label == null) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("etymology cannot be created: the lexical entry label is not definied").build();
                }
                Etymology e = lexiconManager.createEtymology(lexicalEntryID, author, label);
                String json = etymologyHelper.toJson(e);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                Logger.getLogger(LexiconCreation.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
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
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "author",
                    value = "the account is being creating the etymological link",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author) {
        if (key.equals("PRINitant19")) {
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexicalEntry(lexicalEntryID)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + lexicalEntryID + " does not exist").build();
                }
                String label = utilityManager.getLabel(lexicalEntryID);
                if (label == null) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("etymology cannot be created: the lexical entry label is not definied").build();
                }
                if (!utilityManager.isEtymology(etymologyID)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + etymologyID + " does not exist").build();
                }
                EtymologicalLink el = lexiconManager.createEtymologicalLink(lexicalEntryID, author, etymologyID);
                String json = etymologicalLinkHelper.toJson(el);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                Logger.getLogger(LexiconCreation.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
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
                    name = "lexicalEntityID",
                    value = "the lexical entity id, the bibliography refers to",
                    example = "MUStestNOUN",
                    required = true)
            @QueryParam("lexicalEntityID") String lexicalEntityID,
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "author",
                    value = "the account is being creating the bibliographic link",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            Bibliography bibliography) {
        if (key.equals("PRINitant19")) {
            try {
                if ((bibliography.getId() == null || bibliography.getId().isEmpty())) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("id of bibliography must be defined").build();
                }
                BibliographicItem bi = bibliographyManager.createBibliographyReference(lexicalEntityID, author, bibliography);
                String json = bibliographyHelper.toJson(bi);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                Logger.getLogger(LexiconCreation.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

}
