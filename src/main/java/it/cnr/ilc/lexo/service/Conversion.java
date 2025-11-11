/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import it.cnr.ilc.lexo.manager.JobManager;
import it.cnr.ilc.lexo.manager.JobManager.JobInfo;
import it.cnr.ilc.lexo.service.data.lexicon.input.conversion.CancelRequest;
import it.cnr.ilc.lexo.service.data.lexicon.input.conversion.QueryRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Level;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author andreabellandi
 */
@Path("conversion")
@Api("Linguistic Model Converter")
public class Conversion extends Service {

    private static final long MAX_BYTES = 50L * 1024 * 1024; // 50 MB

//    private Response json(Response.Status status, Map<String, ?> body) {
//        return Response.status(status).entity(body).build();
//    }
//    private Response ok(Map<String, ?> body) {
//        return json(Response.Status.OK, body);
//    }
    private Response ok(Object body) throws JsonProcessingException {
        return Response.ok(new ObjectMapper().writeValueAsString(body)).build();
    }

//    private Response error(Response.Status status, String message) {
//        Map<String, Object> m = new LinkedHashMap<>();
//        m.put("error", message);
//        return json(status, m);
//    }
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@HeaderParam("Authorization") String key, @FormDataParam("file") InputStream fileStream,
            @FormDataParam("file") FormDataContentDisposition meta) {
        try {
            checkKey(key);
            if (fileStream == null || meta == null) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Missing file").build();
            }
            final String name = meta.getFileName();
            if (name == null || !(name.endsWith(".ttl") || name.endsWith(".rdf"))) {
                return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).type(MediaType.TEXT_PLAIN).entity("Only .ttl or .rdf allowed").build();
            }
            final String fileId = java.util.UUID.randomUUID().toString();
            log(Level.INFO, "/upload: file " + name + " - " + meta.getSize() + " with id = " + fileId);
            JobManager.get().saveUploadEnforcingLimit(fileId, fileStream, name, MAX_BYTES);
            Map<String, String> resp = new LinkedHashMap<>();
            resp.put("fileId", fileId);
            return Response.ok().entity(new ObjectMapper().writeValueAsString(resp)).build();
        } catch (IOException e) {
            log(Level.ERROR, "/upload: " + e.getMessage());
            return Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (AuthorizationException | ServiceException e) {
            log(Level.ERROR, "/upload: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("/{fileId}/parse")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response parse(@HeaderParam("Authorization") String key, @PathParam("fileId") String fileId) {
        try {
            checkKey(key);
            log(Level.INFO, "/{fileId}/parse: required for id " + fileId);
            JobInfo ji = JobManager.get().startParse(fileId);
            return Response.ok(new ObjectMapper().writeValueAsString(ji)).build();
        } catch (IllegalStateException e) {
            log(Level.ERROR, "/{fileId}/parse: required for id " + fileId + ": " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (JsonProcessingException e) {
            log(Level.ERROR, "/{fileId}/parse: required for id " + fileId + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "/{fileId}/parse: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("/{fileId}/query")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response query(@HeaderParam("Authorization") String key, @PathParam("fileId") String fileId, QueryRequest req) {
        try {
            checkKey(key);
            if (req == null || req.query == null) {
                log(Level.ERROR, "/{fileId}/query: required for id " + fileId + ": Missing query");
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Missing query").build();
            }
            final String q = req.query.trim();

            boolean isSelect = java.util.regex.Pattern
                    .compile("(?is)^\\s*(?:prefix\\s+\\w*:\\s*<[^>]+>\\s*)*(?:base\\s+<[^>]+>\\s*)*select\\b")
                    .matcher(q).find();
            if (!isSelect) {
                log(Level.ERROR, "/{fileId}/query: required for id " + fileId + ": Only SELECT queries allowed");
                return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).type(MediaType.TEXT_PLAIN).entity("Only SELECT queries allowed").build();
            }
//            if (!q.toLowerCase().startsWith("select")) {
//                log(Level.ERROR, "/{fileId}/query: required for id " + fileId + ": Only SELECT queries allowed");
//                return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).type(MediaType.TEXT_PLAIN).entity("Only SELECT queries allowed").build();
//            }
            long timeout = (req.timeoutMs != null) ? req.timeoutMs : 60_000L;
            boolean includeInf = (req.includeInferred != null) ? req.includeInferred : false; // default: false
            JobManager.ResultFormat fmt;
            if (req.resultFormat == null) {
                fmt = JobManager.ResultFormat.JSON; // default JSON
            } else {
                String rf = req.resultFormat.trim().toUpperCase();
                if ("JSON".equals(rf)) {
                    fmt = JobManager.ResultFormat.JSON;
                } else if ("CSV".equals(rf)) {
                    fmt = JobManager.ResultFormat.CSV;
                } else {
                    log(Level.ERROR, "/{fileId}/query: required for id " + fileId + ": Unknown resultFormat: " + req.resultFormat);
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Unknown resultFormat: " + req.resultFormat).build();
                }
            }
            long maxBytes = (req.maxResultBytes != null && req.maxResultBytes > 0) ? req.maxResultBytes : JobManager.DEFAULT_MAX_RESULT_BYTES;
            int maxRows = (req.maxRows != null && req.maxRows > 0) ? req.maxRows : JobManager.DEFAULT_MAX_RESULT_ROWS;
            JobInfo ji = JobManager.get().startQuery(fileId, q, timeout, includeInf, fmt, maxBytes, maxRows);
            return Response.ok(new ObjectMapper().writeValueAsString(ji)).build();
        } catch (IllegalStateException ise) {
            log(Level.ERROR, "/{fileId}/query: required for id " + fileId + ": " + ise.getMessage());
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ise.getMessage()).build();
        } catch (JsonProcessingException e) {
            log(Level.ERROR, "/{fileId}/query: required for id " + fileId + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "/{fileId}/query: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

//    @POST
//    @Path("/{fileId}/convert")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response convert(@HeaderParam("Authorization") String key, @PathParam("fileId") String fileId) {
//        try {
//            checkKey(key);
//            log(Level.INFO, "/{fileId}/convert: required for id " + fileId);
//            JobInfo ji = JobManager.get().startConvert(fileId, null);
//            return Response.ok(new ObjectMapper().writeValueAsString(ji)).build();
//        } catch (IllegalStateException ise) {
//            log(Level.ERROR, "/{fileId}/convert: bad state for id " + fileId + ": " + ise.getMessage());
//            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ise.getMessage()).build();
//        } catch (JsonProcessingException e) {
//            log(Level.ERROR, "/{fileId}/convert: failed for id " + fileId + ": " + e.getMessage());
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
//        } catch (AuthorizationException | ServiceException ex) {
//            log(Level.ERROR, "/{fileId}/convert: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
//            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
//        }
//    }

    @GET
    @Path("/{fileId}/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response status(@HeaderParam("Authorization") String key, @PathParam("fileId") String fileId) {
        try {
            checkKey(key);
            log(Level.INFO, "/{fileId}/status: required for id " + fileId);
            Collection<JobInfo> jobs = JobManager.get().getAllJobsFor(fileId);
            return ok(jobs);
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "/{fileId}/status: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        } catch (JsonProcessingException ex) {
            log(Level.ERROR, "/{fileId}/status: required for id " + fileId + ": " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("/{fileId}/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancel(@HeaderParam("Authorization") String key, @PathParam("fileId") String fileId, CancelRequest req) {
        try {
            checkKey(key);
            Map<String, Object> out = new LinkedHashMap<>();
            if (req != null && req.type != null) {
                log(Level.INFO, "/{fileId}/cancel: required for id " + fileId + " with request type = " + req.getType());
                JobManager.JobType t;
                try {
                    t = JobManager.JobType.valueOf(req.type.toUpperCase());
                } catch (IllegalArgumentException iae) {
                    log(Level.ERROR, "/{fileId}/status: + " + req.getType() + " is unknown type: " + iae.getMessage());
                    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Unknown type: " + req.type).build();
                }
                boolean ok = JobManager.get().cancel(fileId, t);
                log(Level.INFO, "/{fileId}/cancel: CANCEL one type=" + t + " fileId=" + fileId + " -> " + ok);
                out.put("cancelled", ok);
            } else {
                boolean any = false;
                for (JobManager.JobType t : JobManager.JobType.values()) {
                    any |= JobManager.get().cancel(fileId, t);
                }
                log(Level.INFO, "/{fileId}/cancel: CANCEL all for fileId=" + fileId + " -> " + any);
                out.put("cancelledAny", any);
            }
            return ok(out);
        } catch (JsonProcessingException e) {
            log(Level.ERROR, "/{fileId}/cancel: failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "/{fileId}/cancel: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("/{fileId}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@HeaderParam("Authorization") String key, @PathParam("fileId") String fileId) {
        try {
            checkKey(key);
            final java.nio.file.Path toServe = JobManager.get().getConverted(fileId);
            if (toServe == null || !Files.exists(toServe)) {
                log(Level.ERROR, "/{fileId}/download: No converted file");
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("No converted file").build();
            }
            log(Level.INFO, "/{fileId}/download: DOWNLOAD start fileId=" + fileId + " path=" + toServe);
            StreamingOutput stream = os -> {
                try ( InputStream in = Files.newInputStream(toServe)) {
                    byte[] buf = new byte[8192];
                    int r;
                    long sent = 0;
                    while ((r = in.read(buf)) != -1) {
                        os.write(buf, 0, r);
                        sent += r;
                    }
                    os.flush();
                    log(Level.INFO, "/{fileId}/download: DOWNLOAD done fileId=" + fileId + " bytesSent=" + sent);
                } catch (Exception ex) {
                    log(Level.ERROR, "/{fileId}/download: downlaod stream error for fileID=" + fileId + " " + ex.getMessage());
                } finally {
                    log(Level.INFO, "/{fileId}/download: DOWNLOAD cleanup for=" + fileId);
                    try {
                        JobManager.get().cleanupAllFor(fileId);
                    } catch (Exception ex) {
                        log(Level.ERROR, "/{fileId}/download: cleanup for=" + fileId);
                    }
                }
            };
            String fname = toServe.getFileName().toString();
            return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + fname + "\"")
                    .build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "/{fileId}/cancel: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @GET
    @Path("/{fileId}/{queryId}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@HeaderParam("Authorization") String key, @PathParam("fileId") String fileId,
            @PathParam("queryId") String queryId,
            @QueryParam("format") String format) {
        try {
            checkKey(key);
            if (format == null || !("json".equalsIgnoreCase(format) || "csv".equalsIgnoreCase(format))) {
                log(Level.ERROR, "/{fileId}/{queryId}/download: format must be 'json' or 'csv'");
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("format must be 'json' or 'csv'").build();
            }
            final java.nio.file.Path toServe = JobManager.get().getQueryResult(fileId, queryId, format);
            if (toServe == null || !Files.exists(toServe)) {
                log(Level.ERROR, "/{fileId}/{queryId}/download: Result not found for given format");
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("Result not found for given format").build();
            }
            log(Level.INFO, "/{fileId}/{queryId}/download: start filedID=" + fileId + " queryId=" + queryId + " format=" + format + " path=" + toServe);
            StreamingOutput stream = os -> {
                try ( InputStream in = Files.newInputStream(toServe)) {
                    byte[] buf = new byte[8192];
                    int r;
                    long sent = 0;
                    while ((r = in.read(buf)) != -1) {
                        os.write(buf, 0, r);
                        sent += r;
                    }
                    os.flush();
                    log(Level.INFO, "/{fileId}/{queryId}/download: download filedID=" + fileId + " queryId=" + queryId + " bytesSent=" + sent);
                } catch (Exception ex) {
                    log(Level.ERROR, "/{fileId}/{queryId}/download: stream error filedID=" + fileId + " queryId=" + queryId + " error=" + ex.toString());
                } finally {
                    log(Level.INFO, "/{fileId}/{queryId}/download: cleanup filedID=" + fileId + " queryId=" + queryId);
                    JobManager.get().cleanupQueryResult(fileId, queryId); // remove both formats & job refs
                }
            };
            String fname = toServe.getFileName().toString();
            String media = "json".equalsIgnoreCase(format) ? MediaType.APPLICATION_JSON : "text/csv";
            return Response.ok(stream, media)
                    .header("Content-Disposition", "attachment; filename=\"" + fname + "\"")
                    .build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "/{fileId}/{queryId}/download: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("/{fileId}/convert")
    @Produces(MediaType.APPLICATION_JSON)
    public Response convert(@HeaderParam("Authorization") String key,
            @PathParam("fileId") String fileId,
            @QueryParam("from") String from,
            @QueryParam("to") String to,
            @Context UriInfo uriInfo) {
        try {
            checkKey(key);
            if (from == null || to == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.TEXT_PLAIN)
                        .entity("'from' and 'to' query params are required").build();
            }
            // Raccogli tutte le query param come opzioni libere (format, delimiter, rdfFormat, ecc.)
            MultivaluedMap<String, String> qp = uriInfo.getQueryParameters();
            Map<String, String> options = new HashMap<>();
            for (Map.Entry<String, List<String>> e : qp.entrySet()) {
                String k = e.getKey();
                if (!"from".equalsIgnoreCase(k) && !"to".equalsIgnoreCase(k)) {
                    options.put(k, e.getValue().get(0));
                }
            }
            log(Level.INFO, String.format("/%s/convert?from=%s&to=%s options=%s", fileId, from, to, options));
            JobInfo ji = JobManager.get().startGenericConvert(fileId, from, to, options);
            return Response.ok(new ObjectMapper().writeValueAsString(ji)).build();
        } catch (IllegalStateException ise) {
            log(Level.ERROR, "/{fileId}/convert: bad state for id " + fileId + ": " + ise.getMessage());
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ise.getMessage()).build();
        } catch (JsonProcessingException e) {
            log(Level.ERROR, "/{fileId}/convert: failed for id " + fileId + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "/{fileId}/convert: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

}
