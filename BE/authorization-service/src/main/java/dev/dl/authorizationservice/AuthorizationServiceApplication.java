package dev.dl.authorizationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class AuthorizationServiceApplication {

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
        ConfigurableApplicationContext context = SpringApplication.run(AuthorizationServiceApplication.class, args);
    }

}
