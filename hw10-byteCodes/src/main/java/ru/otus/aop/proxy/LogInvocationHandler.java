package ru.otus.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import ru.otus.annotation.Log;

public class LogInvocationHandler implements InvocationHandler {

    private final TestLoggingInterface target;

    LogInvocationHandler(TestLoggingInterface target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());

        if (targetMethod.isAnnotationPresent(Log.class)) {
            String params =
                    args != null ? Arrays.stream(args).map(Object::toString).collect(Collectors.joining(",")) : "";
            System.out.println("executed method: " + method.getName() + ", param: " + params);
        }

        return method.invoke(target, args);
    }
}
