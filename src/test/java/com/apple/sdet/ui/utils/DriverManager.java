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
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        // Headless mode if configured
        if (ConfigReader.isHeadless()) {
            logger.info("Initializing Chrome in HEADLESS mode");
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        } else {
            logger.info("Initializing Chrome in HEADED mode");
        }
        
        // Language configuration
        options.addArguments("--lang=en");
        
        // Disable logging to reduce output
        options.addArguments("--disable-logging");
        options.addArguments("--disable-dev-shm-usage");
        
        WebDriver chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().window().maximize();
        
        logger.info("Chrome WebDriver initialized successfully in {} mode", 
            ConfigReader.isHeadless() ? "HEADLESS" : "HEADED");
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
     * Ensures proper cleanup of browser process and resources.
     */
    public static void quitDriver() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                // Close all windows
                currentDriver.quit();
                logger.info("WebDriver instance closed successfully and all browser processes terminated");
            } catch (Exception e) {
                logger.warn("Error while quitting WebDriver, attempting to force close", e);
                try {
                    currentDriver.quit();
                } catch (Exception ex) {
                    logger.error("Failed to quit WebDriver", ex);
                }
            } finally {
                driver.remove();
                logger.debug("Removed WebDriver from ThreadLocal storage");
            }
        } else {
            logger.debug("No WebDriver instance to quit");
        }
    }
}
