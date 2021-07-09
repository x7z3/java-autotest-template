package ru.bootdev.test.core.tm4j;

public class TestCaseId {

    private static final String REGEXP = "^(?i)([^_]+)_([^_]+)_test$";

    private TestCaseId() {
    }

    public static String getTestCaseIdByMethodName(String methodName) {
        if (methodName.matches(REGEXP)) return methodName.replaceAll(REGEXP, "$1-$2");
        return null;
    }
}
