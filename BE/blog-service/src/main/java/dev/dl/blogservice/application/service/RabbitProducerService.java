package dev.dl.blogservice.application.service;

import dev.dl.common.helper.RestfulHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    @Autowired
    public RabbitProducerService(RabbitTemplate rabbitTemplate,
                                 @Qualifier("queue") Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    public <T> void send(T object) {
        String dataJson = object instanceof String ? (String) object : RestfulHelper.convertObjectToJsonStringGson(object);
        log.info("[RABBIT MQ] SENDING DATA");
        rabbitTemplate.convertAndSend(this.queue.getName(), dataJson);
    }

}
