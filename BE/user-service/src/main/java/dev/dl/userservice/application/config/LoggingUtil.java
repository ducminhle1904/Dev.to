package dev.dl.userservice.application.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.dl.common.helper.ObjectHelper;
import dev.dl.common.helper.RestfulHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Optional;

import static dev.dl.common.constant.Constant.WHILE_LIST_API;

@SuppressWarnings("AccessStaticViaInstance")
@Slf4j
public class LoggingUtil {

    private static volatile LoggingUtil INSTANCE;

    private LoggingUtil() {
    }

    public static synchronized LoggingUtil getInstance() {
        if (Optional.ofNullable(INSTANCE).isEmpty()) {
            INSTANCE = new LoggingUtil();
        }
        return INSTANCE;
    }

    private static final String REQUEST_ID = "request_id";

    @Value("${log.req-res:false}")
    private boolean isLog;

    public void logRequest(HttpServletRequest request, Object body) throws JsonProcessingException {
        if (!isLog) {
            return;
        }
        if (request.getRequestURI().contains("medias")) {
            return;
        }
        if (WHILE_LIST_API.contains(request.getRequestURI())) {
            return;
        }
        if (ObjectHelper.objectIsNull(request)) {
            throw new RuntimeException("Lỗi không xác định");
        }
        StringBuilder data = new StringBuilder();
        String requestId = (String) request.getAttribute(REQUEST_ID);
        data.append("\n-----------------------------------START LOGGING REQUEST-----------------------------------\n")
                .append("[REQUEST-ID]: ").append(requestId).append("\n")
                .append("[PATH]: ").append(request.getRequestURI()).append("\n");
        if (ObjectHelper.isNotNullAndNotEmpty(request.getQueryString())) {
            data.append("[QUERIES]: ").append(request.getQueryString()).append("\n");
        }
        data.append("[HEADERS]: ").append("\n");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            data.append("---").append(key).append(" : ").append(value).append("\n");
        }
        if (ObjectHelper.isNotNullAndNotEmpty(body)) {
            data.append("[BODY REQUEST]: \n").append(RestfulHelper.getInstance().convertObjectToJsonString(body)).append("\n\n");
        }
        data.append("-----------------------------------END LOGGING REQUEST-----------------------------------\n");

        log.info(data.toString());
    }

    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) throws JsonProcessingException {
        if (!isLog) {
            return;
        }
        if (httpServletRequest.getRequestURI().contains("medias")) {
            return;
        }
        if (WHILE_LIST_API.contains(httpServletRequest.getRequestURI())) {
            return;
        }
        if (ObjectHelper.objectIsNull(httpServletResponse) || ObjectHelper.objectIsNull(httpServletRequest)) {
            throw new RuntimeException("Lỗi không xác định");
        }
        Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
        StringBuilder data = new StringBuilder();
        try {
            Assert.notNull(body, "Can not null");
            data.append("\n-----------------------------------START LOGGING RESPONSE-----------------------------------\n")
                    .append("[REQUEST-ID]: ").append(requestId).append("\n")
                    .append("[BODY RESPONSE]: ").append("\n\n")
                    .append(RestfulHelper.getInstance().convertObjectToJsonString(body))
                    .append("\n\n");
        } catch (Exception e) {
            data.setLength(0);
            data.append("\n-----------------------------------START LOGGING RESPONSE-----------------------------------\n")
                    .append("[REQUEST-ID]: ").append(requestId).append("\n");
        }
        data.append("[STATUS]: ").append(httpServletResponse.getStatus()).append("\n");
        data.append("[HEADERS]: ").append("\n");
        Collection<String> headerNames = httpServletResponse.getHeaderNames();
        headerNames.forEach(header -> {
            String value = httpServletResponse.getHeader(header);
            data.append("---").append(header).append(" : ").append(value).append("\n");
        });
        data.append("-----------------------------------END LOGGING RESPONSE-----------------------------------\n");

        log.info(data.toString());
    }
}
