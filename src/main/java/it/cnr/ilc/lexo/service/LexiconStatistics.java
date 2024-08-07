/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.manager.LexiconStatisticsManager;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.helper.CountingHelper;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author andreabellandi
 */
@Path("statistics/lexicon")
@Api("Statistics")
public class LexiconStatistics extends Service {

    private final LexiconStatisticsManager lexiconManager = ManagerFactory.getManager(LexiconStatisticsManager.class);
    private final CountingHelper countingHelper = new CountingHelper();
    

    private void userCheck(String key) throws AuthorizationException, ServiceException {
        if (LexOProperties.getProperty("keycloack.freeViewer") != null) {
            if (!LexOProperties.getProperty("keycloack.freeViewer").equals("true")) {
                checkKey(key);
            }
        }
    }
    
    @GET
    @Path("types")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/types",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry types",
            notes = "This method returns the lexical entry types and their counting")
        
    public Response types(@HeaderParam("Authorization") String key) {
        try {
            userCheck(key);
            log(org.apache.log4j.Level.INFO, "statistics/lexicon/types");
            TupleQueryResult lexicalEntryTypes = lexiconManager.getTypes();
            List<Counting> types = countingHelper.newDataList(lexicalEntryTypes);
            String json = countingHelper.toJson(types);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException ex) {
            log(org.apache.log4j.Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/status",
            produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Lexical entry status",
            notes = "This method returns lexical entry status (verified or not)")
    public Response status(@HeaderParam("Authorization") String key) {
        try {
            userCheck(key);
            log(org.apache.log4j.Level.INFO, "statistics/lexicon/status");
            TupleQueryResult lexicalEntryStates = lexiconManager.getStates();
            List<Counting> status = countingHelper.newDataList(lexicalEntryStates);
            String json = countingHelper.toJson(status);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException ex) {
            log(org.apache.log4j.Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("authors")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/authors",
            produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Lexical entry author",
            notes = "This method returns the authors of each lexical entry")
    public Response authors(@HeaderParam("Authorization") String key) {
        try {
            userCheck(key);
            log(org.apache.log4j.Level.INFO, "statistics/lexicon/authors");
            TupleQueryResult lexicalEntryAuthors = lexiconManager.getAuthors();
            List<Counting> authors = countingHelper.newDataList(lexicalEntryAuthors);
            String json = countingHelper.toJson(authors);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException ex) {
            log(org.apache.log4j.Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("languages")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/languages",
            produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Lexical dataset language(s)",
            notes = "This method returns the language(s) of the lexical dataset")
    public Response languages(@HeaderParam("Authorization") String key) {
        try {
            userCheck(key);
            log(org.apache.log4j.Level.INFO, "statistics/lexicon/languages");
            TupleQueryResult languages = lexiconManager.getLanguages();
            List<Counting> langs = countingHelper.newDataList(languages);
            String json = countingHelper.toJson(langs);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException ex) {
            log(org.apache.log4j.Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("pos")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/pos",
            produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Part of Speech tags",
            notes = "This method returns pos and their counting")
    public Response pos(@HeaderParam("Authorization") String key) {
        try {
            userCheck(key);
            log(org.apache.log4j.Level.INFO, "statistics/lexicon/pos");
            TupleQueryResult _pos = lexiconManager.getPos();
            List<Counting> pos = countingHelper.newDataList(_pos);
            String json = countingHelper.toJson(pos);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException ex) {
            log(org.apache.log4j.Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

}
