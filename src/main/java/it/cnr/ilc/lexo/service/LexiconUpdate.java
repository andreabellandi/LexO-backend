/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.cnr.ilc.lexo.manager.LexiconUpdateManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryUpdater;
import it.cnr.ilc.lexo.service.data.lexicon.input.LinguisticRelationUpdater;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("lexicon/update")
@Api("Lexicon update")
public class LexiconUpdate extends Service {

    private final LexiconUpdateManager lexiconManager = ManagerFactory.getManager(LexiconUpdateManager.class);

//    @GET
//    @Path("{id}/lexicalEntryLabel")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "/{id}/lexicalEntryLabel",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Lexical entry label update",
//            notes = "This method updates the label of a lexical entry")
//    public Response lexicalEntryLabel(
//            @ApiParam(
//                    name = "key",
//                    value = "authentication token",
//                    example = "lexodemo",
//                    required = true)
//            @QueryParam("key") String key,
//            @ApiParam(
//                    name = "label",
//                    value = "the new label of the lexical entry",
//                    example = "label",
//                    required = true)
//            @QueryParam("label") String label,
//            @ApiParam(
//                    name = "id",
//                    value = "lexical entry ID",
//                    example = "MUSaccedereVERB",
//                    required = true)
//            @PathParam("id") String id) {
//        if (key.equals("PRINitant19")) {
//            try {
//                //        log(Level.INFO, "get lexicon entries types");
//                return Response.ok(lexiconManager.updateLexicalEntry(id, "rdfs:label", label, true))
//                        .type(MediaType.TEXT_PLAIN)
//                        .header("Access-Control-Allow-Headers", "content-type")
//                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
//                        .build();
//            } catch (ManagerException  | UpdateExecutionException ex) {
//                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
//                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
//            }
//        } else {
//            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
//        }
//    }
//    
//    @GET
//    @Path("{id}/lexicalEntryType")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "/{id}/lexicalEntryType",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Lexical entry type update",
//            notes = "This method updates the type of a lexical entry")
//    public Response lexicalEntryType(
//            @ApiParam(
//                    name = "key",
//                    value = "authentication token",
//                    example = "lexodemo",
//                    required = true)
//            @QueryParam("key") String key,
//            @ApiParam(
//                    name = "type",
//                    allowableValues = "Word, MultiwordExpression, Affix",
//                    value = "the new type of the lexical entry",
//                    example = "type",
//                    required = true)
//            @QueryParam("type") String type,
//            @ApiParam(
//                    name = "id",
//                    value = "lexical entry ID",
//                    example = "MUSaccedereVERB",
//                    required = true)
//            @PathParam("id") String id) {
//        if (key.equals("PRINitant19")) {
//            try {
//                //        log(Level.INFO, "get lexicon entries types");
//                lexiconManager.validateLexicalEntryType(type);
//                return Response.ok(lexiconManager.updateLexicalEntry(id, "rdf:type", "ontolex:" + type, false))
//                        .type(MediaType.TEXT_PLAIN)
//                        .header("Access-Control-Allow-Headers", "content-type")
//                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
//                        .build();
//            } catch (ManagerException  | UpdateExecutionException ex) {
//                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
//                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
//            }
//        } else {
//            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
//        }
//    }
//    
//    @GET
//    @Path("{id}/lexicalEntryStatus")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "/{id}/lexicalEntryStatus",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Lexical entry status update",
//            notes = "This method updates the status of a lexical entry")
//    public Response lexicalEntryStatus(
//            @ApiParam(
//                    name = "key",
//                    value = "authentication token",
//                    example = "lexodemo",
//                    required = true)
//            @QueryParam("key") String key,
//            @ApiParam(
//                    name = "status",
//                    allowableValues = "working, completed, reviewed",
//                    value = "the new status of the lexical entry",
//                    example = "status",
//                    required = true)
//            @QueryParam("status") String status,
//            @ApiParam(
//                    name = "id",
//                    value = "lexical entry ID",
//                    example = "MUSaccedereVERB",
//                    required = true)
//            @PathParam("id") String id) {
//        if (key.equals("PRINitant19")) {
//            try {
//                //        log(Level.INFO, "get lexicon entries types");
//                lexiconManager.validateLexicalEntryStatus(status);
//                return Response.ok(lexiconManager.updateLexicalEntry(id, "dct:valid", status, true))
//                        .type(MediaType.TEXT_PLAIN)
//                        .header("Access-Control-Allow-Headers", "content-type")
//                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
//                        .build();
//            } catch (ManagerException | UpdateExecutionException ex) {
//                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
//                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
//            }
//        } else {
//            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
//        }
//    }
//    
//    @GET
//    @Path("{id}/lexicalEntryPoS")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "/{id}/lexicalEntryPoS",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Lexical entry part of speech update",
//            notes = "This method updates the part of speech of a lexical entry")
//    public Response lexicalEntryPoS(
//            @ApiParam(
//                    name = "key",
//                    value = "authentication token",
//                    example = "lexodemo",
//                    required = true)
//            @QueryParam("key") String key,
//            @ApiParam(
//                    name = "pos",
//                    allowableValues = "adjective, adjective-i, adjective-na, adposition, adverb, adverbialPronoun, "
//                            + "affirmativeParticle, affixedPersonalPronoun, allusivePronoun, article, auxiliary, bullet, "
//                            + "cardinalNumeral, circumposition, closeParenthesis, collectivePronoun, colon, comma, "
//                            + "commonNoun, comparativeParticle, compoundPreposition, conditionalParticle, "
//                            + "conditionalPronoun, conjuction, conjunction, coordinatingConjunction, "
//                            + "coordinationParticle, copula, deficientVerb, definiteArticle, demonstrativeDeterminer, "
//                            + "demonstrativePronoun, determiner, diminutiveNoun, distinctiveParticle, emphaticPronoun, "
//                            + "exclamativeDeterminer, exclamativePoint, exclamativePronoun, existentialPronoun, fusedPreposition, "
//                            + "fusedPrepositionDeterminer, fusedPrepositionPronoun, fusedPronounAuxiliary, futureParticle, "
//                            + "generalAdverb, generalizationWord, genericNumeral, impersonalPronoun, indefiniteArticle, "
//                            + "indefiniteCardinalNumeral, indefiniteDeterminer, indefiniteMultiplicativeNumeral, "
//                            + "indefiniteOrdinalNumeral, indefinitePronoun, infinitiveParticle, interjection, "
//                            + "interrogativeCardinalNumeral, interrogativeDeterminer, interrogativeMultiplicativeNumeral, "
//                            + "interrogativeOrdinalNumeral, interrogativeParticle, interrogativePronoun, interrogativeRelativePronoun, "
//                            + "invertedComma, irreflexivePersonalPronoun, letter, lightVerb, mainVerb, modal, multiplicativeNumeral, "
//                            + "negativeParticle, negativePronoun, noun, numeral, numeralDeterminer, numeralFraction, numeralPronoun, "
//                            + "openParenthesis, ordinalAdjective, participleAdjective, particle, partitiveArticle, pastParticipleAdjective, "
//                            + "personalPronoun, plainVerb, point, possessiveAdjective, possessiveDeterminer, possessiveParticle, "
//                            + "possessivePronoun, possessiveRelativePronoun, postposition, preposition, prepositionalAdverb, "
//                            + "presentParticipleAdjective, presentativePronoun, pronominalAdverb, pronoun, properNoun, punctuation, "
//                            + "qualifierAdjective, questionMark, reciprocalPronoun, reflexiveDeterminer, reflexivePersonalPronoun, "
//                            + "reflexivePossessivePronoun, relationNoun, relativeDeterminer, relativeParticle, relativePronoun, "
//                            + "semiColon, slash, strongPersonalPronoun, subordinatingConjunction, superlativeParticle, suspensionPoints, "
//                            + "symbol, unclassifiedParticle, verb, weakPersonalPronoun",
//                    value = "the new part of speech of the lexical entry",
//                    example = "pos",
//                    required = true)
//            @QueryParam("pos") String pos,
//            @ApiParam(
//                    name = "id",
//                    value = "lexical entry ID",
//                    example = "MUSaccedereVERB",
//                    required = true)
//            @PathParam("id") String id) {
//        if (key.equals("PRINitant19")) {
//            try {
//                //        log(Level.INFO, "get lexicon entries types");
////                lexiconManager.validateLexicalEntryPoS(pos);
//                return Response.ok(lexiconManager.updateLexicalEntry(id, "lexinfo:partOfSpeech", "lexinfo:" + pos, false))
//                        .type(MediaType.TEXT_PLAIN)
//                        .header("Access-Control-Allow-Headers", "content-type")
//                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
//                        .build();
//            } catch (ManagerException | UpdateExecutionException ex) {
//                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
//                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
//            }
//        } else {
//            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
//        }
//    }
//    
//    @GET
//    @Path("{id}/lexicalEntryNote")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "/{id}/lexicalEntryNote",
//            produces = "application/json; charset=UTF-8")
//    @ApiOperation(value = "Lexical entry note update",
//            notes = "This method updates the note of a lexical entry")
//    public Response lexicalEntryNote(
//            @ApiParam(
//                    name = "key",
//                    value = "authentication token",
//                    example = "lexodemo",
//                    required = true)
//            @QueryParam("key") String key,
//            @ApiParam(
//                    name = "note",
//                    value = "the new note of the lexical entry",
//                    example = "note",
//                    required = true)
//            @QueryParam("note") String note,
//            @ApiParam(
//                    name = "id",
//                    value = "lexical entry ID",
//                    example = "MUSaccedereVERB",
//                    required = true)
//            @PathParam("id") String id) {
//        if (key.equals("PRINitant19")) {
//            try {
//                //        log(Level.INFO, "get lexicon entries types");
//                return Response.ok(lexiconManager.updateLexicalEntry(id, "rdfs:comment", note, true))
//                        .type(MediaType.TEXT_PLAIN)
//                        .header("Access-Control-Allow-Headers", "content-type")
//                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
//                        .build();
//            } catch (ManagerException | UpdateExecutionException ex) {
//                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
//                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
//            }
//        } else {
//            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
//        }
//    }
//    
    @POST
    @Path("{id}/lexicalEntry")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}/lexicalEntry",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Lexical entry update",
            notes = "This method updates the lexical entry according to the input updater")
    public Response lexicalEntry(@QueryParam("key") String key, @QueryParam("user") String user, @PathParam("id") String id, LexicalEntryUpdater leu) {
        if (key.equals("PRINitant19")) {
            try {
                //        log(Level.INFO, "get lexicon entries types");
                return Response.ok(lexiconManager.updateLexicalEntry(id, leu, user))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }
    
    @POST
    @Path("{id}/linguisticRelation")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{id}/linguisticRelation",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "Linguistic relation update",
            notes = "This method updates a linguistic relation according to the input updater")
    public Response linguisticRelation(@QueryParam("key") String key, @PathParam("id") String id, LinguisticRelationUpdater lru) {
        if (key.equals("PRINitant19")) {
            try {
                //        log(Level.INFO, "get lexicon entries types");
                return Response.ok(lexiconManager.updateLexicalEntry(id, lru))
                        .type(MediaType.TEXT_PLAIN)
                        .header("Access-Control-Allow-Headers", "content-type")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
                        .build();
            } catch (ManagerException | UpdateExecutionException ex) {
                Logger.getLogger(LexiconUpdate.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Insertion denied, wrong key").build();
        }
    }

}
