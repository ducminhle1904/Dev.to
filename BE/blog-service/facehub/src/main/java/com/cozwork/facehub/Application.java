package com.cozwork.facehub;

import com.cozwork.facehub.application.grpc.GrpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        final GrpcServer grpcServer = context.getBean(GrpcServer.class);
        grpcServer.startGrpc();
    }

}
