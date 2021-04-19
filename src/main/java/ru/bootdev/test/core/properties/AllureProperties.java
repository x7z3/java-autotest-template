package ru.bootdev.test.core.properties;

public class AllureProperties {

    public static final String ALLURE_SERVER_BASE_URL = System.getProperty("allure.server.baseUrl");
    public static final String ALLURE_RESULTS_DIRECTORY = System.getProperty("allure.results.directory");
    public static final String ALLURE_PROJECT_NAME = System.getProperty("allure.project.name");
    public static final Boolean ALLURE_IS_SEND_RESULTS_ENABLED = Boolean.parseBoolean(System.getProperty("allure.send.results", "false"));
    public static final String ALLURE_SEND_RESULTS_ENDPOINT = "send-results";
    public static final String ALLURE_SEND_RESULTS_URL;

    static {
        ALLURE_SEND_RESULTS_URL = String.format("%s/%s?project_id%s", ALLURE_SERVER_BASE_URL, ALLURE_SEND_RESULTS_ENDPOINT, ALLURE_PROJECT_NAME);
    }
}
