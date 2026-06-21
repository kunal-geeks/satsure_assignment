package com.apple.sdet.ui.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebDriver instance management utility.
 * Handles driver creation, configuration, and teardown.
 */
public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initialize WebDriver instance for current thread.
     *
     * @return Configured WebDriver instance
     */
    public static WebDriver initializeDriver() {
        String browser = ConfigReader.getBrowser();
        
        if ("chrome".equalsIgnoreCase(browser)) {
            return initializeChromeDriver();
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    /**
     * Initialize Chrome WebDriver with optimal configurations.
     *
     * @return Configured ChromeDriver
     */
    private static WebDriver initializeChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        // Basic configuration
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--start-maximized");
        
        // Headless mode if configured
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
        }
        
        // Language configuration
        options.addArguments("--lang=en");
        
        WebDriver chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().window().maximize();
        
        logger.info("Chrome WebDriver initialized successfully");
        return chromeDriver;
    }

    /**
     * Get WebDriver instance for current thread.
     *
     * @return Current thread's WebDriver
     */
    public static WebDriver getDriver() {
        WebDriver currentDriver = driver.get();
        if (currentDriver == null) {
            currentDriver = initializeDriver();
            driver.set(currentDriver);
            logger.debug("Created new WebDriver instance for current thread");
        }
        return currentDriver;
    }

    /**
     * Close and remove WebDriver instance for current thread.
     */
    public static void quitDriver() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                currentDriver.quit();
                logger.info("WebDriver instance closed successfully");
            } catch (Exception e) {
                logger.warn("Error while quitting WebDriver", e);
            } finally {
                driver.remove();
            }
        }
    }
}
