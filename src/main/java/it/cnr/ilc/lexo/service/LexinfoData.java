/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.cnr.ilc.lexo.manager.LexinfoManager;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.helper.LexinfoMorphoHelper;
import it.cnr.ilc.lexo.service.helper.PropertyHierachyHelper;
import it.cnr.ilc.lexo.service.helper.VocabularyValuesHelper;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("lexinfo/data")
@Api("Linguistic vocabulary")
public class LexinfoData extends Service {

    private final LexinfoManager lexiconManager = ManagerFactory.getManager(LexinfoManager.class);
    private final LexinfoMorphoHelper lexinfoMorphoHelper = new LexinfoMorphoHelper();
    private final VocabularyValuesHelper vocabularyValuesHelper = new VocabularyValuesHelper();
    private final PropertyHierachyHelper propertyHierachyHelper = new PropertyHierachyHelper();

    @GET
    @Path("morphology")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "morphology",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Morphological traits and their values",
            notes = "This method returns the morphological traits with their values defined in the Lexinfo vocabulary")
    public Response morpho() {
        String json = lexinfoMorphoHelper.toJson(lexiconManager.getMorpho());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

    @GET
    @Path("representation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "representation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Representation properties for forms",
            notes = "This method returns all the representation properties (segmentation, romanization, transliterations")
    public Response representation() {
        String json = vocabularyValuesHelper.toJson(lexiconManager.getRepresentationProperties());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

    @GET
    @Path("senseDefinition")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "senseDefinition",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Definition properties for senses",
            notes = "This method returns all the definition properties for sense (example, translation, gloss")
    public Response senseDefinition() {
        String json = vocabularyValuesHelper.toJson(lexiconManager.getSenseProperties());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

    @GET
    @Path("lexicalRelations")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalRelations",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical relations",
            notes = "This method returns the lexical relations definied in the Lexinfo vocabulary")
    public Response lexicalRel() {
        String json = propertyHierachyHelper.toJson(lexiconManager.getLexicalRel());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

    @GET
    @Path("senseRelations")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "senseRelations",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Sense relations",
            notes = "This method returns the sense relations definied in the Lexinfo vocabulary")
    public Response senseRel() {
        String json = propertyHierachyHelper.toJson(lexiconManager.getSenseRel());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }
    
    @GET
    @Path("formRelations")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "formRelations",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Form relations",
            notes = "This method returns the form relations definied in the Lexinfo vocabulary")
    public Response formRel() {
        String json = propertyHierachyHelper.toJson(lexiconManager.getFormRel());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

    @GET
    @Path("usage")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "usage",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Usage relations",
            notes = "This method returns the usage relations to be associated with senses")
    public Response usageRel() {
        String json = lexinfoMorphoHelper.toJson(lexiconManager.getUsage());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

    @GET
    @Path("lexicalCategories")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalCategories",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical categories",
            notes = "This method returns some categories for lexical entries")
    public Response lexicalCategories() {
        String json = lexinfoMorphoHelper.toJson(lexiconManager.getLexicalCategory());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }
    
    @GET
    @Path("semanticCategories")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "semanticCategories",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Semantic categories",
            notes = "This method returns some categories for lexical senses")
    public Response semanticCategories() {
        String json = lexinfoMorphoHelper.toJson(lexiconManager.getSemanticCategory());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }
    
    @GET
    @Path("formCategories")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "formCategories",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Form categories",
            notes = "This method returns some categories for forms")
    public Response formCategories() {
        String json = lexinfoMorphoHelper.toJson(lexiconManager.getFormCategory());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

}
