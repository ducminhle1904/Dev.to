package dev.dl.userservice.infrastructure;

import dev.dl.userservice.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByUserId(UUID userId);

}
