package com.application.springboot.service;

import com.application.springboot.dto.ResolutionFactory;
import com.application.springboot.dto.VideoPayload;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService implements MessageBrokerConsumer {

  @Qualifier("VideoProcessingService")
  private final MediaProcessingService mediaProcessingService;

  @Autowired
  public KafkaConsumerService(MediaProcessingService mediaProcessingService) {
    this.mediaProcessingService = mediaProcessingService;
  }

  @Value("${custom.target-resolution-count}")
  private int targetResolutionCount;

  @Value("${GROUP_ID}")
  private String kafkaConsumerGroupId;

  @Value("${RESOLUTION}")
  private String targetResolution; // "720p"

  // Here a single Kafka topic is configured with 3 consumer groups (passed in the ENV), each containing multiple(x) consumers. Each group is responsible for handling a specific image resolution for resizing.

  // Listener with 2 consumer that handle 128 | 512 | 1024 px image resolution
  // @ConditionalOnProperty annotation helps to enable/disable listeners based on the value of environment variable dynamically
  // https://docs.spring.io/spring-boot/api/java/org/springframework/boot/autoconfigure/condition/ConditionalOnProperty.html
  // @ConditionalOnProperty(name = "${GROUP_ID}", havingValue = "group1")
  @KafkaListener(topics = "video-processor", groupId = "${GROUP_ID}")
  @Override
  public void listenToTopic(String payload) throws Exception {
    // parse JSON string to JSON object
    JSONParser parser = new JSONParser();
    JSONObject jsonObj = (JSONObject) parser.parse(payload);

    //Map<String, Object> map = jsonObj.toMap();
    int id = ((Number) jsonObj.get("id")).intValue(); // while storing(put) int is autoboxed into an Integer. And JSONObject treats int as long,so need to recast to int
    int userId = ((Number) jsonObj.get("authenticatedUserId")).intValue();
    String message = (String) jsonObj.get("message");

    System.out.println(message + " A consumer in the group " + kafkaConsumerGroupId + " is now listening to the topic for processing tasks.");

    // using builder method for object initialization
    VideoPayload details = VideoPayload.builder()
      .videoId(id)
      .sourceVideoPath((String) jsonObj.get("sourceVideoPath"))
      .resolutionProfile(ResolutionFactory.createResolutionProfile(targetResolution))
      .segmentDuration(10)
      .authenticatedUserId(userId)
      .message(message)
      .build();

    try {
      mediaProcessingService.process(details);
    } catch (Exception e) {
      System.out.println("Error in processing video: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
