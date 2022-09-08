package dev.dl.blogservice.application.service;

import dev.dl.common.exception.DLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RabbitConsumerService {

    @RabbitListener(queues = {"${rabbit.queue.blog}"})
    public void receive(@Payload Message<Object> message) throws DLException {
        log.info("Message " + message + "  " + LocalDateTime.now());
        String ultima = String.valueOf(message.getHeaders().get("ultima"));
        if(ultima.equals("sim")){
            log.info(ultima);
        }
        //String payload = message.getPayload();
    }

}
