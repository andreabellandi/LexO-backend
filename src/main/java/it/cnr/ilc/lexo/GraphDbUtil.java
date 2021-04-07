package it.cnr.ilc.lexo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.config.RepositoryConfigException;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class GraphDbUtil {

    private static final Object LOCK = new Object();
    private static Repository REPOSITORY = null;
    private static final List<RepositoryConnection> POOL = new ArrayList<>();
    private static final Map<Thread, RepositoryConnection> ACTIVES = new HashMap<>();
    private static final int SIZE = Integer.parseInt(LexoProperties.getProperty("GraphDb.size", "5"));
    private static int current = 0;
    private static final Logger logger = LoggerFactory.getLogger(GraphDbUtil.class);

    static {
        RepositoryManager repositoryManager = new RemoteRepositoryManager(LexoProperties.getProperty("GraphDb.url", "https://lexo-datasets.ilc.cnr.it:7200"));
        repositoryManager.init();
        try {
            REPOSITORY = repositoryManager.getRepository(LexoProperties.getProperty("GraphDb.repository", "Simple"));
        } catch (RepositoryException | RepositoryConfigException e) {
            logger.error("Unable to connect to GraphDB: " + e);
        }
    }

    private static void testConnection(RepositoryConnection connection) throws Exception {
    }

    public static RepositoryConnection getConnection() {
        synchronized (LOCK) {
            RepositoryConnection connection = ACTIVES.get(Thread.currentThread()); 
            if (connection != null) {
                logger.debug(Thread.currentThread().getName() + " get active connection");
                return connection;
            }
            try {
                connection = POOL.remove(0);
                try {
                    testConnection(connection);
                    logger.debug(Thread.currentThread().getName() + " get pool connection");
                } catch (Exception ex) {
                    logger.debug(Thread.currentThread().getName() + " get inactive connection");
                    try {
                        connection.close();
                    } catch (RepositoryException e) {
                        logger.error(e.getLocalizedMessage());
                    }
                    current--;
                    connection = getConnection();
                }
            } catch (IndexOutOfBoundsException ex) {
                if (current < SIZE) {
                    logger.debug(Thread.currentThread().getName() + " get repository connection");
                    connection = REPOSITORY.getConnection();
                    current++;
                } else {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException iex) {
                        logger.error(iex.getLocalizedMessage());
                    }
                    connection = getConnection();
                }
            }
            ACTIVES.put(Thread.currentThread(), connection);
            return connection;
        }
    }

    public static void releaseConnection() {
        synchronized (LOCK) {
            RepositoryConnection connection = ACTIVES.remove(Thread.currentThread());
            if (connection != null) {
                if (connection.isActive()) {
                    connection.rollback();
                }
                POOL.add(connection);
                LOCK.notifyAll();
            }
        }
    }

    public static void close() {
        synchronized (LOCK) {
            while (current != POOL.size()) {
                try {
                    LOCK.wait();
                } catch (InterruptedException ex) {
                    logger.error(ex.getLocalizedMessage());
                    throw new RuntimeException(ex);
                }
            }
            for (RepositoryConnection connection : POOL) {
                try {
                    connection.close();
                } catch (RepositoryException e) {
                    logger.error(e.getLocalizedMessage());
                }
            }
            POOL.clear();
            current = 0;
        }
    }
}
