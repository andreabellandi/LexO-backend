package it.cnr.ilc.lexo.service.conll.core.impl;

import it.cnr.ilc.lexo.service.conll.core.Bundle;
import it.cnr.ilc.lexo.service.conll.core.Document;
import it.cnr.ilc.lexo.service.conll.core.Sentence;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Implementation of Bundle.
 *
 * Bundle has reference to document and serves the purpose of container for sentence trees.
 *
 * @author Martin Vojtek
 */
public class DefaultBundle implements Bundle {

    private List<Sentence> trees = new ArrayList<>();
    private Document document;
    private String id;
    int index = -1;

    public DefaultBundle(Document document) {
        this.document = document;
    }

    public void addTree(Sentence root) {
        root.setBundle(this);
        trees.add(root);
    }

    @Override
    public Sentence createTree() {
        Sentence tree = (Sentence) new DefaultSentence(document, this);
        trees.add(tree);
        return tree;
    }

    @Override
    public List<Sentence> getSentences() {
        return trees;
    }

    @Override
    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public Document getDocument() {
        return document;
    }

    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Optional<Sentence> getTree(String zone) {
        return trees.stream().filter(tree -> tree.getZone().equals(zone)).findFirst();
    }

    @Override
    public void remove() {
        document.getBundles().remove(this);
        document = null;
    }

    @Override
    public int getNumber() {
        return index;
    }

    /**
     *
     * @return bundle id or '?' if missing
     */
    @Override
    public String getAddress() {
        return id != null ? id : "?";
    }
}
