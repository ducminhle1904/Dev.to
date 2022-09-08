package dev.dl.userservice.application.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaHeader {
    private String id;
    private String from;
    private String ts;
    private String msgType;
    private String extData;

    public void setField(String extData, String msgType, String from) {
        this.setId(UUID.randomUUID().toString());
        this.setTs(String.valueOf(System.currentTimeMillis()));
        this.setExtData(extData);
        this.setMsgType(msgType);
        this.setFrom(from);
    }
}
