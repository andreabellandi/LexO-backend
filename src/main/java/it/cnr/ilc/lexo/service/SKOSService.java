/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.service.data.lexicon.input.skos.SKOSDeleter;
import it.cnr.ilc.lexo.service.data.lexicon.input.skos.SKOSUpdater;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("skos")
@Api("SKOS Management")
public class SKOSService extends Service {

    private static final Logger logger = LoggerFactory.getLogger(SKOSService.class);
    Logger statLog = LoggerFactory.getLogger("statistics");

    @GET
    @Path("createConcept")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "createConcept",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "SKOS Concept creation",
            notes = "This method returns the skos concept created")
    public Response createConcept(
            @ApiParam(
                    name = "author",
                    value = "the account is being creating the skos concept",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("createConceptScheme")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "createConceptScheme",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "SKOS Concept scheme creation",
            notes = "This method returns the skos concept scheme created")
    public Response createConceptScheme(
            @ApiParam(
                    name = "author",
                    value = "the account is being creating the skos concept scheme",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("createCollection")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "createCollection",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "SKOS Concept collection creation",
            notes = "This method returns the skos collection created")
    public Response createCollection(
            @ApiParam(
                    name = "author",
                    value = "the account is being creating the skos collection",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            @ApiParam(
                    name = "ordered",
                    value = "if the collection is ordered",
                    example = "false",
                    required = true)
            @QueryParam("ordered") String ordered,
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "delete",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Skos entity/relation deletion",
            notes = "This method deletes a skos entity or a skos relation depending on the input")
    public Response delete(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key, SKOSDeleter sd) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    
    @POST
    @Path("updateSemanticRelation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "updateSemanticRelation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Update skos semantic relation (see https://www.w3.org/TR/2009/REC-skos-reference-20090818/#semantic-relations)",
            notes = "This method update semanitc relation according to the input")
    public Response updateSemanticRelation(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key, SKOSUpdater su) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    
    @POST
    @Path("updateLexicalLabel")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "updateLexicalLabel",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Update skos lexical labels (see https://www.w3.org/TR/2009/REC-skos-reference-20090818/#labels)",
            notes = "This method update labels according to the input")
    public Response updateLexicalLabel(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key, SKOSUpdater su) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    
    @POST
    @Path("updateNotation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "updateNotation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Update skos notation (see https://www.w3.org/TR/2009/REC-skos-reference-20090818/#notations)",
            notes = "This method update skos notation according to the input")
    public Response updateNotation(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key, SKOSUpdater su) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    
    @POST
    @Path("updateSchemeProperty")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "updateSchemeProperty",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Update skos scheme properties (see https://www.w3.org/TR/2009/REC-skos-reference-20090818/#schemes)",
            notes = "This method update scheme properties according to the input")
    public Response updateSchemeProperty(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key, SKOSUpdater su) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    
    @POST
    @Path("updateNoteProperty")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "updateNoteProperty",
            produces = "application/json; charset=UTF-8")
   @ApiOperation(value = "Update skos note relation (see https://www.w3.org/TR/2009/REC-skos-reference-20090818/#notes)",
            notes = "This method update note relation according to the input")
    public Response updateNoteProperty(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key, SKOSUpdater su) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    
    @POST
    @Path("updateMappingProperty")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "updateMappingProperty",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Update skos mapping relation (see https://www.w3.org/TR/2009/REC-skos-reference-20090818/#mapping)",
            notes = "This method update mapping relation according to the input")
    public Response updateMappingProperty(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key, SKOSUpdater su) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    
    @POST
    @Path("updateCollection")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "updateCollection",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Update skos collection properties (see https://www.w3.org/TR/2009/REC-skos-reference-20090818/#collections)",
            notes = "This method update skos collections according to the input")
    public Response updateCollection(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key, SKOSUpdater su) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

}
