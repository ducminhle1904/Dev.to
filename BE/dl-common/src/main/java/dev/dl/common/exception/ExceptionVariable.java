package dev.dl.common.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionVariable {

    @JsonProperty("name")
    private String exceptionVariableName;

    @JsonProperty("value")
    private Object exceptionVariableValue;

}
