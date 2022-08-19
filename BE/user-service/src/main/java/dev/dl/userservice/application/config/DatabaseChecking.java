package dev.dl.userservice.application.config;

import dev.dl.common.constant.Constant;
import dev.dl.common.helper.SHA1Helper;
import dev.dl.userservice.domain.entity.Role;
import dev.dl.userservice.domain.entity.RoleUser;
import dev.dl.userservice.domain.entity.User;
import dev.dl.userservice.infrastructure.RoleRepository;
import dev.dl.userservice.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DatabaseChecking implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public DatabaseChecking(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalAdminRole = roleRepository.findByRoleName(Constant.ADMIN_ROLE);
        if (optionalAdminRole.isEmpty()) {
            Role role = new Role();
            role.setRoleName(Constant.ADMIN_ROLE);
            role.setRoleDescription(Constant.ADMIN_ROLE);
            roles.add(this.roleRepository.save(role));
        } else {
            roles.add(optionalAdminRole.get());
        }
        Optional<Role> optionalUserRole = roleRepository.findByRoleName(Constant.USER_ROLE);
        if (optionalUserRole.isEmpty()) {
            Role role = new Role();
            role.setRoleName(Constant.USER_ROLE);
            role.setRoleDescription(Constant.USER_ROLE);
            roles.add(this.roleRepository.save(role));
        } else {
            roles.add(optionalUserRole.get());
        }
        Optional<Role> optionalModeratorRole = roleRepository.findByRoleName(Constant.MODERATOR_ROLE);
        if (optionalModeratorRole.isEmpty()) {
            Role role = new Role();
            role.setRoleName(Constant.MODERATOR_ROLE);
            role.setRoleDescription(Constant.MODERATOR_ROLE);
            roles.add(this.roleRepository.save(role));
        } else {
            roles.add(optionalModeratorRole.get());
        }
        Optional<User> optionalUser = this.userRepository.findByFirstNameAndLastName("SYSTEM", Constant.ADMIN_ROLE);
        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setUserId(UUID.randomUUID());
            user.setFirstName("SYSTEM");
            user.setLastName(Constant.ADMIN_ROLE);
            user.setUsername("admin");
            user.setCreatedBy(Constant.ADMIN_ROLE);
            user.setUpdatedBy(Constant.ADMIN_ROLE);
            user.setPassword(SHA1Helper.encryptThisString("admin"));
            User user1 = this.userRepository.save(user);
            List<RoleUser> roleUsers = new ArrayList<>();
            roles.forEach(role -> {
                RoleUser roleUser = new RoleUser();
                roleUser.setUser(user1);
                roleUser.setRole(role);
                roleUsers.add(roleUser);
            });
            user1.setRoleUsers(roleUsers);
            this.userRepository.save(user1);
        }
    }
}
