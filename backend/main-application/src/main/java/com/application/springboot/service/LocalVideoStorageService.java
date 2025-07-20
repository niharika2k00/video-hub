package com.application.springboot.service;

import com.application.sharedlibrary.entity.Video;
import com.application.sharedlibrary.enums.StorageType;
import com.application.sharedlibrary.service.VideoService;
import com.application.springboot.dto.VideoUploadRequestDto;
import com.application.springboot.utility.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Primary // setting this as default
@Service("LocalVideoStorageService")
public class LocalVideoStorageService implements StorageService<VideoUploadRequestDto> {

  @Value("${custom.path.project-root-dir}")
  String projectRootPath;

  @Value("${custom.storage-type}")
  String storageType;

  private final FileUtils fileUtils;
  private final VideoService videoService;

  @Autowired
  public LocalVideoStorageService(FileUtils fileUtils, VideoService videoService) {
    this.fileUtils = fileUtils;
    this.videoService = videoService;
  }

  @Override
  public void storeMediaFile(int videoId, VideoUploadRequestDto reqBodyParams) throws Exception {
    MultipartFile videoFile = reqBodyParams.getVideoFile();
    String extention = fileUtils.getFileExtention(videoFile.getOriginalFilename());
    Video video = videoService.findById(videoId);

    // Target path: .../videos/userId/videoId/videoId.mp4
    Path absoluteVideoDirectoryPath = Paths.get(projectRootPath, "videos", Integer.toString(video.getAuthorId()), Integer.toString(videoId)); // /videos/userId/videoId
    System.out.println("absoluteVideoDirectoryPath: " + absoluteVideoDirectoryPath);

    // create directory if don't exist
    if (!Files.exists(absoluteVideoDirectoryPath)) {
      Files.createDirectories(absoluteVideoDirectoryPath);
    }

    // directory if don't have write access
    if (!Files.isWritable(absoluteVideoDirectoryPath)) {
      throw new IOException("Directory is not writable: " + absoluteVideoDirectoryPath);
    }

    // Here mentioning the extension part in Files.copy will help to rename the file otherwise it will automatically save with the original name
    Path fullVideoFilePath = absoluteVideoDirectoryPath.resolve(videoId + extention); // .../videos/userId/videoId/videoId.mp4
    Files.copy(videoFile.getInputStream(), fullVideoFilePath, StandardCopyOption.REPLACE_EXISTING); // overwrite the file if already exists

    System.out.println("Video uploaded: " + fullVideoFilePath);
    video.setVideoDirectoryPath("/videos/" + video.getAuthorId() + "/" + videoId); // /videos/userId/videoId
    video.setStorageType(StorageType.valueOf(storageType));
    videoService.saveOrUpdate(video); // store in SQL database
  }
}
