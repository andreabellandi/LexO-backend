package it.cnr.ilc.lexo.manager;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andreabellandi
 */
public class ManagerFactory {

    private static final Map<Class<? extends Manager>, Manager> MANAGERS = new HashMap<>();

    private static final DomainManager DOMAIN_MANAGER = new DomainManager();

    public static <T extends Manager> T getManager(Class<T> clazz) {
        Manager manager = MANAGERS.get(clazz);
        if (manager == null) {
            try {
                manager = clazz.getDeclaredConstructor().newInstance();
                MANAGERS.put(clazz, manager);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("unable to instantiate " + clazz.getName(), e);
            }
        }
        return (T) manager;
    }

    public static void loadCaches() {
        for (Manager manager : MANAGERS.values()) {
            if (manager instanceof Cached) {
                ((Cached) manager).reloadCache();
            }
        }
    }

    static DomainManager getDomainManager() {
        return DOMAIN_MANAGER;
    }
}
