package ru.bootdev.test.core.tm4j.builder;

import io.qameta.allure.Allure;
import io.qameta.allure.TmsLink;
import io.qameta.allure.model.Link;
import io.qameta.allure.model.TestResult;
import ru.bootdev.test.core.tm4j.TestCase;
import ru.bootdev.test.core.tm4j.model.TestCaseModel;
import ru.bootdev.test.core.tm4j.model.TestExecutionModel;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

        AtomicReference<TestResult> allureTestResult = new AtomicReference<>();
        Allure.getLifecycle().updateTestCase(allureTestResult::set);
        Optional<Link> tms = allureTestResult.get().getLinks().stream().filter(link -> link.getType().equals("tms")).findFirst();
        if (tms.isPresent()) key = tms.get().getName();

        if (key == null && name == null) return null;
        return new TestCaseModel(key, name);
    }

    private String getSource() {
        return testMethod.getDeclaringClass() + "." + testMethod.getName();
    }
}
