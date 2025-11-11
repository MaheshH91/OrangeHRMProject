package com.orangehrm.utilities;

import java.io.InputStream;
import java.util.Properties;

public class configReader {
    private static final Properties prop = new Properties();

    static {
        try (InputStream is = configReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                prop.load(is);
            } else {
                System.out.println("config.properties not found in classpath");
            }
        } catch (Exception e) {
            System.out.println("Failed to load config.properties: " + e.getMessage());
        }
    }

    /**
     * Get raw property value (may be null)
     */
    public static String get(String key) {
        return prop.getProperty(key);
    }

    /**
     * Get int property with default fallback
     */
    public static int getInt(String key, int defaultValue) {
        try {
            String v = prop.getProperty(key);
            if (v == null || v.trim().isEmpty()) {
                return defaultValue;
            }
            return Integer.parseInt(v.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int getExplicitWait() {
        return getInt("explicitWait", 30);
    }

    public static int getImplicitWait() {
        return getInt("implicitWait", 10);
    }
}