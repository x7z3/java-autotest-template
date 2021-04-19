package ru.bootdev.test.core.tm4j.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestRunModel {

    private Integer version = 1;
    private final List<TestExecutionModel> executions = new ArrayList<>();

    public synchronized void addResult(TestExecutionModel execution) {
        executions.add(execution);
    }
}
