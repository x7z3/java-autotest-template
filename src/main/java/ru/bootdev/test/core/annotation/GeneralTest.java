package ru.bootdev.test.core.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import ru.bootdev.test.core.extension.LoggerExtension;
import ru.bootdev.test.core.extension.RerunExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({RerunExtension.class, LoggerExtension.class})
public @interface GeneralTest {

}
