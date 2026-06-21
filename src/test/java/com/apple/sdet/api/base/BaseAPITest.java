package com.apple.sdet.api.base;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Base class for API tests with WireMock setup.
 * Provides mock API server for testing without real backend.
 */
public class BaseAPITest {

    protected static WireMockServer wireMockServer;
    protected static final String API_HOST = "localhost";
    protected static final int API_PORT = 8080;
    protected static final String BASE_URL = "http://" + API_HOST + ":" + API_PORT;

    @BeforeClass(alwaysRun = true)
    public static void setupWireMock() {
        wireMockServer = new WireMockServer(wireMockConfig().port(API_PORT));
        wireMockServer.start();
        WireMock.configureFor(API_HOST, API_PORT);
        setupMockResponses();
    }

    @AfterClass(alwaysRun = true)
    public static void tearDownWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    /**
     * Configure default mock responses for API endpoints.
     */
    private static void setupMockResponses() {
        // Default mock response for form response endpoint (registered first)
        wireMockServer.stubFor(
            get(urlPathEqualTo("/api/form-response"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(getMockFormResponse()))
        );

        // Mock response for incomplete form (missing required field) - registered after default
        wireMockServer.stubFor(
            get(urlPathEqualTo("/api/form-response"))
                .withQueryParam("form_id", equalTo("incomplete-form-001"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\n" +
                        "  \"account_id\": \"ACC-12345\",\n" +
                        "  \"account_email\": \"test@apple.com\",\n" +
                        "  \"text\": \"Sample Text\",\n" +
                        "  \"locale\": \"en-US\",\n" +
                        "  \"timestamp\": \"2024-06-21T10:30:00Z\",\n" +
                        "  \"start_date\": \"2024-06-21T10:00:00Z\"\n" +
                        "}"))
        );

        // Mock response for invalid type form - registered after default
        wireMockServer.stubFor(
            get(urlPathEqualTo("/api/form-response"))
                .withQueryParam("form_id", equalTo("invalid-type-form-001"))
                .willReturn(aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"error\": \"Field type mismatch\"}"))
        );

        // Mock response for POST form response endpoint
        wireMockServer.stubFor(
            post(urlPathEqualTo("/api/form-response"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(getMockSubmitResponse()))
        );
    }

    /**
     * Get mock form response JSON.
     */
    protected static String getMockFormResponse() {
        return "{\n" +
            "  \"account_id\": \"ACC-12345\",\n" +
            "  \"account_email\": \"test@apple.com\",\n" +
            "  \"text\": \"Sample Text\",\n" +
            "  \"locale\": \"en-US\",\n" +
            "  \"timestamp\": \"2024-06-21T10:30:00Z\",\n" +
            "  \"start_date\": \"2024-06-21T10:00:00Z\",\n" +
            "  \"end_date\": \"2024-06-21T10:35:00Z\",\n" +
            "  \"timezone_offset\": \"+00:00\",\n" +
            "  \"completed\": true,\n" +
            "  \"suggestion_list\": \"Sample Text\",\n" +
            "  \"suggestions\": [\"apple\", \"application\", \"apply\"]\n" +
            "}";
    }

    /**
     * Get mock suggestions response JSON.
     */
    protected static String getMockSuggestionsResponse() {
        return "{\n" +
            "  \"suggestions\": [\"apple\", \"application\", \"apply\", \"apricot\"],\n" +
            "  \"total\": 4\n" +
            "}";
    }

    /**
     * Get mock submit response JSON.
     */
    protected static String getMockSubmitResponse() {
        return "{\n" +
            "  \"success\": true,\n" +
            "  \"message\": \"Form submitted successfully\",\n" +
            "  \"submission_id\": \"SUB-67890\"\n" +
            "}";
    }
}
