/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.HibernateUtil;
import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.hibernate.entity.ConfigurationParameter;
import it.cnr.ilc.lexo.service.data.lexicon.input.Config;
import it.cnr.ilc.lexo.service.data.lexicon.input.ConfigUpdater;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;

/**
 *
 * @author andreabellandi
 */
public class ConfigurationManager implements Manager, Cached {

    private final DomainManager domainManager = ManagerFactory.getDomainManager();

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<ConfigurationParameter> loadConfigurations() {
        return HibernateUtil.getSession().createCriteria(ConfigurationParameter.class).list();
    }

    public Date updateConfiguration(ConfigUpdater cu) throws ManagerException {
        ConfigurationParameter cp = new ConfigurationParameter();
        cp.setKey(cu.getParam());
        cp.setValue(cu.getValue());
        try {
            domainManager.update(cp);
        } catch (HibernateException ex) {
            throw new ManagerException("something was wrong: " + ex.getMessage());
        }
        return cp.getTime();
    }

}
