package ru.bootdev.test.core.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonHelper {

    private final ObjectMapper mapper = new ObjectMapper();

    public Boolean compareJsons(String json1, String json2) throws JsonProcessingException {
        return compareJsonNodes(mapper.readTree(json1), mapper.readTree(json2));
    }

    public Boolean compareJsons(File json1, String json2) throws IOException {
        return compareJsonNodes(mapper.readTree(json1), mapper.readTree(json2));
    }

    public Boolean compareJsons(File json1, File json2) throws IOException {
        return compareJsonNodes(mapper.readTree(json1), mapper.readTree(json2));
    }

    private Boolean compareJsonNodes(JsonNode jsonNode1, JsonNode jsonNode2) {
        return jsonNode1.equals(jsonNode2);
    }
}
