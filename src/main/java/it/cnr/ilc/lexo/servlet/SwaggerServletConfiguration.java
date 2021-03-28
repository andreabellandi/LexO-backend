/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.servlet;

import io.swagger.jaxrs.config.BeanConfig;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author andreabellandi
 */

public class SwaggerServletConfiguration extends HttpServlet {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setBasePath("lexo-server/service");
        beanConfig.setHost("localhost:8080");
        beanConfig.setTitle("LexO server API documentation");
        beanConfig.setResourcePackage("it.cnr.ilc.lexo");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
        beanConfig.setSchemes(new String[] {"http"});
        beanConfig.setVersion("0.1");
    }
    
}
