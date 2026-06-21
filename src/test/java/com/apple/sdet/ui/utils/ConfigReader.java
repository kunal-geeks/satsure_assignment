package com.apple.sdet.ui.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Configuration reader for test environment settings.
 * Loads properties from config files and system properties.
 */
public class ConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            String environment = System.getProperty("environment", "test");
            String configFile = String.format("config-%s.properties", environment);

            properties.load(
                    ConfigReader.class.getClassLoader()
                            .getResourceAsStream(configFile));
            logger.info("Configuration loaded from: {}", configFile);
        } catch (IOException | NullPointerException e) {
            logger.warn("Config file not found, using defaults", e);
            loadDefaults();
        }
    }

    private ConfigReader() {
        // Private constructor
    }

    /**
     * Get base URL for test environment.
     *
     * @return Base URL
     */
    public static String getBaseUrl() {
        return properties.getProperty("base.url", "https://test.com");
    }

    /**
     * Get explicit wait timeout in seconds.
     *
     * @return Timeout duration
     */
    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("timeout.explicit", "10"));
    }

    /**
     * Get implicit wait timeout in seconds.
     *
     * @return Timeout duration
     */
    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("timeout.implicit", "5"));
    }

    /**
     * Get browser name.
     *
     * @return Browser name
     */
    public static String getBrowser() {
        return properties.getProperty("browser", "chrome").toLowerCase();
    }

    /**
     * Check if headless mode is enabled.
     *
     * @return true if headless mode is enabled
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    /**
     * Get API base URL.
     *
     * @return API base URL
     */
    public static String getApiBaseUrl() {
        return properties.getProperty("api.base.url", "https://test.com/api");
    }

    /**
     * Load default configuration values.
     */
    private static void loadDefaults() {
        properties.setProperty("base.url", "https://test.com");
        properties.setProperty("api.base.url", "https://test.com/api");
        properties.setProperty("timeout.explicit", "10");
        properties.setProperty("timeout.implicit", "5");
        properties.setProperty("browser", "chrome");
        properties.setProperty("headless", "false");
    }
}
