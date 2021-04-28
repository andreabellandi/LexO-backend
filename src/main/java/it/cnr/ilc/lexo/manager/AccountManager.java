package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.HibernateUtil;
import it.cnr.ilc.lexo.hibernate.entity.Account;
import it.cnr.ilc.lexo.hibernate.entity.AccountType;
import it.cnr.ilc.lexo.hibernate.entity.SuperEntity.Status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.hibernate.query.NativeQuery;

/**
 *
 * @author andreabellandi
 */
public final class AccountManager implements Manager, Cached {

    public enum Setting {

        START_INDEX("startIndex", String.class);

        private final String name;
        private final Class clazz;

        private Setting(String name, Class clazz) {
            this.name = name;
            this.clazz = clazz;
        }

        public String getName() {
            return name;
        }

        public Class getClazz() {
            return clazz;
        }

        public String formatValue(String content) {
            if (clazz.equals(Boolean.class)) {
                return content.toLowerCase();
            }
            return content;
        }

        public boolean chekValue(String value) {
            if (clazz.equals(Boolean.class)) {
                return "true".equals(value.toLowerCase()) || "false".equals(value.toLowerCase());
            } else if (clazz.equals(Integer.class)) {
                try {
                    Integer.parseInt(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            } else if (clazz.equals(Double.class)) {
                try {
                    Double.parseDouble(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return true;
        }
    }

    private static final Map<String, Setting> SETTINGS = new HashMap<>();

    static {
        Arrays.stream(Setting.values()).forEach(s -> SETTINGS.put(s.getName(), s));
    }

    private final List<AccountType> types = new ArrayList<>();
    private final Map<String, AccountType> typesByName = new HashMap<>();

    private final DomainManager domainManager = ManagerFactory.getDomainManager();

    AccountManager() {
        reloadCache();
    }

    @Override
    public void reloadCache() {
        types.clear();
        typesByName.clear();
        String hql = "select at from AccountType at where at.name != 'Superuser' order by at.position";
        List<AccountType> list = HibernateUtil.getSession().createQuery(hql).list();
        for (AccountType accountType : list) {
            types.add(accountType);
            typesByName.put(accountType.getName(), accountType);
        }
    }

    public List<AccountType> getAccountTypes() {
        return types;
    }

    public AccountType getAccountTypeByName(String name) {
        return name == null ? null : typesByName.get(name);
    }

    public Account authenticate(String username, String password) {
        password = Matcher.quoteReplacement(password).replaceAll("'", "''");
        String sql = "select a.* \n"
                + "from Account a join Account s on s.username = 'superuser'\n"
                + "where a.username = ':u' and a.status = 1 and a.enabled = true\n"
                + "and upper(sha1(':p')) in (a.password, s.password)";
        sql = sql.replaceFirst(":u", username).replaceFirst(":p", password);
        NativeQuery<Account> query = HibernateUtil.getSession().createSQLQuery(sql).addEntity(Account.class);
        return query.uniqueResult();
    }

    public List<Map<String, Object>> loadAccounts() {
        String sql = "select \n"
                + " a.id,\n"
                + " at.name type,\n"
                + " a.username,\n"
                + " a.password,\n"
                + " a.name,\n"
                + " a.email,\n"
                + " a.enabled\n"
                + "from Account a \n"
                + "join AccountType at on at.id = a.type_id\n"
                + "where a.username <> 'superuser' and a.status = 1 ";
        return HibernateUtil.getSession().createSQLQuery(sql).setResultTransformer(HibernateUtil.getResultTransformer()).list();
    }

    public Account getAccount(Long id) {
        return HibernateUtil.getSession().get(Account.class, id);
    }

    public Account loadAccount(Long id) {
        Account account = HibernateUtil.getSession().get(Account.class, id);
        return account != null && account.getStatus().equals(Status.VALID) ? account : null;
    }

    public Account loadAccountByUsername(String value) {
        String hql = "select a from Account a where a.username = '" + value + "'";
        return (Account) HibernateUtil.getSession().createQuery(hql).uniqueResult();
    }

    private boolean existsUsername(String username) {
        String sql = "select count(*) from Account where username = '" + username + "' and (status = 1 or status = 2)";
        return ((Number) HibernateUtil.getSession().createSQLQuery(sql).uniqueResult()).intValue() > 0;
    }

    public Account createAccount(String type, String username, String password) throws ManagerException {
        Account account = new Account();
        if (type == null) {
            throw new ManagerException("missing type");
        }
        account.setType(typesByName.get(type));
        if (account.getType() == null) {
            throw new ManagerException("wrong type: " + type);
        }
        if (username == null || username.isEmpty()) {
            throw new ManagerException("missing username");
        }
        if (existsUsername(username)) {
            throw new ManagerException("username already exists");
        }
        account.setUsername(username);
        if (password == null || password.isEmpty()) {
            throw new ManagerException("missing password");
        }
        account.setPassword(getPassword(password));
        account.setEnabled(Boolean.FALSE);
        account.setSettings(new HashMap<>());
        domainManager.insert(account);
        return account;
    }

    private boolean hasReferences(Account account) {
        /*
        Interrogare l'ontologia per determinare se l'utente ha riferimenti a qualche
        entrata per poter essere rimosso.
         */
        return false;
    }

    public void removeAccount(Account account) throws ManagerException {
        if (hasReferences(account)) {
            throw new ManagerException("account referenced");
        }
        if ((account.getUsername() == null || account.getUsername().isEmpty()) && (account.getPassword() == null || account.getPassword().isEmpty())) {
            HibernateUtil.getSession().delete(account);
        } else {
            domainManager.deleteWithHistory(account);
        }
    }

    public void restoreAccount(Account account) throws ManagerException {
        if (!account.getStatus().equals(Status.REMOVED)) {
            throw new ManagerException("account not removed");
        }
        if (account.getType().getStatus().equals(Status.REMOVED)) {
            throw new ManagerException("account not restorable");
        }
        domainManager.updateWithHistory(account);
    }

    public void setName(Account account, String content) {
        account.setName(content);
        domainManager.update(account);
    }

    public void setEmail(Account account, String content) {
        account.setEmail(content);
        domainManager.update(account);
    }

    public void setEnabled(Account account, boolean enabled) {
        account.setEnabled(enabled);
        domainManager.update(account);
    }

    public void setUsername(Account account, String content) throws ManagerException {
        if (content.isEmpty()) {
            throw new ManagerException("empty username not allowed");
        }
        String sql = "select count(*) from Account where id <> " + account.getId() + " and status = 1 and username = '" + content + "'";
        if (((Number) HibernateUtil.getSession().createSQLQuery(sql).uniqueResult()).intValue() > 0) {
            throw new ManagerException("username already exists");
        }
        account.setUsername(content);
        domainManager.update(account);
    }

    private String getPassword(String password) {
        String sql = "select upper(sha1('" + password + "'))";
        return (String) HibernateUtil.getSession().createSQLQuery(sql).uniqueResult();
    }

    public void setPassword(Account account, String newPassword) throws ManagerException {
        if (newPassword.isEmpty()) {
            throw new ManagerException("empty password not allowed");
        }
        newPassword = getPassword(newPassword);
        account.setPassword(newPassword);
        domainManager.update(account);
    }

    public void setPassword(Account account, String currentPassword, String newPassword) throws ManagerException {
        if (newPassword.isEmpty()) {
            throw new ManagerException("empty password not allowed");
        }
        currentPassword = getPassword(currentPassword);
        if (!account.getPassword().equals(currentPassword)) {
            throw new ManagerException("wrong current password");
        }
        newPassword = getPassword(newPassword);
        account.setPassword(newPassword);
        domainManager.update(account);
    }

    public void setType(Account account, String type) throws ManagerException {
        AccountType accountType = typesByName.get(type);
        if (accountType == null) {
            throw new ManagerException("type not found");
        }
        account.setType(accountType);
        domainManager.update(account);
    }

    public void setSetting(Account account, String name, String content) throws ManagerException {
        Setting setting = SETTINGS.get(name);
        if (setting == null) {
            throw new ManagerException("setting name not found");
        }
        if (!setting.chekValue(content)) {
            throw new ManagerException("wrong setting value");
        }
        account.getSettings().put(name, setting.formatValue(content));
        domainManager.update(account);
    }

}
