package com.apple.sdet.ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object Model for Autocomplete Form.
 * Encapsulates all UI interactions with the form elements.
 */
public class AutocompleteFormPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AutocompleteFormPage.class);
    
    // Locators
    private static final By INPUT_FIELD = By.id("input-field");
    private static final By SUGGESTIONS_LIST = By.cssSelector("ul.suggestions");
    private static final By SUGGESTION_ITEMS = By.cssSelector("ul.suggestions li");
    private static final By NEXT_BUTTON = By.id("next-button");
    private static final By ERROR_MESSAGE = By.cssSelector("span.error-message");
    private static final By SUCCESS_MESSAGE = By.cssSelector("div.success-container p");
    
    private final WebDriverWait wait;

    /**
     * Constructor for AutocompleteFormPage.
     *
     * @param driver WebDriver instance
     */
    public AutocompleteFormPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Load the autocomplete form page.
     */
    public void loadPage(String baseUrl) {
        navigateTo(baseUrl + "/autocomplete-form");
        waitForFormToLoad();
        logger.info("Autocomplete form page loaded successfully");
    }

    /**
     * Wait for form elements to be visible.
     */
    private void waitForFormToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_FIELD));
        wait.until(ExpectedConditions.visibilityOfElementLocated(SUGGESTIONS_LIST));
        wait.until(ExpectedConditions.visibilityOfElementLocated(NEXT_BUTTON));
    }

    /**
     * Enter text into the input field.
     *
     * @param text Text to enter
     */
    public void typeInInputField(String text) {
        WebElement inputField = wait.until(
            ExpectedConditions.elementToBeClickable(INPUT_FIELD)
        );
        inputField.clear();
        inputField.sendKeys(text);
        logger.info("Typed text in input field: {}", text);
    }

    /**
     * Get current text in input field.
     *
     * @return Current input field value
     */
    public String getInputFieldValue() {
        WebElement inputField = driver.findElement(INPUT_FIELD);
        return inputField.getAttribute("value");
    }

    /**
     * Get all visible suggestions.
     *
     * @return List of visible suggestion texts
     */
    public List<String> getVisibleSuggestions() {
        List<WebElement> suggestions = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(SUGGESTION_ITEMS)
        );
        
        List<String> visibleSuggestions = suggestions.stream()
            .filter(el -> el.isDisplayed())
            .map(WebElement::getText)
            .collect(Collectors.toList());
        
        logger.debug("Visible suggestions: {}", visibleSuggestions);
        return visibleSuggestions;
    }

    /**
     * Click on a suggestion by text.
     *
     * @param suggestionText Text of suggestion to click
     */
    public void clickSuggestion(String suggestionText) {
        List<WebElement> suggestions = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(SUGGESTION_ITEMS)
        );
        
        WebElement suggestion = suggestions.stream()
            .filter(el -> el.getText().trim().equals(suggestionText))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(
                String.format("Suggestion '%s' not found. Available: %s", 
                    suggestionText, 
                    suggestions.stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(", "))
                )
            ));
        
        wait.until(ExpectedConditions.elementToBeClickable(suggestion)).click();
        logger.info("Clicked suggestion: {}", suggestionText);
    }

    /**
     * Click the Next button.
     */
    public void clickNextButton() {
        WebElement nextButton = wait.until(
            ExpectedConditions.elementToBeClickable(NEXT_BUTTON)
        );
        nextButton.click();
        logger.info("Clicked Next button");
    }

    /**
     * Check if error message is displayed.
     *
     * @return true if error message is visible
     */
    public boolean isErrorMessageDisplayed() {
        try {
            WebElement errorElement = driver.findElement(ERROR_MESSAGE);
            return errorElement.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Get error message text.
     *
     * @return Error message text
     */
    public String getErrorMessageText() {
        WebElement errorElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE)
        );
        return errorElement.getText();
    }

    /**
     * Check if success message is displayed.
     *
     * @return true if success message is visible
     */
    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement successElement = driver.findElement(SUCCESS_MESSAGE);
            return successElement.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Get success message text.
     *
     * @return Success message text
     */
    public String getSuccessMessageText() {
        WebElement successElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(SUCCESS_MESSAGE)
        );
        return successElement.getText();
    }

    /**
     * Clear input field using keyboard.
     */
    public void clearInputFieldWithKeyboard() {
        WebElement inputField = driver.findElement(INPUT_FIELD);
        inputField.sendKeys(Keys.CONTROL + "a");
        inputField.sendKeys(Keys.DELETE);
        logger.info("Cleared input field using keyboard");
    }

    /**
     * Submit form using Enter key.
     */
    public void submitFormWithEnter() {
        WebElement inputField = driver.findElement(INPUT_FIELD);
        inputField.sendKeys(Keys.ENTER);
        logger.info("Submitted form using Enter key");
    }

    /**
     * Navigate to elements using Tab key and verify focus order.
     *
     * @param numberOfTabs Number of Tab presses to simulate
     */
    public void navigateWithTabKey(int numberOfTabs) {
        Actions actions = new Actions(driver);
        for (int i = 0; i < numberOfTabs; i++) {
            actions.sendKeys(Keys.TAB).perform();
        }
        logger.info("Navigated {} times using Tab key", numberOfTabs);
    }

    /**
     * Get currently focused element.
     *
     * @return Currently focused WebElement
     */
    public WebElement getFocusedElement() {
        return driver.switchTo().activeElement();
    }

    /**
     * Wait for all suggestions to be loaded and visible.
     */
    public void waitForSuggestionsToLoad() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(SUGGESTION_ITEMS));
        Thread.sleep(500); // Small delay for rendering
    }
    
    @SuppressWarnings("java:S112")
    private void waitForSuggestionsToLoad(String ignored) throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(SUGGESTION_ITEMS));
        Thread.sleep(500);
    }
}
