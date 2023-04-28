package it.cnr.ilc.lexo.service.conll.core;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * Node represents token (word) of the sentence.
 *
 * @author Martin Vojtek
 */
public interface Token {

    /**
     * Args used for child removal.
     */
    enum RemoveArg {
        REHANG /* rehangs children to parent of the removed parent */,
        WARN
    }

    /**
     * Args used for children retrieval.
     */
    enum ChildrenArg {
        ADD_SELF /* result will include also self node */,
        FIRST_ONLY /* result will include only the first child */,
        LAST_ONLY /* result will include only the last child */
    }

    /**
     * Args used for descendants retrieval.
     */
    enum DescendantsArg {
        ADD_SELF /* result will include also self node */,
        FIRST_ONLY /* result will include only the first child */,
        LAST_ONLY /* result will include only the last child */
    }

    enum ShiftArg {
        WITHOUT_CHILDREN /* shift node without its children */,
        SKIP_IF_DESCENDANT /* skip shifting if given node is descendant of the node to shift after/before */
    }

    /**
     * Remove node from the tree. Only non-root nodes can be removed.
     */
    void remove();

    /**
     * Creates new child of the given node and returns it.
     *
     * @return new child
     */
    Token createChild();

    /**
     * Returns children of the node in word order.
     *
     * @return children of the node
     */
    List<Token> getChildren();

    /**
     * Returns children of the node in word order.
     *
     * @param args args to augment resulting collection
     * @return children of the node
     */
    List<Token> getChildren(EnumSet<ChildrenArg> args);

    /**
     * Returns parent node.
     *
     * @return parent node
     */
    Optional<Token> getParent();

    /**
     * Sets parent.
     *
     * @param node new parent node
     */
    void setParent(Token node);

    /**
     * Sets parent.
     *
     * @param node new parent node
     * @param skipCycles skip operation in case of cycles
     */
    void setParent(Token node, boolean skipCycles);

    /**
     *
     * @return true if the node is technical root
     */
    boolean isRoot();

    /**
     *
     * @return descendants of the node in word order
     */
    List<Token> getTokens();

    /**
     *
     * @param args args to augment resulting collection
     * @return descendants of the node in word order
     */
    List<Token> getTokens(EnumSet<DescendantsArg> args);

    /**
     *
     * @param args args to augment resulting collection
     * @param except the resulting collection without this node
     * @return descendants of the node in word order
     */
    List<Token> getTokens(EnumSet<DescendantsArg> args, Token except);

    /**
     *
     * @return siblings of the node
     */
    List<Token> getSiblings();

    /**
     *
     * @return previous sibling
     */
    Optional<Token> getPrevSibling();

    /**
     *
     * @return next sibling
     */
    Optional<Token> getNextSibling();

    /**
     *
     * @param newNextSibling set new next sibling
     */
    void setNextSibling(Optional<Token> newNextSibling);

    /**
     *
     * @return previous node in word order
     */
    Optional<Token> getPrevNode();

    /**
     *
     * @return next node in word order
     */
    Optional<Token> getNextNode();

    /**
     *
     * @param node node we want to find out if there is descendat relation
     * @return true if this is descendant of given node
     */
    boolean isDescendantOf(Token node);

    /**
     *
     * @return ID of the node
     */
    int getId();

    /**
     *
     * @return form of the node
     */
    String getForm();

    /**
     * Sets form of the node.
     *
     * @param form new form of the node
     */
    void setForm(String form);

    /**
     *
     * @return lemma of the node
     */
    String getLemma();

    /**
     *
     * @param lemma new lemma of the node
     */
    void setLemma(String lemma);

    /**
     *
     * @return upos of the node
     */
    String getUpos();

    /**
     *
     * @param upos new upos of the node
     */
    void setUpos(String upos);

    /**
     *
     * @return xpos of the node
     */
    String getXpos();

    /**
     *
     * @param xpos new xpos of the node
     */
    void setXpos(String xpos);

    /**
     *
     * @return feats of the node
     */
    String getFeats();

    /**
     *
     * @param feats new feats of the node
     */
    void setFeats(String feats);

    /**
     *
     * @return head of the node
     */
    String getHead();

    /**
     *
     * @param head new head of the node
     */
    void setHead(String head);

    /**
     *
     * @return deprel of the node
     */
    String getDeprel();

    /**
     *
     * @param deprel new deprel of the node
     */
    void setDeprel(String deprel);

    /**
     *
     * @return deps of the node
     */
    EnhancedDeps getDeps();


    /**
     *
     * @param deps new deps of the node
     */
    void setDeps(EnhancedDeps deps);

    /**
     *
     * @return misc of the node
     */
    String getMisc();

    /**
     *
     * @param misc new misc of the node
     */
    void setMisc(String misc);

    void addMisc(String key, String value);

    /**
     *
     * @return ord of the node
     */
    int getOrd();

    /**
     *
     * @param ord new ord of the node
     */
    void setOrd(int ord);

    /**
     * Shifts node after given node.
     *
     * @param node node to shift after
     */
    void shiftAfterNode(Token node);

    /**
     * Shifts node after given node.
     *
     * @param node node to shift after
     * @param args args to augment resulting collection
     */
    void shiftAfterNode(Token node, EnumSet<ShiftArg> args);

    /**
     * Shifts node before given node.
     *
     * @param node node to shift before
     */
    void shiftBeforeNode(Token node);

    /**
     * Shifts node before given node.
     *
     * @param node node to shift before
     * @param args args to augment resulting collection
     */
    void shiftBeforeNode(Token node, EnumSet<ShiftArg> args);

    /**
     * Shifts node with its subtree after given node.
     *
     * @param node node to shift after
     */
    void shiftAfterSubtree(Token node);

    /**
     * Shifts node with its subtree after given node.
     *
     * @param node node to shift after
     * @param args args to augment resulting collection
     */
    void shiftAfterSubtree(Token node, EnumSet<ShiftArg> args);

    /**
     * Shifts node with its subtree before given node.
     *
     * @param node node to shift before
     */
    void shiftBeforeSubtree(Token node);

    /**
     * Shifts node with its subtree before given node.
     *
     * @param node node to shift before
     * @param args args to augment resulting collection
     */
    void shiftBeforeSubtree(Token node, EnumSet<ShiftArg> args);

    /**
     *
     * @param anotherNode anotherNode we want to find out precedes relation
     * @return true if the node precedes anotherNode
     */
    boolean precedes(Token anotherNode);

    /**
     * Removes node from the tree.
     *
     * @param args args to augment removal
     */
    void remove(EnumSet<RemoveArg> args);

    /**
     *
     * @return tree of the sentence the node is part of
     */
    Sentence getRoot();

    /**
     *
     * @return bundle the node belongs to
     */
    Bundle getBundle();

    /**
     *
     * @return document the node belongs to
     */
    Document getDocument();

    /**
     *
     * @return address of the node
     */
    String getAddress();

    /**
     *
     * @return associated Mwt node
     */
    Optional<MultiwordToken> getMwt();

    /**
     *
     * @param mwt Mwt node the node is associated with
     */
    void setMwt(MultiwordToken mwt);
}
