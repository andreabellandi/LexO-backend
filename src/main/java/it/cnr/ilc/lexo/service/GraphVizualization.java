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
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.input.graphViz.EdgeGraphFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.graphViz.HopsFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.graphViz.NodeGraphFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.NodeLinks;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.SenseNodeSummary;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.Cytoscape;
import it.cnr.ilc.lexo.service.data.lexicon.output.graphViz.Hop;
import it.cnr.ilc.lexo.service.helper.HelperException;
import it.cnr.ilc.lexo.service.helper.HopHelper;
import it.cnr.ilc.lexo.service.helper.NodeLinksHelper;
import it.cnr.ilc.lexo.service.helper.SenseEdgeSummaryHelper;
import it.cnr.ilc.lexo.service.helper.SenseNodeSummaryHelper;
import it.cnr.ilc.lexo.util.EnumUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    private final HopHelper hopeHelper = new HopHelper();

    @GET
    @Path("nodeSummary")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "nodeSummary",
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
                    required = true)
            @QueryParam("id") String id) {
        try {
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            TupleQueryResult node = graphVizManager.getNode(_id);
            SenseNodeSummary sns = senseNodeSummaryHelper.newData(node);
            TupleQueryResult links = graphVizManager.getLinks(_id);
            List<NodeLinks> _links = nodeLinksHelperHelper.newDataList(links);
            graphVizManager.createNodeSummary(_links, sns);
            String json = senseNodeSummaryHelper.toJson(sns);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("nodeGraph")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "nodeGraph",
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
                    required = true)
            @QueryParam("id") String id,
            NodeGraphFilter ngf) throws HelperException {
        try {
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            TupleQueryResult tqr = null;
            if (ngf.getLenght() == 0) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("lenght must be greather than zero").build();
            }
            if (ngf.getDirection() != null) {
                if (ngf.getDirection().equals(EnumUtil.GraphRelationDirection.incoming.toString())) {
                    tqr = graphVizManager.getNodeGraph(_id, ngf, "dst");
                } else {
                    if (ngf.getDirection().equals(EnumUtil.GraphRelationDirection.outgoing.toString())) {
                        tqr = graphVizManager.getNodeGraph(_id, ngf, "src");
                    } else {
                        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("direction field can set to incoming or outgoing").build();
                    }
                }
                Cytoscape ng = graphVizManager.getNodeGraph(tqr, ngf.getRelation());
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(ng);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("direction field is mandatory").build();
            }
        } catch (JsonProcessingException | UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("edgeGraph")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "edgeGraph",
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
        String json = "";
        if (edge.hasNext()) {
            json = senseEdgeSummaryHelper.toJson(senseEdgeSummaryHelper.newData(edge));
        }
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

    @POST
    @Path("hopsByRel")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/hopsByRel",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Hops by relation",
            notes = "This method returns all the possible hops starting from a node by a specific relation")
    public Response hopsByRel(@ApiParam(
            name = "key",
            value = "authentication token",
            example = "lexodemo",
            required = true)
            @QueryParam("key") String key,
            HopsFilter hf) throws HelperException {
        List<Hop> hops = new ArrayList();
        String json = "";
        if ((hf.getRelation() == null) || (hf.getNode() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Node or relation must be specified").build();
        } else if (hf.getDirection() != null) {
            if (!hf.getDirection().equals(EnumUtil.GraphRelationDirection.incoming.toString())
                    && !hf.getDirection().equals(EnumUtil.GraphRelationDirection.outgoing.toString())) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("direction must be set to \"incoming\" or \"outgoing\"").build();
            } else {
                TupleQueryResult tqp = graphVizManager.getHopsByRel(hf, (hf.getDirection().equals(EnumUtil.GraphRelationDirection.outgoing.toString()) ? "src" : "dst"));
                if (tqp.hasNext()) {
                    hops = hopeHelper.newDataList(tqp);
                }
            }
        } else {
            TupleQueryResult tqp = graphVizManager.getHopsByRel(hf, "src");
            if (tqp.hasNext()) {
                hops = hopeHelper.newDataList(tqp);
            }
            tqp = graphVizManager.getHopsByRel(hf, "dst");
            if (tqp.hasNext()) {
                hops.addAll(hopeHelper.newDataList(tqp));
            }
        }
        if (!hops.isEmpty()) {
            json = hopeHelper.toJson(hops);
        }
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

    @POST
    @Path("minPath")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/minPath",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Minimum path between two nodes",
            notes = "This method returns the minimum path between two nodes according to the input parameters")
    public Response minPath(@ApiParam(
            name = "key",
            value = "authentication token",
            example = "lexodemo",
            required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "source",
                    value = "source node ID",
                    required = true)
            @QueryParam("source") String source,
            @ApiParam(
                    name = "target",
                    value = "target node ID",
                    required = true)
            @QueryParam("target") String target,
            @ApiParam(
                    name = "inference",
                    value = "true or false",
                    required = true)
            @QueryParam("inference") Boolean inference) throws HelperException {
        if (source != null && target != null) {
            try {
                String _source = URLDecoder.decode(source, StandardCharsets.UTF_8.name());
                String _target = URLDecoder.decode(target, StandardCharsets.UTF_8.name());
                try {
                    TupleQueryResult tqp = graphVizManager.getMinPath(_source, _target, inference);
                    ArrayList<Cytoscape> ng = graphVizManager.splitPaths(tqp, inference);
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(ng);
                    return Response.ok(json)
                            .type(MediaType.TEXT_PLAIN)
                            .header("Access-Control-Allow-Headers", "content-type")
                            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                            .build();
                } catch (JsonProcessingException ex) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
                } catch (ManagerException ex) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
                }
            } catch (UnsupportedEncodingException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("inference and concept options must be specified").build();
        }
    }

    @POST
    @Path("allPaths")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/allPaths",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "All paths between two nodes",
            notes = "This method returns all the paths between two nodes according to the input parameters")
    public Response allPaths(@ApiParam(
            name = "key",
            value = "authentication token",
            example = "lexodemo",
            required = true)
            @QueryParam("key") String key,
            HopsFilter hf) throws HelperException {
        return Response.ok()
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

}
