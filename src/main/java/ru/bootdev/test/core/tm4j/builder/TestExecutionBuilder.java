package ru.bootdev.test.core.tm4j.builder;

import io.qameta.allure.TmsLink;
import ru.bootdev.test.core.tm4j.TestCase;
import ru.bootdev.test.core.tm4j.model.TestCaseModel;
import ru.bootdev.test.core.tm4j.model.TestExecutionModel;

import java.lang.reflect.Method;

import static ru.bootdev.test.core.tm4j.TestCaseId.getTestCaseIdByMethodName;

public class TestExecutionBuilder {

    private final Method testMethod;
    private final String testResult;

    public TestExecutionBuilder(Method testMethod, String testResult) {
        this.testMethod = testMethod;
        this.testResult = testResult;
    }

    public TestExecutionModel build() {
        TestCaseModel testCaseModel = getTestCaseModel();
        if (testCaseModel == null) return null;
        return new TestExecutionModel(getSource(), testResult, testCaseModel);
    }

    private TestCaseModel getTestCaseModel() {
        String key = null, name = null;
        TestCase testCase = testMethod.getAnnotation(TestCase.class);
        TmsLink tmsLink = testMethod.getAnnotation(TmsLink.class);

        if (testCase != null) {
            key = testCase.key().isEmpty() ? null : testCase.key();
            name = testCase.name().isEmpty() ? null : testCase.name();
        } else if (tmsLink != null) {
            key = tmsLink.value().isEmpty() ? null : tmsLink.value();
        } else {
            key = getTestCaseIdByMethodName(testMethod.getName());
        }

        if (key == null && name == null) return null;
        return new TestCaseModel(key, name);
    }

    private String getSource() {
        return testMethod.getDeclaringClass() + "." + testMethod.getName();
    }
}
