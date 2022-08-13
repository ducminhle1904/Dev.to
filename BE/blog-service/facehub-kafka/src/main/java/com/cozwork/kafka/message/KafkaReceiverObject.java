package com.cozwork.kafka.message;

public class KafkaReceiverObject<T> {

    private KafkaHeader header;

    private T message;

    public KafkaReceiverObject() {
    }

    public KafkaReceiverObject(KafkaHeader header, T message) {
        this.header = header;
        this.message = message;
    }

    public KafkaHeader getHeader() {
        return header;
    }

    public void setHeader(KafkaHeader header) {
        this.header = header;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
