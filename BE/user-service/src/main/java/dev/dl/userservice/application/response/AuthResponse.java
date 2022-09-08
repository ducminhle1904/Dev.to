package dev.dl.userservice.application.response;

import dev.dl.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResponse extends BaseResponse {
    private String userId;
    private List<String> roles;
    private Boolean nonLock;
}
