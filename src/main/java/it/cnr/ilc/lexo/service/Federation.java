/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.manager.FederationManager;
import it.cnr.ilc.lexo.manager.GraphVizManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.input.graphViz.EdgeGraphFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.graphViz.NodeGraphFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.HitsDataList;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.NodeLinks;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.SenseNodeSummary;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.Cytoscape;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.SenseEdgeSummary;
import it.cnr.ilc.lexo.service.helper.FederatedObjectHelper;
import it.cnr.ilc.lexo.service.helper.HelperException;
import it.cnr.ilc.lexo.service.helper.NodeLinksHelper;
import it.cnr.ilc.lexo.service.helper.SenseEdgeSummaryHelper;
import it.cnr.ilc.lexo.service.helper.SenseNodeSummaryHelper;
import it.cnr.ilc.lexo.util.EnumUtil;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.rdf4j.federated.FedXFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("fedex")
@Api("Query Federation")
public class Federation extends Service {

    private static final Logger logger = LoggerFactory.getLogger(Federation.class);
    Logger statLog = LoggerFactory.getLogger("statistics");

    private final FederationManager federationManager = ManagerFactory.getManager(FederationManager.class);
    private final FederatedObjectHelper federatedObjectHelper = new FederatedObjectHelper();

    @POST
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "search",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Search on external endpoints",
            notes = "This method runs a query on a specific sparql endpoint")
    public Response search(@ApiParam(
            name = "sparqlQuery",
            value = "a well-formed sparql query with explicit binding variable names",
            example = "SELECT ?s ?p ?o WHERE { ?s ?p ?o } LIMIT 10",
            required = true)
            @QueryParam("sparqlQuery") String query,
            @ApiParam(
                    name = "endpoint",
                    value = "Url of the endpoint to query",
                    example = "http://dbpedia.org/sparql",
                    required = true)
            @PathParam("endpoint") String endpoint) {
        if (query != null && endpoint != null) {
            try {
                HitsDataList hdl = federationManager.getFederatedResult();
                String json = federatedObjectHelper.toJson(hdl);
                return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
            } catch (ManagerException ex) {
                logger.error(ex.getMessage(), ex);
                return Response.status(Response.Status.SERVICE_UNAVAILABLE).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("query and endpoint parameters must not be empty").build();
        }
    }

}
