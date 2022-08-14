package dev.dl.userservice.application.service;

import dev.dl.userservice.domain.entity.User;
import dev.dl.userservice.infrastructure.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService extends BaseService<User, UserRepository> {

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
    }

}
