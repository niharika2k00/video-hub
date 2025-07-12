package com.application.springboot.service;

import com.application.sharedlibrary.entity.VideoVariant;
import com.application.sharedlibrary.enums.VideoStatus;
import com.application.sharedlibrary.service.VideoVariantService;
import com.application.springboot.dto.ResolutionFactory;
import com.application.springboot.dto.VideoPayload;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumerService implements MessageBrokerConsumer {

  @Qualifier("VideoProcessingService")
  private final MediaProcessingService mediaProcessingService;
  private final VideoVariantService videoVariantService;
  private final PostProcessingOrchestrator postProcessingOrchestrator;

  @Autowired
  public KafkaConsumerService(MediaProcessingService mediaProcessingService, VideoVariantService videoVariantService, PostProcessingOrchestrator postProcessingOrchestrator) {
    this.mediaProcessingService = mediaProcessingService;
    this.videoVariantService = videoVariantService;
    this.postProcessingOrchestrator = postProcessingOrchestrator;
  }

  @Value("${custom.target-resolution-count}")
  private int targetResolutionCount;

  @Value("${custom.path.project-root-dir}")
  String projectRootPath;

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

    // Map<String, Object> map = jsonObj.toMap();
    int videoId = ((Number) jsonObj.get("id")).intValue(); // while storing(put) int is autoboxed into an Integer. And JSONObject treats int as long,so need to recast to int
    int userId = ((Number) jsonObj.get("authenticatedUserId")).intValue();
    String videoDirRelativePath = (String) jsonObj.get("videoDirectoryPath"); // "/videos/userId/videoId"
    String message = (String) jsonObj.get("message");

    String videoDirAbsolutePath = projectRootPath + videoDirRelativePath;
    System.out.println("relative source video folder path: " + videoDirRelativePath);
    System.out.println("videoDirAbsolutePath: " + videoDirAbsolutePath);

    System.out.println(message + ". A consumer in the group " + kafkaConsumerGroupId + " is now listening to the topic for processing tasks.");

    // using builder method for object initialization
    VideoPayload details = VideoPayload.builder()
      .videoId(videoId)
      .videoDirectoryPath(videoDirAbsolutePath)
      .resolutionProfile(ResolutionFactory.createResolutionProfile(targetResolution))
      .segmentDuration(10)
      .authenticatedUserId(userId)
      .message(message)
      .build();

    try {
      mediaProcessingService.process(details);

      // Post process once the video is fully transcoded to all ABR(Adaptive Bitrate Streaming) variants
      int liveCount = videoVariantService.getCountByVideoId(videoId);
      List<VideoVariant> variants = videoVariantService.findByVideoId(videoId);
      boolean allVariantsAvailable = variants.stream().allMatch(v -> v.getStatus() == VideoStatus.AVAILABLE);

      for (VideoVariant item : variants)
        System.out.println(item);

      if (allVariantsAvailable)
        postProcessingOrchestrator.handler(userId, videoId, videoDirAbsolutePath);
    } catch (Exception e) {
      System.out.println("Error in processing video: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
