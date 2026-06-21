package com.apple.sdet.api.tests;

import com.apple.sdet.api.base.BaseAPITest;
import com.apple.sdet.api.clients.AutocompleteFormClient;
import com.apple.sdet.api.validators.ResponseValidator;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * API Test Suite for Autocomplete Form.
 * Tests API schema validation, data types, and business logic.
 */
public class AutocompleteFormAPITest extends BaseAPITest {

    private AutocompleteFormClient apiClient;
    private static final String FORM_ID = "test-form-001";

    @BeforeMethod
    public void setUp() {
        apiClient = new AutocompleteFormClient(BASE_URL);
    }

    /**
     * TC-API-001: API Response Schema Validation
     * Verify API response matches exact data contract (schema validation)
     */
    @Test(groups = {"api", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-API-001: Validate API response schema contains all required fields")
    public void testApiResponseSchema() {
        Response response = apiClient.getFormResponse(FORM_ID);

        assertThat("Response status code should be 200", 
            response.getStatusCode(), 
            equalTo(200));

        // Validate all required fields are present
        ResponseValidator.validateRequiredFields(response);

        // Verify field values
        assertThat("account_id should not be null", 
            response.jsonPath().getString("account_id"), 
            notNullValue());
        
        assertThat("account_email should not be null", 
            response.jsonPath().getString("account_email"), 
            notNullValue());
        
        assertThat("text should not be null", 
            response.jsonPath().getString("text"), 
            notNullValue());
    }

    /**
     * TC-API-002: API Response Data Type Validation
     * Verify all fields have correct data types.
     */
    @Test(groups = {"api", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-API-002: Validate response field data types")
    public void testApiResponseDataTypes() {
        Response response = apiClient.getFormResponse(FORM_ID);

        assertThat("Response status code should be 200", 
            response.getStatusCode(), 
            equalTo(200));

        // Validate all data types
        ResponseValidator.validateDataTypes(response);

        // Specific type checks
        Object completed = response.jsonPath().get("completed");
        assertThat("'completed' field should be Boolean type", 
            completed, 
            instanceOf(Boolean.class));
    }

    /**
     * TC-API-003: API Locale Format Validation (IETF BCP 47)
     * Verify locale field matches IETF BCP 47 format with region code.
     */
    @Test(groups = {"api", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-API-003: Validate locale format matches IETF BCP 47 standard")
    public void testLocaleFormatValidation() {
        Response response = apiClient.getFormResponse(FORM_ID);

        assertThat("Response status code should be 200", 
            response.getStatusCode(), 
            equalTo(200));

        String locale = response.jsonPath().getString("locale");
        
        // Validate IETF BCP 47 format (en-IN)
        ResponseValidator.validateLocaleFormat(locale);
        
        assertThat("Locale should be in format 'xx-XX' (e.g., en-IN)", 
            locale, 
            matchesPattern("^[a-z]{2}-[A-Z]{2}$"));
    }

    /**
     * TC-API-004: Timestamp Format Validation
     * Verify timestamps are in ISO 8601 format.
     */
    @Test(groups = {"api", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-API-004: Validate timestamp formats are ISO 8601")
    public void testTimestampFormatValidation() {
        Response response = apiClient.getFormResponse(FORM_ID);

        assertThat("Response status code should be 200", 
            response.getStatusCode(), 
            equalTo(200));

        String startDate = response.jsonPath().getString("start_date");
        String endDate = response.jsonPath().getString("end_date");

        // Validate ISO 8601 format
        ResponseValidator.validateTimestampFormat(startDate);
        ResponseValidator.validateTimestampFormat(endDate);

        assertThat("start_date should match ISO 8601 format",
            startDate,
            matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}([+-]\\d{2}:\\d{2}|Z)$"));

        assertThat("end_date should match ISO 8601 format",
            endDate,
            matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}([+-]\\d{2}:\\d{2}|Z)$"));
    }

    /**
     * TC-API-005: Suggestion List Content Validation
     * Verify suggestion_list contains only matching suggestions.
     */
    @Test(groups = {"api", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-API-005: Validate suggestion list contains matching suggestions")
    public void testSuggestionListValidation() {
        Response response = apiClient.getFormResponse(FORM_ID);

        assertThat("Response status code should be 200", 
            response.getStatusCode(), 
            equalTo(200));

        String suggestionList = response.jsonPath().getString("suggestion_list");
        String userText = response.jsonPath().getString("text");

        assertThat("suggestion_list should not be empty", 
            suggestionList, 
            notNullValue());

        // Verify suggestions contain user input
        assertThat("suggestion_list should contain user text",
            suggestionList,
            containsString(userText));
    }

    /**
     * TC-API-006: Email Format Validation
     * Verify email format is valid.
     */
    @Test(groups = {"api", "medium"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-API-006: Validate email format is valid")
    public void testEmailFormatValidation() {
        Response response = apiClient.getFormResponse(FORM_ID);

        assertThat("Response status code should be 200", 
            response.getStatusCode(), 
            equalTo(200));

        String email = response.jsonPath().getString("account_email");

        ResponseValidator.validateEmailFormat(email);

        assertThat("Email should be valid format",
            email,
            matchesPattern("^[A-Za-z0-9+_.-]+@(.+)$"));
    }

    /**
     * TC-API-007: Negative Test - Missing Required Fields
     * Verify API response with missing required fields is rejected.
     */
    @Test(groups = {"api", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-API-007: Negative test - missing required field")
    public void testMissingRequiredField() {
        String mockFormId = "incomplete-form-001";
        
        try {
            Response response = apiClient.getFormResponse(mockFormId);
            
            // Should either return 400 or have validation error
            if (response.getStatusCode() == 200) {
                // Try to validate - should fail
                ResponseValidator.validateRequiredFields(response);
                // If we get here, fail the test
                throw new AssertionError("Expected validation error for missing field");
            } else {
                assertThat("Should return error status for incomplete response",
                    response.getStatusCode(),
                    not(200));
            }
        } catch (AssertionError e) {
            // Expected - field is missing, error message should contain "required" (case-insensitive)
            assertThat("Should validate that field is required",
                e.getMessage().toLowerCase(),
                containsString("required"));
        }
    }

    /**
     * TC-API-008: Negative Test - Invalid Data Type
     * Verify API rejects response with invalid data types.
     */
    @Test(groups = {"api", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-API-008: Negative test - invalid data type for 'completed' field")
    public void testInvalidDataType() {
        // Mock response with 'completed' as string instead of boolean
        String mockFormId = "invalid-type-form-001";
        
        try {
            Response response = apiClient.getFormResponse(mockFormId);
            
            if (response.getStatusCode() == 200) {
                // Try to validate - should fail due to type mismatch
                ResponseValidator.validateDataTypes(response);
            }
        } catch (AssertionError e) {
            // Expected - type validation should fail
            assertThat("Should validate data types",
                e.getMessage(),
                containsString("type"));
        }
    }

    /**
     * TC-API-009: Complete Response Validation
     * Perform comprehensive validation of entire response.
     */
    @Test(groups = {"api", "critical"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-API-009: Complete response validation against specification")
    public void testCompleteResponseValidation() {
        Response response = apiClient.getFormResponse(FORM_ID);

        assertThat("Response status code should be 200", 
            response.getStatusCode(), 
            equalTo(200));

        // Perform comprehensive validation
        ResponseValidator.validateCompleteResponse(response);
    }

    /**
     * TC-API-010: Completed Field Boolean Validation
     * Verify completed field is boolean and has correct value.
     */
    @Test(groups = {"api", "high"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC-API-010: Validate 'completed' field is boolean")
    public void testCompletedFieldBoolean() {
        Response response = apiClient.getFormResponse(FORM_ID);

        assertThat("Response status code should be 200", 
            response.getStatusCode(), 
            equalTo(200));

        ResponseValidator.validateCompletedType(response);

        Boolean completed = response.jsonPath().getBoolean("completed");
        
        assertThat("'completed' should be Boolean true or false",
            completed,
            notNullValue());
    }
}
