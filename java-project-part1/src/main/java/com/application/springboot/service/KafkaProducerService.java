package com.application.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service("KafkaProducerService")
public class KafkaProducerService implements MessageBrokerProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendToTopic(String payload) throws Exception {
    String topicName = "video-processor";
    System.out.println("kafka producer sends message to topic: " + topicName);

    // publish message to kafka topic 1
    kafkaTemplate.send(topicName, payload);
  }
}

// The maximum value of the message that the producer client can send, the default value is 1048576B, 1MB
// https://kafka.apache.org/11/javadoc/overview-summary.html
// https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/producer/ProducerConfig.html
// https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/consumer/ConsumerConfig.html
