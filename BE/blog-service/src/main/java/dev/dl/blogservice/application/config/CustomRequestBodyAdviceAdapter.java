package dev.dl.blogservice.application.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.dl.common.config.LoggingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("ALL")
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    private final dev.dl.common.config.LoggingUtil loggingService = LoggingUtil.getInstance();

    private final HttpServletRequest httpServletRequest;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            loggingService.logRequest(httpServletRequest, body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}

