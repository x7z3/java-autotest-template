package ru.bootdev.test.core.properties;

public class JiraProperties {

    public static final String JIRA_RESULTS_ENDPOINT = System.getProperty("jira.results.endpoint");
    public static final String JIRA_USER = System.getProperty("jira.user");
    public static final String JIRA_PASSWORD = System.getProperty("jira.password");
    public static final Boolean JIRA_IS_SEND_RESULTS_ENABLED = Boolean.parseBoolean(System.getProperty("jira.send.results", "false"));
}
