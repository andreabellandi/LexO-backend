/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.LexOProperties;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import klab.ilc.cnr.it.ontoapi.OntologyManager;
import org.apache.log4j.Level;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("ontology/data")
@Api("OWL Management")
public class OntologyData extends Service {

    private void userCheck(String key) throws AuthorizationException, ServiceException {
        if (LexOProperties.getProperty("keycloack.freeViewer") != null) {
            if (!LexOProperties.getProperty("keycloack.freeViewer").equals("true")) {
                checkKey(key);
            }
        }
    }

    @GET
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "info",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Info",
            notes = "This method returns some ontology information such as name, version, imports, annotations, and reasoner info")
    public Response classes(
            @HeaderParam("Authorization") String key) {
        try {
            userCheck(key);
            log(Level.INFO, "/ontology/data/info");
            OntologyManager om = OntologyManager.getInstance();
            String json = om.toJson(om.getOntologyInfo());
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("classes")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "classes",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Classes",
            notes = "This method returns the direct subclasses of a given class (null for root classes)")
    public Response classes(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "full class IRI")
            @QueryParam("id") String id,
            @ApiParam(
                    name = "direct",
                    value = "inference depth",
                    allowableValues = "true, false",
                    required = true)
            @QueryParam("direct") boolean direct) {
        try {
            if (!direct) {
                log(Level.ERROR, "ontology/data/classes with direct=false not supported yet.");
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("ontology/data/classes with direct=false not supported yet.").build();
            }
            userCheck(key);
            String _id = (id != null ? URLDecoder.decode(id, StandardCharsets.UTF_8.name()) : null);
            log(Level.INFO, "/ontology/data/classes of <" + _id + ">");
            OntologyManager om = OntologyManager.getInstance();
            String json = om.toJson(om.getDirectSubClassesOf(_id));
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException | AuthorizationException | ServiceException ex) {
            log(Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("properties")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "properties",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Properties",
            notes = "This method returns the direct subproperties of a given property (null for root properties)")
    public Response properties(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "full class IRI")
            @QueryParam("id") String id,
            @ApiParam(
                    name = "direct",
                    value = "inference depth",
                    allowableValues = "true, false",
                    required = true)
            @QueryParam("direct") boolean direct,
            @ApiParam(
                    name = "type",
                    value = "the property type",
                    allowableValues = "object, data",
                    required = true)
            @QueryParam("type") String type) {
        try {
            if (!direct) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            userCheck(key);
            String _id = (id != null ? URLDecoder.decode(id, StandardCharsets.UTF_8.name()) : null);
            log(Level.INFO, "/ontology/data/properties of <" + _id + "> (" + type + ", " + direct + ")");
            OntologyManager om = OntologyManager.getInstance();
            String json;
            if (type.equals("object")) {
                json = om.toJson(om.getDirectSubObjectPropertyOf(_id));
            } else if (type.equals("data")) {
                json = om.toJson(om.getDirectSubDataPropertyOf(_id));
            } else {
                log(Level.ERROR, "ontology/data/properties with direct=false not supported yet.");
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("ontology/data/properties with direct=false not supported yet.").build();
            }
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException | AuthorizationException | ServiceException ex) {
            log(Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("individuals")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "individuals",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Individuals",
            notes = "This method returns the individuals belonging to a given class (null for all individuals)")
    public Response individuals(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "full class IRI")
            @QueryParam("id") String id) {
        try {
            userCheck(key);
            String _id = (id != null ? URLDecoder.decode(id, StandardCharsets.UTF_8.name()) : null);
            log(Level.INFO, "/ontology/data/individuals of <" + _id + ">");
            OntologyManager om = OntologyManager.getInstance();
            String json = om.toJson(om.getIndividualsOf(_id));
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException | AuthorizationException | ServiceException ex) {
            log(Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
        try {
            String fileName = fileDetail.getFileName();
            OntologyManager om = OntologyManager.getInstance();
            om.upload(uploadedInputStream, fileName);
            return Response.ok()
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (OWLOntologyCreationException ex) {
            log(Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("restore")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "restore",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "restore",
            notes = "This method loads the ontology placed at java.io.tmpdir/ontoapi/, if it exists")
    public Response restore() throws IOException {
        try {
            OntologyManager om = OntologyManager.getInstance();
            om.restore("testOntology.owl");
            return Response.ok()
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (OWLOntologyCreationException ex) {
            log(Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }
}
