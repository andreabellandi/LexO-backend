/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.conll.SPARQLWriter;
import it.cnr.ilc.lexo.service.data.administration.output.SystemInfo;
import it.cnr.ilc.lexo.service.data.administration.output.SystemInfo.Repository;
import it.cnr.ilc.lexo.service.data.lexicon.output.ImageDetail;
import it.cnr.ilc.lexo.service.data.lexicon.output.ImportDetail;
import it.cnr.ilc.lexo.service.data.lexicon.output.Property;
import it.cnr.ilc.lexo.sparql.SparqlInsertData;
import it.cnr.ilc.lexo.sparql.SparqlQueryUtil;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;

/**
 *
 * @author andreabellandi
 */
public class AdministrationManager implements Manager, Cached {

    private final String idInstancePrefix = LexOProperties.getProperty("repository.instance.id");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));

    @Override
    public void reloadCache() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public SystemInfo getSystemInfo() throws ManagerException {
        SystemInfo si = new SystemInfo();
        String query = SparqlQueryUtil.SYSTEM_INFO;
        GraphQueryResult gqr = RDFQueryUtil.evaluateGQuery(query);
        Iterator<Statement> it = gqr.iterator();
        while (it.hasNext()) {
            Statement st = it.next();
            if (st.getSubject().stringValue().equals("http://www.ontotext.com/SI_has_StorageSizeOnDisk")) si.setStorageSizeOnDisk(st.getObject().stringValue());
            if (st.getSubject().stringValue().equals("http://www.ontotext.com/SI_has_FreeDiskSpace")) si.setFreeDiskSpace(st.getObject().stringValue());
            if (st.getSubject().stringValue().equals("http://www.ontotext.com/SI_has_Revision")) si.setGraphDBVersion(st.getObject().stringValue());
        }
        si.setLexOVersion(LexOProperties.getProperty("application.version"));
        si.setGraphDBUrl(LexOProperties.getProperty("GraphDb.url"));
        ArrayList<Repository> al = new ArrayList();
        SystemInfo.Repository repo = si.new Repository();
        repo.setName(LexOProperties.getProperty("GraphDb.repository"));
        repo.setNamespace(LexOProperties.getProperty("repository.lexicon.namespace"));      
        repo.setType(SystemInfo.repoType.LEXICON.name());
        al.add(repo);
        si.setAttachedRepositories(al);
        return si;
    }
   
}
