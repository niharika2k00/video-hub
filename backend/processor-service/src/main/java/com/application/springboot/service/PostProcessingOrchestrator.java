package com.application.springboot.service;

import com.application.sharedlibrary.entity.Video;
import com.application.sharedlibrary.enums.StorageType;
import com.application.sharedlibrary.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.application.springboot.util.VideoPathUtil.getSourceVideoAbsolutePath;

@Service
public class PostProcessingOrchestrator {

  @Value("${aws.credentials.region}")
  String awsRegion;

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
    String materManifestFilePath = ""; // required for the frontend for HLS streaming
    System.out.println("Post-processing started...");

    Video video = videoService.findById(videoId);
    String sourceVideoAbsolutePath = getSourceVideoAbsolutePath(videoDirPath); // raw video .../videos/1/40/40.mp4
    System.out.println("videoDirPath: " + videoDirPath); // .../videos/5/27
    System.out.println("sourceVideoAbsolutePath: " + sourceVideoAbsolutePath); // .../videos/5/27/27.mp4

    // generate master manifest file
    masterManifestGeneratorService.generate(videoDirPath);

    // generate thumbnail image in local directory and creating static urls
    String thumbnailUrl = "";
    thumbnailGeneratorService.generate(videoDirPath, sourceVideoAbsolutePath);

    if (video.getStorageType() == StorageType.AWS_S3) {
      String baseS3Url = String.format("https://%s.s3.%s.amazonaws.com/", bucketName, awsRegion);

      // https://<bucket_name>.s3.us-east-1.amazonaws.com/videos/1/15/thumbnail.jpg
      String objectKey = String.format("videos/%s/%s/thumbnail.jpg", userId, videoId);
      thumbnailUrl = baseS3Url + objectKey;

      materManifestFilePath = baseS3Url + String.format("videos/%s/%s/master.m3u8", userId, videoId);
    }

    video.setThumbnailUrl(thumbnailUrl);
    video.setVideoDirectoryPath(materManifestFilePath);
    videoService.saveOrUpdate(video);

    // delete the raw video file from local
    deleteSourceVideo(sourceVideoAbsolutePath);

    // syncing directory from local in cloud AWS S3 (if selected)
    if (video.getStorageType() == StorageType.AWS_S3) {
      storageSyncService.createBucket(bucketName);

      // s3://<bucket_name>/videos/1/70
      //String[] parts = videoDirPath.split("/", 4); // ["s3:", "", "<bucket_name>", "videos/1/40"]

      storageSyncService.syncDirectoryFromLocal(videoDirPath, "s3://" + bucketName + "/videos/" + userId + "/" + videoId);
    }

    cleanupLocalVideoDirectory(videoDirPath);
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

  private void cleanupLocalVideoDirectory(String videoDirPath) throws IOException, InterruptedException {
    ProcessBuilder processBuilder = new ProcessBuilder("rm", "-rf", videoDirPath);
    Process process = processBuilder.start();
    int exitCode = process.waitFor();

    if (exitCode == 0)
      System.out.println("✅ Local video directory deleted successfully: " + videoDirPath);
    else
      System.err.println("❌ Failed to delete local video directory: " + videoDirPath);
  }
}
