/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.lexicon.output.Counting;
import it.cnr.ilc.lexo.service.data.lexicon.output.RelationPath;
import it.cnr.ilc.lexo.sparql.SparqlVariable;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class PathLenghtHelper extends TripleStoreDataHelper<RelationPath> {

    @Override
    public Class<RelationPath> getDataClass() {
        return RelationPath.class;
    }

    @Override
    public void fillData(RelationPath data, BindingSet bs) {
        data.setLenght(getIntegerNumber(bs, SparqlVariable.LENGHT));
        data.setLexicalEntry(getStringValue(bs, SparqlVariable.LEXICAL_ENTRY));
        data.setLexicalSense(getStringValue(bs, SparqlVariable.IRI));
    }
    
    
}
