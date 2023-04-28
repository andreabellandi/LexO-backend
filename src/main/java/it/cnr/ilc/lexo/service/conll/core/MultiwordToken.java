package it.cnr.ilc.lexo.service.conll.core;

import java.util.List;

/**
 * Created by mvojtek on 05/07/2017.
 */
public interface MultiwordToken {
    String getForm();

    Misc getMisc();

    void setWords(List<Token> words);

    void setRoot(Sentence root);

    void setForm(String form);

    void setMisc(String misc);

    String getOrdRange();

    String getAddresss();

    List<Token> getTokens();

    String toStringFormat();
}
