package ru.bootdev.test.core.properties;

public class RerunProperties {

    public static final int TEST_RERUN_COUNT = Integer.parseInt(System.getProperty("test.rerun.count", "3"));
    public static final boolean TEST_RERUN_ENABLED = Boolean.parseBoolean(System.getProperty("test.rerun.enabled", "true"));

    private RerunProperties() {
    }
}
