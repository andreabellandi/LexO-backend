/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.manager.LexiconCreationManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.helper.LexicalEntryCoreHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
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
    private final LexicalEntryCoreHelper lexicalEntryCoreHelper = new LexicalEntryCoreHelper();

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
    
//    @GET
//    @Path("form")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "form",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Form creation",
//            notes = "This method creates a new form and returns its id and some metadata")
//    public Response form(
//            @ApiParam(
//                    name = "key",
//                    value = "authentication token",
//                    example = "lexodemo",
//                    required = true)
//            @QueryParam("key") String key,
//            @ApiParam(
//                    name = "author",
//                    value = "the account is being creating the form",
//                    example = "user7",
//                    required = true)
//            @QueryParam("author") String author,
//            @ApiParam(
//                    name = "lexical entry id",
//                    value = "lexical entry id the form belongs to",
//                    required = true)
//            @QueryParam("id") String id) {
//        if (key.equals("PRINitant19")) {
//            try {
//                //        log(Level.INFO, "get lexicon entries types");
//                LexicalEntryCore lec = lexiconManager.createForm(id, author);
//                String json = lexicalEntryCoreHelper.toJson(lec);
//                return Response.ok(json)
//                        .type(MediaType.TEXT_PLAIN)
//                        .header("Access-Control-Allow-Headers", "content-type")
//                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
//                        .build();
//            } catch (ManagerException ex) {
//                Logger.getLogger(LexiconCreation.class.getName()).log(Level.SEVERE, null, ex);
//                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
//            }
//        } else {
//            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
//        }
//    }

}
