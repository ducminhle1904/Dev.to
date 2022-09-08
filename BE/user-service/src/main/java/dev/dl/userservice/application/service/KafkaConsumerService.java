package dev.dl.userservice.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.dl.common.helper.ObjectHelper;
import dev.dl.common.helper.RestfulHelper;
import dev.dl.userservice.application.message.KafkaHeader;
import dev.dl.userservice.application.message.KafkaReceiverObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class KafkaConsumerService {

    private final KafkaProducerService kafkaProducerService;

    @KafkaListener(
            groupId = "${kafka.group-id}",
            topics = "${kafka.topic-user}"
    )
    public void alertTopicListener(@Payload ConsumerRecord<String, String> data,
                                   @Headers MessageHeaders messageHeaders,
                                   Acknowledgment acknowledgment) {
        try {
            if (Optional.ofNullable(acknowledgment).isPresent()) {
                log.info(data.value());
                if (ObjectHelper.isNullOrEmpty(data.value())) {
                    throw new Exception("MESSAGE DATA IS EMPTY");
                }
                acknowledgment.acknowledge();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public KafkaConsumerService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public <MESSAGE> KafkaReceiverObject<MESSAGE> readMessageAndHeader(ConsumerRecord<String, String> data,
                                                                       MessageHeaders messageHeaders,
                                                                       Class<MESSAGE> messageClass) throws JsonProcessingException {
        MESSAGE message = RestfulHelper.convertJsonStringToObjectGson(data.value(), messageClass);
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
        return new KafkaHeader(
                header.get("id"),
                header.get("from"),
                header.get("ts"),
                header.get("msgType"),
                header.get("extData")
        );
    }
}
