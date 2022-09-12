package dev.dl.userservice.integration;

import dev.dl.common.exception.DLException;
import dev.dl.common.helper.DateTimeHelper;
import dev.dl.common.helper.ObjectHelper;
import dev.dl.grpc.auth.CredentialResult;
import dev.dl.userservice.application.grpc.AuthServiceGrpcClient;
import dev.dl.userservice.application.mapper.UserMapper;
import dev.dl.userservice.application.message.KafkaRequest;
import dev.dl.userservice.application.request.AddNewUserRequest;
import dev.dl.userservice.application.request.LogInRequest;
import dev.dl.userservice.application.response.AddNewUserResponse;
import dev.dl.userservice.application.response.AuthResponse;
import dev.dl.userservice.application.response.LogInResponse;
import dev.dl.userservice.application.service.KafkaProducerService;
import dev.dl.userservice.application.service.UserService;
import dev.dl.userservice.domain.dto.UserDto;
import dev.dl.userservice.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final AuthServiceGrpcClient authServiceGrpcClient;

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public UserController(UserService userService, AuthServiceGrpcClient authServiceGrpcClient, KafkaProducerService kafkaProducerService) {
        this.userService = userService;
        this.authServiceGrpcClient = authServiceGrpcClient;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping
    public AddNewUserResponse addNewUser(@RequestBody AddNewUserRequest request) throws Exception {
        UserDto userDto = ObjectHelper.mapObjects(request, UserDto.class);
        User user = UserMapper.getInstance().dtoToEntity(userDto);
        user.setUserId(UUID.randomUUID());
        this.userService.save(user);
        return new AddNewUserResponse();
    }

    @PostMapping("/log-in")
    public LogInResponse login(@RequestBody LogInRequest request) {
        request.validate();
        CredentialResult credentialResult = this.authServiceGrpcClient.login(request.getUsername(), request.getPassword());
        if (ObjectHelper.isNullOrEmpty(credentialResult.getAccessToken())) {
            throw DLException.newBuilder()
                    .timestamp(DateTimeHelper.generateCurrentTimeDefault())
                    .message("Wrong username or password").build();
        }
        return LogInResponse.builder()
                .accessToken(credentialResult.getAccessToken())
                .expiresIn(credentialResult.getExpiresIn())
                .refreshExpiresIn(credentialResult.getRefreshExpiresIn())
                .refreshToken(credentialResult.getRefreshToken())
                .tokenType(credentialResult.getTokenType())
                .idToken(credentialResult.getIdToken())
                .notBeforePolicy(credentialResult.getNotBeforePolicy())
                .sessionState(credentialResult.getSessionState())
                .scope(credentialResult.getScope())
                .build();
    }

    @PostMapping("/validate")
    public AuthResponse check() {
        return new AuthResponse();
    }

    @PostMapping("/push")
    public String push(@RequestParam(name = "message") String message) {
        return (String) this.kafkaProducerService.send(
                new KafkaRequest(
                        null,
                        message
                ),
                "user-hub",
                0
        ).getValue();
    }
}