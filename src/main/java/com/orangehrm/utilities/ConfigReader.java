package com.orangehrm.utilities;

import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigReader {
    private static final Properties prop = new Properties();
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                prop.load(is);
                logger.info("config.properties loaded successfully via ClassLoader.");
            } else {
                logger.error("config.properties not found in classpath!");
            }
        } catch (Exception e) {
            logger.fatal("Failed to load config.properties: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }

    // Helper for Boolean values (Headless, Grid, etc.)
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static int getInt(String key, int defaultValue) {
        try {
            String v = prop.getProperty(key);
            return (v != null && !v.trim().isEmpty()) ? Integer.parseInt(v.trim()) : defaultValue;
        } catch (Exception e) {
            logger.warn("Invalid integer for key: " + key + ". Using default: " + defaultValue);
            return defaultValue;
        }
    }

    public static int getExplicitWait() { return getInt("explicitWait", 30); }
    public static int getImplicitWait() { return getInt("implicitWait", 10); }
}