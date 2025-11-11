/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.manager.converter.Converter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author andreabellandi
 */
public final class ConverterRegistry {
    private static final ConverterRegistry INSTANCE = new ConverterRegistry();
    private final Map<String, Converter> byKey = new ConcurrentHashMap<>();

    private ConverterRegistry() {}

    public static ConverterRegistry get() {
        return INSTANCE;
    }

    public void register(Converter c) {
        byKey.put(key(c.sourceType(), c.targetType()), c);
    }

    public Converter resolve(String from, String to) {
        Converter c = byKey.get(key(from, to));
        if (c == null) {
            throw new IllegalStateException("No converter for " + from + " -> " + to);
        }
        return c;
    }

    private static String key(String from, String to) {
        return from.trim().toLowerCase(Locale.ROOT) + "->" + to.trim().toLowerCase(Locale.ROOT);
    }
}