/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.SKOSManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.ConceptSchemeFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.skos.SKOSDeleter;
import it.cnr.ilc.lexo.service.data.lexicon.input.skos.SKOSUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.output.skos.ConceptScheme;
import it.cnr.ilc.lexo.service.helper.ConceptSchemeHelper;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("skos")
@Api("SKOS Management")
public class SKOSService extends Service {

    private final ConceptSchemeHelper conceptSchemeHelper = new ConceptSchemeHelper();
    private final SKOSManager skosManager = ManagerFactory.getManager(SKOSManager.class);

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
                    value = "the account that is creating the skos concept (if LexO user management disabled)",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    required = true)
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "prefix",
                    value = "prefix of the namespace",
                    example = "myprefix",
                    required = true)
            @QueryParam("prefix") String prefix,
            @ApiParam(
                    name = "baseIRI",
                    value = "base IRI of the language",
                    example = "http://mydata.com#",
                    required = true)
            @QueryParam("baseIRI") String baseIRI) {
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
                    value = "the account that is creating the skos concept scheme (if LexO user management disabled)",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    required = true)
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "prefix",
                    value = "prefix of the namespace",
                    example = "myprefix",
                    required = true)
            @QueryParam("prefix") String prefix,
            @ApiParam(
                    name = "baseIRI",
                    value = "base IRI of the language",
                    example = "http://mydata.com#",
                    required = true)
            @QueryParam("baseIRI") String baseIRI) {
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
                    value = "the account that is creating the skos collection (if LexO user management disabled)",
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
                    required = true)
            @HeaderParam("Authorization") String key) {
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
                    required = true)
            @HeaderParam("Authorization") String key, SKOSDeleter sd) {
        try {
            checkKey(key);
            skosManager.deleteRelation(sd);
            return Response.ok()
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
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
                    required = true)
            @HeaderParam("Authorization") String key, SKOSUpdater su) {
        try {
            checkKey(key);
            String json = skosManager.updateSemanticRelation(su);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } 
        catch (AuthorizationException | ServiceException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
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
                    required = true)
            @HeaderParam("Authorization") String key, SKOSUpdater su) {
        try {
            checkKey(key);
            String json = skosManager.updateLexicalProperty(su);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();

        } catch (ManagerException | UpdateExecutionException | IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
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
                    required = true)
            @HeaderParam("Authorization") String key, SKOSUpdater su) {
        try {
            checkKey(key);
            String json = skosManager.updateNotation(su);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
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
                    required = true)
            @HeaderParam("Authorization") String key, SKOSUpdater su) {
        try {
            checkKey(key);
            String json = skosManager.updateSchemeProperty(su);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
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
                    required = true)
            @HeaderParam("Authorization") String key, SKOSUpdater su) {
        try {
            checkKey(key);
            String json = skosManager.updateNoteProperty(su);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
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
                    required = true)
            @HeaderParam("Authorization") String key, SKOSUpdater su) {
        try {
            checkKey(key);
            String json = skosManager.updateMappingProperty(su);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();

        } catch (ManagerException | UpdateExecutionException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
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
                    required = true)
            @HeaderParam("Authorization") String key, SKOSUpdater su) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("{id}/concept")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "concept",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "SKOS Concept deletion",
            notes = "This method deletes a SKOS concept")
    public Response concept(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    required = true)
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "concept IRI",
                    required = true)
            @PathParam("id") String id) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("{id}/entity")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "entity",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "SKOS entity deletion",
            notes = "This method deletes a SKOS entity (Concept, ConceptSchema or Collection)")
    public Response entity(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    required = true)
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "entity IRI",
                    required = true)
            @PathParam("id") String id) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("conceptSchemes")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "conceptSchemes",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "List of Concept Scheme",
            notes = "This method return the list of Concept Schemes and their metadata")
    public Response conceptSchemes(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    required = true)
            @HeaderParam("Authorization") String key, ConceptSchemeFilter csf) {

        try {
            checkKey(key);
            TupleQueryResult cs = skosManager.getConceptSchemes("skos:ConceptScheme");
            List<ConceptScheme> lcs = conceptSchemeHelper.newDataList(cs);
            String json = conceptSchemeHelper.toJson(lcs);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

}
