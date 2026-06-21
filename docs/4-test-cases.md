# Task 4: Detailed Test Cases

## Test Environment Setup
- **Browser**: Chrome on Windows 10
- **Language**: English
- **Test User**: test123@gmail.com
- **User Location**: India (IST, UTC+05:30)
- **Base URL**: https://test.com/autocomplete-form

---

## Test Case 1: Prefix Match Filtering - Valid Selection

**Test Case ID**: TC-UI-001  
**Title**: Verify suggestion filtering works correctly with prefix matching  
**Category**: UI - Autocomplete Functionality  
**Risk Level**: CRITICAL

### Preconditions
- User is logged in with test123@gmail.com
- Form page is loaded successfully
- All 3 default suggestions are visible:
  - "agile methodology"
  - "agile methodology process"
  - "agile methodology process testing"

### Test Steps
1. Click on the text input field (id="input-field")
2. Type "agile" (5 characters)
3. Wait for suggestion list to update (max 2 seconds)
4. Count visible suggestions
5. Verify each visible suggestion text content
6. Type additional character "m" to make "agile m"
7. Verify suggestions are filtered again
8. Clear input by selecting all and deleting
9. Verify all 3 suggestions reappear

### Expected Results
- **After Step 2 (typing "agile")**:
  - All 3 suggestions remain visible (all start with "agile")
  - Input field shows "agile"

- **After Step 6 (typing "agile m")**:
  - All 3 suggestions remain visible (all contain "agile m")
  - Visual highlight shows prefix match

- **After Step 9 (clear input)**:
  - All 3 original suggestions visible again
  - Input field is empty

### Test Data
- Input: "agile", "agile m", "" (empty)
- Suggestions File: default_suggestions.json

### Expected Behavior Timeline
| Time | Action | Expected Suggestion Count |
|------|--------|--------------------------|
| 0s | Page load | 3 visible |
| 1s | Type "agile" | 3 visible |
| 2s | Type "agile m" | 3 visible |
| 3s | Clear input | 3 visible |

---

## Test Case 2: Invalid Submission - Empty Input

**Test Case ID**: TC-UI-002  
**Title**: User cannot submit form with empty input field  
**Category**: UI - Form Validation  
**Risk Level**: HIGH

### Preconditions
- User is logged in with test123@gmail.com
- Form page is loaded with empty input field
- Next button (id="next-button") is visible and clickable
- Error message element exists but is hidden initially

### Test Steps
1. Verify input field is empty
2. Click Next button without entering any text
3. Wait for response (max 3 seconds)
4. Check if error message is displayed
5. Verify error message text contains "Invalid input"
6. Check that input field still has focus
7. Verify no API call was made (via network tab or response validation)

### Expected Results
- Error message element is visible with text: "Error: Invalid input. Please select a valid suggestion."
- Input field retains focus
- No form submission occurs
- No success message is displayed
- Form remains on same page

### Test Data
- Invalid Input: "" (empty string)
- Expected Error Message: "Error: Invalid input. Please select a valid suggestion."

### API Expectations
- No POST request sent to backend
- Form state is unchanged

---

## Test Case 3: Valid Suggestion Selection via Click

**Test Case ID**: TC-UI-003  
**Title**: User selects suggestion by clicking; input field auto-populates  
**Category**: UI - Suggestion Selection  
**Risk Level**: CRITICAL

### Preconditions
- User is logged in with test123@gmail.com
- Form page is fully loaded
- All suggestions are visible in suggestion list (ul.suggestions)
- No text has been typed yet

### Test Steps
1. Locate suggestion list (class="suggestions")
2. Identify first suggestion item: "agile methodology"
3. Click on first suggestion element
4. Wait for input field to update (max 1 second)
5. Verify input field value equals selected suggestion text
6. Verify suggestion list is still visible
7. Click Next button
8. Wait for API response (max 3 seconds)

### Expected Results
- Input field auto-populated with "agile methodology"
- Suggestion list remains visible
- Form submission succeeds (HTTP 200)
- Success message displayed: "Success! Your response has been recorded."
- Input field value is sent to API as "text" property

### Test Data
- Selected Suggestion: "agile methodology" (first item)
- Expected Input Value: "agile methodology"

### API Expectations
```json
{
  "account_id": "98765",
  "account_email": "test123@gmail.com",
  "text": "agile methodology",
  "suggestion_list": "agile methodology, agile methodology process, agile methodology process testing",
  "completed": true
}
```

---

## Test Case 4: Keyboard Navigation with Tab Key

**Test Case ID**: TC-UI-004  
**Title**: User navigates through form elements using Tab key; focus transitions correctly  
**Category**: UI - Accessibility  
**Risk Level**: HIGH

### Preconditions
- User is logged in with test123@gmail.com
- Form page is loaded
- Browser is Chrome on Windows 10
- No form element has focus initially

### Test Steps
1. Press Tab key once
2. Verify focus moves to input field (id="input-field")
3. Press Tab key second time
4. Verify focus moves to first suggestion or next interactive element
5. Press Tab key multiple times to reach Next button
6. Verify Next button has focus (visible outline)
7. Press Tab key to cycle through all form elements

### Expected Results
- Tab navigation order: input field → suggestions (or skip) → Next button → (loops back to input)
- Each element shows visible focus indicator
- Focus cycles through interactive elements in logical order
- No elements are skipped in tab order

### Test Data
- Keyboard Input: Tab key (multiple presses)
- Focus Elements: [input-field, next-button]

### Accessibility Compliance
- Meets WCAG 2.1 AA keyboard navigation requirements
- Focus indicators are clearly visible

---

## Test Case 5: Keyboard Submission with Enter Key

**Test Case ID**: TC-UI-005  
**Title**: User submits form by pressing Enter key; submission succeeds  
**Category**: UI - Keyboard Interaction  
**Risk Level**: MEDIUM

### Preconditions
- User is logged in with test123@gmail.com
- Form page is loaded
- Input field has focus
- Valid text "agile methodology" is entered

### Test Steps
1. Click input field to give it focus
2. Type "agile" (partial match)
3. Click first suggestion "agile methodology"
4. Verify input field contains "agile methodology"
5. Press Enter key
6. Wait for API response (max 3 seconds)
7. Verify success message is displayed

### Expected Results
- Form submits successfully (Enter key behaves like Next button click)
- HTTP 200 response received from API
- Success message displayed: "Success! Your response has been recorded."
- API payload contains correct text value

### Test Data
- Selected Text: "agile methodology"
- Keyboard Input: Enter key

### API Expectations
- Form submission succeeds
- Response includes "completed": true

---

## Test Case 6: API Response Schema Validation

**Test Case ID**: TC-API-001  
**Title**: Verify API response matches exact data contract (schema validation)  
**Category**: API - Contract  
**Risk Level**: HIGH

### Preconditions
- Test user test123@gmail.com has completed form with selection "agile methodology"
- API endpoint: /api/form-response
- Response is available via GET request with form_id parameter

### Test Steps
1. Make GET request to API: `/api/form-response?form_id={form_id}`
2. Verify HTTP status code is 200
3. Parse JSON response
4. Verify all required fields exist:
   - account_id
   - account_email
   - start_date
   - end_date
   - locale
   - text
   - suggestion_list
   - completed
5. Verify no extra unexpected fields are present
6. Compare field names (case-sensitive) with specification

### Expected Results
- Response contains exactly 8 fields (no more, no less)
- Field names match specification exactly:
  ```
  account_id, account_email, start_date, end_date,
  locale, text, suggestion_list, completed
  ```
- No additional or misspelled fields
- Response structure matches specification in FR-05

### Test Data
- API Endpoint: /api/form-response
- Method: GET
- Expected Fields: 8 (as per FR-05)

### Validation Rules
- Field names are case-sensitive
- Schema must match exactly (no extra fields)
- All fields are mandatory

---

## Test Case 7: API Response Data Type Validation

**Test Case ID**: TC-API-002  
**Title**: Verify API response field types are correct (boolean, timestamp, string)  
**Category**: API - Data Type  
**Risk Level**: HIGH

### Preconditions
- API response is received from form submission
- Response is valid JSON

### Test Steps
1. Retrieve API response from form submission
2. Verify data types for each field:
   - account_id: Must be String type
   - account_email: Must be String type in valid email format
   - start_date: Must be String in ISO 8601 format
   - end_date: Must be String in ISO 8601 format
   - locale: Must be String type
   - text: Must be String type
   - suggestion_list: Must be String type (comma-separated values)
   - completed: Must be Boolean type (not String)
3. Verify no type coercion issues

### Expected Results
- All date fields use ISO 8601 format: YYYY-MM-DDTHH:MM:SS±HH:MM
- All text fields are String type
- completed field is Boolean type: `true` or `false` (not `"true"` or `"false"`)
- All fields parse without type errors

### Test Data
```json
{
  "account_id": "String",
  "account_email": "String (email format)",
  "start_date": "ISO 8601 String",
  "end_date": "ISO 8601 String",
  "locale": "String",
  "text": "String",
  "suggestion_list": "String (comma-separated)",
  "completed": "Boolean"
}
```

---

## Test Case 8: API Locale Format Validation (IETF BCP 47)

**Test Case ID**: TC-API-003  
**Title**: Verify locale field matches IETF BCP 47 format with region code  
**Category**: API - Data Format  
**Risk Level**: HIGH

### Preconditions
- API response received from form submission by user in India
- User's browser locale is set to English
- User's timezone is IST (UTC+05:30)

### Test Steps
1. Retrieve API response
2. Extract locale field value
3. Validate locale format: [language]-[region] (e.g., en-IN)
4. Verify language code is ISO 639-1 (2-letter): "en"
5. Verify region code is ISO 3166-1 alpha-2 (2-letter): "IN"
6. Verify case format: lowercase language, uppercase region

### Expected Results
- Locale field value: "en-IN"
- Format: lowercase language hyphen uppercase region
- Not "en" (missing region code)
- Not "EN-in" (incorrect case)
- Not "eng-IND" (incorrect format)

### Test Data
- Expected Locale: "en-IN"
- User Location: India
- Compliance: IETF BCP 47 standard

### Validation Rules
```
Valid: en-IN, en-US, fr-FR, de-DE
Invalid: en, EN-in, eng-IND, en_IN
```

---

## Test Case 9: Negative Test - Missing Required Fields in API Response

**Test Case ID**: TC-API-004  
**Title**: API response with missing required fields should be rejected  
**Category**: API - Negative Test  
**Risk Level**: HIGH

### Preconditions
- Simulate API returning incomplete response
- Mock API endpoint returns response missing "completed" field

### Test Steps
1. Mock API response without required field "completed"
2. Attempt to deserialize response into ResponseDTO
3. Capture any validation errors
4. Verify mandatory field validation triggers

### Expected Results
- Response validation fails with error: "Field 'completed' is required"
- Application rejects incomplete response
- No partial data is processed
- Error is logged for investigation

### Test Data
```json
{
  "account_id": "98765",
  "account_email": "test123@gmail.com",
  "start_date": "2024-03-15T10:30:00Z",
  "end_date": "2024-03-15T10:32:00Z",
  "locale": "en-IN",
  "text": "agile methodology",
  "suggestion_list": "agile methodology, agile methodology process, agile methodology process testing"
}
```
(Missing: "completed" field)

### Expected Validation Error
- "Field 'completed' is required and cannot be null"
- HTTP response code should be 400 (Bad Request)

---

## Test Case 10: Negative Test - Invalid Timestamp Format

**Test Case ID**: TC-API-005  
**Title**: API response with invalid timestamp format should fail validation  
**Category**: API - Negative Test  
**Risk Level**: MEDIUM

### Preconditions
- Mock API response with malformed timestamp
- Timestamp doesn't match ISO 8601 format

### Test Steps
1. Create response with invalid start_date: "15-03-2024" (wrong format)
2. Attempt to parse and validate
3. Capture validation error
4. Verify timestamp parser rejects invalid format

### Expected Results
- Timestamp validation fails
- Error message: "Invalid timestamp format. Expected ISO 8601: YYYY-MM-DDTHH:MM:SS±HH:MM"
- Response is rejected
- Application handles error gracefully

### Test Data
```json
Invalid timestamp formats:
- "15-03-2024" (DD-MM-YYYY)
- "03/15/2024" (MM/DD/YYYY)
- "2024-03-15" (missing time)
- "2024-03-15 10:30:00" (space instead of T)
```

Valid format:
- "2024-03-15T10:30:00+05:30"
- "2024-03-15T10:30:00Z"

---

## Test Coverage Summary

| Category | Count | Test IDs |
|----------|-------|----------|
| UI Tests | 5 | TC-UI-001 to TC-UI-005 |
| API Tests | 5 | TC-API-001 to TC-API-005 |
| **Total** | **10** | - |

### Coverage by Risk Level
- CRITICAL: 3 test cases (TC-UI-001, TC-UI-003, TC-API-001)
- HIGH: 5 test cases (TC-UI-002, TC-UI-004, TC-API-002, TC-API-003, TC-API-004)
- MEDIUM: 2 test cases (TC-UI-005, TC-API-005)
