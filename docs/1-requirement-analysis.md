# Task 1: Top 10 Test Scenarios - Risk Analysis

## Risk Assessment Methodology
- **Critical**: Core functionality failure; affects form submission or data persistence
- **High**: Major functional defect; impacts user experience or data accuracy
- **Medium**: Moderate functionality impact; workarounds available
- **Low**: Minor issues; UI polish; edge cases with limited user impact

---

## Top 10 Test Scenarios (Ranked by Risk)

### 1. Form Submission with Valid Selection
**Summary**: User selects suggestion from list and clicks Next; form submits successfully with correct API payload  
**Risk Level**: **CRITICAL**  
**Rationale**: Form submission is the core business function; failure blocks all users from completing the task and persisting responses.

---

### 2. API Response Data Persistence Validation
**Summary**: Verify API stores all required fields (account_id, email, timestamps, locale, text, suggestion_list, completed flag)  
**Risk Level**: **CRITICAL**  
**Rationale**: Incorrect data storage corrupts analytics, reporting, and user tracking; directly impacts business intelligence and audit trails.

---

### 3. Suggestion Filtering - Prefix Match (Default Mode)
**Summary**: Typed characters match suggestion start; only matching suggestions remain visible  
**Risk Level**: **HIGH**  
**Rationale**: Primary filtering mode; if broken, suggestions display incorrectly, confusing users and breaking core autocomplete functionality.

---

### 4. Invalid Form Submission Handling
**Summary**: User submits form with invalid input (empty field or non-selected text); error message displays  
**Risk Level**: **HIGH**  
**Rationale**: Form validation bypass would allow invalid data to persist, contaminating datasets and violating business rules.

---

### 5. Suggestion Filtering - Match Anywhere (Configurable Mode)
**Summary**: When enabled, suggestions contain typed text anywhere; all matching suggestions remain visible  
**Risk Level**: **HIGH**  
**Rationale**: Configurable mode affects user experience; incorrect filtering frustrates users and reduces form completion rates.

---

### 6. Keyboard Navigation (Tab Key)
**Summary**: User tabs through form elements (input → suggestions → button); focus transitions correctly  
**Risk Level**: **HIGH**  
**Rationale**: Keyboard accessibility is WCAG requirement; inaccessible forms violate compliance standards and exclude users with disabilities.

---

### 7. API Response Schema Validation
**Summary**: API response matches exact data contract (correct field names, types, format)  
**Risk Level**: **HIGH**  
**Rationale**: Schema mismatch breaks downstream systems consuming this API; causes integration failures and data pipeline breaks.

---

### 8. Keyboard Submission (Enter Key)
**Summary**: User presses Enter to submit form; submission succeeds with same result as button click  
**Risk Level**: **MEDIUM**  
**Rationale**: Alternative submission path; missing Enter support reduces accessibility but has workaround (button click).

---

### 9. Locale & Timezone Handling
**Summary**: API response includes correct locale (en-IN) and timestamps in user's local timezone (IST)  
**Risk Level**: **MEDIUM**  
**Rationale**: Incorrect timezone causes timestamp misalignment in analytics; affects reporting accuracy but not functionality.

---

### 10. Suggestion Selection - Direct Click
**Summary**: User clicks suggestion item; input field auto-populates with selected text  
**Risk Level**: **MEDIUM**  
**Rationale**: Alternative to typing; enhances UX but has workaround (manual typing); impacts usability over functionality.

---

## Risk Matrix Summary

| Risk Level | Count | Functional Areas |
|-----------|-------|-----------------|
| CRITICAL  | 2     | Form Submission, API Persistence |
| HIGH      | 5     | Filtering, Validation, Schema, Accessibility |
| MEDIUM    | 3     | Alternate Inputs, Localization |
| LOW       | 0     | - |

**Total Scenarios**: 10
