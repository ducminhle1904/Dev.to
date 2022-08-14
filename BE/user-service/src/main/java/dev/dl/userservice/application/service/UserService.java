package dev.dl.userservice.application.service;

import dev.dl.userservice.domain.entity.User;
import dev.dl.userservice.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService extends BaseService<User, UserRepository> {

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
    }

}
