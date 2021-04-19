package ru.bootdev.test.core.tm4j;

public class TestCaseId {

    private static final String regexp = "^(?i)([^_]+)_([^_]+)_test$";

    public static String getTestCaseIdByMethodName(String methodName) {
        if (methodName.matches(regexp)) return methodName.replaceAll(regexp, "$1-$2");
        return null;
    }
}
