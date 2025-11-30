package ru.otus.aop.asm;

import java.util.logging.Logger;
import ru.otus.annotation.Log;

public class TestLogging {
    Logger log = Logger.getLogger(TestLogging.class.getName());

    @Log
    public void calculation(int param) {
        log.info("invoke calculation " + param);
    }

    public void calculation(int param1, int param2) {
        log.info("invoke calculation " + param1 + " " + param2);
    }
}
