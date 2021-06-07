/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.vocabulary;

import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
public class Value implements Data {

    private String valueId;
    private String valueLabel;
    private String valueDescription;

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getValueLabel() {
        return valueLabel;
    }

    public void setValueLabel(String valueLabel) {
        this.valueLabel = valueLabel;
    }

    public String getValueDescription() {
        return valueDescription;
    }

    public void setValueDescription(String valueDescription) {
        this.valueDescription = valueDescription;
    }

}
