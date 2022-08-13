package com.cozwork.kafka.message;

public class KafkaHeader {

    private String id;
    private String from;
    private String ts;
    private String msgType;
    private String extData;

    public KafkaHeader() {
    }

    public KafkaHeader(String id, String from, String ts, String msgType, String extData) {
        this.id = id;
        this.from = from;
        this.ts = ts;
        this.msgType = msgType;
        this.extData = extData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }
}
