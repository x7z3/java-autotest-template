package ru.bootdev.test.ui.page.base;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class BasePage {

    protected WebDriver driver = WebDriverRunner.getWebDriver();

    public boolean hasText(String text) {
        return $(withText(text)).shouldBe(Condition.appear).isDisplayed();
    }

    // todo
}
