package com.cozwork.kafka.producer;

import com.cozwork.kafka.message.KafkaHeader;
import com.cozwork.kafka.message.KafkaRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("DuplicatedCode")
public abstract class TopicProducer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
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
    }

    public TopicProducer(KafkaTemplate<String, String> kafkaTemplate, ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    @SuppressWarnings("RedundantClassCall")
    public void sendMessage(KafkaRequest request, String topic, Integer partition) {
        logger.info("[KAFKA PRODUCER] STARTING TO SEND MESSAGE");
        try {
            String dataJsonString = request.getBody() instanceof String ? String.class.cast(request.getBody()) : GSON.toJson(request.getBody());
            List<Header> headers = this.generateHeader(request.getHeader());
            ProducerRecord<String, String> message = new ProducerRecord<>(topic, partition, "key", dataJsonString, headers);
            kafkaTemplate.send(message).addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable ex) {
                    logger.error("[KAFKA RESULT] {}", ex.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    logger.info("[KAFKA RESULT] {}\n{}", result.getProducerRecord(), result.toString());
                }
            });
        } catch (Exception e) {
            logger.error("[KAFKA PRODUCER EXCEPTION] {}", Optional.ofNullable(e.getMessage()).isEmpty() ? "Null pointer exception": e.getMessage());
        } finally {
            logger.info("[KAFKA PRODUCER] SENDING MESSAGE COMPLETED");
        }
    }

    @SuppressWarnings("RedundantClassCall")
    public <RS> RS sendAndReceiveResponse(KafkaRequest request, String topic, Integer partition, Class<RS> responseClass) {
        logger.info("[KAFKA PRODUCER] STARTING TO SEND MESSAGE");
        try {
            String dataJsonString = request.getBody() instanceof String ? String.class.cast(request.getBody()) : GSON.toJson(request.getBody());
            List<Header> headers = this.generateHeader(request.getHeader());
            ProducerRecord<String, String> message = new ProducerRecord<>(topic, partition, "key", dataJsonString, headers);
            RequestReplyFuture<String, String, String> requestReplyFuture = replyingKafkaTemplate.sendAndReceive(message);
            ConsumerRecord<String, String> consumerRecord = requestReplyFuture.get();
            try {
                return GSON.fromJson(consumerRecord.value(), responseClass);
            } catch (Exception e) {
                logger.warn("[CAN NOT PARSE KAFKA RESPONSE]: {}\nThe result will return null", consumerRecord.value());
                return null;
            }
        } catch (Exception e) {
            logger.error("[KAFKA PRODUCER EXCEPTION] {}", Optional.ofNullable(e.getMessage()).isEmpty() ? "Null pointer exception": e.getMessage());
            return null;
        } finally {
            logger.info("[KAFKA PRODUCER] SENDING MESSAGE COMPLETED");
        }
    }

    private List<Header> generateHeader(KafkaHeader header) {
        if (Optional.ofNullable(header).isEmpty()) {
            return new ArrayList<>();
        }
        List<Header> headers = new ArrayList<>();
        List<Field> sourceClassFields = new ArrayList<>(Arrays.asList(header.getClass().getDeclaredFields()));
        sourceClassFields.forEach(field -> {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(header);
                if (value != null) {
                    headers.add(new RecordHeader(field.getName(), String.valueOf(value).getBytes()));
                }
            } catch (Exception e) {
                logger.warn("[EXCEPTION] {}", e.getMessage());
            }
        });
        return headers;
    }

}
