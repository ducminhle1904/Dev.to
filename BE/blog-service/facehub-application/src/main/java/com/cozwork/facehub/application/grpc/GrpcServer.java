package com.cozwork.facehub.application.grpc;

import com.cozwork.grpc.ClientInput;
import com.cozwork.grpc.ProceedMessageGrpc;
import com.cozwork.grpc.ServerOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class GrpcServer {

    @Value("${server.grpc.port}")
    private Integer port;

    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString())
                )
                .registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext) -> new JsonPrimitive(localDateTime.toString())
                )
                .registerTypeAdapter(
                        LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsJsonPrimitive().getAsString())
                )
                .registerTypeAdapter(
                        LocalDate.class,
                        (JsonSerializer<LocalDate>) (localDate, type, jsonSerializationContext) -> new JsonPrimitive(localDate.toString())
                )
                .create();
    }

    private Server server;

    private void start() throws IOException {
        server = ServerBuilder.forPort(port).addService(new ProceedMessageGrpcImpl()).build().start();

        log.info("GRPC SERVER STARTED, LISTENING ON PORT {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.error("SHUTTING DOWN gRPC SERVER");
                try {
                    server.shutdown().awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
            }
        });
    }

    public static class ProceedMessageGrpcImpl extends ProceedMessageGrpc.ProceedMessageImplBase {
        @Override
        public void proceed(ClientInput request, StreamObserver<ServerOutput> responseObserver) {
            log.info("[GRPC - INCOMING NEW REQUEST] {}", request);
            try {
                if (!StringUtils.hasText(request.getServiceName())) {
                    Map<String, String> map = new HashMap<String, String>() {{
                        put("error", "No service name");
                    }};
                    String jsonObject = (new ObjectMapper()).writeValueAsString(map);
                    String jsonMessage = GSON.toJson(map);
                    ServerOutput response = ServerOutput.newBuilder()
                            .setMessage(jsonObject)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                } else {
                    ServerOutput response = ServerOutput.newBuilder()
                            .setMessage(request.getMessage())
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
            } catch (Exception ignored) {
                responseObserver.onCompleted();
            }
        }
    }

    public void startGrpc() throws IOException, InterruptedException {
        this.start();
        this.server.awaitTermination();
    }
}
