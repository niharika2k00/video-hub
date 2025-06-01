package com.application.springboot.service;

import com.application.sharedlibrary.entity.Video;
import com.application.sharedlibrary.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostProcessingOrchestrator {

  private final MasterManifestGeneratorService masterManifestGeneratorService;
  private final ThumbnailGeneratorService thumbnailGeneratorService;
  private final NotificationDispatcherService notificationDispatcherService;
  private final VideoService videoService;

  @Autowired
  public PostProcessingOrchestrator(MasterManifestGeneratorService masterManifestGeneratorService,
      ThumbnailGeneratorService thumbnailGeneratorService, NotificationDispatcherService notificationDispatcherService,
      VideoService videoService) {
    this.masterManifestGeneratorService = masterManifestGeneratorService;
    this.thumbnailGeneratorService = thumbnailGeneratorService;
    this.notificationDispatcherService = notificationDispatcherService;
    this.videoService = videoService;
  }

  public void handler(int userId, int videoId, String sourceVideoPath) throws Exception {
    System.out.println("Post-processing started...");
    masterManifestGeneratorService.generate(sourceVideoPath);

    String thumbnailPath = thumbnailGeneratorService.generate(sourceVideoPath);

    Video video = videoService.findById(videoId);
    video.setThumbnailUrl(thumbnailPath);
    videoService.saveOrUpdate(video);

    deleteSourceVideo(sourceVideoPath);

    notificationDispatcherService.dispatch(videoId, userId);
    System.out.println("Processing pipeline completed.");
  }

  private void deleteSourceVideo(String sourceVideoPath) {

  }
}
