package ru.bootdev.test.ui.page.base;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;

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
        return $(withText(text)).shouldBe(Condition.appear).isDisplayed();
    }
}
