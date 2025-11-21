/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andreabellandi
 */
public class PoSMapping {

    private static final Map<String, String> POS_MAP;

    public static Map<String, String> getPOS_MAP() {
        return POS_MAP;
    }

    static {
        Map<String, String> m = new HashMap<>();
        m.put("http://www.lexinfo.net/ontology/3.0/lexinfo#noun", "noun");
        m.put("http://www.lexinfo.net/ontology/3.0/lexinfo#verb", "verb");
        m.put("http://www.lexinfo.net/ontology/3.0/lexinfo#adjective", "adjective");
        m.put("http://www.lexinfo.net/ontology/3.0/lexinfo#adverb", "adverb");
        POS_MAP = Collections.unmodifiableMap(m);
    }
}
