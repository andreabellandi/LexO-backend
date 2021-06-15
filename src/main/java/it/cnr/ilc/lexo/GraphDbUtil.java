package it.cnr.ilc.lexo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.config.RepositoryConfigException;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class GraphDbUtil {

    private static final Object LOCK = new Object();
    private static final RepositoryManager repositoryManager;
    private static final Repository repository;

    private static final List<RepositoryConnection> POOL = new ArrayList<>();
    //private static final Map<Thread, RepositoryConnection> availableConnection = new HashMap<>();
    private static final int POOLSIZE = Integer.parseInt(LexOProperties.getProperty("GraphDb.size", "5"));
    private static final Logger logger = LoggerFactory.getLogger(GraphDbUtil.class);

    static {
        try {
            //HTTPRepository httprepo = new HTTPRepository(LexOProperties.getProperty("GraphDb.url", "http://localhost:7200"));
            repositoryManager = new RemoteRepositoryManager(LexOProperties.getProperty("GraphDb.url", "http://localhost:7200"));
            repositoryManager.init();
            repository = repositoryManager.getRepository(LexOProperties.getProperty("GraphDb.repository", "SIMPLE"));
            for (int i = 0; i < POOLSIZE; i++) {
                POOL.add(repository.getConnection());
            }
        } catch (RepositoryException | RepositoryConfigException e) {
            logger.error("Unable to connect to GraphDB: " + e);
            throw new RepositoryException("Unable to connect to GraphDB: " + LexOProperties.getProperty("GraphDb.url", "http://localhost:7200"));
        }
    }

    private static  boolean testRDFServerHTTPConnection() {
        logger.info("testRDFServerHTTPConnection() start");
        boolean res = true;
        HttpGet request = new HttpGet(LexOProperties.getProperty("GraphDb.url", "http://localhost:7200"));
        logger.debug("testRDFServerHTTPConnection() request: " + request);
        int timeout = 5;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
            logger.debug("testRDFServerHTTPConnection() before execute: " + request);
            CloseableHttpResponse response = httpClient.execute(request);
            logger.debug("testRDFServerHTTPConnection() after execute: " + request);

            // Get HttpResponse Status
            logger.debug(response.getProtocolVersion().toString());              // HTTP/1.1
            logger.debug("" + response.getStatusLine().getStatusCode());   // 200
            logger.debug(response.getStatusLine().getReasonPhrase()); // OK
            logger.debug(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.info("GraphDB Response is " + response.getStatusLine().getStatusCode());
            }

        } catch (IOException e) {
            logger.error("", e);
            res = false;
        }

        return res;
    }

    public static RepositoryConnection getConnection() throws RepositoryException { 
        logger.info("getConnection() " + Thread.currentThread().getName());
        synchronized (LOCK) {
            RepositoryConnection connection = null;
            while (POOL.isEmpty()) {
                try {
                    LOCK.wait();
                    logger.info("After wait() POOL.size(): " + POOL.size());
                } catch (InterruptedException e) {
                    logger.error("In wait()", e);
                }
            }
            connection = POOL.remove(0);
            logger.info("connection is: " + connection + " POOL.size(): " + POOL.size());
            if (connection != null) {
                if (!testRDFServerHTTPConnection()) {
                    POOL.add(connection);
                    logger.info("RepositoryException, POOL.size(): " + POOL.size());
                    LOCK.notifyAll();
                    throw new RepositoryException("Repository is unreachble");
                }
//                logger.debug("connection is active? " + connection.isActive());
//                logger.debug("connection is empty? " + connection.isEmpty());
                logger.info("connection is open? " + connection.isOpen());
//                logger.debug(Thread.currentThread().getName() + " get active connection");
            }
            LOCK.notifyAll();
            logger.info("getConnection() " + Thread.currentThread().getName() + " connection: " + connection);

            return connection;
        }
    }

    public static void releaseConnection(RepositoryConnection connection) {
        logger.info("releaseConnection() " + Thread.currentThread().getName());
        synchronized (LOCK) {
            if (connection != null) {
                if (connection.isActive()) {
                    connection.rollback();
                }
                POOL.add(connection);
                logger.info("releaseConnection, POOL.size(): " + POOL.size());
                LOCK.notifyAll();
            }
        }
    }

    public static void shutDown() {
        logger.info("shutDown() " + Thread.currentThread().getName());
        synchronized (LOCK) {
            for (RepositoryConnection connection : POOL) {
                try {
                    if (connection != null) {
                        if (connection.isActive()) {
                            connection.rollback();
                        }
                        connection.close();
                    }
                } catch (RepositoryException e) {
                    logger.error(e.getLocalizedMessage());
                }
            }
            POOL.clear();
            if (repository != null) {
                repository.shutDown();
            } else {
                logger.warn("repository is null!");
            }
            LOCK.notifyAll();
        }
    }
}
