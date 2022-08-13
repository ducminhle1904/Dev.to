package com.cozwork.facehub.application.message.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@SuppressWarnings("Lombok")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddContactRequest extends BaseRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("dob")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dob;

    @Override
    public void rules() {
        if (Optional.ofNullable(this.name).isEmpty() || this.name.isEmpty()) {
            this.addEmptyField("name");
        }
        if (Optional.ofNullable(this.address).isEmpty() || this.address.isEmpty()) {
            this.addEmptyField("address");
        }
    }
}
