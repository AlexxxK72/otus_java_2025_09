package ru.otus;

import java.time.LocalDateTime;
import java.util.List;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.homework.DateTimeProvider;
import ru.otus.processor.homework.ProcessorSwapFields11And12;
import ru.otus.processor.homework.ProcessorThrowExceptionOnEvenSecond;

public class HomeWork {

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */

    public static void main(String[] args) {
        // источник времени
        DateTimeProvider dateTimeProvider = LocalDateTime::now;

        // процессоры из задания
        var processors =
                List.of(new ProcessorSwapFields11And12(), new ProcessorThrowExceptionOnEvenSecond(dateTimeProvider));
        var complexProcessor =
                new ComplexProcessor(processors, ex -> System.out.println("Exception caught: " + ex.getMessage()));
        // listeners
        var historyListener = new HistoryListener();
        var consoleListener = new ListenerPrinterConsole();

        complexProcessor.addListener(historyListener);
        complexProcessor.addListener(consoleListener);

        var field13 = new ObjectForMessage();
        field13.setData(List.of("one", "two"));

        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        // обработка
        complexProcessor.handle(message);

        // читаем историю
        historyListener.findMessageById(1L).ifPresent(m -> System.out.println("From history: " + m));
    }
}
