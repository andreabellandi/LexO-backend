package it.cnr.ilc.lexo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger("HibernateUtil");

    private static SessionFactory sessionFactory = null;

    private static SessionFactory getSessionFactory() {
        logger.debug("getSessionFactory() sessionFactory is: " + sessionFactory);
        if (sessionFactory == null) {
            try {
                StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
                logger.debug("Service Registry created " + serviceRegistry);

                Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();
                logger.debug("Metadata created " + metadata);

                sessionFactory = metadata.buildSessionFactory();
                logger.debug("sessionFactory is : " + sessionFactory);
            } catch (Exception e) {
                logger.error("Exception: " + e.getLocalizedMessage());
                throw new HibernateException("Could not get Hibernate Session Factory: " + e.getLocalizedMessage());
            }
        }
        logger.debug("getSessionFactory() sessionFactory return: " + sessionFactory);
        return sessionFactory;
    }

    public static Session getSession() throws HibernateException {
        logger.debug("getSession()");
        if (getSessionFactory() != null) {
            Session s = getSessionFactory().getCurrentSession();
            logger.debug("getSession() session is: " + s);
            return s;
        } else {
            throw new HibernateException("Session Factory is null!");
        }
    }

    public static Session openSession() throws Exception {
        if (getSessionFactory() != null) {
            logger.debug("openSession()");
            return getSessionFactory().openSession();
        } else {
            logger.error("Error opening session");
            throw new HibernateException("Session Factory is null!");
        }
    }

    static void closeFactory() {
        if (getSessionFactory() != null) {
            logger.info("closeSession()");
            getSessionFactory().close();
        }
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
