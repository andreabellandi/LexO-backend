/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import it.cnr.ilc.lexo.service.data.Data;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
public class FormList implements Data {

    private String semanticRelation;
    private int distance;
    private String sense;
    private String senseInstanceName;
//    private String label;
//    private String definition;
//    private String targetSense;
//    private String targetSenseInstanceName;
    private List<FormItem> target;

    public String getSemanticRelation() {
        return semanticRelation;
    }

    public void setSemanticRelation(String semanticRelation) {
        this.semanticRelation = semanticRelation;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getSense() {
        return sense;
    }

    public void setSense(String sense) {
        this.sense = sense;
    }

    public String getSenseInstanceName() {
        return senseInstanceName;
    }

    public void setSenseInstanceName(String senseInstanceName) {
        this.senseInstanceName = senseInstanceName;
    }

//    public String getLabel() {
//        return label;
//    }
//
//    public void setLabel(String label) {
//        this.label = label;
//    }
//
//    public String getDefinition() {
//        return definition;
//    }
//
//    public void setDefinition(String definition) {
//        this.definition = definition;
//    }

    public List<FormItem> getTarget() {
        return target;
    }

    public void setTarget(List<FormItem> target) {
        this.target = target;
    }

//    public String getTargetSense() {
//        return targetSense;
//    }
//
//    public void setTargetSense(String targetSense) {
//        this.targetSense = targetSense;
//    }
//
//    public String getTargetSenseInstanceName() {
//        return targetSenseInstanceName;
//    }
//
//    public void setTargetSenseInstanceName(String targetSenseInstanceName) {
//        this.targetSenseInstanceName = targetSenseInstanceName;
//    }

    
    public FormList() {}
    
    public FormList(String semanticRelation, int distance, String sense, String senseInstanceName, 
//            String targetSense, String targetSenseInstanceName, 
//            String label, String definition, 
            List<FormItem> target) {
//        this.definition = definition;
        this.distance = distance;
//        this.label = label;
        this.semanticRelation = semanticRelation;
        this.sense = sense;
        this.senseInstanceName = senseInstanceName;
        this.target = target;
//        this.targetSense = targetSense;
//        this.targetSenseInstanceName = targetSenseInstanceName;
    }
}
