package dev.dl.authorizationservice.infrastructure;

import dev.dl.authorizationservice.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByUserId(UUID userId);

    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

}
