package ru.bootdev.test.ui.page.base;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class BasePage {

    protected WebDriver driver = WebDriverRunner.getWebDriver();
    protected String hostUrl = System.getProperty("service.host.url");
    protected String pageUrl = "/";

    public void open() {
        open(pageUrl);
    }

    public void open(String pageUrl) {
        driver.get(hostUrl + pageUrl);
    }

    public boolean hasText(String text) {
        return $(withText(text)).should(appear).isDisplayed();
    }

    public void waitForLoad() {
        new WebDriverWait(driver, Configuration.pageLoadTimeout, 500).until(w -> isPageLoaded());
    }

    private Boolean isPageLoaded() {
        return Selenide.executeJavaScript("return document.readyState").toString().contains("complete");
    }
}
