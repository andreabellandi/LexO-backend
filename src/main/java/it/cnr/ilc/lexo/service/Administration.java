/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.cnr.ilc.lexo.manager.AdministrationManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.administration.output.SystemInfo;
import it.cnr.ilc.lexo.service.data.lexicon.output.Property;
import it.cnr.ilc.lexo.service.helper.PropertyValueHelper;
import it.cnr.ilc.lexo.service.helper.SystemInfoHelper;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Level;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("administration")
@Api("Administration")
public class Administration extends Service {


    private final AdministrationManager administrationManager = ManagerFactory.getManager(AdministrationManager.class);
    private final PropertyValueHelper propertyValueHelper = new PropertyValueHelper();
    private final SystemInfoHelper systemInfoHelper = new SystemInfoHelper();

    @GET
    @Path("systemInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "systemInfo",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "System information",
            notes = "This method returns some basic information system e.g., GraphDB version, disk space, attached repo")
    public Response nodeSummary(@HeaderParam("Authorization") String key) {
        String json = "";
        try {
            log(org.apache.log4j.Level.INFO, "administration/systemInfo");
            SystemInfo info = administrationManager.getSystemInfo();
            json = systemInfoHelper.toJson(info);
        } catch (ManagerException ex) {
            log(Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
        return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
    }

}
