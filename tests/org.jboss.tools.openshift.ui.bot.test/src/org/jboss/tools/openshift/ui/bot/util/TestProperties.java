package org.jboss.tools.openshift.ui.bot.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Just static properties holder
 * 
 * @author sbunciak
 *
 */
public class TestProperties {

    public static Properties props = new Properties();

    static {

        try {
            props.load(new FileInputStream(
                    "resources/openshift.ui.bot.test.properties"));

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

    }

    public static String get(String key) {
    	return props.getProperty(key);
    }
    
    public static void put(String key, String value) {
    	props.put(key, value);
    }
}