package ru.otus.service;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import ru.otus.protobuf.NumberRequest;
import ru.otus.protobuf.NumberResponse;
import ru.otus.protobuf.RemoteNumberStreamServiceGrpc;

@SuppressWarnings({"squid:S2142", "squid:S106"})
public class RemoteNumberStreamService extends RemoteNumberStreamServiceGrpc.RemoteNumberStreamServiceImplBase {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void streamNumbers(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {

        int firstValue = request.getFirstValue();
        int lastValue = request.getLastValue();

        final int[] current = {firstValue};

        scheduler.scheduleAtFixedRate(
                () -> {
                    if (current[0] > lastValue) {
                        responseObserver.onCompleted();
                        return;
                    }

                    NumberResponse response =
                            NumberResponse.newBuilder().setValue(++current[0]).build();

                    responseObserver.onNext(response);
                },
                0,
                2,
                TimeUnit.SECONDS);
    }
}
