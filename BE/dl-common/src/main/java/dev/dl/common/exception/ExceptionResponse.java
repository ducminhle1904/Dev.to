package dev.dl.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.dl.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuppressWarnings("ALL")
public class ExceptionResponse extends BaseResponse {

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

    @JsonProperty("variables")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ExceptionVariable> variables;


    public ExceptionResponse(String responseStatus, String responseMessage, String requestId, Object additionalData, LocalDateTime timestamp, Integer code, String message, String debugMessage, List<ExceptionVariable> variables) {
        super(responseStatus, responseMessage, requestId, additionalData);
        this.timestamp = timestamp;
        this.code = code;
        this.message = message;
        this.debugMessage = debugMessage;
        this.variables = variables;
    }

    public ExceptionResponse(LocalDateTime timestamp, Integer code, String message, String debugMessage, List<ExceptionVariable> variables) {
        this.timestamp = timestamp;
        this.code = code;
        this.message = message;
        this.debugMessage = debugMessage;
        this.variables = variables;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Builder {
        private String responseStatus;
        private String responseMessage;
        private String requestId;
        private Object additionalData;
        private LocalDateTime timestamp;
        private Integer code;
        private String message;
        private String debugMessage;
        private List<ExceptionVariable> variables;

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder debugMessage(String debugMessage) {
            this.debugMessage = debugMessage;
            return this;
        }

        public Builder variables(List<ExceptionVariable> variables) {
            this.variables = variables;
            return this;
        }

        public ExceptionResponse build() {
            return new ExceptionResponse(this.timestamp, this.code, this.message, this.debugMessage, this.variables);
        }

        public ExceptionResponse fullBuild() {
            return new ExceptionResponse(
                    this.responseStatus,
                    this.responseMessage,
                    this.requestId,
                    this.additionalData,
                    this.timestamp,
                    this.code,
                    this.message,
                    this.debugMessage,
                    this.variables
            );
        }

    }

}
