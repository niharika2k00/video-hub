package com.application.springboot.service;

import com.application.sharedlibrary.entity.Video;
import com.application.sharedlibrary.enums.StorageType;
import com.application.sharedlibrary.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.application.springboot.util.VideoPathUtil.getSourceVideoAbsolutePath;

@Service
public class PostProcessingOrchestrator {

  @Value("${custom.path.project-root-dir}")
  String projectRootDirectory;

  @Value("${aws.s3.bucket-name}")
  String bucketName;

  private final MasterManifestGeneratorService masterManifestGeneratorService;
  private final CloudStorageSyncService storageSyncService;
  private final ThumbnailGeneratorService thumbnailGeneratorService;
  private final NotificationDispatcherService notificationDispatcherService;
  private final VideoService videoService;

  @Autowired
  public PostProcessingOrchestrator(MasterManifestGeneratorService masterManifestGeneratorService,
                                    @Qualifier("S3StorageSyncService") CloudStorageSyncService storageSyncService,
                                    ThumbnailGeneratorService thumbnailGeneratorService,
                                    NotificationDispatcherService notificationDispatcherService,
                                    VideoService videoService) {
    this.masterManifestGeneratorService = masterManifestGeneratorService;
    this.storageSyncService = storageSyncService;
    this.thumbnailGeneratorService = thumbnailGeneratorService;
    this.notificationDispatcherService = notificationDispatcherService;
    this.videoService = videoService;
  }

  public void handler(int userId, int videoId, String videoDirPath) throws Exception {
    System.out.println("Post-processing started...");

    Video video = videoService.findById(videoId);
    String sourceVideoAbsolutePath = getSourceVideoAbsolutePath(videoDirPath); // raw video .../videos/1/40/40.mp4
    System.out.println("videoDirPath: " + videoDirPath);
    System.out.println("sourceVideoAbsolutePath: " + sourceVideoAbsolutePath);

    // generate master manifest file
    masterManifestGeneratorService.generate(videoDirPath);

    // generate thumbnail image
    String thumbnailPath = thumbnailGeneratorService.generate(videoDirPath, sourceVideoAbsolutePath);

    video.setThumbnailUrl(thumbnailPath);
    videoService.saveOrUpdate(video);

    // delete the raw video file from local
    deleteSourceVideo(sourceVideoAbsolutePath);

    // syncing directory from local in cloud AWS S3 (if selected)
    if (video.getStorageType() == StorageType.AWS_S3) {
      System.out.println("INSIDE SYNCING LOGIC.......");

      storageSyncService.createBucket(bucketName);

      // s3://demobucket-890291224/videos/1/70
      //String[] parts = videoDirPath.split("/", 4); // ["s3:", "", "demobucket", "videos/1/40"]

      storageSyncService.syncDirectoryFromLocal(videoDirPath, "s3://" + bucketName + "/videos/" + userId + "/" + videoId);
    }

    notificationDispatcherService.dispatch(videoId, userId);
    System.out.println("Processing pipeline completed.");
  }

  private void deleteSourceVideo(String sourceVideoAbsolutePath) throws Exception {
    Path value = Paths.get(sourceVideoAbsolutePath);
    try {
      boolean isFileDeleted = Files.deleteIfExists(value);

      if (isFileDeleted)
        System.out.println("✅ Source video file deleted successfully");
      else
        System.out.println("⚠️ Source video file not found");
    } catch (Exception ex) {
      System.out.println("❌ Failed to delete the source video file: " + ex.getMessage());
    }
  }
}
