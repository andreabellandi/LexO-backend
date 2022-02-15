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
import it.cnr.ilc.lexo.manager.GraphVizManager;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.input.graphViz.EdgeGraphFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.graphViz.NodeGraphFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.NodeLinks;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.SenseNodeSummary;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.Cytoscape;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.SenseEdgeSummary;
import it.cnr.ilc.lexo.service.helper.HelperException;
import it.cnr.ilc.lexo.service.helper.NodeLinksHelper;
import it.cnr.ilc.lexo.service.helper.SenseEdgeSummaryHelper;
import it.cnr.ilc.lexo.service.helper.SenseNodeSummaryHelper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("graphViz")
@Api("Graph Visualization Support")
public class GraphVizualization extends Service {

    private static final Logger logger = LoggerFactory.getLogger(GraphVizualization.class);
    Logger statLog = LoggerFactory.getLogger("statistics");

    private final GraphVizManager graphVizManager = ManagerFactory.getManager(GraphVizManager.class);
    private final NodeLinksHelper nodeLinksHelperHelper = new NodeLinksHelper();
    private final SenseNodeSummaryHelper senseNodeSummaryHelper = new SenseNodeSummaryHelper();
    private final SenseEdgeSummaryHelper senseEdgeSummaryHelper = new SenseEdgeSummaryHelper();

    @GET
    @Path("{id}/nodeSummary")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/nodeSummary",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Node Summary of lexical senses",
            notes = "This method returns the summary data related to a specific sense node")
    public Response nodeSummary(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "id",
                    value = "lexical sense ID",
                    example = "USemD2698conciso",
                    required = true)
            @PathParam("id") String id) {
        TupleQueryResult node = graphVizManager.getNode(id);
        TupleQueryResult links = graphVizManager.getLinks(id);
        List<NodeLinks> _links = nodeLinksHelperHelper.newDataList(links);
        SenseNodeSummary sns = senseNodeSummaryHelper.newData(node);
        graphVizManager.createNodeSummary(_links, sns);
        String json = senseNodeSummaryHelper.toJson(sns);
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

    @POST
    @Path("{id}/nodeGraph")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}/nodeGraph",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Graph of a node",
            notes = "This method returns the incoming and outgoing edges of a node")
    public Response nodeGraph(@ApiParam(
            name = "key",
            value = "authentication token",
            example = "lexodemo",
            required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "id",
                    value = "lexical sense ID",
                    example = "USemD2698conciso",
                    required = true)
            @PathParam("id") String id,
            NodeGraphFilter ngf) throws HelperException {

        try {
            TupleQueryResult incoming = graphVizManager.getNodeGraph(id, ngf, true);
            TupleQueryResult outgoing = graphVizManager.getNodeGraph(id, ngf, false);
            Cytoscape ng = graphVizManager.getNodeGraph(incoming, outgoing, ngf.getRelation());
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(ng);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }
    
    @POST
    @Path("edgeGraph")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/edgeGraph",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Edge details",
            notes = "This method returns the details of an eadge")
    public Response edgeGraph(@ApiParam(
            name = "key",
            value = "authentication token",
            example = "lexodemo",
            required = true)
            @QueryParam("key") String key,
            EdgeGraphFilter egf) throws HelperException {
        TupleQueryResult edge = graphVizManager.getEdgeGraph(egf);
        SenseEdgeSummary ses = senseEdgeSummaryHelper.newData(edge);
        String json = senseEdgeSummaryHelper.toJson(ses);
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

}
