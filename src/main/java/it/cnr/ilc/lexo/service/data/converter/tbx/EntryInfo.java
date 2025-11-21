/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.converter.tbx;

/**
 *
 * @author andreabellandi
 */
public class EntryInfo {

    public final String entryId;
    public final String lexLang;
    public String pos;
    public String definition;

    public EntryInfo(String entryId, String lexLang, String pos, String definition) {
        this.entryId = entryId;
        this.lexLang = lexLang;
        this.pos = pos;
        this.definition = definition;
    }
}
