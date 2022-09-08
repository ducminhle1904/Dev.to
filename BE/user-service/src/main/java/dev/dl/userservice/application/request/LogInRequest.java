package dev.dl.userservice.application.request;

import dev.dl.common.exception.DLException;
import dev.dl.common.helper.ObjectHelper;
import dev.dl.common.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogInRequest extends BaseRequest {

    private String username;
    private String password;

    @Override
    public void rules() throws DLException {
        if (ObjectHelper.isNullOrEmpty(username)) {
            addEmptyField("username");
        }
        if (ObjectHelper.isNullOrEmpty(password)) {
            addEmptyField("password");
        }
    }
}
