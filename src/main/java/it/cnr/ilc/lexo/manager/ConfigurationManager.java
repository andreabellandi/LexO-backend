/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.ilc.lexo.HibernateUtil;
import it.cnr.ilc.lexo.hibernate.entity.ConfigurationParameter;
import it.cnr.ilc.lexo.service.data.lexicon.input.Config;
import it.cnr.ilc.lexo.service.data.lexicon.input.ConfigUpdater;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

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

    public ConfigurationParameter loadConfigParameter(String key) {
        Criteria criteria = HibernateUtil.getSession().createCriteria(ConfigurationParameter.class);
        criteria.add(Restrictions.eq("key", key));
        List<ConfigurationParameter> list = criteria.list();
        return list.isEmpty() ? null : list.get(0);
    }

    public Date updateConfiguration(ConfigUpdater cu) throws ManagerException {
        ConfigurationParameter cp = loadConfigParameter(cu.getParam());
        if (cp == null) {
            throw new ManagerException(cu.getParam() + " is not a valid configuration parameter");
        } else {
            ConfigurationParameter _cp = new ConfigurationParameter(cu.getParam(), cu.getValue());
            domainManager.update(_cp);
            return _cp.getTime();
        }
    }

    public List<Config> getConfigurations() {
        // TODO
        return null;
    }

    public void initConfigurations(String json) throws ManagerException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode config = getJsonNode(mapper, json);
        if (config != null) {
            
        } else {
            throw new ManagerException("Input Json syntax error");
        }
    }
    
    private JsonNode getJsonNode(ObjectMapper mapper, String json) {
        try {
            return mapper.readTree(json);
        } catch (JacksonException e) {
            return null;
        }
    }

}
