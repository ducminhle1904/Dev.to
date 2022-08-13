package com.cozwork.facehub.application.service;

import com.cozwork.kafka.consumer.TopicListener;
import com.cozwork.kafka.producer.TopicProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService extends TopicProducer {

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate) {
        super(kafkaTemplate, replyingKafkaTemplate);
    }
}
