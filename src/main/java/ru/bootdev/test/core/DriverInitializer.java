package ru.bootdev.test.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.bootdev.test.core.properties.WebDriverProperties;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class DriverInitializer {

    private static Boolean isRemoteDriver;
    private static URL remoteDriver;
    private static final String chromeSwitches;
    private static final String firefoxSwitches;
    private static final Boolean headless;

    static {
        chromeSwitches = WebDriverProperties.CHROME_ARGUMENTS;
        firefoxSwitches = WebDriverProperties.FIREFOX_ARGUMENTS;
        headless = WebDriverProperties.HEADLESS_MODE;
        try {
            remoteDriver = new URL(WebDriverProperties.REMOTE_DRIVER_HUB);
            isRemoteDriver = WebDriverProperties.REMOTE_DRIVER;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static ChromeOptions chromeDriverOptions() {
        ChromeOptions options = new ChromeOptions();
        String[] chromeArguments = splitArguments(chromeSwitches);
        options.addArguments(chromeArguments);
        options.setHeadless(headless);
        return options;
    }

    private static FirefoxOptions firefoxDriverOptions() {
        FirefoxOptions options = new FirefoxOptions();
        String[] firefoxArguments = splitArguments(firefoxSwitches);
        options.addArguments(firefoxArguments);
        options.setHeadless(headless);
        return options;
    }

    private static String[] splitArguments(String arguments) {
        return Arrays.stream(arguments.split(";")).map(String::trim).toArray(String[]::new);
    }

    public static WebDriver initDriver() {
        if ("firefox".equals(WebDriverProperties.DRIVER)) {
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
