package dev.dl.userservice.application.config;

import dev.dl.common.exception.DLException;
import dev.dl.common.helper.DateTimeHelper;
import dev.dl.common.helper.RestfulHelper;
import dev.dl.userservice.application.constant.AppConstant;
import dev.dl.userservice.application.grpc.AuthServiceGrpcClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_SERVICE_UNAVAILABLE;

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
            authenticate(request.getRequestURI(), request);
        } catch (DLException e) {
            response.setStatus(SC_SERVICE_UNAVAILABLE);
            PrintWriter out = response.getWriter();
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            out.print(RestfulHelper.convertObjectToJsonString(e.bodyBuild()));
            out.flush();
            out.close();
            return;
        }
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

    public void authenticate(String uri, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        List<String> authority = null;
        boolean isAuthenticate = false;
        for (String key : AppConstant.AUTHENTICATE_API.keySet()) {
            if (key.equalsIgnoreCase(uri)) {
                authority = AppConstant.AUTHENTICATE_API.get("key");
                isAuthenticate = true;
                break;
            }
        }
        if (isAuthenticate) {
            if (Optional.ofNullable(authServiceGrpcClient.auth(token, authority)).isEmpty()) {
                throw DLException.newBuilder()
                        .httpStatus(HttpStatus.UNAUTHORIZED)
                        .message("unauthorized")
                        .timestamp(DateTimeHelper.generateCurrentTimeDefault())
                        .build();
            }
        }
    }
}
