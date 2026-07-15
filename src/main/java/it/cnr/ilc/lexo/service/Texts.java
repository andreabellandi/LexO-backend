package it.cnr.ilc.lexo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import it.cnr.ilc.lexo.manager.text.TextJobManager;
import it.cnr.ilc.lexo.manager.text.TextJobManager.TextJobInfo;
import it.cnr.ilc.lexo.manager.text.TextJobManager.UploadKind;
import it.cnr.ilc.lexo.service.data.lexicon.input.converter.CancelRequest;
import it.cnr.ilc.lexo.service.data.text.output.TextRecord;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.log4j.Level;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

/** REST endpoints for persistent text corpora represented in RDF/NIF. */
@javax.ws.rs.Path("texts")
@Api("Text Corpus NIF")
public class Texts extends Service {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final long MAX_TEXT_BYTES = longProperty(
            "lexo.text.maxTextBytes", TextJobManager.DEFAULT_MAX_TEXT_BYTES);
    private static final long MAX_CONLLU_BYTES = longProperty(
            "lexo.text.maxConlluBytes", TextJobManager.DEFAULT_MAX_CONLLU_BYTES);

    @POST
    @javax.ws.rs.Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@HeaderParam("Authorization") String key, FormDataMultiPart multiPart) {
        String fileId = UUID.randomUUID().toString();
        try {
            checkKey(key);
            if (multiPart == null) {
                return plain(Response.Status.BAD_REQUEST, "Missing multipart request");
            }

            List<FormDataBodyPart> parts = new ArrayList<FormDataBodyPart>();
            addAll(parts, multiPart.getFields("file"));
            addAll(parts, multiPart.getFields("conllu"));
            if (parts.isEmpty()) {
                return plain(Response.Status.BAD_REQUEST, "Missing file");
            }

            String textFileName = null;
            String conlluFileName = null;
            for (FormDataBodyPart part : parts) {
                if (part == null) {
                    continue;
                }
                FormDataContentDisposition metadata = part.getFormDataContentDisposition();
                String name = metadata == null ? null : metadata.getFileName();
                if (name == null || name.trim().isEmpty()) {
                    TextJobManager.get().cleanupUpload(fileId);
                    return plain(Response.Status.BAD_REQUEST, "Missing original filename");
                }
                String lower = name.toLowerCase(Locale.ROOT);
                UploadKind kind;
                long maxBytes;
                if (TextJobManager.isTextExtension(lower)) {
                    if (textFileName != null) {
                        TextJobManager.get().cleanupUpload(fileId);
                        return plain(Response.Status.BAD_REQUEST, "Only one TXT/Markdown file is allowed");
                    }
                    kind = UploadKind.TEXT;
                    maxBytes = MAX_TEXT_BYTES;
                    textFileName = name;
                } else if (TextJobManager.isConlluExtension(lower)) {
                    if (conlluFileName != null) {
                        TextJobManager.get().cleanupUpload(fileId);
                        return plain(Response.Status.BAD_REQUEST, "Only one CoNLL-U file is allowed");
                    }
                    kind = UploadKind.CONLLU;
                    maxBytes = MAX_CONLLU_BYTES;
                    conlluFileName = name;
                } else {
                    TextJobManager.get().cleanupUpload(fileId);
                    return plain(Response.Status.UNSUPPORTED_MEDIA_TYPE,
                            "Allowed extensions: .txt, .md, .markdown, .conllu, .conll-u, .conll");
                }

                try (InputStream input = part.getEntityAs(InputStream.class)) {
                    TextJobManager.get().saveUpload(fileId, input, name, kind, maxBytes);
                }
            }

            if (!TextJobManager.get().hasTextUpload(fileId)) {
                TextJobManager.get().cleanupUpload(fileId);
                return plain(Response.Status.BAD_REQUEST,
                        "A TXT/Markdown file is required; CoNLL-U alone is not sufficient");
            }

            Map<String, Object> response = new LinkedHashMap<String, Object>();
            response.put("fileId", fileId);
            response.put("originalFileName", textFileName);
            if (conlluFileName != null) {
                response.put("conlluFileName", conlluFileName);
            }
            log(Level.INFO, "/texts/upload: uploaded fileId=" + fileId
                    + " text=" + textFileName + " conllu=" + conlluFileName);
            return json(response);
        } catch (IOException e) {
            TextJobManager.get().cleanupUpload(fileId);
            log(Level.ERROR, "/texts/upload: " + e.getMessage());
            Response.Status status = e.getMessage() != null && e.getMessage().contains("exceeds")
                    ? Response.Status.REQUEST_ENTITY_TOO_LARGE : Response.Status.BAD_REQUEST;
            return plain(status, e.getMessage());
        } catch (IllegalArgumentException e) {
            TextJobManager.get().cleanupUpload(fileId);
            log(Level.ERROR, "/texts/upload: " + e.getMessage());
            return plain(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (AuthorizationException | ServiceException e) {
            TextJobManager.get().cleanupUpload(fileId);
            return unauthorized("/texts/upload");
        } finally {
            if (multiPart != null) {
                try {
                    multiPart.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    @POST
    @javax.ws.rs.Path("/{fileId}/convert")
    @Produces(MediaType.APPLICATION_JSON)
    public Response convert(@HeaderParam("Authorization") String key,
                            @PathParam("fileId") String fileId) {
        try {
            checkKey(key);
            log(Level.INFO, "/texts/{fileId}/convert: required for id " + fileId);
            return json(TextJobManager.get().startConversion(fileId));
        } catch (IllegalStateException e) {
            return plain(Response.Status.CONFLICT, e.getMessage());
        } catch (IllegalArgumentException e) {
            return plain(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (AuthorizationException | ServiceException e) {
            return unauthorized("/texts/{fileId}/convert");
        }
    }

    @GET
    @javax.ws.rs.Path("/{fileId}/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response status(@HeaderParam("Authorization") String key,
                           @PathParam("fileId") String fileId) {
        try {
            checkKey(key);
            Collection<TextJobInfo> jobs = TextJobManager.get().getAllJobsFor(fileId);
            return json(jobs);
        } catch (AuthorizationException | ServiceException e) {
            return unauthorized("/texts/{fileId}/status");
        }
    }

    @POST
    @javax.ws.rs.Path("/{fileId}/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancel(@HeaderParam("Authorization") String key,
                           @PathParam("fileId") String fileId,
                           CancelRequest request) {
        try {
            checkKey(key);
            if (request != null && request.type != null
                    && !"CONVERT".equalsIgnoreCase(request.type.trim())) {
                return plain(Response.Status.BAD_REQUEST,
                        "Unknown type: " + request.type + ". The text service supports CONVERT");
            }
            Map<String, Object> response = new LinkedHashMap<String, Object>();
            response.put("cancelled", Boolean.valueOf(TextJobManager.get().cancel(fileId)));
            return json(response);
        } catch (AuthorizationException | ServiceException e) {
            return unauthorized("/texts/{fileId}/cancel");
        }
    }

    @GET
    @javax.ws.rs.Path("/{fileId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response record(@HeaderParam("Authorization") String key,
                           @PathParam("fileId") String fileId) {
        try {
            checkKey(key);
            TextRecord record = TextJobManager.get().getRecord(fileId);
            return record == null ? plain(Response.Status.NOT_FOUND, "Text record not found") : json(record);
        } catch (IllegalStateException e) {
            return plain(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (AuthorizationException | ServiceException e) {
            return unauthorized("/texts/{fileId}");
        }
    }

    @GET
    @javax.ws.rs.Path("/{fileId}/nif")
    @Produces("text/turtle")
    public Response nif(@HeaderParam("Authorization") String key,
                        @PathParam("fileId") String fileId) {
        return artifact(key, fileId, Artifact.NIF);
    }

    @GET
    @javax.ws.rs.Path("/{fileId}/original")
    public Response original(@HeaderParam("Authorization") String key,
                             @PathParam("fileId") String fileId) {
        return artifact(key, fileId, Artifact.ORIGINAL);
    }

    @GET
    @javax.ws.rs.Path("/{fileId}/canonical")
    @Produces(MediaType.TEXT_PLAIN)
    public Response canonical(@HeaderParam("Authorization") String key,
                              @PathParam("fileId") String fileId) {
        return artifact(key, fileId, Artifact.CANONICAL);
    }

    @GET
    @javax.ws.rs.Path("/{fileId}/conllu")
    @Produces("text/x-conllu")
    public Response conllu(@HeaderParam("Authorization") String key,
                           @PathParam("fileId") String fileId) {
        return artifact(key, fileId, Artifact.CONLLU);
    }

    @DELETE
    @javax.ws.rs.Path("/{fileId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@HeaderParam("Authorization") String key,
                           @PathParam("fileId") String fileId) {
        try {
            checkKey(key);
            Map<String, Object> response = new LinkedHashMap<String, Object>();
            response.put("deleted", Boolean.valueOf(TextJobManager.get().delete(fileId)));
            return json(response);
        } catch (IOException e) {
            return plain(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (IllegalArgumentException e) {
            return plain(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (AuthorizationException | ServiceException e) {
            return unauthorized("DELETE /texts/{fileId}");
        }
    }

    private Response artifact(String key, String fileId, Artifact artifact) {
        try {
            checkKey(key);
            TextJobManager manager = TextJobManager.get();
            TextRecord record = manager.getRecord(fileId);
            if (record == null) {
                return plain(Response.Status.NOT_FOUND, "Text record not found");
            }
            Path path;
            String mediaType;
            String downloadName;
            switch (artifact) {
                case NIF:
                    path = manager.getNif(fileId);
                    mediaType = "text/turtle; charset=UTF-8";
                    downloadName = fileId + ".ttl";
                    break;
                case ORIGINAL:
                    path = manager.getOriginal(fileId);
                    mediaType = originalMediaType(record.originalFileName);
                    downloadName = record.originalFileName;
                    break;
                case CANONICAL:
                    path = manager.getCanonical(fileId);
                    mediaType = "text/plain; charset=UTF-8";
                    downloadName = fileId + "-canonical.txt";
                    break;
                case CONLLU:
                    path = manager.getConllu(fileId);
                    mediaType = "text/x-conllu; charset=UTF-8";
                    downloadName = record.conlluFileName;
                    break;
                default:
                    throw new IllegalStateException("Unsupported artifact");
            }
            if (path == null || !Files.exists(path)) {
                return plain(Response.Status.NOT_FOUND,
                        artifact == Artifact.CONLLU ? "No CoNLL-U file for this text" : "Artifact not found");
            }
            final Path served = path;
            StreamingOutput stream = output -> {
                try (InputStream input = Files.newInputStream(served)) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = input.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                }
            };
            return Response.ok(stream)
                    .type(mediaType)
                    .header("Content-Disposition", "attachment; filename=\"" + safeHeaderFileName(downloadName) + "\"")
                    .build();
        } catch (IllegalArgumentException e) {
            return plain(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (IllegalStateException e) {
            return plain(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (AuthorizationException | ServiceException e) {
            return unauthorized("/texts/{fileId}/" + artifact.name().toLowerCase(Locale.ROOT));
        }
    }

    private Response unauthorized(String endpoint) {
        String username = authenticationData.getUsername() == null ? "" : authenticationData.getUsername();
        log(Level.ERROR, endpoint + ": " + username + " not authorized");
        return plain(Response.Status.BAD_REQUEST, username + " not authorized");
    }

    private static Response json(Object body) {
        try {
            return Response.ok(MAPPER.writeValueAsString(body), MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException e) {
            return plain(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private static Response plain(Response.Status status, String message) {
        return Response.status(status).type(MediaType.TEXT_PLAIN)
                .entity(message == null ? "" : message).build();
    }

    private static void addAll(List<FormDataBodyPart> target, List<FormDataBodyPart> source) {
        if (source != null) {
            target.addAll(source);
        }
    }

    private static String originalMediaType(String fileName) {
        String lower = fileName == null ? "" : fileName.toLowerCase(Locale.ROOT);
        return lower.endsWith(".md") || lower.endsWith(".markdown")
                ? "text/markdown; charset=UTF-8" : "text/plain; charset=UTF-8";
    }

    private static String safeHeaderFileName(String fileName) {
        if (fileName == null) {
            return "download";
        }
        return fileName.replace("\\", "_").replace("\"", "_")
                .replace("\r", "_").replace("\n", "_");
    }

    private static long longProperty(String name, long fallback) {
        String value = System.getProperty(name);
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        try {
            long parsed = Long.parseLong(value.trim());
            return parsed > 0 ? parsed : fallback;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private enum Artifact {
        NIF, ORIGINAL, CANONICAL, CONLLU
    }
}
