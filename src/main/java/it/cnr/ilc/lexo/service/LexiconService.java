/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.cnr.ilc.lexo.manager.LexiconManager;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.Counting;
import it.cnr.ilc.lexo.service.data.lexicon.FormItem;
import it.cnr.ilc.lexo.service.data.lexicon.LexicalEntryElement;
import it.cnr.ilc.lexo.service.data.lexicon.LexicalEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.LexicalEntryItem;
import it.cnr.ilc.lexo.service.helper.FormsListHelper;
import it.cnr.ilc.lexo.service.helper.HelperException;
import it.cnr.ilc.lexo.service.helper.LexicalEntryFilterHelper;
import it.cnr.ilc.lexo.service.helper.CountingHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryElementHelper;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Path("lexicon")
@Api("Lexicon service")
public class LexiconService {

    private final LexiconManager lexiconManager = ManagerFactory.getManager(LexiconManager.class);
    private final CountingHelper countingHelper = new CountingHelper();
    private final LexicalEntryFilterHelper lexicalEntryFilterHelper = new LexicalEntryFilterHelper();
    private final FormsListHelper formsListHelper = new FormsListHelper();
    private final LexicalEntryElementHelper lexicalEntryElementHelper = new LexicalEntryElementHelper();
    

    @POST
    @Path("lexicalEntries")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/lexicalEntries",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entries list",
            notes = "This method returns a list of lexical entries according to the input filter")
    public Response list(@QueryParam("key") String key, LexicalEntryFilter lef) throws HelperException {
        TupleQueryResult lexicalEntryTypes = lexiconManager.getFilterdLexicalEntries(lef);
        List<LexicalEntryItem> entries = lexicalEntryFilterHelper.newDataList(lexicalEntryTypes);
        String json = lexicalEntryFilterHelper.toJson(entries);
        return Response.ok(json).build();
    }

    @GET
    @Path("{id}/forms")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/forms",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry forms",
            notes = "This method returns all the forms of a lexical entry")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = FormItem.class),
        @ApiResponse(code = 201, message = ""),
        @ApiResponse(code = 400, message = "")
    })
    public Response forms(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key, 
            @ApiParam(
                    name = "id",
                    value = "lexical entry ID",
                    example = "MUSaccedereVERB",
                    required = true)
            @PathParam("id") String id) {
//        log(Level.INFO, "get lexicon entries types");
        TupleQueryResult _forms = lexiconManager.getForms(id);
        List<FormItem> forms = formsListHelper.newDataList(_forms);
        String json = formsListHelper.toJson(forms);
        return Response.ok(json).build();
    }
    
    @GET
    @Path("{id}/elements")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/elements",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry elements",
            notes = "This method returns the counting of all the elements of a lexical entry (forms, senses, frames, etc ...)")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = Counting.class),
        @ApiResponse(code = 201, message = ""),
        @ApiResponse(code = 400, message = "")
    })
    public Response elements(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key, 
            @ApiParam(
                    name = "id",
                    value = "lexical entry ID",
                    example = "MUSaccedereVERB",
                    required = true)
            @PathParam("id") String id) {
//        log(Level.INFO, "get lexicon entries types");
        TupleQueryResult _elements = lexiconManager.getElements(id);
        List<LexicalEntryElement> elements = lexicalEntryElementHelper.newDataList(_elements);
        String json = lexicalEntryElementHelper.toJson(elements);
        return Response.ok(json).build();
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
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = Counting.class),
        @ApiResponse(code = 201, message = ""),
        @ApiResponse(code = 400, message = "")
    })
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
        return Response.ok(json).build();
    }

    @GET
    @Path("states")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/states",
            produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Lexical entry states",
            notes = "This method returns lexical entry states (verified or not)")
    public Response states(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
//        log(Level.INFO, "get lexicon entries types");
        TupleQueryResult lexicalEntryStates = lexiconManager.getStates();
        List<Counting> states = countingHelper.newDataList(lexicalEntryStates);
        String json = countingHelper.toJson(states);
        return Response.ok(json).build();
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
        return Response.ok(json).build();
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
        return Response.ok(json).build();
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
        return Response.ok(json).build();
    }
}
