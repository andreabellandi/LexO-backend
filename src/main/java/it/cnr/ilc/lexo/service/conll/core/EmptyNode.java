package it.cnr.ilc.lexo.service.conll.core;

/**
 * Created by mvojtek on 05/07/2017.
 */
public interface EmptyNode extends Token {
    public String getEmptyNodeId();

    public void setEmptyNodeId(String id);

    public int getEmptyNodePrefixId();
}
