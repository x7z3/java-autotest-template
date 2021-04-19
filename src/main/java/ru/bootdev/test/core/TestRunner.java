package ru.bootdev.test.core;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class TestRunner {

    public static void runPackage(String packageName, String classFilter) {
        runInstance(requestBuilder -> requestBuilder
                .selectors(selectPackage(packageName))
                .filters(includeClassNamePatterns(classFilter)));
    }

    public static void runClasses(Class<?>... javaClasses) {
        if (javaClasses.length == 0) return;
        runInstance(requestBuilder -> Arrays.stream(javaClasses)
                .forEach(className -> requestBuilder.selectors(selectClass(className))));
    }

    public static void runMethod(Class<?> javaClass, String... methodNames) {
        if (methodNames.length == 0) return;
        runInstance(requestBuilder -> Arrays.stream(methodNames)
                .forEach(className -> requestBuilder.selectors(selectClass(className))));
    }

    private static void runInstance(Consumer<LauncherDiscoveryRequestBuilder> requestBuilderConsumer) {
        LauncherDiscoveryRequestBuilder requestBuilder = LauncherDiscoveryRequestBuilder.request();
        requestBuilderConsumer.accept(requestBuilder);
        LauncherDiscoveryRequest request = requestBuilder.build();
        Launcher launcher = LauncherFactory.create();
        launcher.execute(request);
    }
}
