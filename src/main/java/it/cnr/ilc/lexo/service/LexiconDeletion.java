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
@Path("lexicon/delete")
@Api("Lexicon deletion")
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
                            .type(MediaType.TEXT_PLAIN)
                            .header("Access-Control-Allow-Headers", "content-type")
                            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                            .build();
                } catch (ManagerException ex) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
                } catch (AuthorizationException | ServiceException ex) {
                   log(Level.ERROR, "lexicon/creation/language: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.ERROR, "lexicon/creation/lexicalEntry: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException ex) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } catch (UnsupportedEncodingException | ServiceException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException ex) {
            log(Level.ERROR, "lexicon/creation/form: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
                    //        log(Level.INFO, "get lexicon entries types");
                    UtilityManager utilityManager = ManagerFactory.getManager(UtilityManager.class);
                    if (!utilityManager.isLexicalSense(_id)) {
                        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("IRI " + _id + " does not exist").build();
                    }

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
            log(Level.ERROR, "lexicon/creation/lexicalSense: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.ERROR, "lexicon/creation/etymologicalLink: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.ERROR, "lexicon/creation/etymology: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.ERROR, "lexicon/creation/bibliography: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("relation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.GET,
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
            log(Level.ERROR, "lexicon/creation/relation: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.ERROR, "lexicon/creation/component: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
                            .type(MediaType.TEXT_PLAIN)
                            .header("Access-Control-Allow-Headers", "content-type")
                            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                            .build();
                } catch (ManagerException ex) {
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
                }
            } catch (UnsupportedEncodingException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            } 
            catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "lexicon/creation/lexicalConcept: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.ERROR, "lexicon/creation/conceptSet: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
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
            log(Level.ERROR, "lexicon/creation/image: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }
}
