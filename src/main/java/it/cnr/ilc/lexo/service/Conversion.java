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
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.manager.JobManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.manager.converter.InMemoryRepositoryManager;
import it.cnr.ilc.lexo.service.helper.ECDComponentHelper;
import it.cnr.ilc.lexo.util.FileStore;
import it.cnr.ilc.lexo.util.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Level;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author andreabellandi
 */
@Path("conversion")
@Api("Linguistic Model Converter")
public class Conversion extends Service {

    private final JobManager jobManager = ManagerFactory.getManager(JobManager.class);
    private final InMemoryRepositoryManager parser = ManagerFactory.getManager(InMemoryRepositoryManager.class);
    private static final long MAX_UPLOAD = 20L * 1024 * 1024; // 20MB
    private final ECDComponentHelper helper = new ECDComponentHelper();

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@HeaderParam("Authorization") String key,
            @FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition info, @Context UriInfo uri) {
        try {
            checkKey(key);
            log(Level.INFO, "conversion/upload: file " + info.getFileName());
            String id = jobManager.newId();
            String safe = StringUtil.sanitize(info.getFileName());
            java.nio.file.Path dest = FileStore.sourcePath(id, safe);
            OutputStream os = Files.newOutputStream(dest, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            jobManager.copyWithLimit(uploadedInputStream, os, MAX_UPLOAD);
            jobManager.registerUpload(id);
            return Response.created(uri.getAbsolutePathBuilder().path(id).build())
                    .entity(new ObjectMapper().writeValueAsString(Map.of("id", id, "status", JobManager.Status.UPLOADED.name())))
                    .build();
        } catch (IOException ex) {
            log(Level.ERROR, "conversion/upload: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "conversion/upload: " + ex.getMessage());
            return Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "conversion/upload: " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }
    }

    @POST
    @Path("/{id}/convert")
    public Response convert(@HeaderParam("Authorization") String key, @PathParam("id") String id, @Context UriInfo uri) {
        try {
            checkKey(key);
            log(Level.INFO, "/{id}/convert: required for id " + id);
            JobManager.JobInfo job = jobManager.get(id);
            java.nio.file.Path src = FileStore.findSource(id);
            java.nio.file.Path out = FileStore.outPath(id);
            job = jobManager.startAsync(id, src, out);
            return Response.status(Status.ACCEPTED)
                    .location(uri.getBaseUriBuilder().path("files").path(id).path("status").build())
                    .entity(new ObjectMapper().writeValueAsString(Map.of("status", job.status.name())))
                    .header("Retry-After", "2")
                    .build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "/{id}/convert: required for id " + id + ": " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        } catch (IOException ex) {
            log(Level.ERROR, "/{id}/convert: required for id " + id + ": " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}/info")
    public Response info(@HeaderParam("Authorization") String key, @PathParam("id") String id) {
        try {
            checkKey(key);
            log(Level.INFO, "/{id}/info: required for id " + id);
            JobManager.JobInfo job = jobManager.ensureExists(id);
            java.nio.file.Path src = FileStore.findSource(id);
            Map<String, Object> info = parser.getInfo(src);
            return Response.ok(new ObjectMapper().writeValueAsString(info)).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "/{id}/info: required for id " + id + ": " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        } catch (Exception ex) {
            log(Level.ERROR, "/{id}/info: required for id " + id + ": " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } 
    }
    
    @GET
    @Path("/{id}/status")
    public Response status(@HeaderParam("Authorization") String key, @PathParam("id") String id) {
        try {
            checkKey(key);
            log(Level.INFO, "/{id}/status: required for id " + id);
            JobManager.JobInfo job = jobManager.ensureExists(id);
            Map<String, Object> status = new LinkedHashMap();
            status.put("id", job.id);
            status.put("status", job.status.name());
            status.put("progress", job.progress);
            status.put("message", job.message);
            return Response.ok(new ObjectMapper().writeValueAsString(status)).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "/{id}/status: required for id " + id + ": " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        } catch (IOException ex) {
            log(Level.ERROR, "/{id}/status: required for id " + id + ": " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }

    }

    @GET
    @Path("/{id}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@HeaderParam("Authorization") String key, @PathParam("id") String id) {
        try {
            checkKey(key);
            log(Level.INFO, "/{id}/download: required for id " + id);
            JobManager.JobInfo job = jobManager.get(id);
            if (job.status != JobManager.Status.READY) {
                throw new ManagerException("Conversion not ready");
            }
            java.nio.file.Path out = FileStore.outPath(id);
            if (!Files.exists(out)) {
                throw new ManagerException("Converted file not found");
            }
            StreamingOutput stream = os -> Files.copy(out, os);
            String filename = out.getFileName().toString();
            return Response.ok(stream)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .build();
        } catch (ManagerException ex) {
            log(Level.ERROR, "/{id}/download: required for id " + id + ": " + ex.getMessage());
            return Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        } catch (AuthorizationException | ServiceException ex) {
            log(Level.ERROR, "/{id}/download: required for id " + id + ": " + (authenticationData.getUsername() != null ? authenticationData.getUsername() : "") + " not authorized");
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(authenticationData.getUsername() + " not authorized").build();
        }

    }

}
