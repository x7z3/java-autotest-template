package ru.bootdev.test.core.extension;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LoggerExtension implements AfterEachCallback, BeforeEachCallback {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String MESSAGE_TEMPLATE = "↓↓↓\n>>> 🧪 TEST: {} {} {}\n";

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        logStart(extensionContext);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        logEnd(extensionContext);
    }

    private void logStart(ExtensionContext extensionContext) {
        logger.info(MESSAGE_TEMPLATE, getMethodFullName(extensionContext), "[STARTED]", "");
    }

    private void logEnd(ExtensionContext extensionContext) {
        String testDescription = String.format("%n%s%s%s%s", getDescription(extensionContext),
                getSeverity(extensionContext), getOwner(extensionContext), getTmsLink(extensionContext));
        Optional<Throwable> executionException = extensionContext.getExecutionException();

        logger.info(MESSAGE_TEMPLATE, getMethodFullName(extensionContext), executionException.isPresent()
                ? "[FAILED] ❌"
                : "[PASSED] ✔", testDescription);
    }

    private String getMethodFullName(ExtensionContext extensionContext) {
        String testClassName = extensionContext.getRequiredTestInstance().getClass().getName();
        String testMethodName = extensionContext.getRequiredTestMethod().getName() + "()";
        return testClassName + "." + testMethodName;
    }

    private String getDescription(ExtensionContext extensionContext) {
        Description description = extensionContext.getRequiredTestMethod().getAnnotation(Description.class);
        if (description != null) return "> Description: " + description.value() + "\n";
        return "";
    }

    private String getTmsLink(ExtensionContext extensionContext) {
        TmsLink tmsLink = extensionContext.getRequiredTestMethod().getAnnotation(TmsLink.class);
        if (tmsLink != null) return "> TmsLink: " + tmsLink.value() + "\n";
        return "";
    }

    private String getOwner(ExtensionContext extensionContext) {
        Owner owner = extensionContext.getRequiredTestMethod().getAnnotation(Owner.class);
        if (owner != null) return "> Owner: " + owner.value() + "\n";
        return "";
    }

    private String getSeverity(ExtensionContext extensionContext) {
        Severity severity = extensionContext.getRequiredTestMethod().getAnnotation(Severity.class);
        if (severity != null) return "> Severity: " + severity.value().toString().toUpperCase() + "\n";
        return "";
    }
}
