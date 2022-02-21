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
        private ArrayList<_Target> targets = new ArrayList();

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

        public ArrayList<_Target> getTargets() {
            return targets;
        }

        public void setTargets(ArrayList<_Target> targets) {
            this.targets = targets;
        }

        public int getLinkCount() {
            return linkCount;
        }

        public void setLinkCount(int linkCount) {
            this.linkCount = linkCount;
        }

    }

    public static class _Target {

        private String id;
        private String label;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

    }

}
