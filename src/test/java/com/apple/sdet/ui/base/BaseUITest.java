package com.apple.sdet.ui.base;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Base class for UI tests with mock HTML page setup.
 * Provides a local HTML file serving as the test page.
 */
public class BaseUITest {

    protected static final String MOCK_PAGE_PATH = System.getProperty("java.io.tmpdir") + "/autocomplete-form.html";
    protected static final String TEST_PAGE_URL = "file://" + MOCK_PAGE_PATH;

    @BeforeClass(alwaysRun = true)
    public static void setupMockPage() throws IOException {
        createMockHTMLPage();
    }

    @AfterClass(alwaysRun = true)
    public static void tearDownMockPage() {
        try {
            Files.deleteIfExists(Paths.get(MOCK_PAGE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a mock HTML page for testing autocomplete form.
     */
    private static void createMockHTMLPage() throws IOException {
        String htmlContent = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Autocomplete Form</title>\n" +
            "    <style>\n" +
            "        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; margin: 20px; }\n" +
            "        .form-container { max-width: 500px; margin: 0 auto; }\n" +
            "        .form-group { margin-bottom: 20px; }\n" +
            "        label { display: block; margin-bottom: 8px; font-weight: bold; }\n" +
            "        input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; }\n" +
            "        .suggestions { position: absolute; background: white; border: 1px solid #ccc; border-top: none; max-width: 340px; margin-left: 0; display: none; z-index: 10; }\n" +
            "        .suggestions.show { display: block; }\n" +
            "        .suggestion-item { padding: 10px; cursor: pointer; }\n" +
            "        .suggestion-item:hover { background: #f0f0f0; }\n" +
            "        .button { padding: 10px 20px; background: #0071e3; color: white; border: none; border-radius: 4px; cursor: pointer; }\n" +
            "        .button:hover { background: #0077d6; }\n" +
            "        .success-message { color: green; margin-top: 10px; display: none; }\n" +
            "        .success-message.show { display: block; }\n" +
            "        .error-message { color: red; margin-top: 10px; display: none; }\n" +
            "        .error-message.show { display: block; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"form-container\">\n" +
            "        <h1>Autocomplete Form</h1>\n" +
            "        <form id=\"autocompleteForm\">\n" +
            "            <div class=\"form-group\">\n" +
            "                <label for=\"autocompleteInput\">Search</label>\n" +
            "                <input type=\"text\" id=\"autocompleteInput\" placeholder=\"Type to search...\" />\n" +
            "                <div class=\"suggestions\" id=\"suggestionsList\"></div>\n" +
            "            </div>\n" +
            "            <button type=\"submit\" class=\"button\" id=\"submitBtn\">Submit</button>\n" +
            "            <div class=\"error-message\" id=\"errorMessage\">Invalid input</div>\n" +
            "        </form>\n" +
            "        <div class=\"success-message\" id=\"successMessage\">Success - Form submitted successfully and recorded!</div>\n" +
            "    </div>\n" +
            "    <script>\n" +
            "        const input = document.getElementById('autocompleteInput');\n" +
            "        const suggestionsList = document.getElementById('suggestionsList');\n" +
            "        const form = document.getElementById('autocompleteForm');\n" +
            "        const successMessage = document.getElementById('successMessage');\n" +
            "        const errorMessage = document.getElementById('errorMessage');\n" +
            "        \n" +
            "        const allItems = ['apple', 'application', 'apply', 'agile methodology', 'agile methodology process', 'agile methodology process testing'];\n" +
            "        \n" +
            "        let selectedIndex = -1;\n" +
            "        \n" +
            "        // Show initial suggestions on page load\n" +
            "        window.addEventListener('load', () => {\n" +
            "            showSuggestions(['agile methodology', 'agile methodology process', 'agile methodology process testing']);\n" +
            "        });\n" +
            "        \n" +
            "        function showSuggestions(items) {\n" +
            "            if (items.length === 0) {\n" +
            "                suggestionsList.classList.remove('show');\n" +
            "                return;\n" +
            "            }\n" +
            "            suggestionsList.innerHTML = items.map((suggestion, index) => \n" +
            "                `<div class=\"suggestion-item\" data-index=\"${index}\">${suggestion}</div>`\n" +
            "            ).join('');\n" +
            "            suggestionsList.classList.add('show');\n" +
            "            attachSuggestionListeners();\n" +
            "        }\n" +
            "        \n" +
            "        function attachSuggestionListeners() {\n" +
            "            document.querySelectorAll('.suggestion-item').forEach(item => {\n" +
            "                item.addEventListener('click', (e) => {\n" +
            "                    input.value = item.textContent;\n" +
            "                    suggestionsList.classList.remove('show');\n" +
            "                });\n" +
            "            });\n" +
            "        }\n" +
            "        \n" +
            "        input.addEventListener('input', (e) => {\n" +
            "            const value = e.target.value.toLowerCase();\n" +
            "            selectedIndex = -1;\n" +
            "            \n" +
            "            if (!value) {\n" +
            "                showSuggestions(['agile methodology', 'agile methodology process', 'agile methodology process testing']);\n" +
            "                return;\n" +
            "            }\n" +
            "            \n" +
            "            const matches = allItems.filter(s => s.toLowerCase().includes(value));\n" +
            "            showSuggestions(matches);\n" +
            "        });\n" +
            "        \n" +
            "        input.addEventListener('keydown', (e) => {\n" +
            "            const items = document.querySelectorAll('.suggestion-item');\n" +
            "            \n" +
            "            if (e.key === 'ArrowDown') {\n" +
            "                e.preventDefault();\n" +
            "                selectedIndex = Math.min(selectedIndex + 1, items.length - 1);\n" +
            "                updateSelection(items);\n" +
            "            } else if (e.key === 'ArrowUp') {\n" +
            "                e.preventDefault();\n" +
            "                selectedIndex = Math.max(selectedIndex - 1, -1);\n" +
            "                updateSelection(items);\n" +
            "            } else if (e.key === 'Enter') {\n" +
            "                e.preventDefault();\n" +
            "                if (selectedIndex >= 0 && items[selectedIndex]) {\n" +
            "                    input.value = items[selectedIndex].textContent;\n" +
            "                    suggestionsList.classList.remove('show');\n" +
            "                } else {\n" +
            "                    submitForm();\n" +
            "                }\n" +
            "            } else if (e.key === 'Tab') {\n" +
            "                if (selectedIndex >= 0 && items[selectedIndex]) {\n" +
            "                    input.value = items[selectedIndex].textContent;\n" +
            "                    suggestionsList.classList.remove('show');\n" +
            "                }\n" +
            "            }\n" +
            "        });\n" +
            "        \n" +
            "        function updateSelection(items) {\n" +
            "            items.forEach((item, index) => {\n" +
            "                item.style.background = index === selectedIndex ? '#e8e8e8' : 'white';\n" +
            "            });\n" +
            "        }\n" +
            "        \n" +
            "        form.addEventListener('submit', (e) => {\n" +
            "            e.preventDefault();\n" +
            "            submitForm();\n" +
            "        });\n" +
            "        \n" +
            "        function submitForm() {\n" +
            "            if (input.value.trim()) {\n" +
            "                suggestionsList.classList.remove('show');\n" +
            "                errorMessage.classList.remove('show');\n" +
            "                successMessage.classList.add('show');\n" +
            "                setTimeout(() => {\n" +
            "                    successMessage.classList.remove('show');\n" +
            "                    input.value = '';\n" +
            "                    suggestionsList.classList.remove('show');\n" +
            "                }, 2000);\n" +
            "            } else {\n" +
            "                errorMessage.classList.add('show');\n" +
            "                successMessage.classList.remove('show');\n" +
            "            }\n" +
            "        }\n" +
            "    </script>\n" +
            "</body>\n" +
            "</html>";

        Files.write(Paths.get(MOCK_PAGE_PATH), htmlContent.getBytes());
    }
}
