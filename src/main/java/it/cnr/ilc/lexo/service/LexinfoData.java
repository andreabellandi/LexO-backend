/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.cnr.ilc.lexo.manager.LexiconDataManager;
import it.cnr.ilc.lexo.manager.LexiconUpdateManager;
import it.cnr.ilc.lexo.manager.LexinfoManager;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.vocabulary.PropertyHierarchy;
import it.cnr.ilc.lexo.service.helper.LexicalEntryElementHelper;
import it.cnr.ilc.lexo.service.helper.LexinfoMorphoHelper;
import it.cnr.ilc.lexo.service.helper.LexinfoPropertyHierarchyHelper;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("lexinfo/data")
@Api("Lexinfo vocabulary")
public class LexinfoData extends Service {

    private final LexinfoManager lexiconManager = ManagerFactory.getManager(LexinfoManager.class);
    private final LexinfoMorphoHelper lexinfoMorphoHelper = new LexinfoMorphoHelper();
    private final LexinfoPropertyHierarchyHelper lexinfoPropertyHierarchyHelper = new LexinfoPropertyHierarchyHelper();
 
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
//        log(Level.INFO, "get lexicon entries types");
        String json = lexinfoMorphoHelper.toJson(lexiconManager.getMorpho());
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }
    
//    @GET
//    @Path("lexicalRelations")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "variation and translation",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Lexical relations",
//            notes = "This method returns the lexical relations of the vartrans module definied in the Lexinfo vocabulary")
//    public Response lexiclRel() {
////        log(Level.INFO, "get lexicon entries types");
//        PropertyHierarchy ph = new PropertyHierarchy();
//        ph.setPropertyId("prop1ID");
//        ph.setPropertyLabel("propLabel1");
//        PropertyHierarchy ph12 = new PropertyHierarchy();
//        ph12.setPropertyId("sss");
//        ph12.setPropertyLabel("fff");
//        PropertyHierarchy ph121 = new PropertyHierarchy();
//        ph121.setPropertyId("ww");
//        ph121.setPropertyLabel("ll");
//        ArrayList<Object> a = new ArrayList();
//        ArrayList<Object> b = new ArrayList();
//        a.add(ph121);
//        ph12.setChildren(a);
//        b.add(ph12);
//        ph.setChildren(b);
//        String json = lexinfoPropertyHierarchyHelper.toJson(ph);
//        return Response.ok(json)
//                .type(MediaType.TEXT_PLAIN)
//                .header("Access-Control-Allow-Headers", "content-type")
//                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
//                .build();
//    }

}
