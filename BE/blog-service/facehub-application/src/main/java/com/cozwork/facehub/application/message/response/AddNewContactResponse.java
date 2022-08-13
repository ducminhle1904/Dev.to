package com.cozwork.facehub.application.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddNewContactResponse extends BaseResponse {

    @JsonProperty("name")
    private String name;

}
