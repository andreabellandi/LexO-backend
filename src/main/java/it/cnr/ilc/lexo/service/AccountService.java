package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Api;
import it.cnr.ilc.lexo.service.data.AccountData;
import it.cnr.ilc.lexo.hibernate.entity.Account;
import it.cnr.ilc.lexo.hibernate.entity.AccountType;
import it.cnr.ilc.lexo.manager.AccessManager;
import it.cnr.ilc.lexo.manager.AccessManager.Permission;
import it.cnr.ilc.lexo.manager.AccessManager.Topic;
import it.cnr.ilc.lexo.manager.AccountManager;
import it.cnr.ilc.lexo.manager.ManagerException;
import it.cnr.ilc.lexo.manager.ManagerFactory;
import it.cnr.ilc.lexo.service.helper.AccountHelper;
import it.cnr.ilc.lexo.service.helper.AccountTypeHelper;
import it.cnr.ilc.lexo.service.data.AccountTypeData;
import it.cnr.ilc.lexo.service.data.FilterData;
import it.cnr.ilc.lexo.service.data.PasswordData;
import it.cnr.ilc.lexo.service.helper.FilterHelper;
import it.cnr.ilc.lexo.service.helper.Helper;
import it.cnr.ilc.lexo.service.helper.HelperException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author oakgen
 */
@Path("account")
//@Api("User account")
public class AccountService extends Service {

    private static final String ACCOUNTS = "accounts";

    private final AccessManager accessManager = ManagerFactory.getManager(AccessManager.class);
    private final AccountManager accountManager = ManagerFactory.getManager(AccountManager.class);
    private final FilterHelper filterHelper = new FilterHelper();
    private final AccountHelper accountHelper = new AccountHelper();
    private final AccountTypeHelper accountTypeHelper = new AccountTypeHelper();

    @GET
    @Path("types")
    @Produces(MediaType.APPLICATION_JSON)
    public Response types(@QueryParam("key") String key) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        if (!accessManager.hasPermission(account, Topic.TYPES, Permission.READ)) {
//            // log(Level.INFO, "get account types, not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
//        // log(Level.INFO, "get account types");
        List<AccountType> accountTypes = accountManager.getAccountTypes();
        List<AccountTypeData> accountTypesData = accountTypeHelper.newDataList(accountTypes, accountTypeHelper.getDefaultComparator());
        String json = accountTypeHelper.toJson(accountTypesData);
        return Response.ok(json).build();
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@QueryParam("key") String key) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        if (!accessManager.hasPermission(account, Topic.ACCOUNT, Permission.READ)) {
//            // log(Level.INFO, "get accounts, not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        try {
            List<Map<String, Object>> records = accountManager.loadAccounts();
            List<AccountData> accountsData = accountHelper.newDataListSql(records, accountHelper.getDefaultComparator());
//            // log(Level.INFO, "get accounts");
            String json = accountHelper.toJson(accountsData);
            session.remove(ACCOUNTS);
            return Response.ok(json).build();
        } catch (HelperException ex) {
//            // log(Level.INFO, "get accounts, " + ex.getMessage());
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@QueryParam("key") String key, String content) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        if (!accessManager.hasPermission(account, Topic.ACCOUNT, Permission.READ)) {
            // log(Level.INFO, "get accounts with filter , not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        try {
            boolean cache;
            FilterData<AccountData> filterData = filterHelper.fromJson(content);
            FilterData cacheFilterData = (FilterData) session.get(ACCOUNTS);
            if (cache = filterHelper.isModified(filterData, cacheFilterData)) {
                List<Map<String, Object>> records = accountManager.loadAccounts();
                List<AccountData> accountDatas = accountHelper.newDataListSql(records, filterData);
                filterData.setData(accountDatas);
                filterData.setReload(Boolean.TRUE);
                session.put(ACCOUNTS, filterData);
            } else {
                filterData.setData(cacheFilterData.getData());
                filterData.setReload(Boolean.FALSE);
            }
            filterData = filterHelper.newRangedFilterData(filterData);
            // log(Level.INFO, "get accounts with filter" + (!cache ? " from cache" : ""));
            String json = filterHelper.toJson(filterData);
            return Response.ok(json).build();
        } catch (HelperException ex) {
            // log(Level.INFO, "get accounts with filter, " + ex.getMessage());
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@QueryParam("key") String key, String content) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        if (!accessManager.hasPermission(account, Topic.ACCOUNT, Permission.WRITE)) {
            // log(Level.INFO, "create new account, not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        try {
            AccountData accountData = accountHelper.fromJson(content);
            Account caccount = accountManager.createAccount(accountData.getType(), accountData.getUsername(), accountData.getPassword());
            // log(Level.INFO, "create new account '" + accountData.getUsername() + "'");
            session.remove(ACCOUNTS);
            accountHelper.fillAfterCreation(accountData, caccount);
            String json = accountHelper.toJson(accountData);
            return Response.ok(json).build();
        } catch (HelperException | ManagerException ex) {
            // log(Level.INFO, "create new account, " + ex.getMessage());
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("key") String key, @PathParam("id") Long id) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        Account caccount = accountManager.loadAccount(id);
        if (caccount == null) {
            // log(Level.INFO, "get account " + id + ", account not found");
            return Response.ok(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("account not found").build();
        }
        if (!accessManager.hasPermissionOnAccountable(account, Topic.ACCOUNT, Permission.READ, caccount)) {
            // log(Level.INFO, "get account '" + caccount.getUsername() + "', not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        // log(Level.INFO, "get account '" + caccount.getUsername() + "'");
        AccountData caccountData = accountHelper.newData(caccount);
        String json = accountHelper.toJson(caccountData);
        return Response.ok(json).build();
    }

    @GET
    @Path("{id}/remove")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@QueryParam("key") String key, @PathParam("id") Long id) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        Account caccount = accountManager.loadAccount(id);
        if (caccount == null) {
            // log(Level.INFO, "remove account " + id + ", account not found");
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("account not found").build();
        }
        if (!accessManager.hasPermissionOnAccountable(account, Topic.ACCOUNT, Permission.WRITE, caccount)) {
            // log(Level.INFO, "remove account '" + caccount.getUsername() + "', not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        try {
            accountManager.removeAccount(caccount);
            // log(Level.INFO, "remove account '" + caccount.getUsername() + "'");
            session.remove(ACCOUNTS);
            return Response.ok().build();
        } catch (ManagerException ex) {
            // log(Level.INFO, "remove account '" + caccount.getUsername() + "', " + ex.getMessage());
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{id}/restore")
    @Produces(MediaType.APPLICATION_JSON)
    public Response restore(@QueryParam("key") String key, @PathParam("id") Long id) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        Account caccount = accountManager.getAccount(id);
        if (caccount == null) {
            // log(Level.INFO, "restore account " + id + ", account not found");
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("account not found").build();
        }
        if (!accessManager.hasPermissionOnAccountable(account, Topic.ACCOUNT, Permission.WRITE, caccount)) {
            // log(Level.INFO, "restore account '" + caccount.getUsername() + "', not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        try {
            accountManager.restoreAccount(caccount);
            // log(Level.INFO, "restore account '" + caccount.getUsername() + "'");
            session.remove(ACCOUNTS);
            return Response.ok().build();
        } catch (ManagerException ex) {
            // log(Level.INFO, "restore account '" + caccount.getUsername() + "', " + ex.getMessage());
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("{id}/type")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setType(@QueryParam("key") String key, @PathParam("id") Long id, String content) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        Account caccount = accountManager.loadAccount(id);
        if (caccount == null) {
            // log(Level.INFO, "set type of account " + id + ", account not found");
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("account not found").build();
        }
        if (!accessManager.hasPermissionOnAccountable(account, Topic.ACCOUNT, Permission.WRITE, caccount)) {
            // log(Level.INFO, "set type of '" + caccount.getUsername() + "', not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        try {
            accountManager.setType(caccount, content);
            // log(Level.INFO, "set type of '" + caccount.getUsername() + "' to " + content);
            session.remove(ACCOUNTS);
            AccountData caccountData = accountHelper.newData(caccount);
            String json = accountHelper.toJson(caccountData);
            return Response.ok(json).build();
        } catch (ManagerException ex) {
            // log(Level.INFO, "set type of '" + caccount.getUsername() + "', " + ex.getMessage());
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("{id}/username")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setUsername(@QueryParam("key") String key, @PathParam("id") Long id, String content) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        Account caccount = accountManager.loadAccount(id);
        if (caccount == null) {
            // log(Level.INFO, "set username of account " + id + ", account not found");
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("account not found").build();
        }
        if (!accessManager.hasPermissionOnAccountable(account, Topic.ACCOUNT, Permission.WRITE, caccount)) {
            // log(Level.INFO, "set username of '" + caccount.getUsername() + "', not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        try {
            accountManager.setUsername(caccount, content);
            // log(Level.INFO, "set username of '" + caccount.getUsername() + "' to '" + content + "'");
            session.remove(ACCOUNTS);
            AccountData caccountData = accountHelper.newData(caccount);
            String json = accountHelper.toJson(caccountData);
            return Response.ok(json).build();
        } catch (ManagerException ex) {
            // log(Level.INFO, "set username of '" + caccount.getUsername() + "', not allowed");
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("{id}/password")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPassword(@QueryParam("key") String key, @PathParam("id") Long id, String content) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        Account caccount = accountManager.loadAccount(id);
        if (caccount == null) {
            // log(Level.INFO, "set password of account " + id + ", account not found");
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("account not found").build();
        }
        if (!accessManager.hasPermissionOnAccountable(account, Topic.ACCOUNT, Permission.WRITE, caccount)) {
            // log(Level.INFO, "set password of account " + id + ", not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        try {
            PasswordData passwordData = accountHelper.fromJsonPassword(content);
            if (accessManager.hasPermission(account, Topic.ACCOUNT, Permission.WRITE)) {
                accountManager.setPassword(caccount, passwordData.getNewPassword());
            } else {
                accountManager.setPassword(caccount, passwordData.getCurrentPassword(), passwordData.getNewPassword());
            }
            // log(Level.INFO, "set password of '" + caccount.getUsername() + "'");
            session.remove(ACCOUNTS);
            AccountData caccountData = accountHelper.newData(caccount);
            String json = accountHelper.toJson(caccountData);
            return Response.ok(json).build();
        } catch (ManagerException | HelperException ex) {
            // log(Level.INFO, "set password of '" + caccount.getUsername() + "', " + ex.getMessage());
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();

        }
    }

    @POST
    @Path("{id}/name")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setName(@QueryParam("key") String key, @PathParam("id") Long id, String content) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        Account caccount = accountManager.loadAccount(id);
        if (caccount == null) {
            // log(Level.INFO, "set name of account " + id + ", account not found");
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("account not found").build();
        }
        if (!accessManager.hasPermissionOnAccountable(account, Topic.ACCOUNT, Permission.WRITE, caccount)) {
            // log(Level.INFO, "set name of account " + id + ", not allopwed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        // log(Level.INFO, "set name of '" + caccount.getUsername() + "' to '" + content + "'");
        session.remove(ACCOUNTS);
        accountManager.setName(caccount, content);
        AccountData caccountData = accountHelper.newData(caccount);
        String json = accountHelper.toJson(caccountData);
        return Response.ok(json).build();
    }

    @POST
    @Path("{id}/email")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setEmail(@QueryParam("key") String key, @PathParam("id") Long id, String content) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        Account caccount = accountManager.loadAccount(id);
        if (caccount == null) {
            // log(Level.INFO, "set email of account " + id + ", account not found");
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("account not found").build();
        }
        if (!accessManager.hasPermissionOnAccountable(account, Topic.ACCOUNT, Permission.WRITE, caccount)) {
            // log(Level.INFO, "set email of account " + id + ", not allopwed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        // log(Level.INFO, "set email of '" + caccount.getUsername() + "' to '" + content + "'");
        session.remove(ACCOUNTS);
        accountManager.setEmail(caccount, content);
        AccountData caccountData = accountHelper.newData(caccount);
        String json = accountHelper.toJson(caccountData);
        return Response.ok(json).build();
    }

    @POST
    @Path("{id}/enabled")
    @Produces(MediaType.TEXT_HTML)
    public Response setEnabled(@QueryParam("key") String key, @PathParam("id") Long id, String content) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        Account caccount = accountManager.loadAccount(id);
        if (caccount == null) {
            // log(Level.INFO, "set enabled of account " + id + ", account not found");
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("account not found").build();
        }
        if (!accessManager.hasPermissionOnAccountable(account, Topic.ACCOUNT, Permission.WRITE, caccount)) {
            // log(Level.INFO, "set enabled of account " + id + ", not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        try {
            // log(Level.INFO, "set enabled of account '" + caccount.getUsername() + "' to " + content);
            session.remove(ACCOUNTS);
            accountManager.setEnabled(caccount, Helper.parseBoolean(content));
            AccountData caccountData = accountHelper.newData(caccount);
            String json = accountHelper.toJson(caccountData);
            return Response.ok(json).build();
        } catch (HelperException ex) {
            // log(Level.INFO, "set enabled of '" + caccount.getUsername() + "', " + ex.getMessage());
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Path("{id}/settings/{name}")
    @Produces(MediaType.TEXT_HTML)
    public Response setSetting(@QueryParam("key") String key, @PathParam("id") Long id, @PathParam("name") String name, String content) {
        checkKey(key);
        if (account == null) {
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("access denied").build();
        }
        Account caccount = accountManager.loadAccount(id);
        if (caccount == null) {
            // log(Level.INFO, "set setting " + name + " of account " + id + ", account not found");
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("account not found").build();
        }
        if (!accessManager.hasPermissionOnAccountable(account, Topic.ACCOUNT, Permission.WRITE, caccount)) {
            // log(Level.INFO, "set setting " + name + " of account " + id + ", not allowed");
            return Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("not allowed").build();
        }
        try {
            accountManager.setSetting(caccount, name, content);
            // log(Level.INFO, "set setting " + name + " of '" + caccount.getUsername() + "' to '" + content + "'");
            AccountData caccountData = accountHelper.newData(caccount);
            String json = accountHelper.toJson(caccountData);
            return Response.ok(json).build();
        } catch (ManagerException ex) {
            // log(Level.INFO, "set name of '" + caccount.getUsername() + "', " + ex.getMessage());
            return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
    }

}
