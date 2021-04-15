/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.cnr.ilc.lexo.manager.LexiconDataManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryElementItem;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalFilter;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseItem;
import it.cnr.ilc.lexo.service.helper.FormsListHelper;
import it.cnr.ilc.lexo.service.helper.HelperException;
import it.cnr.ilc.lexo.service.helper.LexicalEntryAttestationLinkHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryCoreHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryFilterHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryElementHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryMultimediaLinkHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryOtherLinkHelper;
import it.cnr.ilc.lexo.service.helper.LexicalEntryReferenceLinkHelper;
import it.cnr.ilc.lexo.service.helper.LexicalSenseFilterHelper;
import it.cnr.ilc.lexo.util.EnumUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("lexicon/data")
@Api("Lexicon data")
public class LexiconData {

    private final LexiconDataManager lexiconManager = ManagerFactory.getManager(LexiconDataManager.class);
    private final LexicalEntryFilterHelper lexicalEntryFilterHelper = new LexicalEntryFilterHelper();
    private final LexicalSenseFilterHelper lexicalSenseFilterHelper = new LexicalSenseFilterHelper();
    private final FormsListHelper formsListHelper = new FormsListHelper();
    private final LexicalEntryElementHelper lexicalEntryElementHelper = new LexicalEntryElementHelper();
    private final LexicalEntryCoreHelper lexicalEntryCoreHelper = new LexicalEntryCoreHelper();
    private final LexicalEntryReferenceLinkHelper lexicalEntryReferenceLinkHelper = new LexicalEntryReferenceLinkHelper();
    private final LexicalEntryAttestationLinkHelper lexicalEntryAttestationLinkHelper = new LexicalEntryAttestationLinkHelper();
    private final LexicalEntryMultimediaLinkHelper lexicalEntryMultimediaLinkHelper = new LexicalEntryMultimediaLinkHelper();
    private final LexicalEntryOtherLinkHelper lexicalEntryOtherLinkHelper = new LexicalEntryOtherLinkHelper();

    @GET
    @Path("{id}/lexicalEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/lexicalEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry",
            notes = "This method returns the data related to a specific aspect (morphology, syntax, ...) associated with a given lexical entry")
    public Response lexicalEntry(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "aspect",
                    allowableValues = "core, decomposition, etymology, variation and translation, syntax and semantics",
                    example = "core",
                    required = true)
            @QueryParam("aspect") String aspect,
            @ApiParam(
                    name = "id",
                    value = "lexical entry ID",
                    example = "MUSaccedereVERB",
                    required = true)
            @PathParam("id") String id) {
        try {
            TupleQueryResult lexicalEntry = lexiconManager.getLexicalEntry(id, aspect);
            if (aspect.equals(EnumUtil.LexicalAspects.Core.toString())) {
                LexicalEntryCore lec = lexicalEntryCoreHelper.newData(lexicalEntry);
                TupleQueryResult lexicalEntryReferenceLinks = lexiconManager.getLexicalEntryReferenceLinks(id);
                LexicalEntryElementItem referenceLinks = lexicalEntryReferenceLinkHelper.newData(lexicalEntryReferenceLinks);
                lexiconManager.addLexicalEntryLinks(lec, referenceLinks, 
                        new LexicalEntryElementItem("Multimedia", new ArrayList()), 
                        new LexicalEntryElementItem("Attestation", new ArrayList()), 
                        new LexicalEntryElementItem("Other", new ArrayList()));
                String json = lexicalEntryCoreHelper.toJson(lec);
                return Response.ok(json)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .allow("OPTIONS")
                        .build();
            }
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("lexical aspect not available").build();
        } catch (ManagerException ex) {
            Logger.getLogger(LexiconData.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("lexicalSenses")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/lexicalSenses",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical senses list",
            notes = "This method returns a list of lexical senses according to the input filter")
    public Response sensesList(@QueryParam("key") String key, LexicalFilter lef) throws HelperException {
        try {
            TupleQueryResult lexicalSenses = lexiconManager.getFilterdLexicalSenses(lef);
            List<LexicalSenseItem> entries = lexicalSenseFilterHelper.newDataList(lexicalSenses);
            String json = lexicalSenseFilterHelper.toJson(entries);
            return Response.ok(json)
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            Logger.getLogger(LexiconData.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("lexicalEntries")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/lexicalEntries",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entries list",
            notes = "This method returns a list of lexical entries according to the input filter")
    public Response entriesList(@QueryParam("key") String key, LexicalFilter lef) throws HelperException {
        try {
            TupleQueryResult lexicalEnties = lexiconManager.getFilterdLexicalEntries(lef);
            List<LexicalEntryItem> entries = lexicalEntryFilterHelper.newDataList(lexicalEnties);
            String json = lexicalEntryFilterHelper.toJson(entries);
            return Response.ok(json)
                    .type(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            Logger.getLogger(LexiconData.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{id}/forms")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/forms",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry forms",
            notes = "This method returns all the forms of a lexical entry")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = FormItem.class),
        @ApiResponse(code = 201, message = ""),
        @ApiResponse(code = 400, message = "")
    })
    public Response forms(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "id",
                    value = "lexical entry ID",
                    example = "MUSaccedereVERB",
                    required = true)
            @PathParam("id") String id) {
//        log(Level.INFO, "get lexicon entries types");
        TupleQueryResult _forms = lexiconManager.getForms(id);
        List<FormItem> forms = formsListHelper.newDataList(_forms);
        String json = formsListHelper.toJson(forms);
        return Response.ok(json)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS")
                .build();
    }

    @GET
    @Path("{id}/elements")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/elements",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry elements",
            notes = "This method returns the counting of all the elements of a lexical entry (forms, senses, frames, etc ...)")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = Counting.class),
        @ApiResponse(code = 201, message = ""),
        @ApiResponse(code = 400, message = "")
    })
    public Response elements(
            @ApiParam(
                    name = "key",
                    value = "authentication token",
                    example = "lexodemo",
                    required = true)
            @QueryParam("key") String key,
            @ApiParam(
                    name = "id",
                    value = "lexical entry ID",
                    example = "MUSaccedereVERB",
                    required = true)
            @PathParam("id") String id) {
//        log(Level.INFO, "get lexicon entries types");
        TupleQueryResult _elements = lexiconManager.getElements(id);
        LexicalEntryElementItem elements = lexicalEntryElementHelper.newData(_elements);
        String json = lexicalEntryElementHelper.toJson(elements);
        return Response.ok(json)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS")
                .build();
    }

}
