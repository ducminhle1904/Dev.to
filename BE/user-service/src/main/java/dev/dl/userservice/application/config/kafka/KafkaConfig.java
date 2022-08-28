package dev.dl.userservice.application.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@Slf4j
public class KafkaConfig {

    @Value("${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.security.protocol}")
    private String kafkaSecurityControl;

    @Value("${kafka.sasl.mechanism}")
    private String kafkaSaslMechanism;

    @Value("${kafka.numPartitions}")
    private Integer numPartitions;

    @Value("${kafka.replicationFactor}")
    private Short replicationFactor;

    @Value("${kafka.group-id}")
    private String kafkaGroupId;

    @Value("${kafka.topic-user}")
    private String kafkaTopicUser;

    @Value("${kafka.topic-user-reply}")
    private String kafkaTopicUserReply;

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
    public ProducerFactory<String, Object> producerEventMessage() {
        log.info("[KAFKA - producerEventMessage] GENERATE ProducerFactory");
        System.out.println(System.getProperty("aws.accessKeyId"));
        System.out.println(System.getProperty("aws.secretKey"));
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        configMap.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.kafkaSecurityControl);
        configMap.put(SaslConfigs.SASL_MECHANISM, this.kafkaSaslMechanism);
        return new DefaultKafkaProducerFactory<String, Object>(configMap);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        log.info("[KAFKA - kafkaTemplate] GENERATE KafkaTemplate");
        return new KafkaTemplate<>(producerEventMessage());
    }

    @Bean
    public KafkaMessageListenerContainer<String, String> replyContainer() {
        ContainerProperties containerProperties = new ContainerProperties(kafkaTopicUserReply);
        return new KafkaMessageListenerContainer<>(consumerConfigs(), containerProperties);

    }

    @Bean
    public ReplyingKafkaTemplate<String, Object, String> replyKafkaTemplate() {
        return new ReplyingKafkaTemplate<>(producerEventMessage(), replyContainer());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        log.info("[KAFKA - kafkaAdmin] GENERATE KafkaAdmin");
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        configs.put("security.protocol", this.kafkaSecurityControl);
        configs.put("sasl.mechanism", this.kafkaSaslMechanism);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createTopicUser() {
        return new NewTopic(this.kafkaTopicUser, numPartitions, replicationFactor);
    }

}
