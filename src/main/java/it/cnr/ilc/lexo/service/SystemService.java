package it.cnr.ilc.lexo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cnr.ilc.lexo.HibernateUtil;
import it.cnr.ilc.lexo.hibernate.entity.Account;
import it.cnr.ilc.lexo.hibernate.entity.AccountType;
import it.cnr.ilc.lexo.manager.AccessManager;
import it.cnr.ilc.lexo.manager.AccessManager.Permission;
import it.cnr.ilc.lexo.manager.AccessManager.Topic;
import it.cnr.ilc.lexo.manager.DomainManager;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.data.Info;
import it.cnr.ilc.lexo.service.data.Session;
import it.cnr.ilc.lexo.service.helper.AuthenticationHelper;
import it.cnr.ilc.lexo.service.helper.InfoHelper;
import java.util.EnumSet;
import java.util.List;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
@Path("system")
public class SystemService extends Service {

    static final Logger logger = LoggerFactory.getLogger(SystemService.class.getName());

    private final AccessManager accessManager = ManagerFactory.getManager(AccessManager.class);
    private final InfoHelper infoHelper = new InfoHelper();
    private final AuthenticationHelper authenticationHelper = new AuthenticationHelper();

    @GET
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response info() throws JsonProcessingException {
        Info data = infoHelper.newData();
        String json = infoHelper.toJson(data);
        return Response.ok().entity(json).build();
    }

    @GET
    @Path("sessions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sessions(@QueryParam("key") String key) throws JsonProcessingException {
        Response resp;
        try {
            checkKey(key);
            if (!accessManager.hasPermission(account, Topic.SYSTEM, Permission.READ)) {
//            log(Level.INFO, "get sessions, not allowed");
                return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
            }
//        log(Level.INFO, "get sessions");
            List<Session> sessionDatas = authenticationHelper.createSessionData(list());
            String json = authenticationHelper.sessionsToJson(sessionDatas);
            resp = Response.ok().entity(json).build();
        } catch (Exception e) {
            logger.error("Error on server" + e.getLocalizedMessage());
            resp = Response.serverError().encoding("Error on Server").build();
        }
        return resp;
    }

    @GET
    @Path("caches")
    @Produces(MediaType.APPLICATION_JSON)
    public Response caches(@QueryParam("key") String key) throws JsonProcessingException {
        Response resp;
        try {
            checkKey(key);
            if (!accessManager.hasPermission(account, Topic.SYSTEM, Permission.READ)) {
//            log(Level.INFO, "reload caches, not allowed");
                return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
            }
//        log(Level.INFO, "reload caches");
            ManagerFactory.loadCaches();
            resp = Response.ok().build();
        } catch (Exception e) {
            logger.error("Error on server" + e.getLocalizedMessage());
            resp = Response.serverError().encoding("Error on Server").build();
        }
        return resp;
    }

    @GET
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create() {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
        Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();
        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setDelimiter(";");
        schemaExport.create(EnumSet.of(TargetType.STDOUT), metadata);
        return Response.ok().build();
    }

    @GET
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update() {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
        Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();
        SchemaUpdate schemaUpdate = new SchemaUpdate();
        schemaUpdate.setDelimiter(";");
        schemaUpdate.execute(EnumSet.of(TargetType.STDOUT), metadata);
        return Response.ok().build();
    }

    @GET
    @Path("init")
    @Produces(MediaType.APPLICATION_JSON)
    public Response init() {
        // superuser password can be used in order to get access as any user
        Response resp;
        try {
            DomainManager domainManager = new DomainManager();

            AccountType accountType = new AccountType();
            accountType.setName("Superuser");
            accountType.setColor("lightgrey");
            domainManager.insert(accountType);

            Account account = new Account();
            account.setType(accountType);
            account.setUsername("superuser");
            account.setPassword((String) HibernateUtil.getSession().createSQLQuery("select upper(sha1('passeuord'))").uniqueResult());
            account.setEnabled(Boolean.FALSE);
            domainManager.insert(account);

            accountType = new AccountType();
            accountType.setName("Administrator");
            accountType.setColor("lightsteelblue");
            accountType.setPosition(1);
            domainManager.insert(accountType);

            account = new Account();
            account.setType(accountType);
            account.setUsername("admin");
            account.setPassword((String) HibernateUtil.getSession().createSQLQuery("select upper(sha1('passeuord'))").uniqueResult());
            account.setEnabled(Boolean.TRUE);
            domainManager.insert(account);

            accountType = new AccountType();
            accountType.setName("Editor");
            accountType.setColor("orangered");
            accountType.setPosition(2);
            domainManager.insert(accountType);

            accountType = new AccountType();
            accountType.setName("Viewer");
            accountType.setColor("lightsteelblue");
            accountType.setPosition(3);
            domainManager.insert(accountType);
            resp = Response.ok().build();
        } catch (Exception e) {
            logger.error("Error on server" + e.getLocalizedMessage());
            resp = Response.serverError().encoding("Error on Server").build();
 
        }
        return resp;
    }

}
