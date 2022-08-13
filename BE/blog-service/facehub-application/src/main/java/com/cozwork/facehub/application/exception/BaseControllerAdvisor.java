package com.cozwork.facehub.application.exception;

import com.cozwork.facehub.application.constant.AppConstant;
import com.cozwork.facehub.application.mapper.ExceptionMapper;
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
import java.util.Optional;

@SuppressWarnings("DuplicatedCode")
@RestControllerAdvice
@Slf4j
public class BaseControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CozException.class})
    public ResponseEntity<Object> handleCozException(CozException ex, WebRequest request) {
        LocalDateTime current = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(AppConstant.DEFAULT_TIMEZONE)).toLocalDateTime();
        log.error("[COZ EXCEPTION] {}", ex.getMessage());
        ExceptionResponse response = ExceptionMapper.INSTANCE.exceptionMapper(ex);
        response.setTimestamp(current);
        return ResponseEntity.status(Optional.ofNullable(ex.getHttpStatus()).isEmpty() ? HttpStatus.INTERNAL_SERVER_ERROR : ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception e) {
        LocalDateTime current = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(AppConstant.DEFAULT_TIMEZONE)).toLocalDateTime();
        log.error("[SYSTEM ERROR] {}", e.getMessage());
        e.printStackTrace();
        ExceptionResponse response = new ExceptionResponse(
                current,
                -1,
                Optional.ofNullable(e.getMessage()).isEmpty() ? "Null exception" : e.getMessage(),
                "System error",
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @SuppressWarnings({"NullableProblems"})
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LocalDateTime current = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(AppConstant.DEFAULT_TIMEZONE)).toLocalDateTime();
        log.error("[SYSTEM ERROR] {}", ex.getMessage());
        ex.printStackTrace();
        ExceptionResponse response = new ExceptionResponse(
                current,
                -1,
                Optional.ofNullable(ex.getMessage()).isEmpty() ? "Null exception" : ex.getMessage(),
                "System error",
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
