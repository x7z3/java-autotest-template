package ru.bootdev.test.core.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static ru.bootdev.test.core.helper.WebDriverHelper.initWebDriver;

public class UIExtension implements BeforeEachCallback, TestWatcher {

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        initWebDriver();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        addAllureAttachment();
        WebDriverRunner.getWebDriver().close();
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        WebDriverRunner.getWebDriver().close();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        WebDriverRunner.getWebDriver().close();
    }

    private File takeScreenshot() {
        return Selenide.screenshot(OutputType.FILE);
    }

    private void addAllureAttachment() {
        File screenshot = takeScreenshot();
        try (InputStream is = Files.newInputStream(screenshot.toPath())) {
            Allure.addAttachment("Screenshot", "image/png", is, "png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
