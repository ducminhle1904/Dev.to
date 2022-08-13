package dev.dl.common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.dl.common.constant.Constant;
import dev.dl.common.exception.ExceptionResponse;
import dev.dl.common.helper.ObjectHelper;
import dev.dl.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@RequiredArgsConstructor
@SuppressWarnings("ALL")
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    private final LoggingUtil loggingService = LoggingUtil.getInstance();

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        if (o instanceof BaseResponse) {
            try {
                ((BaseResponse) o).setRequestId(
                        (String) ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getAttribute("request_id")
                );
                if (ObjectHelper.isNullOrEmpty(((BaseResponse) o).getResponseStatus())) {
                    if (o instanceof ExceptionResponse) {
                        ((BaseResponse) o).setResponseStatus(
                                Constant.ERROR
                        );
                    } else {
                        ((BaseResponse) o).setResponseStatus(
                                Constant.SUCCESS
                        );
                    }
                }
            } catch (Exception ignored) {

            }
        }
        if (serverHttpRequest instanceof ServletServerHttpRequest &&
                serverHttpResponse instanceof ServletServerHttpResponse) {
            try {
                loggingService.logResponse(
                        ((ServletServerHttpRequest) serverHttpRequest).getServletRequest(),
                        ((ServletServerHttpResponse) serverHttpResponse).getServletResponse(),
                        o);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return o;
    }
}