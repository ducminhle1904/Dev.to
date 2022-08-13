package com.cozwork.facehub.application.grpc;

import com.cozwork.grpc.ClientInput;
import com.cozwork.grpc.ProceedMessageGrpc;
import com.cozwork.grpc.ServerOutput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class GrpcService {

    @Value("${server.grpc.server}")
    private String serverAddress;

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

    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder
                .forTarget(serverAddress)
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();
    }

    @SuppressWarnings("RedundantClassCall")
    public <T> T sendRequest(Object request, String serviceName, Class<T> outputClass) {
        log.info("[GRPC SEND REQUEST] {} with service name {}", request, serviceName);
        String requestJsonString = request instanceof String ? String.class.cast(request) : GSON.toJson(request);
        ClientInput clientInput = ClientInput.newBuilder().setMessage(requestJsonString).setServiceName(serviceName).build();
        ServerOutput serverOutput;
        try {
            final ProceedMessageGrpc.ProceedMessageBlockingStub blockingStub = ProceedMessageGrpc.newBlockingStub(managedChannel());
            serverOutput = blockingStub.proceed(clientInput);
            log.info("[GRPC RESPONSE] {}", serverOutput);
            try {
                return GSON.fromJson(serverOutput.getMessage(), outputClass);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception e) {
            log.error("[GRPC SEND REQUEST ERROR] {}", e.getMessage());
            return null;
        } finally {
            log.info("[GRPC SEND REQUEST] COMPLETED");
        }
    }

}
