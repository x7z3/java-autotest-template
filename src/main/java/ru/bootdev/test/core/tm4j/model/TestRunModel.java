package ru.bootdev.test.core.tm4j.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TestRunModel {

    private Integer version = 1;
    private final Set<TestExecutionModel> executions = new HashSet<>();

    public synchronized void addResult(TestExecutionModel execution) {
        executions.add(execution);
    }
}
