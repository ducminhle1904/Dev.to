package dev.dl.authorizationservice.infrastructure;

import dev.dl.authorizationservice.entity.Role;

import java.util.Optional;

public interface RoleRepository extends BaseRepository<Role> {

    Optional<Role> findByRoleName(String roleName);

}
