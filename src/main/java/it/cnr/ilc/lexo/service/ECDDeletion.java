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
import it.cnr.ilc.lexo.manager.ECDDeletionManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.input.DictionaryEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.ecd.ECDEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.RelationDeleter;
import it.cnr.ilc.lexo.service.data.lexicon.output.DictionaryEntryItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDComponent;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDEntryItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDEntrySemantics;
import it.cnr.ilc.lexo.service.data.lexicon.output.HitsDataList;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDEntryMorphology;
import it.cnr.ilc.lexo.service.data.lexicon.output.ecd.ECDLexicalFunction;
import it.cnr.ilc.lexo.service.helper.ECDComponentHelper;
import it.cnr.ilc.lexo.service.helper.ECDEntryFilterHelper;
import it.cnr.ilc.lexo.service.helper.ECDEntryMorphologyHelper;
import it.cnr.ilc.lexo.service.helper.ECDEntrySemanticsHelper;
import it.cnr.ilc.lexo.service.helper.ECDLexicalFunctionHelper;
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
@Path("ecd/delete")
@Api("Explanatory Combinatorial Dictionary")
public class ECDDeletion extends Service {

    private static final Logger logger = LoggerFactory.getLogger(ECDDeletion.class);

    private final ECDDeletionManager ecdManager = ManagerFactory.getManager(ECDDeletionManager.class);

    private void userCheck(String key) throws AuthorizationException, ServiceException {
        if (LexOProperties.getProperty("keycloack.freeViewer") != null) {
            if (!LexOProperties.getProperty("keycloack.freeViewer").equals("true")) {
                checkKey(key);
            }
        }
    }

    @GET
    @Path("lexicalFunction")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalFunction",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical function deletion",
            notes = "This method deletes a lexical function relation")
    public Response lexicalFunction(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "lexical function ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            log(Level.INFO, "ecd/delete/lexicalFunction <" + _id + ">");
            return Response.ok(ecdManager.deleteRelation(_id))
                    .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException | ManagerException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "ecd/delete/lexicalFunction: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

}
