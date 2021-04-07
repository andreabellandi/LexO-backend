/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.servlet;

import io.swagger.jaxrs.config.BeanConfig;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author andreabellandi
 */
public class SwaggerServletConfiguration extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        Logger logger = LoggerFactory.getLogger(SwaggerServletConfiguration.class);
        super.init(config);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setBasePath(config.getServletContext().getContextPath() + "/service");
        logger.info("init(): config.getServletContext().getContextPath(): [ " + config.getServletContext().getContextPath() + "/service ]");
        beanConfig.setTitle("LexO server API documentation");
        beanConfig.setResourcePackage("it.cnr.ilc.lexo");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
        beanConfig.setSchemes(new String[]{"https"});
        beanConfig.setVersion("0.1");
    }

}
