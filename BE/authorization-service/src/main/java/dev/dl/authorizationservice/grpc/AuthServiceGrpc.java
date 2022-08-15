package dev.dl.authorizationservice.grpc;

import dev.dl.authorizationservice.entity.User;
import dev.dl.authorizationservice.infrastructure.RoleRepository;
import dev.dl.authorizationservice.infrastructure.UserRepository;
import dev.dl.common.constant.Constant;
import dev.dl.common.exception.DLException;
import dev.dl.common.helper.DateTimeHelper;
import dev.dl.common.helper.ObjectHelper;
import dev.dl.common.helper.ValidateHelper;
import dev.dl.grpc.auth.AuthToken;
import dev.dl.grpc.auth.AuthenticationResult;
import dev.dl.grpc.auth.Credential;
import dev.dl.grpc.auth.CredentialResult;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
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
                Optional<User> optionalUser = this.userRepository.findByUserId(UUID.fromString(userId));
                if (optionalUser.isEmpty()) {
                    throw DLException.newBuilder()
                            .timestamp(DateTimeHelper.generateCurrentTimeDefault())
                            .message("CAN NOT FIND USER")
                            .build();
                }
                User user = optionalUser.get();
                List<String> roles = user.getRoleUsers().stream().map(roleUser -> roleUser.getRole().getRoleName()).collect(Collectors.toList());
                AuthenticationResult.Builder authenticationResultBuilder = AuthenticationResult.newBuilder()
                        .setUserId(user.getUserId().toString())
                        .setLock(user.isActive());
                for (int i = 0; i < roles.size(); i++) {
                    authenticationResultBuilder.setRole(i, roles.get(i));
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
        if (!ObjectHelper.isNullOrEmpty(authorities)) {
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
        Optional<User> optionalUser = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        if (optionalUser.isEmpty()) {
            responseObserver.onNext(null);
            responseObserver.onCompleted();
            return;
        }
        User user = optionalUser.get();
        long millisecond = toMilliSeconds(jwtExpirationDay);
        String roleList = user.getRoleUsers().stream().map(roleUser -> roleUser.getRole().getRoleName()).collect(Collectors.joining(","));
        Date expiredDate = new Date((new Date()).getTime() + millisecond);
        String jwtToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .claim("role", roleList)
                .claim("userId", user.getUserId())
                .compact();
        CredentialResult credentialResult = CredentialResult.newBuilder().setToken(jwtToken).build();
        responseObserver.onNext(credentialResult);
        responseObserver.onCompleted();
    }

    public static long toMilliSeconds(int day) {
        return (long) day * 24 * 60 * 60 * 1000;
    }
}
