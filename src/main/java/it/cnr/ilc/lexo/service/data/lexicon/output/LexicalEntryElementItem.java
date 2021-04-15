/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;

/**
 *
 * @author andreabellandi
 */
public class LexicalEntryElementItem implements Data {

    private String type;
    private ArrayList<Element> elements;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }

    public LexicalEntryElementItem() {}
    
    public LexicalEntryElementItem(String type, ArrayList<Element> elements) {
        this.type = type;
        this.elements = elements;
    }

    public static class Element {

        private String label;
        private int count;
        private boolean hasChildren;

        public Element(String label, int count, boolean hasChildren) {
            this.label = label;
            this.count = count;
            this.hasChildren = hasChildren;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isHasChildren() {
            return hasChildren;
        }

        public void setHasChildren(boolean hasChildren) {
            this.hasChildren = hasChildren;
        }

        public Element() {
        }
    }

}
