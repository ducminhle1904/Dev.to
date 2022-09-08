package dev.dl.blogservice.application.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbit.queue.blog}")
    private String blogQueueName;

    @Value("${rabbit.route.key}")
    private String blogRouteKey;

    @Value("${direct.exchange.name}")
    private String blogDirectExchange;

    @Bean(name = "queue")
    public Queue blogQueue() {
        return new Queue(blogQueueName, true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(blogDirectExchange);
    }

    @Bean
    public Binding blogBinding(@Qualifier("queue") Queue blogQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(blogQueue).to(directExchange).with(blogRouteKey);
    }

}
