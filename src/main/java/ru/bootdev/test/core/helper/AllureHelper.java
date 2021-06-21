package ru.bootdev.test.core.helper;

import io.qameta.allure.Allure;

import java.io.File;
import java.io.IOException;

import static ru.bootdev.test.core.helper.DateTimeHelper.currentDateTime;
import static ru.bootdev.test.core.helper.FileHelper.readFile;
import static ru.bootdev.test.core.helper.TextHelper.stringToFileName;

public class AllureHelper {

    public static final String JSON_MIME_TYPE = "application/json";
    public static final String XML_MIME_TYPE = "application/XML";
    public static final String TEXT_MIME_TYPE = "text/plain";

    public static void tmsLink(String caseId) {
        String tmsLinkPattern = System.getProperty("allure.link.tms.pattern");
        Allure.tms(caseId, tmsLinkPattern.replace("{}", caseId));
    }

    public static void tmsLink(String tmsLink, String description) {
        tmsLink(tmsLink);
        Allure.description(description);
    }

    public String attachJson(String json) {
        Allure.addAttachment("JSON_" + stringToFileName(currentDateTime()), JSON_MIME_TYPE, json);
        return json;
    }

    public String attachXml(String xml) {
        Allure.addAttachment("XML_" + stringToFileName(currentDateTime()), XML_MIME_TYPE, xml);
        return xml;
    }

    public static File attachFile(File file, String type) throws IOException {
        Allure.addAttachment(file.getName(), type, readFile(file));
        return file;
    }

    public static File attachFile(File file) throws IOException {
        return attachFile(file, TEXT_MIME_TYPE);
    }

    public static File attachJsonFile(File file) throws IOException {
        return attachFile(file, JSON_MIME_TYPE);
    }

    public static File attachXmlFile(File file) throws IOException {
        return attachFile(file, XML_MIME_TYPE);
    }
}
