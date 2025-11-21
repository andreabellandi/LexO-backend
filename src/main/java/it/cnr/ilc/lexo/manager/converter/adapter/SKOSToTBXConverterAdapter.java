/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager.converter.adapter;

import it.cnr.ilc.lexo.manager.converter.implementation.OntoLexToTBXConverter;
import it.cnr.ilc.lexo.util.RepositoryRegistry;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import org.eclipse.rdf4j.repository.Repository;

/**
 *
 * @author andreabellandi
 */
public class SKOSToTBXConverterAdapter implements Converter {

    @Override
    public String sourceType() {
        return "skos";
    }

    @Override
    public String targetType() {
        return "tbx";
    }

    @Override
    public void validateOptions(Map<String, String> options) {
    }

    @Override
    public String outputExtension(Map<String, String> options) {
        return ".tbx";
    }
    
    @Override
    public void convert(String fileId, Path input, Path output, Map<String, String> options,
            IntConsumer onProgress, LongConsumer onProcessed, BooleanSupplier shouldCancel) throws Exception {
        Repository repo = RepositoryRegistry.get(fileId);
        if (repo == null) {
            throw new IllegalStateException("No repository for fileId: " + fileId);
        }
        OntoLexToTBXConverter.Result res = OntoLexToTBXConverter.convert(repo, input, output, options, onProgress, onProcessed, shouldCancel
        );
    }
}
