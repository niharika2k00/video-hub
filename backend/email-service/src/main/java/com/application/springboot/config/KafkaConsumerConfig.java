package com.application.springboot.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  public String kafkaServerPort;

  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    Map<String, Object> config = new HashMap<>();

    // Offset and commit settings
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

    // Deserializers
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    // Message size limits
    config.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "20000000"); // 20 MB = 20000000 bytes
    config.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "20000000");

    // Timeout settings
    config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 60_000); // 60 seconds
    config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 15_000); // 15 seconds (< 60/3)
    config.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 90_000); // 90 seconds (> 60)
    config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "14400000"); // 4 hours - critical for long video processing

    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerPort); // kafka broker address
    // config.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");

    // Disable telemetry (fixes Raspberry Pi issues) and NullPointerException in Kafka client 3.8.1
    config.put("client.telemetry.enable", false);

    return new DefaultKafkaConsumerFactory<>(config);
  }
}
