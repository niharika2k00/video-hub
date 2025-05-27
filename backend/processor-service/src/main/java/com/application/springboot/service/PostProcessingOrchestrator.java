package com.application.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostProcessingOrchestrator {

  private final MasterManifestGeneratorService masterManifestGeneratorService;
  private final ThumbnailGeneratorService thumbnailGeneratorService;
  private final NotificationDispatcherService notificationDispatcherService;

  @Autowired
  public PostProcessingOrchestrator(MasterManifestGeneratorService masterManifestGeneratorService, ThumbnailGeneratorService thumbnailGeneratorService, NotificationDispatcherService notificationDispatcherService) {
    this.masterManifestGeneratorService = masterManifestGeneratorService;
    this.thumbnailGeneratorService = thumbnailGeneratorService;
    this.notificationDispatcherService = notificationDispatcherService;
  }

  public void handler(int userId, int videoId, String sourceVideoPath) throws Exception {
    masterManifestGeneratorService.generate(sourceVideoPath);
    thumbnailGeneratorService.generate(sourceVideoPath);
    deleteSourceVideo(sourceVideoPath);

    notificationDispatcherService.dispatch(videoId, userId);
  }

  private void deleteSourceVideo(String sourceVideoPath) {

  }
}
