package ru.bootdev.test.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static ru.bootdev.test.core.properties.WebDriverProperties.*;

public class DriverInitializer {

    private static boolean isRemoteDriver;
    private static URL remoteDriver;

    static {
        try {
            remoteDriver = new URL(REMOTE_DRIVER_HUB_URL);
            isRemoteDriver = REMOTE_DRIVER;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private DriverInitializer() {
    }

    private static ChromeOptions chromeDriverOptions() {
        ChromeOptions options = new ChromeOptions();
        String[] chromeArguments = splitArguments(CHROME_ARGUMENTS);
        options.addArguments(chromeArguments);
        options.setHeadless(HEADLESS_MODE);
        return options;
    }

    private static FirefoxOptions firefoxDriverOptions() {
        FirefoxOptions options = new FirefoxOptions();
        String[] firefoxArguments = splitArguments(FIREFOX_ARGUMENTS);
        options.addArguments(firefoxArguments);
        options.setHeadless(HEADLESS_MODE);
        return options;
    }

    private static String[] splitArguments(String arguments) {
        return Arrays.stream(arguments.split(";")).map(String::trim).toArray(String[]::new);
    }

    public static WebDriver initDriver() {
        if ("firefox".equals(DRIVER)) {
            return isRemoteDriver
                    ? new RemoteWebDriver(remoteDriver, firefoxDriverOptions())
                    : new FirefoxDriver(firefoxDriverOptions());
        } else {
            return isRemoteDriver
                    ? new RemoteWebDriver(remoteDriver, chromeDriverOptions())
                    : new ChromeDriver(chromeDriverOptions());
        }
    }
}
