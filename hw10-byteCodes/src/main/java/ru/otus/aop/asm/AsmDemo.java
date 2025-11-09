package ru.otus.aop.asm;

public class AsmDemo {
    public static void main(String[] args) {

        TestLogging testLogging = new TestLogging();
        testLogging.calculation(6);
        testLogging.calculation(6, 3);
    }
}
