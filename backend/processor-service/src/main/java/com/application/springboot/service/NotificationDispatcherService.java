package com.application.springboot.service;

import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.service.ResourceLoaderService;
import com.application.sharedlibrary.service.UserService;
import com.application.sharedlibrary.service.VideoVariantService;
import com.application.sharedlibrary.util.EmailTemplateProcessor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationDispatcherService {

  private final EmailTemplateProcessor emailTemplateProcessor;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ResourceLoaderService resourceLoaderService;
  private final UserService userService;
  private final VideoVariantService videoVariantService;

  @Autowired
  public NotificationDispatcherService(EmailTemplateProcessor emailTemplateProcessor, KafkaTemplate<String, String> kafkaTemplate, ResourceLoaderService resourceLoaderService, UserService userService, VideoVariantService videoVariantService) {
    this.emailTemplateProcessor = emailTemplateProcessor;
    this.kafkaTemplate = kafkaTemplate;
    this.resourceLoaderService = resourceLoaderService;
    this.userService = userService;
    this.videoVariantService = videoVariantService;
  }

  public void dispatch(int videoId, int userId) throws Exception {
    User authenticatedUser = userService.findById(userId);

    // Mapping placeholders for replacement
    Map<String, String> replacements = Map.of(
      "{{username}}", authenticatedUser.getName().toUpperCase()
    );

    String mailBodyMd = resourceLoaderService.readFileFromResources("email-templates/video-process-success-email.md");
    String mailBodyHtml = emailTemplateProcessor.processContent(mailBodyMd, replacements);

    JSONObject jsonPayload = new JSONObject();
    jsonPayload.put("subject", "Your Video Has Been Successfully Processed!");
    jsonPayload.put("body", mailBodyHtml);
    jsonPayload.put("receiverEmail", authenticatedUser.getEmail());

    // producer publishes message to a kafka topic for sending emails
    System.out.println("Message published to 2nd topic...Sending mail...");
    kafkaTemplate.send("email-notification", jsonPayload.toJSONString());
  }
}
