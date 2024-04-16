/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.manager.DictionaryStatisticsManager;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.helper.CountingHelper;
import it.cnr.ilc.lexo.service.helper.MetadataHelper;
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
@Path("statistics/dictionary")
@Api("Statistics")
public class DictionaryStatistics extends Service {

    private final DictionaryStatisticsManager dictionaryManager = ManagerFactory.getManager(DictionaryStatisticsManager.class);
    private final CountingHelper countingHelper = new CountingHelper();
    private final MetadataHelper metadataHelper = new MetadataHelper();

    private void userCheck(String key) throws AuthorizationException, ServiceException {
        if (LexOProperties.getProperty("keycloack.freeViewer") != null) {
            if (!LexOProperties.getProperty("keycloack.freeViewer").equals("true")) {
                checkKey(key);
            }
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
    @ApiOperation(value = "Dictionary entry status",
            notes = "This method returns dictionary entry status (verified or not)")
    public Response status(@HeaderParam("Authorization") String key) {
        try {
            userCheck(key);
            log(org.apache.log4j.Level.INFO, "statistics/dictionary/status");
            TupleQueryResult dictionaryEntryStates = dictionaryManager.getStates();
            List<Counting> status = countingHelper.newDataList(dictionaryEntryStates);
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
            notes = "This method returns the authors of each dictionary entry")
    public Response authors(@HeaderParam("Authorization") String key) {
        try {
            userCheck(key);
            log(org.apache.log4j.Level.INFO, "statistics/dictionary/authors");
            TupleQueryResult dictionaryEntryAuthors = dictionaryManager.getAuthors();
            List<Counting> authors = countingHelper.newDataList(dictionaryEntryAuthors);
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
    @ApiOperation(value = "Dictionary language(s)",
            notes = "This method returns the language(s) of the ditionary")
    public Response languages(@HeaderParam("Authorization") String key) {
        try {
            userCheck(key);
            log(org.apache.log4j.Level.INFO, "statistics/dictionary/languages");
            TupleQueryResult languages = dictionaryManager.getLanguages();
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
//        try {
//            userCheck(key);
//            log(org.apache.log4j.Level.INFO, "statistics/dictionary/pos");
//            TupleQueryResult _pos = dictionaryManager.getPos();
//            List<Counting> pos = countingHelper.newDataList(_pos);
//            String json = countingHelper.toJson(pos);
//            return Response.ok(json)
//                    .type(MediaType.TEXT_PLAIN)
//                    .header("Access-Control-Allow-Headers", "content-type")
//                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
//                    .build();
//        } catch (AuthorizationException | ServiceException ex) {
//            log(org.apache.log4j.Level.ERROR, ex.getMessage());
//            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
//        }
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

}
