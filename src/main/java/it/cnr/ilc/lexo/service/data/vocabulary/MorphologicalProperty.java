/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.vocabulary;

import it.cnr.ilc.lexo.service.data.Data;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
public class MorphologicalProperty implements Data {

    private String propertyId;
    private String propertyLabel;
    private String propertyDescription;
    private List<MorphologicalValue> propertyValues;

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public String getPropertyLabel() {
        return propertyLabel;
    }

    public void setPropertyLabel(String propertyLabel) {
        this.propertyLabel = propertyLabel;
    }

    public List<MorphologicalValue> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(List<MorphologicalValue> propertyValues) {
        this.propertyValues = propertyValues;
    }

    public static class MorphologicalValue {

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

        public MorphologicalValue(String valueId, String valueLabel, String valueDescription) {
            this.valueId = valueId;
            this.valueLabel = valueLabel;
            this.valueDescription = valueDescription;
        }
    }

}
