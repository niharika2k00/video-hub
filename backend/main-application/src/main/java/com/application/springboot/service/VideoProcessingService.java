package com.application.springboot.service;

import com.application.sharedlibrary.exception.InvalidRequestException;
import com.application.springboot.dto.VideoUploadRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoProcessingService {
  private final MetadataService metadataService;
  private final StorageService storageService;
  private final QueuePublisherService queuePublisherService;

  @Autowired
  public VideoProcessingService(@Qualifier("SqlMetadataService") MetadataService metadataService,
                                @Qualifier("LocalVideoStorageService") StorageService storageService,
                                @Qualifier("KafkaQueuePublisherService") QueuePublisherService queuePublisherService) {
    this.metadataService = metadataService;
    this.storageService = storageService;
    this.queuePublisherService = queuePublisherService;
  }

  public void uploadVideo(VideoUploadRequestDto reqBodyParams) throws Exception {
    // save metadata in DB
    Optional<Integer> videoId = metadataService.saveMetadata(reqBodyParams);

    if (videoId.isPresent()) {
      int video_id = videoId.get();

      // save raw image
      storageService.storeMediaFile(video_id, reqBodyParams); // unboxing from Integer to int

      // publishing to kafka queue
      queuePublisherService.publishToQueue(video_id);
    } else {
      throw new InvalidRequestException("Video with the specified id is not found.");
    }
  }
}
