/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.converter.tbx;

import java.util.List;

/**
 *
 * @author andreabellandi
 */
public class TermSelection {

    public final String mainTerm;
    public final List<String> altForms;

    public TermSelection(String mainTerm, List<String> altForms) {
        this.mainTerm = mainTerm;
        this.altForms = altForms;
    }
}
