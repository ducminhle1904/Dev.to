package com.cozwork.facehub.application.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    @JsonProperty("timestamp")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime timestamp;

    @JsonProperty("code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer code;

    @JsonProperty("message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonProperty("debug_message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String debugMessage;

    @JsonProperty("exception_variables")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ExceptionVariable> variables;

}
