/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager.converter;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/**
 *
 * @author andreabellandi
 */
public interface Converter {
    /** Identifica il tipo di input supportato, es. "rdf", "tbx", "csv" */
    String sourceType();

    /** Identifica il tipo di output prodotto, es. "tbx", "rdf", "csv" */
    String targetType();

    /**
     * Esegue la conversione. Le opzioni sono parametri liberi (es. "format" per RDF, "delimiter" per CSV).
     * onProgress: 0..100 (il JobManager terrà 0..99 fino al completamento)
     * onProcessed: contatore record/statement ecc.
     * shouldCancel: ritorna true se il job è stato cancellato/interrotto
     */
    void convert(String fileId,
                 Path input,
                 Path output,
                 Map<String, String> options,
                 IntConsumer onProgress,
                 LongConsumer onProcessed,
                 BooleanSupplier shouldCancel) throws Exception;

    /** Suggerisce estensione output (es. ".tbx", ".ttl", ".csv") */
    String outputExtension(Map<String,String> options);

    /** Facoltativo: valida opzioni richieste, lancia IllegalArgumentException se mancano */
    default void validateOptions(Map<String,String> options) {}
}
