package com.cozwork.kafka.message;

public class KafkaRequest<T> {

    private KafkaHeader header;

    private T body;

    public KafkaRequest() {
    }

    public KafkaRequest(KafkaHeader header, T body) {
        this.header = header;
        this.body = body;
    }

    public KafkaHeader getHeader() {
        return header;
    }

    public void setHeader(KafkaHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
