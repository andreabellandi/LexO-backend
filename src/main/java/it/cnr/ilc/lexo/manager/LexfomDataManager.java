/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.sparql.SparqlSelectLexfom;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import org.eclipse.rdf4j.query.TupleQueryResult;


/**
 *
 * @author Abdou
 */
public class LexfomDataManager implements Manager, Cached {


    @Override
    public void reloadCache() {

    }

    public  TupleQueryResult getLexicalfunction(String type) throws ManagerException {
       String query = "";
        if (type == null)
        {
            query = SparqlSelectLexfom.GET_ALL_LEXICAL_FUNCTIONS;
        }
        else 
        {
            
            query = SparqlSelectLexfom.GET_FILTER_LEXICAL_FUNCTIONS.replace("[typeLF]", type);
                    
        }
        return RDFQueryUtil.evaluateTQuery(query);    
    }

}
