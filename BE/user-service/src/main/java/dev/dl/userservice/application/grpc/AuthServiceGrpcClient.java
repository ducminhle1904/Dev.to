package dev.dl.userservice.application.grpc;

import dev.dl.common.helper.ObjectHelper;
import dev.dl.common.helper.SHA1Helper;
import dev.dl.grpc.auth.AuthServiceGrpc;
import dev.dl.grpc.auth.AuthToken;
import dev.dl.grpc.auth.AuthenticationResult;
import dev.dl.grpc.auth.Credential;
import dev.dl.grpc.auth.CredentialResult;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class AuthServiceGrpcClient {

    @Value("${server.grpc.auth-server}")
    private String serverAddress;

    private final ManagedChannel authManagedChannel;

    @Autowired
    public AuthServiceGrpcClient(ManagedChannel authManagedChannel) {
        this.authManagedChannel = authManagedChannel;
    }


    public CredentialResult login(String username, String password) {
        log.info("[GRPC SEND REQUEST] LOG IN FOR USER {}", username);
        Credential credential = Credential.newBuilder().setUsername(username).setPassword(password).build();
        CredentialResult result;
        try {
            final AuthServiceGrpc.AuthServiceBlockingStub blockingStub = AuthServiceGrpc.newBlockingStub(authManagedChannel);
            result = blockingStub.login(credential);
            return result;
        } catch (Exception e) {
            log.error("[GRPC SEND REQUEST ERROR] {}", e.getMessage());
            return null;
        } finally {
            log.info("[GRPC SEND REQUEST] COMPLETED");
        }
    }

    public AuthenticationResult auth(String token, List<String> authority) {
        log.info("[GRPC SEND REQUEST] AUTH FOR USER");
        try {
            AuthToken.Builder authToken = AuthToken.newBuilder();
            if (StringUtils.hasText(token)) {
                authToken.setToken(token);
            }
            if (!ObjectHelper.isNullOrEmpty(authority)) {
                authToken.addAllAuthorityRole(authority);
            }
            AuthenticationResult result;
            final AuthServiceGrpc.AuthServiceBlockingStub blockingStub = AuthServiceGrpc.newBlockingStub(authManagedChannel);
            result = blockingStub.auth(authToken.build());
            return result;
        } catch (Exception e) {
            log.error("[GRPC SEND REQUEST ERROR] {}", e.getMessage());
            return null;
        } finally {
            log.info("[GRPC SEND REQUEST] COMPLETED");
        }
    }

}
