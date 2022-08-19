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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = {"dev.dl.common.config"})
@ComponentScan(basePackages = {"dev.dl.common.exception"})
public class UserServiceApplication {

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
}
