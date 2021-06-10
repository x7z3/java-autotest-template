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

import static ru.bootdev.test.core.properties.WebDriverProperties.*;

public class DriverInitializer {

    private static Boolean isRemoteDriver;
    private static URL remoteDriver;
    private static final String chromeSwitches;
    private static final String firefoxSwitches;
    private static final Boolean headless;

    static {
        chromeSwitches = CHROME_ARGUMENTS;
        firefoxSwitches = FIREFOX_ARGUMENTS;
        headless = HEADLESS_MODE;
        try {
            remoteDriver = new URL(REMOTE_DRIVER_HUB_URL);
            isRemoteDriver = REMOTE_DRIVER;
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
