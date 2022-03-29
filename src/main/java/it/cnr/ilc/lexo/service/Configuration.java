/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.service.data.lexicon.input.Config;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
@Path("configuration")
@Api("Configuration")
public class Configuration extends Service {

    @GET
    @Path("parameter")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "parameter",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Configuration parameter",
            notes = "This method returns a configuration parameter")
    public Response parameter(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "param",
                    value = "parameter name",
                    required = true)
            @QueryParam("param") String param) {
        if (key.equals("PRINitant19")) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).type(MediaType.TEXT_PLAIN).entity("Not implemented yet").build();
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

    @PUT
    @Path("parameter")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.PUT,
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
            @ApiParam(
                    name = "param",
                    value = "parameter name",
                    required = true)
            @QueryParam("param") String param,
            @ApiParam(
                    name = "value",
                    value = "parameter value",
                    required = true)
            @QueryParam("value") String value) {
        if (key.equals("PRINitant19")) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).type(MediaType.TEXT_PLAIN).entity("Not implemented yet").build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

    @PUT
    @Path("parameters")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "parameters",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Configuration parameters",
            notes = "This method sets all the configuration parameters")
    public Response parametersSetup(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            Config config) {
        if (key.equals("PRINitant19")) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).type(MediaType.TEXT_PLAIN).entity("Not implemented yet").build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }
}