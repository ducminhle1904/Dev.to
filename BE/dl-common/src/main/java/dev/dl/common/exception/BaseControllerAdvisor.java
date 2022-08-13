package dev.dl.common.exception;

import dev.dl.common.constant.Constant;
import dev.dl.common.helper.ObjectHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@SuppressWarnings("NullableProblems")
@RestControllerAdvice
@Slf4j
public class BaseControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DLException.class})
    public ResponseEntity<Object> handleException(DLException ex, WebRequest request) {
        log.error(
                "[DL EXCEPTION] {}",
                ObjectHelper.isNullOrEmpty(ex.getMessage()) ? "Null pointer exception" : ex.getMessage()
        );
        return this.buildErrorResponseBody(ex);
    }

    private ResponseEntity<Object> buildErrorResponseBody(DLException ex) {
        if (ObjectHelper.isNullOrEmpty(ex.getHttpStatus())) {
            ex.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
        try {
            ExceptionResponse response = ObjectHelper.mapObjects(ex, ExceptionResponse.class);
            response.setResponseMessage(ex.getMessage());
            return ResponseEntity.status(ex.getHttpStatus()).body(response);
        } catch (Exception e) {
            return new ResponseEntity<>(ex.bodyBuild(), ex.getHttpStatus());
        }
    }

    private DLException convertToDLExceptionFormat(Exception e) {
        LocalDateTime current = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(Constant.DEFAULT_TIMEZONE)).toLocalDateTime();
        return DLException.newBuilder()
                .timestamp(current)
                .code(-1)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(
                "[EXCEPTION - {}] {}",
                ex.getClass().getSimpleName(),
                ObjectHelper.isNullOrEmpty(ex.getMessage()) ? "Null pointer exception" : ex.getMessage()
        );
        DLException exception = convertToDLExceptionFormat(ex);
        exception.setHttpStatus(status);
        return this.buildErrorResponseBody(exception);
    }

}
