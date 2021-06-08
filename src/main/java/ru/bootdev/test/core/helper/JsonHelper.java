package ru.bootdev.test.core.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JsonHelper {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Boolean compareJsons(String json1, String json2) throws JsonProcessingException {
        return compareJsonNodes(getJsonNode(json1), getJsonNode(json2));
    }

    public static Boolean compareJsons(File json1, String json2) throws IOException {
        return compareJsonNodes(getJsonNode(json1), getJsonNode(json2));
    }

    public static Boolean compareJsons(File json1, File json2) throws IOException {
        return compareJsonNodes(getJsonNode(json1), getJsonNode(json2));
    }

    private static Boolean compareJsonNodes(JsonNode a, JsonNode b) {
        return a.equals(b);
    }

    public static String objectToJsonString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T jsonStringToObject(String jsonString, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(jsonString, clazz);
    }

    public static <T> T jsonFileToObject(File jsonFile, Class<T> clazz) throws IOException {
        return mapper.readValue(jsonFile, clazz);
    }

    public static <T> T jsonNodeToObject(JsonNode jsonNode, Class<T> clazz) throws JsonProcessingException {
        return mapper.treeToValue(jsonNode, clazz);
    }

    public static void objectToJsonFile(Object object, File jsonFile) throws IOException {
        mapper.writeValue(jsonFile, object);
    }

    public static JsonNode getJsonNode(String jsonString) throws JsonProcessingException {
        return mapper.readTree(jsonString);
    }

    public static JsonNode getJsonNode(File jsonString) throws IOException {
        return mapper.readTree(jsonString);
    }

    public static JsonNode getJsonNode(URL url) throws IOException {
        return mapper.readTree(url);
    }

    public static ObjectNode getObjectNode(JsonNode jsonNode, String jsonPath) {
        return (ObjectNode) jsonNode.at(jsonPath);
    }

    public static ObjectNode getObjectNode(File jsonFile, String jsonPath) throws IOException {
        return getObjectNode(getJsonNode(jsonFile), jsonPath);
    }

    public static ObjectNode getObjectNode(String jsonString, String jsonPath) throws IOException {
        return getObjectNode(getJsonNode(jsonString), jsonPath);
    }

    public static ObjectNode getObjectNode(URL url, String jsonPath) throws IOException {
        return getObjectNode(getJsonNode(url), jsonPath);
    }

    public static String jsonNodeToString(Object jsonNode) throws JsonProcessingException {
        return mapper.writeValueAsString(jsonNode);
    }

    public static Boolean compareObjectNodes(ObjectNode a, ObjectNode b) {
        return a.equals(b);
    }
}
