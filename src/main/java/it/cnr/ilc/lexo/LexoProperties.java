package it.cnr.ilc.lexo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.hibernate.Hibernate;

/**
 *
 * @author andreabellandi
 */
public class LexoProperties {

    private static final Properties PROPERTIES = new Properties();

    static {
        load();
    }

    public static final void load() {
        System.err.println("Lexofilter.context " + LexoFilter.CONTEXT);
        InputStream input = null;
        try {
//            input = LexoProperties.class.getResourceAsStream("/" + LexoFilter.CONTEXT + ".properties");
            input = LexoProperties.class.getClassLoader().getResourceAsStream("lexo-server.properties");
            PROPERTIES.load(input);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }
}
