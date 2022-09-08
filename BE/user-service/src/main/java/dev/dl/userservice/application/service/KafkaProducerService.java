package dev.dl.userservice.application.service;

import dev.dl.common.helper.RestfulHelper;
import dev.dl.userservice.application.message.KafkaHeader;
import dev.dl.userservice.application.message.KafkaRequest;
import dev.dl.userservice.application.message.KafkaResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ReplyingKafkaTemplate<String, Object, String> replyingKafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate, ReplyingKafkaTemplate<String, Object, String> replyingKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    public KafkaResponse send(KafkaRequest request, String topic, Integer partition) {
        log.info("[KAFKA] Sending message");
        try {
            String dataJson = request.getBody() instanceof String ? (String) request.getBody() : RestfulHelper.convertObjectToJsonStringGson(request.getBody());
            List<Header> headers = this.generateHeader(request.getHeader());
            ProducerRecord<String, Object> message = new ProducerRecord<>(topic, partition, "key", dataJson, headers);
            ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaTemplate.send(message);
            return new KafkaResponse("result", listenableFuture.get().getProducerRecord().value());
        } catch (Exception e) {
            log.error("[KAFKA - EXCEPTION] Exception caused when publish data: {}", e.getMessage());
            return new KafkaResponse("error", e.getMessage());
        }
    }

    private List<Header> generateHeader(KafkaHeader header) {
        if (!Optional.ofNullable(header).isPresent()) {
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
                log.error("[EXCEPTION] {}", e.getMessage());
            }
        });
        return headers;
    }

}
