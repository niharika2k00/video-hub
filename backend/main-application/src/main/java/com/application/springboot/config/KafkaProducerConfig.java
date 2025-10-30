package com.application.springboot.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  public String kafkaServerPort;

  @Bean
  // https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/producer/ProducerConfig.html
  // https://docs.spring.io/spring-kafka/api/org/springframework/kafka/annotation/KafkaListener.html
  public ProducerFactory<String, String> producerFactory() {
    Map<String, Object> props = new HashMap<>();

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerPort); // kafka broker address
    props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "209715200"); // 20 MB = 20000000 bytes || 200 MB = 209715200 bytes
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

    // Disable telemetry (fixes Raspberry Pi issues)
    props.put("client.telemetry.enable", false);

    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    // simplify for sending messages to kafka
    return new KafkaTemplate<>(producerFactory());
  }
}
