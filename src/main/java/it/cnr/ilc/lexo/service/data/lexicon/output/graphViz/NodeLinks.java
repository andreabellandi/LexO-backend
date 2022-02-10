/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.graphViz;

import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;

/**
 *
 * @author andreabellandi
 */
public class NodeLinks implements Data {

    private boolean inferred;
    private ArrayList<_Links> nodeLinks = new ArrayList();

    public ArrayList<_Links> getNodeLinks() {
        return nodeLinks;
    }

    public void setNodeLinks(ArrayList<_Links> nodeLinks) {
        this.nodeLinks = nodeLinks;
    }

    public boolean isInferred() {
        return inferred;
    }

    public void setInferred(boolean inferred) {
        this.inferred = inferred;
    }

    public static class _Links {

        private String linkType;
        private String linkName;
        private int linkCount;
        private ArrayList<String> targets = new ArrayList();

        public String getLinkType() {
            return linkType;
        }

        public void setLinkType(String linkType) {
            this.linkType = linkType;
        }

        public String getLinkName() {
            return linkName;
        }

        public void setLinkName(String linkName) {
            this.linkName = linkName;
        }

        public ArrayList<String> getTargets() {
            return targets;
        }

        public void setTargets(ArrayList<String> targets) {
            this.targets = targets;
        }

        public int getLinkCount() {
            return linkCount;
        }

        public void setLinkCount(int linkCount) {
            this.linkCount = linkCount;
        }

    }

}
