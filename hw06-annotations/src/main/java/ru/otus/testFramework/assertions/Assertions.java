package ru.otus.testFramework.assertions;

import java.util.Objects;

public class Assertions {

    private Assertions() {}

    public static void assertEquals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + " but found " + actual);
        }
    }
}
