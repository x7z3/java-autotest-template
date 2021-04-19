package ru.bootdev.test.api.request.base;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class BaseRequest {

    public ValidatableResponse get(String url) {
        return given().contentType(ContentType.JSON).accept(ContentType.ANY).log().all().when().get(url).then().log().all();
    }
}
