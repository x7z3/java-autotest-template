package ru.bootdev.test.api.request.base;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class BaseRequest {

    protected final static String MULTIPART_FORM_DATA = "multipart/form-data";

    public ValidatableResponse get(String url) {
        return given().accept(ContentType.ANY).log().all().when().get(url).then().log().all();
    }

    public <T> T sendPostJsonRequest(URL url, File body, Integer expectedStatusCode, Class<T> responseModel) {
        return given().contentType(ContentType.JSON).body(body).log().all().
                when().log().all().post(url).
                then().log().all().statusCode(expectedStatusCode).extract().body().as(responseModel);
    }

    public ValidatableResponse postMultipart(String url, String fileField, File... files) {
        RequestSpecification givenRequest = given().accept(ContentType.ANY).contentType(MULTIPART_FORM_DATA);
        Arrays.asList(files).forEach(file -> givenRequest.multiPart(fileField, file));
        return givenRequest.log().all().when().post(url).then().log().all();
    }

    public ValidatableResponse postJson(String url, String json) {
        return given().accept(ContentType.ANY).contentType(ContentType.JSON).body(json).log().all()
                .when().post(url).then().log().all();
    }

    public ValidatableResponse delete(String url) {
        return given().accept(ContentType.ANY).log().all().when().delete(url).then().log().all();
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
