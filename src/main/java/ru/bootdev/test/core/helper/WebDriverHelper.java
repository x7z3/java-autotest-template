package ru.bootdev.test.core.helper;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import ru.bootdev.test.core.DriverInitializer;

import static ru.bootdev.test.core.properties.WebDriverProperties.WINDOW_MAXIMIZE;

public class WebDriverHelper {

    public static final String ABOUT_BLANK = "about:blank";

    private WebDriverHelper() {
    }

    public static void initWebDriver() {
        WebDriver driver = DriverInitializer.getWebDriver();
        WebDriverRunner.setWebDriver(driver);
        driver.get(ABOUT_BLANK);
        if (WINDOW_MAXIMIZE) driver.manage().window().maximize();
    }
}
