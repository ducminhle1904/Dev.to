package dev.dl.authorizationservice.grpc;

import dev.dl.authorizationservice.dto.KeyCloakLoginResponse;
import dev.dl.authorizationservice.dto.UserRoleDto;
import dev.dl.authorizationservice.infrastructure.RoleRepository;
import dev.dl.authorizationservice.infrastructure.UserRepository;
import dev.dl.common.constant.Constant;
import dev.dl.common.exception.DLException;
import dev.dl.common.helper.DateTimeHelper;
import dev.dl.common.helper.ObjectHelper;
import dev.dl.common.helper.RestfulHelper;
import dev.dl.common.helper.ValidateHelper;
import dev.dl.grpc.auth.AuthToken;
import dev.dl.grpc.auth.AuthenticationResult;
import dev.dl.grpc.auth.Credential;
import dev.dl.grpc.auth.CredentialResult;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@GrpcService
public class AuthServiceGrpc extends dev.dl.grpc.auth.AuthServiceGrpc.AuthServiceImplBase {

    private final UserRepository userRepository;

    @Value("${security.jwt-secret}")
    private String jwtSecret;

    @Value("${security.jwt-expiration}")
    private int jwtExpirationDay;

    @Autowired
    public AuthServiceGrpc(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void auth(AuthToken request, StreamObserver<AuthenticationResult> responseObserver) {
        log.info("[GRPC AUTH SERVICE] AUTHENTICATE A USER");
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(request.getToken()).getBody();
            hasAuthority(claims, request.getAuthorityRoleList());
            String userId = String.valueOf(claims.get("userId"));
            if (ValidateHelper.validate(Constant.UUID_REGEX, userId)) {
                List<UserRoleDto> userRoleDtos = this.userRepository.getUserRoleDto(UUID.fromString(userId));
                if (ObjectHelper.isNullOrEmpty(userRoleDtos)) {
                    throw DLException.newBuilder()
                            .timestamp(DateTimeHelper.generateCurrentTimeDefault())
                            .message("CAN NOT FIND USER")
                            .build();
                }
                AuthenticationResult.Builder authenticationResultBuilder = AuthenticationResult.newBuilder()
                        .setUserId(userId)
                        .setNonLock(userRoleDtos.get(0).getActive());
                for (int i = 0; i < userRoleDtos.size(); i++) {
                    authenticationResultBuilder.addRole(userRoleDtos.get(i).getRole());
                }
                responseObserver.onNext(authenticationResultBuilder.build());
                responseObserver.onCompleted();
            }
        } catch (Exception e) {
            responseObserver.onNext(null);
            responseObserver.onCompleted();
        }
    }

    private static void hasAuthority(Claims claims, List<String> authorities) {
        if (ObjectHelper.isNullOrEmpty(authorities)) {
            return;
        }
        List<String> tokenRole = List.of((String.valueOf(claims.get("role"))).split(","));
        for (String role : tokenRole) {
            if (authorities.contains(role)) {
                return;
            }
        }
        throw DLException.newBuilder()
                .timestamp(DateTimeHelper.generateCurrentTimeDefault())
                .message("USER ROLE NOT ACCEPTED")
                .build();
    }

    @Override
    public void login(Credential request, StreamObserver<CredentialResult> responseObserver) {
        log.info("[GRPC AUTH SERVICE] LOGIN USER {}", request.getUsername());
        if (ObjectHelper.isNullOrEmpty(request.getUsername()) || ObjectHelper.isNullOrEmpty(request.getPassword())) {
            responseObserver.onNext(null);
            responseObserver.onCompleted();
            return;
        }
        /*
         * Optional<User> optionalUser = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
         * if (optionalUser.isEmpty()) {
         *     responseObserver.onNext(null);
         *     responseObserver.onCompleted();
         *     return;
         * }
         * User user = optionalUser.get();
         * List<String> role = this.userRepository.findRoleOfUser(user.getUserId());
         * String roleStrings = String.join(",", role);
         * long millisecond = toMilliSeconds(jwtExpirationDay);
         * Date expiredDate = new Date((new Date()).getTime() + millisecond);
         * String jwtToken = Jwts.builder()
         *         .setSubject(user.getUsername())
         *         .setIssuedAt(new Date())
         *         .setExpiration(expiredDate)
         *         .signWith(SignatureAlgorithm.HS512, jwtSecret)
         *         .claim("userId", user.getUserId())
         *         .claim("role", roleStrings)
         *         .compact();
         */
        String input = String.format(
                "client_id=demo-client&client_secret=CPEhzFZZp9J254wOfwEUhp2J4yHH58nS&username=%1$s&password=%2$s&grant_type=password&scope=openid",
                request.getUsername(),
                request.getPassword()
        );
        try {
            KeyCloakLoginResponse keyCloakLoginResponse = RestfulHelper.getInstance().consume(
                    "http://localhost:2000/realms/dev/protocol/openid-connect/token",
                    new HashMap<>() {
                        {
                            put("Content-Type", "application/x-www-form-urlencoded");
                        }
                    },
                    "POST",
                    input,
                    KeyCloakLoginResponse.class,
                    null
            );
            CredentialResult credentialResult = CredentialResult.newBuilder()
                    .setAccessToken(keyCloakLoginResponse.getAccessToken())
                    .setExpiresIn(keyCloakLoginResponse.getExpiresIn())
                    .setRefreshToken(keyCloakLoginResponse.getRefreshToken())
                    .setRefreshExpiresIn(keyCloakLoginResponse.getRefreshExpiresIn())
                    .setTokenType(keyCloakLoginResponse.getTokenType())
                    .setIdToken(keyCloakLoginResponse.getIdToken())
                    .setNotBeforePolicy(keyCloakLoginResponse.getNotBeforePolicy())
                    .setSessionState(keyCloakLoginResponse.getSessionState())
                    .setScope(keyCloakLoginResponse.getScope())
                    .build();
            responseObserver.onNext(credentialResult);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(null);
            responseObserver.onCompleted();
        }
    }

    public static long toMilliSeconds(int day) {
        return (long) day * 24 * 60 * 60 * 1000;
    }
}
