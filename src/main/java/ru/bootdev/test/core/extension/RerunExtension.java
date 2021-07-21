package ru.bootdev.test.core.extension;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.bootdev.test.core.helper.WebDriverHelper.initWebDriver;
import static ru.bootdev.test.core.properties.RerunProperties.TEST_RERUN_COUNT;
import static ru.bootdev.test.core.properties.RerunProperties.TEST_RERUN_ENABLED;

public class RerunExtension implements InvocationInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {
        rerun(invocation, invocationContext, extensionContext);
    }

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                            ExtensionContext extensionContext) throws Throwable {
        rerun(invocation, invocationContext, extensionContext);
    }

    @Override
    public void interceptDynamicTest(Invocation<Void> invocation, ExtensionContext extensionContext) throws Throwable {
        rerun(invocation, null, extensionContext);
    }

    private void rerun(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                       ExtensionContext extensionContext) throws Throwable {
        int rerunCount = TEST_RERUN_COUNT;
        AtomicInteger currentRun = new AtomicInteger(1);

        try {
            invocation.proceed();
        } catch (Throwable throwable) {
            if (TEST_RERUN_ENABLED) {
                Object testObject = extensionContext.getRequiredTestInstance();
                Method testMethod = extensionContext.getRequiredTestMethod();
                while (currentRun.get() <= rerunCount) {
                    logger.debug("Test run {}/{} of {}", currentRun.get(), rerunCount, extensionContext.getDisplayName());
                    try {
                        if (isWebBrowserUp()) restartWebBrowser();
                        testMethod.invoke(testObject, methodArgs(invocationContext));
                        return;
                    } catch (Exception e) {
                        currentRun.incrementAndGet();
                    }
                }
            }
            throw throwable;
        }
    }

    private Object[] methodArgs(ReflectiveInvocationContext<Method> invocationContext) {
        return invocationContext != null
                ? invocationContext.getArguments().toArray()
                : new Object[]{};
    }

    private boolean isWebBrowserUp() {
        return WebDriverRunner.getWebDriver() != null;
    }

    private void restartWebBrowser() {
        logger.debug("Restarting Web-Browser");
        WebDriverRunner.getWebDriver().close();
        initWebDriver();
    }
}
