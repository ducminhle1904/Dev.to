package dev.dl.userservice.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import dev.dl.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LogInResponse extends BaseResponse {

    @SerializedName("access_token")
    @JsonProperty("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    @JsonProperty("expires_in")
    private Integer expiresIn;

    @SerializedName("refresh_expires_in")
    @JsonProperty("refresh_expires_in")
    private Integer refreshExpiresIn;

    @SerializedName("refresh_token")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @SerializedName("token_type")
    @JsonProperty("token_type")
    private String tokenType;

    @SerializedName("id_token")
    @JsonProperty("id_token")
    private String idToken;

    @SerializedName("not-before-policy")
    @JsonProperty("not-before-policy")
    private Integer notBeforePolicy;

    @SerializedName("session_state")
    @JsonProperty("session_state")
    private String sessionState;

    @SerializedName("scope")
    @JsonProperty("scope")
    private String scope;

}
