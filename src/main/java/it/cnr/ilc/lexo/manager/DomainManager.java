package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.HibernateUtil;
import it.cnr.ilc.lexo.hibernate.entity.SuperEntity;
import java.io.Serializable;
import java.util.Date;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

/**
 *
 * @author andreabellandi
 */
public class DomainManager implements Serializable {

    public Long insert(SuperEntity entity) {
        entity.setTime(new Date());
        entity.setStatus(SuperEntity.Status.VALID);
        entity.setValid(null);
        HibernateUtil.getSession().save(entity);
        return entity.getId();
    }

    public void hiddenUpdate(SuperEntity entity) {
        entity.setStatus(SuperEntity.Status.VALID);
        entity.setValid(null);
        HibernateUtil.getSession().merge(entity);
    }

    public void update(SuperEntity entity) {
        entity.setTime(new Date());
        entity.setStatus(SuperEntity.Status.VALID);
        entity.setValid(null);
        HibernateUtil.getSession().merge(entity);
    }

    public SuperEntity updateWithHistory(SuperEntity entity) {
        HibernateUtil.getSession().evict(entity);
        SuperEntity previous = (SuperEntity) HibernateUtil.getSession().get(entity.persistentClass(), entity.getId());
        HibernateUtil.getSession().evict(previous);
        previous = unproxy(previous);
        previous.setId(null);
        previous.setStatus(SuperEntity.Status.HISTORY);
        previous.setValid(entity);
        HibernateUtil.getSession().save(previous);
        entity.setTime(new Date());
        entity.setStatus(SuperEntity.Status.VALID);
        entity.setValid(null);
        HibernateUtil.getSession().update(entity);
        return previous;
    }

    public void delete(SuperEntity entity) {
        entity.setTime(new Date());
        entity.setStatus(SuperEntity.Status.REMOVED);
        entity.setValid(null);
        HibernateUtil.getSession().merge(entity);
    }

    public SuperEntity deleteWithHistory(SuperEntity entity) {
        HibernateUtil.getSession().evict(entity);
        SuperEntity previous = (SuperEntity) HibernateUtil.getSession().get(entity.persistentClass(), entity.getId());
        HibernateUtil.getSession().evict(previous);
        previous = unproxy(previous);
        previous.setId(null);
        previous.setStatus(SuperEntity.Status.HISTORY);
        previous.setValid(entity);
        HibernateUtil.getSession().save(previous);
        entity.setTime(new Date());
        entity.setStatus(SuperEntity.Status.REMOVED);
        entity.setValid(null);
        HibernateUtil.getSession().update(entity);
        return previous;
    }

    public SuperEntity unproxy(SuperEntity entity) {
        if (entity instanceof HibernateProxy) {
            Hibernate.initialize(entity);
            entity = (SuperEntity) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
        }
        return entity;
    }

}
