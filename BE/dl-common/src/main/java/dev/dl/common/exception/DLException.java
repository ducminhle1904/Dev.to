package dev.dl.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Lombok")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DLException extends Exception {

    private LocalDateTime timestamp;
    private Integer code;
    private String message;
    private String debugMessage;
    private List<ExceptionVariable> variables;
    private HttpStatus httpStatus;

    public Map<String, Object> bodyBuild() {
        // Map<String, Object> body = new LinkedHashMap<>();
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", this.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
        error.put("error_code", this.getCode());
        error.put("message", this.getMessage());
        if (this.getDebugMessage() != null && !this.getDebugMessage().isEmpty()) {
            error.put("debug_message", this.getDebugMessage());
        }
        if (this.getVariables() != null) {
            error.put("variables", this.getVariables());
        }
        // body.put("error", error);
        return error;
    }


    public static DLException.Builder newBuilder() {
        return new Builder();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Builder {

        private LocalDateTime timestamp;
        private Integer code;
        private String message;
        private String debugMessage;
        private List<ExceptionVariable> variables;
        private HttpStatus httpStatus;

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
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

        public DLException build() {
            return new DLException(
                    this.timestamp,
                    this.code,
                    this.message,
                    this.debugMessage,
                    this.variables,
                    this.httpStatus
            );
        }

    }

}
