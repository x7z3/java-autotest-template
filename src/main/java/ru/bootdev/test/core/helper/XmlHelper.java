package ru.bootdev.test.core.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class XmlHelper {

    private static final XmlMapper mapper = new XmlMapper();

    public static JsonNode getXmlNode(String xmlText) throws JsonProcessingException {
        return mapper.readTree(xmlText);
    }

    public static JsonNode getXmlNode(File xmlFile) throws IOException {
        return mapper.readTree(xmlFile);
    }

    public static JsonNode getXmlNode(URL url) throws IOException {
        return mapper.readTree(url);
    }

    public static <T> T xmlStringToObject(String xmlText, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(xmlText, clazz);
    }

    public static <T> T xmlFileToObject(File xmlFile, Class<T> clazz) throws IOException {
        return mapper.readValue(xmlFile, clazz);
    }

    public static <T> T xmlUrlToObject(URL xmlUrl, Class<T> clazz) throws IOException {
        return mapper.readValue(xmlUrl, clazz);
    }

    public static String xmlNodeToString(Object xmlNode) throws JsonProcessingException {
        return mapper.writeValueAsString(xmlNode);
    }
}
