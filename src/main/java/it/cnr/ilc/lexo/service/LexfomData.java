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
import it.cnr.ilc.lexo.manager.LexfomDataManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalFunction;
import it.cnr.ilc.lexo.service.helper.HelperException;
import it.cnr.ilc.lexo.service.helper.LexicalFunctionHelper;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Level;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Abdou
 */
@Path("lexfom/data")
@Api("Linguistic vocabulary")
public class LexfomData extends Service {


    private final LexicalFunctionHelper lexicalFunctionHelper = new LexicalFunctionHelper();
    private final LexfomDataManager lexfomDataManager = new LexfomDataManager();

    private void userCheck(String key) throws AuthorizationException, ServiceException {
        if (LexOProperties.getProperty("keycloack.freeViewer") != null) {
            if (!LexOProperties.getProperty("keycloack.freeViewer").equals("true")) {
                checkKey(key);
            }
        }
    }
    
    @GET
    @Path("lexicalFunctions")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalFunctions",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical functions list",
            notes = "This method returns a list of lexical function according to the input LF")
    public Response lexicalFunctions(@HeaderParam("Authorization") String key,
    @ApiParam(
                    name = "type",
                    value = "Type of lexical function"
              )
            @QueryParam("type") String type 
    )
    throws HelperException, UnsupportedEncodingException {{
     try {   
        userCheck(key);
        TupleQueryResult lexicalfunction = lexfomDataManager.getLexicalfunction(type);
        List<LexicalFunction> lexicalFunction = lexicalFunctionHelper.newDataList(lexicalfunction);     
        String json = lexicalFunctionHelper.toJson(lexicalFunction);
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }
     catch (ManagerException | AuthorizationException | ServiceException ex) {
            log(Level.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }
    }
    /* ---------------------------------- */
    
}
