package ru.otus;

import io.grpc.ServerBuilder;
import java.io.IOException;
import ru.otus.service.RemoteNumberStreamService;

@SuppressWarnings({"squid:S106"})
public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var remoteNSServiceImpl = new RemoteNumberStreamService();

        var server = ServerBuilder.forPort(SERVER_PORT)
                .addService(remoteNSServiceImpl)
                .build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
