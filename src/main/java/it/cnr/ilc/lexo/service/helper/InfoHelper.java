package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.HibernateUtil;
import it.cnr.ilc.lexo.LexOFilter;
import it.cnr.ilc.lexo.service.data.Info;
import java.util.Date;
import org.hibernate.HibernateException;

/**
 *
 * @author andreabellandi
 */
public class InfoHelper extends Helper<Info> {

    @Override
    public Class<Info> getDataClass() {
        return Info.class;
    }

    public Info newData() {
        Info data = new Info();
        data.setName(LexOFilter.CONTEXT);
        data.setVersion(LexOFilter.VERSION);
        Date date = null;
        try {
            date = (Date) HibernateUtil.getSession().createSQLQuery("select now()").uniqueResult();
        } catch (HibernateException e) {
            date = new Date();
        }
        data.setServerTime(date);
        data.setJavaVersion(System.getProperty("java.version"));
        return data;
    }

}
