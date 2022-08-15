package dev.dl.userservice.application.grpc;

import dev.dl.common.helper.SHA1Helper;
import dev.dl.grpc.auth.AuthServiceGrpc;
import dev.dl.grpc.auth.AuthToken;
import dev.dl.grpc.auth.AuthenticationResult;
import dev.dl.grpc.auth.Credential;
import dev.dl.grpc.auth.CredentialResult;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceGrpcClient {

    @Value("${server.grpc.auth-server}")
    private String serverAddress;

    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder
                .forTarget(serverAddress)
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();
    }

    public CredentialResult login(String username, String password) {
        log.info("[GRPC SEND REQUEST] LOG IN FOR USER {}", username);
        Credential credential = Credential.newBuilder().setUsername(username).setPassword(SHA1Helper.encryptThisString(password)).build();
        CredentialResult result;
        try {
            final AuthServiceGrpc.AuthServiceBlockingStub blockingStub = AuthServiceGrpc.newBlockingStub(managedChannel());
            result = blockingStub.login(credential);
            return result;
        } catch (Exception e) {
            log.error("[GRPC SEND REQUEST ERROR] {}", e.getMessage());
            return null;
        } finally {
            log.info("[GRPC SEND REQUEST] COMPLETED");
        }
    }

    public AuthenticationResult auth(String token) {
        log.info("[GRPC SEND REQUEST] AUTH FOR USER");
        AuthToken authToken = AuthToken.newBuilder().setToken(token).build();
        AuthenticationResult result;
        try {
            final AuthServiceGrpc.AuthServiceBlockingStub blockingStub = AuthServiceGrpc.newBlockingStub(managedChannel());
            result = blockingStub.auth(authToken);
            return result;
        } catch (Exception e) {
            log.error("[GRPC SEND REQUEST ERROR] {}", e.getMessage());
            return null;
        } finally {
            log.info("[GRPC SEND REQUEST] COMPLETED");
        }
    }

}
