package it.cnr.ilc.lexo.service.conll.core.impl;

import it.cnr.ilc.lexo.service.conll.core.Misc;
import it.cnr.ilc.lexo.service.conll.core.MultiwordToken;
import it.cnr.ilc.lexo.service.conll.core.Sentence;
import it.cnr.ilc.lexo.service.conll.core.Token;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mvojtek on 05/07/2017.
 *
 * Represents multi-word token in UD tree.
 */
public class DefaultMultiwordToken implements MultiwordToken {
    private List<Token> words = new ArrayList<>();
    private String form;
    private Misc misc;
    private Sentence root;
    private static final String TAB = "\t";
    private static final String UNDERSCORE = "_";

    @Override
    public String getForm() {
        return form;
    }

    @Override
    public Misc getMisc() {
        return misc;
    }

    @Override
    public void setWords(List<Token> words) {
        this.words.clear();
        if (null != words) {
            this.words.addAll(words);
        }
    }

    public List<Token> getTokens() {
        return words;
    }

    @Override
    public String toStringFormat() {
        StringBuilder sb = new StringBuilder();

        sb.append(getOrdRange());
        sb.append(TAB);
        sb.append(null != form ? form : UNDERSCORE);
        sb.append(TAB);
        sb.append("_\t_\t_\t_\t_\t_\t_\t");
        sb.append(null != misc ? misc.toStringFormat() : "_");

        return sb.toString();
    }

    @Override
    public void setRoot(Sentence root) {
        this.root = root;
    }

    @Override
    public void setForm(String form) {
        this.form = form;
    }

    @Override
    public void setMisc(String misc) {
        this.misc = new DefaultMisc(misc);
    }

    @Override
    public String getOrdRange() {
        if (words.isEmpty()) {
            return "?-?";
        } else {
            return String.format("%d-%d", words.get(0).getOrd(), words.get(words.size()-1).getOrd());
        }
    }

    @Override
    public String getAddresss() {
        String rootAddress = "?";
        if (null != root) {
            rootAddress = root.getAddress();
        }
        return rootAddress + "#" + getOrdRange();
    }


}
