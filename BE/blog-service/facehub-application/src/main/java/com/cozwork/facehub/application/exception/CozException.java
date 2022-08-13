package com.cozwork.facehub.application.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("Lombok")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CozException extends Exception {

    private LocalDateTime timestamp;
    private Integer code;
    private String message;
    private String debugMessage;
    private List<ExceptionVariable> variables;
    private HttpStatus httpStatus;

}
