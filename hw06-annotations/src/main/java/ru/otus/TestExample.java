package ru.otus;

import static ru.otus.testFramework.assertions.Assertions.assertEquals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.testFramework.annotation.After;
import ru.otus.testFramework.annotation.Before;
import ru.otus.testFramework.annotation.Test;

public class TestExample {

    private static final Logger log = LoggerFactory.getLogger(TestExample.class);
    private int a;
    private int b;

    @Before
    void before1() {
        a = 2;
        b = 2;
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
    void testEquals() {
        log.info("Call test method: 1");
        assertEquals(a, b);
    }

    @Test
    void testNotEquals() {
        log.info("Call test method: 2");
        a = 3;
        assertEquals(a, b);
    }
}
