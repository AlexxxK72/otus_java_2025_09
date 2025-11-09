package ru.otus.aop.proxy;

import java.lang.reflect.Proxy;
import ru.otus.annotation.Log;

public class TestLogging implements TestLoggingInterface {

    private TestLogging() {}

    public static TestLoggingInterface createTestLogging() {
        return (TestLoggingInterface) Proxy.newProxyInstance(
                TestLogging.class.getClassLoader(),
                new Class<?>[] {TestLoggingInterface.class},
                new LogInvocationHandler(new TestLogging()));
    }

    @Log
    public void calculation(int param) {}

    public void calculation(int param1, int param2) {}
}
