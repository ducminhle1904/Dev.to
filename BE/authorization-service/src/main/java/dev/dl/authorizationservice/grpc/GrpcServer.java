package dev.dl.authorizationservice.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class GrpcServer {

    @Value("${server.grpc.port}")
    private Integer port;

    private Server server;

    private final AuthServiceGrpc authServiceGrpc;

    @Autowired
    public GrpcServer(AuthServiceGrpc authServiceGrpc) {
        this.authServiceGrpc = authServiceGrpc;
    }

    private void start() throws IOException {
        server = ServerBuilder.forPort(port).addService(authServiceGrpc).build().start();

        log.info("GRPC SERVER STARTED, LISTENING ON PORT {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("SHUTTING DOWN gRPC SERVER");
            try {
                server.shutdown().awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }));
    }

    public void startGrpc() throws IOException, InterruptedException {
        this.start();
        this.server.awaitTermination();
    }
}
