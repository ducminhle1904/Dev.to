package com.cozwork.facehub.application.service;

import com.cozwork.kafka.consumer.TopicListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@Slf4j
public class KafkaConsumerService extends TopicListener {

    @Override
    @KafkaListener(
            groupId = "cozwork-facehub",
            topics = "${kafka.topic-reminder}"
    )
    @SendTo
    public String reminderTopicLister(@Payload ConsumerRecord<String, String> data,
                                      @Headers MessageHeaders messageHeaders,
                                      Acknowledgment acknowledgment) {
        try {
            if (Optional.ofNullable(acknowledgment).isPresent()) {
                log.info("\n{}\nOFFSET {}\nPARTITION {}", data.value(), data.offset(), data.partition());
                acknowledgment.acknowledge();
            }
        } catch (Exception ignored) {
        }
        return this.getGSON().toJson(new HashMap<String, String>() {{
            put("message", "done");
        }});
    }
}
