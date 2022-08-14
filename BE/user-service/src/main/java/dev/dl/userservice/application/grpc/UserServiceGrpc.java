package dev.dl.userservice.application.grpc;

import dev.dl.grpc.user.User;
import dev.dl.grpc.user.UserId;
import dev.dl.userservice.application.service.UserService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceGrpc extends dev.dl.grpc.user.UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;

    @Autowired
    public UserServiceGrpc(UserService userService) {
        this.userService = userService;
    }



    @Override
    public void findUserById(UserId request, StreamObserver<User> responseObserver) {
        log.info("[GRPC USER SERVICE] FIND USER BY ID {}", request.getUserId());
        if (!StringUtils.hasText(request.getUserId())) {
            responseObserver.onNext(null);
            responseObserver.onCompleted();
            return;
        }
        long userId = Long.parseLong(request.getUserId());
        Optional<dev.dl.userservice.domain.entity.User> optionalUser = this.userService.findById(userId);
        if (optionalUser.isEmpty()) {
            responseObserver.onNext(null);
            responseObserver.onCompleted();
            return;
        }
        dev.dl.userservice.domain.entity.User user = optionalUser.get();
        User response = User.newBuilder()
                .setUserId(user.getUserId().toString())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}