package com.cozwork.kafka.consumer;

import com.cozwork.kafka.message.KafkaHeader;
import com.cozwork.kafka.message.KafkaReceiverObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class TopicListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract String reminderTopicLister(@Payload ConsumerRecord<String, String> data,
                                              @Headers MessageHeaders messageHeaders,
                                              Acknowledgment acknowledgment);
    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(
                    LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString())
            )
            .registerTypeAdapter(
                    LocalDateTime.class,
                    (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext) -> new JsonPrimitive(localDateTime.toString())
            )
            .registerTypeAdapter(
                    LocalDate.class,
                    (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsJsonPrimitive().getAsString())
            )
            .registerTypeAdapter(
                    LocalDate.class,
                    (JsonSerializer<LocalDate>) (localDate, type, jsonSerializationContext) -> new JsonPrimitive(localDate.toString())
            )
            .create();

    public Gson getGSON() {
        return GSON;
    }

    public <MESSAGE> KafkaReceiverObject<MESSAGE> readMessageAndHeader(ConsumerRecord<String, String> data,
                                                                       MessageHeaders messageHeaders,
                                                                       Class<MESSAGE> messageClass) {
        Gson gson = new Gson();
        MESSAGE message = gson.fromJson(data.value(), messageClass);
        KafkaHeader header = this.getHeader(messageHeaders);
        return new KafkaReceiverObject<>(header, message);
    }

    public KafkaHeader getHeader(MessageHeaders messageHeaders) {
        Map<String, String> header = new HashMap<>();
        messageHeaders.keySet().forEach(key -> {
            Object value = messageHeaders.get(key);
            if (Optional.ofNullable(value).isPresent() && value instanceof byte[]) {
                header.put(
                        key,
                        new String((byte[]) value, StandardCharsets.UTF_8)
                );
            }
        });
        KafkaHeader kafkaHeader = new KafkaHeader();
        List<Field> kafkaHeaderFields = List.of(kafkaHeader.getClass().getDeclaredFields());
        kafkaHeaderFields.forEach(field -> {
            field.setAccessible(true);
            Object headerValue = header.get(field.getName());
            if (Optional.ofNullable(headerValue).isPresent()) {
                try {
                    field.set(kafkaHeader, headerValue);
                } catch (Exception ignored) {
                }
            }
        });
        return kafkaHeader;
    }

}
