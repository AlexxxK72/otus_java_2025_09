package ru.otus.testFramework.verification;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import ru.otus.testFramework.annotation.After;
import ru.otus.testFramework.annotation.Before;
import ru.otus.testFramework.annotation.Test;

@Slf4j
public class TestRunner {

    private TestRunner() {}

    public static void run(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            log.info("Starting test run for {}", clazz.getSimpleName());
            SequencedMap<String, String> results = new LinkedHashMap<>();

            List<Method> beforeMethods = getMethodsWithAnnotation(clazz, Before.class);
            List<Method> afterMethods = getMethodsWithAnnotation(clazz, After.class);
            List<Method> testMethods = getMethodsWithAnnotation(clazz, Test.class);

            testMethods.forEach(method -> {
                Object subjectTest = null;
                try {
                    results.put(method.getName(), null);

                    Constructor<?> defaultConstructor = clazz.getConstructor();
                    subjectTest = defaultConstructor.newInstance();

                    Object finalSubject = subjectTest;
                    beforeMethods.forEach(beforeMethod -> callMethod(finalSubject, beforeMethod));
                    callMethod(subjectTest, method);

                } catch (Exception e) {
                    results.put(results.lastEntry().getKey(), getErrorMessage(e));
                } finally {
                    Object finalSubject = subjectTest;
                    if (finalSubject != null) {
                        afterMethods.forEach(afterMethod -> callMethod(finalSubject, afterMethod));
                    }
                }
            });

            long passedTests = results.values().stream().filter(Objects::isNull).count();
            log.info("Total tests: {}", results.size());
            log.info("Passed: {}, failed: {}", passedTests, results.size() - passedTests);
            results.forEach((key, value) -> {
                log.info("Test: {}, {}", key, (value == null ? "Passed" : value));
            });
        } catch (Exception e) {
            log.error("Test run failed", e);
        }
    }

    public static List<Method> getMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    public static void callMethod(Object subject, Method method) {
        try {
            method.setAccessible(true);
            method.invoke(subject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getErrorMessage(Exception e) {
        String message = e.getMessage();
        Throwable cause = e;
        while (cause != null) {
            if (cause instanceof InvocationTargetException) {
                Throwable target = ((InvocationTargetException) cause).getTargetException();
                return target.getMessage() != null ? target.getMessage() : cause.getMessage();
            }
            cause = cause.getCause();
        }
        return message;
    }
}
