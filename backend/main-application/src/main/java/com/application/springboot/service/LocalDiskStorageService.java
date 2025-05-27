package com.application.springboot.service;

import com.application.sharedlibrary.entity.Video;
import com.application.springboot.dto.VideoUploadRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service("LocalDiskStorageService")
public class LocalDiskStorageService implements StorageService<VideoUploadRequestDto> {

  @Value("${custom.path.source-video-dir}")
  String sourceVideoDirectory;

  private final VideoService videoService;

  @Autowired
  public LocalDiskStorageService(VideoService videoService) {
    this.videoService = videoService;
  }

  @Override
  public void storeMediaFile(int videoId, VideoUploadRequestDto reqBodyParams) throws Exception {
    MultipartFile videoFile = reqBodyParams.getVideoFile();
    String extention = getFileExtention(videoFile.getOriginalFilename());
    Video video = videoService.findById(videoId);

    // Target path: /videos/userId/videoId/videoId.mp4
    Path rawVideoDirectoryPath = Paths.get(sourceVideoDirectory, Integer.toString(video.getAuthorId()), Integer.toString(videoId)); // /videos/userId/videoId

    // create directory if don't exist
    if (!Files.exists(rawVideoDirectoryPath)) {
      Files.createDirectories(rawVideoDirectoryPath);
    }

    // directory if don't have write access
    if (!Files.isWritable(rawVideoDirectoryPath)) {
      throw new IOException("Directory is not writable: " + rawVideoDirectoryPath);
    }

    Path videoFilePath = rawVideoDirectoryPath.resolve(videoId + extention); // /videos/userId/videoId/videoId.mp4
    Files.copy(videoFile.getInputStream(), videoFilePath, StandardCopyOption.REPLACE_EXISTING); // overwrite the file if already exists

    System.out.println("videoFilePath: " + videoFilePath);
    video.setFilePath(String.valueOf(videoFilePath));
    videoService.saveOrUpdate(video); // store in SQL database
  }

  public String getFileExtention(String fileName) {
    int dotIndex = fileName.lastIndexOf(".");

    return (dotIndex > 0 && dotIndex < fileName.length() - 1) ? fileName.substring(dotIndex) : "";
  }
}
