package it.cnr.ilc.lexo.service.conll;

import it.cnr.ilc.lexo.service.conll.core.Bundle;
import it.cnr.ilc.lexo.service.conll.core.impl.DefaultDocument;
import it.cnr.ilc.lexo.service.conll.core.Document;
import it.cnr.ilc.lexo.service.conll.core.EmptyNode;
import it.cnr.ilc.lexo.service.conll.core.EnhancedDeps;
import it.cnr.ilc.lexo.service.conll.core.Sentence;
import it.cnr.ilc.lexo.service.conll.core.Token;
import it.cnr.ilc.lexo.service.conll.core.impl.DefaultEmptyNode;
import it.cnr.ilc.lexo.service.conll.core.impl.DefaultEnhancedDeps;
import it.cnr.ilc.lexo.service.conll.core.impl.DefaultSentence;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reader of files in CoNLLU format.
 *
 * @author Martin Vojtek
 */
public class CoNLLUReader implements DocumentReader {

    private final Reader reader;
    private static final Pattern idRangePattern = Pattern.compile("(\\d+)-(\\d+)");
    private static final String EMPTY_STRING = "";
    private static final String TAB = "\\t";
    private static final String DASH = "-";
    private static final String DOT = ".";
    private static final String NEWPAR = "newpar";
    private static final char HASH = '#';
    private static final Pattern tabPattern = Pattern.compile(TAB);
    private static final Pattern sentIdPattern = Pattern.compile("^# sent_id\\s*=?\\s*(\\S+)");
    private static final Pattern textPattern = Pattern.compile("^# text\\s*=\\s*(.+)");
    private static final Pattern newParDocPattern = Pattern.compile("^# ("+NEWPAR+"|newdoc) (?:\\s*id\\s*=\\s*(.+))?");

    public CoNLLUReader(Reader reader) {
        this.reader = reader;
    }

    public CoNLLUReader(String inCoNLL) throws ConllException {
        try {
            reader = new FileReader(Paths.get(inCoNLL).toFile());
        } catch (FileNotFoundException e) {
            throw new ConllException("Provided CoNLL file '" + inCoNLL + "' not found.");
        }
    }

    public CoNLLUReader(Path inCoNLL) throws ConllException {
        try {
            reader = new FileReader(inCoNLL.toFile());
        } catch (FileNotFoundException e) {
            throw new ConllException("Provided CoNLL file '" + inCoNLL + "' not found.");
        }
    }

    public CoNLLUReader(File inCoNLL) throws ConllException {
        try {
            reader = new FileReader(inCoNLL);
        } catch (FileNotFoundException e) {
            throw new ConllException("Provided CoNLL file '" + inCoNLL.getAbsolutePath() + "' not found.");
        }
    }

    @Override
    public Document readDocument() throws ConllException {
        final Document document = new DefaultDocument();
        readInDocument(document);

        return document;
    }

    /**
     * Reads CoNLLU file into given document.
     *
     * @param document document to read into
     * @throws ConllException If any IOException happens
     */
    @Override
    public void readInDocument(final Document document) throws ConllException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        int sentenceId = 1;

        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String currLine;
            List<String> words = new ArrayList<>();

            while ((currLine = bufferedReader.readLine()) != null) {
                String trimLine = currLine.trim();
                if (EMPTY_STRING.equals(trimLine)) {
                    //end of sentence
                    List<String> finalWords = words;
                    final int finalSentenceId = sentenceId++;
                    executor.submit(() -> processSentenceWithBundle(finalSentenceId, document, finalWords));
                    words = new ArrayList<>();
                } else {
                    words.add(trimLine);
                }
            }
            //process last sentence if there was no empty line after it
            List<String> finalWords = words;
            final int finalSentenceId = sentenceId;
            executor.submit(() -> processSentenceWithBundle(finalSentenceId, document, finalWords));
        } catch (IOException e) {
            throw new ConllException(e);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("Wait for executor termination interrupted.");
        }
    }

    /**
     * Reads tree into document and returns it.
     * Uses BufferedReader owned by external class.
     *
     * Close of the reader is responsibility of caller.
     *
     * @param bufferedReader reader to use
     * @param document document to read into
     * @return parsed tree
     * @throws ConllException If any IOException happens
     */
    @Override
    public Optional<Sentence> readTree(BufferedReader bufferedReader, final Document document) throws ConllException {
        try {
            String currLine;
            List<String> words = new ArrayList<>();

            while ((currLine = bufferedReader.readLine()) != null) {
                String trimLine = currLine.trim();
                if (EMPTY_STRING.equals(trimLine)) {
                    //end of sentence
                    List<String> finalWords = words;
                    Sentence root = processSentence(document, finalWords);
                    if (null != root) {
                        return Optional.of(root);
                    }
                    words = new ArrayList<>();
                } else {
                    words.add(trimLine);
                }
            }
            //process last sentence if there was no empty line after it
            List<String> finalWords = words;
            Sentence root = processSentence(document, finalWords);
            if (null == root) {
                return Optional.empty();
            }
            return Optional.of(root);
        } catch (IOException e) {
            throw new ConllException(e);
        }
    }

    /**
     * Reads tree into document and returns it.
     *
     * @param document document to load into
     * @return tree of the sentence
     * @throws ConllException If any IOException happens
     */
    @Override
    public Optional<Sentence> readTree(final Document document) throws ConllException {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            return readTree(bufferedReader, document);
        } catch (IOException e) {
            throw new ConllException(e);
        }
    }

    private void processSentenceWithBundle(int sentenceId, final Document document, List<String> words) {

        Sentence tree = processSentence(document, words);

        String treeId = tree.getId();
        //add tree to correct bundle
        // Based on treeId the tree is added either to the last existing bundle or to a new bundle.
        // treeId should contain bundleId/zone.
        // The "/zone" part is optional. If missing, zone='und' is used for the new tree.
        if (null == treeId) {
            Bundle newBundle = document.createBundle();
            newBundle.addTree(tree);
            newBundle.setId(String.valueOf(sentenceId));
        } else {
            String bundleId;
            int slashIndex = treeId.indexOf("/");

            if (-1 != slashIndex) {
                bundleId = treeId.substring(0, slashIndex);
                if (slashIndex < treeId.length() - 1) {
                    tree.setZone(treeId.substring(slashIndex + 1));
                    tree.validateZone();
                }
            } else {
                bundleId = treeId;
            }

            if (document.getBundles().isEmpty()) {
                Bundle newBundle = document.createBundle();
                newBundle.setId(bundleId);
                newBundle.addTree(tree);
            } else {
                Bundle lastBundle = document.getBundles().get(document.getBundles().size() - 1);
                if (null != bundleId && !bundleId.equals(lastBundle)) {
                    Bundle newBundle = document.createBundle();
                    newBundle.setId(bundleId);
                    newBundle.addTree(tree);
                } else {
                    lastBundle.addTree(tree);
                }
            }
        }
        tree.setId(null);
    }

    /**
     * Processes sentence.
     *
     * @param document document to load into
     * @param words words of the sentence
     * @return constructed tree
     */
    private Sentence processSentence(final Document document, List<String> words) {

        //ignore empty sentences
        if (words.isEmpty()) {
            return null;
        }

        Sentence tree = (Sentence) new DefaultSentence(document);

        Token root = tree.getToken();

        List<Token> nodes = new ArrayList<>();
        List<EmptyNode> emptyNodes = new ArrayList<>();
        nodes.add(root);
        List<Integer> parents = new ArrayList<>();
        parents.add(0);

        List<MwtStruct> mwtStructs = new ArrayList<>();

        for (String word : words) {
            if (word.charAt(0) == HASH) {

                boolean processedComment = false;
                //process comment
                Matcher sentIdMatcher = sentIdPattern.matcher(word);
                if (sentIdMatcher.matches()) {
                    tree.setSentId(sentIdMatcher.group(1));
                    processedComment = true;
                } else {

                    Matcher textMatcher = textPattern.matcher(word);
                    if (textMatcher.matches()) {
                        tree.setText(textMatcher.group(1));
                    } else {

                        Matcher newParDocMatcher = newParDocPattern.matcher(word);
                        if (newParDocMatcher.matches()) {
                            processedComment = true;
                            if (newParDocMatcher.group(1).equals(NEWPAR)) {
                                tree.setIsNewPar(true);
                                if (newParDocMatcher.groupCount() > 1) {
                                    tree.setNewParId(newParDocMatcher.group(2));
                                }
                            } else {
                                tree.setIsNewDoc(true);
                                if (newParDocMatcher.groupCount() > 1) {
                                    tree.setNewDocId(newParDocMatcher.group(2));
                                }
                            }
                        }
                    }

                }

                //comment
                if (word.length() > 1) {
                    if (!processedComment) {
                        tree.addComment(word.substring(1));
                    }
                } else {
                    tree.addComment(EMPTY_STRING);
                }
            } else {
                //process word
                processWord(tree, root, nodes, parents, emptyNodes, mwtStructs, word);
            }
        }

        //process multiwords
        mwtStructs.forEach(m -> {
            List<Token> wordsList = nodes.subList(m.rangeStart, m.rangeEnd+1);
            tree.addMultiword(wordsList, m.form, m.misc);

        });

        //add empty nodes to the tree
        tree.setEmptyNodes(emptyNodes);

        //set correct parents
        for (int i = 1; i < nodes.size(); i++) {
            nodes.get(i).setParent(nodes.get(parents.get(i)));
        }

        return tree;
    }

    /**
     * Processes word.
     */
    private void processWord(Sentence tree, Token root, List<Token> nodes, List<Integer> parents, List<EmptyNode> emptyNodes, List<MwtStruct> mwtStructs, String word) {

        String[] fields = tabPattern.split(word, 10);
        String id = fields[0];
        String form = fields[1];
        String lemma = fields[2];
        String upos = fields[3];
        String xpos = fields[4];
        String feats = fields[5];
        String head = fields[6];
        String deprel = fields[7];
        String deps = fields[8];
        String misc = null;
        if (10 == fields.length) {
            misc = fields[9];
        }

        if (id.contains(DASH)) {
            Matcher m = idRangePattern.matcher(id);
            if (m.matches()) {

                MwtStruct mwtStruct = new MwtStruct();
                mwtStruct.form = form;
                mwtStruct.misc = misc;
                mwtStruct.rangeStart = Integer.parseInt(m.group(1));
                mwtStruct.rangeEnd = Integer.parseInt(m.group(2));

                mwtStructs.add(mwtStruct);
            }
        } else if (id.contains(DOT)) {
            //empty node

            EmptyNode newEmptyNode = new DefaultEmptyNode(tree);
            newEmptyNode.setForm(form);
            newEmptyNode.setLemma(lemma);
            newEmptyNode.setUpos(upos);
            newEmptyNode.setXpos(xpos);
            newEmptyNode.setFeats(feats);
            newEmptyNode.setHead(head);
            newEmptyNode.setDeprel(deprel);
            newEmptyNode.setDeps((EnhancedDeps) new DefaultEnhancedDeps(deps, tree));
            newEmptyNode.setMisc(misc);
            newEmptyNode.setEmptyNodeId(id);

            emptyNodes.add(newEmptyNode);
        } else {
            Token child = root.createChild();
            child.setForm(form);
            child.setLemma(lemma);
            child.setUpos(upos);
            child.setXpos(xpos);
            child.setFeats(feats);
            child.setHead(head);
            child.setDeprel(deprel);
            child.setDeps((EnhancedDeps) new DefaultEnhancedDeps(deps, tree));
            child.setMisc(misc);

            nodes.add(child);
            parents.add(Integer.parseInt(head));
        }
    }

    private static class MwtStruct {
        int rangeStart;
        int rangeEnd;
        String form;
        String misc;
    }
}
