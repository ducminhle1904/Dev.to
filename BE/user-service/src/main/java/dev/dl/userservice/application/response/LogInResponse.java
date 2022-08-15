package dev.dl.userservice.application.response;

import dev.dl.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogInResponse extends BaseResponse {

    private String token;

}
