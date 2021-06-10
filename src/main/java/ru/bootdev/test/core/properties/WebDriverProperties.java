package ru.bootdev.test.core.properties;

public class WebDriverProperties {

    public static final String DRIVER = System.getProperty("webdriver.driver", "chrome").toLowerCase();
    public static final Boolean REMOTE_DRIVER = Boolean.parseBoolean(System.getProperty("webdriver.remote", "true"));
    public static final Boolean HEADLESS_MODE = Boolean.parseBoolean(System.getProperty("webdriver.headless", "false"));
    public static final String REMOTE_DRIVER_HUB_URL = System.getProperty("webdriver.remote.hub", "http://localhost:4444/wd/hub");
    public static final String CHROME_ARGUMENTS = System.getProperty("chrome.switches", "--window-size=1920,1080; --incognito");
    public static final String FIREFOX_ARGUMENTS = System.getProperty("firefox.switches", "-private; -width; 1920; -height; 1080");
    public static final Boolean WINDOW_MAXIMIZE = Boolean.parseBoolean(System.getProperty("webdriver.window.maximize", "false"));
}