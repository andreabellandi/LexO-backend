/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.converter.tbx;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
public class EntryForms {

    public final List<String> canonical = new ArrayList<>();
    public final List<String> other = new ArrayList<>();
    public final List<String> lexical = new ArrayList<>();
    public final List<String> label = new ArrayList<>();

    public void add(String kind, String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        if ("canonical".equals(kind)) {
            canonical.add(text);
        } else if ("other".equals(kind)) {
            other.add(text);
        } else if ("lexical".equals(kind)) {
            lexical.add(text);
        } else if ("label".equals(kind)) {
            label.add(text);
        }
    }
}
