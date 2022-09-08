package dev.dl.blogservice.application.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ManageChannelConfig {

    @Value("${server.grpc.user-server}")
    private String serverAddress;

    @Bean
    public ManagedChannel userManagedChannel() {
        log.info("CREATE MANAGED CHANNEL AT {}", serverAddress);
        return ManagedChannelBuilder
                .forTarget(serverAddress)
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();
    }
}
