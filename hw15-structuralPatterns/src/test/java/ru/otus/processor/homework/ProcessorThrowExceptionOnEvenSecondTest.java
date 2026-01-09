package ru.otus.processor.homework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

class ProcessorThrowExceptionOnEvenSecondTest {

    @Test
    @DisplayName("Исключение выбрасывается в чётную секунду")
    void shouldThrowExceptionOnEvenSecond() {

        DateTimeProvider dateTimeProvider = () -> LocalDateTime.of(2024, 1, 1, 12, 0, 2); // чётная секунда

        Processor processor = new ProcessorThrowExceptionOnEvenSecond(dateTimeProvider);

        Message message = new Message.Builder(1L).build();

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> processor.process(message))
                .withMessage("This is an even second");
    }

    @Test
    @DisplayName("Сообщение проходит в нечётную секунду")
    void shouldNotThrowExceptionOnOddSecond() {

        DateTimeProvider dateTimeProvider = () -> LocalDateTime.of(2024, 1, 1, 12, 0, 3); // нечётная секунда

        Processor processor = new ProcessorThrowExceptionOnEvenSecond(dateTimeProvider);

        Message message = new Message.Builder(1L).build();
        Message result = processor.process(message);

        assertThat(result).isSameAs(message);
    }
}
