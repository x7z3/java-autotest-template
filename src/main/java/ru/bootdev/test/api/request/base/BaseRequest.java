package ru.bootdev.test.api.request.base;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class BaseRequest {

    protected final static String MULTIPART_FORM_DATA = "multipart/form-data";

    public ValidatableResponse get(String url) {
        return given().accept(ContentType.ANY).when().get(url).then();
    }

    public ValidatableResponse postMultipart(String url, String fileField, File... files) {
        RequestSpecification givenRequest = given().accept(ContentType.ANY).contentType(MULTIPART_FORM_DATA);
        Arrays.asList(files).forEach(file -> givenRequest.multiPart(fileField, file));
        return givenRequest.when().post(url).then();
    }

    private RequestSpecification givenContentTypeJson() {
        return given().accept(ContentType.ANY).contentType(ContentType.JSON);
    }

    public ValidatableResponse postJson(String url, File jsonBody) {
        return givenContentTypeJson().body(jsonBody).when().post(url).then();
    }

    public ValidatableResponse postJson(String url, String jsonBody) {
        return givenContentTypeJson().body(jsonBody).when().post(url).then();
    }

    public ValidatableResponse delete(String url) {
        return given().accept(ContentType.ANY).when().delete(url).then();
    }

    public String joinUrl(String baseUrl, String... urlParts) {
        if (urlParts.length == 0) return baseUrl;
        StringBuilder url = new StringBuilder(baseUrl);
        for (String urlPart : urlParts) {
            if ("/&@?=".indexOf(urlPart.charAt(0)) == -1) url.append("/");
            url.append(urlPart);
        }
        return url.toString();
    }
}
