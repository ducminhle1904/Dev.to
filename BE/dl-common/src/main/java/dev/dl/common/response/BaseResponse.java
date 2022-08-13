package dev.dl.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseResponse {
    @JsonProperty("response_status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String responseStatus;

    @JsonProperty("response_message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String responseMessage = "Thành công";

    @JsonProperty("request_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String requestId;

    @JsonProperty("additional_data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object additionalData;
}
