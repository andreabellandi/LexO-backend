package it.cnr.ilc.lexo.hibernate.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author andreabellandi
 */
@Entity
public class AccountTypeEntity extends SuperEntity {

    private String name;
    private String color;
    private Integer position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
