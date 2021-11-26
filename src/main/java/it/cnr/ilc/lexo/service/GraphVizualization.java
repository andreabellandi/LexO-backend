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
import it.cnr.ilc.lexo.manager.LexiconDataManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.QueryExpansionManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.ConceptList;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormBySenseFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryElementItem;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryList;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalSenseFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.graphViz.NodeGraphFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormByLexicalEntry;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.queryExpansion.FormList;
import it.cnr.ilc.lexo.service.data.lexicon.output.HitsDataList;
import it.cnr.ilc.lexo.service.data.lexicon.output.Language;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.service.data.lexicon.output.ReferencedLinguisticObject;
import it.cnr.ilc.lexo.service.data.lexicon.output.RelationPath;
import it.cnr.ilc.lexo.service.data.lexicon.output.pippo.NodeLinks;
import it.cnr.ilc.lexo.service.data.lexicon.output.pippo.SenseNodeSummary;
import it.cnr.ilc.lexo.service.data.lexicon.output.pippo.Cytoscape;
import it.cnr.ilc.lexo.service.data.lexicon.output.queryExpansion.Form;
import it.cnr.ilc.lexo.service.helper.CountingHelper;
import it.cnr.ilc.lexo.service.helper.FormByLexicalEntryHelper;
import it.cnr.ilc.lexo.service.helper.FormCoreHelper;
import it.cnr.ilc.lexo.service.helper.FormHelper;
import it.cnr.ilc.lexo.service.helper.FormItemsHelper;
import it.cnr.ilc.lexo.service.helper.FormListHelper;
import it.cnr.ilc.lexo.service.helper.HelperException;
import it.cnr.ilc.lexo.service.helper.LanguageHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryCoreHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryFilterHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryElementHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryReferenceLinkHelper;
import it.cnr.ilc.lexo.service.helper.LexicalSenseCoreHelper;
import it.cnr.ilc.lexo.service.helper.LexicalSenseFilterHelper;
import it.cnr.ilc.lexo.service.helper.LinkedEntityHelper;
import it.cnr.ilc.lexo.service.helper.NodeLinksHelper;
import it.cnr.ilc.lexo.service.helper.PathLenghtHelper;
import it.cnr.ilc.lexo.service.helper.ReferencedLinguisticObjectListHelper;
import it.cnr.ilc.lexo.service.helper.SenseNodeSummaryHelper;
import it.cnr.ilc.lexo.util.EnumUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            notes = "This method returns the incoming and outcoming edges of a node")
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
            TupleQueryResult outcoming = graphVizManager.getNodeGraph(id, ngf, false);
            Cytoscape ng = graphVizManager.getNodeGraph(incoming, outcoming, ngf.getRelation());
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

}