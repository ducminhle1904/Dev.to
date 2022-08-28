package dev.dl.userservice.application.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaResponse {

    private String key;
    private Object value;

}
