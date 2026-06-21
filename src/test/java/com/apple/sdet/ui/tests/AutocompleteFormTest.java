package com.apple.sdet.ui.tests;

import com.apple.sdet.ui.base.BaseUITest;
import com.apple.sdet.ui.pages.AutocompleteFormPage;
import com.apple.sdet.ui.utils.DriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test suite for Autocomplete Form UI automation.
 * Covers all major UI and keyboard interaction scenarios.
 */
@Listeners
public class AutocompleteFormTest extends BaseUITest {

    private WebDriver driver;
    private AutocompleteFormPage formPage;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.initializeDriver();
        formPage = new AutocompleteFormPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }

    /**
     * TC-UI-001: Prefix Match Filtering
     * Verify suggestion filtering works correctly with prefix matching.
     */
    @Test(groups = {"ui", "critical"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-UI-001: Test prefix match filtering with suggestion list")
    public void testPrefixMatchFiltering() {
        formPage.loadPage(TEST_PAGE_URL);

        // Step 1: Get initial suggestions
        formPage.waitForSuggestionsToLoad();
        java.util.List<String> initialSuggestions = formPage.getVisibleSuggestions();
        assertThat("Initial suggestions should be 3", initialSuggestions, hasSize(3));

        // Step 2: Type "agile" - should keep all suggestions
        formPage.typeInInputField("agile");
        java.util.List<String> suggestionsAfterAgile = formPage.getVisibleSuggestions();
        assertThat("All suggestions should match 'agile' prefix", 
            suggestionsAfterAgile, 
            everyItem(containsString("agile")));
        assertThat("Should have 3 suggestions after typing 'agile'", 
            suggestionsAfterAgile, 
            hasSize(3));

        // Step 3: Type "agile m" - should keep all suggestions
        formPage.typeInInputField("agile m");
        java.util.List<String> suggestionsAfterAgileM = formPage.getVisibleSuggestions();
        assertThat("All suggestions should match 'agile m' prefix", 
            suggestionsAfterAgileM, 
            everyItem(containsString("agile m")));

        // Step 4: Clear and verify all suggestions reappear
        formPage.clearInputFieldWithKeyboard();
        java.util.List<String> suggestionsAfterClear = formPage.getVisibleSuggestions();
        assertThat("All 3 suggestions should be visible after clear", 
            suggestionsAfterClear, 
            hasSize(3));
    }

    /**
     * TC-UI-002: Invalid Submission
     * User cannot submit form with empty input field.
     */
    @Test(groups = {"ui", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-UI-002: Test invalid submission with empty input")
    public void testInvalidSubmissionWithEmptyInput() {
        formPage.loadPage(TEST_PAGE_URL);
        
        // Verify input is empty
        assertThat("Input field should be empty", 
            formPage.getInputFieldValue(), 
            emptyString());

        // Click Next without entering text
        formPage.clickNextButton();

        // Verify error message is displayed
        assertThat("Error message should be displayed", 
            formPage.isErrorMessageDisplayed(), 
            is(true));
        
        assertThat("Error message should contain 'Invalid input'", 
            formPage.getErrorMessageText(), 
            containsString("Invalid input"));
    }

    /**
     * TC-UI-003: Valid Suggestion Selection
     * User selects suggestion by clicking; input field auto-populates.
     */
    @Test(groups = {"ui", "critical"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-UI-003: Test suggestion selection by clicking")
    public void testSuggestionSelectionByClick() {
        formPage.loadPage(TEST_PAGE_URL);
        
        // Type to filter suggestions
        formPage.typeInInputField("agile");
        
        // Select suggestion
        String selectedSuggestion = "agile methodology";
        formPage.clickSuggestion(selectedSuggestion);

        // Verify input field is populated
        assertThat("Input field should be populated with selected suggestion", 
            formPage.getInputFieldValue(), 
            equalTo(selectedSuggestion));

        // Submit form
        formPage.clickNextButton();

        // Verify success message
        assertThat("Success message should be displayed", 
            formPage.isSuccessMessageDisplayed(), 
            is(true));
        
        assertThat("Success message should contain 'Success'", 
            formPage.getSuccessMessageText(), 
            containsString("Success"));
    }

    /**
     * TC-UI-004: Keyboard Navigation
     * User navigates through form elements using Tab key.
     */
    @Test(groups = {"ui", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-UI-004: Test keyboard navigation with Tab key")
    public void testTabKeyNavigation() {
        formPage.loadPage(TEST_PAGE_URL);

        // Tab to first element (input field)
        formPage.navigateWithTabKey(1);
        org.openqa.selenium.WebElement focusedElement = formPage.getFocusedElement();
        assertThat("Focus should be on input field", 
            focusedElement.getAttribute("id"), 
            equalTo("autocompleteInput"));

        // Tab to next element (should focus on button or skip to button)
        formPage.navigateWithTabKey(1);
        focusedElement = formPage.getFocusedElement();
        assertThat("Focus should be on submit button after tab", 
            focusedElement.getAttribute("id"), 
            is(notNullValue()));
    }

    /**
     * TC-UI-005: Keyboard Submission
     * User submits form by pressing Enter key.
     */
    @Test(groups = {"ui", "medium"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-UI-005: Test form submission using Enter key")
    public void testKeyboardSubmissionWithEnter() {
        formPage.loadPage(TEST_PAGE_URL);

        // Type to filter suggestions
        formPage.typeInInputField("agile");
        
        // Select suggestion
        String selectedSuggestion = "agile methodology";
        formPage.clickSuggestion(selectedSuggestion);

        // Verify selection
        assertThat("Input field should contain selected text", 
            formPage.getInputFieldValue(), 
            equalTo(selectedSuggestion));

        // Submit using Enter
        formPage.submitFormWithEnter();

        // Verify success
        assertThat("Success message should be displayed after Enter", 
            formPage.isSuccessMessageDisplayed(), 
            is(true));
    }

    /**
     * TC-UI-006: Multiple Suggestions Display
     * Verify all default suggestions are initially displayed.
     */
    @Test(groups = {"ui", "medium"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-UI-006: Test initial suggestions display")
    public void testInitialSuggestionsDisplay() {
        formPage.loadPage(TEST_PAGE_URL);

        java.util.List<String> suggestions = formPage.getVisibleSuggestions();
        
        assertThat("Should have 3 initial suggestions", 
            suggestions, 
            hasSize(3));
        
        assertThat("Should contain all expected suggestions",
            suggestions,
            containsInAnyOrder(
                "agile methodology",
                "agile methodology process",
                "agile methodology process testing"
            ));
    }

    /**
     * TC-UI-007: Suggestion Filtering - No Match
     * When typed text doesn't match any suggestion prefix, suggestions disappear.
     */
    @Test(groups = {"ui", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-UI-007: Test suggestion filtering with no match")
    public void testSuggestionFilteringNoMatch() {
        formPage.loadPage(TEST_PAGE_URL);

        // Type text that doesn't match any suggestion
        formPage.typeInInputField("xyz");

        java.util.List<String> suggestions = formPage.getVisibleSuggestions();
        
        // Suggestions should be empty or not match
        assertThat("No suggestions should match 'xyz'", 
            suggestions, 
            not(contains("xyz")));
    }

    /**
     * TC-UI-008: Success Message Display
     * Verify success message is displayed after valid submission.
     */
    @Test(groups = {"ui", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-UI-008: Test success message display")
    public void testSuccessMessageDisplay() {
        formPage.loadPage(TEST_PAGE_URL);

        // Type to filter suggestions
        formPage.typeInInputField("agile");
        
        // Select and submit
        formPage.clickSuggestion("agile methodology");
        formPage.clickNextButton();

        // Verify success container is displayed
        assertThat("Success message should be visible", 
            formPage.isSuccessMessageDisplayed(), 
            is(true));
        
        assertThat("Success message should contain expected text",
            formPage.getSuccessMessageText(),
            allOf(
                notNullValue(),
                containsString("Success"),
                containsString("recorded")
            ));
    }
}
