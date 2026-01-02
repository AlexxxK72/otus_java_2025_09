package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorThrowExceptionOnEvenSecond implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowExceptionOnEvenSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        int second = dateTimeProvider.getCurrentDateTime().getSecond();
        if (second % 2 == 0) {
            throw new RuntimeException("This is an even second");
        }
        return message;
    }
}
