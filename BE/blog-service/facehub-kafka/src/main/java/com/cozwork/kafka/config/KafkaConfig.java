package com.cozwork.kafka.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.GenericMessageListener;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("CommentedOutCode")
@EnableKafka
@Configuration
public class KafkaConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.security.protocol}")
    private String kafkaSecurityControl;

    @Value("${kafka.sasl.mechanism}")
    private String kafkaSaslMechanism;

    @Value("${kafka.sasl.jaas.config}")
    private String kafkaSaslJaasConfig;

    @Value("${kafka.sasl.client.callback.handler.class}")
    private String kafkaSaslClientCallbackHandlerClass;

    @Value("${kafka.group-id}")
    private String kafkaGroupId;

    @Value("${kafka.topic-reminder}")
    private String kafkaTopicReminder;

    @Value("${kafka.topic-reply}")
    private String kafkaTopicReply;

    @Value("${kafka.numPartitions}")
    private Integer numPartitions;

    @Value("${kafka.replicationFactor}")
    private Short replicationFactor;

    @Bean
    public ConsumerFactory<String, Object> consumerConfigs() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configMap.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configMap.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, this.kafkaGroupId);
        configMap.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.kafkaSecurityControl);
        configMap.put(SaslConfigs.SASL_MECHANISM, this.kafkaSaslMechanism);
        //configMap.put(SaslConfigs.SASL_JAAS_CONFIG, this.kafkaSaslJaasConfig);
        //configMap.put(SaslConfigs.SASL_CLIENT_CALLBACK_HANDLER_CLASS, this.kafkaSaslClientCallbackHandlerClass);
        return new DefaultKafkaConsumerFactory<String, Object>(configMap);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerConfigs());
        factory.setReplyTemplate(kafkaTemplate());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    @Bean
    public DefaultKafkaHeaderMapper headerMapper() {
        return new DefaultKafkaHeaderMapper();
    }

    @Bean
    public ProducerFactory<String, String> producerEventMessage() {
        logger.info("[KAFKA - producerEventMessage] GENERATE ProducerFactory");
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        configMap.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.kafkaSecurityControl);
        configMap.put(SaslConfigs.SASL_MECHANISM, this.kafkaSaslMechanism);
        //configMap.put(SaslConfigs.SASL_JAAS_CONFIG, this.kafkaSaslJaasConfig);
        //configMap.put(SaslConfigs.SASL_CLIENT_CALLBACK_HANDLER_CLASS, this.kafkaSaslClientCallbackHandlerClass);
        return new DefaultKafkaProducerFactory<>(configMap);
    }

    @Bean
    @DependsOn(value = {"producerEventMessage"})
    public KafkaTemplate<String, String> kafkaTemplate() {
        logger.info("[KAFKA - kafkaTemplate] GENERATE KafkaTemplate");
        return new KafkaTemplate<>(producerEventMessage());
    }

    @Bean
    @DependsOn(value = {"consumerConfigs"})
    public KafkaMessageListenerContainer<String, String> replyContainer() {
        ContainerProperties containerProperties = new ContainerProperties(this.kafkaTopicReply);
        containerProperties.setMessageListener(new GenericMessageListenerImplement());
        return new KafkaMessageListenerContainer<>(consumerConfigs(), containerProperties);
    }

    @Bean
    @DependsOn(value = {"producerEventMessage", "replyContainer"})
    public ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate() {
        return new ReplyingKafkaTemplate<>(producerEventMessage(), replyContainer());
    }

    @Bean
    @DependsOn
    public KafkaAdmin kafkaAdmin() {
        logger.info("[KAFKA - kafkaAdmin] GENERATE KafkaAdmin");
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        configs.put("security.protocol", this.kafkaSecurityControl);
        configs.put("sasl.mechanism", this.kafkaSaslMechanism);
        //configs.put("sasl.jaas.config", this.kafkaSaslJaasConfig);
        //configs.put("sasl.client.callback.handler.class", this.kafkaSaslClientCallbackHandlerClass);
        return new KafkaAdmin(configs);
    }


    @Bean
    public NewTopic createTopicOTP() {
        return new NewTopic(this.kafkaTopicReminder, numPartitions, replicationFactor);
    }

    public static class GenericMessageListenerImplement implements MessageListener<String, String> {

        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        public GenericMessageListenerImplement() {}

        @Override
        public void onMessage(ConsumerRecord<String, String> stringStringConsumerRecord) {
            logger.info(stringStringConsumerRecord.value());
        }

        @Override
        public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        }

        @Override
        public void onMessage(ConsumerRecord<String, String> data, Consumer<?, ?> consumer) {
        }

        @Override
        public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        }
    }

}
