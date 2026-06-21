# Headless Mode Configuration

## Overview

This SDET automation suite supports **headless mode** for UI tests, allowing tests to run without opening a visible browser window. This is useful for:
- Running tests in CI/CD pipelines
- Reducing resource consumption
- Running tests on headless servers
- Faster test execution

## Current Configuration

**Default Mode**: HEADLESS (enabled by default)

```properties
# src/test/resources/config-test.properties
headless=true
```

## Switching Between Headless and Headed Modes

### Run Tests in Headless Mode (Default)
```bash
mvn clean test
```

### Run Tests in Headed Mode (with visible browser window)
```bash
mvn clean test -Dheadless=false
```

### Configure via Configuration File
Edit `src/test/resources/config-test.properties`:

```properties
# For headless execution (no visible browser)
headless=true

# For headed execution (visible browser window)
headless=false
```

## Browser Lifecycle Management

### UI Tests (AutocompleteFormTest)
- **Before each test**: New Chrome browser instance initialized
- **After each test**: Browser instance properly closed and terminated
- **Per-test isolation**: Each test gets a fresh browser state

### API Tests (AutocompleteFormAPITest)
- **No browser**: API tests use RestAssured only, no browser processes
- **No overhead**: Zero browser initialization/cleanup time

## Headless Mode Options

When headless=true, the following Chrome arguments are applied:

```
--headless              # Enable headless mode
--disable-gpu           # Disable GPU acceleration
--window-size=1920x1080 # Set window size for consistent rendering
--disable-dev-shm-usage # Reduce memory usage
```

## Performance Comparison

| Mode | Browser Window | Memory | Speed | Best For |
|------|---|---|---|---|
| **Headed** | Visible | Higher | Normal | Local debugging |
| **Headless** | Hidden | Lower | ~15-20% faster | CI/CD, production tests |

## Troubleshooting

### Issue: Tests fail in headless mode but pass in headed mode

**Solution**: Some JavaScript interactions may behave differently. Use explicit waits:

```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
```

### Issue: Chrome process not closing

**Solution**: Ensure `@AfterMethod` tearDown is called:

```java
@AfterMethod
public void tearDown() {
    DriverManager.quitDriver();  // Properly closes all browser processes
}
```

### Issue: Port 8080 already in use for API mock server

**Solution**: Kill the existing process:

```bash
lsof -i :8080
kill -9 <PID>
```

## Test Execution Summary

```
Total Tests:     18
├── UI Tests:      8 (with headless mode support)
├── API Tests:    10 (no browser, no overhead)
└── Status:       ✅ All passing

Test Run Time: ~18 seconds (headless mode)
```

## CI/CD Integration Example

```yaml
# GitHub Actions example
- name: Run SDET Tests
  run: mvn clean test  # Uses headless mode by default
```

## Notes

- **Thread Safety**: Each test thread gets its own driver instance via ThreadLocal storage
- **Automatic Cleanup**: Browser processes automatically terminated after each test
- **No Memory Leaks**: Proper resource cleanup prevents driver instance accumulation
- **Logging**: Detailed logs show when drivers initialize and terminate
