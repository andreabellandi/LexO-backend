/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.converter.tbx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author andreabellandi
 */
public class TermRecord {

    public final String term;
    public final String pos;
    public final String definition;
    public final List<String> altForms;

    public TermRecord(String term, String pos, String definition, List<String> altForms) {
        this.term = term;
        this.pos = pos;
        this.definition = definition;
        this.altForms = (altForms == null) ? Collections.<String>emptyList()
                : Collections.unmodifiableList(new ArrayList<>(altForms));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TermRecord)) {
            return false;
        }
        TermRecord t = (TermRecord) o;
        return Objects.equals(term, t.term)
                && Objects.equals(pos, t.pos)
                && Objects.equals(definition, t.definition)
                && Objects.equals(altForms, t.altForms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, pos, definition, altForms);
    }
}
