package it.cnr.ilc.lexo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.transform.ResultTransformer;

/**
 *
 * @author andreabellandi
 */
public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY;

    static {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build(); 
        Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata(); 
        SESSION_FACTORY = metadata.buildSessionFactory();
    }

    public static Session getSession() {
        return SESSION_FACTORY.getCurrentSession();
    }

    public static Session openSession() {
        return SESSION_FACTORY.openSession();
    }

    static void closeFactory() {
        SESSION_FACTORY.close();
    }

    public static ResultTransformer getResultTransformer() {
        return new ResultTransformer() {

            @Override
            public Object transformTuple(Object[] values, String[] names) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < values.length; i++) {
                    map.put(names[i], values[i]);
                }
                return map;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
    }
    
}
