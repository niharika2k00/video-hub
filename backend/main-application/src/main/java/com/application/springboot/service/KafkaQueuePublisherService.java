package com.application.springboot.service;

import com.application.sharedlibrary.entity.Video;
import com.application.sharedlibrary.service.VideoService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("KafkaQueuePublisherService")
public class KafkaQueuePublisherService implements QueuePublisherService {

  @Qualifier("KafkaProducerService")
  private final MessageBrokerProducer messageBrokerProducer;
  private final VideoService videoService;

  @Autowired
  public KafkaQueuePublisherService(VideoService videoService, MessageBrokerProducer messageBrokerProducer) {
    this.videoService = videoService;
    this.messageBrokerProducer = messageBrokerProducer;
  }

  @Override
  public void publishToQueue(int videoId) throws Exception {

    Video video = videoService.findById(videoId);

    JSONObject jsonObj = new JSONObject();
    jsonObj.put("id", video.getId());
    jsonObj.put("authenticatedUserId", video.getAuthorId());
    jsonObj.put("videoDirectoryPath", video.getVideoDirectoryPath());
    jsonObj.put("message", "Video with ID " + video.getId() + " has been successfully published to kafka for further processing");

    messageBrokerProducer.sendToTopic(jsonObj.toJSONString());
  }
}
