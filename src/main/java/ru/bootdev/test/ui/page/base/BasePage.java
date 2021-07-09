package ru.bootdev.test.ui.page.base;

import com.codeborne.selenide.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static ru.bootdev.test.ui.page.base.JavaScriptEvent.*;

public class BasePage {

    protected WebDriver driver = WebDriverRunner.getWebDriver();
    protected String hostUrl = System.getProperty("web.host.url");
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
        Selenide.executeJavaScript(String.format("arguments[0].dispatchEvent(new %s('%s', {'bubbles':true}));",
                eventClass, eventMethod), element);
    }

    protected void jsClick(SelenideElement element) {
        jsDispatchEvent(element, MOUSE_EVENT, "click");
    }

    protected void jsMouseOver(SelenideElement element) {
        jsDispatchEvent(element, MOUSE_EVENT, "mouseover");
    }

    protected void jsMouseOut(SelenideElement element) {
        jsDispatchEvent(element, MOUSE_EVENT, "mouseout");
    }

    protected void jsInput(SelenideElement element) {
        jsDispatchEvent(element, INPUT_EVENT, "input");
    }

    protected void jsFocusIn(SelenideElement element) {
        jsDispatchEvent(element, FOCUS_EVENT, "focusin");
    }

    protected void jsFocusOut(SelenideElement element) {
        jsDispatchEvent(element, FOCUS_EVENT, "focusout");
    }

    protected void jsKeyPress(SelenideElement element) {
        jsDispatchEvent(element, KEYBOARD_EVENT, "keypress");
    }

    protected void jsKeyDown(SelenideElement element) {
        jsDispatchEvent(element, KEYBOARD_EVENT, "keydown");
    }

    protected void jsKeyUp(SelenideElement element) {
        jsDispatchEvent(element, KEYBOARD_EVENT, "keyup");
    }

    protected void jsWheel(SelenideElement element) {
        jsDispatchEvent(element, WHEEL_EVENT, "wheel");
    }

    protected void jsScroll(SelenideElement element) {
        jsDispatchEvent(element, EVENT, "scroll");
    }

    protected void click(SelenideElement element) {
        element.should(appear).click();
    }
}
