package it.cnr.ilc.lexo.service.conll.core.impl;

import it.cnr.ilc.lexo.service.conll.core.EmptyNode;
import it.cnr.ilc.lexo.service.conll.core.Sentence;


/**
 * Created by mvojtek on 05/07/2017.
 */
public class DefaultEmptyNode extends DefaultToken implements EmptyNode {

    private String id;

    public DefaultEmptyNode(Sentence tree) {
        super(tree);
    }

    @Override
    public String getEmptyNodeId() {
        return id;
    }

    @Override
    public void setEmptyNodeId(String id) {
        this.id = id;
    }

    @Override
    public int getEmptyNodePrefixId() {
        return Integer.parseInt(id.substring(0, id.indexOf(".")));
    }
}
