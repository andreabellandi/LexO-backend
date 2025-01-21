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
import it.cnr.ilc.lexo.manager.ECDDataManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.input.DictionaryEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.ECDEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.ECDComponent;
import it.cnr.ilc.lexo.service.data.lexicon.output.ECDEntryItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.ECDEntryTree;
import it.cnr.ilc.lexo.service.data.lexicon.output.HitsDataList;
import it.cnr.ilc.lexo.service.helper.ECDComponentHelper;
import it.cnr.ilc.lexo.service.helper.ECDEntryFilterHelper;
import it.cnr.ilc.lexo.service.helper.ECDEntryTreeHelper;
import it.cnr.ilc.lexo.service.helper.HelperException;
import it.cnr.ilc.lexo.util.LogUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Level;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("ecd/data")
@Api("Explanatory Combinatorial Dictionary")
public class ECDData extends Service {

    private static final Logger logger = LoggerFactory.getLogger(ECDData.class);

    private final ECDDataManager ecdManager = ManagerFactory.getManager(ECDDataManager.class);
    private final ECDComponentHelper ECDComponentHelper = new ECDComponentHelper();
    private final ECDEntryTreeHelper ECDEntryTreeHelper = new ECDEntryTreeHelper();
    private final ECDEntryFilterHelper ECDEntryFilterHelper = new ECDEntryFilterHelper();
    

    private void userCheck(String key) throws AuthorizationException, ServiceException {
        if (LexOProperties.getProperty("keycloack.freeViewer") != null) {
            if (!LexOProperties.getProperty("keycloack.freeViewer").equals("true")) {
                checkKey(key);
            }
        }
    }

    @GET
    @Path("ECDComponents")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "ECDComponents",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Dictionary entry components",
            notes = "This method returns the elements belonging to a given dictionary entry component")
    public Response ECDComponents(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "ECD component ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            userCheck(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            log(Level.INFO, "ecd/data/ECDComponents: <" + _id + ">");
            TupleQueryResult comps = ecdManager.getECDComponents(_id);
            List<ECDComponent> lcs = ECDComponentHelper.newDataList(comps);
            String json = ECDComponentHelper.toJson(lcs);
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UnsupportedEncodingException | AuthorizationException | ServiceException ex) {
            log(Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("ECDEntryTree")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "ECDEntryTree",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Dictionary entry tree structure",
            notes = "This method returns the tree of all the elements belonging to a given dictionary entry")
    public Response ECDEntryTree(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "dictionary entry ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            userCheck(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            log(Level.INFO, "ecd/data/dictionaryEntryTree: <" + _id + ">");
            List<ECDEntryTree> tree = getTreeNode(_id);
            String json = ECDEntryTreeHelper.toJson(tree);
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UnsupportedEncodingException | AuthorizationException | ServiceException ex) {
            log(Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    private List<ECDEntryTree> getTreeNode(String id) throws ManagerException {
        TupleQueryResult comps = ecdManager.getECDComponents(id);
        List<ECDComponent> lcs = ECDComponentHelper.newDataList(comps);
        List<ECDEntryTree> tree = new ArrayList<>();
        ECDEntryTree _tree;
        for (ECDComponent component : lcs) {
            _tree = new ECDEntryTree(component);
            tree.add(_tree);
            _tree.setChildren(getTreeNode(_tree.getId()));
        }
        return tree;
    }

    @POST
    @Path("ECDEntries")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "ECDEntries",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Dictionary entries list",
            notes = "This method returns a list of dictionary entries according to the input filter")
    public Response ECDEntryList(@HeaderParam("Authorization") String key, ECDEntryFilter def) throws HelperException {
        try {
            userCheck(key);
            log(Level.INFO, "data/ECDEntries\n" + LogUtil.getLogFromPayload(def));
            TupleQueryResult ECDEntries = ecdManager.getFilterdECDEntries(def);
            String json = "";
            if (ECDEntries != null) {
                List<ECDEntryItem> entries = ECDEntryFilterHelper.newDataList(ECDEntries);
                HitsDataList hdl = new HitsDataList(ECDEntryFilterHelper.getTotalHits(), entries);
                json = ECDEntryFilterHelper.toJson(hdl);
            } else {
                HitsDataList hdl = new HitsDataList(0, new ArrayList<>());
                json = ECDEntryFilterHelper.toJson(hdl);
            }
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | AuthorizationException | ServiceException ex) {
            log(Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }
}
