package dev.dl.userservice.application.config;

import dev.dl.common.helper.DateTimeHelper;
import dev.dl.userservice.application.grpc.AuthServiceGrpcClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("NullableProblems")
@Component
@Slf4j
public class RequestFilter extends OncePerRequestFilter {

    @Value("${server.port}")
    private String serverPort;

    private final AuthServiceGrpcClient authServiceGrpcClient;

    @Autowired
    public RequestFilter(AuthServiceGrpcClient authServiceGrpcClient) {
        this.authServiceGrpcClient = authServiceGrpcClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String appHost = String.format(
                    "%1$s:%2$s",
                    InetAddress.getLocalHost().getHostAddress(),
                    this.serverPort
            );
            MDC.put("serviceDomain", appHost);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            MDC.put("timestamp", formatter.format(DateTimeHelper.generateCurrentTimeDefault()));
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
            String responseContent = String.format("{\" error \": %1$s}", e.getMessage());
            response.getWriter().write(responseContent);
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
