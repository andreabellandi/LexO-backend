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
public class BibliographyService_Settings extends SuperEntity {

    private String url;

    public BibliographyService_Settings() {
    }

}
