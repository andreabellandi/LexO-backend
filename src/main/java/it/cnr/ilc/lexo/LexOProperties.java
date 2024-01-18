package it.cnr.ilc.lexo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class LexOProperties {

    private static final Properties PROPERTIES = new Properties();
    static final Logger logger = LoggerFactory.getLogger(LexOFilter.class.getName());

    static {
        load();
    }

    public static final void load() {
        logger.debug("Lexofilter.context: " + LexOFilter.CONTEXT);
        InputStream input = null;
        try {
            input = LexOProperties.class.getClassLoader().getResourceAsStream("lexo-server.properties");
            PROPERTIES.load(input);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            try {
                if (null != input) {
                    input.close();
                }
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }
    
    public static void setProperty(String key, String defaultValue) {
        PROPERTIES.put(key, defaultValue);
    }
}
