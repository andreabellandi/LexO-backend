package it.cnr.ilc.lexo.service.conll.core.impl;

import it.cnr.ilc.lexo.service.conll.core.Sentence;
import it.cnr.ilc.lexo.service.conll.core.Token;
import java.util.*;


/**
 * Represents technical root of the sentence.
 *
 * @author Martin Vojtek
 */
public class DefaultRootToken extends DefaultToken {

    public DefaultRootToken(Sentence tree, Token parent) {
        super(tree, parent);
        setRootFields();
    }

    public DefaultRootToken(Sentence tree) {
        super(tree, null);
        setRootFields();
    }

    private void setRootFields() {
        setForm("<ROOT>");
        setLemma("<ROOT>");
        setUpos("<ROOT>");
        setXpos("<ROOT>");
        setFeats("<ROOT>");
        setDeprel("<ROOT>");
        setDeps(new DefaultEnhancedDeps("_", (Sentence) tree));
    }

    @Override
    public List<Token> getTokens() {
        return Collections.unmodifiableList(tree.getTokens());
    }

    @Override
    public List<Token> getTokens(EnumSet<DescendantsArg> args) {
        if (args.isEmpty()) {
            return Collections.unmodifiableList(tree.getTokens());
        }

        return getDescendantsInner(args, Optional.empty());
    }

    @Override
    public List<Token> getTokens(EnumSet<DescendantsArg> args, Token except) {
        if (args.isEmpty()) {
            return Collections.unmodifiableList(tree.getTokens());
        }

        return getDescendantsInner(args, Optional.of(except));
    }

    @Override
    public List<Token> getDescendantsF() {
        return Collections.unmodifiableList(tree.getTokens());
    }

    @Override
    protected List<Token> getDescendantsInner(EnumSet<DescendantsArg> args, Optional<Token> except) {
        if (args.contains(DescendantsArg.FIRST_ONLY)) {
            if (args.contains(DescendantsArg.ADD_SELF)) {
                return Arrays.asList(this);
            }
            return Arrays.asList(tree.getTokens().get(0));
        }

        return super.getDescendantsInner(args, except);
    }

    @Override
    public boolean isDescendantOf(Token node) {
        return false;
    }

    @Override
    public Optional<Token> getPrevNode() {
        return Optional.empty();
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public boolean precedes(Token anotherNode) {
        return true;
    }

    @Override
    public Optional<Token> getNextNode() {
        List<Token> descendants = tree.getTokens();
        if (descendants.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(descendants.get(0));
    }
}
