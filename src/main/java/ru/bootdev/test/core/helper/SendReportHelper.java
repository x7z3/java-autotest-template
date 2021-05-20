package ru.bootdev.test.core.helper;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class SendReportHelper {

    public static int sendAllureReport(String url, File... files) {
        if (url == null && files.length == 0) return -1;
        return sendMultipartRequest(given(), url, "files[]", files);
    }

    public static int sendJiraReport(String url, String user, String password, File... files) {
        if (url == null && user == null && password == null && files.length == 0) return -1;
        RequestSpecification givenRequest = given().auth().preemptive().basic(user, password);
        return sendMultipartRequest(givenRequest, url, "file", files);
    }

    private static int sendMultipartRequest(RequestSpecification givenRequest, String url, String fileField, File... files) {
        givenRequest.contentType("multipart/form-data").accept(ContentType.ANY);
        Arrays.asList(files).forEach(file -> givenRequest.multiPart(fileField, file));
        return givenRequest.when().post(url).then().extract().statusCode();
    }
}
