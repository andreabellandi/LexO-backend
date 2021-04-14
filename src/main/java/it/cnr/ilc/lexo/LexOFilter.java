package it.cnr.ilc.lexo;

import java.io.File;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
@WebFilter(urlPatterns = {"/faces/*", "/service/*", "/servlet/*"})
public class LexOFilter implements Filter {

    static final Logger logger = LoggerFactory.getLogger(LexOFilter.class.getName());
    public static String CONTEXT;
    public static String VERSION;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        CONTEXT = filterConfig.getServletContext().getContextPath().substring(1);
        VERSION = LexOProperties.getProperty("application.version");
        File logFile = new File(filterConfig.getServletContext().getRealPath("/"));
        logger.info(CONTEXT + " start");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            HibernateUtil.getSession().beginTransaction();
            HibernateUtil.getSession().enableFilter("status");
            GraphDbUtil.getConnection().begin();
            chain.doFilter(request, response);
            if (GraphDbUtil.getConnection().isActive()) {
                GraphDbUtil.getConnection().commit();
            }
            if (HibernateUtil.getSession().getTransaction().isActive()) {
                HibernateUtil.getSession().getTransaction().commit();
            }
        } catch (Exception ex) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            logger.error("Type of Exception: " + ex.getClass());

            if (ex instanceof org.hibernate.exception.GenericJDBCException) {
                logger.error("doFilter() Error connecting MySQL", ex);
                httpResponse.addHeader("Error", "Unable to connect to MySQL");
                httpResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Unable to connect to MySQL");
            } else {
                httpResponse.addHeader("Error", "Unable to connect to ???");
                logger.error("doFilter()", ex);
                httpResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Unable to connect to ???");
            }
            try {
                if (GraphDbUtil.getConnection().isActive()) {
                    GraphDbUtil.getConnection().rollback();
                }
            } catch (Exception e) {
                logger.error("doFilter(), Unable to connect to GraphDB", e);
                httpResponse.addHeader("Error", "Unable to connect to GraphDB");
                httpResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Unable to connect to GraphDB");
            }
            try {
                if (HibernateUtil.getSession().getTransaction().isActive()) {
                    HibernateUtil.getSession().getTransaction().rollback();
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }

        } finally {
            try {
                GraphDbUtil.releaseConnection();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
            try {
                HibernateUtil.getSession().close();
            } catch (HibernateException e) {
                logger.error(e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void destroy() {
        // Logger.getLogger(CONTEXT).info(CONTEXT + " stop");
        logger.info(CONTEXT + " stop");
        HibernateUtil.closeFactory();
        GraphDbUtil.close();
    }

}
