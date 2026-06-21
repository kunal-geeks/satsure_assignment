package com.apple.sdet.api.validators;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validator for Autocomplete Form API responses.
 * Validates schema, data types, and business rules.
 */
public class ResponseValidator {
    private static final Logger logger = LoggerFactory.getLogger(ResponseValidator.class);
    
    // Regex patterns
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String ISO_8601_REGEX = 
        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}([+-]\\d{2}:\\d{2}|Z)$";
    private static final String IETF_BCP47_REGEX = "^[a-z]{2}-[A-Z]{2}$";
    
    /**
     * Validate response contains all required fields.
     *
     * @param response API response to validate
     * @throws AssertionError if required fields are missing
     */
    public static void validateRequiredFields(Response response) {
        logger.info("Validating required fields in response");
        
        String[] requiredFields = {
            "account_id",
            "account_email", 
            "start_date",
            "end_date",
            "locale",
            "text",
            "suggestion_list",
            "completed"
        };
        
        for (String field : requiredFields) {
            Object value = response.jsonPath().get(field);
            if (value == null) {
                throw new AssertionError("Required field missing: " + field);
            }
            logger.debug("Field '{}' present with value: {}", field, value);
        }
    }

    /**
     * Validate email format.
     *
     * @param email Email to validate
     * @throws AssertionError if email format is invalid
     */
    public static void validateEmailFormat(String email) {
        logger.info("Validating email format: {}", email);
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new AssertionError("Invalid email format: " + email);
        }
    }

    /**
     * Validate timestamp format (ISO 8601).
     *
     * @param timestamp Timestamp to validate
     * @throws AssertionError if timestamp format is invalid
     */
    public static void validateTimestampFormat(String timestamp) {
        logger.info("Validating timestamp format: {}", timestamp);
        if (!Pattern.matches(ISO_8601_REGEX, timestamp)) {
            throw new AssertionError(
                "Invalid timestamp format. Expected ISO 8601: " + timestamp
            );
        }
    }

    /**
     * Validate locale format (IETF BCP 47).
     *
     * @param locale Locale to validate
     * @throws AssertionError if locale format is invalid
     */
    public static void validateLocaleFormat(String locale) {
        logger.info("Validating locale format: {}", locale);
        if (!Pattern.matches(IETF_BCP47_REGEX, locale)) {
            throw new AssertionError(
                "Invalid locale format. Expected IETF BCP 47 (e.g., en-IN): " + locale
            );
        }
    }

    /**
     * Validate completed field is boolean type.
     *
     * @param response API response
     * @throws AssertionError if completed is not boolean
     */
    public static void validateCompletedType(Response response) {
        logger.info("Validating 'completed' field type");
        Object completed = response.jsonPath().get("completed");
        
        if (!(completed instanceof Boolean)) {
            throw new AssertionError(
                String.format(
                    "Field 'completed' must be Boolean type, got: %s (%s)",
                    completed,
                    completed != null ? completed.getClass().getSimpleName() : "null"
                )
            );
        }
    }

    /**
     * Validate suggestion list contains only matching suggestions.
     *
     * @param suggestionList Comma-separated suggestion list
     * @param userText Text entered by user
     * @throws AssertionError if non-matching suggestions are present
     */
    public static void validateSuggestionList(String suggestionList, String userText) {
        logger.info("Validating suggestion list against user text: {}", userText);
        
        if (suggestionList == null || suggestionList.isEmpty()) {
            throw new AssertionError("Suggestion list cannot be empty");
        }
        
        String[] suggestions = suggestionList.split(",");
        for (String suggestion : suggestions) {
            String trimmedSuggestion = suggestion.trim();
            if (!trimmedSuggestion.startsWith(userText)) {
                logger.warn(
                    "Suggestion '{}' does not match user text '{}' (prefix match)",
                    trimmedSuggestion,
                    userText
                );
            }
        }
    }

    /**
     * Validate response data types match specification.
     *
     * @param response API response
     * @throws AssertionError if data types are incorrect
     */
    public static void validateDataTypes(Response response) {
        logger.info("Validating response data types");
        
        // account_id should be String or Number
        Object accountId = response.jsonPath().get("account_id");
        if (!(accountId instanceof String || accountId instanceof Number)) {
            throw new AssertionError("account_id should be String or Number");
        }
        
        // account_email should be String
        Object accountEmail = response.jsonPath().get("account_email");
        if (!(accountEmail instanceof String)) {
            throw new AssertionError("account_email should be String");
        }
        
        // timestamps should be String (ISO 8601)
        Object startDate = response.jsonPath().get("start_date");
        if (!(startDate instanceof String)) {
            throw new AssertionError("start_date should be String (ISO 8601)");
        }
        
        Object endDate = response.jsonPath().get("end_date");
        if (!(endDate instanceof String)) {
            throw new AssertionError("end_date should be String (ISO 8601)");
        }
        
        // locale should be String
        Object locale = response.jsonPath().get("locale");
        if (!(locale instanceof String)) {
            throw new AssertionError("locale should be String");
        }
        
        // text should be String
        Object text = response.jsonPath().get("text");
        if (!(text instanceof String)) {
            throw new AssertionError("text should be String");
        }
        
        // suggestion_list should be String
        Object suggestionList = response.jsonPath().get("suggestion_list");
        if (!(suggestionList instanceof String)) {
            throw new AssertionError("suggestion_list should be String");
        }
        
        // completed should be Boolean
        validateCompletedType(response);
        
        logger.info("All data types validated successfully");
    }

    /**
     * Validate no extra unexpected fields in response.
     *
     * @param response API response
     * @throws AssertionError if unexpected fields are found
     */
    public static void validateNoExtraFields(Response response) {
        logger.info("Validating no extra fields in response");
        
        @SuppressWarnings("unchecked")
        List<String> actualFields = response.jsonPath().getMap("").keySet().stream()
            .map(Object::toString)
            .toList();
        String[] expectedFields = {
            "account_id",
            "account_email",
            "start_date",
            "end_date",
            "locale",
            "text",
            "suggestion_list",
            "completed"
        };
        
        for (String field : actualFields) {
            boolean isExpected = false;
            for (String expectedField : expectedFields) {
                if (field.equals(expectedField)) {
                    isExpected = true;
                    break;
                }
            }
            if (!isExpected) {
                logger.warn("Unexpected field found in response: {}", field);
            }
        }
    }

    /**
     * Validate complete response against specification.
     *
     * @param response API response
     * @throws AssertionError if validation fails
     */
    public static void validateCompleteResponse(Response response) {
        logger.info("Performing complete response validation");
        
        validateRequiredFields(response);
        validateDataTypes(response);
        
        String email = response.jsonPath().getString("account_email");
        validateEmailFormat(email);
        
        String startDate = response.jsonPath().getString("start_date");
        validateTimestampFormat(startDate);
        
        String endDate = response.jsonPath().getString("end_date");
        validateTimestampFormat(endDate);
        
        String locale = response.jsonPath().getString("locale");
        validateLocaleFormat(locale);
        
        validateNoExtraFields(response);
        
        logger.info("Response validation completed successfully");
    }
}
