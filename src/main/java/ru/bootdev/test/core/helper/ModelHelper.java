package ru.bootdev.test.core.helper;

import java.lang.reflect.Field;

public class ModelHelper {

    public static void printObjectFields(Object object) {
        if (object == null) return;
        System.out.println("\n" + object.getClass().getTypeName());
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                System.out.printf("%s %s = %s\n", field.getType().getSimpleName(), field.getName(), field.get(object));
            } catch (IllegalAccessException ignore) {
            }
            field.setAccessible(false);
        }
    }
}
