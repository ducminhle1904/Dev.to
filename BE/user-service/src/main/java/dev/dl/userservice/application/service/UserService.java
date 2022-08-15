package dev.dl.userservice.application.service;

import dev.dl.common.exception.DLException;
import dev.dl.common.helper.DateTimeHelper;
import dev.dl.userservice.application.mapper.UserMapper;
import dev.dl.userservice.domain.dto.UserDto;
import dev.dl.userservice.domain.entity.User;
import dev.dl.userservice.infrastructure.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService extends BaseService<User, UserRepository> {

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
    }

    public UserDto findUserByUserId(UUID userId) {
        log.info("FIND USER WITH ID {}", userId);
        Optional<User> optionalUser = this.repository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            throw DLException.newBuilder()
                    .timestamp(DateTimeHelper.generateCurrentTimeDefault())
                    .message("User not exist")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
        return UserMapper.getInstance().entityToDto(optionalUser.get());
    }

}
