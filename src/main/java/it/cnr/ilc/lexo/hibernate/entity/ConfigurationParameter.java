/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.hibernate.entity;

/**
 *
 * @author andreabellandi
 */
import javax.persistence.Entity;

@Entity
public class ConfigurationParameter extends SuperEntity {

    private String key;
    private String value;
    
    public ConfigurationParameter() {}
    
    public ConfigurationParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
