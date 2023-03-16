/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.queryExpansion;

import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.lexicon.output.FormItem;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
public class FormList implements Data {

    private String semanticRelation;
    private int distance;
    private String sense;
    private List<Form> target;

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

    public List<Form> getTarget() {
        return target;
    }

    public void setTarget(List<Form> target) {
        this.target = target;
    }

    public FormList() {}
    
    public FormList(String semanticRelation, int distance, String sense, 
//            String senseInstanceName, 
//            String targetSense, String targetSenseInstanceName, 
//            String label, String definition, 
            List<Form> target) {
//        this.definition = definition;
        this.distance = distance;
//        this.label = label;
        this.semanticRelation = semanticRelation;
        this.sense = sense;
        this.target = target;
//        this.targetSense = targetSense;
//        this.targetSenseInstanceName = targetSenseInstanceName;
    }
}
