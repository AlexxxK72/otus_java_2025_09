package ru.otus;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import ru.otus.protobuf.NumberRequest;
import ru.otus.protobuf.NumberResponse;
import ru.otus.protobuf.RemoteNumberStreamServiceGrpc;

@SuppressWarnings({"squid:S106", "squid:S2142"})
@Slf4j
public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private static final int SERVER_FIRST_VALUE = 0;
    private static final int SERVER_LAST_VALUE = 30;
    private static final int CLIENT_LAST_VALUE = 50;

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = RemoteNumberStreamServiceGrpc.newBlockingStub(channel);

        var request = NumberRequest.newBuilder()
                .setFirstValue(SERVER_FIRST_VALUE)
                .setLastValue(SERVER_LAST_VALUE)
                .build();

        Iterator<NumberResponse> responses = stub.streamNumbers(request);

        AtomicInteger currentValue = new AtomicInteger();
        AtomicInteger lastServerValue = new AtomicInteger();

        // основной цикл клиента
        scheduler.scheduleAtFixedRate(
                () -> processTick(currentValue, lastServerValue, channel), 0, 1, TimeUnit.SECONDS);

        // Поток чтения стрима от сервера
        Thread readerThread = new Thread(() -> readServerStream(responses, lastServerValue), "grpc-reader");
        readerThread.setDaemon(true);
        readerThread.start();
    }

    /**
     * Читает поток чисел от сервера и сохраняет последнее полученное значение.
     */
    private static void readServerStream(Iterator<NumberResponse> responses, AtomicInteger lastServerValue) {
        try {
            while (responses.hasNext()) {
                int value = responses.next().getValue();
                lastServerValue.set(value);
                log.info("число от сервера: {}", value);
            }
        } catch (Exception e) {
            log.error("Ошибка при чтении стрима от сервера", e);
        }
    }

    /**
     * Выполняется каждую секунду: берёт последнее значение от сервера,
     * прибавляет +1 и обновляет накопленное значение.
     */
    private static void processTick(AtomicInteger currentValue, AtomicInteger lastServerValue, ManagedChannel channel) {
        int serverValue = lastServerValue.getAndSet(0);
        int newValue = currentValue.addAndGet(serverValue + 1);
        if (newValue <= CLIENT_LAST_VALUE) {
            log.info("currentValue: {}", newValue);
        } else {
            log.info("request completed");
            scheduler.shutdown();
            channel.shutdownNow();
        }
    }
}
