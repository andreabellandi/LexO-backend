/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager.converter;

import it.cnr.ilc.lexo.manager.Manager;
import it.cnr.ilc.lexo.util.ParserUtil;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.RDF;

/**
 *
 * @author andreabellandi
 */
public class RemoteRepositoryManager implements Manager {

    private void parseRDF(Path file, Model m) throws Exception {
        RDFFormat fmt = Rio.getParserFormatForFileName(file.getFileName().toString())
                .orElse(RDFFormat.TURTLE);
        try ( InputStream is = Files.newInputStream(file)) {
            RDFParser parser = Rio.createParser(fmt);
            parser.setRDFHandler(new StatementCollector(m));           // colleziona gli statement nel Model
            parser.parse(is, file.toUri().toString());                 // baseURI = path del file
        }
    }

    private Collection<Resource> resourcesOfType(Model m, IRI type) {
        return m.filter(null, RDF.TYPE, type).subjects();
    }

    public Map<String, Object> getInfo(Path src) throws Exception {
        Model m = new TreeModel();
        parseRDF(src, m);
        Map<String, Object> info = new LinkedHashMap();
        int lexicalEntries = resourcesOfType(m, ParserUtil.LEXICAL_ENTRY).size();
        int lexicalSenses = resourcesOfType(m, ParserUtil.LEXICAL_SENSE).size();
        info.put("Lexical entries", lexicalEntries);
        info.put("Words", resourcesOfType(m, ParserUtil.WORD).size());
        info.put("Multiwords", resourcesOfType(m, ParserUtil.MULTIWORD).size());
        info.put("Forms", resourcesOfType(m, ParserUtil.FORM).size());
        info.put("Lexical Senses", lexicalSenses);
        return info;
    }

}
