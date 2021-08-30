/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormByLexicalEntry;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormList;
import it.cnr.ilc.lexo.service.data.lexicon.output.HitsDataList;
import it.cnr.ilc.lexo.service.data.lexicon.output.Language;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalEntryItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseCore;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicalSenseItem;
import it.cnr.ilc.lexo.service.data.lexicon.output.LinkedEntity;
import it.cnr.ilc.lexo.service.data.lexicon.output.ReferencedLinguisticObject;
import it.cnr.ilc.lexo.service.data.lexicon.output.RelationPath;
import it.cnr.ilc.lexo.service.helper.FormByLexicalEntryHelper;
import it.cnr.ilc.lexo.service.helper.FormCoreHelper;
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
import it.cnr.ilc.lexo.service.helper.PathLenghtHelper;
import it.cnr.ilc.lexo.service.helper.ReferencedLinguisticObjectListHelper;
import it.cnr.ilc.lexo.util.EnumUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@Path("queryExpansion")
@Api("Query Expansion Support")
public class QueryExpansion extends Service {

    private static final Logger logger = LoggerFactory.getLogger(QueryExpansion.class);
    Logger statLog = LoggerFactory.getLogger("statistics");

    private final QueryExpansionManager queryExpansionManager = ManagerFactory.getManager(QueryExpansionManager.class);
    private final ReferencedLinguisticObjectListHelper referencedLinguisticObjectListHelper = new ReferencedLinguisticObjectListHelper();
    private final FormByLexicalEntryHelper formByLexicalEntryHelper = new FormByLexicalEntryHelper();
 
    @POST
    @Path("referencedLinguisticObject")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/referencedLinguisticObject",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Linguistic objects list",
            notes = "This method returns a list of linguistic objects starting from a list of concepts")
    public Response lemmaByConcept(@QueryParam("key") String key, ConceptList cl) throws HelperException, ManagerException {
        TupleQueryResult lingObjs = queryExpansionManager.getReferencedLinguisticObject(cl);
        List<ReferencedLinguisticObject> los = referencedLinguisticObjectListHelper.newDataList(lingObjs);
        String json = referencedLinguisticObjectListHelper.toJson(los);
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }
    
    @POST
    @Path("forms")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/forms",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry forms",
            notes = "This method returns all the forms of a set of lexical entries according to the input")
    public Response forms(@QueryParam("key") String key, LexicalEntryList lel) throws ManagerException {
        TupleQueryResult _forms = queryExpansionManager.getForms(lel);
        List<FormByLexicalEntry> forms = formByLexicalEntryHelper.newDataList(_forms);
        String json = formByLexicalEntryHelper.toJson(forms);
        return Response.ok(json)
                .type(MediaType.TEXT_PLAIN)
                .header("Access-Control-Allow-Headers", "content-type")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                .build();
    }

}
