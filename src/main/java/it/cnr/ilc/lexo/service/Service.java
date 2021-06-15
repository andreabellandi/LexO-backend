package it.cnr.ilc.lexo.service;

import io.swagger.annotations.Contact;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import it.cnr.ilc.lexo.HibernateUtil;
import it.cnr.ilc.lexo.LexOFilter;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.hibernate.entity.Account;
import it.cnr.ilc.lexo.hibernate.entity.SuperEntity.Status;
import it.cnr.ilc.lexo.service.data.AuthenticationData;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author andreabellandi
 */
@SwaggerDefinition(
        info = @Info(
                description = "OntoLex-Lemon lexica manager",
                version = "V0.1",
                title = "LexO API"//,
//                license = @License(
//                        name = "Apache 2.0",
//                        url = "http://www.apache.org/licenses/LICENSE-2.0"
//                )
        ),
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"},
        schemes = {SwaggerDefinition.Scheme.HTTPS, SwaggerDefinition.Scheme.HTTP},
        externalDocs = @ExternalDocs(value = "LexO-backend", url = "https://github.com/andreabellandi/LexO-backend")
)
abstract class Service {

    protected AuthenticationData authenticationData;
    protected Account account;
    protected Map<String, Object> session;

    protected void putkKey(AuthenticationData authenticationData) {
        this.authenticationData = authenticationData;
        KEY_MANAGER.put(authenticationData);
    }

    protected void checkKey(String key) throws Exception {
        authenticationData = KEY_MANAGER.get(key);
        if (authenticationData != null) {
            account = HibernateUtil.getSession().get(Account.class, authenticationData.getId());
            if (account == null || !account.getStatus().equals(Status.VALID) || !account.getEnabled()) {
                KEY_MANAGER.remove(key);
                authenticationData = null;
                account = null;
            }
        }
        session = KEY_MANAGER.getSession(key);
    }

    protected List<AuthenticationData> list() {
        return KEY_MANAGER.list();
    }

//    protected void log(Level level, String message) {
//        Logger.getLogger(LexOFilter.CONTEXT).log(level, "(" + authenticationData.getUsername() + ") [" + authenticationData.getKey() + "] " + message);
//    }
//
//    protected void log(Level level, String message, Throwable t) {
//        Logger.getLogger(LexOFilter.CONTEXT).log(level, "(" + authenticationData.getUsername() + ") [" + authenticationData.getKey() + "] " + message, t);
//    }

    private static final KeyManager KEY_MANAGER = new KeyManager();

    private static class KeyManager {

        private static final long TIMEOUT = Long.parseLong(LexOProperties.getProperty("service.sessionTimeout", "1800")) * 1000;
        private static final int KEY_BUFFER_SIZE = 21;

        private final Random random = new Random(System.currentTimeMillis());
        private final Map<String, AuthenticationData> keys = new HashMap<>();
        private final Map<String, Timer> timers = new HashMap<>();
        private final Map<String, Map<String, Object>> sessions = new HashMap<>();

        private AuthenticationData get(String key) {
            if (key == null) {
                return null;
            }
            synchronized (keys) {
                Timer timer = timers.remove(key);
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                }
                AuthenticationData authenticationData = keys.get(key);
                if (authenticationData != null) {
                    timer = new Timer();
                    timer.schedule(new CancelTask(key), TIMEOUT);
                    timers.put(key, timer);
                    authenticationData.setLastActionTime(new Date());
                }
                return authenticationData;
            }
        }

        private Map<String, Object> getSession(String key) {
            if (key == null) {
                return null;
            }
            Map<String, Object> session = sessions.get(key);
            if (session == null) {
                session = new HashMap<>();
                sessions.put(key, session);
            }
            return session;
        }

        private void put(AuthenticationData authenticationData) {
            synchronized (keys) {
                String key = generateKey();
                while (keys.containsKey(key)) {
                    key = generateKey();
                }
                authenticationData.setKey(key);
                authenticationData.setLoginTime(new Date());
                authenticationData.setLastActionTime(authenticationData.getLoginTime());
                keys.put(key, authenticationData);
                Timer timer = new Timer();
                timer.schedule(new CancelTask(key), TIMEOUT);
                timers.put(key, timer);
            }
        }

        private void remove(String key) {
            synchronized (keys) {
                keys.remove(key);
                sessions.remove(key);
                Timer timer = timers.remove(key);
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                }
            }
        }

        private List<AuthenticationData> list() {
            return new ArrayList(keys.values());
        }

        private String generateKey() {
            byte[] buffer = new byte[KEY_BUFFER_SIZE];
            random.nextBytes(buffer);
            String key = Base64.getUrlEncoder().encodeToString(buffer);
            return key;
        }

        private class CancelTask extends TimerTask {

            private final String key;

            private CancelTask(String key) {
                this.key = key;
            }

            @Override
            public void run() {
                synchronized (keys) {
                    Timer timer = timers.remove(key);
                    timer.cancel();
                    timer.purge();
                    sessions.remove(key);
                    AuthenticationData authenticationData = keys.remove(key);
//                    Logger.getLogger(LexOFilter.CONTEXT).info("(" + authenticationData.getUsername() + ") [" + key + "] session expired");
                }
            }

        }

    }

}
