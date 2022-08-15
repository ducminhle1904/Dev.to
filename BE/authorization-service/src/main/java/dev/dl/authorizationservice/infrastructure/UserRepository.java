package dev.dl.authorizationservice.infrastructure;

import dev.dl.authorizationservice.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByUserId(UUID userId);

    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    @Query(nativeQuery = true, value = "SELECT r.role_name FROM \"public\".\"user\" u LEFT JOIN \"public\".\"role_user\" ru ON ru.\"user_id\" = u.\"id\" RIGHT JOIN \"public\".\"role\" r ON r.\"id\" = ru.\"role_id\" WHERE u.\"user_id\" = :userId")
    List<String> findRoleOfUser(@Param("userId") UUID userId);
}
