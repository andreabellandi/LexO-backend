/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.manager.LexiconStatisticsManager;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.helper.CountingHelper;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
@Path("lexicon/statistics")
@Api("Lexicon statistics")
public class LexiconStatistics extends Service {

    private final LexiconStatisticsManager lexiconManager = ManagerFactory.getManager(LexiconStatisticsManager.class);
    private final CountingHelper countingHelper = new CountingHelper();

    @GET
    @Path("types")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/types",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry types",
            notes = "This method returns the lexical entry types and their counting")
//    @ApiResponses(value = {
//        @ApiResponse(code = 200, message = "OK", response = Counting.class),
//        @ApiResponse(code = 201, message = ""),
//        @ApiResponse(code = 400, message = "")
//    })
    public Response types(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
//        log(Level.INFO, "get lexicon entries types");
        TupleQueryResult lexicalEntryTypes = lexiconManager.getTypes();
        List<Counting> types = countingHelper.newDataList(lexicalEntryTypes);
        String json = countingHelper.toJson(types);
        return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
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
    public Response status(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
//        log(Level.INFO, "get lexicon entries types");
        TupleQueryResult lexicalEntryStates = lexiconManager.getStates();
        List<Counting> status = countingHelper.newDataList(lexicalEntryStates);
        String json = countingHelper.toJson(status);
        return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
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
    public Response authors(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
//        log(Level.INFO, "get lexicon entries types");
        TupleQueryResult lexicalEntryAuthors = lexiconManager.getAuthors();
        List<Counting> authors = countingHelper.newDataList(lexicalEntryAuthors);
        String json = countingHelper.toJson(authors);
        return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
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
    public Response languages(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
//        log(Level.INFO, "get lexicon entries types");
        TupleQueryResult languages = lexiconManager.getLanguages();
        List<Counting> langs = countingHelper.newDataList(languages);
        String json = countingHelper.toJson(langs);
        return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
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
    public Response pos(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
//        log(Level.INFO, "get lexicon entries types");
        TupleQueryResult _pos = lexiconManager.getPos();
        List<Counting> pos = countingHelper.newDataList(_pos);
        String json = countingHelper.toJson(pos);
        return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
    }
}
