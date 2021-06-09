package ru.bootdev.test.core.helper;

import java.util.Random;

public class TextHelper {

    public static String generateId() {
        return String.valueOf(System.nanoTime());
    }

    public static String generateRandomId(int length, int randomNumberOrigin, int randomNumberBound) {
        if (length <= 0) return "";
        StringBuilder buffer = new StringBuilder();
        new Random(System.nanoTime()).ints(length, randomNumberOrigin, randomNumberBound)
                .forEach(i -> buffer.append(buffer.length() == 0 ? 1 : i));
        return buffer.toString();
    }

    public static String generateRandomId(int length) {
        return generateRandomId(length, 0, 9);
    }
}
