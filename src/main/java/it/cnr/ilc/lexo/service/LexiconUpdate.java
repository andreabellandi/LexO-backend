/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.cnr.ilc.lexo.manager.LexiconUpdateManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.UtilityManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.EtymologyUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.GenericRelationUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LanguageUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalSenseUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LinguisticRelationUpdater;
import it.cnr.ilc.lexo.util.EnumUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @POST
    @Path("{id}/language")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}/language",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexicon language update",
            notes = "This method updates the lexicon language according to the input updater")
    public Response language(@QueryParam("key") String key, @QueryParam("user") String user, @PathParam("id") String id, LanguageUpdater lu) {
        if (key.equals("PRINitant19")) {
            try {
                //        log(Level.INFO, "get lexicon entries types");
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexiconLanguage(id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + id + " does not exist").build();
                }
                return Response.ok(lexiconManager.updateLexiconLanguage(id, lu, user))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("{id}/lexicalEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}/lexicalEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry update",
            notes = "This method updates the lexical entry according to the input updater")
    public Response lexicalEntry(@QueryParam("key") String key, @QueryParam("user") String user, @PathParam("id") String id, LexicalEntryUpdater leu) {
        if (key.equals("PRINitant19")) {
            try {
                //        log(Level.INFO, "get lexicon entries types");
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexicalEntry(id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + id + " does not exist").build();
                }
                return Response.ok(lexiconManager.updateLexicalEntry(id, leu, user))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("{id}/form")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}/form",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Form update",
            notes = "This method updates the form according to the input updater")
    public Response form(@QueryParam("key") String key, @QueryParam("user") String user, @PathParam("id") String id, FormUpdater fu) {
        if (key.equals("PRINitant19")) {
            try {
                //        log(Level.INFO, "get lexicon entries types");
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isForm(id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + id + " does not exist").build();
                }
                return Response.ok(lexiconManager.updateForm(id, fu, user))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("{id}/lexicalSense")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}/lexicalSense",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical Sense update",
            notes = "This method updates the sense according to the input updater")
    public Response lexicalSense(@QueryParam("key") String key, @QueryParam("user") String user, @PathParam("id") String id, LexicalSenseUpdater lsu) {
        if (key.equals("PRINitant19")) {
            try {
                //        log(Level.INFO, "get lexicon entries types");
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexicalSense(id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + id + " does not exist").build();
                }
                return Response.ok(lexiconManager.updateLexicalSense(id, lsu, user))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("{id}/etymology")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}/etymology",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Etymology update",
            notes = "This method updates the etymology according to the input updater")
    public Response etymology(@QueryParam("key") String key, @QueryParam("user") String user, @PathParam("id") String id, EtymologyUpdater eu) {
        if (key.equals("PRINitant19")) {
            try {
                //        log(Level.INFO, "get lexicon entries types");
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isEtymology(id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + id + " does not exist").build();
                }
                return Response.ok(lexiconManager.updateEtymology(id, eu, user))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }
    
    @POST
    @Path("{id}/linguisticRelation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}/linguisticRelation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Linguistic relation update",
            notes = "This method updates a linguistic relation according to the input updater")
    public Response linguisticRelation(@QueryParam("key") String key, @PathParam("id") String id, LinguisticRelationUpdater lru) {
        if (key.equals("PRINitant19")) {
            try {
                return Response.ok(lexiconManager.updateLinguisticRelation(id, lru))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("{id}/genericRelation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}/genericRelation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Generic relation update",
            notes = "This method updates a generic relation according to the input updater")
    public Response genericRelation(@QueryParam("key") String key, @PathParam("id") String id, GenericRelationUpdater gru) {
        if (key.equals("PRINitant19")) {
            try {
                return Response.ok(lexiconManager.updateGenericRelation(id, gru))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }
    

}
