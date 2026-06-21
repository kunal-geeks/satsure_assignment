package com.apple.sdet.ui.pages;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base page class containing common functionality for all page objects.
 * Provides centralized wait and navigation methods.
 */
public class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected WebDriver driver;

    /**
     * Constructor for BasePage.
     *
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Navigate to a specific URL.
     *
     * @param url URL to navigate to
     */
    protected void navigateTo(String url) {
        driver.navigate().to(url);
        logger.info("Navigated to: {}", url);
    }

    /**
     * Get current page URL.
     *
     * @return Current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get page title.
     *
     * @return Page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
}
