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
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 *
 * @author andreabellandi
 */
@WebFilter(urlPatterns = {"/faces/*", "/service/*", "/servlet/*"})
public class LexoFilter implements Filter {

    public static String CONTEXT;
    public static String VERSION;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        CONTEXT = filterConfig.getServletContext().getContextPath().substring(1);
        VERSION = LexoProperties.getProperty("application.version");
        File logFile = new File(filterConfig.getServletContext().getRealPath("/"));
        logFile = new File(logFile.getParentFile().getParentFile(), "logs/" + CONTEXT + ".log");
        PatternLayout layout = new PatternLayout();
        String conversionPattern = "%d %p %m\n";
        layout.setConversionPattern(conversionPattern);
        DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
        rollingAppender.setFile(logFile.getAbsolutePath());
        rollingAppender.setDatePattern("'.'yyyy-MM-dd");
        rollingAppender.setLayout(layout);
        rollingAppender.activateOptions();
        Logger logger = Logger.getLogger(CONTEXT);
        logger.setLevel(Level.INFO);
        logger.addAppender(rollingAppender);
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
        } catch (Throwable ex) {
            Logger.getLogger(CONTEXT).error("", ex);
            ex.printStackTrace();
            try {
                if (GraphDbUtil.getConnection().isActive()) {
                    GraphDbUtil.getConnection().rollback();
                }
            } catch (Exception e) {
            }
            try {
                if (HibernateUtil.getSession().getTransaction().isActive()) {
                    HibernateUtil.getSession().getTransaction().rollback();
                }
            } catch (Exception e) {
            }
        } finally {
            try {
                GraphDbUtil.releaseConnection();
            } catch (Exception e) {
            }
            try {
                HibernateUtil.getSession().close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void destroy() {
        Logger.getLogger(CONTEXT).info(CONTEXT + " stop");
        HibernateUtil.closeFactory();
        GraphDbUtil.close();
    }

}
