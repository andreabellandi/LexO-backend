/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.manager.BibliographyManager;
import it.cnr.ilc.lexo.manager.LexiconDataManager;
import it.cnr.ilc.lexo.manager.LexiconDeletionManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.SKOSManager;
import it.cnr.ilc.lexo.manager.UtilityManager;
import it.cnr.ilc.lexo.service.data.lexicon.input.RelationDeleter;
import it.cnr.ilc.lexo.service.data.lexicon.output.EtymologicalLink;
import it.cnr.ilc.lexo.service.helper.EtymologicalLinkHelper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.log4j.Level;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("delete")
@Api("Deletion")
public class LexiconDeletion extends Service {

    private final LexiconDeletionManager lexiconManager = ManagerFactory.getManager(LexiconDeletionManager.class);
    private final LexiconDataManager lexiconDataManager = ManagerFactory.getManager(LexiconDataManager.class);
    private final SKOSManager skosManager = ManagerFactory.getManager(SKOSManager.class);
    private final BibliographyManager bibliographyManager = ManagerFactory.getManager(BibliographyManager.class);
    private final EtymologicalLinkHelper etymologicalLinkHelper = new EtymologicalLinkHelper();

    @GET
    @Path("language")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "language",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexicon language deletion",
            notes = "This method deletes a lexicon language, if it has no entries")
    public Response language(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "lexicon language ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                checkKey(key);
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexiconLanguage(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                if (utilityManager.lexicalEntriesNumberByLanguage(_id) != 0) {
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " cannot be deleted. Remove all its entries first").build();
                }
                lexiconManager.deleteLexiconLanguage(_id);
                return Response.ok()
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            } catch (AuthorizationException | ServiceException ex) {
                log(Level.ERROR, "lexicon/delete/language: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("dictionary")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "dictionary",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Dictionary deletion",
            notes = "This method deletes a dictionary, if it has no entries")
    public Response dictionary(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "dictionary ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                checkKey(key);
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isDictionary(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                if (utilityManager.dictionaryHasEntry(_id)) {
                    return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " cannot be deleted. Remove all its entries first").build();
                }
                lexiconManager.deleteDictionary(_id);
                return Response.ok()
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            } catch (AuthorizationException | ServiceException ex) {
                log(Level.ERROR, "lexicon/delete/dictionary: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("lexicalEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry deletion",
            notes = "This method deletes a lexical entry")
    public Response lexicalEntry(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "lexical entry ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexicalEntry(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                return Response.ok(lexiconManager.deleteLexicalEntry(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/lexicalEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("dictionaryEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "dictionaryEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Dictionary entry deletion",
            notes = "This method deletes a dictionary entry")
    public Response dictionaryEntry(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "dictionary entry ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isDictEntry(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                return Response.ok(lexiconManager.deleteDictionaryEntry(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/dictionaryEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("lexicographicComponent")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicographicComponent",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexicographic component deletion",
            notes = "This method deletes a lexicographic component")
    public Response lexicographicComponent(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "lexicographic component ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                String superLexComp = utilityManager.getSuperLexicographicComponent(id);
                if (superLexComp == null) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " is not a Lexicographic Component or it has no position setted").build();
                }
                TreeMap<Integer, String> map = utilityManager.getLexicographicComponetsPositions(id);
                return Response.ok(lexiconManager.deleteLexicographicComponent(superLexComp, map, _id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/lexicographicComponent: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("form")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "form",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Form deletion",
            notes = "This method deletes a form")
    public Response form(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                //        log(Level.INFO, "get lexicon entries types");
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isForm(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }

                return Response.ok(lexiconManager.deleteForm(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException | ServiceException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException ex) {
            log(Level.ERROR, "lexicon/delete/form: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("lexicalSense")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalSense",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical sense deletion",
            notes = "This method deletes a sense")
    public Response lexicalSense(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexicalSense(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                if (utilityManager.hasSubLexicographicComponent(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Deletion forbidden. Seleted sense has sub-senses.").build();
                }
                String lexicographicComponent = utilityManager.getLexicographicComponentBySense(_id);
                if (!lexicographicComponent.isEmpty()) {
                    String superLexComp = utilityManager.getSuperLexicographicComponent(lexicographicComponent);
                    TreeMap<Integer, String> map = utilityManager.getLexicographicComponetsPositions(lexicographicComponent);
                    lexiconManager.deleteLexicographicComponent(superLexComp, map, lexicographicComponent);
                }
//                String lexicographicComponent = utilityManager.getLexicographicComponentBySense(_id);
//                if (!lexicographicComponent.isEmpty()) {
//                    if (utilityManager.hasSubLexicographicComponent(lexicographicComponent)) {
//                        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Deletion forbidden. Seleted sense has sub-senses. Remove them first").build();
//                    }
//                    String superLexComp = utilityManager.getSuperLexicographicComponent(lexicographicComponent);
//                    TreeMap<Integer, String> map = utilityManager.getLexicographicComponetsPositions(lexicographicComponent);
//                    lexiconManager.deleteLexicographicComponent(superLexComp, map, lexicographicComponent);
//                }
                return Response.ok(lexiconManager.deleteLexicalSense(_id))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/lexicalSense: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("etymologicalLink")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "etymologicalLink",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Etymological link deletion",
            notes = "This method deletes an etymological Link")
    public Response etymologicalLink(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isEtymologicalLink(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                return Response.ok(lexiconManager.deleteEtymologicalLink(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/etymologicalLink: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("etymology")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "etymology",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Etymology deletion",
            notes = "This method deletes an etymology")
    public Response etymology(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isEtymology(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                }
                TupleQueryResult etymologicalLinks = lexiconDataManager.getEtymologicalLinks(_id);
                List<EtymologicalLink> etyLinks = etymologicalLinkHelper.newDataList(etymologicalLinks);
                for (EtymologicalLink el : etyLinks) {
                    lexiconManager.deleteEtymologicalLink(el.getEtymologicalLink());
                }

                return Response.ok(lexiconManager.deleteEtymology(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/etymology: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("bibliography")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "bibliography",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Bibliography deletion",
            notes = "This method deletes a bibliography")
    public Response bibliography(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "bibliography ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                return Response.ok(bibliographyManager.deleteBibliography(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/bibliography: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("relation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "relation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Relation deletion",
            notes = "This method deletes a relation")
    public Response relation(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "lexical entity ID",
                    required = true)
            @QueryParam("id") String id, RelationDeleter rd) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {

                return Response.ok(lexiconManager.deleteRelation(_id, rd))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/relation: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("component")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "component",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Multiword component deletion",
            notes = "This method deletes a component of a multiword")
    public Response component(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "component ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isComponent(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist or it is not a Component").build();
                }

                return Response.ok(lexiconManager.deleteComponent(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/deletion/component: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("collocation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "collocation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Collocation deletion",
            notes = "This method deletes a collocation")
    public Response collocation(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "collocation ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isCollocation(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist or it is not a collocation").build();
                }
                return Response.ok(lexiconManager.deleteCollocation(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/deletion/collocation: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("corpusFrequency")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "corpusFrequency",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Corpus Frequency deletion",
            notes = "This method deletes a corpus frequency")
    public Response corpusFrequency(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "Corpus Frequency ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isCorpusFrequency(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist or it is not a corpus frequency").build();
                }
                return Response.ok(lexiconManager.deleteCorpusFrequency(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/deletion/corpusFrequency: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("formRestriction")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "formRestriction",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Form Restriction deletion",
            notes = "This method deletes a form restriction")
    public Response formRestriction(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "formRestriction ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isFormRestriction(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist or it is not a form restriction").build();
                }
                return Response.ok(lexiconManager.deleteFormRestriction(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/deletion/formRestriction: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("lexicoSemanticRelation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicoSemanticRelation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexico semantic relation deletion",
            notes = "This method deletes a lexico semntic relation according to the vartrans module")
    public Response lexicoSemanticRelation(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "lexico semantic relation ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexicoSemanticRelation(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist or it is not a lexico semantic relation").build();
                }
                return Response.ok(lexiconManager.deleteLexicoSemanticRelation(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/lexicoSemanticRelation: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("translationSet")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "translationSet",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Translation set deletion",
            notes = "This method deletes a translation set according to the vartrans module")
    public Response translationSet(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "translation set ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isTranslationSet(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist or it is not a translation set").build();
                }
                return Response.ok(lexiconManager.deleteTranslationSet(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/translationSet: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("lexicalConcept")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "lexicalConcept",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical Concept deletion",
            notes = "This method deletes a lexical concept")
    public Response lexicalConcept(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "lexical concept ID",
                    required = true)
            @QueryParam("id") String id,
            @ApiParam(
                    name = "recursive",
                    value = "boolean",
                    required = true)
            @QueryParam("recursive") boolean recursive) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isLexicalConcept(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist or it is not a Lexical Concept").build();
                }
                return Response.ok(skosManager.deleteLexicalConcept(_id, recursive))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/lexicalConcept: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("conceptSet")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "conceptSet",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Concept Set deletion",
            notes = "This method deletes a concept set")
    public Response conceptSet(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "concept set ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                if (!utilityManager.isConceptSet(_id)) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist or it is not a Concept Set").build();
                }

                return Response.ok(skosManager.deleteConceptSet(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/conceptSet: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("image")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "image",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Image deletion",
            notes = "This method deletes an image associated to a lexical entity")
    public Response image(
            @HeaderParam("Authorization") String key,
            @ApiParam(
                    name = "id",
                    value = "image ID",
                    required = true)
            @QueryParam("id") String id) {
        try {
            checkKey(key);
            String _id = URLDecoder.decode(id, StandardCharsets.UTF_8.name());
            try {
                String imgUrl = null;
                UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                imgUrl = utilityManager.getImageUrl(_id);
                if (imgUrl == null) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " has no image url").build();
                }
                // TODO:
                // rimuovi file url dal server
                return Response.ok(lexiconManager.deleteImage(_id))
                        .type(MediaType.TEXT_PLAIN).header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/delete/image: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }
}
