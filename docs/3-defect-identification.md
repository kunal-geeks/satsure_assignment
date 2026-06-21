# Task 3: Defect Identification - API Response Discrepancies

## Requirement: FR-05 Backend Data Contract

The persisted response MUST contain the following properties:

| Property | Description | Requirements |
|----------|-------------|--------------|
| account_id | ID of user account | Unique identifier |
| account_email | Email of user account | Valid email format |
| start_date | Timestamp when user reached form | User's local time |
| end_date | Timestamp when user selected Next | User's local time |
| locale | User's locale | IETF BCP 47 format (e.g., en-IN) |
| text | Text given by user | Exact text entered/selected |
| suggestion_list | Comma-separated matching suggestions | Only MATCHING suggestions, not all |
| completed | Boolean status of upload | Boolean type (true/false) |

---

## Received API Response

```json
{
  "account_id": "98765",
  "account_email": "test123@gmail.com",
  "start_date": "2024-03-15T10:30:00Z",
  "end_date": "2024-03-15T10:32:00Z",
  "locale": "en",
  "text": "agile methodology",
  "suggestion_list": "agile methodology, agile methodology process, agile methodology process testing",
  "completed": "true"
}
```

---

## Discrepancy Analysis

### **CRITICAL DEFECTS** 🔴

#### 1. **Incorrect Locale Format**
- **Field**: `locale`
- **Expected**: IETF BCP 47 format with region code (e.g., `en-IN`)
- **Received**: `en`
- **Impact**: Missing region code violates FR-05 specification; breaks locale-specific features
- **Severity**: CRITICAL
- **Category**: Data Contract Violation
- **Remediation**: Backend must return `en-IN` for India location

---

#### 2. **Timestamp Format Mismatch - Wrong Timezone**
- **Field**: `start_date`, `end_date`
- **Expected**: User's local time (IST/UTC+05:30 per test environment)
- **Received**: UTC time with `Z` suffix
- **Issue**: Timestamps show `10:30:00Z` (UTC) instead of `15:30:00+05:30` (IST)
- **Impact**: 5.5 hour time difference; analytics queries, reporting, and time-based filtering all fail
- **Severity**: CRITICAL
- **Category**: Timezone Violation
- **Remediation**: Convert timestamps to user's local timezone before persisting

---

### **HIGH SEVERITY DEFECTS** 🟠

#### 3. **Completed Field Type Violation**
- **Field**: `completed`
- **Expected**: Boolean type (`true` or `false` - no quotes)
- **Received**: String value `"true"` (with quotes)
- **Issue**: Type is String instead of Boolean
- **Impact**: Downstream consumers expecting Boolean will fail type validation; JSON parsing may fail in strict-typed systems
- **Severity**: HIGH
- **Category**: Data Type Mismatch
- **Example Impact**:
  ```java
  // This fails if completed is a string
  if (response.completed) { } // null check fails
  boolean status = response.getCompleted(); // ClassCastException
  ```
- **Remediation**: Return native boolean type without quotes

---

### **SUMMARY OF DEFECTS**

| # | Defect | Type | Severity | Impact |
|---|--------|------|----------|--------|
| 1 | Locale missing region code (en vs en-IN) | Specification | CRITICAL | Violates FR-05 contract |
| 2 | Timestamps in UTC instead of local IST | Timezone | CRITICAL | 5.5 hour offset breaks analytics |
| 3 | `completed` is string not boolean | Type | HIGH | Breaks type validation & parsing |

---

## Recommendation

**Block Deployment** until all 3 defects are resolved:
1. Update backend locale formatter to include region code
2. Implement timezone-aware timestamp serialization
3. Fix boolean serialization in JSON response

These are contract violations that directly impact data integrity and system integration.
