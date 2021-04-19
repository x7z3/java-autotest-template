package ru.bootdev.test.api.request;

import io.restassured.response.ValidatableResponse;
import ru.bootdev.test.api.request.base.BaseRequest;

public class AllureRequest extends BaseRequest {

    private String baseUrl;
    private String projectId;
    private final String cleanResultsEndpoint = "clean-results";
    private final String cleanHistoryEndpoint = "clean-history";
    private final String generateReportEndpoint = "generate-report";
    private final String urlFormat = "%s/%s?project_id=%s";

    public AllureRequest(String baseUrl, String projectId) {
        this.baseUrl = baseUrl;
        this.projectId = projectId;
    }

    public Integer cleanAllureResults() {
        return endpointRequest(cleanResultsEndpoint).extract().statusCode();
    }

    public Integer cleanAllureHistoryAndTrends() {
        return endpointRequest(cleanHistoryEndpoint).extract().statusCode();
    }

    public Integer generateAllureReport() {
        return endpointRequest(generateReportEndpoint).extract().statusCode();
    }

    private ValidatableResponse endpointRequest(String endpoint) {
        return get(String.format(urlFormat, baseUrl, endpoint, projectId));
    }
}
