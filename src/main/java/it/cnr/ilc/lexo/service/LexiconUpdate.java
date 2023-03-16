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
import it.cnr.ilc.lexo.manager.LexiconUpdateManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.UtilityManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.ComponentPositionUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.EtymologicalLinkUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.EtymologyUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.GenericRelationUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LanguageUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalSenseUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LinguisticRelationUpdater;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("lexicon/update")
@Api("Lexicon update")
public class LexiconUpdate extends Service {

    private final LexiconUpdateManager lexiconManager = ManagerFactory.getManager(LexiconUpdateManager.class);
    private final BibliographyManager bibliographyManager = ManagerFactory.getManager(BibliographyManager.class);

    @POST
    @Path("language")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "language",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexicon language update",
            notes = "This method updates the lexicon language according to the input updater")
    public Response language(@QueryParam("key") String key, @QueryParam("user") String user, @QueryParam("id") String id, LanguageUpdater lu) {
        if (key.equals("PRINitant19")) {
            try {
                String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
                try {
                    UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                    if (!utilityManager.isLexiconLanguage(_id)) {
                        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                    }
                    return Response.ok(lexiconManager.updateLexiconLanguage(_id, lu, user))
                            .type(MediaType.TEXT_PLAIN)
                            .header("Access-Control-Allow-Headers", "content-type")
                            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                            .build();
                } catch (ManagerException | UpdateExecutionException ex) {
                    Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
                }
            } catch (UnsupportedEncodingException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("lexicalEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "lexicalEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry update",
            notes = "This method updates the lexical entry according to the input updater")
    public Response lexicalEntry(@QueryParam("key") String key, @QueryParam("user") String user, @QueryParam("id") String id, LexicalEntryUpdater leu) {
        if (key.equals("PRINitant19")) {
            try {
                String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                String type = utilityManager.getLexicalEntryType(_id);
                if (type == null) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                } 
                return Response.ok(lexiconManager.updateLexicalEntry(_id, leu, user, type))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException | UnsupportedEncodingException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("form")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "form",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Form update",
            notes = "This method updates the form according to the input updater")
    public Response form(@QueryParam("key") String key, @QueryParam("user") String user, @QueryParam("id") String id, FormUpdater fu) {
        if (key.equals("PRINitant19")) {
            try {
                String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isForm(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                return Response.ok(lexiconManager.updateForm(_id, fu, user))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException | UnsupportedEncodingException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("lexicalSense")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "lexicalSense",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical Sense update",
            notes = "This method updates the sense according to the input updater")
    public Response lexicalSense(@QueryParam("key") String key, @QueryParam("user") String user, @QueryParam("id") String id, LexicalSenseUpdater lsu) {
        if (key.equals("PRINitant19")) {
            try {
                String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexicalSense(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                return Response.ok(lexiconManager.updateLexicalSense(_id, lsu, user))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException | UnsupportedEncodingException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("etymology")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "etymology",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Etymology update",
            notes = "This method updates the etymology according to the input updater")
    public Response etymology(@QueryParam("key") String key, @QueryParam("user") String user, @QueryParam("id") String id, EtymologyUpdater eu) {
        if (key.equals("PRINitant19")) {
            try {
                String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
                try {
                    UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                    if (!utilityManager.isEtymology(_id)) {
                        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                    }
                    return Response.ok(lexiconManager.updateEtymology(_id, eu, user))
                            .type(MediaType.TEXT_PLAIN)
                            .header("Access-Control-Allow-Headers", "content-type")
                            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                            .build();
                } catch (ManagerException | UpdateExecutionException ex) {
                    Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
                }
            } catch (UnsupportedEncodingException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("etymologicalLink")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "etymologicalLink",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Etymological link update",
            notes = "This method updates the etymological link according to the input updater")
    public Response etymologicalLink(@QueryParam("key") String key, @QueryParam("user") String user,
            @QueryParam("id") String id, EtymologicalLinkUpdater elu) {
        if (key.equals("PRINitant19")) {
            try {
                String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
                try {
                    UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                    if (!utilityManager.isEtymologicalLink(_id)) {
                        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                    }
                    return Response.ok(lexiconManager.updateEtymologicalLink(_id, elu, user))
                            .type(MediaType.TEXT_PLAIN)
                            .header("Access-Control-Allow-Headers", "content-type")
                            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                            .build();
                } catch (ManagerException | UpdateExecutionException ex) {
                    Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
                }
            } catch (UnsupportedEncodingException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("linguisticRelation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "linguisticRelation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Linguistic relation update",
            notes = "This method updates a linguistic relation according to the input updater")
    public Response linguisticRelation(@QueryParam("key") String key, @QueryParam("id") String id, LinguisticRelationUpdater lru) {
        if (key.equals("PRINitant19")) {
            try {
                String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
                try {
                    String json = lexiconManager.updateLinguisticRelation(_id, lru);
                    return Response.ok(json)
                            .type(MediaType.TEXT_PLAIN)
                            .header("Access-Control-Allow-Headers", "content-type")
                            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                            .build();
                    
                } catch (ManagerException | UpdateExecutionException ex) {
                    Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
                }
            } catch (UnsupportedEncodingException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("genericRelation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "genericRelation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Generic relation update",
            notes = "This method updates a generic relation according to the input updater")
    public Response genericRelation(@QueryParam("key") String key, @QueryParam("id") String id, GenericRelationUpdater gru) {
        if (key.equals("PRINitant19")) {
            try {
                String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
                try {
                    return Response.ok(lexiconManager.updateGenericRelation(_id, gru))
                            .type(MediaType.TEXT_PLAIN)
                            .header("Access-Control-Allow-Headers", "content-type")
                            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                            .build();
                } catch (ManagerException | UpdateExecutionException ex) {
                    Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
                }
            } catch (UnsupportedEncodingException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("componentPosition")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "componentPosition",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Component position update",
            notes = "This method updates the position of a multiword component according to the input updater")
    public Response componentPosition(@QueryParam("key") String key, @QueryParam("id") String id, ComponentPositionUpdater cpu) {
        if (key.equals("PRINitant19")) {
            try {
                String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
                try {
                    return Response.ok(lexiconManager.updateComponentPosition(_id, cpu))
                            .type(MediaType.TEXT_PLAIN)
                            .header("Access-Control-Allow-Headers", "content-type")
                            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                            .build();
                } catch (ManagerException | UpdateExecutionException ex) {
                    Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
                }
            } catch (UnsupportedEncodingException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("synchronizeBibliography")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "synchronizeBibliography",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Bibliography synchronization with Zotero",
            notes = "This method synchronized author, date, and title fields of the local repository with the corresponding Zotero values")
    public Response synchronizeBibliography(
            @ApiParam(
                    name = "id",
                    value = "the lexical entity id, the bibliography refers to",
                    required = true)
            @QueryParam("id") String id,
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
            @ApiParam(
                    name = "itemKey",
                    value = "Zotero item key",
                    example = "AXZSXE45",
                    required = true)
            @QueryParam("itemKey") String itemKey) {
        if (key.equals("PRINitant19")) {
            try {
                String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
                String lastUpdate = "";
                if ((itemKey == null || itemKey.isEmpty())) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("item Key must be defined").build();
                }
                String bibID = bibliographyManager.getBibliographyID(_id, itemKey);
                if (bibID == null) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("item Key is not valid the this lexical entity").build();
                } else {
                    try {
                        lastUpdate = bibliographyManager.synchronizeBibliography(bibID, itemKey);
                    } catch (RuntimeException e) {
                        if (e.getMessage().contains("404")) {
                            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Zotero: Key " + itemKey + " not found").build();
                        } else {
                            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Zotero: " + e.getMessage()).build();
                        }
                    }
                }
                return Response.ok(lastUpdate)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (UnsupportedEncodingException ex) {
               return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

}
