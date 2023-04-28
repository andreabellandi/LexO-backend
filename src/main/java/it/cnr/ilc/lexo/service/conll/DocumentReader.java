package it.cnr.ilc.lexo.service.conll;

import it.cnr.ilc.lexo.service.conll.core.Document;
import it.cnr.ilc.lexo.service.conll.core.Sentence;
import java.io.BufferedReader;
import java.util.Optional;


/**
 * Used for reading document from different formats.
 *
 * @author Martin Vojtek
 */
public interface DocumentReader {
    /**
     * Reads bundles into document.
     *
     * @return Document document containing some bundles
     * @throws ConllException If any error occurs
     */
    Document readDocument() throws ConllException;

    /**
     * Reads bundles into Document.
     *
     * @param document document to read into
     * @throws ConllException If any error occurs
     */
    void readInDocument(Document document) throws ConllException;

    /**
     * Reads tree into document.
     *
     * @param document document to read into
     * @return tree of the sentence
     * @throws ConllException If any error occurs
     */
    Optional<Sentence> readTree(final Document document) throws ConllException;

    /**
     * Reads tree into document with given reader.
     *
     * @param bufferedReader reader encapsulating input to process
     * @param document document to read into
     * @return tree of the sentence
     * @throws ConllException If any error occurs
     */
    Optional<Sentence> readTree(BufferedReader bufferedReader, final Document document) throws ConllException;
}
