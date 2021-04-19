package ru.bootdev.test.core.tm4j.builder;

import ru.bootdev.test.core.tm4j.model.TestExecutionModel;
import ru.bootdev.test.core.tm4j.model.TestRunModel;

public class TestRunBuilder {

    private volatile static TestRunBuilder instance;
    private final TestRunModel testRunModel = new TestRunModel();

    private TestRunBuilder() {
    }

    public synchronized static TestRunBuilder getInstance() {
        if (instance == null) instance = new TestRunBuilder();
        return instance;
    }

    public void addResult(TestExecutionModel executionResult) {
        if (executionResult != null) testRunModel.addResult(executionResult);
    }

    public TestRunModel getTestRunModel() {
        return testRunModel;
    }
}
