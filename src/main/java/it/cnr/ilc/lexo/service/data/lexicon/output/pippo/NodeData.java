/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.pippo;

import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
public class NodeData implements Data {
        private String id;
        private String label;
        private String definition;
        private String IRI;
        private String pos;

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

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public String getIRI() {
            return IRI;
        }

        public void setIRI(String IRI) {
            this.IRI = IRI;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public NodeData() {
        }
        
        public NodeData(String id, String label, String definition, String IRI, String pos) {
            this.id = id;
            this.label = label;
            this.definition = definition;
            this.IRI = IRI;
            this.pos = pos;
        }
}
