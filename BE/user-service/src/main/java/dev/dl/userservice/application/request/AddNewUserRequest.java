package dev.dl.userservice.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.dl.common.exception.DLException;
import dev.dl.common.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddNewUserRequest extends BaseRequest {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @Override
    public void rules() throws DLException {

    }
}
