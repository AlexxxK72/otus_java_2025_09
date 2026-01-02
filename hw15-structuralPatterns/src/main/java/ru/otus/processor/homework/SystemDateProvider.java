package ru.otus.processor.homework;

import java.time.LocalDateTime;

public class SystemDateProvider implements DateTimeProvider {
    @Override
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
