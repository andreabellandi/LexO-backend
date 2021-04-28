package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.cnr.ilc.lexo.manager.AccountManager;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.helper.AuthenticationHelper;
import it.cnr.ilc.lexo.service.helper.HelperException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andreabellandi
 */
@Path("authentication")
//@Api("Authentication")
public class AuthenticationService extends Service {

    private final AccountManager accountManager = ManagerFactory.getManager(AccountManager.class);
    private final AuthenticationHelper authenticationHelper = new AuthenticationHelper();

    @POST
    @Path("authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/authenticate",
            produces = "application/json; charset=UTF-8")
    @ApiOperation(value = "An authentication token",
            notes = "This method returns an authentication token that must be used to run the services")
    public Response authenticate(@Context HttpServletRequest request, String content, @QueryParam("onlyKey") Boolean onlyKey) throws HelperException {
        authenticationData = authenticationHelper.fromJson(content);
        account = accountManager.authenticate(authenticationData.getUsername(), authenticationData.getPassword());
        if (account == null) {
            account = accountManager.loadAccountByUsername(authenticationData.getUsername());
            if (account == null) {
//                log(Level.INFO, "access denied from " + request.getRemoteAddr() + " on " + request.getHeader("user-agent"));
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("user not found").build();
            } else if (!account.getEnabled()) {
//                log(Level.INFO, "access denied from " + request.getRemoteAddr() + " on " + request.getHeader("user-agent"));
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("user disabled").build();
            } else {
//                log(Level.INFO, "access denied from " + request.getRemoteAddr() + " on " + request.getHeader("user-agent"));
                return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("wrong password").build();
            }
        } else {
            authenticationHelper.fillData(authenticationData, account, request);
            putkKey(authenticationData);
//            log(Level.INFO, "authenticate from " + request.getRemoteAddr() + " on " + request.getHeader("user-agent"));
            if (Boolean.TRUE.equals(onlyKey)) {
                return Response.ok().type(MediaType.TEXT_PLAIN).entity(authenticationData.getKey()).build();
            } else {
                String json = authenticationHelper.toJson(authenticationData);
                return Response.ok(json).build();
            }
        }
    }

    @GET
    @Path("keepAlive")
    @Produces(MediaType.APPLICATION_JSON)
    public Response keepAlive(@QueryParam("key") String key) {
        checkKey(key);
        if (authenticationData == null) {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
//        log(Level.INFO, "keep alive");
        return Response.ok().build();
    }
}
