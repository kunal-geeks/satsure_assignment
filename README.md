# Autocomplete Form SDET Assignment - Java Selenium

## Overview
This repository contains a comprehensive test automation suite for the Autocomplete Form application, developed following SDET best practices and industry standards.

**Target Application**: https://test.com/autocomplete-form

## Project Structure

```
├── README.md
├── pom.xml
├── docs/
│   ├── 1-requirement-analysis.md
│   ├── 2-test-scenarios.md
│   ├── 3-defect-identification.md
│   ├── 4-test-cases.md
│   └── 6-ai-reflection.md
├── src/
│   └── test/
│       ├── java/
│       │   └── com/apple/sdet/
│       │       ├── ui/
│       │       │   ├── pages/
│       │       │   │   ├── AutocompleteFormPage.java
│       │       │   │   └── BasePage.java
│       │       │   ├── tests/
│       │       │   │   └── AutocompleteFormTest.java
│       │       │   ├── listeners/
│       │       │   │   └── TestListener.java
│       │       │   └── utils/
│       │       │       ├── DriverManager.java
│       │       │       ├── ConfigReader.java
│       │       │       └── TestDataBuilder.java
│       │       └── api/
│       │           ├── tests/
│       │           │   └── AutocompleteFormAPITest.java
│       │           ├── clients/
│       │           │   └── AutocompleteFormClient.java
│       │           └── validators/
│       │               └── ResponseValidator.java
│       └── resources/
│           ├── config.properties
│           └── testng.xml
```

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Chrome browser (latest version)
- ChromeDriver (automatically managed by WebDriverManager)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/apple/sdet-assignment.git
   cd sdet-assignment
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure environment**
   Edit `src/test/resources/config.properties`:
   ```properties
   base.url=https://test.com
   browser=chrome
   timeout.explicit=10
   timeout.implicit=5
   headless=false
   ```

## Running Tests

### Run all tests
```bash
mvn clean test
```

### Run UI tests only
```bash
mvn clean test -Dgroups=ui
```

### Run API tests only
```bash
mvn clean test -Dgroups=api
```

### Run specific test class
```bash
mvn clean test -Dtest=AutocompleteFormTest
```

### Run with specific configuration
```bash
mvn clean test -Dheadless=true -Dbrowser=chrome
```

## Test Suite Coverage

### UI Automation Tests
- ✓ Text Input & Interaction
- ✓ Tab Navigation
- ✓ Keyboard Interactions (Enter, Escape)
- ✓ Suggestion Filtering (Prefix Match)
- ✓ Suggestion Filtering (Match Anywhere)
- ✓ Suggestion Selection
- ✓ Form Submission
- ✓ Error/Success Message Display

### API Automation Tests
- ✓ Response Schema Validation
- ✓ Data Type Validation
- ✓ Locale Format Validation
- ✓ Suggestion List Filtering
- ✓ Negative Test Cases (Missing Fields, Invalid Data)

## Documentation

### Requirement Analysis
See `docs/1-requirement-analysis.md` for detailed functional requirements breakdown.

### Test Scenarios
See `docs/2-test-scenarios.md` for top 10 test scenarios ranked by risk level (Critical → Low).

### Defect Identification
See `docs/3-defect-identification.md` for API response discrepancies against FR-05.

### Detailed Test Cases
See `docs/4-test-cases.md` for 8+ detailed test cases with:
- Test Case IDs
- Titles
- Preconditions
- Test Steps (numbered)
- Expected Results
- Test Data

### AI Reflection
See `docs/6-ai-reflection.md` for documentation of AI usage and modifications made.

## Design Patterns & Best Practices

### Page Object Model (POM)
- Encapsulates UI elements and interactions
- Improves maintainability and reusability
- Clear separation between test logic and page interactions

### Test Data Builder
- Creates complex test data objects
- Improves readability of test scenarios
- Enables flexible test case configuration

### Custom Assertions
- Provides clear, business-readable assertions
- Centralizes assertion logic for consistency
- Improves test failure diagnostics

### WebDriverManager
- Automatic ChromeDriver management
- No manual driver setup required
- Cross-platform compatibility

## Dependencies

Key libraries used:
- **Selenium WebDriver 4.x** - UI automation framework
- **RestAssured 4.x** - REST API testing
- **TestNG 7.x** - Test execution framework
- **WebDriverManager 5.x** - WebDriver binary management
- **JUnit 5.x** - Assertions
- **Lombok 1.x** - Boilerplate reduction
- **Jackson 2.x** - JSON processing
- **SLF4J** - Logging

## Reporting

Test reports are generated automatically:
- HTML Report: `target/surefire-reports/`
- TestNG Report: `target/surefire-reports/index.html`

## CI/CD Integration

For GitHub Actions CI/CD pipeline, add workflow file at `.github/workflows/test.yml`:
```yaml
name: Automated Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: 11
      - run: mvn clean test
```

## Troubleshooting

### WebDriver Issues
- Ensure Chrome browser is installed
- Clear browser cache: `~/.wdm/` directory
- Update ChromeDriver manually if needed

### Connection Issues
- Verify API endpoint is accessible
- Check network proxy settings
- Validate SSL certificates for HTTPS connections

### Test Failures
- Check logs in `target/surefire-reports/`
- Review screenshots in `target/screenshots/` (if enabled)
- Validate test data configuration

## Contributing Guidelines

1. Follow Google Java Code Style
2. Write meaningful commit messages
3. Add appropriate Javadoc comments
4. Update this README for new features
5. Ensure all tests pass before submitting PR

## Support & Contact

For questions or issues, please contact the SDET team at sdet@apple.com

---

**Last Updated**: June 2024
**Version**: 1.0.0
**Status**: Production Ready
