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
import it.cnr.ilc.lexo.manager.ConfigurationManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.RepositoryData;
import it.cnr.ilc.lexo.util.LogUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Level;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("repository")
@Api("Repository")
public class Configuration extends Service {

    private final ConfigurationManager configManager = ManagerFactory.getManager(ConfigurationManager.class);
    private final String repositoryID = LexOProperties.getProperty("GraphDb.repository");

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "create",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Create a repository in an attached RDF4J location (ttl file)",
            notes = "Create a repository in an attached RDF4J location (ttl file)")
    public Response createRepository(
            @HeaderParam("Authorization") String key, RepositoryData rd) {
        try {
            checkKey(key);
            log(Level.INFO, "POST create repository " + LogUtil.getLogFromPayload(rd));
            Response response = configManager.restCall(LexOProperties.getProperty("repository.url") + "rest/" + "repositories", "POST", rd);
            System.out.println(response.getStatus() + " "
                    + response.getStatusInfo() + " " + response);
            return Response.ok()
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "POST create repository: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        } catch (URISyntaxException | IOException | ManagerException ex) {
            log(Level.ERROR, "POST create repository: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "configuration",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Get repository configuration as JSON",
            notes = "Get repository configuration as JSON")
    public Response getRepositoryConfiguration(
            @HeaderParam("Authorization") String key) {
        try {
            checkKey(key);
            log(Level.INFO, "GET configuration of " + repositoryID + " repository");
            Response response = configManager.restCall(LexOProperties.getProperty("repository.url") + "rest/" + "repositories/" + repositoryID, "GET", null);
            return Response.ok(response.readEntity(String.class))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException | URISyntaxException | IOException | ManagerException ex) {
            log(Level.ERROR, "GET configuration of " + repositoryID + " repository: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }

    }

    @GET
    @Path("size")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "size",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Get repository size",
            notes = "Get repository size")
    public Response getRepositorySize(
            @HeaderParam("Authorization") String key) {
        try {
            checkKey(key);
            log(Level.INFO, "GET size of " + repositoryID + " repository");
            Response response = configManager.restCall(LexOProperties.getProperty("repository.url") + "rest/" + "repositories/" + repositoryID + "/size", "GET", null);
            return Response.ok(response.readEntity(String.class))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException | URISyntaxException | IOException | ManagerException ex) {
            log(Level.ERROR, "GET size of " + repositoryID + " repository: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }
    
    @GET
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "delete",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Delete a repository in an attached RDF4J location",
            notes = "Delete a repository in an attached RDF4J location")
    public Response deleteRepository(
            @HeaderParam("Authorization") String key) {
        try {
            checkKey(key);
            log(Level.INFO, "DELETE " + repositoryID + " repository");
            Response response = configManager.restCall(LexOProperties.getProperty("repository.url") + "rest/" + "repositories/" + repositoryID, "DELETE", null);
            return Response.ok(response.readEntity(String.class))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException | URISyntaxException | IOException | ManagerException ex) {
            log(Level.ERROR, "DELETE " + repositoryID + " repository " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("restart")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "restart",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Restart a repository",
            notes = "Restart a repository")
    public Response restartRepository(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "sync",
                    value = "Restart it synchronously",
                    required = true)
            @QueryParam("sync") boolean sync) {
        try {
            checkKey(key);
            log(Level.INFO, "POST restart " + repositoryID + " repository");
            Response response = configManager.restCall(LexOProperties.getProperty("repository.url") + "rest/" + "repositories/" 
                    + repositoryID + (sync ? "/restart?sync=true" : "/restart?sync=false"), "POST", null);
            return Response.ok(response.readEntity(String.class))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException | URISyntaxException | IOException | ManagerException ex) {
            log(Level.ERROR, "POST restart " + repositoryID + " repository: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }
    
    @GET
    @Path("updateIndex")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "updateIndex",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Update repository indexes",
            notes = "Update repository indexes")
    public Response updateIndex(
            @HeaderParam("Authorization") String key) {
        try {
            checkKey(key);
            log(Level.INFO, "GET update indexes of " + repositoryID + " repository");
            configManager.updateIndex(repositoryID);
            return Response.ok()
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "GET update indexes of " + repositoryID + " repository: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }

    }
    
//    @GET
//    @Path("create")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "create",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Configuration database schema creation",
//            notes = "This method creates the configuration schema (pay attention, current configuration data will be lost!)")
//    public Response create(
//            @ApiParam(
//                    name = "key",
//                    value = "authentication token",
//                    example = "lexodemo",
//                    required = true)
//            @QueryParam("key") String key) {
//        if (key.equals("PRINitant19")) {
//            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
//            Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();
//            SchemaExport schemaExport = new SchemaExport();
//            schemaExport.setDelimiter(";");
//            schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata);
//            schemaExport.execute(EnumSet.of(TargetType.DATABASE), SchemaExport.Action.BOTH, metadata);
//            return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).build();
//        } else {
//            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
//        }
//    }
//
//    @GET
//    @Path("update")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "update",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Configuration database schema update",
//            notes = "This method updates the configuration schema (current configuration data should not be lost!)")
//    public Response update(
//            @ApiParam(
//                    name = "key",
//                    value = "authentication token",
//                    example = "lexodemo",
//                    required = true)
//            @QueryParam("key") String key) {
//        if (key.equals("PRINitant19")) {
//            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
//            Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();
//            SchemaUpdate schemaUpdate = new SchemaUpdate();
//            schemaUpdate.setDelimiter(";");
//            schemaUpdate.execute(EnumSet.of(TargetType.STDOUT), metadata);
//            return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).build();
//        } else {
//            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
//        }
//    }
//
//    @POST
//    @Path("parameter")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.POST,
//            value = "parameter",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Configuration parameter",
//            notes = "This method sets a configuration parameter")
//    public Response parameterSetup(
//            @ApiParam(
//                    name = "key",
//                    value = "authentication token",
//                    example = "lexodemo",
//                    required = true)
//            @QueryParam("key") String key,
//            ConfigUpdater cu) {
//        if (key.equals("PRINitant19")) {
//            try {
//                Date d = configManager.updateConfiguration(cu);
//                return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity(d).build();
//            } catch (ManagerException ex) {
//                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
//            }
//        } else {
//            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
//        }
//    }
//    
//    @GET
//    @Path("parameters")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "parameters",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Configuration parameters",
//            notes = "This method returns all the configuration parameters")
//    public Response parameters(
//            @ApiParam(
//                    name = "key",
//                    value = "authentication token",
//                    example = "lexodemo",
//                    required = true)
//            @QueryParam("key") String key) {
//        if (key.equals("PRINitant19")) {
//            return Response.status(Response.Status.NOT_IMPLEMENTED).type(MediaType.TEXT_PLAIN).entity("Not implemented yet").build();
//        } else {
//            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
//        }
//    }
//
//    @POST
//    @Path("init")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.POST,
//            value = "init",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Configuration initialization",
//            notes = "This method initializes the configuration database with a given configuration JSON file as input")
//    public Response init(
//            @ApiParam(
//                    name = "key",
//                    value = "authentication token",
//                    example = "lexodemo",
//                    required = true)
//            @QueryParam("key") String key,
//            String json) {
//        if (key.equals("PRINitant19")) {
//            try {
//                configManager.initConfigurations(json);
//                return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).build();
//            } catch (ManagerException ex) {
//                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
//            }
//        } else {
//            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
//        }
//    }
}
