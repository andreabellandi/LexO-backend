package it.cnr.ilc.lexo;

import static it.cnr.ilc.lexo.LexoFilter.CONTEXT;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;

/**
 *
 * @author andreabellandi
 */
public class GraphDbUtil {

    private static final Object LOCK = new Object();
    private static final Repository REPOSITORY;
    private static final List<RepositoryConnection> POOL = new ArrayList<>();
    private static final Map<Thread, RepositoryConnection> ACTIVES = new HashMap<>();
    private static final int SIZE = Integer.parseInt(LexoProperties.getProperty("GraphDb.size", "5"));
    private static int current = 0;

    static {
        RepositoryManager repositoryManager = new RemoteRepositoryManager(LexoProperties.getProperty("GraphDb.url", "http://localhost:7200"));
        repositoryManager.init();
        REPOSITORY = repositoryManager.getRepository(LexoProperties.getProperty("GraphDb.repository", "SIMPLE"));
    }

    private static void testConnection(RepositoryConnection connection) throws Exception {
    }

    public static RepositoryConnection getConnection() {
        synchronized (LOCK) {
            RepositoryConnection connection = ACTIVES.get(Thread.currentThread());
            if (connection != null) {
                Logger.getLogger(CONTEXT).debug(Thread.currentThread().getName() + " get active connection");
                return connection;
            }
            try {
                connection = POOL.remove(0);
                try {
                    testConnection(connection);
                    Logger.getLogger(CONTEXT).debug(Thread.currentThread().getName() + " get pool connection");
                } catch (Exception ex) {
                    Logger.getLogger(CONTEXT).debug(Thread.currentThread().getName() + " get inactive connection");
                    try {
                        connection.close();
                    } catch (RepositoryException e) {
                    }
                    current--;
                    connection = getConnection();
                }
            } catch (IndexOutOfBoundsException ex) {
                if (current < SIZE) {
                    Logger.getLogger(CONTEXT).debug(Thread.currentThread().getName() + " get repository connection");
                    connection = REPOSITORY.getConnection();
                    current++;
                } else {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException iex) {
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
                    throw new RuntimeException(ex);
                }
            }
            for (RepositoryConnection connection : POOL) {
                try {
                    connection.close();
                } catch (RepositoryException e) {
                }
            }
            POOL.clear();
            current = 0;
        }
    }
}
