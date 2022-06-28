/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.manager.ConfigurationManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.input.ConfigUpdater;
import java.util.Date;
import java.util.EnumSet;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("configuration")
@Api("Configuration")
public class Configuration extends Service {

    private final ConfigurationManager configManager = ManagerFactory.getManager(ConfigurationManager.class);

    @GET
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "create",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Configuration database schema creation",
            notes = "This method creates the configuration schema (pay attention, current configuration data will be lost!)")
    public Response create(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
        if (key.equals("PRINitant19")) {
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();
            SchemaExport schemaExport = new SchemaExport();
            schemaExport.setDelimiter(";");
            schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata);
            schemaExport.execute(EnumSet.of(TargetType.DATABASE), SchemaExport.Action.BOTH, metadata);
            return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @GET
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "update",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Configuration database schema update",
            notes = "This method updates the configuration schema (current configuration data should not be lost!)")
    public Response update(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
        if (key.equals("PRINitant19")) {
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();
            SchemaUpdate schemaUpdate = new SchemaUpdate();
            schemaUpdate.setDelimiter(";");
            schemaUpdate.execute(EnumSet.of(TargetType.STDOUT), metadata);
            return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("parameter")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "parameter",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Configuration parameter",
            notes = "This method sets a configuration parameter")
    public Response parameterSetup(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            ConfigUpdater cu) {
        if (key.equals("PRINitant19")) {
            try {
                Date d = configManager.updateConfiguration(cu);
                return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity(d).build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }
    
    @GET
    @Path("parameters")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "parameters",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Configuration parameters",
            notes = "This method returns all the configuration parameters")
    public Response parameters(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key) {
        if (key.equals("PRINitant19")) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).type(MediaType.TEXT_PLAIN).entity("Not implemented yet").build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @POST
    @Path("init")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "init",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Configuration initialization",
            notes = "This method initializes the configuration database with a given configuration JSON file as input")
    public Response init(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            String json) {
        if (key.equals("PRINitant19")) {
            try {
                configManager.initConfigurations(json);
                return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

}
