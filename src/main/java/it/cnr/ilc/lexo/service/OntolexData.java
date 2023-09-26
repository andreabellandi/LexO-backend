/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.OntolexManager;
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
@Path("ontolex/data")
@Api("Linguistic vocabulary")
public class OntolexData extends Service {

    private final OntolexManager lexiconManager = ManagerFactory.getManager(OntolexManager.class);
    private final VocabularyValuesHelper ontolexValuesHelper = new VocabularyValuesHelper();
 
    @GET
    @Path("lexicalEntryType")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalEntryType",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry types from the OntoLex vocabulary",
            notes = "This method returns the lexical entry types from the OntoLex vocabulary")
    public Response lexicalEntryType() {
//        log(Level.INFO, "get lexicon entries types");
        String json = ontolexValuesHelper.toJson(lexiconManager.getLexicalEntryTypes());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }
    
    @GET
    @Path("etymologicalEntryType")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "etymologicalEntryType",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Etymological entry types from the LemonEty module of the OntoLex vocabulary",
            notes = "This method returns the etymological entry types from the LemonEty module of the OntoLex vocabulary")
    public Response etymologicalEntryType() {
//        log(Level.INFO, "get lexicon entries types");
        String json = ontolexValuesHelper.toJson(lexiconManager.getEtymologicalEntryTypes());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }
    
    @GET
    @Path("formType")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "formType",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Form types from the OntoLex vocabulary",
            notes = "This method returns the form types from the OntoLex vocabulary")
    public Response formType() {
//        log(Level.INFO, "get lexicon entries types");
        String json = ontolexValuesHelper.toJson(lexiconManager.getFormTypes());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }
    
    @GET
    @Path("translationType")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "translationType",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Translation types from the OntoLex vocabulary",
            notes = "This method returns the translation types from the OntoLex vocabulary")
    public Response translationType() {
        String json = ontolexValuesHelper.toJson(lexiconManager.getTranslationTypes());
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
    @ApiOperation(value = "Forms represenation properties from the OntoLex vocabulary",
            notes = "This method returns the forms represenatation properties from the OntoLex vocabulary")
    public Response representation() {
        String json = ontolexValuesHelper.toJson(lexiconManager.getRepresentations());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }
    
    
}
