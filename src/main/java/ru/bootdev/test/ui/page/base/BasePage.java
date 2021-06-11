package ru.bootdev.test.ui.page.base;

import com.codeborne.selenide.*;
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

    public void waitPopup(String popupMessage) {
        $(withText(popupMessage)).should(appear);
        $(withText(popupMessage)).should(disappear);
    }

    public void closeAndSwitch() {
        String currentTab = driver.getWindowHandle();
        driver.close();
        driver.getWindowHandles().stream().filter(tab -> !tab.equals(currentTab)).findFirst()
                .ifPresent(newTab -> driver.switchTo().window(newTab));
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public String getPageUrl() {
        return driver.getCurrentUrl();
    }

    private void jsDispatchEvent(SelenideElement element, String eventClass, String eventMethod) {
        Selenide.executeJavaScript(String.format("arguments[0].dispatchEvent(new %s(\"%s\", {\"bubbles\":true}));",
                eventClass, eventMethod), element);
    }

    protected void jsClick(SelenideElement element) {
        jsDispatchEvent(element, "MouseEvent", "click");
    }

    protected void jsMouseOver(SelenideElement element) {
        jsDispatchEvent(element, "MouseEvent", "mouseover");
    }

    protected void jsMouseOut(SelenideElement element) {
        jsDispatchEvent(element, "MouseEvent", "mouseout");
    }

    protected void jsInput(SelenideElement element) {
        jsDispatchEvent(element, "InputEvent", "input");
    }

    protected void jsFocusIn(SelenideElement element) {
        jsDispatchEvent(element, "FocusEvent", "focusin");
    }

    protected void jsFocusOut(SelenideElement element) {
        jsDispatchEvent(element, "FocusEvent", "focusout");
    }

    protected void jsKeyPress(SelenideElement element) {
        jsDispatchEvent(element, "KeyboardEvent", "keypress");
    }

    protected void jsKeyDown(SelenideElement element) {
        jsDispatchEvent(element, "KeyboardEvent", "keydown");
    }

    protected void jsKeyUp(SelenideElement element) {
        jsDispatchEvent(element, "KeyboardEvent", "keyup");
    }

    protected void jsWheel(SelenideElement element) {
        jsDispatchEvent(element, "WheelEvent", "wheel");
    }

    protected void jsScroll(SelenideElement element) {
        jsDispatchEvent(element, "Event", "scroll");
    }
}
