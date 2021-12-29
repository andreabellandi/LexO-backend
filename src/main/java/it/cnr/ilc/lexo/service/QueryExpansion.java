/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.QueryExpansionManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.ConceptList;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormBySenseFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryList;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormByLexicalEntry;
import it.cnr.ilc.lexo.service.data.lexicon.output.queryExpansion.FormList;
import it.cnr.ilc.lexo.service.data.lexicon.output.ReferencedLinguisticObject;
import it.cnr.ilc.lexo.service.data.lexicon.output.RelationPath;
import it.cnr.ilc.lexo.service.data.lexicon.output.queryExpansion.Form;
import it.cnr.ilc.lexo.service.helper.FormByLexicalEntryHelper;
import it.cnr.ilc.lexo.service.helper.FormHelper;
import it.cnr.ilc.lexo.service.helper.FormListHelper;
import it.cnr.ilc.lexo.service.helper.HelperException;
import it.cnr.ilc.lexo.service.helper.PathLenghtHelper;
import it.cnr.ilc.lexo.service.helper.ReferencedLinguisticObjectListHelper;
import it.cnr.ilc.lexo.util.EnumUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
    private final FormListHelper formListHelper = new FormListHelper();
    private final PathLenghtHelper pathLenghtHelper = new PathLenghtHelper();
    private final FormHelper formHelper = new FormHelper();

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

    @POST
    @Path("formsBySense")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/formsBySense",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Forms list",
            notes = "This method returns a list of forms according to the form by sense input filter")
    public Response formsBySense(@QueryParam("key") String key, FormBySenseFilter ff) throws HelperException {
        try {
            List<FormList> list = new ArrayList();
            TupleQueryResult res = queryExpansionManager.getFilterdForms(ff);
            String targetSense = "", targetSenseInstanceName = "";
            list.add(new FormList("", 0, "", "",
                    formHelper.newDataList(res)));
            if (!ff.getExtendTo().equals(EnumUtil.AcceptedSearchFormExtendTo.None.toString())) {
                for (String sense : ff.getSenseUris()) {
                    if (ff.getExtendTo().equals(EnumUtil.AcceptedSearchFormExtendTo.Hypernym.toString())
                            || ff.getExtendTo().equals(EnumUtil.AcceptedSearchFormExtendTo.Hyponym.toString())) {
                        int lenght = 1;
                        List<Form> forms = new ArrayList();

                        List<RelationPath> rps = pathLenghtHelper.newDataList(queryExpansionManager.getRelationByLenght(ff.getExtendTo(), sense));
                        int blockSize = 0, c = 0, blockNumber = 0;
                        String senseIDs = "";
                        for (int count = 0; count < rps.size(); count++) {
                            if (ff.getExtensionDegree() >= rps.get(count).getLenght()) {
                                if (lenght == rps.get(count).getLenght() && blockNumber < 6) {
                                    if (blockSize == 100) {
                                        TupleQueryResult _res = queryExpansionManager.getFilterdForms(senseIDs.substring(0, senseIDs.length() - 4));
                                        List<Form> _forms = new ArrayList();
                                        _forms.addAll(formHelper.newDataList(_res));
                                        forms.addAll(_forms);
                                        System.out.println(c/blockSize);
                                        blockSize = 0;
                                        blockNumber++;
                                        senseIDs = "\\\"" + rps.get(count).getLexicalSense() + "\\\"" + " OR ";
                                    } else {
                                        senseIDs = senseIDs + "\\\"" + rps.get(count).getLexicalSense() + "\\\"" + " OR ";
                                    }
                                    blockSize++;
                                    c++;
                                } else {
                                    if (blockNumber == 6) {
                                        break;
                                    }
                                    TupleQueryResult _res = queryExpansionManager.getFilterdForms(senseIDs.substring(0, senseIDs.length() - 4));
                                    List<Form> _forms = new ArrayList();
                                    _forms.addAll(formHelper.newDataList(_res));
                                    forms.addAll(_forms);
                                    list.add(new FormList(ff.getExtendTo(), lenght, queryExpansionManager.getNamespace() + sense, sense,
                                            queryExpansionManager.getFormItemListCopy(forms)));
                                    forms.clear();
                                    lenght++;
                                    blockSize = 0;
                                    senseIDs = "\\\"" + rps.get(count).getLexicalSense() + "\\\"" + " OR ";
                                }
                            }
                        }

                        if (!senseIDs.isEmpty()) {
                            TupleQueryResult _res = queryExpansionManager.getFilterdForms(senseIDs.substring(0, senseIDs.length() - 4));
                            List<Form> _forms = new ArrayList();
                            _forms.addAll(formHelper.newDataList(_res));
                            forms.addAll(_forms);
                            list.add(new FormList(ff.getExtendTo(), lenght, queryExpansionManager.getNamespace() + sense, sense,
                                    queryExpansionManager.getFormItemListCopy(forms)));
                        }

                    } else if (ff.getExtendTo().equals(EnumUtil.AcceptedSearchFormExtendTo.Synonym.toString())) {
                        TupleQueryResult _resForms = queryExpansionManager.getFormsBySenseRelation(ff, sense);
                        if (_resForms.hasNext()) {
                            List<Form> lfi = formHelper.newDataList(_resForms);
                            List<Form> newFormList = new ArrayList<>();
                            String target = "", targetInstanceName = "";
                            for (Form fi : lfi) {
                                if (target.isEmpty()) {
                                    targetInstanceName = fi.getTargetSenseInstanceName();
                                }
                                if (targetInstanceName.equals(fi.getTargetSenseInstanceName())) {
                                    newFormList.add(fi);
                                } else {
                                    list.add(new FormList("synonym", 1, queryExpansionManager.getNamespace() + sense, sense,
                                            queryExpansionManager.getFormItemListCopy(newFormList)));
                                    targetInstanceName = fi.getTargetSenseInstanceName();
                                    newFormList.clear();
                                }
                            }
                            list.add(new FormList("synonym", 1, queryExpansionManager.getNamespace() + sense, sense,
                                    queryExpansionManager.getFormItemListCopy(newFormList)));
                        }
                    }
                }
            }

//                        
//                        for (RelationPath c : pathLenghtHelper.newDataList(queryExpansionManager.getRelationByLenght(ff.getExtendTo(), sense))) {
//                            if (ff.getExtensionDegree() >= c.getLenght()) {
//                                if (lenght == c.getLenght()) {
//                                    targetSense = c.getLexicalSense();
//                                    targetSenseInstanceName = c.getLexicalSenseInstanceName();
//                                    TupleQueryResult _res = queryExpansionManager.getFilterdForms(c.getLexicalEntryInstanceName());
//                                    List<FormItem> _forms = new ArrayList();
//                                    _forms.addAll(formItemsHelper.newDataList(_res));
//                                    for (FormItem fi : _forms) {
//                                        fi.setTargetSense(targetSense);
//                                        fi.setTargetSenseInstanceName(targetSenseInstanceName);
//                                    }
//                                    forms.addAll(_forms);
//                                } else {
//                                    list.add(new FormList(ff.getExtendTo(), lenght, queryExpansionManager.getNamespace() + sense, sense,
//                                            queryExpansionManager.getFormItemListCopy(forms)));
//                                    forms.clear();
//                                    lenght++;
//                                    targetSense = c.getLexicalSense();
//                                    targetSenseInstanceName = c.getLexicalSenseInstanceName();
//                                    TupleQueryResult _res = queryExpansionManager.getFilterdForms(c.getLexicalEntryInstanceName());
//                                    List<FormItem> _forms = new ArrayList();
//                                    _forms.addAll(formItemsHelper.newDataList(_res));
//                                    for (FormItem fi : _forms) {
//                                        fi.setTargetSense(targetSense);
//                                        fi.setTargetSenseInstanceName(targetSenseInstanceName);
//                                    }
//                                    forms.addAll(_forms);
//                                }
//                            }
//                        }
//                        list.add(new FormList(ff.getExtendTo(), lenght, queryExpansionManager.getNamespace() + sense, sense,
//                                queryExpansionManager.getFormItemListCopy(forms)));
//                    } else if (ff.getExtendTo().equals(EnumUtil.AcceptedSearchFormExtendTo.Synonym.toString())) {
//                        TupleQueryResult _resForms = queryExpansionManager.getFormsBySenseRelation(ff, sense);
//                        if (_resForms.hasNext()) {
//                            List<FormItem> lfi = formItemsHelper.newDataList(_resForms);
//                            List<FormItem> newFormList = new ArrayList<>();
//                            String target = "", targetInstanceName = "";
//                            for (FormItem fi : lfi) {
//                                if (target.isEmpty()) {
//                                    target = fi.getTargetSense();
//                                    targetInstanceName = fi.getTargetSenseInstanceName();
//                                }
//                                if (target.equals(fi.getTargetSense())) {
//                                    newFormList.add(fi);
//                                } else {
//                                    list.add(new FormList("synonym", 1, queryExpansionManager.getNamespace() + sense, sense,
//                                            queryExpansionManager.getFormItemListCopy(newFormList)));
//                                    target = fi.getTargetSense();
//                                    targetInstanceName = fi.getTargetSenseInstanceName();
//                                    newFormList.clear();
//                                }
//                            }
//                            list.add(new FormList("synonym", 1, queryExpansionManager.getNamespace() + sense, sense,
//                                    queryExpansionManager.getFormItemListCopy(newFormList)));
//                        }
//                    }
//                }
//            }
            String json = formListHelper.toJson(list);
            return Response.ok(json)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException ex) {
            logger.error(ex.getMessage(), ex);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

}
