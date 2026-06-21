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
│           ├── config-test.properties
│           └── testng.xml
```

## Recent Updates

- Multi-browser support added: Chrome, Firefox, Edge, and Safari (Safari on macOS only)
- Headless mode enabled by default for CI/CD (configurable)
- API tests run without any browser initialization (RestAssured + WireMock)
- Improved WebDriver lifecycle: each UI test gets its own driver (initialized in @BeforeMethod and quit in @AfterMethod)
- Added comprehensive docs: HEADLESS_MODE.md and BROWSER_LIFECYCLE.md

## Setup Instructions

### Prerequisites
- Java 22.0.1 or compatible version
- Maven 3.6+
- One or more browsers installed for UI testing: Chrome, Firefox, Edge, Safari (macOS)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/kunal-geeks/satsure_assignment.git
   cd satsure_assignment
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure environment**
   Edit `src/test/resources/config-test.properties` (defaults in repo):
   ```properties
   base.url=https://test.com
   browser=chrome        # chrome | firefox | edge | safari
   headless=true         # true (CI) or false (local debugging)
   timeout.explicit=10
   timeout.implicit=5
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

### Run with specific configuration (example: Firefox headed)
```bash
mvn clean test -Dheadless=false -Dbrowser=firefox
```

## Test Suite Coverage

### UI Automation Tests (8)
- Text Input & Interaction
- Tab Navigation
- Keyboard Interactions (Enter, Escape)
- Suggestion Filtering (Prefix Match)
- Suggestion Filtering (Match Anywhere)
- Suggestion Selection
- Form Submission
- Error/Success Message Display

### API Automation Tests (10)
- Response Schema Validation
- Data Type Validation
- Locale Format Validation
- Suggestion List Filtering
- Negative Test Cases (Missing Fields, Invalid Data)

## Browser Support

- Chrome: Full support with headless and headed modes (recommended)
- Firefox: Full support with headless and headed modes
- Edge: Supported via EdgeOptions (headless available)
- Safari: Supported on macOS (no headless for SafariDriver)

Notes:
- WebDriver binaries are managed automatically by WebDriverManager
- SafariDriver does not require WebDriverManager and works only on macOS with Safari's 'Allow Remote Automation' enabled

## Documentation

See `HEADLESS_MODE.md` and `BROWSER_LIFECYCLE.md` for detailed configuration, troubleshooting, and CI recommendations.

## CI/CD Integration

Example GitHub Actions workflow:
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
          java-version: '22'
      - name: Run tests
        run: mvn clean test -Dheadless=true -Dbrowser=chrome
```

## Troubleshooting

- Ensure the browser you choose is installed and compatible with the driver downloaded by WebDriverManager
- For Safari: enable `Allow Remote Automation` in Safari Develop menu
- If tests fail in headless mode but pass in headed mode, add explicit waits where needed

## Contributing Guidelines

1. Follow Google Java Code Style
2. Write meaningful commit messages
3. Add appropriate Javadoc comments
4. Update this README for new features
5. Ensure all tests pass before submitting PR

## Support & Contact

For questions or issues, please contact the SDET team at sdet@apple.com

---

**Last Updated**: June 22, 2026
**Version**: 1.1.0
**Status**: Production Ready

