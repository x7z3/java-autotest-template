package ru.bootdev.test.core;

import io.qameta.allure.Allure;
import io.qameta.allure.TmsLink;
import io.qameta.allure.model.Link;
import io.qameta.allure.util.ResultsUtils;
import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import ru.bootdev.test.core.tm4j.TestCase;
import ru.bootdev.test.core.tm4j.TestResult;
import ru.bootdev.test.core.tm4j.builder.TestExecutionBuilder;
import ru.bootdev.test.core.tm4j.builder.TestRunBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.platform.engine.TestExecutionResult.Status.FAILED;
import static org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL;
import static ru.bootdev.test.core.helper.FileHelper.*;
import static ru.bootdev.test.core.helper.SendReportHelper.sendAllureReport;
import static ru.bootdev.test.core.helper.SendReportHelper.sendJiraReport;
import static ru.bootdev.test.core.properties.AllureProperties.*;
import static ru.bootdev.test.core.properties.JiraProperties.*;
import static ru.bootdev.test.core.tm4j.TestCaseId.getTestCaseIdByMethodName;
import static ru.bootdev.test.core.tm4j.file.ResultFile.DEFAULT_TM4J_RESULT_FILE_NAME;
import static ru.bootdev.test.core.tm4j.file.ResultFile.generateResultFile;

public class SuiteListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        restAssuredSetUp();
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        Method testMethod = getMethodFromTestIdentifier(testIdentifier);
        if (testMethod != null) {
            gatherTestResult(testMethod, testExecutionResult.getStatus());
            setAllureTmsLink(testMethod);
        }
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        try {
            generateResultFile(TestRunBuilder.getInstance().getTestRunModel());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (JIRA_SEND_RESULTS_ENABLED) sendJiraResults();
        if (ALLURE_SEND_RESULTS_ENABLED) sendAllureResults();
    }

    private void restAssuredSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private void setAllureTmsLink(Method testMethod) {
        TestCase testCase = testMethod.getAnnotation(TestCase.class);
        TmsLink tmsLink = testMethod.getAnnotation(TmsLink.class);
        if (testCase != null && tmsLink != null) return;

        String testCaseId = getTestCaseIdByMethodName(testMethod.getName());
        if (testCaseId == null) return;

        List<Link> allureLinks = new ArrayList<>();
        allureLinks.add(ResultsUtils.createTmsLink(testCaseId));
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setLinks(allureLinks));
    }

    private Method getMethodFromTestIdentifier(TestIdentifier testIdentifier) {
        Optional<TestSource> testSource = testIdentifier.getSource();
        if (!testSource.isPresent()) return null;
        if (testSource.get() instanceof MethodSource)
            return ((MethodSource) testSource.get()).getJavaMethod();
        return null;
    }

    private void addTestResult(Method testMethod, String testResult) {
        TestRunBuilder.getInstance().addResult(new TestExecutionBuilder(testMethod, testResult).build());
    }

    private void gatherTestResult(Method testMethod, TestExecutionResult.Status status) {
        if (status == SUCCESSFUL) {
            addTestResult(testMethod, TestResult.PASSED);
        } else if (status == FAILED) {
            addTestResult(testMethod, TestResult.FAILED);
        }
    }

    private static void sendJiraResults() {
        File jiraZippedResults = packFileToZip(createTmpFile(), new File(DEFAULT_TM4J_RESULT_FILE_NAME));
        sendJiraReport(JIRA_RESULTS_ENDPOINT, JIRA_USER, JIRA_PASSWORD, jiraZippedResults);
    }

    private static void sendAllureResults() {
        List<File> allureFiles = getDirectoryFiles(ALLURE_RESULTS_DIRECTORY);
        sendAllureReport(ALLURE_SEND_RESULTS_URL, allureFiles.toArray(new File[0]));
    }
}
