package dev.dl.userservice;

import dev.dl.common.constant.Constant;
import dev.dl.common.helper.SHA1Helper;
import dev.dl.userservice.application.grpc.GrpcServer;
import dev.dl.userservice.domain.entity.Role;
import dev.dl.userservice.domain.entity.RoleUser;
import dev.dl.userservice.domain.entity.User;
import dev.dl.userservice.infrastructure.RoleRepository;
import dev.dl.userservice.infrastructure.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@Slf4j
public class UserServiceApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static void setProperty() {
        String directory = String.format("%1$s%2$slog", System.getProperty("user.dir"), File.separator);
        String destination = String.format("%1$s%2$s%3$s", directory, File.separator, "log.log");
        System.setProperty("logging.file.name", destination);
        String logPattern = "%d{HH:mm:ss.SSS dd/MM/yyyy} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%15.15t] %-40.40logger{1} : [%X{clientIP}] [%X{httpMethod}] [%X{serviceDomain}] [%X{operatorName}] [%X{requestId}] [%X{timestamp}] >>>>>>>>>>>> %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}";
        System.setProperty("logging.pattern.file", logPattern);
        System.setProperty("logging.pattern.console", logPattern);
        String logStoreFolder = String.format(
                "%1$s%2$s%3$s",
                System.getProperty("user.dir"),
                File.separator,
                "store");
        System.setProperty("logging.store.folder", logStoreFolder);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        setProperty();
        ConfigurableApplicationContext context = SpringApplication.run(UserServiceApplication.class, args);
        final GrpcServer grpcServer = context.getBean(GrpcServer.class);
        grpcServer.startGrpc();
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
