package dev.dl.blogservice.application.grpc;

import dev.dl.grpc.user.User;
import dev.dl.grpc.user.UserId;
import dev.dl.grpc.user.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrpcService {

    @Value("${server.grpc.server}")
    private String serverAddress;

    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder
                .forTarget(serverAddress)
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();
    }

    public User findUserById(long id) {
        log.info("[GRPC SEND REQUEST] FIND USER WITH ID {}", id);
        UserId userIdRequest = UserId.newBuilder().setUserId(String.valueOf(id)).build();
        User response;
        try {
            final UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(managedChannel());
            response = blockingStub.findUserById(userIdRequest);
            return response;
        } catch (Exception e) {
            log.error("[GRPC SEND REQUEST ERROR] {}", e.getMessage());
            return null;
        } finally {
            log.info("[GRPC SEND REQUEST] COMPLETED");
        }
    }
}
