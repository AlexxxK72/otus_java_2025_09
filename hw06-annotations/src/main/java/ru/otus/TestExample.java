package ru.otus;

import static ru.otus.testFramework.assertions.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;
import ru.otus.testFramework.annotation.After;
import ru.otus.testFramework.annotation.Before;
import ru.otus.testFramework.annotation.Test;

@Slf4j
public class TestExample {

    private int a;
    private int b;

    @Before
    void before1() {
        a = 2;
        b = 3;
        log.info("Call before method: 1");
    }

    @Before
    void before2() {
        log.info("Call before method: 2");
    }

    @After
    void after1() {
        log.info("Call after method: 1");
    }

    @After
    void after2() {
        log.info("Call after method: 2");
    }

    @Test
    void testNotEquals() {
        log.info("Call test method: 1");
        b = 2;
        assertEquals(a, b);
    }

    @Test
    void testEquals() {
        log.info("Call test method: 2");
        assertEquals(a, b);
    }
}
