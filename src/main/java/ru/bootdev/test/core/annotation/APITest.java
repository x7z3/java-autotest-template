package ru.bootdev.test.core.annotation;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.bootdev.test.core.extension.APIExtension;
import ru.bootdev.test.core.extension.RerunExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("APITest")
@ExtendWith({APIExtension.class, RerunExtension.class})
public @interface APITest {

}
