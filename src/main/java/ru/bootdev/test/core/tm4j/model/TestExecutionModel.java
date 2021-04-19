package ru.bootdev.test.core.tm4j.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestExecutionModel {

    private String source;
    private String result;
    private TestCaseModel testCase;
}
