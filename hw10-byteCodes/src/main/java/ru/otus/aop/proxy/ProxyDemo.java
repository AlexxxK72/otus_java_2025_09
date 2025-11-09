package ru.otus.aop.proxy;

public class ProxyDemo {
    public static void main(String[] args) {

        TestLoggingInterface testLogging = TestLogging.createTestLogging();

        testLogging.calculation(6);
        testLogging.calculation(6, 8);
    }
}
