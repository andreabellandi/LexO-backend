package it.cnr.ilc.lexo.service.conll.core.impl;

import it.cnr.ilc.lexo.service.conll.core.Bundle;
import it.cnr.ilc.lexo.service.conll.ConllException;
import it.cnr.ilc.lexo.service.conll.core.Document;
import it.cnr.ilc.lexo.service.conll.core.EnhancedDeps;
import it.cnr.ilc.lexo.service.conll.core.MultiwordToken;
import it.cnr.ilc.lexo.service.conll.core.Sentence;
import it.cnr.ilc.lexo.service.conll.core.Token;
import java.util.*;


/**
 * Default implementation of node.
 *
 * Every word in a sentence is represented by a node.
 *
 * @author Martin Vojtek
 */
public class DefaultToken implements Token {

    protected final Sentence tree;

    private final int id;
    private int ord = -1;
    private boolean isRemoved;

    private String form;
    private String lemma;
    private String upos;
    private String xpos;
    private String feats;
    private String head;
    private String deprel;
    private EnhancedDeps deps;
    private String misc;
    private Optional<MultiwordToken> mwt = Optional.empty();

    private Optional<Token> firstChild = Optional.empty();
    private Optional<Token> nextSibling = Optional.empty();

    private Optional<Token> parent;

    public DefaultToken(Sentence tree, Token parent) {
        this.parent = Optional.ofNullable(parent);
        this.tree = tree;
        this.id = tree.getDocument().getUniqueNodeId();
    }

    public DefaultToken(Sentence tree) {
        this(tree, null);
    }

    @Override
    public void remove() {
        remove(EnumSet.noneOf(Token.RemoveArg.class));
    }

    /**
     * For non-root nodes, the general address format is:
     * node.bundle.bundle_id + '/' + node.root.zone + '#' + node.ord,
     * e.g. s123/en_udpipe#4. If zone is empty, the slash is excluded as well,
     * e.g. s123#4.
     * @return full (document-wide) id of the node.
     */
    @Override
    public String getAddress() {
        return (null != getRoot() ? getRoot().getAddress() : "?") + "#" + getOrd();
    }

    @Override
    public void remove(EnumSet<Token.RemoveArg> args) {
        //already removed
        if (isRemoved) return;

        Optional<Token> parent = getParent();
        if (args.contains(RemoveArg.REHANG)) {
            for (Token child : getChildren()) {
                child.setParent(parent.get());
            }
        }

        if (args.contains(RemoveArg.WARN)) {
            System.err.println(getAddress() + " is being removed by remove, but it has (unexpected) children");
        }

        List<Token> toRemove = getDescendantsF();
        toRemove.add(this);
        if (!toRemove.isEmpty()) {
            List<Token> allNodes = tree.getTokens();
            allNodes.removeAll(toRemove);

            //update ord of the nodes in the tree
            getRoot().normalizeOrder();
        }

        //Disconnect the node from its parent (& siblings) and delete all attributes
        Optional<Token> node = toDefaultNode(parent.get()).getFirstChild();
        if (node.isPresent() && node.get() == this) {
            toDefaultNode(parent.get()).setFirstChild(this.getNextSibling());
        } else {
            while (node.isPresent() && (!node.get().getNextSibling().isPresent() || this != node.get().getNextSibling().get())) {
                node = node.get().getNextSibling();
            }
            if (node.isPresent()) {
                node.get().setNextSibling(getNextSibling());
            }
        }

        for (Token removedNode : toRemove) {
            toDefaultNode(removedNode).isRemoved = true;
        }
    }

    @Override
    public Sentence getRoot() {
        return tree;
    }

    @Override
    public Bundle getBundle() {
        return tree.getBundle();
    }

    @Override
    public Document getDocument() {
        return getBundle().getDocument();
    }

    public List<Token> getDescendantsF() {
        if (!getFirstChild().isPresent()) {
            return new ArrayList<>();
        }

        Deque<Token> stack = new ArrayDeque<>();
        stack.push(getFirstChild().get());

        List<Token> descs = new ArrayList<>();
        while (!stack.isEmpty()) {
            Token node = stack.pop();
            descs.add(node);
            node.getNextSibling().ifPresent(next -> stack.push(next));
            toDefaultNode(node).getFirstChild().ifPresent(first -> stack.push(first));
        }

        return descs;
    }

    @Override
    public Token createChild() {
        Token newChild = createNode();
        newChild.setParent(this);
        return newChild;
    }

    protected Token createNode() {
        DefaultToken newNode = new DefaultToken(tree);
        tree.getTokens().add(newNode);
        newNode.ord = tree.getTokens().size();
        return newNode;
    }

    @Override
    public List<Token> getChildren(EnumSet<ChildrenArg> args) {

        List<Token> result = new ArrayList<>();

        Optional<Token> child = getFirstChild();
        while (child.isPresent()) {
            result.add(child.get());
            child = child.get().getNextSibling();
        }

        if (!args.isEmpty()) {
            if (args.contains(ChildrenArg.ADD_SELF)) {
                result.add(this);
            }
            if (args.contains(ChildrenArg.FIRST_ONLY)) {
                return getFirstLastNode(result, true);
            }
            if (args.contains(ChildrenArg.LAST_ONLY)) {
                return getFirstLastNode(result, false);
            }
        }

        result.sort((o1, o2) -> o1.getOrd() - o2.getOrd());
        return result;
    }

    @Override
    public List<Token> getChildren() {
        return getChildren(EnumSet.noneOf(Token.ChildrenArg.class));
    }

    @Override
    public Optional<Token> getParent() {
        return parent;
    }

    public void setParent(Token parent) {
        setParent(parent, false);
    }

    @Override
    public void setParent(Token parent, boolean skipCycles) {

        if (null == parent) {
            throw new ConllException("Not allowed to set null parent.");
        }

        //check cycles
        if (this == parent) {
            if (skipCycles) return;
            throw new ConllException("Bundle " + tree.getBundle().getId() + ": Attempt to set parent of " + ord
                    + " to itself (cycle).");
        }
        if (firstChild.isPresent()) {
            Optional<Token> grandpa = parent.getParent();
            while (grandpa.isPresent()) {
                if (grandpa.get() == this) {
                    if (skipCycles) return;
                    throw new ConllException("Bundle " + tree.getBundle().getId() + ": Attempt to set parent of " + ord
                            + " to the node " + parent.getId() + ", which would lead to a cycle.");
                }
                grandpa = grandpa.get().getParent();
            }
        }

        //Disconnect the node from its original parent
        Optional<Token> origParent = getParent();
        if (origParent.isPresent()) {
            Optional<Token> node = toDefaultNode(origParent.get()).getFirstChild();
            if (node.isPresent() && this == node.get()) {
                toDefaultNode(origParent.get()).setFirstChild(nextSibling);
            } else {
                while (node.isPresent() && (!node.get().getNextSibling().isPresent() || this != node.get().getNextSibling().get())) {
                    node = node.get().getNextSibling();
                }
                if (node.isPresent()) {
                    node.get().setNextSibling(nextSibling);
                }
            }
        }

        //Attach the node to its parent and linked list of siblings.
        this.parent = Optional.of(parent);
        this.nextSibling = toDefaultNode(parent).getFirstChild();
        toDefaultNode(parent).setFirstChild(Optional.of(this));
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public List<Token> getTokens(EnumSet<Token.DescendantsArg> args, Token except) {

        if (args.isEmpty()) {
            return getDescendantsInner(args, Optional.of(except));
        }

        return getDescendantsInner(args, Optional.of(except));
    }

    @Override
    public List<Token> getTokens(EnumSet<Token.DescendantsArg> args) {
        return getDescendantsInner(args, Optional.empty());
    }

    @Override
    public List<Token> getTokens() {
        return getDescendantsInner(EnumSet.noneOf(Token.DescendantsArg.class), Optional.empty());
    }

    protected List<Token> getDescendantsInner(EnumSet<Token.DescendantsArg> args, Optional<Token> except) {
        if (except.isPresent() && this == except.get()) {
            return new ArrayList<>();
        }

        List<Token> descs = new ArrayList<>();
        Deque<Token> stack = new ArrayDeque<>();
        getFirstChild().ifPresent(first -> stack.push(first));
        Token node;
        while (!stack.isEmpty()) {
            node = stack.pop();
            node.getNextSibling().ifPresent(next -> stack.push(next));
            if (except.isPresent() && except.get() == node) {
                continue;
            }
            descs.add(node);
            toDefaultNode(node).getFirstChild().ifPresent(first -> stack.push(first));
        }

        if (args.contains(DescendantsArg.ADD_SELF)) {
            descs.add(this);
        }

        if (args.contains(DescendantsArg.FIRST_ONLY)) {
            return getFirstLastNode(descs, true);
        }

        if (args.contains(DescendantsArg.LAST_ONLY)) {
            return getFirstLastNode(descs, false);
        }

        descs.sort((o1, o2) -> o1.getOrd() - o2.getOrd());
        return descs;
    }

    private DefaultToken toDefaultNode(Token node) {
        return (DefaultToken) node;
    }

    private List<Token> getFirstLastNode(List<Token> descs, boolean first) {
        if (!descs.isEmpty()) {
            Token firstLast = descs.get(0);
            for (int i = 1; i < descs.size(); i++) {
                Token next = descs.get(i);
                if (first) {
                    if (next.getOrd() < firstLast.getOrd()) {
                        firstLast = next;
                    }
                } else {
                    if (next.getOrd() > firstLast.getOrd()) {
                        firstLast = next;
                    }
                }
            }
            return Arrays.asList(firstLast);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Token> getSiblings() {
        if (parent.isPresent()) {
            List<Token> siblings = parent.get().getChildren();
            siblings.remove(this);
            return siblings;
        } else return new ArrayList<>();
    }

    @Override
    public Optional<Token> getPrevSibling() {
        if (parent.isPresent()) {
            List<Token> parentChildren = parent.get().getChildren();

            int index = parentChildren.indexOf(this);
            if (index != -1 && index > 0) {
                return Optional.of(parentChildren.get(index - 1));
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Token> getNextSibling() {
        return nextSibling;
    }

    @Override
    public void setNextSibling(Optional<Token> newNextSibling) {
        this.nextSibling = newNextSibling;
    }

    @Override
    public Optional<Token> getNextNode() {
        int ord = getOrd();
        List<Token> rootDescendants = tree.getTokens();
        if (ord == rootDescendants.size()) {
            return Optional.empty();
        }
        return Optional.of(rootDescendants.get(ord));
    }

    @Override
    public Optional<Token> getPrevNode() {

        int ord = getOrd() - 1;

        if (0 == ord) {
            return Optional.of(tree.getToken());
        }

        return Optional.of(tree.getTokens().get(ord - 1));
    }

    @Override
    public boolean isDescendantOf(Token node) {

        if (!toDefaultNode(node).getFirstChild().isPresent()) {
            return false;
        }

        Optional<Token> pathParent = parent;
        while (pathParent.isPresent()) {
            if (pathParent.get() == node) {
                return true;
            } else {
                pathParent = pathParent.get().getParent();
            }
        }
        return false;
    }

    public void shiftAfterNode(Token node) {
        shiftAfterNode(node, EnumSet.noneOf(ShiftArg.class));
    }

    public void shiftAfterNode(Token node, EnumSet<ShiftArg> args) {
        shiftToNode(node, true, false, args);
    }

    public void shiftBeforeNode(Token node) {
        shiftBeforeNode(node, EnumSet.noneOf(ShiftArg.class));
    }

    public void shiftBeforeNode(Token node, EnumSet<ShiftArg> args) {
        shiftToNode(node, false, false, args);
    }

    public void shiftBeforeSubtree(Token node) {
        shiftBeforeSubtree(node, EnumSet.noneOf(ShiftArg.class));
    }

    public void shiftBeforeSubtree(Token node, EnumSet<ShiftArg> args) {
        shiftToNode(node, false, true, args);
    }

    public void shiftAfterSubtree(Token node) {
        shiftAfterSubtree(node, EnumSet.noneOf(ShiftArg.class));
    }

    public void shiftAfterSubtree(Token node, EnumSet<ShiftArg> args) {
        shiftToNode(node, true, true, args);
    }

    private void shiftToNode(Token referenceNode, boolean after, boolean subtree, EnumSet<ShiftArg> args) {

        //node.shiftAfterNode(node) should result in no action.
        if (!subtree && this == referenceNode) {
            return;
        }

        boolean withoutChildren = args.contains(ShiftArg.WITHOUT_CHILDREN);
        boolean skipIfDescendant = args.contains(ShiftArg.SKIP_IF_DESCENDANT);

        if (!firstChild.isPresent()) {
            withoutChildren = true;
        }

        if (!withoutChildren && referenceNode.isDescendantOf(this)) {
            if (skipIfDescendant) {
                return;
            }

            System.err.println("Node " + referenceNode + " is a descendant of " + this
                    + ". Maybe you have forgotten ShiftArg.WITHOUT_CHILDREN.");
        }

        //For shiftSubtree* methods, we need to find the real reference node first.
        if (subtree) {
            if (withoutChildren) {
                Token newRef = null;
                if (after) {

                    if (this != referenceNode) {
                        newRef = referenceNode;
                    }

                    for (Token node : toDefaultNode(referenceNode).getDescendantsF()) {
                        if (this == node) continue;
                        if (null == newRef || node.getOrd() > newRef.getOrd()) {
                            newRef = node;
                        }
                    }
                } else {
                    if (this != referenceNode) {
                        newRef = referenceNode;
                    }

                    for (Token node : toDefaultNode(referenceNode).getDescendantsF()) {
                        if (this == node) continue;
                        if (null == newRef || node.getOrd() < newRef.getOrd()) {
                            newRef = node;
                        }
                    }
                }
                if (null == newRef) {
                    return;
                }
                referenceNode = newRef;
            } else {
                //$reference_node, 1, !$after, $after, $self
                EnumSet<DescendantsArg> descendantsArgs = EnumSet.of(DescendantsArg.ADD_SELF);
                if (after) {
                    descendantsArgs.add(DescendantsArg.LAST_ONLY);
                } else {
                    descendantsArgs.add(DescendantsArg.FIRST_ONLY);
                }

                List<Token> descendants = referenceNode.getTokens(descendantsArgs, this);
                referenceNode = descendants.get(0);
            }
        }

        //convert shiftAfter* to shiftBefore*
        List<Token> allNodes = tree.getTokens();
        int referenceOrd = referenceNode.getOrd();
        if (after) {
            referenceOrd++;
        }

        //without children means moving just one node, which is easier
        if (withoutChildren) {
            int myOrd = getOrd();
            if (referenceOrd > myOrd + 1) {
                for (int newOrd = myOrd; newOrd < referenceOrd - 1; newOrd++) {
                    Token ordNode = allNodes.get(newOrd);
                    allNodes.set(newOrd - 1, ordNode);
                    ordNode.setOrd(newOrd);
                }
                allNodes.set(referenceOrd - 2, this);
                setOrd(referenceOrd - 1);
            } else if (referenceOrd < myOrd) {
                for (int newOrd = myOrd; newOrd > referenceOrd; newOrd--) {
                    Token ordNode = allNodes.get(newOrd - 2);
                    allNodes.set(newOrd - 1, ordNode);
                    ordNode.setOrd(newOrd);
                }
                allNodes.set(referenceOrd - 1, this);
                setOrd(referenceOrd);
            }
            return;
        }

        //which nodes are to be moved?
        //this and all its descendants
        List<Token> nodesToMove = getTokens(EnumSet.of(DescendantsArg.ADD_SELF));
        int firstOrd = nodesToMove.get(0).getOrd();
        int lastOrd = nodesToMove.get(nodesToMove.size() - 1).getOrd();

        //TODO: optimization in case of no "gaps"

        //First, move a node from position sourceOrd to position targetOrd RIGH-ward.
        //sourceOrd iterates decreasingly over nodes which are not moving.
        int targetOrd = lastOrd;
        int sourceOrd = lastOrd - 1;
        int moveOrd = nodesToMove.size() - 2;

        RIGHTSWIPE:
        while (sourceOrd >= referenceOrd) {
            while (moveOrd >= 0 && allNodes.get(sourceOrd - 1) == nodesToMove.get(moveOrd)) {
                sourceOrd--;
                moveOrd--;
                if (sourceOrd < referenceOrd) {
                    break RIGHTSWIPE;
                }
            }
            Token ordNode = allNodes.get(sourceOrd - 1);
            allNodes.set(targetOrd - 1, ordNode);
            ordNode.setOrd(targetOrd);
            targetOrd--;
            sourceOrd--;
        }

        //Second, move a node from position sourceOrd to position targetOrd LEFT-ward.
        //sourceOrd iterates increasingly over nodes which are not moving.
        targetOrd = firstOrd;
        sourceOrd = firstOrd + 1;
        moveOrd = 1;

        LEFTSWIPE:
        while (sourceOrd < referenceOrd) {
            while (moveOrd < nodesToMove.size() && allNodes.get(sourceOrd - 1) == nodesToMove.get(moveOrd)) {
                sourceOrd++;
                moveOrd++;
                if (sourceOrd >= referenceOrd) {
                    break LEFTSWIPE;
                }
            }
            Token ordNode = allNodes.get(sourceOrd - 1);
            allNodes.set(targetOrd - 1, ordNode);
            ordNode.setOrd(targetOrd);
            targetOrd++;
            sourceOrd++;
        }

        //Third, move nodesToMove to targetOrd RIGHT-ward
        if (referenceOrd < firstOrd) {
            targetOrd = referenceOrd;
        }
        for (Token node : nodesToMove) {
            allNodes.set(targetOrd - 1, node);
            node.setOrd(targetOrd++);
        }

    }

    @Override
    public boolean precedes(Token anotherNode) {
        return ord < anotherNode.getOrd();
    }

    Optional<Token> getFirstChild() {
        return firstChild;
    }

    void setFirstChild(Optional<Token> newFirstChild) {
        this.firstChild = newFirstChild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultToken that = (DefaultToken) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public int getId() {
        return id;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getUpos() {
        return upos;
    }

    public void setUpos(String upos) {
        this.upos = upos;
    }

    public String getXpos() {
        return xpos;
    }

    public void setXpos(String xpos) {
        this.xpos = xpos;
    }

    public String getFeats() {
        return feats;
    }

    public void setFeats(String feats) {
        this.feats = feats;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDeprel() {
        return deprel;
    }

    public void setDeprel(String deprel) {
        this.deprel = deprel;
    }

    public EnhancedDeps getDeps() {
        return deps;
    }

    public void setDeps(EnhancedDeps deps) {
        this.deps = deps;
    }

    public String getMisc() {
        return misc;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }

    public void addMisc(String key, String value) {
        String keyValue = String.format("%s=%s", key, value);

        if (!misc.equals("_"))
            misc += "|" + keyValue;
        else misc = keyValue;
    }

    @Override
    public Optional<MultiwordToken> getMwt() {
        return mwt;
    }

    @Override
    public void setMwt(MultiwordToken mwt) {
        this.mwt = Optional.of(mwt);
    }

    @Override
    public String toString() {
        return "DefaultNode[ord='" + ord + "', form='" + form + "']";
    }
}
