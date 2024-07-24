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
import it.cnr.ilc.lexo.manager.BibliographyManager;
import it.cnr.ilc.lexo.manager.LexiconCreationManager;
import it.cnr.ilc.lexo.manager.LexiconUpdateManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.UtilityManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.MultiwordComponentPositionUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.DictionaryEntryUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.EtymologicalLinkUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.EtymologyUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.GenericRelationUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LanguageUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalSenseUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicographicComponentPositionUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LinguisticRelationUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.output.LexicographicComponent;
import it.cnr.ilc.lexo.util.OntoLexEntity;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Level;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("update")
@Api("Update")
public class LexiconUpdate extends Service {

    private final LexiconUpdateManager lexiconManager = ManagerFactory.getManager(LexiconUpdateManager.class);
    private final LexiconCreationManager lexiconCreationManager = ManagerFactory.getManager(LexiconCreationManager.class);
    private final BibliographyManager bibliographyManager = ManagerFactory.getManager(BibliographyManager.class);

    @POST
    @Path("lexicon/language")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "language",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexicon language update",
            notes = "This method updates the lexicon language according to the input updater")
    public Response lexiconLanguage(
            @HeaderParam("Authorization") String key, @QueryParam("id") String id, LanguageUpdater lu) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexiconLanguage(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                return Response.ok(lexiconManager.updateLexiconLanguage(_id, lu))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/language: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("lexicalEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "lexicalEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry update",
            notes = "This method updates the lexical entry according to the input updater")
    public Response lexicalEntry(
            @HeaderParam("Authorization") String key, @QueryParam("id") String id,
            @ApiParam(
                    name = "author",
                    value = "if LexO user management is disabled, the account that is updating the status of the lexical entry",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            LexicalEntryUpdater leu) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            String type = utilityManager.getLexicalEntryType(_id);
            if (type == null) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
            }
            return Response.ok(lexiconManager.updateLexicalEntry(_id, leu, author, type))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException | UnsupportedEncodingException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/lexicalEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("dictionaryEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "dictionaryEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Dictionary entry update",
            notes = "This method updates the dictionary entry according to the input updater")
    public Response dictionaryEntry(
            @HeaderParam("Authorization") String key, @QueryParam("id") String id,
            @ApiParam(
                    name = "author",
                    value = "if LexO user management is disabled, the account that is updating the status of the lexical entry",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            DictionaryEntryUpdater deu) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            if (!utilityManager.isDictionaryEntry(_id)) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
            }
            return Response.ok(lexiconManager.updateDictionaryEntry(_id, deu, author))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException | UnsupportedEncodingException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/dictionaryEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("form")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "form",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Form update",
            notes = "This method updates the form according to the input updater")
    public Response form(@HeaderParam("Authorization") String key, @QueryParam("id") String id, FormUpdater fu) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            if (!utilityManager.isForm(_id)) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
            }
            return Response.ok(lexiconManager.updateForm(_id, fu))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException | UnsupportedEncodingException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/form: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("lexicalSense")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "lexicalSense",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical Sense update",
            notes = "This method updates the sense according to the input updater")
    public Response lexicalSense(@HeaderParam("Authorization") String key, @QueryParam("id") String id, LexicalSenseUpdater lsu) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
            if (!utilityManager.isLexicalSense(_id)) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
            }
            return Response.ok(lexiconManager.updateLexicalSense(_id, lsu))
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (ManagerException | UpdateExecutionException | UnsupportedEncodingException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/lexicalSense: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("etymology")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "etymology",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Etymology update",
            notes = "This method updates the etymology according to the input updater")
    public Response etymology(@HeaderParam("Authorization") String key, @QueryParam("id") String id, EtymologyUpdater eu) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isEtymology(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                return Response.ok(lexiconManager.updateEtymology(_id, eu))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/etymology: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("etymologicalLink")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "etymologicalLink",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Etymological link update",
            notes = "This method updates the etymological link according to the input updater")
    public Response etymologicalLink(@HeaderParam("Authorization") String key,
            @QueryParam("id") String id, EtymologicalLinkUpdater elu) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isEtymologicalLink(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                return Response.ok(lexiconManager.updateEtymologicalLink(_id, elu))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/etymologicalLink: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("linguisticRelation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "linguisticRelation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Linguistic relation update",
            notes = "This method updates a linguistic relation according to the input updater")
    public Response linguisticRelation(@HeaderParam("Authorization") String key, @QueryParam("id") String id, LinguisticRelationUpdater lru) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                // if a lexical entry is associated to a dictionary entry we need to order its lexical senses
                if (lru.getRelation().equals(OntoLexEntity.Lexicog.describes.toString())) {
                    if (lru.getSensesCustomOrder() == null) {
                        UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                        if (utilityManager.isLexicalEntry(lru.getValue())) {
                            Map<String, String> senses = utilityManager.getLexicalSensesByLexicalEntry(lru.getValue());
                            int senseNumber = 1;
                            for (Map.Entry<String, String> entry : senses.entrySet()) {
                                LexicographicComponent lc = lexiconCreationManager.createLexicographicComponent(entry.getValue(), null, entry.getKey().split("#")[0] + "#", null);
                                lexiconManager.addLexicographicComponentOfSense(lru.getValue(), entry.getKey(), lc.getComponent(), senseNumber);
                                senseNumber++;
                            }
                        }
                    }
                }
                String json = lexiconManager.updateLinguisticRelation(_id, lru);
                return Response.ok(json)
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();

            } catch (ManagerException | UpdateExecutionException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/linguisticRelation: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("genericRelation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "genericRelation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Generic relation update",
            notes = "This method updates a generic relation according to the input updater")
    public Response genericRelation(@HeaderParam("Authorization") String key, @QueryParam("id") String id, GenericRelationUpdater gru) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                return Response.ok(lexiconManager.updateGenericRelation(_id, gru))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/genericRelation: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("multiwordComponentPosition")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "multiwordComponentPosition",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Multiword component position update",
            notes = "This method updates the position of a multiword component according to the input updater")
    public Response multiwordComponentPosition(@HeaderParam("Authorization") String key, @QueryParam("id") String id, MultiwordComponentPositionUpdater mcpu) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                return Response.ok(lexiconManager.updateMultiwordComponentPosition(_id, mcpu))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "update/multiwordComponentPosition: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("lexicographicComponentPosition")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "lexicographicComponentPosition",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexicographic Component position update",
            notes = "This method updates the position of a lexicographic component according to the input updater")
    public Response lexicographicComponentPosition(@HeaderParam("Authorization") String key, @QueryParam("id") String id, String _lcpu) {
        try {
            ObjectMapper om = new ObjectMapper();
            LexicographicComponentPositionUpdater lcpu = om.readValue(_lcpu, LexicographicComponentPositionUpdater.class);
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                return Response.ok(lexiconManager.updateLexicographicComponentPosition(_id, lcpu))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "update/lexicographicComponentPosition: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("synchronizeBibliography")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "synchronizeBibliography",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Bibliography synchronization with Zotero",
            notes = "This method synchronized author, date, and title fields of the local repository with the corresponding Zotero values")
    public Response synchronizeBibliography(
            @ApiParam(
                    name = "id",
                    value = "the lexical entity id, the bibliography refers to",
                    required = true)
            @QueryParam("id") String id,
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "author",
                    value = "the account that is creating the bibliographic link (if LexO user management disabled)",
                    example = "user7",
                    required = true)
            @QueryParam("author") String author,
            @ApiParam(
                    name = "itemKey",
                    value = "Zotero item key",
                    example = "AXZSXE45",
                    required = true)
            @QueryParam("itemKey") String itemKey) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            String lastUpdate = "";
            if ((itemKey == null || itemKey.isEmpty())) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("item Key must be defined").build();
            }
            String bibID = bibliographyManager.getBibliographyID(_id, itemKey);
            if (bibID == null) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("item Key is not valid the this lexical entity").build();
            } else {
                try {
                    lastUpdate = bibliographyManager.synchronizeBibliography(bibID, itemKey);
                } catch (RuntimeException e) {
                    if (e.getMessage().contains("404")) {
                        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Zotero: Key " + itemKey + " not found").build();
                    } else {
                        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Zotero: " + e.getMessage()).build();
                    }
                }
            }
            return Response.ok(lastUpdate)
                    .type(MediaType.TEXT_PLAIN)
                    .header("Access-Control-Allow-Headers", "content-type")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                    .build();
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/synchronizeBibliography: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

}
