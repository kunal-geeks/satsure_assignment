package com.apple.sdet.api.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST API client for Autocomplete Form service.
 * Handles all API communication for form submission and retrieval.
 */
public class AutocompleteFormClient {
    private static final Logger logger = LoggerFactory.getLogger(AutocompleteFormClient.class);

    private final String baseUrl;
    private static final String FORM_RESPONSE_ENDPOINT = "/api/form-response";

    /**
     * Constructor for AutocompleteFormClient.
     *
     * @param baseUrl API base URL
     */
    public AutocompleteFormClient(String baseUrl) {
        this.baseUrl = baseUrl;
        RestAssured.baseURI = baseUrl;
    }

    /**
     * Submit form response via POST.
     *
     * @param requestBody Request payload
     * @return Response object
     */
    public Response submitFormResponse(String requestBody) {
        logger.info("Submitting form response");
        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .when()
                .body(requestBody)
                .post(FORM_RESPONSE_ENDPOINT)
                .then()
                .log().all()
                .extract()
                .response();

        logger.info("Form submission response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Retrieve form response via GET.
     *
     * @param formId Form ID to retrieve
     * @return Response object
     */
    public Response getFormResponse(String formId) {
        logger.info("Retrieving form response for formId: {}", formId);
        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .queryParam("form_id", formId)
                .when()
                .get(FORM_RESPONSE_ENDPOINT)
                .then()
                .log().all()
                .extract()
                .response();

        logger.info("Form retrieval response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Retrieve all form responses (paginated).
     *
     * @param page  Page number
     * @param limit Results per page
     * @return Response object
     */
    public Response getAllFormResponses(int page, int limit) {
        logger.info("Retrieving form responses - page: {}, limit: {}", page, limit);
        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .queryParam("page", page)
                .queryParam("limit", limit)
                .when()
                .get(FORM_RESPONSE_ENDPOINT)
                .then()
                .log().all()
                .extract()
                .response();

        logger.info("Form list retrieval response status: {}", response.getStatusCode());
        return response;
    }

    /**
     * Delete form response via DELETE.
     *
     * @param formId Form ID to delete
     * @return Response object
     */
    public Response deleteFormResponse(String formId) {
        logger.info("Deleting form response for formId: {}", formId);
        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .when()
                .delete(FORM_RESPONSE_ENDPOINT + "/" + formId)
                .then()
                .log().all()
                .extract()
                .response();

        logger.info("Form deletion response status: {}", response.getStatusCode());
        return response;
    }
}
