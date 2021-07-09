package ru.bootdev.test.core.tm4j.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bootdev.test.core.tm4j.model.TestRunModel;

import java.io.File;
import java.io.IOException;

public class ResultFile {

    public static final String DEFAULT_TM4J_RESULT_FILE_NAME = "tm4j_result.json";

    private ResultFile() {
    }

    public static void generateResultFile(TestRunModel testRunModel) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DEFAULT_TM4J_RESULT_FILE_NAME), testRunModel);
    }
}
